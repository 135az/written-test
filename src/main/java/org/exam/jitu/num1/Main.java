package org.exam.jitu.num1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author yjz
 * @Date 2025/9/26 14:25
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < T; i++) {
            String[] parts = br.readLine().trim().split(" ");
            long a = Long.parseLong(parts[0]);
            long b = Long.parseLong(parts[1]);
            long c = Long.parseLong(parts[2]);
            result.append(solve(a, b, c)).append("\n");
        }
        System.out.print(result);
    }

    private static long solve(long a, long b, long c) {
        if (b > a && a > c) {
            return 0;
        }
        long x = Math.max(0, c - a + 1);
        long newA = a + x;
        long y = Math.max(0, newA - b + 1);
        return x + y;
    }
}
