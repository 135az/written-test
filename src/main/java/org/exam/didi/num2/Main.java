package org.exam.didi.num2;

import java.util.Scanner;
import java.util.TreeMap;


public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        while (t-- > 0) {
            int n = sc.nextInt();
            long m = sc.nextLong();
            TreeMap<Long, Integer> counts = new TreeMap<>();
            for (int i = 0; i < n; i++) {
                long x = sc.nextLong();
                counts.put(x, counts.getOrDefault(x, 0) + 1);
            }
            while (m > 0 && counts.size() > 1) {
                long minVal = counts.firstKey();
                long maxVal = counts.lastKey();
                if (maxVal - minVal <= 1) {
                    break;
                }
                int minCount = counts.get(minVal);
                int maxCount = counts.get(maxVal);
                long moves = Math.min(m, Math.min(minCount, maxCount));
                if (moves == 0) {
                    moves = 1;
                }
                updateCount(counts, maxVal, -moves);
                updateCount(counts, maxVal - 1, moves);
                updateCount(counts, minVal, -moves);
                updateCount(counts, minVal + 1, moves);

                m -= moves;
            }
            System.out.println(counts.lastKey() - counts.firstKey());
        }
        sc.close();
    }

    private static void updateCount(TreeMap<Long, Integer> counts, long key, long detal) {
        int newCount = counts.getOrDefault(key, 0) + (int) detal;
        if (newCount > 0) {
            counts.put(key, newCount);
        } else {
            counts.remove(key);
        }
    }

}
