package org.exam.youkushengshi.num1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author yjz
 * @Date 2025/9/10 14:58
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < T; i++) {
            long x = Long.parseLong(br.readLine().trim());
            int steps = 0;

            while (x != 0) {
                steps++;
                int bitCount = Long.bitCount(x);
                if (bitCount % 2 == 1) {
                    x ^= 1;
                } else {
                    int leadingZeros = Long.numberOfLeadingZeros(x);
                    int msbPos = 63 - leadingZeros;
                    long mask = 1L << msbPos;
                    x ^= mask;
                }
            }
            sb.append(steps).append("\n");
        }
        System.out.print(sb);
    }
}
