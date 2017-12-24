package se.olander.android.four;

import android.graphics.Path;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Painting {

    final ArrayList<List<Integer>> neighboursList = new ArrayList<>();
    final Map<PaintRegion, Integer> colors = new HashMap<>();
    final ArrayList<PaintRegion> regions = new ArrayList<>();

    public Painting() {

    }

    public static Painting somePainting() {
        Painting painting = new Painting();
        Polygon triangle = Polygon.triangle(540, 760, 50);
        Polygon circle = Polygon.circle(540, 760, 150);
        Polygon rectangle = Polygon.rectangle(540, 760, 540, 760);
        PaintRegion triangleRegion = new PaintRegion(triangle);
        PaintRegion circleRegion = new PaintRegion(circle, triangle);
        PaintRegion rectangleRegion = new PaintRegion(rectangle, circle);
        painting.regions.add(triangleRegion);
        painting.regions.add(circleRegion);
        painting.regions.add(rectangleRegion);
        painting.colors.put(triangleRegion, 0);
        painting.colors.put(circleRegion, null);
        painting.colors.put(rectangleRegion, 2);
        painting.neighboursList.add(Arrays.asList(1));
        painting.neighboursList.add(Arrays.asList(0, 2));
        painting.neighboursList.add(Arrays.asList(1));
        return painting;
    }

    public static class PaintRegion {
        public final Polygon base;
        public final List<Polygon> holes = new ArrayList<>();

        public PaintRegion(Polygon base, Polygon... holes) {
            this.base = base;
            this.holes.addAll(Arrays.asList(holes));
        }
    }

    public static class Polygon {
        final ArrayList<PointF> points = new ArrayList<>();

        public Path getPath() {
            Path path = new Path();
            boolean first = true;
            for (PointF point : points) {
                if (first) {
                    path.moveTo(point.x, point.y);
                    first = false;
                }
                else {
                    path.lineTo(point.x, point.y);
                }
            }
            path.close();
            return path;
        }

        public static Polygon circle(float cx, float cy, float r) {
            return circle(cx, cy, r, 20);
        }

        public static Polygon circle(float cx, float cy, float r, int numPoints) {
            Polygon circle = new Polygon();
            for (int i = 0; i < numPoints; i++) {
                double angle = 2 * Math.PI * i / numPoints;
                double x = cx + Math.cos(angle) * r;
                double y = cy + Math.sin(angle) * r;
                circle.points.add(new PointF((float) x, (float) y));
            }
            return circle;
        }

        public static Polygon rectangle(float cx, float cy, float rx, float ry) {
            Polygon rectangle = new Polygon();
            rectangle.points.add(new PointF(cx - rx, cy - ry));
            rectangle.points.add(new PointF(cx + rx, cy - ry));
            rectangle.points.add(new PointF(cx + rx, cy + ry));
            rectangle.points.add(new PointF(cx - rx, cy + ry));
            return rectangle;
        }

        public static Polygon triangle(float cx, float cy, float r) {
            return circle(cx, cy, r, 3);
        }
    }
}
