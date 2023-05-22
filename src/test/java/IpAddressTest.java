import Blockchain.SmartContractConnection;
import store.helper.NodeHelper;

import java.net.SocketException;

public class IpAddressTest {

    public static void main(String[] args) throws Exception {
        SmartContractConnection con = new SmartContractConnection("0x1e64fa970f8f28c9841a223aa6ceb40b45c5cef0",
                "0xe4fd6950dce3bd61ef090c0b630d138804a420e8ac18dc4c63b2df3ba9e1d42d",
                "http://10.0.0.30:7545");
        con.heartbeat("10.0.0.30:9020");
    }
}
