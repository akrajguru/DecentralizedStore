import junit.framework.Assert;
import org.junit.jupiter.api.Test;
import store.helper.PersistAndRetrieveMetadata;
import store.helper.TreeStructure;
import store.pojo.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        PersistAndRetrieveMetadata.persistMetadataToFile(store,node);
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
            PersistAndRetrieveMetadata.persistMetadataToFile(store,node);
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
        Node node = new Node("10.0.0.30:9020",true);
        List<Storage> list =PersistAndRetrieveMetadata.getAllDataFromFileStore(node);
        Assert.assertEquals(2, list.size());
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
        node.getFingertableMap().put(alKeys.get(0),new FingerTable(alKeys.get(0),node.getSuccessor()));
        node.getFingertableMap().put(alKeys.get(1),new FingerTable(alKeys.get(1),node.getSuccessor()));
        node.getFingertableMap().put(alKeys.get(2),new FingerTable(alKeys.get(2),node.getSuccessor()));
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


}
