import chord.Chord;
import chord.MyServiceGrpc;
import chord.SendReceiveGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import store.helper.CalcHelper;
import store.helper.RPCFunctions;
import store.helper.TreeStructure;
import store.pojo.Chunk;
import store.pojo.Content;
import store.pojo.FileDetails;
import store.pojo.Node;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StoreClientTester {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        boolean quit =false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("1. Store a file");
            System.out.println("2. get a file using its content ID");
            int option = sc.nextInt();
            switch (option) {

                // Case
                case 1:
                    System.out.println("Enter the file to be stored ( with absolute path) ");
                    String file = sc.next();
                    FileDetails fD = getFileDetails(file);
                    System.out.println("Enter the host address and port of the arbitraty node");
                    String host = sc.next();
                    sendFileForStorageOnTheFS(host,fD);
                case 2:
                    System.out.println("Enter the port number of the Server");
                    String addressOfArbNode = sc.next();
                    System.out.println("Enter the content id ");
                    String contentID = sc.next();
                    System.out.println("Enter the file name");
                    String fileName = sc.next();
                    retrieveRequest( contentID,fileName,addressOfArbNode);

            }
        }while(!quit);

    }

    private static void retrieveRequest(String contentId, String fileName,String addressOfArbNode) {
        Node node = RPCFunctions.findSuccessorCall(addressOfArbNode, contentId);
        ManagedChannel channel = ManagedChannelBuilder.forTarget(node.getIpAddress())
                .usePlaintext()
                .build();
        System.out.println("looking for file at "+ node.getIpAddress());
        SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
        Chord.RetRequest retRequest = Chord.RetRequest.newBuilder().setFileName(fileName).setHash(contentId).
                setClientIP("10.0.0.30:8085").setIsContent(false).build();
        Chord.RetResponse resp = sRBlockingStub.retrieveFileRequest(retRequest);
    }

    private static FileDetails getFileDetails(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb= new StringBuilder();
        List<Content> contentList= TreeStructure.createNodeList(chunks,sb);
        String[] arr = filePath.split("/");
        String fileName = arr[arr.length-1];
        FileDetails fD = new FileDetails(fileName,byteArray.length, sb.toString(),  contentList);
        return fD;
    }

    private static void sendFileForStorageOnTheFS(String nodeAddr, FileDetails fD) {


        Node node = RPCFunctions.findSuccessorCall(nodeAddr, fD.getHashOfFile());
        ManagedChannel channel1 = ManagedChannelBuilder.forTarget(node.getIpAddress())
                .usePlaintext()
                .build();
        SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel1);
        List<String> fDList = fD.getContentList().stream().map(x -> x.getHash()).collect(Collectors.toList());
        Chord.FDRequest requestFD = Chord.FDRequest.newBuilder().setFileName(fD.getFileName()).setFileSiz(fD.getFileSize()).
                setFileRootHash(fD.getHashOfFile()).addAllData(fDList).build();
        Chord.FDResponse resp = sRBlockingStub.sendFD(requestFD);
        Map<String,String> senderMap = new LinkedHashMap<>();
        senderMap.put(node.getIpAddress(),"");
        if(resp.getResp()==1){
            for(Content content: fD.getContentList()){
                Chord.BytesResponse sentResp = sendContentAndGetBytesResponse(fD, node, content,nodeAddr);
                while(sentResp.getResp()!=1){
                    sentResp=sendContentAndGetBytesResponse(fD, node, content,nodeAddr);
                }
            }
        }
        System.out.println(" File saved:"+ fD);
    }

    private static Chord.BytesResponse sendContentAndGetBytesResponse(FileDetails fD, Node node, Content content,String nodeAddr) {
        Node nodeSucc = RPCFunctions.findSuccessorCall(nodeAddr, content.getHash());
        ManagedChannel chnl = ManagedChannelBuilder.forTarget(nodeSucc.getIpAddress())
                .usePlaintext()
                .build();
        SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
        Chord.BytesRequest sendBytes = Chord.BytesRequest.newBuilder().setRootHash(fD.getHashOfFile()).setFileName(fD.getFileName()).setBlockHash(content.getHash())
                .setEndOfBlock(content.getEndByte()).setData(ByteString.copyFrom(content.getData())).build();
        Chord.BytesResponse sentResp = sRBStub.sendBytes(sendBytes);
        return sentResp;
    }
    }

