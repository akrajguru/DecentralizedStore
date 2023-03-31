package store.start;

import chord.Chord;
import chord.MyServiceGrpc;
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
        client.start();
    }

    @Override
    public void sendFD(Chord.FDRequest request, StreamObserver<Chord.FDResponse> responseObserver) {
        System.out.println(" received FD request for:"+ request.getFileName() +" in "+ node.getIpAddress());
        String fileName=request.getFileName();
        long size = request.getFileSiz();
        String hash = request.getFileRootHash();
        List<String> contentList = request.getDataList();
        Storage storage = new Storage(fileName,size,hash,   contentList);
        try {
            PersistAndRetrieveMetadata.persistMetadataToFile(storage, node);
        }catch (Exception e){
            e.printStackTrace();
        }
        Chord.FDResponse response = Chord.FDResponse.newBuilder().setResp(1).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendBytes(Chord.BytesRequest request, StreamObserver<Chord.BytesResponse> responseObserver) {
        System.out.println(" received sendByte req for:"+ request.getFileName() +" in "+ node.getIpAddress());
        String fN= request.getFileName();
        String cHash = request.getBlockHash();
        long eob=request.getEndOfBlock();
        String rootHash = request.getRootHash();
        byte[] bs = request.getData().toByteArray();
        Storage storage = new Storage(fN,rootHash, bs, cHash,  eob);
        try {
            PersistAndRetrieveMetadata.persistMetadataToFile(storage, node);
        }catch (Exception e){
            e.printStackTrace();
        }
        Chord.BytesResponse response = Chord.BytesResponse.newBuilder().setResp(1).build();
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

    private void retrieveRequest(String contentId, String fileName,String addressOfArbNode) {
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
