package se.olander.android.four;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class Graph {

    private static final String TAG = Graph.class.getSimpleName();

    private List<Node> nodes;
    private Map<Node, List<Node>> neighboursMap;
    public List<Face> faces;
    public Map<Face, Set<Face>> faceNeighboursMap;

    public Graph() {
        nodes = new ArrayList<>();
        neighboursMap = new HashMap<>();
    }

    public Graph addNode(PointF point) {
        return addNode(new Node(point));
    }

    public Graph addNode(Node node) {
        for (Node node1 : nodes) {
            if (node1.point.equals(node.point)) {
                throw new IllegalArgumentException("Point " + node.point + " already exists");
            }
        }
        nodes.add(node);
        neighboursMap.put(node, new ArrayList<Node>());
        return this;
    }

    public Graph connect(int i1, int i2) {
        return connect(nodes.get(i1), nodes.get(i2));
    }

    public Graph connect(Node n1, Node n2) {
        if (n1.equals(n2)) {
            throw new IllegalArgumentException("n1 and n2 can't be equal");
        }
        neighboursMap.get(n1).add(n2);
        neighboursMap.get(n2).add(n1);
        return this;
    }

    public Graph disconnect(int i1, int i2) {
        return disconnect(nodes.get(i1), nodes.get(i2));
    }

    public Graph disconnect(Node n1, Node n2) {
        neighboursMap.get(n1).remove(n2);
        neighboursMap.get(n2).remove(n1);
        return this;
    }

    public Collection<Node> getNeighbours(int i) {
        return getNeighbours(nodes.get(i));
    }

    public Collection<Node> getNeighbours(Node node) {
        return neighboursMap.get(node);
    }

    public int indexOf(Node node) {
        return this.nodes.indexOf(node);
    }

    public boolean areNeighbours(int n1, int n2) {
        return getNeighbours(n1).contains(nodes.get(n2));
    }

    public boolean areNeighbours(Node n1, Node n2) {
        return getNeighbours(n1).contains(n2);
    }

    public boolean edgeWouldIntersectAnotherEdge(Node n1, Node n2) {
        for (Node n3 : nodes) {
            if (n1.equals(n3) || n2.equals(n3)) {
                continue;
            }
            for (Node n4 : getNeighbours(n3)) {
                if (n1.equals(n4) || n2.equals(n4)) {
                    continue;
                }
                if (MathUtils.linesIntersect(n1.point, n2.point, n3.point, n4.point)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void connectAllShortest() {
        List<UEdge> possibleEdges = new ArrayList<>();
        for (int i1 = 0; i1 < nodes.size(); i1++) {
            for (int i2 = i1 + 1; i2 < nodes.size(); i2++) {
                possibleEdges.add(new UEdge(nodes.get(i1), nodes.get(i2)));
            }
        }
        possibleEdges.sort(new UEdge.DistanceComparator());
        for (UEdge edge : possibleEdges) {
            if (!edgeWouldIntersectAnotherEdge(edge.n1, edge.n2)) {
                connect(edge.n1, edge.n2);
            }
        }
    }

    public Graph build() {
        removeUnnecessaryNodes();
        sortNeighbours();
        computeFaces();
        removeBoundaryFace();
        computeFaceNeighboursMap();
        return this;
    }

    private void removeUnnecessaryNodes() {
        again: while (true) {
            for (Node node : nodes) {
                Collection<Node> neighbours = getNeighbours(node);
                if (neighbours.size() <= 1) {
                    for (Node neighbour : neighbours) {
                        disconnect(node, neighbour);
                    }
                    nodes.remove(node);
                    neighboursMap.remove(node);
                    continue again;
                }
            }
            break;
        }
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
            Set<Face> neigbours = new HashSet<>();
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
            // TODO Remove check
            if (faceNodes.get(0).equals(faceNodes.get(faceNodes.size() - 1))) {
                faceNodes.remove(faceNodes.size() - 1);
            }
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

    public Graph copy() {
        Graph graph = new Graph();
        for (Node node : nodes) {
            graph.addNode(node.point);
        }
        HashMap<Node, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            indexMap.put(nodes.get(i), i);
        }
        for (int i1 = 0; i1 < nodes.size(); i1++) {
            for (Node n2 : getNeighbours(i1)) {
                int i2 = indexMap.get(n2);
                if (i2 > i1) {
                    graph.connect(i1, i2);
                }
            }
        }
        return graph;
    }

    private void dfsMaze(Random random) {
        Graph faceGraph = copy().build();
        for (Node node : faceGraph.nodes) {
            Log.d(TAG, "dfsMaze: " + node);
        }
        Face currentFace = faceGraph.faces.get(random.nextInt(faceGraph.faces.size()));
        dfsMaze(currentFace, faceGraph, new HashSet<Face>(), new HashSet<Face>(), random);
    }

    private void dfsMaze(Face currentFace, Graph faceGraph, Set<Face> currentRoom, Set<Face> visited, Random random) {
        visited.add(currentFace);
        currentRoom.add(currentFace);
        List<Face> neighbourFaces = new ArrayList<>(faceGraph.faceNeighboursMap.get(currentFace));
        Collections.shuffle(neighbourFaces, random);
        boolean branched = false;
        for (Face face : neighbourFaces) {
            if (visited.contains(face)) {
                continue;
            }

            boolean adjacentToCurrentRoom = false;
            for (Face f2 : faceGraph.faceNeighboursMap.get(face)) {
                if (f2.equals(currentFace)) {
                    continue;
                }
                if (currentRoom.contains(f2)) {
                    adjacentToCurrentRoom = true;
                    break;
                }
            }
            if (adjacentToCurrentRoom) {
                continue;
            }

            UEdge commonEdge = currentFace.findSingleCommonEdge(face);
            int i1 = faceGraph.indexOf(commonEdge.n1);
            int i2 = faceGraph.indexOf(commonEdge.n2);
            if (branched) {
                dfsMaze(face, faceGraph, new HashSet<Face>(), visited, random);
            }
            else {
                disconnect(i1, i2);
                dfsMaze(face, faceGraph, currentRoom, visited, random);
            }
            branched = true;
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

    private static Graph square(int width, int height, float cellSize) {
        Graph graph = new Graph();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                graph.addNode(new PointF(cellSize * x, cellSize * y));
            }
        }
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width - 1; x++) {
                int n = y * width + x;
                graph.connect(n, n + 1);
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height - 1; y++) {
                int n = y * width + x;
                graph.connect(n, n + width);
            }
        }

        return graph;
    }

    public static Graph bfsMazeGraph(int width, int height, int seed) {
        int size = 100;

        Graph faceGraph = new Graph();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                faceGraph.addNode(new PointF(size * x, size * y));
            }
        }

        Random random = new Random(seed);
        boolean[][] visited = new boolean[width][height];
        visited[0][0] = true;
        dfsMaze(faceGraph, width, height, new Coord(0, 0), visited, random, new HashSet<Coord>());

        Graph wallGraph = square(width + 1, height + 1, size);
        for (int i = 0; i < faceGraph.nodes.size(); i++) {
            int x = i % width;
            int y = i / width;
            for (Node node : faceGraph.getNeighbours(i)) {
                int ni = faceGraph.indexOf(node);
                int nx = ni % width;
                int ny = ni / width;
                if (y == ny) {
                    int wx = Math.max(x, nx);
                    int wi1 = y * (width + 1) + wx;
                    int wi2 = (y + 1) * (width + 1) + wx;
                    wallGraph.disconnect(wi1, wi2);
                }
                if (x == nx) {
                    int wy = Math.max(y, ny);
                    int wi1 = wy * (width + 1) + x;
                    int wi2 = wy * (width + 1) + (x + 1);
                    wallGraph.disconnect(wi1, wi2);
                }
            }
        }

        return wallGraph;
    }

    private static void dfsMaze(Graph graph, int width, int height, Coord current, boolean[][] visited, Random random, Set<Coord> currentRoom) {
        currentRoom.add(current);
        int n1 = current.y * width + current.x;
        List<Coord> nexts = mazeNeighbours(current, width, height);
        Collections.shuffle(nexts, random);
        boolean branched = false;
        for (Coord next : nexts) {
            if (visited[next.x][next.y]) {
                continue;
            }
            boolean skip = false;
            for (Coord neighbour : mazeNeighbours(next, width, height)) {
                if (!neighbour.equals(current) && currentRoom.contains(neighbour)) {
                    skip = true;
                }
            }
            if (skip) {
                continue;
            }

            int n2 = next.y * width + next.x;
            if (!branched) {
                graph.connect(n1, n2);
            }
            visited[next.x][next.y] = true;
            dfsMaze(graph, width, height, next, visited, random, branched ? new HashSet<Coord>() : currentRoom);
            branched = true;
        }
    }

    private static List<Coord> mazeNeighbours(Coord c, int width, int height) {
        List<Coord> neighbours = new ArrayList<>();
        if (c.x > 0) {
            neighbours.add(new Coord(c.x - 1, c.y));
        }
        if (c.x < width - 1) {
            neighbours.add(new Coord(c.x + 1, c.y));
        }
        if (c.y > 0) {
            neighbours.add(new Coord(c.x, c.y - 1));
        }
        if (c.y < height - 1) {
            neighbours.add(new Coord(c.x, c.y + 1));
        }

        return neighbours;
    }

    private static void printWallGraph(Graph g, int w, int h) {
        StringBuilder builder = new StringBuilder();
        builder.append('\n');
        for (int n = 0; n < w - 1; n++) {
            builder.append('·');
            if (g.areNeighbours(n, n + 1)) {
                builder.append('-');
            }
            else {
                builder.append(' ');
            }
        }
        builder.append('·');
        builder.append('\n');
        for (int y = 1; y < h; ++y) {
            for (int x = 0; x < w; x++) {
                int n = y * w + x;
                if (g.areNeighbours(n, n - w)) {
                    builder.append('|');
                }
                else {
                    builder.append(' ');
                }
                builder.append(' ');
            }
            builder.append('\n');
            for (int x = 0; x < w - 1; x++) {
                int n = y * w + x;
                builder.append('·');
                if (g.areNeighbours(n, n + 1)) {
                    builder.append('-');
                }
                else {
                    builder.append(' ');
                }
            }
            builder.append('·');
            builder.append('\n');
        }
        Log.d(TAG, "printWallGraph: " + builder.toString());
    }

    public static Graph sunflower(int numberOfPoints, float alpha, Random random) {
        float radius = 900;
        Graph graph = new Graph();
        int numberOfBoundaryPoints = (int) Math.round(alpha * Math.sqrt(numberOfPoints));
        for (int i = 0; i < numberOfPoints; i++) {
            float r = i + 1 > numberOfPoints - numberOfBoundaryPoints
                ? 1
                : (float) (Math.sqrt(i + 0.5) / Math.sqrt(numberOfPoints - (numberOfBoundaryPoints + 1) / 2));
            float theta = MathUtils.TAU * (i + 1) / (MathUtils.PHI * MathUtils.PHI);
            PointF point = new PointF(r * radius * MathUtils.cos(theta), r * radius * MathUtils.sin(theta));
            graph.addNode(point);
        }
        graph.connectAllShortest();
        graph.dfsMaze(random);
        return graph;
    }

    public static class Coord {
        public int x, y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Coord coord = (Coord) o;

            if (x != coord.x) return false;
            return y == coord.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    private static class DistanceComparator implements Comparator<Node> {

        private final Node node;

        public DistanceComparator(@NonNull Node node) {
            this.node = node;
        }

        @Override
        public int compare(Node n1, Node n2) {
            return Float.compare(MathUtils.distanceSquared(node.point, n1.point), MathUtils.distanceSquared(node.point, n2.point));
        }
    }
}
