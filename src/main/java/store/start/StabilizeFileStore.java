package store.start;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import store.helper.*;
import store.pojo.Node;
import store.pojo.Storage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StabilizeFileStore extends Thread {
    Node node;
    Map<String,List<String>> checkIfDataSentToServerAlready;

    public StabilizeFileStore(Node node) {
        this.node = node;
    }
    long timer;

    @Override
    public void run() {
        timer=0;
        boolean firstPay=true;

        while(true){
            ManagedChannel chnl=null;
            try {
                Thread.sleep(20000);
                timer+=20000;
                //List<Storage> storageList =PersistAndRetrieveMetadata.retrieveFilesAsAList(node.getStorageInfo().getServerStoreInformation().get("primary"), node);
                checkIfDataSentToServerAlready=node.getCheckIfDataSentToServerAlready();
                List<Storage> storageList =PersistAndRetrieveMetadata.getAllDataFromFileStore(node);
                if(storageList!=null) {
                    for (Storage storage : storageList) {
                        String iP;
                        if (storage.isContainsContent()) {
                            iP = node.findSuccessor(NodeHelper.fnv1aModifiedHash(storage.getContentHash()), node);
                            if (!node.getIpAddress().equals(iP)) {
                                if (!checkIfDataSentToServerAlready.containsKey(iP) || !checkIfDataSentToServerAlready.get(iP).contains(storage.getContentHash())) {
                                    //TODO
                                    RPCFunctions.sendDataContentBytesToNode(storage, iP,node);
                                    if (checkIfDataSentToServerAlready.containsKey(iP)) {
                                        checkIfDataSentToServerAlready.get(iP).add(storage.getContentHash());
                                    } else {
                                        List<String> temp = new ArrayList<>();
                                        temp.add(storage.getContentHash());
                                        checkIfDataSentToServerAlready.put(iP, temp);
                                    }
                                }
                                if( node.getStorageInfo().getServerStoreInformation().get("primary").contains(storage.getContentHash())){
                                    node.getStorageInfo().getServerStoreInformation().get("primary").remove(storage.getContentHash());
                                }
                            }else{
                                if( node.getStorageInfo().getServerStoreInformation().get("replica1").contains(storage.getContentHash())){
                                    node.getStorageInfo().getServerStoreInformation().get("replica1").remove(storage.getContentHash());
                                }
                                if( node.getStorageInfo().getServerStoreInformation().get("replica2").contains(storage.getContentHash())){
                                    node.getStorageInfo().getServerStoreInformation().get("replica2").remove(storage.getContentHash());
                                }
                                //node.getGarbageCollector().get("CONTENT").add(storage.getContentHash());
                            }
                        } else {
                            iP = node.findSuccessor(NodeHelper.fnv1aModifiedHash(storage.getRootHash()), node);
                            if (!node.getIpAddress().equals(iP)) {
                                if (!checkIfDataSentToServerAlready.containsKey(iP) || !checkIfDataSentToServerAlready.get(iP).contains(storage.getRootHash())) {
                                    RPCFunctions.sendFileDetailsToNode(storage, iP,node);
                                    if (checkIfDataSentToServerAlready.containsKey(iP)) {
                                        checkIfDataSentToServerAlready.get(iP).add(storage.getRootHash());
                                    } else {
                                        List<String> temp = new ArrayList<>();
                                        temp.add(storage.getRootHash());
                                        checkIfDataSentToServerAlready.put(iP, temp);
                                    }
                                }
                                if( node.getStorageInfo().getServerStoreInformation().get("primary").contains(storage.getRootHash())){
                                    node.getStorageInfo().getServerStoreInformation().get("primary").remove(storage.getRootHash());
                                }
                            }else{
                                if( node.getStorageInfo().getServerStoreInformation().get("replica1").contains(storage.getRootHash())){
                                    node.getStorageInfo().getServerStoreInformation().get("replica1").remove(storage.getRootHash());
                                }
                                if( node.getStorageInfo().getServerStoreInformation().get("replica2").contains(storage.getRootHash())){
                                    node.getStorageInfo().getServerStoreInformation().get("replica2").remove(storage.getRootHash());
                                }
                                //node.getGarbageCollector().get("FD").add(storage.getContentHash());
                            }
                        }
                    }
                }
                node.setCheckIfDataSentToServerAlready(checkIfDataSentToServerAlready);
                List<String> toBeReplicatedFiles = node.getStorageInfo().getServerStoreInformation().get("primary");
                // RPC call to successor and give all file names they should have
                   /* send filenames
                        send I am your predecessor
                        receieve all the file names they dont have
                        after that send those files
                    */
                if(toBeReplicatedFiles==null) {
                    toBeReplicatedFiles= new ArrayList<>();
                }
                //if(toBeReplicatedFiles!=null && !toBeReplicatedFiles.isEmpty()) {
                    chnl = ManagedChannelBuilder.forTarget(node.getSuccessor().getIpAddress())
                            .usePlaintext()
                            .build();

                    // replicate to successor so send true
                    List<String> filesToReplicateSucc = RPCFunctions.sendFileNamesToReplicate(toBeReplicatedFiles, node, chnl, true);
                    // force replication to replicate existing files with changes like - addition in owners
                    if (node.getForceReplication() == null) {
                        node.setForceReplication(new ArrayList<>());
                    }
                    for (String file : filesToReplicateSucc) {
                        if (!node.getForceReplication().contains(file)) {
                            node.getForceReplication().add(file);
                        }
                    }
                    System.out.println("replicating files:");
                    node.getForceReplication().stream().forEach(x-> System.out.println(x));
                    if (node.getForceReplication() != null && !node.getForceReplication().isEmpty()) {
                        List<Storage> storageList1 = PersistAndRetrieveMetadata.retrieveFilesAsAList(node.getForceReplication(), node);
                        RPCFunctions.sendFilesToReplicate(storageList1, node, chnl, true);
                    }
                    chnl.shutdown();
                    chnl = ManagedChannelBuilder.forTarget(node.getPredecessor().getIpAddress())
                            .usePlaintext()
                            .build();
                    // replicate to successor so send false
                    List<String> filesToReplicatePred = RPCFunctions.sendFileNamesToReplicate(toBeReplicatedFiles, node, chnl, false);
                    for (String file : filesToReplicatePred) {
                        if (!node.getForceReplication().contains(file)) {
                            node.getForceReplication().add(file);
                        }
                    }
                    System.out.println("replicating files:");
                    node.getForceReplication().stream().forEach(x-> System.out.println(x));
                    if (node.getForceReplication() != null && !node.getForceReplication().isEmpty()) {
                        List<Storage> storageListPred = PersistAndRetrieveMetadata.retrieveFilesAsAList(node.getForceReplication(), node);
                        RPCFunctions.sendFilesToReplicate(storageListPred, node, chnl, false);
                    }
                    chnl.shutdown();

                    node.setForceReplication(new ArrayList<>());
               // }

                    /*

                    Garbage collection

                     */

                    if(timer%300000==0){

                        for(Storage storage: storageList){
                            if(storage.isContainsContent()){
                                if(!(node.getStorageInfo().getServerStoreInformation().get("primary").contains(storage.getContentHash())
                                || node.getStorageInfo().getServerStoreInformation().get("replica1").contains(storage.getContentHash())
                                || node.getStorageInfo().getServerStoreInformation().get("replica2").contains(storage.getContentHash()))){
                                    node.getGarbageCollector().get("CONTENT").add(storage.getContentHash());
                                }
                            }else{
                                if(!(node.getStorageInfo().getServerStoreInformation().get("primary").contains(storage.getRootHash())
                                        || node.getStorageInfo().getServerStoreInformation().get("replica1").contains(storage.getRootHash())
                                        || node.getStorageInfo().getServerStoreInformation().get("replica2").contains(storage.getRootHash()))){
                                    node.getGarbageCollector().get("FD").add(storage.getContentHash());
                                }
                            }
                        }
                        int f =PersistAndRetrieveMetadata.deleteFilesGarbageCollection(node.getGarbageCollector().get("FD"),node,false);
                        int c =PersistAndRetrieveMetadata.deleteFilesGarbageCollection(node.getGarbageCollector().get("CONTENT"),node,true);
                        int t =f+c;
                        System.out.println("Garbage collected files:" +t);
                    }
                    if(timer%600000==0 || (firstPay && checkIfAnyFileIsPresent())) {
                        List<String> paidTrue = new ArrayList<>();
                        List<String> notPaid = new ArrayList<>();
                        for (Map.Entry<String, Integer> set : node.getPaidList().entrySet()) {
                            try {
                                if (set.getValue() >= 0) {
                                    SolidityHelper.collectAmount(node, set.getKey());
                                    paidTrue.add(set.getKey());
                                } else {
                                    node.getGarbageCollector().get("CONTENT").add(set.getKey());
                                    System.out.println("not paid for " + set.getKey() + " in whole hour" + " - tagging for garbage collection");
                                }
                            } catch (Exception e) {
                                System.out.println("counter for: " + set.getKey() + " is " + set.getValue());
                                notPaid.add(set.getKey());
                            }
                        }
                        for (String file : notPaid) {
                            int count = node.getPaidList().get(file);
                            node.getPaidList().put(file, count - 1);
                        }
                        for (String file : paidTrue) {
                            node.getPaidList().put(file, 6);
                        }
                        firstPay=false;
                    }

            } catch (InterruptedException e) {
                chnl.shutdown();
                throw new RuntimeException(e);
            } catch (IOException e) {
                chnl.shutdown();
                throw new RuntimeException(e);
            } catch(Exception e){
                chnl.shutdown();
                e.printStackTrace();
            }
        }
    }

    private boolean checkIfAnyFileIsPresent() {
        return node.getStorageInfo().getServerStoreInformation().get("primary").size() > 0
                || node.getStorageInfo().getServerStoreInformation().get("replica1").size() > 0
                || node.getStorageInfo().getServerStoreInformation().get("replica2").size() > 0;
    }
}
