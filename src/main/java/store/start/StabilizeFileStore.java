package store.start;

import store.helper.CalcHelper;
import store.helper.PersistAndRetrieveMetadata;
import store.helper.RPCFunctions;
import store.pojo.Node;
import store.pojo.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StabilizeFileStore extends Thread {
    Node node;
    Map<String,List<String>> checkIfDataSentToServerAlready;

    public StabilizeFileStore(Node node) {
        this.node = node;
        checkIfDataSentToServerAlready=new HashMap<>();
    }

    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(20000);
                List<Storage> storageList =PersistAndRetrieveMetadata.getAllDataFromFileStore(node);
                if(storageList!=null) {
                    for (Storage storage : storageList) {
                        String iP;
                        if (storage.isContainsContent()) {
                            iP = node.findSuccessor(CalcHelper.getBigInt(storage.getContentHash()), node);
                            if (!node.getIpAddress().equals(iP)) {
                                if (!checkIfDataSentToServerAlready.containsKey(iP) || !checkIfDataSentToServerAlready.get(iP).contains(storage.getContentHash())) {
                                    RPCFunctions.sendDataContentBytesToNode(storage, iP,node);
                                    if (checkIfDataSentToServerAlready.containsKey(iP)) {
                                        checkIfDataSentToServerAlready.get(iP).add(storage.getContentHash());
                                    } else {
                                        List<String> temp = new ArrayList<>();
                                        temp.add(storage.getContentHash());
                                        checkIfDataSentToServerAlready.put(iP, temp);
                                    }
                                }

                            }
                        } else {
                            iP = node.findSuccessor(CalcHelper.getBigInt(storage.getRootHash()), node);
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
                            }
                        }
                    }
                }
//                else{
//                    System.out.println("storage is still null");
//                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
