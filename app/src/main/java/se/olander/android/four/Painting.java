package se.olander.android.four;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Painting {

    private static final double TAU = 2 * Math.PI;

    final Map<PaintRegion, List<PaintRegion>> neighboursList = new HashMap<>();
    final Map<PaintRegion, Colour> colors = new HashMap<>();
    final ArrayList<PaintRegion> regions = new ArrayList<>();

    public Painting() {

    }

    public Painting(List<PaintRegion> regions, List<List<Integer>> neighboursList) {
        for (int i = 0; i < neighboursList.size(); i++) {
            ArrayList<PaintRegion> neighbours = new ArrayList<>();
            for (Integer integer : neighboursList.get(i)) {
                neighbours.add(regions.get(integer));
            }
            this.neighboursList.put(regions.get(i), neighbours);
        }
        Collections.sort(regions, new Comparator<PaintRegion>() {
            @Override
            public int compare(PaintRegion r1, PaintRegion r2) {
                return Float.compare(r2.base.area, r1.base.area);
            }
        });
        this.regions.addAll(regions);

        if (neighboursList.size() != regions.size()) {
            throw new IllegalArgumentException("Neighbours and regions have different size (" + neighboursList.size() + " != " + regions.size() + ")");
        }
        for (List<Integer> neighbours : neighboursList) {
            for (Integer neighbour : neighbours) {
                if (neighbour >= regions.size()) {
                    throw new ArrayIndexOutOfBoundsException("Neighbour with index " + neighbour + " >= " + regions.size());
                }
            }
        }
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

    public int getRegionIndex(PaintRegion region) {
        Objects.requireNonNull(region);
        for (int i = 0; i < regions.size(); i++) {
            if (region.equals(regions.get(i))) {
                return i;
            }
        }
        throw new IllegalArgumentException("Region " + region + "does not belong to painting");
    }

    public List<PaintRegion> getNeighbours(PaintRegion region) {
        return neighboursList.get(region);
    }

    public boolean isCorrectlyColored() {
        return !getSomeDiscoloredRegions().isEmpty();
    }

    public List<PaintRegion> getSomeDiscoloredRegions() {
        for (PaintRegion region : regions) {
            if (colors.get(region) == null) {
                return Collections.singletonList(region);
            }
        }
        for (PaintRegion r1 : regions) {
            Colour colour = colors.get(r1);
            for (PaintRegion r2 : getNeighbours(r1)) {
                if (colors.get(r2) == colour) {
                    return Arrays.asList(r1, r2);
                }
            }
        }
        return Collections.emptyList();
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
        final float area;

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

            float area = 0;
            for (int i = 0; i < points.size(); i++) {
                PointF p1 = points.get(i), p2 = points.get((i + 1) % points.size());
                area += p1.x * p2.y - p2.x * p1.y;
            }
            area = Math.abs(area / 2);
            this.area = area;
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
