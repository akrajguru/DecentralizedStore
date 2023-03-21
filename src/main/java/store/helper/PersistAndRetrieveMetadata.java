package store.helper;

import store.pojo.Node;
import store.pojo.Storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PersistAndRetrieveMetadata {

    public static Storage getDataFromHash(String hash, Node node, boolean isContent) throws IOException {

        StringBuilder fileName = new StringBuilder();
        fileName.append(node.getAppPath());
        fileName.append("/");
        if(isContent){
            fileName.append("/content");
            fileName.append("/");
        }else{
            fileName.append("/fileDetails");
            fileName.append("/");
        }
        fileName.append(hash);
        List<String> data = Files.readAllLines(Paths.get(fileName.toString()));
        Storage storage = new Storage();
        if(isContent){
            storage.setContainsContent(true);
            storage.setFileName(data.get(0));
            storage.setEndOfBlock(Long.parseLong(data.get(3)));
            storage.setRootHash(data.get(1));
            storage.setContentHash(data.get(2));
            storage.setDataBytes(Base64.getDecoder().decode(data.get(4)));
        }else{
            storage.setContainsContent(false);
            storage.setFileName(data.get(0));
            storage.setSize(Long.parseLong(data.get(2)));
            storage.setRootHash(data.get(1));
            List<String> contentList = new ArrayList<>();
            if(data.size()>3){
                for(int i =3; i< data.size();i++){
                    contentList.add(data.get(i));
                }
                storage.setContentList(contentList);
            }
        }
        return storage;
    }

    public static String persistMetadataToFile(Storage storage, Node node) throws IOException {

        StringBuilder metaData = new StringBuilder();
        StringBuilder fileName = new StringBuilder();
        metaData.append(storage.getFileName());
        metaData.append("\n");
        metaData.append(storage.getRootHash());
        metaData.append("\n");
        Path path;
        if(storage.isContainsContent()){
            metaData.append(storage.getContentHash());
            metaData.append("\n");
            metaData.append(storage.getEndOfBlock());
            metaData.append("\n");
            metaData.append(Base64.getEncoder().encodeToString(storage.getDataBytes()));
            fileName.append(storage.getContentHash());
             path = Files.createDirectories(Paths.get(node.getAppPath()+"/content"));
        }else{
            metaData.append(storage.getSize());
            metaData.append("\n");
            for(String content: storage.getContentList()) {
                metaData.append(content);
                metaData.append("\n");
            }
            metaData.delete(metaData.length()-1,metaData.length());
            fileName.append(storage.getRootHash());
            path = Files.createDirectories(Paths.get(node.getAppPath()+"/fileDetails"));
        }

        String fileLocation = path.toString()+"/"+ fileName.toString();
        File file = new File(fileLocation);
        if(file.exists()){
            List<String> fileContents = Files.readAllLines(Paths.get(fileLocation));
            if(!fileContents.get(1).equals(storage.getRootHash())){
                System.out.println("hashes clashes");
            }
        }
        FileWriter writer = new FileWriter(file);
        writer.append(metaData);
        writer.close();
        return fileLocation;
    }

    public static List<Storage> getAllDataFromFileStore(Node node) throws IOException {
        StringBuilder fileName = new StringBuilder();
        fileName.append(node.getAppPath());
        List<Storage> storageList = new ArrayList<>();
        File dir = new File(fileName.toString());
        File[] directoryListing = dir.listFiles();
        recursiveFn(directoryListing,storageList, node);
        if(!storageList.isEmpty()) {
            return storageList;
        }
        return null;
    }

    static void recursiveFn(File[] dir, List<Storage> storageList,Node node) throws IOException {
        //iterate through directory
        for(int i =0; i<dir.length;i++) {
            // add to files list if it is a file
            if (dir[i].isFile()) {
                if(dir[i].getPath().contains("/content")) {
                    //System.out.println(dir[i]);
                    storageList.add(getDataFromHash(dir[i].getName(),node,true));
                }else if(dir[i].getPath().contains("/fileDetails")){
                    storageList.add(getDataFromHash(dir[i].getName(),node,false));
                }
            } else if (dir[i].isDirectory()) {
                //recursively call fucntion to get files from directory
                recursiveFn(dir[i].listFiles(), storageList,node);
            }
        }
    }

}
