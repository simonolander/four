package se.olander.android.four;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Face {
    private final HashSet<UEdge> edgeSet;
    private final List<UEdge> edgeList;
    public List<Node> nodes;

    public Face(List<Node> nodes) {
        if (nodes.size() < 3) {
            throw new IllegalArgumentException("Cannot make a face with less than 3 nodes: " + nodes);
        }
        this.nodes = nodes;
        this.edgeList = new ArrayList<>(nodes.size());
        for (int i1 = 0; i1 < nodes.size(); i1++) {
            int i2 = (i1 + 1) % nodes.size();
            edgeList.add(new UEdge(nodes.get(i1), nodes.get(i2)));
        }
        this.edgeSet = new HashSet<>(edgeList);
    }

    public static boolean areNeighbours(Face f1, Face f2) {
        for (UEdge uEdge : f1.edgeSet) {
            if (f2.edgeSet.contains(uEdge)) {
                return true;
            }
        }

        return false;
    }

    public static int numFacesInCommon(Face f1, Face f2) {
        int count = 0;
        for (UEdge uEdge : f1.edgeSet) {
            if (f2.edgeSet.contains(uEdge)) {
                count += 1;
            }
        }

        return count;
    }

    public static Face mergeFaces(Face f1, Face f2) {
        List<Node> nodes = new ArrayList<>();
        Node breakNode = null;
        for (int i = 0; i < f1.nodes.size(); i++) {
            Node n1 = f1.nodes.get(i);
            Node n2 = f1.nodes.get((i + 1) % f1.nodes.size());
            nodes.add(n1);
            if (f2.edgeSet.contains(new UEdge(n1, n2))) {
                breakNode = n1;
                break;
            }
        }
        int f2Offset = f2.nodes.indexOf(breakNode);
        breakNode = null;
        for (int i = 0; i < f2.nodes.size(); i++) {
            Node n1 = f2.nodes.get((i + f2Offset) % f2.nodes.size());
            Node n2 = f2.nodes.get((i + f2Offset + 1) % f2.nodes.size());
            nodes.add(n2);
            if (f1.edgeSet.contains(new UEdge(n1, n2))) {
                breakNode = n1;
                break;
            }
        }
        int f1Offset = f1.nodes.indexOf(breakNode);
        for (int i = f1Offset + 1; i < f1.nodes.size(); i++) {
            nodes.add(f1.nodes.get(i));
        }
        return new Face(nodes);
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

    public List<UEdge> findCommonEdges(Face other) {
        ArrayList<UEdge> edges = new ArrayList<>();
        for (UEdge edge : other.edgeSet) {
            if (this.edgeSet.contains(edge)) {
                edges.add(edge);
            }
        }
        return edges;
    }

    public UEdge findSingleCommonEdge(Face other) {
        List<UEdge> edges = findCommonEdges(other);
        if (edges.size() == 1) {
            return edges.get(0);
        }
        else if (edges.size() == 0) {
            throw new IllegalStateException("No common edges: " + this.edgeList + ", " + other.edgeList);
        }
        else {
            throw new IllegalStateException("Found " + edges.size() + " common edges: " + this.edgeList + ", " + other.edgeList + ", " + edges);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Face face = (Face) o;

        if (edgeSet != null ? !edgeSet.equals(face.edgeSet) : face.edgeSet != null) return false;
        if (edgeList != null ? !edgeList.equals(face.edgeList) : face.edgeList != null)
            return false;
        return nodes != null ? nodes.equals(face.nodes) : face.nodes == null;
    }

    @Override
    public int hashCode() {
        int result = edgeSet != null ? edgeSet.hashCode() : 0;
        result = 31 * result + (edgeList != null ? edgeList.hashCode() : 0);
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        return result;
    }
}
