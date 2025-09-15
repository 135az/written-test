package org.exam.zhongan.num2;

import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/10 19:38
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            int y = sc.nextInt();
            int m = sc.nextInt();
            int d = sc.nextInt();

            int a = sc.nextInt();
            int b = sc.nextInt();
            int c = sc.nextInt();
            int aPrime = sc.nextInt();
            int bPrime = sc.nextInt();
            int cPrime = sc.nextInt();

            int startDate = dateToDays(a, b, c);
            int endDate = dateToDays(aPrime, bPrime, cPrime);

            int count = 0;
            int startYear = Math.max(y + 1, a);
            int endYear = aPrime;

            for (int year = startYear; year <= endYear; year++) {
                int brithMonth = m;
                int brithDay = d;
                if (m == 2 && d == 29) {
                    if (isLeapYear(year)) {
                        brithMonth = 2;
                        brithDay = 29;
                    } else {
                        brithMonth = 2;
                        brithDay = 28;
                    }
                }

                int brithday = dateToDays(year, brithMonth, brithDay);

                if (brithday >= startDate && brithday <= endDate) {
                    count++;
                }
            }

            int brithDate = dateToDays(y, m, d);
            if (brithDate >= dateToDays(a, b, c) && brithDate <= dateToDays(aPrime, bPrime, cPrime)) {
                count++;
            }
            System.out.println(count);
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static int dateToDays(int year, int month, int day) {
        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (isLeapYear(year)) {
            monthDays[1] = 29;
        }
        int totalDays = 0;
        for (int y = 1; y < year; y++) {
            totalDays += isLeapYear(y) ? 366 : 365;
        }
        for (int m = 0; m < month - 1; m++) {
            totalDays += monthDays[m];
        }
        totalDays += day;
        return totalDays;
    }
}
