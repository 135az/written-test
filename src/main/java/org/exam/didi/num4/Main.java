package org.exam.didi.num4;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author yjz
 * @Date 2025/9/27 16:29
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner(System.in);
        int T = fs.nextInt();
        StringBuilder sb = new StringBuilder();
        while (T-- > 0) {
            int n = fs.nextInt();
            int[] cnt = new int[n + 1];
            for (int i = 2; i <= n; i++) {
                int p = fs.nextInt();
                cnt[p]++;
            }
            ArrayList<Integer> a = new ArrayList<>();
            for (int i = 1; i <= n; i++) {
                if (cnt[i] > 0) {
                    a.add(cnt[i]);
                }
            }
            a.add(1);
            Collections.sort(a, Collections.reverseOrder());
            int m = a.size();
            ArrayList<Integer> b = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                int val = a.get(i) - (m - i);
                if (val > 0) {
                    b.add(val);
                }
            }
            if (b.isEmpty()) {
                sb.append(m).append("\n");
                continue;
            }

            long lo = 0, hi = (long) 1e9;
            while (lo < hi) {
                long mid = (lo + hi) >>> 1;
                long need = 0;
                for (int val : b) {
                    if (val > mid) {
                        need += (val - mid);
                    }
                    if (need > mid) {
                        break;
                    }
                }
                if (need <= mid) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            sb.append(m + lo).append("\n");
        }
        System.out.println(sb);
    }

    static class FastScanner {
        BufferedInputStream in;
        byte[] buffer = new byte[1 << 16];
        int ptr = 0, len = 0;

        FastScanner(InputStream input) {
            in = new BufferedInputStream(input);
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

        private int nextInt() throws IOException {
            int c, s = 1, x = 0;
            do {
                c = read();
            } while (c <= 32 && c != -1);
            if (c == '-') {
                s = -1;
                c = read();
            }
            while (c > 32) {
                x = x * 10 + c - '0';
                c = read();
            }
            return x * s;
        }
    }
}
