package store.Client;

import chord.Chord;
import chord.SendReceiveGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import store.helper.HashSHA256StoreHelper;
import store.helper.RPCFunctions;
import store.helper.TreeStructure;
import store.pojo.Content;
import store.pojo.FileDetails;
import store.pojo.Node;
import store.start.SendReceiveService;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;

public class ClientOnly  extends SendReceiveGrpc.SendReceiveImplBase {


    Map<String, FileDetails> fileDetails;
    Map<String,String> files;
    List<Content> cL;


    public ClientOnly() {
        this.fileDetails= new HashMap<>();
        files=new HashMap<>();
        cL=new ArrayList<>();
    }

    public static void main(String[] args) {
        Thread serverThread = new Thread(() ->{
            Server server = ServerBuilder.forPort(8089).addService(new ClientOnly()).build();
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();

                return;
            }
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        ClientOnly cO = new ClientOnly();
        cO.switchCase();
    }

    private void switchCase() {
        boolean quit =false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. Store a file");
            System.out.println("2. get a file using its content ID");
            System.out.println("3. get file using exact file name");
            System.out.println("4. Show my files stored");
            System.out.println("5. Get a file using shared content ID");
            int option = sc.nextInt();
            String nodeAddr=null;
            switch (option) {
                // Case

                case 1:
                    System.out.println("Enter the file to be stored ( with absolute path) ");
                    String file = sc.next();
                    FileDetails fD = null;
                    System.out.println("Enter an existing arbitrary node  ");
                     nodeAddr = sc.next();
                    try {
                        fD = TreeStructure.getFileDetails(file);
                        if(files.containsKey(fD.getFileName())){
                            String temp = fD.getFileName()+"-"+2;
                            fD.setFileName(temp);
                        }
                        RPCFunctions.sendFileForStorageOnTheFS(nodeAddr,fD,null,"8089");
                        files.put(fD.getFileName(),fD.getHashOfFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
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
                    System.out.println("Enter an existing arbitrary node  ");
                    nodeAddr = sc.next();
                    if(fileName.stream().count()>0) {
                        if (fileNotPresentInMemory(contentID)) {
                            RPCFunctions.retrieveRequest("10.0.0.30:8089", contentID, fileName.get(), nodeAddr,null);
                        } else {
                            try {
                                reconstructFile(fileDetails.get(contentID).getContentList(), fileName.get());

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
                    System.out.println("Enter an existing arbitrary node  ");
                    nodeAddr = sc.next();
                    if(files.containsKey(nameOfFile)){
                        if(fileNotPresentInMemory(files.get(nameOfFile))) {
                            RPCFunctions.retrieveRequest("10.0.0.30:8089", files.get(nameOfFile), nameOfFile, nodeAddr,null);
                        }else{
                            try {
                                reconstructFile(fileDetails.get(files.get(nameOfFile)).getContentList(),nameOfFile);

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
                    System.out.println("Enter a new name for the file");
                    String fileName2 = sc.next();
                    System.out.println("Enter an existing arbitrary node  ");
                    nodeAddr = sc.next();
                    RPCFunctions.retrieveRequest("10.0.0.30:8089", contentID2,fileName2, nodeAddr,null);
                    break;
            }
        }while(!quit);
    }

    @Override
    public void sendData(Chord.sendDataToOwner request, StreamObserver<Chord.acknowledgement> responseObserver) {
        FileDetails fD=null;
        if(fileDetails.containsKey(request.getRootHash())){
            fD = fileDetails.get(request.getRootHash());

        }else{
            fD = new FileDetails(request.getFileName(), 0, request.getRootHash(),new ArrayList<>());
        }
        fD.getContentList().add(new Content(request.getContentHash(),request.getEndOfBlock(),request.getData().toByteArray()));
        cL.add(new Content(request.getContentHash(),request.getEndOfBlock(),request.getData().toByteArray()));
        System.out.println(fD.getContentList());
        Chord.acknowledgement.Builder resp = Chord.acknowledgement.newBuilder();
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
        try {
            checkRoothash(request.getRootHash(),request.getFileName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void checkRoothash(String rootHash,String gilename) throws NoSuchAlgorithmException, IOException {
        //System.out.println("inside check roothash");
        List<Content> newList = new ArrayList<>();
        newList.addAll(cL);
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        Collections.sort(cL, (c1, c2)-> (int) (c1.getEndByte()- c2.getEndByte()));
        newList.stream().forEach(x->sb.append(x.getHash()));
        String rHashChecker = HashSHA256StoreHelper.createHashFromFileContent(sb.toString().getBytes(StandardCharsets.UTF_8));
        if(rHashChecker.equals(rootHash)){
            newList.stream().forEach(x-> {
                try {
                    outputStream.write(x.getData());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources/"+gilename));
                outputStream.writeTo(fos);
            } catch(IOException ioe) {
                // Handle exception here
                ioe.printStackTrace();
            } finally {
                fos.close();
            }
            System.out.println("File can be found at: " +"/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources/"+gilename);
            tryToOpenTheFile("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources/"+gilename);
            cL=new ArrayList<>();
            fileDetails.put(rootHash, new FileDetails(gilename,0,rootHash,newList));
        }
    }

    private void tryToOpenTheFile(String filePath) {
        {
            try {
                File file_open = new File(filePath);
                if (!Desktop.isDesktopSupported()) {
                    System.out.println("Desktop Support Not Present in the system.");
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                if (file_open.exists())
                    desktop.open(file_open);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private boolean fileNotPresentInMemory(String contentID) {
        if(fileDetails.containsKey(contentID)) return false;

        return true;
    }

    public static void reconstructFile(List<Content> content, String fileName) throws IOException {
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
            fos = new FileOutputStream("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources"+"/resources/"+fileName);
            outputStream.writeTo(fos);
        } catch(IOException ioe) {
            // Handle exception here
            ioe.printStackTrace();
        } finally {
            fos.close();
        }
    }
}

