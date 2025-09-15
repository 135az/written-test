package org.exam.meituan.num3;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author yjz
 * @Date 2025/9/13 19:37
 * @Description
 *
 */
public class Main {
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1024];
        private int ptr = 0, len = 0;

        public FastScanner(InputStream in) {
            this.in = in;
        }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) {
                    return -1;
                }
            }
            return buffer[ptr++];
        }

        public int nextInt() throws IOException {
            int c;
            while ((c = read()) <= ' ') {
                if (c == -1) {
                    return Integer.MIN_VALUE;
                }
            }
            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }
            int val = 0;
            while (c > ' ') {
                val = val * 10 + c - '0';
                c = read();
            }
            return val * sign;
        }
    }

    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner(System.in);
        int n = fs.nextInt();
        int q = fs.nextInt();
        ArrayList<Integer>[] g = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            g[i] = new ArrayList<>();
        }
        for (int i = 0; i < n - 1; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();
            g[u].add(v);
            g[v].add(u);
        }

        int LOG = 1;
        while ((1 << LOG) <= n) {
            LOG++;
        }
        int[][] up = new int[LOG][n + 1];
        int[] parent = new int[n + 1];
        int[] depth = new int[n + 1];
        int root = 1;
        int[] stack = new int[n];
        int top = 0;
        stack[top++] = root;
        parent[root] = 0;
        depth[root] = 0;
        ArrayList<Integer> order = new ArrayList<>(n);
        boolean[] vis = new boolean[n + 1];
        vis[root] = true;
        while (top > 0) {
            int node = stack[--top];
            order.add(node);
            for (int next : g[node]) {
                if (!vis[next]) {
                    vis[next] = true;
                    parent[next] = node;
                    depth[next] = depth[node] + 1;
                    stack[top++] = next;
                }
            }
        }
        for (int i = 1; i <= n; i++) {
            up[0][i] = parent[i];
        }
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                int mid = up[j - 1][i];
                up[j][i] = mid != 0 ? up[j - 1][mid] : 0;
            }
        }

        int finalLOG = LOG;
        class LCA {
            int lca(int a, int b) {
                if (a == 0 || b == 0) {
                    return a ^ b;
                }
                if (depth[a] < depth[b]) {
                    int temp = a;
                    a = b;
                    b = temp;
                }
                int diff = depth[a] - depth[b];
                for (int k = 0; k < finalLOG; k++) {
                    if ((diff & (1 << k)) != 0) {
                        a = up[k][a];
                    }
                }
                if (a == b) {
                    return a;
                }
                for (int k = finalLOG - 1; k >= 0; k--) {
                    if (up[k][a] != up[k][b]) {
                        a = up[k][a];
                        b = up[k][b];
                    }
                }
                return parent[a];
            }
        }
        LCA lca = new LCA();

        int[] diff = new int[n + 1];
        for (int i = 0; i < q; i++) {
            int u = fs.nextInt();
            int v = fs.nextInt();
            int w = lca.lca(u, v);
            diff[u] += 1;
            diff[v] += 1;
            diff[w] -= 1;
            if (parent[w] != 0) {
                diff[parent[w]] -= 1;
            }
        }

        for (int i = order.size() - 1; i >= 0; i--) {
            int node = order.get(i);
            if (parent[node] != 0) {
                diff[parent[node]] += diff[node];
            }
        }

        int redNodes = 0, redEdges = 0;
        for (int i = 1; i <= n; i++) {
            if (diff[i] > 0) {
                redNodes++;
            }
        }
        for (int i = 1; i < n; i++) {
            int p = parent[i];
            if (p != 0 && diff[p] > 0 && diff[i] > 0) {
                redEdges++;
            }
        }

        int ans = redNodes - redEdges;
        System.out.println(ans);
    }
}
