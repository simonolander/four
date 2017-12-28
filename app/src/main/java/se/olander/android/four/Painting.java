package se.olander.android.four;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Painting {

    private static final double TAU = 2 * Math.PI;

    final ArrayList<List<Integer>> neighboursList = new ArrayList<>();
    final Map<PaintRegion, Colour> colors = new HashMap<>();
    final ArrayList<PaintRegion> regions = new ArrayList<>();

    public Painting() {

    }

    public static Painting somePainting() {
        Painting painting = new Painting();
        float cx = 500, cy = 900;
        Polygon triangle = Polygon.triangle(cx, cy, 50);
        Polygon circle = Polygon.circle(cx, cy, 150);
        Polygon rectangle = Polygon.rectangle(cx, cy, cx, cy);
        PaintRegion triangleRegion = new PaintRegion(triangle);
        PaintRegion circleRegion = new PaintRegion(circle, triangle);
        PaintRegion rectangleRegion = new PaintRegion(rectangle, circle);
        painting.regions.add(triangleRegion);
        painting.regions.add(circleRegion);
        painting.regions.add(rectangleRegion);
        painting.colors.put(triangleRegion, Colour.COLOUR_1);
        painting.colors.put(circleRegion, null);
        painting.colors.put(rectangleRegion, Colour.COLOUR_3);
        painting.neighboursList.add(Arrays.asList(1));
        painting.neighboursList.add(Arrays.asList(0, 2));
        painting.neighboursList.add(Arrays.asList(1));
        return painting;
    }

    public static Painting outcastLevel1() {
        Painting painting = new Painting();
        float radius = 500;
        Polygon center = Polygon.triangle(radius);
        Polygon ne = new Polygon(Arrays.asList(
            new PointF((float) Math.cos(0) * radius, (float) Math.sin(0) * radius),
            new PointF((float) Math.cos(TAU / 3) * radius, (float) Math.sin(TAU / 3) * radius),
            new PointF((float) Math.cos(TAU / 3) * 2 * radius, (float) Math.sin(TAU / 3) * 2 * radius),
            new PointF((float) Math.cos(0) * 2 * radius, (float) Math.sin(0) * 2 * radius)
        ));
        Polygon se = new Polygon(Arrays.asList(
            new PointF((float) Math.cos(0) * radius, (float) Math.sin(0) * radius),
            new PointF((float) Math.cos(0) * 2 * radius, (float) Math.sin(0) * 2 * radius),
            new PointF((float) Math.cos(TAU * 2 / 3) * 2 * radius, (float) Math.sin(TAU * 2 / 3) * 2 * radius),
            new PointF((float) Math.cos(TAU * 2 / 3) * radius, (float) Math.sin(TAU * 2 / 3) * radius)
        ));
        Polygon w = new Polygon(Arrays.asList(
            new PointF((float) Math.cos(TAU * 2 / 3) * radius, (float) Math.sin(TAU * 2 / 3) * radius),
            new PointF((float) Math.cos(TAU * 2 / 3) * 2 * radius, (float) Math.sin(TAU * 2 / 3) * 2 * radius),
            new PointF((float) Math.cos(TAU / 3) * 2 * radius, (float) Math.sin(TAU / 3) * 2 * radius),
            new PointF((float) Math.cos(TAU / 3) * radius, (float) Math.sin(TAU / 3) * radius)
        ));
        PaintRegion centerRegion = new PaintRegion(center);
        PaintRegion neRegion = new PaintRegion(ne);
        PaintRegion seRegion = new PaintRegion(se);
        PaintRegion wRegion = new PaintRegion(w);
        painting.regions.add(centerRegion);
        painting.regions.add(neRegion);
        painting.regions.add(seRegion);
        painting.regions.add(wRegion);
        painting.neighboursList.add(Arrays.asList(1, 2, 3));
        painting.neighboursList.add(Arrays.asList(0, 2, 3));
        painting.neighboursList.add(Arrays.asList(0, 1, 3));
        painting.neighboursList.add(Arrays.asList(0, 1, 2));
        return painting;
    }

    @Nullable
    public PaintRegion getRegion(PointF point) {
        for (PaintRegion region : regions) {
            if (region.containsPoint(point.x, point.y)) {
                return region;
            }
        }
        return null;
    }

    public float getWidth() {
        return getMaxX() - getMinX();
    }

    public float getHeight() {
        return getMaxY() - getMinY();
    }

    public float getMinX() {
        float minX = Float.POSITIVE_INFINITY;
        for (PaintRegion region : regions) {
            minX = Math.min(minX, region.base.minX);
        }
        return minX;
    }

    public float getMaxX() {
        float maxX = Float.NEGATIVE_INFINITY;
        for (PaintRegion region : regions) {
            maxX = Math.max(maxX, region.base.maxX);
        }
        return maxX;
    }

    public float getMinY() {
        float minY = Float.POSITIVE_INFINITY;
        for (PaintRegion region : regions) {
            minY = Math.min(minY, region.base.minY);
        }
        return minY;
    }

    public float getMaxY() {
        float maxY = Float.NEGATIVE_INFINITY;
        for (PaintRegion region : regions) {
            maxY = Math.max(maxY, region.base.maxY);
        }
        return maxY;
    }

    public static class PaintRegion {
        public final Polygon base;
        public final List<Polygon> holes = new ArrayList<>();

        public PaintRegion(Polygon base, Polygon... holes) {
            this.base = base;
            this.holes.addAll(Arrays.asList(holes));
        }

        public boolean containsPoint(float x, float y) {
            if (base.containsPoint(x, y)) {
                for (Polygon hole : holes) {
                    if (hole.containsPoint(x, y)) {
                        return false;
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
    }

    public static class Polygon {
        final List<PointF> points;
        final float minX, maxX, minY, maxY;
        final Path path;

        public Polygon(List<PointF> points) {
            if (points.isEmpty()) throw new IllegalArgumentException();

            this.points = points;
            float minX = Float.POSITIVE_INFINITY;
            for (PointF point : points) {
                minX = Math.min(point.x, minX);
            }
            float maxX = Float.NEGATIVE_INFINITY;
            for (PointF point : points) {
                maxX = Math.max(point.x, maxX);
            }
            float minY = Float.POSITIVE_INFINITY;
            for (PointF point : points) {
                minY = Math.min(point.y, minY);
            }
            float maxY = Float.NEGATIVE_INFINITY;
            for (PointF point : points) {
                maxY = Math.max(point.y, maxY);
            }

            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;

            this.path = new Path();
            boolean first = true;
            for (PointF point : points) {
                if (first) {
                    this.path.moveTo(point.x, point.y);
                    first = false;
                }
                else {
                    this.path.lineTo(point.x, point.y);
                }
            }
            this.path.close();
        }

        public boolean containsPoint(float x, float y) {
            float farawayX = maxX + 1000;
            float farawayY = maxY + 1000;
            boolean containsPoint = false;
            for (int i = 0; i < points.size(); i++) {
                PointF p1 = points.get(i);
                PointF p2 = points.get((i + 1) % points.size());

                if (MathUtils.linesIntersect(x, y, farawayX, farawayY, p1.x, p1.y, p2.x, p2.y)) {
                    containsPoint = !containsPoint;
                }
            }

            return containsPoint;
        }

        public static Polygon circle(float cx, float cy, float r) {
            return circle(cx, cy, r, 20);
        }

        public static Polygon circle(float cx, float cy, float r, int numPoints) {
            ArrayList<PointF> points = new ArrayList<>();
            for (int i = 0; i < numPoints; i++) {
                double angle = 2 * Math.PI * i / numPoints;
                double x = cx + Math.cos(angle) * r;
                double y = cy + Math.sin(angle) * r;
                points.add(new PointF((float) x, (float) y));
            }
            return new Polygon(points);
        }

        public static Polygon rectangle(float cx, float cy, float rx, float ry) {
            ArrayList<PointF> points = new ArrayList<>();
            points.add(new PointF(cx - rx, cy - ry));
            points.add(new PointF(cx + rx, cy - ry));
            points.add(new PointF(cx + rx, cy + ry));
            points.add(new PointF(cx - rx, cy + ry));
            return new Polygon(points);
        }

        public static Polygon triangle(float r) {
            return circle(0, 0, r, 3);
        }

        public static Polygon triangle(float cx, float cy, float r) {
            return circle(cx, cy, r, 3);
        }
    }
}
