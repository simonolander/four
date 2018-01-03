package se.olander.android.four;

/**
 * Created by sios on 2018-01-03.
 */

public class DEdge {
    public Node from;
    public Node to;

    public DEdge(Node from, Node to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DEdge dEdge = (DEdge) o;

        if (from != null ? !from.equals(dEdge.from) : dEdge.from != null) return false;
        return to != null ? to.equals(dEdge.to) : dEdge.to == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}
