package se.olander.android.four;

import android.graphics.PointF;

/**
 * Created by sios on 2018-01-03.
 */

public class Node {
    public PointF point;

    public Node(PointF point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "Node{" +
            "point=" + point +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return point != null ? point.equals(node.point) : node.point == null;
    }

    @Override
    public int hashCode() {
        return point != null ? point.hashCode() : 0;
    }
}
