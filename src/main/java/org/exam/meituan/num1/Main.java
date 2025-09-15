package org.exam.meituan.num1;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author yjz
 * @Date 2025/9/13 19:07
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
            while (c >= ' ') {
                val = val * 10 + c - '0';
                c = read();
            }
            return val * sign;
        }
    }

    public static void main(String[] args) throws IOException {
        FastScanner fs = new FastScanner(System.in);
        StringBuilder out = new StringBuilder();
        int T = fs.nextInt();
        for (int tc = 0; tc < T; tc++) {
            int n = fs.nextInt();
            if (n % 2 == 1) {
                out.append("-1\n");
            } else {
                for (int i = 1; i <= n; i += 2) {
                    out.append(i + 1).append(' ').append(i);
                    if (i + 2 <= n) {
                        out.append(' ');
                    }
                }
                out.append('\n');
            }
        }
        System.out.print(out);
    }
}
