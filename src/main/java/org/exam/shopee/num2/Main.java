package org.exam.shopee.num2;

import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/16 16:41
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        sc.close();

        String[] parts = line.split(",");
        int a = Integer.parseInt(parts[0]);
        int b = Integer.parseInt(parts[1]);
        int c = Integer.parseInt(parts[2]);

        int sum = a + b + c;
        int max = Math.max(a, Math.max(b, c));

        int score = Math.min(sum / 2, sum - max);
        System.out.println(score);
    }
}
