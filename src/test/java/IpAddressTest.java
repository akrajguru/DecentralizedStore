import store.helper.NodeHelper;

import java.net.SocketException;

public class IpAddressTest {

    public static void main(String[] args) throws SocketException {
        String var = NodeHelper.getIPAddress();
        System.out.println(var);
    }
}
