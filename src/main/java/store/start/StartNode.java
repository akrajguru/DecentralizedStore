package store.start;

import Blockchain.SmartContractConnection;
import chord.Chord;
import chord.NodeGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import store.helper.CalcHelper;
import store.helper.NodeHelper;
import store.pojo.Node;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class StartNode extends NodeGrpc.NodeImplBase implements Runnable  {

    private Node node;
    private CountDownLatch connectedSignal;
    String args[];
    String nodeIP;
    String ipAddressOfKnownNode;


    public StartNode(Node node,CountDownLatch cd){
        this.node=node;
        this.connectedSignal=  new CountDownLatch(1);
    }


    public static void main(String[] args) throws Exception {
    //public StartNode(String[] args) {
        String ip=null;
        try {
             ip = NodeHelper.getIPAddress();
        }catch(Exception e){
            e.printStackTrace();
        }
        String port = args[0];
        String nodeIP = ip + ":" + port;
        String ipAddressOfKnownNode = null;
        String contractAddress = null,  credentials=null, networkIp =null;
        if (args.length == 4) {
            ipAddressOfKnownNode = nodeIP;
            contractAddress = args[1];
            credentials=args[2];
            networkIp=args[3];
        } else if(args.length == 5){
            ipAddressOfKnownNode = args[1];
            contractAddress = args[2];
            credentials=args[3];
            networkIp=args[4];
        }else{
            System.out.println("need either 4 [new node, contractAddress, credentials, network IP] " +
                    "or 5 [new node, arbitrary node to join a chord ring ,contractAddress, credentials, network IP] " +
                    "arguments you provided "+ args.length);
        }
        SmartContractConnection con=null;
        try {
             con = new SmartContractConnection(contractAddress, credentials, networkIp);
        }catch(Exception e){
            System.out.println("contract cannot be loaded check info");
            e.printStackTrace();
            return;
        }
//        this.args=args;
//        this.nodeIP=nodeIP;
//        this.ipAddressOfKnownNode=ipAddressOfKnownNode;
        if(con.checkBalanceServer()) {
            runMethod(args, nodeIP, ipAddressOfKnownNode, new CountDownLatch(1), con);
        }else{
            System.out.println("low on ether");
        }
        // }
    }


    private static void runMethod(String[] args, String nodeIP, String ipAddressOfKnownNode,CountDownLatch cd,SmartContractConnection con) throws Exception {
        Node node = new Node(nodeIP, true,con);
        Node arbNode = new Node(ipAddressOfKnownNode);

        StartNode startNode = new StartNode(node, cd);
        Thread serverThread = new Thread(() -> {
            Server server = ServerBuilder.forPort(Integer.parseInt(args[0].substring(args[0].indexOf(":") + 1))).addService(startNode).addService(new SendReceiveService(node)).build();
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Port already in Use:+" + args[0]);
                return;
            }
            System.out.println("Server started at" + node.getIpAddress());
            System.out.println(args[0].substring(args[0].indexOf(":") + 1));
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        node.join(arbNode);
        try{
            cd.await();
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void forwardKey(Chord.ForwardKeyRequest request, StreamObserver<Chord.ForwardKeyReply> responseObserver) {
        node.getPredecessor();
        super.forwardKey(request, responseObserver);
    }

    @Override
    public void getNodeData(Chord.NodeInfoRequest request, StreamObserver<Chord.NodeInfoResponse> responseObserver) {
        super.getNodeData(request, responseObserver);
    }

    @Override
    public void heartBeat(Chord.HeartBeatToPredecessorRequest request, StreamObserver<Chord.HeartBeatReplyFromSuccessor> responseObserver) {
        super.heartBeat(request, responseObserver);
    }

    @Override
    public void closestPrecedFinger(Chord.ClosestPFRequest request, StreamObserver<Chord.ClosestPFReply> responseObserver) {

        BigInteger start =new BigInteger(request.getId());
        Node retPort = node.closestPreceedingFinger(start, CalcHelper.getBigInt(node.getHashId()),node);
        Chord.ClosestPFReply.Builder resp = Chord.ClosestPFReply.newBuilder();
        Chord.NodeFull nodeFull = Chord.NodeFull.newBuilder().setIpAddress(retPort.getIpAddress()).setHash(node.getHashId()).build();
        resp.setClosestPrecedingFinger(nodeFull);
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();

    }

    @Override
    public void updateSuccessorPredecessor(Chord.UpdateSPRequest request, StreamObserver<Chord.UpdateSPResponse> responseObserver) {
        String nodeN = request.getPredecessor();
        if(node.getPredecessor()!=null && !node.getPredecessor().getIpAddress().equals(nodeN)){
            if(node.getCheckIfDataSentToServerAlready().containsKey(node.getPredecessor().getIpAddress())){
                node.getCheckIfDataSentToServerAlready().remove(node.getPredecessor().getIpAddress());
            }
            if(node.getCheckIfDataSentToServerAlready().containsKey(node.getSuccessor().getIpAddress())){
                node.getCheckIfDataSentToServerAlready().remove(node.getSuccessor().getIpAddress());
            }
        }
        node.setPredecessor(new Node(nodeN));
        System.out.println("updating predecessor to: "+nodeN );
        //System.out.println("updating predecessor to:"+ nodeN);
        Chord.UpdateSPResponse.Builder resp = Chord.UpdateSPResponse.newBuilder();
        resp.setResponse(0);
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    @Override
    public void updateFingerTable(Chord.FingerTableUpdateRequest request, StreamObserver<Chord.FingerTableUpdateResponse> responseObserver) {
        super.updateFingerTable(request, responseObserver);
    }

    @Override
    public void successorCall(Chord.SuccessorCallRequest request, StreamObserver<Chord.SuccessorCallReply> responseObserver) {
        String start =request.getId();
        String retPort = node.findSuccessor(CalcHelper.getBigInt(start),node);
        Chord.SuccessorCallReply.Builder resp = Chord.SuccessorCallReply.newBuilder();
        Chord.NodeFull nodeFull = Chord.NodeFull.newBuilder().setIpAddress(retPort).build();
        resp.setSuccessor(nodeFull);
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getPredecessorOfNode(Chord.PredecessorOfNodeRequest request, StreamObserver<Chord.PredecessorOfNodeReqponse> responseObserver) {
        Chord.PredecessorOfNodeReqponse.Builder resp = Chord.PredecessorOfNodeReqponse.newBuilder();
        resp.setPredecessor(node.getPredecessor().getIpAddress());

        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getSuccessorOfNode(Chord.SuccessorOfNodeRequest request, StreamObserver<Chord.SuccessorOfNodeResponse> responseObserver) {
        Chord.SuccessorOfNodeResponse.Builder resp = Chord.SuccessorOfNodeResponse.newBuilder();
        resp.setSuccessor(node.getSuccessor().getIpAddress());
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
    }

    @Override
    public void run() {
        try {
            runMethod(args, nodeIP, ipAddressOfKnownNode,connectedSignal,null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
