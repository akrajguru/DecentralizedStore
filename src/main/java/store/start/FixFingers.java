package store.start;

import store.helper.CalcHelper;
import store.helper.RPCFunctions;
import store.pojo.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FixFingers extends Thread{


    Node node;
    Random rand;

    public FixFingers(Node node) {
        this.node = node;
        rand = new Random();
    }

    @Override
    public void run() {
        try {
        Thread.sleep(5000);
        while(true){
            //System.out.println("in fingerfixer");
            try {
                Thread.sleep(100);
                int i = rand.nextInt(32);
                List keyList = new ArrayList(node.getFingertableMap().keySet());
                Object key = keyList.get(i);
                long start = node.getFingertableMap().get(key).getStart();
                String node1 = node.findSuccessor(start, node);
                node.getFingertableMap().get(key).setNode(new Node(node1));
                String successor = RPCFunctions.getSuccessorOfNode(node1, node);
                if (successor != null) {
                    node.addEntryToSuccessorMap(node1, successor);
                }
                //System.out.println("finger fixed:"+ i+" node: "+ node1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        } catch (InterruptedException e) {
           e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
