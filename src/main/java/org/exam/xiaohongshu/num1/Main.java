package org.exam.xiaohongshu.num1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/21 19:11
 * @Description
 *
 */
public class Main {
    static class Slot {
        int index;
        int pos;
        long weight;

        Slot(int index, int pos, long weight) {
            this.index = index;
            this.pos = pos;
            this.weight = weight;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String[] arr = new String[n];
        for (int i = 0; i < n; i++) {
            arr[i] = sc.next();
        }
        List<Integer> digits = new ArrayList<>();
        int[] lens = new int[n];
        for (int i = 0; i < n; i++) {
            String s = arr[i];
            lens[i] = s.length();
            for (char c : s.toCharArray()) {
                digits.add(c - '0');
            }
        }

        digits.sort((a, b) -> b - a);
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int len = lens[i];
            for (int pos = 0; pos < len; pos++) {
                long weight = (long) Math.pow(10, len - pos - 1);
                slots.add(new Slot(i, pos, weight));
            }
        }

        slots.sort((a, b) -> Long.compare(b.weight, a.weight));

        int[] res = new int[n];
        for (int i = 0; i < slots.size(); i++) {
            Slot slot = slots.get(i);
            res[slot.index] += digits.get(i) * (int) slot.weight;
        }

        long sum = 0;
        for (int x : res) {
            sum += x;
        }
        System.out.println(sum);
    }
}
