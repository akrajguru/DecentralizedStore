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
                    System.out.println("X node is not null"+ xNode.getIpAddress());
                    BigInteger x = CalcHelper.getBigInt(xNode.getHashId());
                    System.out.println("x ="+ x);
                    BigInteger succ_rel_id = CalcHelper.calculateRelID(CalcHelper.getBigInt(node.getSuccessor().getHashId()), CalcHelper.getBigInt(node.getHashId()));
                    System.out.println("succ_rel_id ="+ succ_rel_id);
                    BigInteger x_rel_id = CalcHelper.calculateRelID(x, CalcHelper.getBigInt(node.getHashId()));
                    System.out.println("x_rel_id ="+ x_rel_id);
                    if (x_rel_id.compareTo(BigInteger.ZERO) == 1 && x_rel_id.compareTo(succ_rel_id) < 0) {
                        node.setSuccessor(xNode);
                        node.addEntryToSuccessorMap(node.getIpAddress(), xNode.getIpAddress());
                    }
                    if (!node.getIpAddress().equals(node.getSuccessor().getIpAddress()))
                        node.notifyCall(node.getIpAddress(), node.getSuccessor().getIpAddress());
                    // updateSuccessor(successorPort);
                    System.out.println("I am " + node.getIpAddress() + ", successor is:" + node.getSuccessor().getIpAddress());
                    System.out.println("I am " + node.getIpAddress() + ", predcessor is:" + node.getPredecessor().getIpAddress());
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


