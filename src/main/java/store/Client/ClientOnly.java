package store.Client;

import Blockchain.SmartContractConnection;
import chord.Chord;
import chord.SendReceiveGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import store.helper.HashSHA256StoreHelper;
import store.helper.RPCFunctions;
import store.helper.SolidityHelper;
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
    static SmartContractConnection smartContractConnection;
    static String ownerName;
    static String myIP;
    static String key;


    public ClientOnly() {
        this.fileDetails= new HashMap<>();
        files=new HashMap<>();
        cL=new ArrayList<>();
    }

    public static void main(String[] args) {

        if(args.length!=4){
            System.out.println("need 4 arguments \n" +
                    "1. port\n" +
                    "2. contract address \n" +
                    "3. credentials \n" +
                    "4. network ip" +
                    "");
            return;
        }

        Thread serverThread = new Thread(() ->{
            Server server = ServerBuilder.forPort(Integer.parseInt(args[0])).addService(new ClientOnly()).build();
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
        myIP=args[0];
        smartContractConnection = new SmartContractConnection(args[1],args[2],args[3]);
        ownerName = smartContractConnection.getCredentials().getAddress();
        System.out.println("Enter a secret key to encrypt data");
        Scanner sc1 = new Scanner(System.in);
        key = sc1.next();

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
            System.out.println("6. Delete a file using its name");
            int option = sc.nextInt();
            String nodeAddr=null;
            switch (option) {
                // Case
//1682926120377
                case 1:
                    System.out.println("Enter the file to be stored ( with absolute path) ");
                    String file = sc.next();
                    FileDetails fD = null;
//                    System.out.println("Enter an existing arbitrary node  ");
//                     nodeAddr = sc.next();
                    try {
                        //fD = TreeStructure.getFileDetails(file);
                        fD = TreeStructure.getFileDetailsEncrypted(file,key);
                        if(files.containsKey(fD.getFileName())){
                            String temp = fD.getFileName()+"-"+2;
                            fD.setFileName(temp);
                        }
//                        long timeBeforeSolidity = System.currentTimeMillis();

                        //fD = TreeStructure.encryptFileBits(fD,key);
                        nodeAddr =SolidityHelper.storeFileSolidity(fD.getContentList(),ownerName,smartContractConnection);
                        //nodeAddr="10.0.0.30:9000";
                        long timeAfterSolidity = System.currentTimeMillis();
                        //long timeToContract = timeAfterSolidity-timeBeforeSolidity;
                        System.out.println("time to store on contract: "+timeAfterSolidity);
                        RPCFunctions.sendFileForStorageOnTheFS(nodeAddr,fD,null,ownerName);
                        long timeAfterStorage = System.currentTimeMillis();
                        long finalTime = timeAfterStorage-timeAfterSolidity;
                        System.out.println("time to store after contract: "+finalTime);
                        long finalT = timeAfterStorage-timeAfterSolidity;
                        System.out.println("total time:"+ finalT);
                        files.put(fD.getFileName(),fD.getHashOfFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        System.out.println("exception while storing details on smartcontract");
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
                        long timeS = System.currentTimeMillis();
                        System.out.println("retrieval started "+timeS);
                        if (fileNotPresentInMemory(contentID)) {

                            RPCFunctions.retrieveRequest("10.0.0.30:"+myIP, contentID, fileName.get(), nodeAddr,null);
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
                        long timeS = System.currentTimeMillis();
                        System.out.println("retrieval started "+timeS);
                       // if(fileNotPresentInMemory(files.get(nameOfFile))) {
                            RPCFunctions.retrieveRequest("10.0.0.30:"+myIP, files.get(nameOfFile), nameOfFile, nodeAddr,null);
                        //}else{
//                            try {
//                                reconstructFile(fileDetails.get(files.get(nameOfFile)).getContentList(),nameOfFile);
//
//                            } catch (IOException e) {
//                                throw new RuntimeException(e);
//                            }
                        //}

                    }else{
                        System.out.println("check the file name again");
                    }
                    break;
                case 4:
                    files.entrySet().forEach(x-> System.out.println(x.getKey()));
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
                case 6:
                    System.out.println("Enter the file name to be deleted");
                    String nameToBeDeleted = sc.next();
                    System.out.println("Enter an existing arbitrary node  ");
                    nodeAddr = sc.next();
                    if(files.containsKey(nameToBeDeleted)){

                        RPCFunctions.deleteRequest("10.0.0.30:8089", files.get(nameToBeDeleted), nameToBeDeleted, nodeAddr,null,ownerName,false);

                    }else{
                        System.out.println("check the file name again");
                    }
                    break;

                case 7:
                    System.out.println("Enter the file name");
                    String fileN = sc.next();
                    System.out.println("Enter an existing arbitrary node  ");
                    nodeAddr = sc.next();
                    if(files.containsKey(fileN)){
                        if(fileNotPresentInMemory(files.get(fileN))) {
                            RPCFunctions.retrieveRequest("10.0.0.30:8089", files.get(fileN), fileN, nodeAddr,null);
                        }else{
                            try {
                                reconstructFile(fileDetails.get(files.get(fileN)).getContentList(),fileN);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }else{
                        System.out.println("check the file name again");
                    }
                    payForTheFile(files.get(fileN));
                    break;

            }
        }while(!quit);
    }

    private void payForTheFile(String rootHash) {
        while(!fileDetails.containsKey(rootHash)){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            SolidityHelper.storeFileSolidity(fileDetails.get(rootHash).getContentList(),ownerName,smartContractConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
                    outputStream.write(HashSHA256StoreHelper.decrypt(x.getData(),key));
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
                cL=new ArrayList<>();
            } finally {
                fos.close();
            }
            long timeS = System.currentTimeMillis();
            System.out.println("retrieval ended "+timeS);
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

