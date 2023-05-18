package store.Client;

import store.helper.RPCFunctions;
import store.helper.TreeStructure;
import store.pojo.Content;
import store.pojo.FileDetails;
import store.pojo.Node;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class Client extends Thread {

    Node node;
    Map<String, FileDetails> fileDetails;
    Map<String,String> files;


    public Client(Node node, Map<String, FileDetails> fileDetailList) {
        this.node=node;
        this.fileDetails=fileDetailList;
        files=new HashMap<>();
    }

    @Override
    public void run() {

        boolean quit =false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. Store a file");
            System.out.println("2. get a file using its content ID");
            System.out.println("3. get file using exact file name");
            System.out.println("4. Show my files stored");
            System.out.println("5. Get a file using shared content ID");
            int option = sc.nextInt();
            switch (option) {
                // Case
                case 1:
                    System.out.println("Enter the file to be stored ( with absolute path) ");
                    String file = sc.next();
                    FileDetails fD = null;
//                    try {
//                        fD = TreeStructure.getFileDetails(file);
//                        if(files.containsKey(fD.getFileName())){
//                            String temp = fD.getFileName()+"-"+2;
//                            fD.setFileName(temp);
//                        }
//                        RPCFunctions.sendFileForStorageOnTheFS(node.getIpAddress(),fD,node,node.getIpAddress());
//                        files.put(fD.getFileName(),fD.getHashOfFile());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    } catch (NoSuchAlgorithmException e) {
//                        throw new RuntimeException(e);
//                    }
                    break;
                case 2:
                    System.out.println("Enter the content id ");
                    String contentID = sc.next();
                    Optional<String> fileName = files
                            .entrySet()
                            .stream()
                            .filter(entry -> Objects.equals(entry.getValue(), contentID))
                            .map(Map.Entry::getKey)
                            .findFirst();
                    if(fileName.stream().count()>0) {
                        if (fileNotPresentInMemory(contentID)) {
                            RPCFunctions.retrieveRequest(node.getIpAddress(), contentID, fileName.get(), node.getIpAddress(),node);
                        } else {
                            try {
                                reconstructFile(fileDetails.get(contentID).getContentList(), fileName.get(), node);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }else{
                        System.out.println("check the content id again");
                    }
                    break;
                case 3:
                    System.out.println("Enter the file name");
                    String nameOfFile = sc.next();
                    if(files.containsKey(nameOfFile)){
                        if(fileNotPresentInMemory(files.get(nameOfFile))) {
                            RPCFunctions.retrieveRequest(node.getIpAddress(), files.get(nameOfFile), nameOfFile, node.getIpAddress(),node);
                        }else{
                            try {
                                reconstructFile(fileDetails.get(files.get(nameOfFile)).getContentList(),nameOfFile,node);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }else{
                        System.out.println("check the file name again");
                    }
                    break;
                case 4:
                    files.entrySet().forEach(x-> System.out.println(x.getValue()));
                    break;
                case 5:
                    System.out.println("Enter the shared content id");
                    String contentID2 = sc.next();
                    System.out.println("Enter any name for the file");
                    String fileName2 = sc.next();
                    RPCFunctions.retrieveRequest(node.getIpAddress(), contentID2,fileName2, node.getIpAddress(),node);
                    break;
            }
        }while(!quit);
    }

    private boolean fileNotPresentInMemory(String contentID) {
        if(fileDetails.containsKey(contentID)) return false;

        return true;
    }

    public static void reconstructFile(List<Content> content, String fileName,Node node) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        content.stream().forEach(x-> {
            try {
                outputStream.write(x.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(node.getAppPath()+"/resources/"+fileName);
            outputStream.writeTo(fos);
        } catch(IOException ioe) {
            // Handle exception here
            ioe.printStackTrace();
        } finally {
            fos.close();
        }
    }
}

