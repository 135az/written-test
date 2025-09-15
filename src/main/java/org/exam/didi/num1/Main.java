package org.exam.didi.num1;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author yjz
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int[] b = new int[n];
        int minSum = 0, maxSum = 0;
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            b[i] = sc.nextInt();
            if (a[i] > 0) {
                maxSum += a[i];
            } else {
                minSum += a[i];
            }
        }

        int offset = -minSum;
        int target = offset + 1;
        int range = maxSum - minSum + 1;
        int[] dp = new int[range];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[offset] = 0;
        for (int i = 0; i < n; i++) {
            for (int j = range - 1; j >= 0; j--) {
                if (dp[j] != Integer.MAX_VALUE) {
                    int next = j + a[i];
                    if (next >= 0 && next < range) {
                        dp[next] = Math.min(dp[next], dp[j] + b[i]);
                    }
                }
            }
        }
        if (target >= 0 && target < range && dp[target] != Integer.MAX_VALUE) {
            System.out.println(dp[target]);
        } else {
            System.out.println(-1);
        }
        sc.close();
    }
}
