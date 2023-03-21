package store.pojo;

public class Chunk {

    String hash;
    byte[] content;
    long endOfBlock;
    String rootHash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getEndOfBlock() {
        return endOfBlock;
    }

    public void setEndOfBlock(long endOfBlock) {
        this.endOfBlock = endOfBlock;
    }

    public String getRootHash() {
        return rootHash;
    }

    public void setRootHash(String rootHash) {
        this.rootHash = rootHash;
    }
}
