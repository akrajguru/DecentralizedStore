package store.pojo;

import java.util.ArrayList;
import java.util.List;

public class DirectoryTree {

    List<DirectoryTree> childDirectories;
    DirectoryTree parent;
    List<String> files;
    String directoryName;
    String absolutePath;

    public DirectoryTree(String name) {
        this.directoryName=name;
        files= new ArrayList<>();
    }

    public DirectoryTree(List<DirectoryTree> childDirectories, DirectoryTree parent, List<String> files,String name) {
        this.childDirectories = childDirectories;
        this.parent = parent;
        this.files = files;
        this.directoryName = name;
    }

    public List<DirectoryTree> getChildDirectories() {
        return childDirectories;
    }

    public void setChildDirectories(List<DirectoryTree> childDirectories) {
        this.childDirectories = childDirectories;
    }

    public DirectoryTree getParent() {
        return parent;
    }

    public void setParent(DirectoryTree parent) {
        this.parent = parent;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }


}
