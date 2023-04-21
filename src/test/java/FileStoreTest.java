import junit.framework.Assert;
import org.apache.zookeeper.common.Time;
import org.junit.jupiter.api.Test;
import store.Logger.LogToFile;
import store.helper.CalcHelper;
import store.helper.NodeHelper;
import store.helper.PersistAndRetrieveMetadata;
import store.helper.TreeStructure;
import store.pojo.*;
import store.start.StartNode;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class FileStoreTest {


    @Test
    void checkStore() throws IOException, NoSuchAlgorithmException {

        Node node = new Node("10.0.0.30:9000");
        node.setAppPath("/Users/ajinkyarajguru/Documents/CS298_data/metadata");
        Storage store = getFileDetails("/Users/ajinkyarajguru/Downloads/CS298_Proposal.pdf");
        PersistAndRetrieveMetadata.persistMetadataToFile(store,node,"primary","abc");
    }

    private static Storage getFileDetails(String fileName) throws IOException, NoSuchAlgorithmException {
        File file = new File(fileName);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb= new StringBuilder();
        List<Content> contentList= TreeStructure.createNodeList(chunks,sb);
        String[] arr = fileName.split("/");
        String fileName1 = arr[arr.length-1];
        List<String> fDList = contentList.stream().map(x -> x.getHash()).collect(Collectors.toList());
        Storage storage = new Storage(fileName1,byteArray.length,sb.toString(),   fDList);
        return storage;
    }
    @Test
    void getContents() throws IOException, NoSuchAlgorithmException {
        Node node = new Node("10.0.0.30:9000");
        node.setAppPath("/Users/ajinkyarajguru/Documents/CS298_data/metadata");
        String fileName= "/Users/ajinkyarajguru/Downloads/CS298_Proposal.pdf";
        File file = new File(fileName);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb= new StringBuilder();
        List<Content> contentList= TreeStructure.createNodeList(chunks,sb);
        String[] arr = fileName.split("/");
        String fileName1 = arr[arr.length-1];
        List<String> fDList = contentList.stream().map(x -> x.getHash()).collect(Collectors.toList());
        Storage storage = new Storage(fileName1,byteArray.length,sb.toString(),   fDList);
        for(Content content: contentList){
            Storage store = new Storage(fileName1,sb.toString(), content.getData(), content.getHash(),  content.getEndByte());
            PersistAndRetrieveMetadata.persistMetadataToFile(store,node,"primary","abc");
        }
    }
    @Test
    void retrieveFile() throws IOException {
        Node node = new Node("10.0.0.30:9000");
        node.setAppPath("/Users/ajinkyarajguru/Documents/CS298_data/metadata");
        Storage store = PersistAndRetrieveMetadata.getDataFromHash("2f93a2eaaf2b266e1380dc87b33b67d2b4e187435aa5c3db331467423bf89aef", node, true);
        System.out.println(store);
    }
    @Test
    void getUserDirPath(){
        System.out.println(System.getProperty("user.dir"));

    }
    @Test
    void checkGetAllData() throws IOException {
//        Node node = new Node("10.0.0.30:9020",true);
//        List<Storage> list =PersistAndRetrieveMetadata.getAllDataFromFileStore(node);
//        Assert.assertEquals(2, list.size());
    }

    @Test
    void mapTester(){
        Map<String, String> map = new HashMap<>();
        map.put("ajinkya","boy");
        map.put("kalyani", "girl");
        map.put("neha","girl");
        map.put("aadiethya","boy");
        Optional<String> fileName = map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), "girl"))
                .map(Map.Entry::getKey)
                .findFirst();
        System.out.println(fileName.get());
    }
    @Test
    void openingAFile(){
        {
            try
            {
                File file_open = new File("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources/adharcard.pdf");
                if(!Desktop.isDesktopSupported())
                {
                    System.out.println("Desktop Support Not Present in the system.");
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                if(file_open.exists())
                    desktop.open(file_open);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    @Test
    void testDeleteNodeInfoCase1(){
        Node node = new Node("10.0.0.30:9020", new LinkedHashMap<String,FingerTable>());
        node.setPredecessor(new Node("10.0.0.30:9010"));
        node.setSuccessor(new Node("10.0.0.30:9030"));
        node.setSuccessorMap(new LinkedHashMap<>());
        node.addEntryToSuccessorMap("10.0.0.30:9010","10.0.0.30:9020");
        node.addEntryToSuccessorMap("10.0.0.30:9030","10.0.0.30:9010");
        node.addEntryToSuccessorMap("10.0.0.30:9020","10.0.0.30:9030");
        List<String> alKeys = new ArrayList<>(node.getFingertableMap().keySet());
        Collections.reverse(alKeys);
//        node.getFingertableMap().put(alKeys.get(0),new FingerTable(alKeys.get(0),node.getSuccessor()));
//        node.getFingertableMap().put(alKeys.get(1),new FingerTable(alKeys.get(1),node.getSuccessor()));
//        node.getFingertableMap().put(alKeys.get(2),new FingerTable(alKeys.get(2),node.getSuccessor()));
        node.deleteUnavailableNodeInfo("10.0.0.30:9030");



    }
    @Test
    void exceptionTester(){
        try{
           int ans = divideFunc();
        }catch(Exception e){
            System.out.println("exception outside");
        }
        System.out.println("done");
    }

    private int divideFunc() {
        int ans = 0;
        try {
            ans= 1 / 0;
        }catch(Exception e){
            System.out.println("exception in function");
        }
        return ans;
    }

    @Test
    void decrementTester(){

        int i =23;
        --i;
        System.out.println(i);


    }

    @Test
    void logTester(){

        LogToFile log= new LogToFile("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources2");

        for(int i =0;i<100;i++){
            log.writeLog("info","hey there",null);
            log.writeLog("error","hey there",new Exception("exception mein aya na"));
        }


    }
@Test
    void TestStabilize(){
            String xNode ="10.0.0.30:9321";
            String myHash = NodeHelper.getNodeHashId("10.0.0.30:9300");
            Node node = new Node("10.0.0.30:9300");
        String succHash = NodeHelper.getNodeHashId("10.0.0.30:9301");
        if ("10.0.0.30:9321" != null) {
            System.out.println("X node is not null"+ xNode);
            BigInteger x = CalcHelper.getBigInt(NodeHelper.getNodeHashId(xNode));
            BigInteger succ_rel_id = CalcHelper.calculateRelID(CalcHelper.getBigInt(succHash), CalcHelper.getBigInt(myHash));
            BigInteger x_rel_id = CalcHelper.calculateRelID(x, CalcHelper.getBigInt(myHash));
            if (x_rel_id.compareTo(BigInteger.ZERO) == 1 && x_rel_id.compareTo(succ_rel_id) < 0) {
                node.setSuccessor(new Node(xNode));
                //node.addEntryToSuccessorMap(node.getIpAddress(), xNode.getIpAddress());
            }
            if (!node.getIpAddress().equals(node.getSuccessor().getIpAddress()))
                node.notifyCall(node.getIpAddress(), node.getSuccessor().getIpAddress());
            // updateSuccessor(successorPort);
            System.out.println("I am " + node.getIpAddress() + ", successor is:" + node.getSuccessor().getIpAddress());
            System.out.println("I am " + node.getIpAddress() + ", predcessor is:" + node.getPredecessor().getIpAddress());
        }
    }

    @Test
    void testMap(){
        Map<String,List<String >> map = new LinkedHashMap();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        list1.add("abcd");
        list1.add("pqre");
        list2.add("list2");
        list3.add("list3");
        map.put("replica1",list1 );
        map.put("replica2", list2);

        list2.add("lis2next");

       //map.get("replica2").stream().forEach(x-> System.out.println(x));

       map.put("replica1",map.get("replica2"));
       map.put("replica2", list3);

        map.get("replica1").stream().forEach(x-> System.out.println(x));
    }

    @Test
    void testMapper(){
        Node node = new Node("10.0.0.30:9300");
        Map<String, List<String>> map = new LinkedHashMap<>();
        node.setStorageInfo(new StorageInformation("10.0.0.30:9300"));
//        node.getStorageInfo().setServerStoreInformation(map);
        List<String> list = new LinkedList<>();
        list.add("CNT");
        list.add("fD");
        node.getStorageInfo().getServerStoreInformation().put("replica2",list);
        List<String> replica1 = new ArrayList<>();
        replica1 = node.getStorageInfo().getServerStoreInformation().get("replica1");
        node.getStorageInfo().getServerStoreInformation().get("primary").addAll(replica1);
        List<String> oldReplica2 = node.getStorageInfo().getServerStoreInformation().get("replica2");
        node.getStorageInfo().getServerStoreInformation().put("replica1", oldReplica2);
        node.getStorageInfo().getServerStoreInformation().put("replica2", new ArrayList<>());
        System.out.println(node.getStorageInfo().toString());
        node.getStorageInfo().getServerStoreInformation().get("replica1").add("newAdded");
        System.out.println(node.getStorageInfo().toString());
    }

    @Test
    void testerWhole() throws IOException, NoSuchAlgorithmException {

        FileDetails fd = TreeStructure.getFileDetails("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources2/adharcard.pdf");
        Content c = fd.getContentList().get(0);
        Storage s = new Storage("abc",fd.getHashOfFile(),c.getData(),c.getHash(),c.getEndByte());

     //  PersistAndRetrieveMetadata.persistMetadataToFile(s,new Node("10.0.0.30:9300",new LinkedHashMap<>()),"primary","");
        //PersistAndRetrieveMetadata.getDataFromHash(null,null,true);
        PersistAndRetrieveMetadata.deleteFileForOwner("dilip", new Node("10.0.0.30:9300",new LinkedHashMap<>()),true,"KOwns");


    }

    @Test
    void testHash(){
        //NodeHelper.getNodeHashId("10.0.0.30:9300");
        for(int i =0;i<100;i++) {
            int m=9000;
            m+=i;
            String hex = NodeHelper.encryptPassword(("10.0.0.30:"+String.valueOf(m)));
            BigInteger b = new BigInteger(hex,16);
            System.out.println(hex+ " its value:"+b);
        }
    }

    @Test
    void testBigInt(){
        long t1 = System.currentTimeMillis();
        long myHash = NodeHelper.fnv1aModifiedHash("3876a69b5a67d0b907c1767afd48a2fb2446ab3883e05acc7ee99ae6caa3799c");
        long t2 =System.currentTimeMillis();
        long t3=t2-t1;
        System.out.println("time req"+t3);
        System.out.println(myHash);
         t1 = System.currentTimeMillis();
        String myHash2 = NodeHelper.getNodeHashId("10.0.0.30:9800");
         t2 =System.currentTimeMillis();
         t3=t2-t1;
        System.out.println("time req"+t3);


//        System.out.println(CalcHelper.getBigInt(myHash));
//        String startHex = CalcHelper.getBigInt(myHash).toString(16);
//        System.out.println(startHex);
//        System.out.println(CalcHelper.getBigInt(startHex));
//        System.out.println("10.0.0.30:9300".hashCode());
//        System.out.println(myHash);
//        System.out.println(BigInteger.valueOf(0xFFFFFFFFL));
    }

    @Test
    void testFinger(){
        System.out.println(NodeHelper.fnv1aModifiedHash("10.0.0.30:9304"));
        for(int i =1;i<=32;i++) {
            long a = NodeHelper.getFingerStart32(i, "172.16.0.30:9304");
            System.out.println(a);
        }
    }
}
// x = 9388678843049699872693523751868732993745455895450095253087370288178975330688
//succ_rel_id = 104746184419508495330189265421246045466027213160551391808614594263847840006779
// x_relid =    70145733255680817016647183804354613911377163843883562658187451729054354231157


