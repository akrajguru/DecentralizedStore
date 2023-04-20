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

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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
            channel.shutdown();
        }
        catch(Exception e){
            System.out.println("exception in findSuccessorCall: "+ finger1Node);
            e.printStackTrace();
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
        ManagedChannel channel=null;
        try {
            channel = ManagedChannelBuilder.forTarget(successor).usePlaintext().build();
            NodeGrpc.NodeBlockingStub stub = NodeGrpc.newBlockingStub(channel);
            Chord.UpdateSPRequest req = Chord.UpdateSPRequest.newBuilder().setPredecessor(nD).build();
            Chord.UpdateSPResponse response = stub.updateSuccessorPredecessor(req);
            if (response.getResponse() == 0) //System.out.println(successor+ " node's predecessor updated to:" + myPort);
                channel.shutdown();
        }catch(StatusRuntimeException e){
            channel.shutdown();
            myNode.deleteUnavailableNodeInfo(successor);
        }catch(Exception e){
            e.printStackTrace();
            channel.shutdown();
        }
    }

    public static Chord.BytesResponse sendDataContentBytesToNode(Storage store, String nodeAddr,Node myNode) {
        try {
            ManagedChannel chnl = ManagedChannelBuilder.forTarget(nodeAddr)
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
            List<Chord.fileContent> fCList = new ArrayList<>();
            Chord.fileContent fC = Chord.fileContent.newBuilder().setRootHash(store.getRootHash()).setFileName(store.getFileName()).setBlockHash(store.getContentHash())
                    .setEndOfBlock(store.getEndOfBlock()).setDataContent(ByteString.copyFrom(store.getDataBytes())).setOwner(store.getOwner()).build();
            fCList.add(fC);
            Chord.BytesRequest sendBytes = Chord.BytesRequest.newBuilder().addAllFileContent(fCList).build();
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

    public static void sendFileForStorageOnTheFS(String nodeAddr, FileDetails fD,Node myNode,String owner) {

        try {
            Node node = RPCFunctions.findSuccessorCall(nodeAddr, fD.getHashOfFile(),myNode);
            ManagedChannel channel1 = ManagedChannelBuilder.forTarget(node.getIpAddress())
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel1);
            List<String> fDList = fD.getContentList().stream().map(x -> x.getHash()).collect(Collectors.toList());
            Chord.FDRequest requestFD = Chord.FDRequest.newBuilder().setFileName(fD.getFileName()).setFileSiz(fD.getFileSize()).
                    setFileRootHash(fD.getHashOfFile()).addAllData(fDList).setOwner(owner).build();
            Chord.FDResponse resp = sRBlockingStub.sendFD(requestFD);
            Map<String, List<Content>> senderMap = new LinkedHashMap<>();
            if (resp.getResp() == 1) {
                for (Content content : fD.getContentList()) {
                   Node nodeSucc = RPCFunctions.findSuccessorCall(nodeAddr, content.getHash(),node);
                   if(senderMap.containsKey(nodeSucc.getIpAddress())){
                       List<Content> list = senderMap.get(nodeSucc.getIpAddress());
                       list.add(content);
                   }else{
                       List<Content> contentL = new ArrayList<>();
                       contentL.add(content);
                       senderMap.put(nodeSucc.getIpAddress(),contentL);
                   }
                }
                System.out.println("out of findsuccessor calls for all data");
                for(Map.Entry<String,List<Content>> entry: senderMap.entrySet()) {
                    Chord.BytesResponse sentResp = sendContentAndGetBytesResponse(fD, myNode, entry.getValue(), entry.getKey(),owner);
                    if(sentResp!=null && sentResp.getResp()==1) System.out.println("files stored on "+entry.getKey());
                }
            }else{
                System.out.println("Cannot store file as it cannot be replicated");
            }
            channel1.shutdown();
        }catch(StatusRuntimeException e){
            if(myNode!=null) {
                myNode.deleteUnavailableNodeInfo(myNode.getIpAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private static Chord.BytesResponse sendContentAndGetBytesResponse(FileDetails fD, Node node, List<Content> content,String nodeAddr,String owner) {
        Node nodeSucc=null;
        Chord.BytesResponse sentResp=null;
        int cnt =0;
        try {
            ManagedChannel chnl = ManagedChannelBuilder.forTarget(nodeAddr)
                    .usePlaintext()
                    .build();
            List<Content> limitedSizeUnder4MB = new ArrayList<>();
            int stored=0;
            for(int i =0; i<=content.size();i++) {
                if (i < content.size() && content.get(i).getData().length + stored < 4000000) {
                    limitedSizeUnder4MB.add(content.get(i));
                    stored += content.get(i).getData().length;
                } else {
                    SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
                    List<Chord.fileContent> fileContentList = new ArrayList<>();
                    for (Content storage : limitedSizeUnder4MB) {
                        Chord.fileContent fileContent = null;
                        fileContent = Chord.fileContent.newBuilder().setFileName(fD.getFileName())
                                .setBlockHash(storage.getHash()).setRootHash(fD.getHashOfFile())
                                .setDataContent(ByteString.copyFrom(storage.getData())).setEndOfBlock(storage.getEndByte()).setOwner(owner)
                                .setIsContent(true).build();
                        fileContentList.add(fileContent);
                        cnt++;
                    }
                    Chord.BytesRequest sendBytes = Chord.BytesRequest.newBuilder().addAllFileContent(fileContentList).setOwner(owner).build();
                    sentResp = sRBStub.sendBytes(sendBytes);
                    limitedSizeUnder4MB = new ArrayList<>();
                    if(i<content.size()) {
                        limitedSizeUnder4MB.add(content.get(i));
                    }
                    stored=0;
                }
            }
            System.out.println("sent fileContents:" +cnt);
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

    public static List<String> sendFileNamesToReplicate(List<String> fileList, Node node,ManagedChannel chnl, boolean setPredecessor) {
            SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
            Chord.replicaFileNames replicate=null;
            if(setPredecessor) {
                replicate = Chord.replicaFileNames.newBuilder().addAllFileNames(fileList).setYourPredecessor(node.getIpAddress()).build();
            }else{
                replicate = Chord.replicaFileNames.newBuilder().addAllFileNames(fileList).setYourSuccessor(node.getIpAddress()).build();
            }
            Chord.filesNotPresent sentResp = sRBStub.checkIFReplicasPresent(replicate);
            if(sentResp!=null && sentResp.getFilesNotPresentList()!=null){
                return sentResp.getFilesNotPresentList();
            }

        if(setPredecessor && sentResp.getError()==1){
            System.out.println("The pointers of our successor are incorrect");
        }else if(!setPredecessor && sentResp.getError()==1){
            System.out.println("The pointers of our predecessir are incorrect");
        }else if(sentResp.getFilesNotPresentList()!=null && !sentResp.getFilesNotPresentList().isEmpty()){
            return sentResp.getFilesNotPresentList();
        }
        return null;
    }

    public static int sendFilesToReplicate(List<Storage> fileList, Node node,ManagedChannel chnl, boolean setPredecessor) {
        SendReceiveGrpc.SendReceiveBlockingStub sRBStub = SendReceiveGrpc.newBlockingStub(chnl);
       
        Chord.sendFileData request = null;
        List<Chord.fileContent> fileContentList = new ArrayList<>();
        for(Storage storage: fileList) {
            Chord.fileContent fileContent=null;
            if (storage.isContainsContent()) {
                fileContent = Chord.fileContent.newBuilder().setFileName(storage.getFileName())
                        .setBlockHash(storage.getContentHash()).setRootHash(storage.getRootHash())
                        .setDataContent(ByteString.copyFrom(storage.getDataBytes())).setEndOfBlock(storage.getEndOfBlock())
                        .setIsContent(true).setOwner(storage.getOwner()).build();
            } else {
                fileContent = Chord.fileContent.newBuilder().setFileName(storage.getFileName())
                        .setRootHash(storage.getRootHash()).setFileSize(storage.getSize())
                        .addAllDataFD(storage.getContentList()).setOwner(storage.getOwner()).build();
            }
            fileContentList.add(fileContent);
        }
        if(setPredecessor) {
            request = Chord.sendFileData.newBuilder().addAllData(fileContentList).setYourPredecessor(node.getIpAddress()).build();
        }else{
            request = Chord.sendFileData.newBuilder().addAllData(fileContentList).setYourSuccessor(node.getIpAddress()).build();
        }
        Chord.replicaAck sentResp = sRBStub.replicateAbsentFiles(request);
        if(sentResp.getResponse()==1){
            System.out.println("successfully replicated remaining files");
            return 1;
        }
        return 0;
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

    public static int deleteRequest(String clientIP, String contentId, String fileName, String addressOfArbNode,Node myNode,String owner,boolean isContent) {
        Node node=null;
        try {
            node = RPCFunctions.findSuccessorCall(addressOfArbNode, contentId,myNode);
            if(myNode!=null && node.getIpAddress().equals(myNode.getIpAddress())){
                try {
                    PersistAndRetrieveMetadata.deleteFileForOwner(contentId, myNode, isContent, owner);
                }catch (IOException e){
                    return 1;
                }
                return 0;
            }else {
                ManagedChannel channel = ManagedChannelBuilder.forTarget(node.getIpAddress())
                        .usePlaintext()
                        .build();
                SendReceiveGrpc.SendReceiveBlockingStub sRBlockingStub = SendReceiveGrpc.newBlockingStub(channel);
                Chord.RetRequest retRequest = Chord.RetRequest.newBuilder().setFileName(fileName).setHash(contentId).
                        setClientIP(clientIP).setIsContent(isContent).setIsDeleteRequest(true).setOwner(owner).build();
                Chord.RetResponse resp = sRBlockingStub.retrieveFileRequest(retRequest);
                channel.shutdown();
                return resp.getResponse();
            }
        }catch (StatusRuntimeException e){
            if(myNode!=null){
                myNode.deleteUnavailableNodeInfo(node.getIpAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return 2;
    }

    public static int replicateCall(Node node, boolean isContent, List<Chord.fileContent> fileContentList,int replicateForward,String primary, String replica){

        ManagedChannel channel =null;
        try{
            channel = ManagedChannelBuilder.forTarget(replica)
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub blockingStub = SendReceiveGrpc.newBlockingStub(channel);
            Chord.sendReplica request = null;

            request = Chord.sendReplica.newBuilder().addAllFileContent(fileContentList)
                        .setPrimary(primary).setReplicateFurther(replicateForward).build();
            Chord.replicaAck ack = blockingStub.replicate(request);
            channel.shutdown();
            if(ack!=null) return ack.getResponse();
        }
        catch (StatusRuntimeException e){
            if(node!=null){
                node.deleteUnavailableNodeInfo(node.getIpAddress());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public static int  replicateAfterDeletion(Node node, String deletedNode, int replicateFurther,List<Storage> files) throws IOException {
        ManagedChannel channel = null;
        try{
            channel = ManagedChannelBuilder.forTarget(node.getSuccessor().getIpAddress())
                    .usePlaintext()
                    .build();
            SendReceiveGrpc.SendReceiveBlockingStub blockingStub = SendReceiveGrpc.newBlockingStub(channel);
            Chord.replicateMyFiles request = null;
//            List<Chord.fileContent> fileContentList = new ArrayList<>();
//            for(Storage storage: files) {
//                Chord.fileContent fileContent=null;
//                if (storage.isContainsContent()) {
//                    fileContent = Chord.fileContent.newBuilder().setFileName(storage.getFileName())
//                            .setBlockHash(storage.getContentHash()).setRootHash(storage.getRootHash())
//                            .setDataContent(ByteString.copyFrom(storage.getDataBytes())).setEndOfBlock(storage.getEndOfBlock())
//                            .setIsContent(true).build();
//                } else {
//                    fileContent = Chord.fileContent.newBuilder().setFileName(storage.getFileName())
//                            .setRootHash(storage.getRootHash()).setFileSize(storage.getSize())
//                            .addAllDataFD(storage.getContentList()).build();
//                }
//                fileContentList.add(fileContent);
//            }
            Chord.replicaInfo repInfo = Chord.replicaInfo.newBuilder().setReplicateFurther(replicateFurther)
                    .setDeletedNode(deletedNode).setMeYourPredecessor(node.getIpAddress()).build();
            request= Chord.replicateMyFiles.newBuilder().setCreate(repInfo).build();
            Chord.replicateMyFilesAck ack = blockingStub.replicateAsSuccessorDeleted(request);
            channel.shutdown();
            if(ack.getAck()>01) return ack.getAck();
        }
        catch (StatusRuntimeException e){
            if(node!=null){
                node.deleteUnavailableNodeInfo(node.getSuccessor().getIpAddress());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    }


