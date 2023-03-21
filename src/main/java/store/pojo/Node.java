package store.pojo;


import store.helper.CalcHelper;
import store.helper.RPCFunctions;
import store.helper.NodeHelper;
import store.start.DisplayFingerTable;
import store.start.FixFingers;
import store.start.Stabilize;
import store.start.StabilizeFileStore;

import java.io.File;
import java.math.BigInteger;
import java.util.*;

public class Node {
    private String ipAddress;
    private String hashId;
    private Map<String, FingerTable> fingertableMap;
    private Node successor;
    private Node predecessor;
    private Map<String,Storage> fileMap;
    private Map<String, Storage> contentMap;
    private String appPath;

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        this.successor = successor;
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public Map<String, FingerTable> getFingertableMap() {
        return fingertableMap;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public void setFingertableMap(Map<String, FingerTable> fingertableMap) {
        this.fingertableMap = fingertableMap;
    }

    public Node(String ipAddress) {
        this.ipAddress = ipAddress;
        this.hashId = NodeHelper.getNodeHashId(ipAddress);

    }

    public Node(String ipAddress, boolean fingerTable) {
        this.ipAddress = ipAddress;
        this.hashId = NodeHelper.getNodeHashId(ipAddress);
        this.fingertableMap = new LinkedHashMap<>();
        // initialize fingertable entries
        for (int i = 1; i <= 256; i++) {
            String start = NodeHelper.getFingerStart(i,ipAddress);
            fingertableMap.put(start, new FingerTable(start,new Node(ipAddress)));
        }
        this.appPath = System.getProperty("user.dir")+"/"+"StorageMetadata-"+hashId;

        File file = new File(this.appPath);
        if(!file.exists()){
            file.mkdir();
        }
        File file2 = new File(this.appPath+"/"+"resources");
        if(!file2.exists()){
            file2.mkdir();
        }
        // if want to stop as required - add one more argument boolean where you can set the while loop as false in stabilize
        Stabilize stabilize = new Stabilize(this);
        FixFingers fix_fingers = new FixFingers(this);
        DisplayFingerTable dFT = new DisplayFingerTable(this);
        StabilizeFileStore stabilizeFileStore = new StabilizeFileStore(this);
        stabilize.start();
        fix_fingers.start();
        //dFT.start();
        stabilizeFileStore.start();
    }

    public void join(Node arbNode){
        {
            if(getIpAddress().equals(arbNode.getIpAddress())){
                setPredecessor(this);
                setSuccessor(this);
            }else {
                setPredecessor(null);
                setSuccessor(RPCFunctions.findSuccessorCall(arbNode.getIpAddress(), getHashId()));
                //System.out.println("successor:" + getSuccessor().getIpAddress());
            }
        }

    }

    public  String findSuccessor(BigInteger id, Node myNode){
        Node nD = myNode.findPredecessor(id,myNode);
        //get the node info from string
        if(nD.getIpAddress().equals(getIpAddress())){
            return getSuccessor().getIpAddress();
        }
        return RPCFunctions.getSuccessorOfNode(nD.getIpAddress());
    }

    public  Node findPredecessor(BigInteger id,Node myNode) {
        // my nodes hex string and bigInt value
        String myNodeHex = myNode.getHashId();
        BigInteger myNodeBigInt = CalcHelper.getBigInt(myNodeHex);
        // my successor node's hex string and bigInt value
        String mySuccessorHex = myNode.getSuccessor().getHashId();
        BigInteger mySuccessorBigInt = CalcHelper.getBigInt(mySuccessorHex);
        String nDSuccessor = null;
        String newNd = myNodeHex;
        BigInteger succRelativeId = CalcHelper.calculateRelID(mySuccessorBigInt,myNodeBigInt);
        BigInteger relativeID = CalcHelper.calculateRelID(id,myNodeBigInt);
        String prevMem = null;
        //initialize newNode as my node as at the start its going ti go in if statement and then resset the newNode from closestPreceedingFinger call.
        Node newNode=myNode;
        while(!(relativeID.compareTo(BigInteger.ZERO)==1 &&  relativeID.compareTo(succRelativeId) <=0)){

            if(newNd.equals(myNodeHex)) {
                newNode = closestPreceedingFinger(id,myNodeBigInt,myNode);
                newNd= newNode.getHashId();
            }else{
                newNode = RPCFunctions.closestPrecedingFingerCall(myNode,newNode,id);
                newNd=newNode.getHashId();

            }
            if(prevMem!=null && prevMem==newNd) return newNode;
            if(newNd.equals(myNodeHex)) return newNode;
            if(prevMem==null) prevMem=newNd;
            myNodeBigInt =CalcHelper.getBigInt(newNd);
            relativeID = CalcHelper.calculateRelID(id,myNodeBigInt);
            // here we might need the whole node object for newNd as we need the ip address
            if(myNode.getIpAddress().equals(newNode.getIpAddress())){
                nDSuccessor = myNode.getSuccessor().getIpAddress();
            }else {
                nDSuccessor = RPCFunctions.getSuccessorOfNode(newNode.getIpAddress());
            }
            mySuccessorHex = NodeHelper.getNodeHashId(nDSuccessor);
            //  System.out.println("successor of"+ newNd+" is "+ nDSuccessor+"while finding id: "+ id);
            mySuccessorBigInt=CalcHelper.getBigInt(mySuccessorHex);
            succRelativeId = CalcHelper.calculateRelID(mySuccessorBigInt,myNodeBigInt);
        }
        return newNode;
    }



    public  Node closestPreceedingFinger(BigInteger id,BigInteger myNodeId,Node myNode) {
        BigInteger relativeId = CalcHelper.calculateRelID(id,myNodeId);
        Set<String> keySet = myNode.getFingertableMap().keySet();
        List<String> setList = new ArrayList<>(keySet);
        setList.sort(Collections.reverseOrder());
        if(myNode.getFingertableMap().size()==256) {
            for (String key:setList) {
                BigInteger ithRelativeId = CalcHelper.calculateRelID(CalcHelper.getBigInt(myNode.getFingertableMap().get(key).getNode().getHashId()), myNodeId);
                if (ithRelativeId.compareTo(BigInteger.ZERO) == 1 && ithRelativeId.compareTo(relativeId) ==-1) {
                    return new Node(myNode.getFingertableMap().get(key).getNode().getIpAddress());
                }
            }
        }
        return myNode;
    }

    public static void notifyCall(String nD, String successor){
        RPCFunctions.updateSuccessor(nD,successor);
    }

    @Override
    public String toString() {
        return "Node{" +
                "ipAddress='" + ipAddress + '\'' +
                ", hashId='" + hashId + '\'' +
                '}';
    }
}
