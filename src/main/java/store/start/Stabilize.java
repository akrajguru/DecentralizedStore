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
        Thread.sleep(1000);
        while (true) {
           // System.out.println("in stabilize");
            Thread.sleep(1000);
            Node xNode = RPCFunctions.getPredecessorOfNode(node.getSuccessor(), node);
            BigInteger x = CalcHelper.getBigInt(xNode.getHashId());
            BigInteger succ_rel_id = CalcHelper.calculateRelID(CalcHelper.getBigInt(node.getSuccessor().getHashId()), CalcHelper.getBigInt(node.getHashId()));
            BigInteger x_rel_id = CalcHelper.calculateRelID(x, CalcHelper.getBigInt(node.getHashId()));
            if (x_rel_id.compareTo(BigInteger.ZERO) == 1 && x_rel_id.compareTo(succ_rel_id) < 0) {
                node.setSuccessor(xNode);
            }
            if (!node.getIpAddress().equals(node.getSuccessor().getIpAddress()))
                node.notifyCall(node.getIpAddress(), node.getSuccessor().getIpAddress());
            // updateSuccessor(successorPort);
            //  System.out.println("successor is:" + successorPort);
        }
            } catch (Exception e) {
                System.out.println("exception in stabilize");
            }

        }
    }


