package store.helper;

import org.bouncycastle.util.Arrays;
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
import java.util.stream.Collectors;

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
       // fileName.append("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/null/content/1976a69b5a67d0b907c1767afd48a2fb2446ab3883e05acc7ee99ae6caa3799c");
        List<String> data = Files.readAllLines(Paths.get(fileName.toString()));
        Storage storage = new Storage();
        if(isContent){
            storage.setContainsContent(true);
            storage.setFileName(data.get(0));
            storage.setEndOfBlock(Long.parseLong(data.get(4)));
            storage.setRootHash(data.get(1));
            storage.setContentHash(data.get(3));
            storage.setDataBytes(Base64.getDecoder().decode(data.get(5)));
            storage.setOwner(data.get(2));
        }else{
            storage.setContainsContent(false);
            storage.setFileName(data.get(0));
            storage.setSize(Long.parseLong(data.get(3)));
            storage.setOwner(data.get(2));
            storage.setRootHash(data.get(1));
            List<String> contentList = new ArrayList<>();
            if(data.size()>4){
                for(int i =4; i< data.size();i++){
                    contentList.add(data.get(i));
                }
                storage.setContentList(contentList);
            }
        }
        return storage;
    }

    public static void deleteFileForOwner(String hash, Node node, boolean isContent,String owner) throws IOException {
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
        //fileName.append("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/StorageMetadata-79acb9d0c00c0244daf7b95993a27c587a682e286b309712e58e6839dfa8020b/fileDetails/38bb8ad7a3df0184cc667b1eb3b42f9becacaf6f280107d38abf826634ba5480");

        Path filePath = Paths.get(fileName.toString());
        List<String> data = Files.readAllLines(filePath);
        String[] owners = data.get(2).split(",");
        if(owners.length==1 && owners[0].equals(owner)) {
            //Path p = Files.move(filePath,Paths.get("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/StorageMetadata-79acb9d0c00c0244daf7b95993a27c587a682e286b309712e58e6839dfa8020b/"+hash+"deleted"));
            Files.delete(filePath);
            node.getStorageInfo().getServerStoreInformation().get("primary").remove(hash);

            return ;
        }
        StringBuilder remainingOwners = new StringBuilder();
        for(String ownerIt: owners){
            if(!owner.equals(ownerIt)){
                remainingOwners.append(ownerIt);
                remainingOwners.append(",");
            }
        }
        remainingOwners.replace(remainingOwners.length()-1,remainingOwners.length(),"");
        data.set(2,remainingOwners.toString());
        updateFile(fileName.toString(),data);

    }
    private static void updateFile(String file, List<String> arrData)
            throws IOException {
        FileWriter writer = new FileWriter(file);
        int size = arrData.size();
        for (int i=0;i<size;i++) {
            String str = arrData.get(i).toString();
            writer.write(str);
            if(i < size-1)//This prevent creating a blank like at the end of the file**
            writer.write("\n");
        }
        writer.close();
    }

    public static String persistMetadataToFile(Storage storage, Node node,String typeOfFile,String owner) throws IOException {

        StringBuilder metaData = new StringBuilder();
        StringBuilder fileName = new StringBuilder();
        metaData.append(storage.getFileName());
        metaData.append("\n");
        metaData.append(storage.getRootHash());
        metaData.append("\n");
        Path path = null;
        boolean write=true;
        if(storage.isContainsContent()){
            path = Files.createDirectories(Paths.get(node.getAppPath()+"/content"));
            fileName.append(storage.getContentHash());
        }else{
            path = Files.createDirectories(Paths.get(node.getAppPath()+"/fileDetails"));
            fileName.append(storage.getRootHash());
        }
        String fileLocation = path.toString()+"/"+ fileName.toString();
        File file = new File(fileLocation);
        if(typeOfFile.equals("primary")) {
            write = checkIfFileForSameOwnerExists(owner, metaData, fileLocation, file, write);
        }else{
            metaData.append(owner);
            metaData.append("\n");
        }
        if(storage.isContainsContent()){
            metaData.append(storage.getContentHash());
            metaData.append("\n");
            metaData.append(storage.getEndOfBlock());
            metaData.append("\n");
            metaData.append(Base64.getEncoder().encodeToString(storage.getDataBytes()));
            metaData.append("\n");

        }else{
            metaData.append(storage.getSize());
            metaData.append("\n");
            for(String content: storage.getContentList()) {
                metaData.append(content);
                metaData.append("\n");
            }
            //path = Files.createDirectories(Paths.get("abc"+"/fileDetails"));
        }

        if(!node.getStorageInfo().getServerStoreInformation().get(typeOfFile).contains(fileName.toString())) {
            node.getStorageInfo().getServerStoreInformation().get(typeOfFile).add(fileName.toString());
        }

        FileWriter writer = new FileWriter(file);
        writer.append(metaData);
        writer.close();
        if(write) {
            if (typeOfFile.equals("primary")) {
                if (node.getForceReplication() == null) {
                    node.setForceReplication(new ArrayList<>());
                }
                node.getForceReplication().add(fileName.toString());
            }
        }

        return fileLocation;
    }

    private static boolean checkIfFileForSameOwnerExists(String owner, StringBuilder metaData, String fileLocation, File file, boolean write) throws IOException {
        if(file.exists()) {
            List<String> fileContents = Files.readAllLines(Paths.get(fileLocation));
            String owners= fileContents.get(2);
            String[] ownerArr = owners.split(",");
            if (ownerArr.length > 0) {
                for (String s : ownerArr) {
                    if (s.equals(owner) || owner.isBlank()) {
                        write = false;
                    }
                }
                if (write) {
                    owner +=  ","+owners ;
                }else{
                    owner=owners;
                }
            }
        }
        metaData.append(owner);
        metaData.append("\n");
        return write;
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

    public static List<Storage> retrieveFilesAsAList(List<String> fileNames,Node node) throws IOException {
        List<Storage> allFilesList = getAllDataFromFileStore(node);
       // List<Storage> list = allFilesList.stream().filter(x -> fileNames.contains(x.getFileName())).collect(Collectors.toList());
        List<Storage> sendList = new ArrayList<>();
    if(allFilesList!=null && !allFilesList.isEmpty()) {
        for (Storage storage : allFilesList) {
            if (storage.isContainsContent()) {
                if (fileNames.contains(storage.getContentHash())) {
                    sendList.add(storage);
                }
            } else {
                if (fileNames.contains(storage.getRootHash())) {
                    sendList.add(storage);
                }
            }
        }
    }
        return sendList;
    }

}
