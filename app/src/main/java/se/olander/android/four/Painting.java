package se.olander.android.four;

import android.graphics.Path;
import android.graphics.PointF;
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

    public static Painting outcastLevel2() {
        float radius = 250;
        PointF nw1 = new PointF(-radius, -radius);
        PointF ne1 = new PointF(radius, -radius);
        PointF se1 = new PointF(radius, radius);
        PointF sw1 = new PointF(-radius, radius);
        PointF w1 = new PointF(-radius, 0);
        PointF n1 = new PointF(0, -radius);
        PointF e1 = new PointF(radius, 0);
        PointF s1 = new PointF(0, radius);
        PointF nw2 = new PointF(-radius * 2, -radius * 2);
        PointF ne2 = new PointF(radius * 2, -radius * 2);
        PointF se2 = new PointF(radius * 2, radius * 2);
        PointF sw2 = new PointF(-radius * 2, radius * 2);
        PointF w2 = new PointF(-radius * 2, 0);
        PointF n2 = new PointF(0, -radius * 2);
        PointF e2 = new PointF(radius * 2, 0);
        PointF s2 = new PointF(0, radius * 2);
        PointF nw3 = new PointF(-radius * 3, -radius * 3);
        PointF ne3 = new PointF(radius * 3, -radius * 3);
        PointF se3 = new PointF(radius * 3, radius * 3);
        PointF sw3 = new PointF(-radius * 3, radius * 3);
        PaintRegion center = new PaintRegion(new Polygon(nw1, ne1, se1, sw1));
        PaintRegion nw = new PaintRegion(new Polygon(nw2, n2, n1, nw1, w1, w2));
        PaintRegion ne = new PaintRegion(new Polygon(n1, n2, ne2, e2, e1, ne1));
        PaintRegion se = new PaintRegion(new Polygon(e1, e2, se2, s2, s1, se1));
        PaintRegion sw = new PaintRegion(
            new Polygon(sw3, nw3, ne3, se3),
            new Polygon(sw1, w1, w2, nw2, ne2, se2, s2, s1)
        );
        Painting painting = new Painting();
        painting.regions.add(center);
        painting.regions.add(nw);
        painting.regions.add(ne);
        painting.regions.add(se);
        painting.regions.add(sw);
        painting.neighboursList.add(Arrays.asList(1, 2, 3, 4));
        painting.neighboursList.add(Arrays.asList(0, 2, 4));
        painting.neighboursList.add(Arrays.asList(0, 1, 3, 4));
        painting.neighboursList.add(Arrays.asList(0, 2, 4));
        painting.neighboursList.add(Arrays.asList(0, 1, 2, 3));
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

        @Override
        public String toString() {
            return "PaintRegion{" +
                "base=" + base +
                ", holes=" + holes +
                '}' ;
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

        public Polygon(PointF... points) {
            this(Arrays.asList(points));
        }

        public boolean containsPoint(float x, float y) {
            if (x < minX || x > maxX || y < minY || y > maxY) {
                return false;
            }

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

        @Override
        public String toString() {
            return "Polygon{" +
                "points=" + points +
                '}' ;
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
