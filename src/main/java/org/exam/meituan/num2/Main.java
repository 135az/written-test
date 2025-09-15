package org.exam.meituan.num2;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/13 19:17
 * @Description
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        StringBuilder out = new StringBuilder();

        if (!sc.hasNext()) {
            sc.close();
            System.out.print(out);
            return;
        }
        int T = sc.nextInt();
        for (int tc = 0; tc < T; tc++) {
            if (!sc.hasNext()) {
                break;
            }
            int n = sc.nextInt();

            long[] a = new long[n];
            long[] b = new long[n];
            for (int i = 0; i < n; i++) {
                a[i] = sc.nextLong();
            }
            for (int i = 0; i < n; i++) {
                b[i] = sc.nextLong();
            }
            Arrays.sort(a);
            Arrays.sort(b);

            long[] aAns = new long[n];
            long[] bAns = new long[n];
            int lA = 0, rA = n - 1;
            int lB = 0, rB = n - 1;
            for (int i = 0; i < n; i++) {
                if ((i & 1) == 0) {
                    aAns[i] = a[rA--];
                    bAns[i] = b[lB++];
                } else {
                    aAns[i] = a[lA++];
                    bAns[i] = b[rB--];
                }
            }
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    out.append(" ");
                }
                out.append(aAns[i]);
            }
            out.append("\n");
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    out.append(" ");
                }
                out.append(bAns[i]);
            }
            out.append("\n");
        }
        System.out.print(out);
        sc.close();
    }
}
