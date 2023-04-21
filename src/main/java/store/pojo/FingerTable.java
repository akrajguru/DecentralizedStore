package store.pojo;

public class FingerTable {
    long start;

    Node node;

    public FingerTable(long start) {
        this.start = start;
    }

    public FingerTable(long start,Node node) {
        this.start = start;
        this.node = node;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }


    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return "FingerTable{" +
                "start='" + start + '\'' +
                ", node=" + node +
                '}';
    }
}
