package store.pojo;

import java.util.Arrays;
import java.util.List;

public class Storage {

    String fileName;
    long size;
    String rootHash;
    byte[] dataBytes;
    boolean containsContent;
    String contentHash;
    List<String> contentList;
    long endOfBlock;
    String owner;

    public Storage() {
    }

    public Storage(String fileName, long size, String rootHash, List<String> contentList) {
        this.fileName = fileName;
        this.size = size;
        this.rootHash = rootHash;
        this.containsContent = false;
        this.contentList = contentList;
    }

    public Storage(String fileName, String rootHash, byte[] dataBytes, String contentHash, long endOfBlock) {
        this.fileName = fileName;
        this.rootHash = rootHash;
        this.dataBytes = dataBytes;
        this.containsContent = true;
        this.contentHash = contentHash;
        this.endOfBlock=endOfBlock;
    }

    public long getEndOfBlock() {
        return endOfBlock;
    }

    public void setEndOfBlock(long endOfBlock) {
        this.endOfBlock = endOfBlock;
    }

    public List<String> getContentList() {
        return contentList;
    }

    public void setContentList(List<String> contentList) {
        this.contentList = contentList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getRootHash() {
        return rootHash;
    }

    public void setRootHash(String rootHash) {
        this.rootHash = rootHash;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public void setDataBytes(byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }

    public boolean isContainsContent() {
        return containsContent;
    }

    public void setContainsContent(boolean containsContent) {
        this.containsContent = containsContent;
    }

    public String getContentHash() {
        return contentHash;
    }

    public void setContentHash(String contentHash) {
        this.contentHash = contentHash;
    }

    public void printBytes(){}


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "fileName='" + fileName + '\'' +
                ", size=" + size +
                ", rootHash='" + rootHash + '\'' +
                ", dataBytes=" + dataBytes.length +
                ", containsContent=" + containsContent +
                ", contentHash='" + contentHash + '\'' +
                ", contentList=" + contentList +
                ", endOfBlock=" + endOfBlock +
                '}';
    }
}
