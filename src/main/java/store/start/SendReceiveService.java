package store.start;

import chord.Chord;
import chord.SendReceiveGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import store.Client.Client;
import store.helper.CalcHelper;
import store.helper.HashSHA256StoreHelper;
import store.helper.PersistAndRetrieveMetadata;
import store.helper.RPCFunctions;
import store.pojo.Content;
import store.pojo.FileDetails;
import store.pojo.Node;
import store.pojo.Storage;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SendReceiveService extends SendReceiveGrpc.SendReceiveImplBase {

    Node node;
    Map<String, FileDetails> fileDetailList;
    List<Content> cL;
    public SendReceiveService(Node node){
        this.node=node;
        cL = new ArrayList<>();
        fileDetailList= new HashMap<>();
        Client client = new Client(node, fileDetailList);
        //client.start();
    }


    @Override
    public void sendFD(Chord.FDRequest request, StreamObserver<Chord.FDResponse> responseObserver) {
        System.out.println(" received FD request for:"+ request.getFileName() +" in "+ node.getIpAddress());
        Storage storage=null;
        String file=null;
        String fileName=request.getFileName();
        long size = request.getFileSiz();
        String hash = request.getFileRootHash();
        List<String> contentList = request.getDataList();
        storage = new Storage(fileName,size,hash, contentList);
        Chord.FDResponse response = null;
        int res=2;
        List<Chord.fileContent> fcList = new ArrayList<>();
        Chord.fileContent fc = Chord.fileContent.newBuilder().setIsContent(false).setFileName(fileName).setFileSize(size).setRootHash(hash).setOwner(request.getOwner()).addAllDataFD(contentList).build();
        fcList.add(fc);
        if (!node.getIpAddress().equals(node.getSuccessor().getIpAddress())) {
            try {
                file = PersistAndRetrieveMetadata.persistMetadataToFile(storage, node, "primary",request.getOwner());
                res=1;
            } catch (Exception e) {
                e.printStackTrace();
            }
//            if (file != null) {
//                res = RPCFunctions.replicateCall(node, false, fcList, 1, node.getIpAddress(), node.getSuccessor().getIpAddress());
//                res = RPCFunctions.replicateCall(node, false, fcList, 2, node.getIpAddress(), node.getPredecessor().getIpAddress());
//            }
        }

        response = Chord.FDResponse.newBuilder().setResp(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendBytes(Chord.BytesRequest request, StreamObserver<Chord.BytesResponse> responseObserver) {
        //System.out.println(" received sendByte req for:"+ request.getgetFileName() +" in "+ node.getIpAddress());

        String file=null;
        List<Storage> storageList = new ArrayList<>();
        for(Chord.fileContent fC : request.getFileContentList()){
            Storage storage = new Storage(fC.getFileName(),fC.getRootHash(), fC.getDataContent().toByteArray(), fC.getBlockHash(),  fC.getEndOfBlock());
            storageList.add(storage);
        }
        try {
            for(Storage store: storageList) {
                file = PersistAndRetrieveMetadata.persistMetadataToFile(store, node, "primary",request.getOwner());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        int res=1;
//        if(file!=null){
//            res = RPCFunctions.replicateCall(node, true, request.getFileContentList(), 1, node.getIpAddress(), node.getSuccessor().getIpAddress());
//            res = RPCFunctions.replicateCall(node, true, request.getFileContentList(), 2, node.getIpAddress(), node.getPredecessor().getIpAddress());
//        }
        Chord.BytesResponse response = Chord.BytesResponse.newBuilder().setResp(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void retrieveFileRequest(Chord.RetRequest request, StreamObserver<Chord.RetResponse> responseObserver) {
        System.out.println(" received retrieve request for:"+ request.getFileName() +" in "+ node.getIpAddress());
        String fileName =request.getFileName();
        String rootHash = request.getHash();
        String  clientIP= request.getClientIP();
        Storage store=null;
        Chord.RetResponse.Builder resp = Chord.RetResponse.newBuilder();
        Node node1 = RPCFunctions.findSuccessorCall(node.getIpAddress(), request.getHash(),null);
        if(!node1.getIpAddress().equals(node.getIpAddress())) {
            retrieveRequest(store.getRootHash(), store.getFileName(), node1.getIpAddress());
        }else {
            if (!request.getIsContent()) {
                try {
                    store = PersistAndRetrieveMetadata.getDataFromHash(rootHash, node, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!store.isContainsContent()) {
                    for (String content : store.getContentList()) {
                        forwardRequestToRespectiveServers(fileName, clientIP, content);
                    }
                }
            } else {
                try {
                    store = PersistAndRetrieveMetadata.getDataFromHash(rootHash, node, true);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                sendDataToOwner(fileName, clientIP, store);
            }
        }
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    private static void sendDataToOwner(String fileName, String clientIP, Storage store) {
        System.out.println(" send data to owners");
        ManagedChannel channel;
        Chord.sendDataToOwner retRequest;
        Chord.acknowledgement resp;
        channel = ManagedChannelBuilder.forTarget(clientIP)
                .usePlaintext()
                .build();
        SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
        retRequest = Chord.sendDataToOwner.newBuilder().setFileName(fileName).setData(ByteString.copyFrom(store.getDataBytes())).
                setEndOfBlock(store.getEndOfBlock()).setRootHash(store.getRootHash()).setContentHash(store.getContentHash()).build();
        resp = sRBlockingStub.sendData(retRequest);
        channel.shutdown();
    }

    private void forwardRequestToRespectiveServers(String fileName, String clientIP, String content) {

        ManagedChannel channel = null;
        Chord.RetResponse resp;
        Chord.RetRequest retRequest;
        try {
            String succ = node.findSuccessor(CalcHelper.getBigInt(content), node);
            channel = ManagedChannelBuilder.forTarget(succ)
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
            retRequest = Chord.RetRequest.newBuilder().setFileName(fileName).setHash(content).
                    setClientIP(clientIP).setIsContent(true).build();
            resp = sRBlockingStub.retrieveFileRequest(retRequest);
        }catch (Exception e){

        }finally {
            channel.shutdown();
        }
    }

    @Override
    public void sendData(Chord.sendDataToOwner request, StreamObserver<Chord.acknowledgement> responseObserver) {
        FileDetails fD=null;
        if(fileDetailList.containsKey(request.getRootHash())){
            fD = fileDetailList.get(request.getRootHash());

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

    @Override
    public void replicate(Chord.sendReplica request, StreamObserver<Chord.replicaAck> responseObserver) {

        String file = null;
        Storage storage=null;
        Chord.replicaAck response=null;
        int replicateForward = request.getReplicateFurther();
//        if(request.getReplicateFurther()==2 && request.getPrimary().equals(node.getSuccessor().getIpAddress())){
//            response = Chord.replicaAck.newBuilder().setResponse(2).build();
//        }else {
        List<Chord.fileContent> fCList = request.getFileContentList();
        for(Chord.fileContent fC: fCList) {
            if (!fC.getIsContent()) {
                long size = fC.getFileSize();
                List<String> contentList = fC.getDataFDList();
                storage = new Storage(fC.getFileName(), size, fC.getRootHash(), contentList);
                try {
                    if (replicateForward == 1) {
                        file = PersistAndRetrieveMetadata.persistMetadataToFile(storage, node, "replica1",fC.getOwner());
                    } else if (replicateForward == 2) {
                        file = PersistAndRetrieveMetadata.persistMetadataToFile(storage, node, "replica2",fC.getOwner());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String cHash = fC.getBlockHash();
                long eob = fC.getEndOfBlock();
                byte[] byteArray = fC.getDataContent().toByteArray();
                storage = new Storage(fC.getFileName(), fC.getRootHash(), byteArray, cHash, eob);
                try {
                    if (replicateForward == 1) {
                        file = PersistAndRetrieveMetadata.persistMetadataToFile(storage, node, "replica1",fC.getOwner());
                    } else if (replicateForward == 2) {
                        file = PersistAndRetrieveMetadata.persistMetadataToFile(storage, node, "replica2",fC.getOwner());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//            --replicateForward;
//            if (replicateForward > 0) {
//                if (file != null) {
//                    RPCFunctions.replicateCall(node, request.getIsContent(), storage, replicateForward, request.getPrimary(), node.getIpAddress());
//                }
//            }
            response = Chord.replicaAck.newBuilder().setResponse(1).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    private void retrieveRequest(String contentId, String fileName, String addressOfArbNode) {
        Node node = RPCFunctions.findSuccessorCall(addressOfArbNode, contentId,null);
        ManagedChannel channel = ManagedChannelBuilder.forTarget(node.getIpAddress())
                .usePlaintext()
                .build();
        System.out.println("looking for file at "+ node.getIpAddress());
        SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
        Chord.RetRequest retRequest = Chord.RetRequest.newBuilder().setFileName(fileName).setHash(contentId).
                setClientIP("10.0.0.30:8085").setIsContent(false).build();
        Chord.RetResponse resp = sRBlockingStub.retrieveFileRequest(retRequest);
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
                fos = new FileOutputStream(new File(node.getAppPath()+"/resources/"+gilename));
                outputStream.writeTo(fos);
            } catch(IOException ioe) {
                // Handle exception here
                ioe.printStackTrace();
            } finally {
                fos.close();
            }
            System.out.println("File can be found at: " +node.getAppPath()+"/resources/"+gilename);
            tryToOpenTheFile(node.getAppPath()+"/resources/"+gilename);
            cL=new ArrayList<>();
            fileDetailList.put(rootHash, new FileDetails(gilename,0,rootHash,newList));
        }
//        else{
//            System.out.println(" root hash exisitng: " +rootHash);
//            System.out.println(" root hash exisitng: " +rHashChecker);
//        }
    }
    @Override
    public void replicateAsSuccessorDeleted(Chord.replicateMyFiles request, StreamObserver<Chord.replicateMyFilesAck> responseObserver) {
        int replicateFurther= request.getCreate().getReplicateFurther();
        String predecessor = request.getCreate().getMeYourPredecessor();
        node.setPredecessor(new Node(predecessor));
        Chord.replicateMyFilesAck response = null;
//        if(node.getPredecessor().getIpAddress().equals(node.getSuccessor().getIpAddress())){
//            response = Chord.replicateMyFilesAck.newBuilder().setAck(2).build();
//            responseObserver.onNext(response);
//            responseObserver.onCompleted();
//        }
//        node.getLogger().writeLog("info","Predecessor updated to "+predecessor,null);
//        --replicateFurther;
//
//        if(replicateFurther==2) {
//            node.getStorageInfo().getServerStoreInformation().get("primary").addAll(node.getStorageInfo().getServerStoreInformation().get("replica1"));
//            node.getStorageInfo().getServerStoreInformation().put("replica1",  node.getStorageInfo().getServerStoreInformation().get("replica2"));
//            node.getStorageInfo().getServerStoreInformation().put("replica2", new ArrayList<>());
//            persistData(request.getDataList(),"replica2");
//            List<Storage> storageList=null;
//            try {
//                List<String> list = node.getStorageInfo().getServerStoreInformation().get("replica1");
//                storageList= PersistAndRetrieveMetadata.retrieveFilesAsAList(list, node);
//                int ack = RPCFunctions.replicateAfterDeletion(node, request.getCreate().getDeletedNode(), replicateFurther,storageList);
//                if(ack==1){
//                    response = Chord.replicateMyFilesAck.newBuilder().setAck(1).build();
//                }else{
//                    response = Chord.replicateMyFilesAck.newBuilder().setAck(0).build();
//                }
//            }catch (Exception e){
//                node.getLogger().writeLog("info","error in replicateAsSuccessorDeleted line calling retrieveFilesAsAList",e);
//            }
//        }else if(replicateFurther==1){
//            List<Storage> storageList=null;
//            List<String> replica2 = new ArrayList<>();
//            replica2 = node.getStorageInfo().getServerStoreInformation().get("replica2");
//            node.getStorageInfo().getServerStoreInformation().get("replica1").addAll(replica2);
//            node.getStorageInfo().getServerStoreInformation().put("replica2", new ArrayList<>());
//            persistData(request.getDataList(),"replica2");
//            try {
//                response = Chord.replicateMyFilesAck.newBuilder().setAck(1).build();
//                storageList = PersistAndRetrieveMetadata.retrieveFilesAsAList(replica2, node);
//                int ack = RPCFunctions.replicateAfterDeletion(node, request.getCreate().getDeletedNode(), replicateFurther, storageList);
//                if (ack == 1) {
//                    response = Chord.replicateMyFilesAck.newBuilder().setAck(1).build();
//                } else {
//                    response = Chord.replicateMyFilesAck.newBuilder().setAck(0).build();
//                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }else if(replicateFurther==0){
//            persistData(request.getDataList(),"replica2");
//            response = Chord.replicateMyFilesAck.newBuilder().setAck(1).build();
//        }

        node.getStorageInfo().getServerStoreInformation().get("primary1").addAll(node.getStorageInfo().getServerStoreInformation().get("replica1"));
        node.getStorageInfo().getServerStoreInformation().put("replica1", new ArrayList<String>());
        response = Chord.replicateMyFilesAck.newBuilder().setAck(1).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void checkIFReplicasPresent(Chord.replicaFileNames request, StreamObserver<Chord.filesNotPresent> responseObserver) {
        Chord.filesNotPresent response = null;
        List<String> predFiles = request.getFileNamesList();

        if(request.getYourPredecessor()!=null && node.getPredecessor().getIpAddress().equals(request.getYourPredecessor())){
         List<String> Rep1Files =    node.getStorageInfo().getServerStoreInformation().get("replica1");
         List<String> newReps = predFiles.stream().filter(x-> Rep1Files.contains(x)).collect(Collectors.toList());
         List<String> retList =new ArrayList<>();
         if(!newReps.isEmpty()) {
             for(String s: newReps) {
                 if(!predFiles.contains(s)) {
                     retList.add(s);
                 }
             }
             response= Chord.filesNotPresent.newBuilder().addAllFilesNotPresent(retList).build();
         }else{
             response= Chord.filesNotPresent.newBuilder().addAllFilesNotPresent(predFiles).build();
         }

            node.getStorageInfo().getServerStoreInformation().put("replica1",newReps);
        }else if(request.getYourSuccessor()!=null && request.getYourSuccessor().equals(node.getSuccessor().getIpAddress())){
            List<String> Rep2Files =    node.getStorageInfo().getServerStoreInformation().get("replica2");
            List<String> newReps = predFiles.stream().filter(x-> Rep2Files.contains(x)).collect(Collectors.toList());
            List<String> retList =new ArrayList<>();
            if(!newReps.isEmpty()) {
                for(String s: newReps) {
                    if(!predFiles.contains(s)) {
                        retList.add(s);
                    }
                }
                response= Chord.filesNotPresent.newBuilder().addAllFilesNotPresent(retList).build();
            }else{
                response= Chord.filesNotPresent.newBuilder().addAllFilesNotPresent(predFiles).build();
            }

            node.getStorageInfo().getServerStoreInformation().put("replica2",newReps);
        }else{
            response= Chord.filesNotPresent.newBuilder().setError(1).build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void replicateAbsentFiles(Chord.sendFileData request, StreamObserver<Chord.replicaAck> responseObserver) {

        Chord.replicaAck resp =null;
        if(request.getYourPredecessor()!=null && node.getPredecessor().getIpAddress().equals(request.getYourPredecessor())){
            persistData(request.getDataList(), "replica1");
            resp = Chord.replicaAck.newBuilder().setResponse(1).build();

        }else if(request.getYourSuccessor()!=null && request.getYourSuccessor().equals(node.getSuccessor().getIpAddress())){
            persistData(request.getDataList(), "replica2");
            resp = Chord.replicaAck.newBuilder().setResponse(1).build();
        }else{
            resp = Chord.replicaAck.newBuilder().setResponse(0).build();
        }

        responseObserver.onNext(resp);
        responseObserver.onCompleted();

    }

    private void persistData( List<Chord.fileContent> dataList, String typeOfFile) {
        for (Chord.fileContent fC : dataList) {
            Storage storage = null;
            if (fC.getIsContent()) {
                storage = new Storage(fC.getFileName(), fC.getRootHash(), fC.getDataContent().toByteArray(), fC.getBlockHash(), fC.getEndOfBlock());
            } else {
                storage = new Storage(fC.getFileName(), fC.getFileSize(), fC.getRootHash(), fC.getDataFDList());
            }
            try {
                PersistAndRetrieveMetadata.persistMetadataToFile(storage, node, typeOfFile,fC.getOwner());
            } catch (Exception e) {
                node.getLogger().writeLog("error", " error in replicateAsSuccessorDeleted", e);
            }
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
}
