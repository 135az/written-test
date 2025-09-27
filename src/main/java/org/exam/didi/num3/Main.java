package org.exam.didi.num3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * @author yjz
 * @Date 2025/9/27 16:12
 * @Description
 *
 */
public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int l = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int rangeLen = r - l + 1;
        if (rangeLen <= 0) {
            return;
        }

        int[] counts = new int[rangeLen];
        for (int base = a; base <= b; base++) {
            for (int d = 1; d < base; d++) {
                if (d < l) {
                    continue;
                }
                if (d > r) {
                    break;
                }
                counts[d - l]++;
            }

            for (int x = 1; x < base; x++) {
                for (int y = 0; y < base; y++) {
                    if (x == y) {
                        continue;
                    }
                    long val = (long) x * base + y;
                    if (val > r) {
                        continue;
                    }
                    boolean nextIsX = true;
                    long curr = val;
                    while (curr <= r) {
                        if (curr >= l) {
                            counts[(int) (curr - l)]++;
                        }

                        int nextDigit = nextIsX ? x : y;
                        curr = curr * base + nextDigit;
                        nextIsX = !nextIsX;
                    }
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rangeLen; i++) {
            if (counts[i] >= k) {
                sb.append(i + l).append("\n");
            }
        }
        System.out.println(sb);
    }
}
