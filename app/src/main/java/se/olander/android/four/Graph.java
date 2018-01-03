package se.olander.android.four;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {

    private List<Node> nodes;
    private Map<Node, List<Node>> neighboursMap;
    public List<Face> faces;
    public Map<Face, List<Face>> faceNeighboursMap;

    public Graph() {
        nodes = new ArrayList<>();
        neighboursMap = new HashMap<>();
    }

    public Graph addNode(PointF point) {
        return addNode(new Node(point));
    }

    public Graph addNode(Node node) {
        nodes.add(node);
        neighboursMap.put(node, new ArrayList<Node>());
        return this;
    }

    public Graph connect(int i1, int i2) {
        return connect(nodes.get(i1), nodes.get(i2));
    }

    public Graph connect(Node n1, Node n2) {
        neighboursMap.get(n1).add(n2);
        neighboursMap.get(n2).add(n1);
        return this;
    }

    public Graph build() {
        sortNeighbours();
        computeFaces();
        removeBoundaryFace();
        computeFaceNeighboursMap();
        return this;
    }

    private void removeBoundaryFace() {
        faces.remove(getBoundaryFace());
    }

    public Painting computePainting() {
        List<Painting.PaintRegion> regions = new ArrayList<>();
        for (Face face : faces) {
            ArrayList<PointF> points = new ArrayList<>();
            for (Node node : face.nodes) {
                points.add(node.point);
            }
            regions.add(new Painting.PaintRegion(new Painting.Polygon(points)));
        }
        Map<Face, Integer> faceIndices = new HashMap<>();
        for (int i = 0; i < faces.size(); i++) {
            faceIndices.put(faces.get(i), i);
        }
        List<List<Integer>> neighboursList = new ArrayList<>();
        for (Face face : faces) {
            List<Integer> neighbours = new ArrayList<>();
            for (Face neighbour : faceNeighboursMap.get(face)) {
                neighbours.add(faceIndices.get(neighbour));
            }
            neighboursList.add(neighbours);
        }
        return new Painting(regions, neighboursList);
    }

    private void computeFaceNeighboursMap() {
        faceNeighboursMap = new HashMap<>();
        for (Face f1 : faces) {
            List<Face> neigbours = new ArrayList<>();
            for (Face f2 : faces) {
                if (f1.equals(f2)) {
                    continue;
                }

                if (Face.areNeighbours(f1, f2)) {
                    neigbours.add(f2);
                }
            }

            faceNeighboursMap.put(f1, neigbours);
        }
    }

    private void sortNeighbours() {
        for (final Node node : nodes) {
            List<Node> neighbours = neighboursMap.get(node);
            Collections.sort(neighbours, new Comparator<Node>() {
                @Override
                public int compare(Node n1, Node n2) {
                    double x1 = n1.point.x - node.point.x;
                    double y1 = n1.point.y - node.point.y;
                    double x2 = n2.point.x - node.point.x;
                    double y2 = n2.point.y - node.point.y;
                    double a1 = Math.atan2(y1, x1);
                    double a2 = Math.atan2(y2, x2);
                    return Double.compare(a1, a2);
                }
            });
        }
    }

    private void computeFaces() {
        faces = new ArrayList<>();
        Set<DEdge> visitedEdges = new HashSet<>();
        for (Node node : nodes) {
            faces.addAll(computeFaces(node, visitedEdges));
        }
    }

    private Collection<? extends Face> computeFaces(final Node node, Set<DEdge> visitedEdges) {
        Set<Face> faces = new HashSet<>();
        List<Node> neighbours = neighboursMap.get(node);
        for (Node neighbour : neighbours) {
            DEdge dEdge = new DEdge(node, neighbour);
            boolean visited = !visitedEdges.add(dEdge);
            if (visited) {
                continue;
            }
            List<Node> faceNodes = new ArrayList<>();
            faceNodes.add(node);
            faceNodes.add(neighbour);
            traverseFace(faceNodes, visitedEdges);
            faces.add(new Face(faceNodes));
        }
        return faces;
    }

    private void traverseFace(List<Node> faceNodes, Set<DEdge> visitedEdges) {
        Node current = faceNodes.get(faceNodes.size() - 1);
        Node last = faceNodes.get(faceNodes.size() - 2);
        while (true) {
            Node next = nextNeighbour(current, last);
            DEdge dEdge = new DEdge(current, next);
            boolean visited = !visitedEdges.add(dEdge);
            if (visited) {
                return;
            }
            faceNodes.add(next);
            last = current;
            current = next;
        }
    }

    private Node nextNeighbour(Node currentNode, Node lastNeighbour) {
        int lastNeighbourIndex = -1;
        List<Node> neighbours = neighboursMap.get(currentNode);
        for (int i = 0; i < neighbours.size(); i++) {
            if (lastNeighbour.equals(neighbours.get(i))) {
                lastNeighbourIndex = i;
                break;
            }
        }
        if (lastNeighbourIndex == -1) {
            throw new RuntimeException();
        }
        return neighbours.get((lastNeighbourIndex + 1) % neighbours.size());
    }

    private Face getBoundaryFace() {
        int value = 0;
        Face face = null;
        for (Face f : faces) {
            int v = 0;
            for (Node node : nodes) {
                if (f.containsNode(node)) {
                    v += 1;
                }
            }
            if (v > value) {
                value = v;
                face = f;
            }
        }
        return face;
    }
}
