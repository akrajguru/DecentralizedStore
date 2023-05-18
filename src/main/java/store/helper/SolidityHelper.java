package store.helper;

import Blockchain.SmartContractConnection;
import org.jetbrains.annotations.NotNull;
import store.pojo.Content;
import store.pojo.Node;
import store.pojo.Storage;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SolidityHelper {


    public static String storeFileSolidity(List<Content> contents, String owner, SmartContractConnection cn) throws Exception {
        for(Content content : contents){
        byte[] bytes = Arrays.copyOf(content.getData(),content.getData().length);
        int num = generateRandomNumber(bytes.length);
        ByteBuffer b = getByteBufferFromRandomNumber(bytes, num);
        String keccak = NodeHelper.Keccak(b.array());
        //System.out.print(keccak + " ");
        String verificationHash= NodeHelper.Keccak((keccak + num).getBytes());
        cn.addFileInfo(content.getHash(),String.valueOf(num),verificationHash,owner);

        //NodeHelper.Keccak(
    }
        return cn.getIPAddress();
    }

    public static BigInteger storeFileSolidityCalGas(List<Content> contents, String owner, SmartContractConnection cn) throws Exception {
        BigInteger cumGas = BigInteger.ZERO;
        for(Content content : contents){
            byte[] bytes = content.getData();
            int num = generateRandomNumber(bytes.length);
            ByteBuffer b = getByteBufferFromRandomNumber(bytes, num);
            String keccak = NodeHelper.Keccak(b.array());
            //System.out.print(keccak + " ");
            String verificationHash= NodeHelper.Keccak((keccak + num).getBytes());
            BigInteger g1 = cn.addFileInfo(content.getHash(),String.valueOf(num),verificationHash,owner);
            cumGas = cumGas.add(g1);

            //NodeHelper.Keccak(
        }
        return cumGas;
    }

    public static String getHashForVerification(byte[] bytes, SmartContractConnection cn,String fileHash) throws Exception {
            int num = generateRandomNumber(bytes.length);
            num = Integer.parseInt(cn.getFileStore().getRandomNumberForVerification(fileHash).send());
            ByteBuffer b = getByteBufferFromRandomNumber(bytes, num);
            String keccak = NodeHelper.Keccak(b.array());
            System.out.print(keccak + " ");
            return keccak;
    }

    public static boolean collectAmount(Node node, String fileName) throws IOException {

        Storage storage=PersistAndRetrieveMetadata.getDataFromHash(fileName,node,true);
        try {
            String hash =getHashForVerification(storage.getDataBytes(),node.getContract(),storage.getContentHash());

            System.out.println(node.getContract().getPaidFunction(storage.getContentHash(),hash)+" got paid for: "+ fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        return true;
    }

    @NotNull
    private static ByteBuffer getByteBufferFromRandomNumber(byte[] bytes, int num) {
        int byteValue = num & 0xFF;
        byte positiveByteValue = (byteValue >= 0 ? (byte) byteValue : (byte) (256 + byteValue));
        ByteBuffer b = ByteBuffer.wrap(bytes);
        b.put(num, positiveByteValue);
        return b;
    }

    public static String calculateHashFileSolidity(byte[] arr,int rand,byte[] randArray){
        ByteBuffer b = ByteBuffer.wrap(arr);
        b.put(randArray);
        String keccak = NodeHelper.Keccak(b.array());
        return keccak;
    }

//    private static byte generateRandomArray(){
//        Random rand = new Random();
//
//        //return rand.next;
//
//
//    }

    private static int generateRandomNumber(int limit){
        Random rand = new Random();
        int int_random = rand.nextInt(limit);
        return int_random;
    }

}
