package org.exam.haixin.num2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * @author yjz
 * @Date 2025/9/11 14:27
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));

        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            String[] firstLine = br.readLine().split(" ");
            long n = Long.parseLong(firstLine[0]);
            long m = Long.parseLong(firstLine[1]);
            long x = Long.parseLong(firstLine[2]);
            long y = Long.parseLong(firstLine[3]);

            String[] aLine = br.readLine().split(" ");
            long[] a = new long[(int) m];
            for (int i = 0; i < m; i++) {
                a[i] = Long.parseLong(aLine[i]);
            }

            long sold = 0;
            long totalRevenue = 0;

            for (int i = 0; i < m && sold < n; i++) {
                long price = a[i];
                if (price >= y) {
                    totalRevenue += price;
                    sold++;
                }
            }
            long totalCost = n * x;
            long profit = totalRevenue - totalCost;

            pw.println(profit);
        }
        pw.flush();
        br.close();
    }
}
