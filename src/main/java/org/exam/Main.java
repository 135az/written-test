package org.exam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static class Point {
        double x, y;
        int cluster; // 所属簇编号

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int k = sc.nextInt();

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            points.add(new Point(sc.nextDouble(), sc.nextDouble()));
        }

        // KMeans 聚类
        List<Point> centers = kMeans(points, k);

        // 计算簇的轮廓系数
        double[] clusterScores = new double[k];
        int[] clusterCounts = new int[k];
        for (Point p : points) {
            double s = silhouette(p, points, centers, k);
            clusterScores[p.cluster] += s;
            clusterCounts[p.cluster]++;
        }

        double minScore = Double.MAX_VALUE;
        int worstCluster = -1;
        for (int i = 0; i < k; i++) {
            if (clusterCounts[i] > 0) {
                double avg = clusterScores[i] / clusterCounts[i];
                if (avg < minScore) {
                    minScore = avg;
                    worstCluster = i;
                }
            }
        }

        // 输出最差簇的中心
        Point c = centers.get(worstCluster);
        System.out.println(format(c.x) + "," + format(c.y));
    }

    // ----------------- KMeans 实现 -----------------
    private static List<Point> kMeans(List<Point> points, int k) {
        List<Point> centers = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            Point p = points.get(i);
            centers.add(new Point(p.x, p.y));
        }

        for (int iter = 0; iter < 100; iter++) {
            // 分配簇
            for (Point p : points) {
                int cluster = 0;
                double bestDist = dist(p, centers.get(0));
                for (int j = 1; j < k; j++) {
                    double d = dist(p, centers.get(j));
                    if (d < bestDist) {
                        bestDist = d;
                        cluster = j;
                    }
                }
                p.cluster = cluster;
            }

            // 更新中心
            double maxShift = 0;
            for (int j = 0; j < k; j++) {
                double sumX = 0, sumY = 0;
                int count = 0;
                for (Point p : points) {
                    if (p.cluster == j) {
                        sumX += p.x;
                        sumY += p.y;
                        count++;
                    }
                }
                if (count > 0) {
                    Point old = centers.get(j);
                    double newX = sumX / count;
                    double newY = sumY / count;
                    double shift = Math.sqrt((old.x - newX) * (old.x - newX) + (old.y - newY) * (old.y - newY));
                    maxShift = Math.max(maxShift, shift);
                    old.x = newX;
                    old.y = newY;
                }
            }

            if (maxShift <= 1e-6) {
                break;
            }
        }
        return centers;
    }

    // ----------------- 轮廓系数 -----------------
    private static double silhouette(Point p, List<Point> points, List<Point> centers, int k) {
        int cluster = p.cluster;

        // a(i): 平均簇内距离
        double a = 0;
        int cntA = 0;
        for (Point q : points) {
            if (q != p && q.cluster == cluster) {
                a += dist(p, q);
                cntA++;
            }
        }
        if (cntA > 0) {
            a /= cntA;
        }

        // b(i): 最近其他簇的平均距离
        double b = Double.MAX_VALUE;
        for (int j = 0; j < k; j++) {
            if (j == cluster) {
                continue;
            }
            double avg = 0;
            int cnt = 0;
            for (Point q : points) {
                if (q.cluster == j) {
                    avg += dist(p, q);
                    cnt++;
                }
            }
            if (cnt > 0) {
                avg /= cnt;
                b = Math.min(b, avg);
            }
        }

        if (a == 0 && b == Double.MAX_VALUE) {
            return 0;
        }
        return (b - a) / Math.max(a, b);
    }

    private static double dist(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    // 保留两位小数，四舍五入 (HALF_EVEN)
    private static String format(double val) {
        return new BigDecimal(val).setScale(2, RoundingMode.HALF_EVEN).toPlainString();
    }
}
