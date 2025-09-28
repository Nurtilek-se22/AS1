package Closest;

import org.Util.MetricsTracker;

import java.util.Arrays;

/**
 * ClosestPair (2D points) with divide-and-conquer, O(n log n).
 * Sorts points by x, splits, and checks a strip with y-order (7–8 neighbors).
 */
public class ClosestPair {

    /**
     * Represents a 2D point.
     */
    public static class Point implements Comparable<Point> {
        public final double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point other) {
            return Double.compare(this.x, other.x);
        }
    }

    /**
     * Entry point.
     */
    public static double findClosestPair(Point[] points, MetricsTracker tracker) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        tracker.start();
        Point[] pointsByX = points.clone();
        tracker.incAllocation();
        Arrays.sort(pointsByX); // sort by x

        double result = recursive(pointsByX, 0, pointsByX.length - 1, tracker);
        tracker.stop();
        return result;
    }

    /**
     * Recursive helper.
     * T(n) = 2T(n/2) + O(n) → Θ(n log n).
     */
    private static double recursive(Point[] pts, int left, int right, MetricsTracker tracker) {
        tracker.enterRecursion();
        try {
            int n = right - left + 1;
            if (n <= 3) {
                return bruteForce(pts, left, right, tracker);
            }

            int mid = (left + right) / 2;
            double midX = pts[mid].x;

            double dLeft = recursive(pts, left, mid, tracker);
            double dRight = recursive(pts, mid + 1, right, tracker);
            double d = Math.min(dLeft, dRight);

            // Build strip
            Point[] strip = new Point[n];
            tracker.incAllocation();
            int stripSize = 0;
            for (int i = left; i <= right; i++) {
                if (Math.abs(pts[i].x - midX) < d) {
                    strip[stripSize++] = pts[i];
                }
            }

            Arrays.sort(strip, 0, stripSize, (a, b) -> Double.compare(a.y, b.y));

            // Scan neighbors (≤ 7–8)
            for (int i = 0; i < stripSize; i++) {
                for (int j = i + 1; j < stripSize && (strip[j].y - strip[i].y) < d && (j - i) < 8; j++) {
                    tracker.incComparison();
                    d = Math.min(d, distance(strip[i], strip[j]));
                }
            }

            return d;
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Brute force for small sets (≤ 3 points).
     */
    private static double bruteForce(Point[] pts, int low, int high, MetricsTracker tracker) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = low; i <= high; i++) {
            for (int j = i + 1; j <= high; j++) {
                tracker.incComparison();
                min = Math.min(min, distance(pts[i], pts[j]));
            }
        }
        return (min == Double.POSITIVE_INFINITY) ? 0.0 : min;
    }

    /**
     * Euclidean distance.
     */
    private static double distance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
