package store.pojo;

import java.util.LinkedList;
import java.util.List;

public class Content {

    List<Content> childrenContents;
    String hash;
    long endByte;

    boolean isLeaf;
    boolean isRoot;
    byte[] data;

    public Content(){

    }

    @Override
    public String toString() {
        return "Node{" +
                "childrenNodes=" + childrenContents +
                ", hash='" + hash + '\'' +
                ", endByte=" + endByte +
                ", isLeaf=" + isLeaf +
                ", isRoot=" + isRoot +
                '}';
    }

    public Content(List<Content> childrenContents, String hash, long endByte, boolean isLeaf, boolean isRoot) {
        this.childrenContents = childrenContents;
        this.hash = hash;
        this.endByte = endByte;
        this.isLeaf = isLeaf;
        this.isRoot = isRoot;
    }

    public Content(List<Content> childrenContents, String hash, long endByte) {
        this.childrenContents = childrenContents;
        this.hash = hash;
        this.endByte = endByte;
    }

    public Content(String hash, long endByte,byte[] data) {
        this.hash = hash;
        this.endByte = endByte;
        this.data=data;
    }

    public Content(String hash, long endByte, boolean isLeaf, byte[] data) {
        this.hash = hash;
        this.endByte = endByte;
        this.isLeaf=isLeaf;
        this.data =data;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public List<Content> getChildrenNodes() {
        return childrenContents;
    }

    public void setChildrenNodes(List<Content> childrenContents) {
        this.childrenContents = childrenContents;
    }

    public void addChild(Content childContent){

        if(childrenContents ==null) childrenContents = new LinkedList<>();
        childrenContents.add(childContent);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public long getEndByte() {
        return endByte;
    }

    public void setEndByte(long endByte) {
        this.endByte = endByte;
    }

    public List<Content> getChildrenContents() {
        return childrenContents;
    }

    public void setChildrenContents(List<Content> childrenContents) {
        this.childrenContents = childrenContents;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
