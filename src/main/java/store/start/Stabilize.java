package store.start;

import store.helper.CalcHelper;
import store.helper.NodeHelper;
import store.helper.RPCFunctions;
import store.pojo.Node;

import java.math.BigInteger;

public class Stabilize extends Thread {

    Node node;

    public Stabilize(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        try {
        Thread.sleep(5000);
        while (true) {
            try {
                //System.out.println("in stabilize");
                Thread.sleep(5000);
                Node xNode = RPCFunctions.getPredecessorOfNode(node.getSuccessor(), node);
                if (xNode != null) {
                   // System.out.println("X node is not null"+ xNode.getIpAddress());
                    long x = Long.valueOf(xNode.getHashId());
                   // System.out.println("x ="+ x);
                    long succ_rel_id = CalcHelper.calculateRelID32(Long.valueOf(node.getSuccessor().getHashId()), Long.valueOf(node.getHashId()));
                   // System.out.println("succ_rel_id ="+ succ_rel_id);
                    long x_rel_id = CalcHelper.calculateRelID32(x, Long.valueOf(node.getHashId()));
                   // System.out.println("x_rel_id ="+ x_rel_id);
                    if (x_rel_id> 0 && x_rel_id<succ_rel_id) {
                        node.setSuccessor(xNode);
                        System.out.println("setting succ: "+ xNode.getIpAddress() );
                        node.addEntryToSuccessorMap(node.getIpAddress(), xNode.getIpAddress());
                        // delete entries from map so that new succ/pred can receieve files from stablize filestore
                        //null pointer check TODO
                        if(node.getPredecessor()!=null && node.getCheckIfDataSentToServerAlready().containsKey(node.getPredecessor().getIpAddress())){
                            node.getCheckIfDataSentToServerAlready().remove(node.getPredecessor().getIpAddress());
                        }
                        if(node.getCheckIfDataSentToServerAlready().containsKey(node.getSuccessor().getIpAddress())){
                            node.getCheckIfDataSentToServerAlready().remove(node.getSuccessor().getIpAddress());
                        }
                    }
                    if (!node.getIpAddress().equals(node.getSuccessor().getIpAddress()))
                        node.notifyCall(node.getIpAddress(), node.getSuccessor().getIpAddress());
                    // updateSuccessor(successorPort);
                    System.out.println("I am " + node.getIpAddress() + ", successor is:" + node.getSuccessor().getIpAddress());
                    if(node.getPredecessor()!=null && node.getPredecessor().getIpAddress()!=null) {
                        System.out.println("I am " + node.getIpAddress() + ", predcessor is:" + node.getPredecessor().getIpAddress());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
            } catch (Exception e) {
                System.out.println("exception in stabilize");
            }

        }
    }


