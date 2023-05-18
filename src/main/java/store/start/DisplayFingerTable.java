package store.start;

import store.pojo.Node;

public class DisplayFingerTable extends Thread {

    Node node;

    public DisplayFingerTable(Node node){
        this.node=node;
    }

    @Override
    public void run() {
        try {
        Thread.sleep(20000);
        while(true) {

            Thread.sleep(10000);
            node.getFingertableMap().entrySet().stream().forEach(x -> node.getLogger().writeLog("info",x.getValue().toString(),null));
            node.getLogger().writeLog("info","-----------------------------------------------------X------------------------------------------------",null);
            // node.getFingertableMap().entrySet().stream().forEach(x -> System.out.println(x.getValue().toString()));
            //System.out.println("-----------------------------------------------------X------------------------------------------------");
            System.out.println(node.getStorageInfo().toString());
        }
        } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
}
