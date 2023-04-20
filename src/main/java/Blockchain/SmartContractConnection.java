package Blockchain;


import generated.com.zuehlke.blockchain.model.FileStore;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        this.contractAddress= contractAddress; //contractAddress "0x7eb61c49b69455790ec839f3289330cfcaff5f93"
        this.web3j=Web3j.build(new HttpService(networkIp)); //networkIp http://localhost:7545
        this.credentials = Credentials.create(credentials); //credentials 0xcd7de3919060b47520f4ea46bd6ec06c2a2a4ad3fcebd6111efde2936e1daf6a
        this.transactionManager = new RawTransactionManager(web3j,this.credentials);
        fileStore = loadContract(this.contractAddress);
    }


    public static <bal> void main(String[] args) throws Exception {

        SmartContractConnection cn = new SmartContractConnection(args[0],args[1],args[2]);
        List<String> files = new ArrayList<>();

        if(cn.checkBalanceClient()){
            System.out.println("shrimanta");
        }

//
       // cn.storeServerInformation("10.0.0.30:9304",BigInteger.valueOf(0),files);
        cn.viewInformationStoredOnTheServer("10.0.0.30:9304");
        //cn.transferFunds(web3j, transactionManager);
        //cn.deployContract(web3j,transactionManager);
//        EthGetBalance bal = web3j.ethGetBalance(add, DefaultBlockParameter.valueOf("latest")).sendAsync().get(10, TimeUnit.SECONDS);
//
//        BigInteger balresp = bal.getBalance();
//        System.out.println(balresp);
//        fileStore.addServer("server3",BigInteger.valueOf(20),files).send();

    }
    public  void transferFunds(Web3j client, TransactionManager transactionManager) throws Exception {
        Transfer transfer = new Transfer(client, transactionManager);
        TransactionReceipt transactionReceipt = transfer.sendFunds(
                "0x77B7A36EE890a3eb085D4f19201fa0cE91166Cfd",
                BigDecimal.ONE,
                Convert.Unit.ETHER,
                gasPrice,
                gasLimit
        ).send();
    }

    public  void deployContract(Web3j client, TransactionManager transactionManager) throws Exception {
       String dep = FileStore.deploy(client, transactionManager, gasPrice, gasLimit).send().getContractAddress();
        System.out.println(dep);
    }

    public  FileStore loadContract(String contractAddress){
        return FileStore.load(contractAddress,web3j,credentials,gasPrice,gasLimit);
    }

    public void storeServerInformation(String name, BigInteger numberOfFiles, List<String> fileList) throws Exception {
        fileStore.addServer(name,numberOfFiles,fileList).send();

    }
    public void viewInformationStoredOnTheServer(String serverName) throws Exception {
        Tuple3<String, BigInteger, List<String>> f = fileStore.getServer(serverName).send();
        System.out.println(" name: "+ f.component1()+ " num files:" + f.component2()+ " file names");
        f.component3().stream().forEach(System.out::println);
    }

    public Tuple3<String, BigInteger, List<String>> getInformation(String serverName) throws Exception {
        Tuple3<String, BigInteger, List<String>> f = fileStore.getServer(serverName).send();
        return f;
    }

    public boolean checkBalanceClient() throws ExecutionException, InterruptedException, TimeoutException {
        EthGetBalance bal = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest")).sendAsync().get(10, TimeUnit.SECONDS);
        BigInteger balresp = bal.getBalance();
        System.out.println(balresp);
        // 10 eth
        BigInteger bn = new BigInteger("10000000000000000000");
        if(balresp.compareTo(bn) >=0){
            return true;
        }
        return false;
    }

    public boolean checkBalanceServer() throws ExecutionException, InterruptedException, TimeoutException {
        EthGetBalance bal = web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameter.valueOf("latest")).sendAsync().get(10, TimeUnit.SECONDS);
        BigInteger balresp = bal.getBalance();
        System.out.println(balresp);
        // 5 eth
        BigInteger bn = new BigInteger("5000000000000000000");
        if(balresp.compareTo(bn) >=0){
            return true;
        }
        return false;
    }
}
