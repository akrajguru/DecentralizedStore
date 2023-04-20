package store.pojo;


import Blockchain.SmartContractConnection;
import store.Logger.LogToFile;
import store.helper.CalcHelper;
import store.helper.PersistAndRetrieveMetadata;
import store.helper.RPCFunctions;
import store.helper.NodeHelper;
import store.start.*;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Node {
    private LogToFile logger;
    private StorageInformation storageInfo;
    private String ipAddress;
    private String hashId;
    private Map<String, FingerTable> fingertableMap;
    private Node successor;
    Stabilize stabilize;
    private Node predecessor;
    private Map<String,Storage> fileMap;
    private Map<String, Storage> contentMap;
    private String appPath;
    private Map<String,String> successorMap;
    Map<String,List<String>> checkIfDataSentToServerAlready;
    SmartContractConnection contract;
    List<String> forceReplication;
    List<String> garbageCollectedFiles;


    public List<String> getGarbageCollectedFiles() {
        return garbageCollectedFiles;
    }

    public void setGarbageCollectedFiles(List<String> garbageCollectedFiles) {
        this.garbageCollectedFiles = garbageCollectedFiles;
    }

    public List<String> getForceReplication() {
        return forceReplication;
    }

    public void setForceReplication(List<String> forceReplication) {
        this.forceReplication = forceReplication;
    }

    public SmartContractConnection getContract() {
        return contract;
    }

    public void setContract(SmartContractConnection contract) {
        this.contract = contract;
    }

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

    public Node(String ipAddress, LinkedHashMap map){
        this.ipAddress = ipAddress;
        this.fingertableMap = map;
        for (int i = 1; i <= 256; i++) {
            String start = NodeHelper.getFingerStart(i,ipAddress);
            fingertableMap.put(start, new FingerTable(start,new Node(ipAddress)));
        }
        this.appPath = System.getProperty("user.dir")+"/"+"testData-"+hashId;
    }

    public Node(String ipAddress, boolean fingerTable, SmartContractConnection con) throws Exception {
        this.ipAddress = ipAddress;
        this.hashId = NodeHelper.getNodeHashId(ipAddress);
        this.fingertableMap = new LinkedHashMap<>();
        // initialize fingertable entries
        for (int i = 1; i <= 256; i++) {
            String start = NodeHelper.getFingerStart(i,ipAddress);
            fingertableMap.put(start, new FingerTable(start,new Node(ipAddress)));
        }
        this.appPath = System.getProperty("user.dir")+"/"+"StorageMetadata-"+hashId;
        this.successorMap = new LinkedHashMap<>();
        this.contract=con;
        contract.storeServerInformation(ipAddress,BigInteger.ZERO,new ArrayList<>());
        File file = new File(this.appPath);
        if(!file.exists()){
            file.mkdir();
        }
        File file2 = new File(this.appPath+"/"+"resources");
        if(!file2.exists()){
            file2.mkdir();
        }
        storageInfo = new StorageInformation(ipAddress);
        // if want to stop as required - add one more argument boolean where you can set the while loop as false in stabilize
        this.stabilize = new Stabilize(this);
        FixFingers fix_fingers = new FixFingers(this);
        DisplayFingerTable dFT = new DisplayFingerTable(this);
        checkIfDataSentToServerAlready=new HashMap<>();
        StabilizeFileStore stabilizeFileStore = new StabilizeFileStore(this);
        PopulateSuccessorList populateSuccessorList = new PopulateSuccessorList(this);
        logger=new LogToFile(this.appPath);
        stabilize.start();
        fix_fingers.start();
        dFT.start();
        stabilizeFileStore.start();
        populateSuccessorList.start();
    }

    public Map<String, List<String>> getCheckIfDataSentToServerAlready() {
        return checkIfDataSentToServerAlready;
    }

    public void setCheckIfDataSentToServerAlready(Map<String, List<String>> checkIfDataSentToServerAlready) {
        this.checkIfDataSentToServerAlready = checkIfDataSentToServerAlready;
    }

    public void join(Node arbNode){
        {
            if(getIpAddress().equals(arbNode.getIpAddress())){
                setPredecessor(this);
                setSuccessor(this);
                addEntryToSuccessorMap(getIpAddress(),getIpAddress());
            }else {
                setPredecessor(null);
                setSuccessor(RPCFunctions.findSuccessorCall(arbNode.getIpAddress(), getHashId(),this));
                addEntryToSuccessorMap(getIpAddress(), getSuccessor().getIpAddress());
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
        return RPCFunctions.getSuccessorOfNode(nD.getIpAddress(),this);
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
                nDSuccessor = RPCFunctions.getSuccessorOfNode(newNode.getIpAddress(),this);
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

    public  void notifyCall(String nD, String successor){
        RPCFunctions.updateSuccessor(nD,successor,this);
    }

    public void deleteUnavailableNodeInfo(String ipAddr)  {
        //stabilize.wait();
        List<Map.Entry<String, String>> list = getSuccessorMap().entrySet().stream().filter(x -> x.getValue().equals(ipAddr)).collect(Collectors.toList());
        String delKey=null;
        for(Map.Entry<String, String> m:list){
            delKey=m.getKey();
            getSuccessorMap().remove(m.getKey());
        }
        List<String> alKeys = new ArrayList<>(fingertableMap.keySet());
        Collections.reverse(alKeys);
        String succIP = fingertableMap.get(alKeys.get(0)).getNode().getIpAddress();
        for(String key: alKeys){
            if(fingertableMap.get(key).getNode().getIpAddress().equals(ipAddr)){
                if(getSuccessorMap().containsKey(ipAddr)){
                    fingertableMap.get(key).setNode(new Node(getSuccessorMap().get(ipAddr)));
                }else if(!succIP.equals(ipAddr)){
                    fingertableMap.get(key).setNode(new Node(succIP));
                }else{
                    fingertableMap.get(key).setNode(this);
                }
            }else{
                succIP=fingertableMap.get(key).getNode().getIpAddress();
            }
        }
        if(succIP==null) succIP=getIpAddress();
        if(getSuccessor().getIpAddress().equals(ipAddr)){
            System.out.println("updating successor in the deleteUnavailableNodeInfo func");
            setSuccessor(getSuccessorMap().containsKey(ipAddr) ? new Node(getSuccessorMap().get(ipAddr)):new Node(succIP));
            try {
                if(!successor.getIpAddress().equals(ipAddress)) {
//                    List<Storage> files = PersistAndRetrieveMetadata.retrieveFilesAsAList(getStorageInfo().getServerStoreInformation().get("replica1"), this);
//                    int ack = RPCFunctions.replicateAfterDeletion(this, ipAddr, 3,files);
//                    if(ack==1){
//                        System.out.println("files replicated");
//                    }else if(ack==2){
//                        System.out.println("only 2 nodes left in the ring");
//                    }else{
//                        System.out.println("some issue with file replica changes");
//                    }
                    //notify successor that I am your new pred
                    notifyCall(getIpAddress(),successor.getIpAddress());
                    //int ack = RPCFunctions.replicateAfterDeletion(this, ipAddr, 3,null);
                }
            }catch (Exception e){
                logger.writeLog("error","error in replicateAfterDeletion",e);
                e.printStackTrace();
            }
        }
        if(getPredecessor().getIpAddress().equals(ipAddr)){
            setPredecessor(getSuccessorMap().containsKey(ipAddr) ? new Node(getSuccessorMap().get(ipAddr)):new Node(succIP));
            System.out.println("settin preedecessor to:"+ ipAddr +" or " + succIP);
        }
        if(getCheckIfDataSentToServerAlready().containsKey(ipAddr)){
            getCheckIfDataSentToServerAlready().remove(ipAddr);
        }
        if(getCheckIfDataSentToServerAlready().containsKey(predecessor.getIpAddress())){
            getCheckIfDataSentToServerAlready().remove(predecessor.getIpAddress());
        }
        if(getCheckIfDataSentToServerAlready().containsKey(successor.getIpAddress())){
            getCheckIfDataSentToServerAlready().remove(successor.getIpAddress());
        }
        //stabilize.notify();
    }

    @Override
    public String toString() {
        return "Node{" +
                "ipAddress='" + ipAddress + '\'' +
                ", hashId='" + hashId + '\'' +
                '}';
    }

    public Map<String, String> getSuccessorMap() {
        return successorMap;
    }

    public void setSuccessorMap(Map<String, String> successorMap) {
        this.successorMap = successorMap;
    }

    public void addEntryToSuccessorMap(String key, String value) {
        this.successorMap.put(key,value);
    }

    public LogToFile getLogger() {
        return logger;
    }

    public void setLogger(LogToFile logger) {
        this.logger = logger;
    }

    public StorageInformation getStorageInfo() {
        return storageInfo;
    }

    public void setStorageInfo(StorageInformation storageInfo) {
        this.storageInfo = storageInfo;
    }

    private void replicateDataAfterSuccessorDeletion(){



    }
}
