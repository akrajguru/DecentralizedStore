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
                System.out.println("in stabilize");
                Thread.sleep(5000);
                Node xNode = RPCFunctions.getPredecessorOfNode(node.getSuccessor(), node);
                if (xNode != null) {
                    BigInteger x = CalcHelper.getBigInt(xNode.getHashId());
                    BigInteger succ_rel_id = CalcHelper.calculateRelID(CalcHelper.getBigInt(node.getSuccessor().getHashId()), CalcHelper.getBigInt(node.getHashId()));
                    BigInteger x_rel_id = CalcHelper.calculateRelID(x, CalcHelper.getBigInt(node.getHashId()));
                    if (x_rel_id.compareTo(BigInteger.ZERO) == 1 && x_rel_id.compareTo(succ_rel_id) < 0) {
                        node.setSuccessor(xNode);
                        node.addEntryToSuccessorMap(node.getIpAddress(), xNode.getIpAddress());
                    }
                    if (!node.getIpAddress().equals(node.getSuccessor().getIpAddress()))
                        node.notifyCall(node.getIpAddress(), node.getSuccessor().getIpAddress());
                    // updateSuccessor(successorPort);
                    System.out.println("I am " + node.getIpAddress() + ", successor is:" + node.getSuccessor().getIpAddress());
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


