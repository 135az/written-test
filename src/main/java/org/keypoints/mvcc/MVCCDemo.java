package org.keypoints.mvcc;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 简化的 MVCC 仿真
 * - 支持 REPEATABLE_READ 和 READ_COMMITTED
 * - 支持 begin/commit/rollback, read/write
 */
public class MVCCDemo {

    enum IsolationLevel {REPEATABLE_READ, READ_COMMITTED}

    enum TxState {ACTIVE, COMMITTED, ABORTED}

    // 每个 key 的版本
    static class Version {
        final long trxId;     // 创建该版本的事务 id
        final String value;

        Version(long trxId, String value) {
            this.trxId = trxId;
            this.value = value;
        }

        @Override
        public String toString() {
            return "V(tx=" + trxId + ", val=" + value + ")";
        }
    }

    // Read View（snapshot）
    static class ReadView {
        final long upLimit;
        final long lowLimit;
        final Set<Long> activeTxIds; // snapshot of active tx ids

        ReadView(long upLimit, long lowLimit, Set<Long> activeTxIds) {
            this.upLimit = upLimit;
            this.lowLimit = lowLimit;
            this.activeTxIds = activeTxIds;
        }

        @Override
        public String toString() {
            return "RV[up=" + upLimit + ", low=" + lowLimit + ", active=" + activeTxIds + "]";
        }
    }

    // 全局 MVCC 管理器
    static class MVCCManager {
        private final AtomicLong nextTrxId = new AtomicLong(1);
        private final ConcurrentSkipListSet<Long> activeTxs = new ConcurrentSkipListSet<>();

        long nextTrxId() {
            return nextTrxId.getAndIncrement();
        }

        void addActive(long id) {
            activeTxs.add(id);
        }

        void removeActive(long id) {
            activeTxs.remove(id);
        }

        ReadView createReadView() {
            long up = nextTrxId.get(); // transactions with id >= up started after this view
            long low = activeTxs.isEmpty() ? up : activeTxs.first(); // minimum active id
            // snapshot active tx ids
            Set<Long> act = new HashSet<>(activeTxs);
            return new ReadView(up, low, act);
        }
    }

    // 简单的 MVCC 存储（key -> deque(versions)）
    static class MVCCStore {
        private final ConcurrentHashMap<String, Deque<Version>> store = new ConcurrentHashMap<>();
        private final MVCCManager manager;

        MVCCStore(MVCCManager manager) {
            this.manager = manager;
        }

        // read visible version for transaction using its read view
        Version readVisible(String key, Transaction tx) {
            ReadView rv = tx.getReadViewForStatement();
            Deque<Version> versions = store.get(key);
            if (versions == null) {
                return null;
            }
            synchronized (versions) {
                for (Version v : versions) {
                    if (isVisible(v, tx.trxId, rv, tx)) {
                        return v;
                    }
                }
            }
            return null;
        }

        // append new version (visible to its trx immediately)
        Version write(String key, String value, Transaction tx) {
            Version v = new Version(tx.trxId, value);
            Deque<Version> versions = store.computeIfAbsent(key, k -> new LinkedList<>());
            synchronized (versions) {
                versions.addFirst(v);
            }
            tx.recordVersion(key, v); // <-- 需要 Transaction.recordVersion 实现
            return v;
        }

        // remove versions created by a rolled-back transaction
        void removeVersion(String key, Version v) {
            Deque<Version> versions = store.get(key);
            if (versions == null) {
                return;
            }
            synchronized (versions) {
                versions.remove(v);
            }
        }

        // visibility check according to simplified rules described above
        boolean isVisible(Version v, long readTxId, ReadView rv, Transaction tx) {
            // same transaction always sees its own versions
            if (v.trxId == readTxId) {
                return true;
            }
            // If read view is null (shouldn't happen), create on the fly (safe fallback)
            if (rv == null) {
                rv = manager.createReadView();
            }

            if (v.trxId >= rv.upLimit) {
                // created after this view was taken -> not visible
                return false;
            }
            if (v.trxId < rv.lowLimit) {
                // created and committed before the view -> visible
                return true;
            }
            // otherwise v.trxId is between lowLimit and upLimit
            if (rv.activeTxIds.contains(v.trxId)) {
                // was active when the view was taken -> not visible
                return false;
            }
            // else visible (committed by the time the view was created)
            return true;
        }
    }

    // 事务对象
    static class Transaction {
        final long trxId;
        final IsolationLevel isolation;
        final MVCCManager manager;
        final MVCCStore store;
        volatile TxState state = TxState.ACTIVE;
        private ReadView repeatableReadView = null;

        // keep track of versions this tx created (for rollback)
        private final List<VersionRecord> created = new ArrayList<>();

        static class VersionRecord {
            final String key;
            final Version v;

            VersionRecord(String key, Version v) {
                this.key = key;
                this.v = v;
            }
        }

        Transaction(long id, IsolationLevel iso, MVCCManager manager, MVCCStore store) {
            this.trxId = id;
            this.isolation = iso;
            this.manager = manager;
            this.store = store;
            manager.addActive(id);
            if (iso == IsolationLevel.REPEATABLE_READ) {
                // snapshot at transaction start
                this.repeatableReadView = manager.createReadView();
            }
        }

        // 新增的方法：记录事务创建的版本（供 MVCCStore.write 调用）
        public void recordVersion(String key, Version v) {
            created.add(new VersionRecord(key, v));
        }

        // get read view for a statement: depends on isolation level
        ReadView getReadViewForStatement() {
            if (isolation == IsolationLevel.REPEATABLE_READ) {
                return repeatableReadView;
            }
            // READ_COMMITTED -> fresh read view per statement
            return manager.createReadView();
        }

        // read operation
        String read(String key) {
            if (state != TxState.ACTIVE) {
                throw new IllegalStateException("Tx not active");
            }
            Version v = store.readVisible(key, this);
            return v == null ? null : v.value;
        }

        // write operation (creates a new version visible to self)
        void write(String key, String value) {
            if (state != TxState.ACTIVE) {
                throw new IllegalStateException("Tx not active");
            }
            Version v = store.write(key, value, this);
            // created list already recorded by recordVersion
        }

        // commit: mark committed and remove from active list
        void commit() {
            if (state != TxState.ACTIVE) {
                throw new IllegalStateException("Tx not active");
            }
            state = TxState.COMMITTED;
            manager.removeActive(trxId);
        }

        // rollback: remove created versions and mark aborted
        void rollback() {
            if (state != TxState.ACTIVE) {
                throw new IllegalStateException("Tx not active");
            }
            // remove created versions
            for (VersionRecord r : created) {
                store.removeVersion(r.key, r.v);
            }
            created.clear();
            state = TxState.ABORTED;
            manager.removeActive(trxId);
        }
    }

    // simple manager to start transactions
    static class TxFactory {
        private final MVCCManager mgr;
        private final MVCCStore store;

        TxFactory(MVCCManager mgr, MVCCStore store) {
            this.mgr = mgr;
            this.store = store;
        }

        Transaction begin(IsolationLevel iso) {
            long id = mgr.nextTrxId();
            return new Transaction(id, iso, mgr, store);
        }
    }

    // ========== Demo scenarios ==========
    public static void main(String[] args) throws Exception {
        MVCCManager manager = new MVCCManager();
        MVCCStore store = new MVCCStore(manager);
        TxFactory factory = new TxFactory(manager, store);

        System.out.println("=== Scenario A: REPEATABLE_READ (事务开始时 snapshot 固定) ===");
        repeatableReadScenario(factory);

        System.out.println("\n=== Scenario B: READ_COMMITTED (每个语句 fresh snapshot) ===");
        readCommittedScenario(factory);

        System.out.println("\n=== Scenario C: rollback undo demonstration ===");
        rollbackScenario(factory);
    }

    // RR: tx1 starts, tx2 writes+commit, tx1 should NOT see tx2's commit if tx1 read-view was created before commit
    static void repeatableReadScenario(TxFactory factory) {
        Transaction tx1 = factory.begin(IsolationLevel.REPEATABLE_READ);
        System.out.println("tx1 started (RR), id=" + tx1.trxId);
        String v1 = tx1.read("x");
        System.out.println("tx1 read x -> " + v1);

        Transaction tx2 = factory.begin(IsolationLevel.REPEATABLE_READ);
        System.out.println("tx2 started, id=" + tx2.trxId);
        tx2.write("x", "v2-by-tx2");
        System.out.println("tx2 wrote x=v2-by-tx2");
        tx2.commit();
        System.out.println("tx2 committed");

        String v2Again = tx1.read("x");
        System.out.println("tx1 read x again -> " + v2Again + "  (still null under RR snapshot)");
        tx1.commit();
    }

    // RC: tx1 starts (but snapshot per statement), tx2 writes+commit, tx1 second read should see the committed value
    static void readCommittedScenario(TxFactory factory) {
        Transaction tx1 = factory.begin(IsolationLevel.READ_COMMITTED);
        System.out.println("tx1 started (RC), id=" + tx1.trxId);
        String v1 = tx1.read("y");
        System.out.println("tx1 read y -> " + v1);

        Transaction tx2 = factory.begin(IsolationLevel.READ_COMMITTED);
        System.out.println("tx2 started, id=" + tx2.trxId);
        tx2.write("y", "v2-by-tx2");
        System.out.println("tx2 wrote y=v2-by-tx2");
        tx2.commit();
        System.out.println("tx2 committed");

        String v2After = tx1.read("y");
        System.out.println("tx1 read y again -> " + v2After + "  (RC sees new commit)");
        tx1.commit();
    }

    static void rollbackScenario(TxFactory factory) {
        Transaction tx = factory.begin(IsolationLevel.REPEATABLE_READ);
        System.out.println("tx started, id=" + tx.trxId);
        tx.write("z", "will-be-rolled-back");
        System.out.println("tx wrote z=will-be-rolled-back");
        // another transaction to show it's not visible
        Transaction other = factory.begin(IsolationLevel.READ_COMMITTED);
        System.out.println("other read z -> " + other.read("z"));
        tx.rollback();
        System.out.println("tx rolled back");
        System.out.println("other read z after rollback -> " + other.read("z"));
        other.commit();
    }
}
