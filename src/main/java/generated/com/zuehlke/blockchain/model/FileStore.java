package generated.com.zuehlke.blockchain.model;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.9.4.
 */
@SuppressWarnings("rawtypes")
public class FileStore extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50610a25806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c80632e6439451461005c5780633080f0d714610071578063b4460b8014610087578063cc4516e5146100a8578063e9b9a095146100bb575b600080fd5b61006f61006a366004610710565b6100dd565b005b6001546040519081526020015b60405180910390f35b61009a610095366004610774565b610191565b60405161007e92919061080d565b61006f6100b636600461082f565b610240565b6100ce6100c9366004610774565b61030b565b60405161007e9392919061091f565b600080836040516100ee9190610998565b90815260200160405180910390209050600081600001805461010f906109b4565b9050116101565760405162461bcd60e51b815260206004820152601060248201526f14d95c9d995c881b9bdd08199bdd5b9960821b60448201526064015b60405180910390fd5b600281018054600181018255600091825260209182902084516101819391909201919085019061050d565b5060028101546001909101555050565b80516020818301810180516000825292820191909301209152805481906101b7906109b4565b80601f01602080910402602001604051908101604052809291908181526020018280546101e3906109b4565b80156102305780601f1061020557610100808354040283529160200191610230565b820191906000526020600020905b81548152906001019060200180831161021357829003601f168201915b5050505050908060010154905082565b60006040518060600160405280858152602001848152602001838152509050806000856040516102709190610998565b9081526020016040518091039020600082015181600001908051906020019061029a92919061050d565b506020828101516001830155604083015180516102bd9260028501920190610591565b5050600180548082018255600091909152855161030492507fb10e2d527612073b26eecdfd717e6a320cf44b4afac2b0732d9fcbe2b7fa0cf690910190602087019061050d565b5050505050565b606060006060600080856040516103229190610998565b908152602001604051809103902060405180606001604052908160008201805461034b906109b4565b80601f0160208091040260200160405190810160405280929190818152602001828054610377906109b4565b80156103c45780601f10610399576101008083540402835291602001916103c4565b820191906000526020600020905b8154815290600101906020018083116103a757829003601f168201915b505050505081526020016001820154815260200160028201805480602002602001604051908101604052809291908181526020016000905b828210156104a857838290600052602060002001805461041b906109b4565b80601f0160208091040260200160405190810160405280929190818152602001828054610447906109b4565b80156104945780601f1061046957610100808354040283529160200191610494565b820191906000526020600020905b81548152906001019060200180831161047757829003601f168201915b5050505050815260200190600101906103fc565b505050915250508051519091506104f45760405162461bcd60e51b815260206004820152601060248201526f14d95c9d995c881b9bdd08199bdd5b9960821b604482015260640161014d565b8051602082015160409092015190969195509350915050565b828054610519906109b4565b90600052602060002090601f01602090048101928261053b5760008555610581565b82601f1061055457805160ff1916838001178555610581565b82800160010185558215610581579182015b82811115610581578251825591602001919060010190610566565b5061058d9291506105ea565b5090565b8280548282559060005260206000209081019282156105de579160200282015b828111156105de57825180516105ce91849160209091019061050d565b50916020019190600101906105b1565b5061058d9291506105ff565b5b8082111561058d57600081556001016105eb565b8082111561058d576000610613828261061c565b506001016105ff565b508054610628906109b4565b6000825580601f10610638575050565b601f01602090049060005260206000209081019061065691906105ea565b50565b634e487b7160e01b600052604160045260246000fd5b604051601f8201601f1916810167ffffffffffffffff8111828210171561069857610698610659565b604052919050565b600082601f8301126106b157600080fd5b813567ffffffffffffffff8111156106cb576106cb610659565b6106de601f8201601f191660200161066f565b8181528460208386010111156106f357600080fd5b816020850160208301376000918101602001919091529392505050565b6000806040838503121561072357600080fd5b823567ffffffffffffffff8082111561073b57600080fd5b610747868387016106a0565b9350602085013591508082111561075d57600080fd5b5061076a858286016106a0565b9150509250929050565b60006020828403121561078657600080fd5b813567ffffffffffffffff81111561079d57600080fd5b6107a9848285016106a0565b949350505050565b60005b838110156107cc5781810151838201526020016107b4565b838111156107db576000848401525b50505050565b600081518084526107f98160208601602086016107b1565b601f01601f19169290920160200192915050565b60408152600061082060408301856107e1565b90508260208301529392505050565b60008060006060848603121561084457600080fd5b833567ffffffffffffffff8082111561085c57600080fd5b610868878388016106a0565b94506020915081860135935060408601358181111561088657600080fd5b8601601f8101881361089757600080fd5b8035828111156108a9576108a9610659565b8060051b6108b885820161066f565b918252828101850191858101908b8411156108d257600080fd5b86850192505b8383101561090e578235868111156108f05760008081fd5b6108fe8d89838901016106a0565b83525091860191908601906108d8565b809750505050505050509250925092565b60608152600061093260608301866107e1565b6020858185015283820360408501528185518084528284019150828160051b85010183880160005b8381101561098857601f198784030185526109768383516107e1565b9486019492509085019060010161095a565b50909a9950505050505050505050565b600082516109aa8184602087016107b1565b9190910192915050565b600181811c908216806109c857607f821691505b602082108114156109e957634e487b7160e01b600052602260045260246000fd5b5091905056fea2646970667358221220111ec7ac79ae872feabb9b44dbe2fba9fc0d955b3b191b99a2591f88d829610b64736f6c63430008090033";

    public static final String FUNC_ADDFILETOSERVER = "addFileToServer";

    public static final String FUNC_ADDSERVER = "addServer";

    public static final String FUNC_GETSERVER = "getServer";

    public static final String FUNC_GETSERVERCOUNT = "getServerCount";

    public static final String FUNC_SERVERS = "servers";

    @Deprecated
    protected FileStore(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected FileStore(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected FileStore(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected FileStore(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addFileToServer(String _serverKey, String _fileName) {
        final Function function = new Function(
                FUNC_ADDFILETOSERVER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_serverKey), 
                new org.web3j.abi.datatypes.Utf8String(_fileName)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> addServer(String _name, BigInteger _numFiles, List<String> _fileNames) {
        final Function function = new Function(
                FUNC_ADDSERVER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name), 
                new org.web3j.abi.datatypes.generated.Uint256(_numFiles), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Utf8String>(
                        org.web3j.abi.datatypes.Utf8String.class,
                        org.web3j.abi.Utils.typeMap(_fileNames, org.web3j.abi.datatypes.Utf8String.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, List<String>>> getServer(String _name) {
        final Function function = new Function(FUNC_GETSERVER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_name)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Utf8String>>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, List<String>>>(function,
                new Callable<Tuple3<String, BigInteger, List<String>>>() {
                    @Override
                    public Tuple3<String, BigInteger, List<String>> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, List<String>>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                convertToNative((List<Utf8String>) results.get(2).getValue()));
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getServerCount() {
        final Function function = new Function(FUNC_GETSERVERCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple2<String, BigInteger>> servers(String param0) {
        final Function function = new Function(FUNC_SERVERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple2<String, BigInteger>>(function,
                new Callable<Tuple2<String, BigInteger>>() {
                    @Override
                    public Tuple2<String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple2<String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue());
                    }
                });
    }

    @Deprecated
    public static FileStore load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new FileStore(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static FileStore load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new FileStore(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static FileStore load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new FileStore(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static FileStore load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new FileStore(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<FileStore> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(FileStore.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<FileStore> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FileStore.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<FileStore> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(FileStore.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<FileStore> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(FileStore.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }
}
