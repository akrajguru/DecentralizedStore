package store.pojo;

public class FingerTable {
    String start;

    Node node;

    public FingerTable(String start) {
        this.start = start;
    }

    public FingerTable(String start,Node node) {
        this.start = start;
        this.node = node;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
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
