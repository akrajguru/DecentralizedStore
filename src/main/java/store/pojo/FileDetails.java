package store.pojo;

import java.util.List;

public class FileDetails {

    String fileName;
    long fileSize;
    String hashOfFile;
    List<Content> contentList;

    public FileDetails(String fileName, long fileSize, String hashOfFile, List<Content> contentList) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.hashOfFile = hashOfFile;
        this.contentList = contentList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getHashOfFile() {
        return hashOfFile;
    }

    public void setHashOfFile(String hashOfFile) {
        this.hashOfFile = hashOfFile;
    }

    public List<Content> getContentList() {
        return contentList;
    }

    public void setContentList(List<Content> contentList) {
        this.contentList = contentList;
    }

    @Override
    public String toString() {
        return "FileDetails{" +
                "fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", hashOfFile='" + hashOfFile + '\'' +
                '}';
    }
}
