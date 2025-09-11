package org.example.haixin.num1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author yjz
 * @Date 2025/9/11 14:20
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        String[] firstLine = br.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        int m = Integer.parseInt(firstLine[1]);

        String[] aLine = br.readLine().split(" ");
        long[] a = new long[n + 1];
        long xorSum = 0;
        for (int i = 1; i <= n; i++) {
            a[i] = Long.parseLong(aLine[i - 1]);
            xorSum ^= a[i];
        }

        pw.println(xorSum != 0 ? "win" : "lose");

        for (int i = 0; i < m; i++) {
            String[] modifyLine = br.readLine().split(" ");
            int p = Integer.parseInt(modifyLine[0]);
            long k = Long.parseLong(modifyLine[1]);

            xorSum ^= a[p];
            a[p] += k;
            xorSum ^= a[p];
            pw.println(xorSum != 0 ? "win" : "lose");
        }
        pw.flush();
        br.close();
    }
}
