package org.exam.shopee.num1;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/16 16:33
 * @Description
 *
 */
public class Main {
    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        int i = left, j = right;
        int privot = arr[left];
        while (i < j) {
            while (i < j && arr[j] >= privot) {
                j--;
            }
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }
            while (i < j && arr[i] <= privot) {
                i++;
            }
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }
        }
        arr[i] = privot;
        quickSort(arr, left, i - 1);
        quickSort(arr, i + 1, right);
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int[] arr = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            arr[i] = s.charAt(i) - '0';
        }
        quickSort(arr, 0, arr.length - 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
        }
        System.out.println(sb);
    }
}
