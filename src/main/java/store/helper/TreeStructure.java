package store.helper;

import store.pojo.Chunk;
import store.pojo.Content;
import store.pojo.FileDetails;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class TreeStructure {

    public static List<Content> createNodeList(List<Chunk> chunksForTree,StringBuilder rootHash) throws NoSuchAlgorithmException {
        Collections.sort(chunksForTree, (c1, c2)-> (int) (c1.getEndOfBlock()- c2.getEndOfBlock()));
        List<Content> list = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for(Chunk chunk: chunksForTree){
            Content content = new Content(chunk.getHash(), chunk.getEndOfBlock(),true,chunk.getContent());
            sb.append(chunk.getHash());
            list.add(content);
        }
        rootHash.append(HashSHA256StoreHelper.createHashFromFileContent(sb.toString().getBytes(StandardCharsets.UTF_8)));
        return list;
    }

    public static FileDetails encryptFileBits(FileDetails fD,String key){
        for(Content content: fD.getContentList()){
            byte[] prev = content.getData();
            content.setData(HashSHA256StoreHelper.encrypt(prev,key));
        }
        return fD;
    }

    public static Map<String,String> createMetadata(List<Content> contents) throws IOException {
        Map<String,String> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        // BufferedWriter writer1 = new BufferedWriter(new FileWriter("/Users/ajinkyarajguru/Documents/CS297/Deliverable3/DecentralizedFileSystem/src/main/resources/MyFiles"+"metadata"));
        //writer1.append("filename:abc\n");
        for(Content content : contents){
            //writer1.append(node.getHash()+", ");
            sb.append(content.getHash()+", ");
        }
        //writer1.close();

        map.put("filename","file1");
        map.put("chunks", sb.toString());
        return map;
    }

    public static List<Chunk> getFileChunks(byte[] mainFile) throws NoSuchAlgorithmException {
        int kb = 64 * 1024;
        int endOfChunk=0;
        List<Chunk> chunks = new ArrayList<>();
        for (int i = 0; i < mainFile.length; ) {
            Chunk chunk = new Chunk();
            int min = Math.min(kb, mainFile.length - i);
            byte[] chunkContent = new byte[min];
            endOfChunk+= min;
            for (int j = 0; j < chunkContent.length; j++, i++) {
                chunkContent[j] = mainFile[i];
            }
            chunk.setContent(chunkContent);
            chunk.setHash(HashSHA256StoreHelper.createHashFromFileContent(chunkContent));
            chunk.setEndOfBlock(endOfChunk);
            chunks.add(chunk);
        }
        return chunks;
    }

    public static List<Content> getFileContentsEncrypted(byte[] mainFile, String key,StringBuilder sb) throws NoSuchAlgorithmException {
        int kb = 64 * 1024;
        int endOfChunk=0;
        List<Content> contents = new ArrayList<>();
        for (int i = 0; i < mainFile.length; ) {
            Content chunk = new Content();
            int min = Math.min(kb, mainFile.length - i);
            byte[] chunkContent = new byte[min];
            endOfChunk+= min;
            for (int j = 0; j < chunkContent.length; j++, i++) {
                chunkContent[j] = mainFile[i];
            }
            chunk.setData(HashSHA256StoreHelper.encrypt(chunkContent,key));
            chunk.setHash(HashSHA256StoreHelper.createHashFromFileContent(chunk.getData()));
            sb.append(chunk.getHash());
            chunk.setEndByte(endOfChunk);
            contents.add(chunk);
        }
        return contents;
    }

    public static FileDetails getFileDetails(String filePath) throws IOException, NoSuchAlgorithmException {
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
        return fD;
    }

    public static FileDetails getFileDetailsEncrypted(String filePath,String key) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
       // List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb= new StringBuilder();
        List<Content> contentList = TreeStructure.getFileContentsEncrypted(byteArray,key,sb);
        String[] arr = filePath.split("/");
        String fileName = arr[arr.length-1];
        FileDetails fD = new FileDetails(fileName,byteArray.length, HashSHA256StoreHelper.createHashFromFileContent(sb.toString().getBytes(StandardCharsets.UTF_8)),  contentList);
        return fD;
    }

    public static List<FileDetails> getFileDetailsEncryptedList(String filePath,String key) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        // List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        StringBuilder sb= new StringBuilder();
        List<Content> contentList = TreeStructure.getFileContentsEncrypted(byteArray,key,sb);
        List<FileDetails> fDList = new ArrayList<>();
        int i =0;
        int prev=0;
        String[] arr = filePath.split("/");
        String fileName = arr[arr.length-1];
        while(i< contentList.size()){
            if(i+4000< contentList.size()){
                i+=4000;
            }else{
                i=contentList.size();
            }
            List<Content> sub = contentList.subList(prev, i);
            sb = new StringBuilder();
            StringBuilder finalSb = sb;
            sub.stream().map(x-> finalSb.append(x.getHash()));
            FileDetails fD = new FileDetails(fileName,byteArray.length, HashSHA256StoreHelper.createHashFromFileContent(sb.toString().getBytes(StandardCharsets.UTF_8)),  sub);
            fDList.add(fD);
        }
        return fDList;
    }

    public static List<FileDetails> getFileDetailsList(String filePath) throws IOException, NoSuchAlgorithmException {
        File file = new File(filePath);
        double start = System.currentTimeMillis();
        byte[] byteArray;
        FileInputStream inputStream = new FileInputStream(file);
        byteArray = inputStream.readAllBytes();
        List<Chunk> chunks = TreeStructure.getFileChunks(byteArray);
        List<FileDetails> fDList = new ArrayList<>();
        int startCounter=0;
        int i=0;
        while( i <chunks.size()) {
            if(i+1000 < chunks.size()){
                i+=1000;
            }else{
                i=chunks.size();
            }
            StringBuilder sb = new StringBuilder();
            List<Content> contentList = TreeStructure.createNodeList(chunks.subList(startCounter,i), sb);
            String[] arr = filePath.split("/");
            String fileName = arr[arr.length - 1];
            FileDetails fD = new FileDetails(fileName, byteArray.length, sb.toString(), contentList);
            fDList.add(fD);
            startCounter+=1000;
        }
        return fDList;
    }
}
