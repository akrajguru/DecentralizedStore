package store.start;

import store.helper.CalcHelper;
import store.helper.RPCFunctions;
import store.pojo.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PopulateSuccessorList extends Thread {


    Node node;

    public PopulateSuccessorList(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10000);
            while(true){
                Thread.sleep(10000);
                List<String> list = node.getSuccessorMap().entrySet().stream().map(x -> x.getValue()).collect(Collectors.toList());
                for(String val:list){
                    if(!node.getSuccessorMap().containsKey(val)){
                        String succ = RPCFunctions.getSuccessorOfNode(val,node);
                        if(succ!=null)
                            node.addEntryToSuccessorMap(val,succ);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
