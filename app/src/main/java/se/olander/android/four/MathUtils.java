package se.olander.android.four;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.Nullable;

public abstract class MathUtils {

    /**
     * Tells whether the two line segments cross.
     * From https://android.googlesource.com/platform/frameworks/native/+/e09fd9e/awt/java/awt/geom/Line2D.java
     *
     * @param x1 the x coordinate of the starting point of the first segment.
     * @param y1 the y coordinate of the starting point of the first segment.
     * @param x2 the x coordinate of the end point of the first segment.
     * @param y2 the y coordinate of the end point of the first segment.
     * @param x3 the x coordinate of the starting point of the second segment.
     * @param y3 the y coordinate of the starting point of the second segment.
     * @param x4 the x coordinate of the end point of the second segment.
     * @param y4 the y coordinate of the end point of the second segment.
     * @return true, if the two line segments cross.
     */
    public static boolean linesIntersect(
        float x1,
        float y1,
        float x2,
        float y2,
        float x3,
        float y3,
        float x4,
        float y4
    ) {
        /*
         * A = (x2-x1, y2-y1) B = (x3-x1, y3-y1) C = (x4-x1, y4-y1) D = (x4-x3,
         * y4-y3) = C-B E = (x1-x3, y1-y3) = -B F = (x2-x3, y2-y3) = A-B Result
         * is ((AxB) (AxC) <=0) and ((DxE) (DxF) <= 0) DxE = (C-B)x(-B) =
         * BxB-CxB = BxC DxF = (C-B)x(A-B) = CxA-CxB-BxA+BxB = AxB+BxC-AxC
         */
        x2 -= x1; // A
        y2 -= y1;
        x3 -= x1; // B
        y3 -= y1;
        x4 -= x1; // C
        y4 -= y1;
        float AvB = x2 * y3 - x3 * y2;
        float AvC = x2 * y4 - x4 * y2;
        // Online
        if (AvB == 0.0 && AvC == 0.0) {
            if (x2 != 0.0) {
                return (x4 * x3 <= 0.0)
                    || ((x3 * x2 >= 0.0) && (x2 > 0.0 ? x3 <= x2 || x4 <= x2 : x3 >= x2
                    || x4 >= x2));
            }
            if (y2 != 0.0) {
                return (y4 * y3 <= 0.0)
                    || ((y3 * y2 >= 0.0) && (y2 > 0.0 ? y3 <= y2 || y4 <= y2 : y3 >= y2
                    || y4 >= y2));
            }
            return false;
        }
        float BvC = x3 * y4 - x4 * y3;
        return (AvB * AvC <= 0.0) && (BvC * (AvB + BvC - AvC) <= 0.0);
    }

    @Nullable
    public static PointF getIntersectionPoint(PointF p11, PointF p12, PointF p21, PointF p22) {
        return getIntersectionPoint(p11.x, p11.y, p12.x, p12.y, p21.x, p21.y, p22.x, p22.y);
    }

    @Nullable
    public static PointF getIntersectionPoint(
        float x1,
        float y1,
        float x2,
        float y2,
        float x3,
        float y3,
        float x4,
        float y4
    ) {
        if (!linesIntersect(x1, y1, x2, y2, x3, y3, x4, y4)) {
            return null;
        }

        float t = cross(x3 - x1, y3 - y1, x4 - x3, y4 - y3) / cross(x2 - x1, y2 - y1, x4 - x3, y4 - y3);
        return new PointF(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
    }

    public static int interpolateRGB(int from, int to, double t) {
        int fromAlpha = Color.alpha(from);
        int fromRed = Color.red(from);
        int fromGreen = Color.green(from);
        int fromBlue = Color.blue(from);

        int toAlpha = Color.alpha(to);
        int toRed = Color.red(to);
        int toGreen = Color.green(to);
        int toBlue = Color.blue(to);

        int alpha = (int) (fromAlpha + (toAlpha - fromAlpha) * t);
        int red = (int) (fromRed + (toRed - fromRed) * t);
        int green = (int) (fromGreen + (toGreen - fromGreen) * t);
        int blue = (int) (fromBlue + (toBlue - fromBlue) * t);

        return Color.argb(alpha, red, green, blue);
    }

    public static double linearToFullSin(double t) {
        return (Math.sin(2*Math.PI * t) + 1) / 2;
    }

    public static double linearToBellCos(double t) {
        return 1 - (Math.cos(2*Math.PI * t) + 1) / 2;
    }

    public static float cos(float rads) {
        return (float) Math.cos(rads);
    }

    public static float cross(float x1, float y1, float x2, float y2) {
        return x1 * y2 - x2 * y1;
    }

    public static <T> int countOccurrences(T[] array, T value) {
        int occurrences = 0;
        for (T t : array) {
            if (t == null ? value == null : t.equals(value)) {
                occurrences += 1;
            }
        }
        return occurrences;
    }
}
