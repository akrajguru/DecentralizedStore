//import store.helper.CalcHelper;
//import store.helper.NodeHelper;
//import store.helper.RPCFunctions;
//import store.helper.TreeStructure;
//import store.pojo.FileDetails;
//import store.pojo.FingerTable;
//import store.pojo.Node;
//import store.start.StartNode;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.security.NoSuchAlgorithmException;
//import java.util.*;
//
//public class NodeTester {
//
//
//    public static void main(String[] args) throws InterruptedException {
//
//        StartNode s = new StartNode(new String[]{"9100"});
//        Thread t = new Thread(s);
//        t.start();
//        int iP =9301;
//        List<String> list = new ArrayList<>();
//        for(int i =0; i<20;i++){
//            iP+=i;
//           list.add(String.valueOf(iP));
//        }
//        List<BigInteger> chrono = new ArrayList<>();
//        chrono.add(CalcHelper.getBigInt(NodeHelper.getNodeHashId("10.0.0.30:9100")));
//        Map<String, Thread> map = new HashMap<>();
//        for(String sIp: list) {
//            Thread.sleep(1000);
//            StartNode s1 = new StartNode(new String[]{sIp, "10.0.0.30:9100"});
//            Thread t1 = new Thread(s1);
//            chrono.add(CalcHelper.getBigInt(NodeHelper.getNodeHashId("10.0.0.30:"+sIp)));
//            map.put(sIp,t1);
//            t1.start();
//        }
//
//        Collections.sort(chrono);
//
//
//        boolean quit =false;
//        do {
//            Scanner sc = new Scanner(System.in);
//            System.out.println("1. to delete a server");
//            System.out.println("2. show order");
//            int option = sc.nextInt();
//
//            String nodeAddr=null;
//            switch (option) {
//                // Case
//                case 1:
//                    System.out.println("1. Enter a server to delete");
//                    String file = sc.next();
//                    if(map.containsKey(file)){
//                        map.get(file).stop();
//                    }
//                case 2:
//                    chrono.stream().forEach(System.out::println);
//                case 3:
//                 //
//                    break;
//                case 4:
//                   //
//                    break;
//                case 5:
//                  //
//            }
//        }while(!quit);
//
//
//
//       // node.displayFingerTable();
//      //  Map<String, FingerTable> mapTest = node.getFingertableMap();
//      //  BigInteger prev = null;
//       // int cnt=0;
////        for(Map.Entry<String, FingerTable> m:mapTest.entrySet()){
////            cnt++;
////            if(prev==null){
////                prev = new BigInteger(m.getKey(),16);
////                continue;
////            }else{
////                BigInteger bg = new BigInteger(m.getKey(),16);
////
////                String checker = String.valueOf(bg);
////                BigInteger retran = new BigInteger(checker);
////                System.out.println(retran);
////                System.out.println(checker);
////                System.out.println(bg);
////               if( prev.compareTo(bg) !=-1){
////                   System.out.println("wrong");
////                   break;
////               }
////               prev=bg;
////            }
////
//////        }
////        ArrayList<BigInteger> arr = new ArrayList<>();
////        System.out.println("b5f8c41e6e725a2c7d8fc2f0c75d575c1d337125525e7e7f43b904ea4598f656:  "+ new BigInteger("b5f8c41e6e725a2c7d8fc2f0c75d575c1d337125525e7e7f43b904ea4598f656",16));
////        System.out.println("e972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69: "+ new BigInteger("e972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69",16));
////        System.out.println("dced57fd9773b04678f1775535e8c2f167c82ea883feac9acb172261d1095c61:  "+ new BigInteger("dced57fd9773b04678f1775535e8c2f167c82ea883feac9acb172261d1095c61",16));
////       // System.out.println("e972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69: "+ new BigInteger("e972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69",16));
////       // System.out.println("9050: "+CalcHelper.getBigInt(NodeHelper.getNodeHashId("10.0.0.30:9050")));
////        arr.add(new BigInteger("b5f8c41e6e725a2c7d8fc2f0c75d575c1d337125525e7e7f43b904ea4598f656",16));
////        arr.add(new BigInteger("e972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69",16));
////        arr.add(new BigInteger("dced57fd9773b04678f1775535e8c2f167c82ea883feac9acb172261d1095c61",16));
////        //arr.add(new BigInteger("e972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69",16));
////       // arr.add(CalcHelper.getBigInt(NodeHelper.getNodeHashId("10.0.0.30:9050")));
////        Collections.sort(arr);
////        System.out.println(arr);
////        //System.out.println(CalcHelper.getBigInt("6972ef1af553dd47c007f7f628205bb3cf6d3ba354dab86620c8385c90d0ff69"));
//        //System.out.println(cnt);
//       // System.out.println(NodeHelper.getNodeHashId(("10.0.0.30:900")));
//    }
//}
