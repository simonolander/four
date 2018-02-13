package se.olander.android.four;

import java.util.Comparator;

public class UEdge {
    public final Node n1;
    public final Node n2;

    public UEdge(Node n1, Node n2) {
        if (n1.equals(n2)) {
            throw new IllegalArgumentException("n1 == n2: " + n1 + ", " + n2);
        }
        this.n1 = n1;
        this.n2 = n2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UEdge uEdge = (UEdge) o;

        return (n1.equals(uEdge.n1) && n2.equals(uEdge.n2)) || (n1.equals(uEdge.n2) && n2.equals(uEdge.n1));
    }

    @Override
    public int hashCode() {
        return n1.hashCode() + n2.hashCode();
    }

    public static class DistanceComparator implements Comparator<UEdge> {

        @Override
        public int compare(UEdge e1, UEdge e2) {
            return Float.compare(
                MathUtils.distanceSquared(e1.n1.point, e1.n2.point),
                MathUtils.distanceSquared(e2.n1.point, e2.n2.point)
            );
        }
    }

    @Override
    public String toString() {
        return "UEdge{" +
            "n1=" + n1 +
            ", n2=" + n2 +
            '}';
    }
}
