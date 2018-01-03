package se.olander.android.four;

import android.graphics.PointF;

import java.util.HashSet;
import java.util.List;

public class Face {
    private final HashSet<UEdge> uEdges;
    public List<Node> nodes;

    public Face(List<Node> nodes) {
        this.nodes = nodes;
        this.uEdges = new HashSet<>();
        for (int i1 = 0; i1 < nodes.size(); i1++) {
            int i2 = (i1 + 1) % nodes.size();
            uEdges.add(new UEdge(nodes.get(i1), nodes.get(i2)));
        }
    }

    public static boolean areNeighbours(Face f1, Face f2) {
        for (UEdge uEdge : f1.uEdges) {
            if (f2.uEdges.contains(uEdge)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsNode(Node node) {
        for (Node n : nodes) {
            if (node.equals(n)) {
                return true;
            }
        }

        float farawayX = 1e10f;
        float farawayY = 1e10f;
        boolean containsNode = false;
        for (int i = 0; i < nodes.size(); i++) {
            PointF p1 = nodes.get(i).point;
            PointF p2 = nodes.get((i + 1) % nodes.size()).point;

            if (MathUtils.linesIntersect(node.point.x, node.point.y, farawayX, farawayY, p1.x, p1.y, p2.x, p2.y)) {
                containsNode = !containsNode;
            }
        }
        return containsNode;
    }
}
