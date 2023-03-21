package store.start;

import store.helper.CalcHelper;
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
        Thread.sleep(1000);
        while(true){
            Thread.sleep(100);
            int i =   rand.nextInt(256);
            List keyList =  new ArrayList(node.getFingertableMap().keySet());
            Object key=keyList.get(i);
            String start = node.getFingertableMap().get(key).getStart();
            String node1 = node.findSuccessor(CalcHelper.getBigInt(start),node);
            node.getFingertableMap().get(key).setNode(new Node(node1));
            //System.out.println("finger fixed:"+ i+" node: "+ node1);

        }
        } catch (InterruptedException e) {
           e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
