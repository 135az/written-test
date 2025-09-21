package org.exam.xiaohongshu.num3;

import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/21 19:57
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String s = sc.next();
        char[] chars = s.toCharArray();
        String minString = s;

        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                for (int k = 1; k <= n; k++) {
                    if (i - k >= 1 && j + k <= n) {
                        char[] temp = s.toCharArray();
                        char tmp = temp[i - 1];
                        temp[i - 1] = temp[i - k - 1];
                        temp[i - k - 1] = tmp;
                        tmp = temp[j - 1];
                        temp[j - 1] = temp[j + k - 1];
                        temp[j + k - 1] = tmp;

                        String newString = new String(temp);
                        if (newString.compareTo(minString) < 0) {
                            minString = newString;
                        }
                    }
                }
            }
        }
        System.out.println(minString);
        sc.close();
    }
}
