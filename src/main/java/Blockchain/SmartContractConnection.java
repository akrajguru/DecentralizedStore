package Blockchain;


//import com.zuehlke.blockchain.model.FileStoreContract;
import com.zuehlke.blockchain.model.FileStoreContract;
import org.web3j.abi.datatypes.Bytes;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import store.helper.NodeHelper;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
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
    FileStoreContract fileStore;

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigInteger gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    public FileStoreContract getFileStore() {
        return fileStore;
    }

    public void setFileStore(FileStoreContract fileStore) {
        this.fileStore = fileStore;
    }

    public SmartContractConnection(String contractAddress, String credentials, String networkIp){
        this.gasPrice = BigInteger.valueOf(875000000L);
        this.gasLimit =  BigInteger.valueOf(6721975L);
        this.contractAddress= contractAddress; //contractAddress "0x8ac81cb2196a6b95f3088e760dfd4aa4505adb86"
        this.web3j=Web3j.build(new HttpService(networkIp)); //networkIp http://localhost:7545
        this.credentials = Credentials.create(credentials); //credentials 0xcd7de3919060b47520f4ea46bd6ec06c2a2a4ad3fcebd6111efde2936e1daf6a
        this.transactionManager = new RawTransactionManager(web3j,this.credentials);
        fileStore = loadContract(this.contractAddress);
    }

    public SmartContractConnection(String credentials, String networkIp){
        this.gasPrice = BigInteger.valueOf(875000000L);
        this.gasLimit =  BigInteger.valueOf(6721975L);
        this.web3j=Web3j.build(new HttpService(networkIp)); //networkIp http://localhost:7545
        this.credentials = Credentials.create(credentials); //credentials 0xcd7de3919060b47520f4ea46bd6ec06c2a2a4ad3fcebd6111efde2936e1daf6a
        this.transactionManager = new RawTransactionManager(web3j,this.credentials);
    }


    public static <bal> void main(String[] args) throws Exception {


        SmartContractConnection cn = new SmartContractConnection(args[0],args[1]);
       // cn.deployContract(cn.web3j,cn.transactionManager);
        cn.checkBalanceClient(Credentials.create("0xdff73a61f68ad4f44fe0a2ebc682669208da3000e5a5fa1e78947344b69922c1"));
        //cn.getWeb3j().

 //cn.deployContract(cn.web3j, cn.transactionManager);
        //SmartContractConnection cn = new SmartContractConnection(args[0],args[1],args[2]);

        //cn.checkBalanceClient(Credentials.create("0xa11d7b8903f5edda6655e7e754dcff93cb8e310b68daf11b3ff8d207e4d1ab3f"));
        //cn.heartbeat();
        //cn.addFileInfo();
        //cn.viewInfo();
        //System.out.println(cn.getIPAddress());

        //cn.getPaidFunction();
//
       // cn.storeServerInformation("10.0.0.30:9304",BigInteger.valueOf(0),files);
        //cn.viewInformationStoredOnTheServer("10.0.0.30:9304");
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

       String dep = FileStoreContract.deploy(client, transactionManager, gasPrice, gasLimit).send().getContractAddress();
       System.out.println(dep);
       contractAddress=dep;
    }

    public  FileStoreContract loadContract(String contractAddress){
        return FileStoreContract.load(contractAddress,web3j,credentials,gasPrice,gasLimit);
    }

//    public void storeServerInformation(String name, BigInteger numberOfFiles, List<String> fileList) throws Exception {
//        //fileStore.addServer(name,numberOfFiles,fileList).send();
//
//    }
//    public void viewInformationStoredOnTheServer(String serverName) throws Exception {
//        //Tuple3<String, BigInteger, List<String>> f = fileStore.getServer(serverName).send();
//        System.out.println(" name: "+ f.component1()+ " num files:" + f.component2()+ " file names");
//        f.component3().stream().forEach(System.out::println);
//    }

//    public Tuple3<String, BigInteger, List<String>> getInformation(String serverName) throws Exception {
//        Tuple3<String, BigInteger, List<String>> f = fileStore.getServer(serverName).send();
//        return f;
//    }

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

    public boolean checkBalanceClient(Credentials cred) throws ExecutionException, InterruptedException, TimeoutException {
        EthGetBalance bal = web3j.ethGetBalance(cred.getAddress(), DefaultBlockParameter.valueOf("latest")).sendAsync().get(10, TimeUnit.SECONDS);
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

        public BigInteger addFileInfo(String filehash, String randomNum, String verificationHash,String owner) throws Exception {
            byte[] bytes = Numeric.hexStringToByteArray(verificationHash);

            //Bytes32 bt = new Bytes32("0x3d5c3927ab5924163a067fb04397589649bae9f09bea03154391d49c4e3d2690".getBytes());
            TransactionReceipt f = fileStore.addFileBlock(filehash, randomNum, bytes, owner, BigInteger.valueOf(600000000000000L)).send();
            System.out.println(f);
            return f.getCumulativeGasUsed();

    }

        public void getInformation() throws Exception {
            TransactionReceipt f = fileStore.getPaid("f1", "2d0ec7431ec5da4a36c5898f77c06eb7487fb7ee65f22cce3a764e68d54cddaa", BigInteger.ZERO).send();
            System.out.println(f);
    }

        public void heartbeat(String ip) throws Exception {
            fileStore.heartBeat(ip).send();
        }

        public void viewInfo() throws Exception {
            Tuple4<String, String, byte[], BigInteger> out = fileStore.blockMap("f1").send();
            System.out.println(out.component1());
            System.out.println(out.component2());
            System.out.println(out.component3().toString());
            System.out.println(out.component4());
        }

        public String getPaidFunction(String fileHash, String hashCalculated) throws Exception {
//        FileStoreContract f1 = FileStoreContract.load(contractAddress, web3j, Credentials.create("0x123dadafaae07e0a577ecbdb930f1b6896abdf81bbee955f702aa2468be83e72"), gasPrice, gasLimit);
//        FileStoreContract f2 = FileStoreContract.load(contractAddress, web3j, Credentials.create("0xa11d7b8903f5edda6655e7e754dcff93cb8e310b68daf11b3ff8d207e4d1ab3f"), gasPrice, gasLimit);
//        FileStoreContract f3 = FileStoreContract.load(contractAddress, web3j, Credentials.create("0xe83aafb42a3b2c2556db477b3362cfbc776fb03e156ccf8bd4fba85d4842643e"), gasPrice, gasLimit);
//        try {
//            f1.getPaid("f1","2d0ec7431ec5da4a36c5898f77c06eb7487fb7ee65f22cce3a764e68d54cddaa",BigInteger.ZERO).send();
//            f2.getPaid("f1","2d0ec7431ec5da4a36c5898f77c06eb7487fb7ee65f22cce3a764e68d54cddaa",BigInteger.ZERO).send();
//            f3.getPaid("f1","2d0ec7431ec5da4a36c5898f77c06eb7487fb7ee65f22cce3a764e68d54cddaa",BigInteger.ZERO).send();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
            TransactionReceipt paid = fileStore.getPaid(fileHash, hashCalculated, BigInteger.ZERO).send();
            return paid.toString();
    }

    public String getIPAddress() throws Exception {
        String iP = fileStore.getIpAddress().send();
        return  iP;
    }




}
