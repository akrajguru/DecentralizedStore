package store.helper;

import store.pojo.Node;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Formatter;

public class NodeHelper {


    public static String getNodeHashId(String ipAddress) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(ipAddress.getBytes());
            String hashHex = bytesToHex(hashBytes);
            return hashHex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getFingerStart(int k, String ipAddress) {
        String hashHex = getNodeHashId(ipAddress);
        BigInteger hashInt = new BigInteger(hashHex, 16);
        BigInteger m = BigInteger.valueOf(2).pow(256);
        BigInteger start = hashInt.add(BigInteger.valueOf(2).pow(k - 1)).mod(m);
        String startHex = start.toString(16);
        if (startHex.length() < 64) {
            // pad with zeros to ensure consistent length
            startHex = String.format("%0" + (64 - startHex.length()) + "d%s", 0, startHex);
        }
        return startHex;
    }

    public static long getFingerStart32(int k, String ipAddress) {
        long hashHex = fnv1aModifiedHash(ipAddress);
       // BigInteger hashInt = new BigInteger(hashHex, 16);
        double m = Math.pow(2,32);
        double start = (hashHex +(Math.pow(2,k-1))) %m ;
//        String startHex = start.toString(16);
//        if (startHex.length() < 64) {
//            // pad with zeros to ensure consistent length
//            startHex = String.format("%0" + (64 - startHex.length()) + "d%s", 0, startHex);
//        }
        return (long)start;
    }

    public Node getFingerSuccessor(int k) {
//        String start = getFingerStart(k);
//        Node successor = findSuccessor(start,null);
//        return successor;
        return null;
    }



    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String getIPAddress() throws SocketException {
        Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
        while (nics.hasMoreElements()) {
            NetworkInterface nic = nics.nextElement();
            Enumeration<InetAddress> addresses = nic.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (!address.isLinkLocalAddress() && !address.isLoopbackAddress()
                        && address instanceof Inet4Address) {
                    return address.getHostAddress();
                }
            }
        }
        return null;
    }

    private static final int FNV_OFFSET_BASIS_32 = 0x811c9dc5;
    private static final int FNV_PRIME_32 = 0x01000193;

    public static int hash32(byte[] data) {
        int hash = FNV_OFFSET_BASIS_32;
        for (byte b : data) {
            hash ^= (b & 0xff); // XOR the low 8 bits of the byte into the hash
            hash *= FNV_PRIME_32;
        }
        return hash;
    }

    public static String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("MD5");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    private static String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static long fnv1aModifiedHash(String data1) {
        byte[] data = data1.getBytes(StandardCharsets.UTF_8);
        final int prime = 0x01000193;
        long hash = 0x811c9dc5;
        for (byte b : data) {
            hash ^= b;
            hash *= prime;
        }
        return hash & 0xFFFFFFFFL;
    }


}
