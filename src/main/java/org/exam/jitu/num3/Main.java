package org.exam.jitu.num3;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/26 14:53
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int q = sc.nextInt();
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        for (int i = 0; i < q; i++) {
            int l = sc.nextInt() - 1;
            int r = sc.nextInt() - 1;

            int[] subArray = new int[r - l + 1];
            System.arraycopy(arr, l, subArray, 0, subArray.length);
            Arrays.sort(subArray);
            System.arraycopy(subArray, 0, arr, l, subArray.length);
        }

        for (int i = 0; i < n; i++) {
            System.out.print(arr[i]);
            if (i < n - 1) {
                System.out.print(" ");
            }
        }
    }
}
