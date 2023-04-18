package Blockchain;


import generated.com.zuehlke.blockchain.model.FileStore;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SmartContractConnection {
    BigInteger gasPrice;
    BigInteger gasLimit;

    String contractAddress;
    TransactionManager transactionManager;
    Credentials credentials;
    Web3j web3j;
    FileStore fileStore;

    public SmartContractConnection(String contractAddress, String credentials,String networkIp){
        this.gasPrice = BigInteger.valueOf(20000000000L);
        this.gasLimit =  BigInteger.valueOf(6721975L);
        this.contractAddress="0x7eb61c49b69455790ec839f3289330cfcaff5f93"; //contractAddress
        this.web3j=Web3j.build(new HttpService("http://localhost:7545")); //networkIp
        this.credentials = Credentials.create("0xcd7de3919060b47520f4ea46bd6ec06c2a2a4ad3fcebd6111efde2936e1daf6a"); //credentials
        this.transactionManager = new RawTransactionManager(web3j,this.credentials);
        fileStore = loadContract(this.contractAddress);
    }


    public static <bal> void main(String[] args) throws Exception {

        SmartContractConnection cn = new SmartContractConnection(null,null,null);
        List<String> files = new ArrayList<>();

        for(int i =0; i<10;i++){
            files.add("ef781a1677facffd5770b6f91c761e78a45f5c850b670262de69c03650e5ccce");
        }
        cn.storeServerInformation("server5",BigInteger.valueOf(20),files);
        //cn.transferFunds(web3j, transactionManager);
        //cn.deployContract(web3j,transactionManager);
//        EthGetBalance bal = web3j.ethGetBalance(add, DefaultBlockParameter.valueOf("latest")).sendAsync().get(10, TimeUnit.SECONDS);
//
//        BigInteger balresp = bal.getBalance();
//        System.out.println(balresp);
//        fileStore.addServer("server3",BigInteger.valueOf(20),files).send();

    }
    private  void transferFunds(Web3j client, TransactionManager transactionManager) throws Exception {
        Transfer transfer = new Transfer(client, transactionManager);
        TransactionReceipt transactionReceipt = transfer.sendFunds(
                "0x77B7A36EE890a3eb085D4f19201fa0cE91166Cfd",
                BigDecimal.ONE,
                Convert.Unit.ETHER,
                gasPrice,
                gasLimit
        ).send();
    }

    private  void deployContract(Web3j client, TransactionManager transactionManager) throws Exception {
       String dep = FileStore.deploy(client, transactionManager, gasPrice, gasLimit).send().getContractAddress();
        System.out.println(dep);
    }

    private  FileStore loadContract(String contractAddress){
        return FileStore.load(contractAddress,web3j,credentials,gasPrice,gasLimit);
    }

    private void storeServerInformation(String name, BigInteger numberOfFiles, List<String> fileList) throws Exception {
        fileStore.addServer(name,numberOfFiles,fileList).send();

    }
    private void viewInformationStoredOnTheServer(String serverName) throws Exception {
        Tuple3<String, BigInteger, List<String>> f = fileStore.getServer("server3").send();
        System.out.println(" name: "+ f.component1()+ " num files:" + f.component2()+ " file names");
        f.component3().stream().forEach(System.out::println);
    }
}
