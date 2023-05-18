import Blockchain.SmartContractConnection;
import chord.Chord;
import com.google.protobuf.ByteString;
import junit.framework.Assert;
import org.apache.zookeeper.common.Time;
import org.junit.jupiter.api.Test;
import org.web3j.abi.datatypes.generated.Bytes32;
import store.Logger.LogToFile;
import store.helper.*;
import store.pojo.*;
import store.start.StartNode;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
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
        PersistAndRetrieveMetadata.persistMetadataToFile(store, node, "primary", "abc");
    }

    private static Storage getFileDetails(String fileName) throws IOException, NoSuchAlgorithmException {
        File file = new File(fileName);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb = new StringBuilder();
        List<Content> contentList = TreeStructure.createNodeList(chunks, sb);
        String[] arr = fileName.split("/");
        String fileName1 = arr[arr.length - 1];
        List<String> fDList = contentList.stream().map(x -> x.getHash()).collect(Collectors.toList());
        Storage storage = new Storage(fileName1, byteArray.length, sb.toString(), fDList);
        return storage;
    }

    @Test
    void getContents() throws IOException, NoSuchAlgorithmException {
        Node node = new Node("10.0.0.30:9000");
        node.setAppPath("/Users/ajinkyarajguru/Documents/CS298_data/metadata");
        String fileName = "/Users/ajinkyarajguru/Downloads/CS298_Proposal.pdf";
        File file = new File(fileName);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb = new StringBuilder();
        List<Content> contentList = TreeStructure.createNodeList(chunks, sb);
        String[] arr = fileName.split("/");
        String fileName1 = arr[arr.length - 1];
        List<String> fDList = contentList.stream().map(x -> x.getHash()).collect(Collectors.toList());
        Storage storage = new Storage(fileName1, byteArray.length, sb.toString(), fDList);
        for (Content content : contentList) {
            Storage store = new Storage(fileName1, sb.toString(), content.getData(), content.getHash(), content.getEndByte());
            PersistAndRetrieveMetadata.persistMetadataToFile(store, node, "primary", "abc");
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
    void getUserDirPath() {
        System.out.println(System.getProperty("user.dir"));

    }

    @Test
    void checkGetAllData() throws IOException {
//        Node node = new Node("10.0.0.30:9020",true);
//        List<Storage> list =PersistAndRetrieveMetadata.getAllDataFromFileStore(node);
//        Assert.assertEquals(2, list.size());
    }

    @Test
    void mapTester() {
        Map<String, String> map = new HashMap<>();
        map.put("ajinkya", "boy");
        map.put("kalyani", "girl");
        map.put("neha", "girl");
        map.put("aadiethya", "boy");
        Optional<String> fileName = map
                .entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), "girl"))
                .map(Map.Entry::getKey)
                .findFirst();
        System.out.println(fileName.get());
    }

    @Test
    void openingAFile() {
        {
            try {
                File file_open = new File("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources/adharcard.pdf");
                if (!Desktop.isDesktopSupported()) {
                    System.out.println("Desktop Support Not Present in the system.");
                    return;
                }
                Desktop desktop = Desktop.getDesktop();
                if (file_open.exists())
                    desktop.open(file_open);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testDeleteNodeInfoCase1() {
        Node node = new Node("10.0.0.30:9020", new LinkedHashMap<String, FingerTable>());
        node.setPredecessor(new Node("10.0.0.30:9010"));
        node.setSuccessor(new Node("10.0.0.30:9030"));
        node.setSuccessorMap(new LinkedHashMap<>());
        node.addEntryToSuccessorMap("10.0.0.30:9010", "10.0.0.30:9020");
        node.addEntryToSuccessorMap("10.0.0.30:9030", "10.0.0.30:9010");
        node.addEntryToSuccessorMap("10.0.0.30:9020", "10.0.0.30:9030");
        List<String> alKeys = new ArrayList<>(node.getFingertableMap().keySet());
        Collections.reverse(alKeys);
//        node.getFingertableMap().put(alKeys.get(0),new FingerTable(alKeys.get(0),node.getSuccessor()));
//        node.getFingertableMap().put(alKeys.get(1),new FingerTable(alKeys.get(1),node.getSuccessor()));
//        node.getFingertableMap().put(alKeys.get(2),new FingerTable(alKeys.get(2),node.getSuccessor()));
        node.deleteUnavailableNodeInfo("10.0.0.30:9030");


    }

    @Test
    void exceptionTester() {
        try {
            int ans = divideFunc();
        } catch (Exception e) {
            System.out.println("exception outside");
        }
        System.out.println("done");
    }

    private int divideFunc() {
        int ans = 0;
        try {
            ans = 1 / 0;
        } catch (Exception e) {
            System.out.println("exception in function");
        }
        return ans;
    }

    @Test
    void decrementTester() {

        int i = 23;
        --i;
        System.out.println(i);


    }

    @Test
    void logTester() {

        LogToFile log = new LogToFile("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources2");

        for (int i = 0; i < 100; i++) {
            log.writeLog("info", "hey there", null);
            log.writeLog("error", "hey there", new Exception("exception mein aya na"));
        }


    }

    @Test
    void TestStabilize() {
        String xNode = "10.0.0.30:9321";
        String myHash = NodeHelper.getNodeHashId("10.0.0.30:9300");
        Node node = new Node("10.0.0.30:9300");
        String succHash = NodeHelper.getNodeHashId("10.0.0.30:9301");
        if ("10.0.0.30:9321" != null) {
            System.out.println("X node is not null" + xNode);
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
    void testMap() {
        Map<String, List<String>> map = new LinkedHashMap();
        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        List<String> list3 = new ArrayList<>();
        list1.add("abcd");
        list1.add("pqre");
        list2.add("list2");
        list3.add("list3");
        map.put("replica1", list1);
        map.put("replica2", list2);

        list2.add("lis2next");

        //map.get("replica2").stream().forEach(x-> System.out.println(x));

        map.put("replica1", map.get("replica2"));
        map.put("replica2", list3);

        map.get("replica1").stream().forEach(x -> System.out.println(x));
    }

    @Test
    void testMapper() {
        Node node = new Node("10.0.0.30:9300");
        Map<String, List<String>> map = new LinkedHashMap<>();
        node.setStorageInfo(new StorageInformation("10.0.0.30:9300"));
//        node.getStorageInfo().setServerStoreInformation(map);
        List<String> list = new LinkedList<>();
        list.add("CNT");
        list.add("fD");
        node.getStorageInfo().getServerStoreInformation().put("replica2", list);
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

        List<FileDetails> fd = TreeStructure.getFileDetailsList("/Users/ajinkyarajguru/Downloads/IMG_8448.MOV");
        //Content c = fd.getContentList().get(0);
        //Storage s = new Storage("abc",fd.getHashOfFile(),c.getData(),c.getHash(),c.getEndByte());

        //  PersistAndRetrieveMetadata.persistMetadataToFile(s,new Node("10.0.0.30:9300",new LinkedHashMap<>()),"primary","");
        //PersistAndRetrieveMetadata.getDataFromHash(null,null,true);
        //PersistAndRetrieveMetadata.deleteFileForOwner("dilip", new Node("10.0.0.30:9300",new LinkedHashMap<>()),true,"KOwns");

        System.out.println("4f79382a578026a6899168ad7c2bbcda6dd036374da0a8b54c57abe684b4525a".getBytes(StandardCharsets.UTF_8).length);

    }

    @Test
    void bytesTest() {
        System.out.println("4f79382a578026a6899168ad7c2bbcda6dd036374da0a8b54c57abe684b4525a".getBytes(StandardCharsets.UTF_8).length);
    }

    @Test
    void testHash() {
        //NodeHelper.getNodeHashId("10.0.0.30:9300");
        for (int i = 0; i < 100; i++) {
            int m = 9000;
            m += i;
            String hex = NodeHelper.encryptPassword(("10.0.0.30:" + String.valueOf(m)));
            BigInteger b = new BigInteger(hex, 16);
            System.out.println(hex + " its value:" + b);
        }
    }

    @Test
    void testBigInt() {
        long t1 = System.currentTimeMillis();
        long myHash = NodeHelper.fnv1aModifiedHash("3876a69b5a67d0b907c1767afd48a2fb2446ab3883e05acc7ee99ae6caa3799c");
        long t2 = System.currentTimeMillis();
        long t3 = t2 - t1;
        System.out.println("time req" + t3);
        System.out.println(myHash);
        t1 = System.currentTimeMillis();
        String myHash2 = NodeHelper.getNodeHashId("10.0.0.30:9800");
        t2 = System.currentTimeMillis();
        t3 = t2 - t1;
        System.out.println("time req" + t3);


//        System.out.println(CalcHelper.getBigInt(myHash));
//        String startHex = CalcHelper.getBigInt(myHash).toString(16);
//        System.out.println(startHex);
//        System.out.println(CalcHelper.getBigInt(startHex));
//        System.out.println("10.0.0.30:9300".hashCode());
//        System.out.println(myHash);
//        System.out.println(BigInteger.valueOf(0xFFFFFFFFL));
    }

    @Test
    void testFinger() {
        System.out.println(NodeHelper.fnv1aModifiedHash("10.0.0.30:9304"));
        for (int i = 1; i <= 32; i++) {
            long a = NodeHelper.getFingerStart32(i, "172.16.0.30:9304");
            System.out.println(a);
        }
    }

    @Test
    void testKeccak() {


        String arr = NodeHelper.getkecckakHash("hasher32");
        System.out.println(arr);
//        Bytes32 byte32 = Bytes32.class
        // System.out.println(Bytes32.DEFAULT.getValue());
//        System.out.println();
//        for(byte b:arr){
//            System.out.print(Byte.valueOf(b));
//        }
        //System.out.println(NodeHelper.Keccak("hasher32"));
    }

    //-82,-62,-115,10,-86,38,22,-30,90,9
    @Test
    void testHashContent() throws Exception {
        FileDetails fd = TreeStructure.getFileDetails("/Users/ajinkyarajguru/Downloads/IMG_8448.MOV");
        System.out.println(SolidityHelper.storeFileSolidity(fd.getContentList(), "ajowns", null));

    }

    @Test
    void testLoop() {
        List<Integer> fileList = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            fileList.add(i);
        }
        int count = 0;
        while (count < fileList.size()) {
            List<Integer> fileContentList = new ArrayList<>();
            for (int i = count; i < fileList.size(); i++) {

                Chord.fileContent fileContent = null;
//                if (fileList.get(i).isContainsContent()) {
//                    fileContent = Chord.fileContent.newBuilder().setFileName(fileList.get(i).getFileName())
//                            .setBlockHash(fileList.get(i).getContentHash()).setRootHash(fileList.get(i).getRootHash())
//                            .setDataContent(ByteString.copyFrom(fileList.get(i).getDataBytes())).setEndOfBlock(fileList.get(i).getEndOfBlock())
//                            .setIsContent(true).setOwner(fileList.get(i).getOwner()).build();
//                } else {
//                    fileContent = Chord.fileContent.newBuilder().setFileName(fileList.get(i).getFileName())
//                            .setRootHash(fileList.get(i).getRootHash()).setFileSize(fileList.get(i).getSize())
//                            .addAllDataFD(fileList.get(i).getContentList()).setOwner(fileList.get(i).getOwner()).build();
//                }
                fileContentList.add(i);
                ++count;
                if (count % 50 == 0) break;
            }
            System.out.println("sending" + count);
        }
    }

    @Test
    void nodeVals() {
        Node node = new Node("127.0.0.1.8080", new LinkedHashMap<>());
        System.out.println(node.getHashId());
        node.getFingertableMap().entrySet().stream().forEach(x -> System.out.println(x.getValue().toString()));
    }

    @Test
    void collectAmount() throws IOException {
        Node node = new Node("10.0.0.30:9312", new LinkedHashMap<>());
        SmartContractConnection cn = new SmartContractConnection("0x1e64fa970f8f28c9841a223aa6ceb40b45c5cef0", "0x9721f88555501c669ee7aac42107bfa7fb9baf3d19b8a8248b22a9d213e3ed23",
                "http://10.0.0.30:7545");
        node.setContract(cn);
        node.setAppPath("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/StorageMetadata-2873464167");
        SolidityHelper.collectAmount(node, "1976a69b5a67d0b907c1767afd48a2fb2446ab3883e05acc7ee99ae6caa3799c");
    }
    //0x565fd

    @Test
    void gasUsed() throws Exception {
        FileDetails fD = TreeStructure.getFileDetails("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources2/IMG_8448.MOV");
        SmartContractConnection cn = new SmartContractConnection("0x1e64fa970f8f28c9841a223aa6ceb40b45c5cef0", "0x9721f88555501c669ee7aac42107bfa7fb9baf3d19b8a8248b22a9d213e3ed23",
                "http://10.0.0.30:7545");
//                        long timeBeforeSolidity = System.currentTimeMillis();
        BigInteger gas = SolidityHelper.storeFileSolidityCalGas(fD.getContentList(), "aj", cn);
        System.out.println("gas used:" + gas);
    }

    @Test
    void testFileEncoding() throws IOException, NoSuchAlgorithmException {
        Node node = new Node("10.0.0.30:9000");
        node.setAppPath("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources/testerdata3");
        String fileName = "/Users/ajinkyarajguru/Downloads/CS298_Proposal.pdf";
        File file = new File(fileName);
//        double start = System.currentTimeMillis();
//        byte[] byteArray;
//        FileInputStream inputStream = new FileInputStream(file);
//        byteArray = inputStream.readAllBytes();
//        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
//        StringBuilder sb = new StringBuilder();
//        List<Content> contentList = TreeStructure.createNodeList(chunks, sb);
//        String[] arr = fileName.split("/");
//        String fileName1 = arr[arr.length - 1];
//        List<String> fDList = contentList.stream().map(x -> x.getHash()).collect(Collectors.toList());
//        Storage storage = new Storage(fileName1, byteArray.length, sb.toString(), fDList);
        FileDetails fD = TreeStructure.getFileDetails(fileName);
        for (Content content : fD.getContentList()) {
            ByteString bs = ByteString.copyFrom(content.getData());
            Storage store = new Storage(fileName, fD.getHashOfFile(), bs.toByteArray(), content.getHash(), content.getEndByte());
            PersistAndRetrieveMetadata.persistMetadataToFile(store, node, "primary", "pqr");
        }

        /*
          File file = new File(filePath);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb= new StringBuilder();
        List<Content> contentList= TreeStructure.createNodeList(chunks,sb);
        String[] arr = filePath.split("/");
        String fileName = arr[arr.length-1];
        FileDetails fD = new FileDetails(fileName,byteArray.length, sb.toString(),  contentList);
         */
        //PersistAndRetrieveMetadata.getDataFromHash()
    }

    @Test
    public void testFileReconstruct() throws IOException, NoSuchAlgorithmException {
        Node node = new Node("10.0.0.30:9000");
        node.setAppPath("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources/testerdata");
        List<Content> content = new ArrayList<>();
        Storage store = PersistAndRetrieveMetadata.getDataFromHash("2f93a2eaaf2b266e1380dc87b33b67d2b4e187435aa5c3db331467423bf89aef", node, true);
        content.add(retCFromStore(store));
        Storage store2 = PersistAndRetrieveMetadata.getDataFromHash("7e35adcc50e1b9c741a0d30010b7d7a37bfb0c21e007338eaeedd0a86bffe93d", node, true);
        Storage store3 = PersistAndRetrieveMetadata.getDataFromHash("c5d864e6cf0e2bad26f686194b63baa785ee673953e9cdf41cd258d7aaf28bef", node, true);
        Storage store4 = PersistAndRetrieveMetadata.getDataFromHash("f807d618952336513f8cfca734ab5609e09a963c8a5b37ac0aea04de9a16f9e3", node, true);

//        PersistAndRetrieveMetadata.persistMetadataToFile(store, node, "primary", "pqr");
//        PersistAndRetrieveMetadata.persistMetadataToFile(store2, node, "primary", "pqr");
//        PersistAndRetrieveMetadata.persistMetadataToFile(store3, node, "primary", "pqr");
//        PersistAndRetrieveMetadata.persistMetadataToFile(store4, node, "primary", "pqr");


        content.add(retCFromStore(store2));
        content.add(retCFromStore(store3));
        content.add(retCFromStore(store4));
        //content.add(retCFromStore(store));
        reconstrcut(content);

        System.out.println(store);
    }

    private void reconstrcut(List<Content> newList) throws IOException, NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Collections.sort(newList, (c1, c2) -> (int) (c1.getEndByte() - c2.getEndByte()));
        newList.stream().forEach(x -> sb.append(x.getHash()));
        String rHashChecker = HashSHA256StoreHelper.createHashFromFileContent(sb.toString().getBytes(StandardCharsets.UTF_8));
        if (rHashChecker.equals("9d6d5ba1d40ddcdaba246ee3dc19922cacf191a7493529942d9e139dc0a9568a")) {

            newList.stream().forEach(x -> {
                try {
                    outputStream.write(x.getData());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/test/testResources/testerdata/" + "recreated3.pdf"));
                outputStream.writeTo(fos);
            } catch (IOException ioe) {
                // Handle exception here
                ioe.printStackTrace();
            } finally {
                fos.close();
            }
            long timeS = System.currentTimeMillis();
        }
    }

    private Content retCFromStore(Storage storage){
        return new Content(storage.getContentHash(),storage.getEndOfBlock(),storage.getDataBytes());
    }





}

// x = 9388678843049699872693523751868732993745455895450095253087370288178975330688
//succ_rel_id = 104746184419508495330189265421246045466027213160551391808614594263847840006779
// x_relid =    70145733255680817016647183804354613911377163843883562658187451729054354231157


