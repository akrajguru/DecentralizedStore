package store.helper;

import chord.Chord;
import chord.NodeGrpc;
import chord.SendReceiveGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
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
    //add channel shutdown in finally as well
    public static String getSuccessorOfNode(String finger1Node,Node node) {
        ManagedChannel channel=null;
        try {
            channel = ManagedChannelBuilder.forTarget(finger1Node).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.SuccessorOfNodeRequest request = Chord.SuccessorOfNodeRequest.newBuilder().build();
            Chord.SuccessorOfNodeResponse resp = stub.getSuccessorOfNode(request);
            if (resp.getSuccessor() != null) {
                //successorList.put(finger1Node, resp.getSuccessor());
                channel.shutdown();
                return resp.getSuccessor();

            }
            channel.shutdown();
           // System.out.println("predecessor call returned null from: " + finger1Node);

        }catch(StatusRuntimeException e){
            if(node!=null){
                node.deleteUnavailableNodeInfo(finger1Node);
            }
        }
        catch(Exception e){
            System.out.println("exception in findSuccessorCall: "+ finger1Node);
            e.printStackTrace();
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
        }
    catch(StatusRuntimeException e){
        if(myNode!=null){
            myNode.deleteUnavailableNodeInfo(newNd.getIpAddress());
            System.out.println("exception in closestPrecedingFingerCall function");
        }
    }
        catch(Exception e){
            System.out.println("exception in closestPrecedingFingerCall function");
            e.printStackTrace();

        }finally {
            channel.shutdown();
        }

            return myNode;
        }


    public static Node findSuccessorCall(String nodeD, String start1, Node myNode) {
        ManagedChannel channel =null;
        try {
             channel = ManagedChannelBuilder.forTarget(nodeD).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.SuccessorCallRequest request = Chord.SuccessorCallRequest.newBuilder().setId(start1).build();
            Chord.SuccessorCallReply resp = stub.successorCall(request);
            if(resp!=null && resp.getSuccessor()!=null) {
                channel.shutdown();
                Node node = new Node(resp.getSuccessor().getIpAddress());
                return node;
            }
           // System.out.println("successor call returned null from: " + nodeD);
        }

        catch(StatusRuntimeException e){
            if(myNode!=null){
                myNode.deleteUnavailableNodeInfo(nodeD);
            }
        }
        catch(Exception e){
            System.out.println("exception in findSuccessorCall: "+ nodeD);
            e.printStackTrace();
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
            if (resp.getPredecessor() != null){
                channel.shutdown();
                return new Node(resp.getPredecessor());
            }
            System.out.println("predecessor call returned null from: " + succNode.getIpAddress());
            channel.shutdown();
        }catch(StatusRuntimeException e){
            myNode.deleteUnavailableNodeInfo(succNode.getIpAddress());
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("node:" + succNode.getIpAddress()+" has been deleted");
            //checkSuccessorsSucc(finger1Node);
        }
        return null;
    }

    public static void updateSuccessor(String nD,String successor,Node myNode) {
        //if(successor.equals())
        //System.out.println("updating successor: "+successor);
        try {
            ManagedChannel channel = ManagedChannelBuilder.forTarget(successor).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.UpdateSPRequest req = Chord.UpdateSPRequest.newBuilder().setPredecessor(nD).build();
            Chord.UpdateSPResponse response = stub.updateSuccessorPredecessor(req);
            if (response.getResponse() == 0) //System.out.println(successor+ " node's predecessor updated to:" + myPort);
                channel.shutdown();
        }catch(StatusRuntimeException e){
            myNode.deleteUnavailableNodeInfo(successor);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static Chord.BytesResponse sendDataContentBytesToNode(Storage store, String nodeAddr,Node myNode) {
        try {
            ManagedChannel chnl = ManagedChannelBuilder.forTarget(nodeAddr)
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
            Chord.BytesRequest sendBytes = Chord.BytesRequest.newBuilder().setRootHash(store.getRootHash()).setFileName(store.getFileName()).setBlockHash(store.getContentHash())
                    .setEndOfBlock(store.getEndOfBlock()).setData(ByteString.copyFrom(store.getDataBytes())).build();
            Chord.BytesResponse sentResp = sRBStub.sendBytes(sendBytes);
            chnl.shutdown();
            return sentResp;
        } catch(StatusRuntimeException e){
            myNode.deleteUnavailableNodeInfo(nodeAddr);
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    public static Chord.FDResponse sendFileDetailsToNode(Storage store, String nodeAddr,Node node){
        try {
            ManagedChannel channel1 = ManagedChannelBuilder.forTarget(nodeAddr)
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel1);
            Chord.FDRequest requestFD = Chord.FDRequest.newBuilder().setFileName(store.getFileName()).setFileSiz(store.getSize()).
                    setFileRootHash(store.getRootHash()).addAllData(store.getContentList()).build();
            Chord.FDResponse resp = sRBlockingStub.sendFD(requestFD);
            channel1.shutdown();
            return resp;
        }catch(StatusRuntimeException e){
            if(node!=null) {
                node.deleteUnavailableNodeInfo(nodeAddr);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void sendFileForStorageOnTheFS(String nodeAddr, FileDetails fD,Node myNode) {

        try {
            Node node = RPCFunctions.findSuccessorCall(nodeAddr, fD.getHashOfFile(),myNode);
            ManagedChannel channel1 = ManagedChannelBuilder.forTarget(node.getIpAddress())
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel1);
            List<String> fDList = fD.getContentList().stream().map(x -> x.getHash()).collect(Collectors.toList());
            Chord.FDRequest requestFD = Chord.FDRequest.newBuilder().setFileName(fD.getFileName()).setFileSiz(fD.getFileSize()).
                    setFileRootHash(fD.getHashOfFile()).addAllData(fDList).build();
            Chord.FDResponse resp = sRBlockingStub.sendFD(requestFD);
            Map<String, String> senderMap = new LinkedHashMap<>();
            senderMap.put(node.getIpAddress(), "");
            if (resp.getResp() == 1) {
                for (Content content : fD.getContentList()) {
                    Chord.BytesResponse sentResp = sendContentAndGetBytesResponse(fD, myNode, content, nodeAddr);
                    while (sentResp.getResp() != 1) {
                        sentResp = sendContentAndGetBytesResponse(fD, myNode, content, nodeAddr);
                    }
                }
            }
            System.out.println(" File saved:" + fD);
            channel1.shutdown();
        }catch(StatusRuntimeException e){
            if(myNode!=null) {
                myNode.deleteUnavailableNodeInfo(myNode.getIpAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static Chord.BytesResponse sendContentAndGetBytesResponse(FileDetails fD, Node node, Content content,String nodeAddr) {
        Node nodeSucc=null;
        try {
             nodeSucc = RPCFunctions.findSuccessorCall(nodeAddr, content.getHash(),node);
            ManagedChannel chnl = ManagedChannelBuilder.forTarget(nodeSucc.getIpAddress())
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
            Chord.BytesRequest sendBytes = Chord.BytesRequest.newBuilder().setRootHash(fD.getHashOfFile()).setFileName(fD.getFileName()).setBlockHash(content.getHash())
                    .setEndOfBlock(content.getEndByte()).setData(ByteString.copyFrom(content.getData())).build();
            Chord.BytesResponse sentResp = sRBStub.sendBytes(sendBytes);
            chnl.shutdown();
            return sentResp;
        }catch (StatusRuntimeException e){
            if(node!=null ){
                node.deleteUnavailableNodeInfo(nodeSucc.getIpAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void retrieveRequest(String clientIP, String contentId, String fileName, String addressOfArbNode,Node myNode) {
        Node node=null;
        try {
             node = RPCFunctions.findSuccessorCall(addressOfArbNode, contentId,myNode);
            ManagedChannel channel = ManagedChannelBuilder.forTarget(node.getIpAddress())
                    .usePlaintext()
                    .build();
            //System.out.println("looking for file at "+ node.getIpAddress());
            SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
            Chord.RetRequest retRequest = Chord.RetRequest.newBuilder().setFileName(fileName).setHash(contentId).
                    setClientIP(clientIP).setIsContent(false).build();
            Chord.RetResponse resp = sRBlockingStub.retrieveFileRequest(retRequest);
            channel.shutdown();
        }catch (StatusRuntimeException e){
            if(myNode!=null){
                myNode.deleteUnavailableNodeInfo(node.getIpAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    }

