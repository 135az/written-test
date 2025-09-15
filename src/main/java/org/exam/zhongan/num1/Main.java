package org.exam.zhongan.num1;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/10 19:32
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] scores = new int[n];
        for (int i = 0; i < n; i++) {
            scores[i] = sc.nextInt();
        }
        Arrays.sort(scores);
        int groups = 0;
        int i = 0;
        while (i < n) {
            if (i + 2 < n && scores[i + 2] - scores[1] <= 10) {
                groups++;
                i += 3;
            } else if (i + 1 < n && scores[i + 1] - scores[i] <= 20) {
                groups++;
                i += 2;
            } else {
                groups++;
                i++;
            }
        }
        System.out.println(groups);
    }
}
