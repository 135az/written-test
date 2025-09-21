package org.exam.xiaohongshu.num2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author yjz
 * @Date 2025/9/21 19:29
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        while (T-- > 0) {
            String[] nk = br.readLine().split(" ");
            int n = Integer.parseInt(nk[0]);
            int k = Integer.parseInt(nk[1]);
            int[] a = new int[n];
            String[] arr = br.readLine().split(" ");
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(arr[i]);
            }

            long[][] f = new long[n][n + 1];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= n; j++) {
                    f[i][j] = Long.MAX_VALUE / 2;
                }
            }
            for (int i = 0; i < n; i++) {
                f[i][1] = a[i];
            }
            for (int m = 2; m <= n; m++) {
                long minPrev = Long.MAX_VALUE / 2;
                for (int i = 0; i < n; i++) {
                    if (minPrev < Long.MAX_VALUE / 2) {
                        f[i][m] = minPrev + a[i];
                    }
                    if (f[i][m - 1] < minPrev) {
                        minPrev = f[i][m - 1];
                    }
                }
            }
            long[] res = new long[n];
            for (int m = 1; m <= n; m++) {
                long best = Long.MAX_VALUE;
                for (int i = m - 1; i < n; i++) {
                    long cost = (i - m + 1) * (long) k + f[i][m];
                    if (cost < best) {
                        best = cost;
                    }
                }
                res[m - 1] = best;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.append(res[i]);
                if (i < n - 1) {
                    sb.append(" ");
                }
            }
            System.out.println(sb);
        }
    }
}
