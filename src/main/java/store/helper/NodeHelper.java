package store.helper;

import store.pojo.Node;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

public class NodeHelper {


    public static String getNodeHashId(String ipAddress) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
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


}
