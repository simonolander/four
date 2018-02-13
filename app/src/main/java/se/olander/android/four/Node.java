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
}
