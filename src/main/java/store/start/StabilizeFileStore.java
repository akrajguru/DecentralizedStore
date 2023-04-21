package store.start;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import store.helper.CalcHelper;
import store.helper.NodeHelper;
import store.helper.PersistAndRetrieveMetadata;
import store.helper.RPCFunctions;
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

    @Override
    public void run() {
        while(true){
            ManagedChannel chnl=null;
            try {
                Thread.sleep(20000);
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
                     chnl = ManagedChannelBuilder.forTarget(node.getSuccessor().getIpAddress())
                            .usePlaintext()
                            .build();
                    // replicate to successor so send true
                    List<String> filesToReplicateSucc=RPCFunctions.sendFileNamesToReplicate(toBeReplicatedFiles,node,chnl,true);
                    if(node.getForceReplication()==null) {
                        node.setForceReplication(new ArrayList<>());
                    }
                        for(String file: filesToReplicateSucc) {
                            if (!node.getForceReplication().contains(file)){
                                node.getForceReplication().add(file);
                            }
                        }

                    if(node.getForceReplication()!=null && !node.getForceReplication().isEmpty()) {
                        List<Storage> storageList1 = PersistAndRetrieveMetadata.retrieveFilesAsAList(node.getForceReplication(), node);
                        RPCFunctions.sendFilesToReplicate(storageList1, node, chnl, true);
                    }
                    chnl.shutdown();
                    chnl = ManagedChannelBuilder.forTarget(node.getPredecessor().getIpAddress())
                            .usePlaintext()
                            .build();
                    // replicate to predecessor so send true
                    List<String> filesToReplicatePred=RPCFunctions.sendFileNamesToReplicate(toBeReplicatedFiles,node,chnl,false);
                for(String file: filesToReplicatePred) {
                    if (!node.getForceReplication().contains(file)){
                        node.getForceReplication().add(file);
                    }
                }
                    if(node.getForceReplication()!=null && !node.getForceReplication().isEmpty()) {
                        List<Storage> storageListPred = PersistAndRetrieveMetadata.retrieveFilesAsAList(node.getForceReplication(), node);
                        RPCFunctions.sendFilesToReplicate(storageListPred, node, chnl, false);
                    }
                    chnl.shutdown();

                    node.setForceReplication(new ArrayList<>());

//                    if(node.getContract()!=null && !node.getStorageInfo().getServerStoreInformation().get("primary").isEmpty()){
//                        if(node.getContract().getInformation(node.getIpAddress()).component2().compareTo(BigInteger.valueOf(node.getStorageInfo().getServerStoreInformation().get("primary").size()))!=0) {
//                            node.getContract().storeServerInformation(node.getIpAddress(), BigInteger.valueOf(node.getStorageInfo().getServerStoreInformation().get("primary").size())
//                                    , node.getStorageInfo().getServerStoreInformation().get("primary"));
//                        }else{
//                            System.out.println("no update to store on the contract");
//                        }
//                    }

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
}
