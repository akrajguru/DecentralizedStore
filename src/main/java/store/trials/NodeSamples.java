package store.trials;

import store.helper.HashSHA256;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

public class NodeSamples {

    String hash;
    String port;

    public String getHash() {
        return hash;
    }

    public String getPort() {
        return port;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public BigInteger generateHash(String port) {
        BigInteger hashB = null;
        try {
             hashB = HashSHA256.createHashFromFileContent(port.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashB;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        NodeSamples n = new NodeSamples();
        System.out.println(n.generateHash("localhost:8080"));
        BigInteger A = n.generateHash("localhost:8080");
        BigInteger B = n.generateHash("192.168.0.1:5000");
        System.out.println(B);
        System.out.println(A.compareTo(B));
        String hex=HashSHA256.createHashFromFileContentHex("192.168.0.1:5000".getBytes(StandardCharsets.UTF_8));
        System.out.println(hex);
        BigInteger X = new BigInteger(hex,16);
        System.out.println(X);
        BigInteger b = new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff",16);
        System.out.println(b);
//        BigInteger C = BigInteger.valueOf(-2);
//        BigInteger D = BigInteger.valueOf(Integer.MAX_VALUE);
//        BigInteger E = C.pow(Integer.MAX_VALUE);
//        System.out.println(E);
//        for(int i=1; i<=7;i++) {
//            BigInteger start = (A.add(BigInteger.valueOf((long) Math.pow(2, i - 1))).mod(BigInteger.valueOf(Integer.MAX_VALUE)));
//            BigInteger end = (A.add(BigInteger.valueOf((long) Math.pow(2, i - 1))).mod(BigInteger.valueOf(Integer.MAX_VALUE)));
//            FingerTable fT = new FingerTable(myNodeId, myPort, start, end);
//            fingerTableForNode.add(i - 1, fT);
//        }
    }
//            109632357765126371919995172876458605737156600462501694669597139394859723095835
//            6159731472189823503575812132229302116113384203138869369860444613053406544101
}
//f261b72d5e21fe9a0e46e7cfa7464d689a878e40a6d5b307baa2b1c30ba37f1b

/*

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Node {
    private String ipAddress;
    private BigInteger id;
    private Map<BigInteger, Node> fingertable;

    public Node(String ipAddress) {
        this.ipAddress = ipAddress;
        this.id = getNodeId(ipAddress);
        this.fingertable = new HashMap<>();
        // initialize fingertable entries
        for (int i = 1; i <= 160; i++) {
            BigInteger start = getFingerStart(i);
            fingertable.put(start, null);
        }
    }

    private BigInteger getNodeId(String ipAddress) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(ipAddress.getBytes());
            BigInteger hashInt = new BigInteger(1, hashBytes);
            BigInteger m = BigInteger.valueOf(2).pow(160);
            return hashInt.mod(m);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private BigInteger getFingerStart(int k) {
        BigInteger m = BigInteger.valueOf(2).pow(160);
        BigInteger hashInt = getNodeId(ipAddress);
        BigInteger start = hashInt.add(BigInteger.valueOf(2).pow(k-1)).mod(m);
        return start;
    }

    public Node getFingerSuccessor(int k) {
        BigInteger start = getFingerStart(k);
        Node successor = findSuccessor(start);
        return successor;
    }

    private Node findSuccessor(BigInteger id) {
        // implementation omitted for brevity
    }

    // other methods omitted for brevity
}

 */


/*
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Node {
    private String ipAddress;
    private String id;
    private Map<String, Node> fingertable;

    public Node(String ipAddress) {
        this.ipAddress = ipAddress;
        this.id = getNodeId(ipAddress);
        this.fingertable = new HashMap<>();
        // initialize fingertable entries
        for (int i = 1; i <= 160; i++) {
            String start = getFingerStart(i);
            fingertable.put(start, null);
        }
    }

    private String getNodeId(String ipAddress) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(ipAddress.getBytes());
            String hashHex = bytesToHex(hashBytes);
            return hashHex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFingerStart(int k) {
        String hashHex = getNodeId(ipAddress);
        BigInteger hashInt = new BigInteger(hashHex, 16);
        BigInteger m = BigInteger.valueOf(2).pow(160);
        BigInteger start = hashInt.add(BigInteger.valueOf(2).pow(k-1)).mod(m);
        String startHex = start.toString(16);
        if (startHex.length() < 40) {
            // pad with zeros to ensure consistent length
            startHex = String.format("%0" + (40 - startHex.length()) + "d%s", 0, startHex);
        }
        return startHex;
    }

    public Node getFingerSuccessor(int k) {
        String start = getFingerStart(k);
        Node successor = findSuccessor(start);
        return successor;
    }

    private Node findSuccessor(String id) {
        // implementation omitted for brevity
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // other methods omitted for brevity
}


 */

/*


 */