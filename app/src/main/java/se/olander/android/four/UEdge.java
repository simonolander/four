package se.olander.android.four;

public class UEdge {
    public Node n1;
    public Node n2;

    public UEdge(Node n1, Node n2) {
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
}
