package store.helper;

import chord.Chord;
import chord.NodeGrpc;
import chord.SendReceiveGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import store.pojo.Content;
import store.pojo.FileDetails;
import store.pojo.Node;
import store.pojo.Storage;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RPCFunctions {

    public static String getSuccessorOfNode(String finger1Node) {
        ManagedChannel channel=null;
        try {
            channel = ManagedChannelBuilder.forTarget(finger1Node).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.SuccessorOfNodeRequest request = Chord.SuccessorOfNodeRequest.newBuilder().build();
            Chord.SuccessorOfNodeResponse resp = stub.getSuccessorOfNode(request);
            if (resp.getSuccessor() != null) {
                //successorList.put(finger1Node, resp.getSuccessor());
                return resp.getSuccessor();
            }
           // System.out.println("predecessor call returned null from: " + finger1Node);

        }catch(Exception e){

            //updateFingerTableAfterException(finger1Node);
            //getSuccessorOfNode
            //channel.shutdown();
        } finally {
            channel.shutdown();
        }
        //return successorPort;
        return null;
    }
    public static Node closestPrecedingFingerCall(Node myNode, Node newNd, BigInteger id) {
        ManagedChannel channel =null;
        try {
             channel = ManagedChannelBuilder.forTarget(newNd.getIpAddress()).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.ClosestPFRequest request = Chord.ClosestPFRequest.newBuilder().setId(String.valueOf(id)).build();
            Chord.ClosestPFReply resp = stub.closestPrecedFinger(request);
            channel.shutdown();
            if(resp!=null && resp.getClosestPrecedingFinger()!=null) {
                Node node = new Node(resp.getClosestPrecedingFinger().getIpAddress());
                return node;
            }
        }catch(Exception e){
            System.out.println("exception in closestPrecedingFingerCall function");
            e.printStackTrace();
        }finally {
            channel.shutdown();
        }

            return myNode;
        }


    public static Node findSuccessorCall(String nodeD, String start1) {
        ManagedChannel channel =null;
        try {
             channel = ManagedChannelBuilder.forTarget(nodeD).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.SuccessorCallRequest request = Chord.SuccessorCallRequest.newBuilder().setId(start1).build();
            Chord.SuccessorCallReply resp = stub.successorCall(request);
            if(resp!=null && resp.getSuccessor()!=null) {
                Node node = new Node(resp.getSuccessor().getIpAddress());
                return node;
            }
           // System.out.println("successor call returned null from: " + nodeD);
        }catch(Exception e){
            System.out.println("exception in findSuccessorCall: "+ nodeD);

        }finally {
            channel.shutdown();
        }
        return null;
    }

    public static Node getPredecessorOfNode(Node succNode,Node myNode) {
        if(succNode.getIpAddress().equals(myNode.getIpAddress())) {
            return myNode.getPredecessor();
        }
        ManagedChannel channel=null;
        try {
             channel = ManagedChannelBuilder.forTarget(succNode.getIpAddress()).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.PredecessorOfNodeRequest request = Chord.PredecessorOfNodeRequest.newBuilder().build();
            Chord.PredecessorOfNodeReqponse resp = stub.getPredecessorOfNode(request);
            if (resp.getPredecessor() != null) return new Node(resp.getPredecessor());
            System.out.println("predecessor call returned null from: " + succNode.getIpAddress());
            channel.shutdown();
        }catch (Exception e){
            e.printStackTrace();
            //System.out.println("node:" + finger1Node+" has been deleted");
            //checkSuccessorsSucc(finger1Node);
        }finally {
            channel.shutdown();
        }
        return null;
    }

    public static void updateSuccessor(String nD,String successor) {
        //if(successor.equals())
        //System.out.println("updating successor: "+successor);
        ManagedChannel channel = ManagedChannelBuilder.forTarget(successor).usePlaintext().build();
        NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
        Chord.UpdateSPRequest req = Chord.UpdateSPRequest.newBuilder().setPredecessor(nD).build();
        Chord.UpdateSPResponse response =stub.updateSuccessorPredecessor(req);
        if(response.getResponse()==0) //System.out.println(successor+ " node's predecessor updated to:" + myPort);
            channel.shutdown();
    }

    public static Chord.BytesResponse sendDataContentBytesToNode(Storage store, String nodeAddr) {

        ManagedChannel chnl = ManagedChannelBuilder.forTarget(nodeAddr)
                .usePlaintext()
                .build();
        SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
        Chord.BytesRequest sendBytes = Chord.BytesRequest.newBuilder().setRootHash(store.getRootHash()).setFileName(store.getFileName()).setBlockHash(store.getContentHash())
                .setEndOfBlock(store.getEndOfBlock()).setData(ByteString.copyFrom(store.getDataBytes())).build();
        Chord.BytesResponse sentResp = sRBStub.sendBytes(sendBytes);
        chnl.shutdown();
        return sentResp;
    }

    public static Chord.FDResponse sendFileDetailsToNode(Storage store, String nodeAddr){
        ManagedChannel channel1 = ManagedChannelBuilder.forTarget(nodeAddr)
                .usePlaintext()
                .build();
        SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel1);
        Chord.FDRequest requestFD = Chord.FDRequest.newBuilder().setFileName(store.getFileName()).setFileSiz(store.getSize()).
                setFileRootHash(store.getRootHash()).addAllData(store.getContentList()).build();
        Chord.FDResponse resp = sRBlockingStub.sendFD(requestFD);
        channel1.shutdown();
        return resp;
    }

    public static void sendFileForStorageOnTheFS(String nodeAddr, FileDetails fD) {


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
        channel1.shutdown();
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
        chnl.shutdown();
        return sentResp;
    }

    public static void retrieveRequest(String clientIP, String contentId, String fileName, String addressOfArbNode) {
        Node node = RPCFunctions.findSuccessorCall(addressOfArbNode, contentId);
        ManagedChannel channel = ManagedChannelBuilder.forTarget(node.getIpAddress())
                .usePlaintext()
                .build();
        //System.out.println("looking for file at "+ node.getIpAddress());
        SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
        Chord.RetRequest retRequest = Chord.RetRequest.newBuilder().setFileName(fileName).setHash(contentId).
                setClientIP(clientIP).setIsContent(false).build();
        Chord.RetResponse resp = sRBlockingStub.retrieveFileRequest(retRequest);
        channel.shutdown();
    }

    }

