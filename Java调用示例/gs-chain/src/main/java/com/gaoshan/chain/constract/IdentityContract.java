package com.gaoshan.chain.constract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.storm3j.abi.EventEncoder;
import org.storm3j.abi.TypeReference;
import org.storm3j.abi.datatypes.Address;
import org.storm3j.abi.datatypes.Bool;
import org.storm3j.abi.datatypes.DynamicBytes;
import org.storm3j.abi.datatypes.Event;
import org.storm3j.abi.datatypes.Type;
import org.storm3j.abi.datatypes.generated.Int256;
import org.storm3j.abi.datatypes.generated.Uint256;
import org.storm3j.crypto.Credentials;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.core.DefaultBlockParameter;
import org.storm3j.protocol.core.RemoteCall;
import org.storm3j.protocol.core.RemoteFunctionCall;
import org.storm3j.protocol.core.methods.request.FstFilter;
import org.storm3j.protocol.core.methods.response.BaseEventResponse;
import org.storm3j.protocol.core.methods.response.Log;
import org.storm3j.protocol.core.methods.response.TransactionReceipt;
import org.storm3j.tx.Contract;
import org.storm3j.tx.TransactionManager;
import org.storm3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.5.
 */
@SuppressWarnings("rawtypes")
public class IdentityContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50611486806100206000396000f3fe608060405234801561001057600080fd5b506004361061009e5760003560e01c8063a16ce21d11610066578063a16ce21d14610368578063a77fe1a81461043b578063c4d66de81461050e578063e6ddae4f14610552578063f2fde38b1461062f5761009e565b80630a03e738146100a35780631c3f03c9146101765780632871c8a914610245578063715018a6146103145780638da5cb5b1461031e575b600080fd5b61015c600480360360208110156100b957600080fd5b81019080803590602001906401000000008111156100d657600080fd5b8201836020820111156100e857600080fd5b8035906020019184600183028401116401000000008311171561010a57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610673565b604051808215151515815260200191505060405180910390f35b61022f6004803603602081101561018c57600080fd5b81019080803590602001906401000000008111156101a957600080fd5b8201836020820111156101bb57600080fd5b803590602001918460018302840111640100000000831117156101dd57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506106a9565b6040518082815260200191505060405180910390f35b6102fe6004803603602081101561025b57600080fd5b810190808035906020019064010000000081111561027857600080fd5b82018360208201111561028a57600080fd5b803590602001918460018302840111640100000000831117156102ac57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061071c565b6040518082815260200191505060405180910390f35b61031c61074a565b005b6103266108d4565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6104216004803603602081101561037e57600080fd5b810190808035906020019064010000000081111561039b57600080fd5b8201836020820111156103ad57600080fd5b803590602001918460018302840111640100000000831117156103cf57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192905050506108fd565b604051808215151515815260200191505060405180910390f35b6104f46004803603602081101561045157600080fd5b810190808035906020019064010000000081111561046e57600080fd5b82018360208201111561048057600080fd5b803590602001918460018302840111640100000000831117156104a257600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290505050610c47565b604051808215151515815260200191505060405180910390f35b6105506004803603602081101561052457600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f23565b005b6106156004803603604081101561056857600080fd5b810190808035906020019064010000000081111561058557600080fd5b82018360208201111561059757600080fd5b803590602001918460018302840111640100000000831117156105b957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803590602001909291905050506110d8565b604051808215151515815260200191505060405180910390f35b6106716004803603602081101561064557600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506111e4565b005b6001818051602081018201805184825260208301602085012081835280955050505050506000915054906101000a900460ff1681565b60006002826040518082805190602001908083835b602083106106e157805182526020820191506020810190506020830392506106be565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020549050919050565b6002818051602081018201805184825260208301602085012081835280955050505050506000915090505481565b6107526113f4565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610814576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a360008060026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550565b60008060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60006109076113f4565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146109c9576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b6001826040518082805190602001908083835b602083106109ff57805182526020820191506020810190506020830392506109dc565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060009054906101000a900460ff16610ab2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964656e7469747920686173207265766f6b656400000000000000000000000081525060200191505060405180910390fd5b60006001836040518082805190602001908083835b60208310610aea5780518252602082019150602081019050602083039250610ac7565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060006101000a81548160ff021916908315150217905550426002836040518082805190602001908083835b60208310610b6b5780518252602082019150602081019050602083039250610b48565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055507fdc730e218cfc1f20ca0db2b8f2d6de5a20643197dfce3030ffb6162d979eec56826040518080602001828103825283818151815260200191508051906020019080838360005b83811015610c04578082015181840152602081019050610be9565b50505050905090810190601f168015610c315780820380516001836020036101000a031916815260200191505b509250505060405180910390a160019050919050565b6000610c516113f4565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610d13576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b6001826040518082805190602001908083835b60208310610d495780518252602082019150602081019050602083039250610d26565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060009054906101000a900460ff1615610dfd576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f6964656e7469747920686173206578697374000000000000000000000000000081525060200191505060405180910390fd5b600180836040518082805190602001908083835b60208310610e345780518252602082019150602081019050602083039250610e11565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060006101000a81548160ff0219169083151502179055507fd78c379e1912ab387fe6b610ef43af8b64ea376337da41e5c4776532f6d84fa4826040518080602001828103825283818151815260200191508051906020019080838360005b83811015610ee0578082015181840152602081019050610ec5565b50505050905090810190601f168015610f0d5780820380516001836020036101000a031916815260200191505b509250505060405180910390a160019050919050565b600060019054906101000a900460ff1680610f4a57506000809054906101000a900460ff16155b610f9f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180611423602e913960400191505060405180910390fd5b60008060019054906101000a900460ff161590508015610fef576001600060016101000a81548160ff02191690831515021790555060016000806101000a81548160ff0219169083151502179055505b610ff76113f4565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a381600060026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080156110d45760008060016101000a81548160ff0219169083151502179055505b5050565b60006001836040518082805190602001908083835b6020831061111057805182526020820191506020810190506020830392506110ed565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060009054906101000a900460ff161561115f57600190506111de565b816002846040518082805190602001908083835b602083106111965780518252602082019150602081019050602083039250611173565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390205411156111d957600190506111de565b600090505b92915050565b6111ec6113f4565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146112ae576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415611334576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001806113fd6026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a380600060026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b60003390509056fe4f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373496e697469616c697a61626c653a20636f6e747261637420697320616c726561647920696e697469616c697a6564a264697066735822122081597a4c700a97c91bd14b2137c37993a58f2c9e75a649d66891cfa739ffaac864736f6c63430006000033";

    public static final String FUNC_ADDIDENTITY = "addIdentity";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETINDEXOFKEY = "getIndexOfKey";

    public static final String FUNC_GETKEYATINDEX = "getKeyAtIndex";

    public static final String FUNC_ISVALID = "isValid";

    public static final String FUNC_LASTEFFICACYTIME = "lastEfficacyTime";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_QUERYEFFICACYTIME = "queryEfficacyTime";

    public static final String FUNC_QUERYIDENTITY = "queryIdentity";

    public static final String FUNC_REMOVE = "remove";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_REVOKEIDENTITY = "revokeIdentity";

    public static final String FUNC_SET = "set";

    public static final String FUNC_SIZE = "size";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPGRADETO = "upgradeTo";

    public static final Event UPGRADED_EVENT = new Event("Upgraded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event ADDIDENTITYEVENT_EVENT = new Event("AddIdentityEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event LOSEEFFICACYEVENT_EVENT = new Event("LoseEfficacyEvent", 
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected IdentityContract(String contractAddress, Storm3j storm3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, storm3j, credentials, gasPrice, gasLimit);
    }

    protected IdentityContract(String contractAddress, Storm3j storm3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, storm3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected IdentityContract(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, storm3j, transactionManager, gasPrice, gasLimit);
    }

    protected IdentityContract(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, storm3j, transactionManager, contractGasProvider);
    }

    public List<IdentityContract.UpgradedEventResponse> getUpgradedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPGRADED_EVENT, transactionReceipt);
        ArrayList<IdentityContract.UpgradedEventResponse> responses = new ArrayList<IdentityContract.UpgradedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            IdentityContract.UpgradedEventResponse typedResponse = new IdentityContract.UpgradedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.implementation = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }


    public List<AddIdentityEventEventResponse> getAddIdentityEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDIDENTITYEVENT_EVENT, transactionReceipt);
        ArrayList<AddIdentityEventEventResponse> responses = new ArrayList<AddIdentityEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddIdentityEventEventResponse typedResponse = new AddIdentityEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AddIdentityEventEventResponse> addIdentityEventEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, AddIdentityEventEventResponse>() {
            @Override
            public AddIdentityEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDIDENTITYEVENT_EVENT, log);
                AddIdentityEventEventResponse typedResponse = new AddIdentityEventEventResponse();
                typedResponse.log = log;
                typedResponse.account = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddIdentityEventEventResponse> addIdentityEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        FstFilter filter = new FstFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDIDENTITYEVENT_EVENT));
        return addIdentityEventEventFlowable(filter);
    }

    public List<LoseEfficacyEventEventResponse> getLoseEfficacyEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOSEEFFICACYEVENT_EVENT, transactionReceipt);
        ArrayList<LoseEfficacyEventEventResponse> responses = new ArrayList<LoseEfficacyEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LoseEfficacyEventEventResponse typedResponse = new LoseEfficacyEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<LoseEfficacyEventEventResponse> loseEfficacyEventEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, LoseEfficacyEventEventResponse>() {
            @Override
            public LoseEfficacyEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(LOSEEFFICACYEVENT_EVENT, log);
                LoseEfficacyEventEventResponse typedResponse = new LoseEfficacyEventEventResponse();
                typedResponse.log = log;
                typedResponse.account = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<LoseEfficacyEventEventResponse> loseEfficacyEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        FstFilter filter = new FstFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(LOSEEFFICACYEVENT_EVENT));
        return loseEfficacyEventEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        FstFilter filter = new FstFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addIdentity(byte[] _identity) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_ADDIDENTITY, 
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(_identity)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> get(String key) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_GET,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, key)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<BigInteger> getIndexOfKey(String key) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_GETINDEXOFKEY,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, key)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Int256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> getKeyAtIndex(BigInteger index) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_GETKEYATINDEX,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.generated.Uint256(index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Boolean> isValid(byte[] param0) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_ISVALID,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<BigInteger> lastEfficacyTime(byte[] param0) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_LASTEFFICACYTIME,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> queryEfficacyTime(byte[] _identity) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_QUERYEFFICACYTIME,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(_identity)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Boolean> queryIdentity(byte[] _identity, BigInteger _time) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_QUERYIDENTITY,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(_identity),
                new org.storm3j.abi.datatypes.generated.Uint256(_time)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> remove(String key) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_REMOVE, 
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, key)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeIdentity(byte[] _identity) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_REVOKEIDENTITY, 
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(_identity)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> set(String key, BigInteger val) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_SET, 
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, key),
                new org.storm3j.abi.datatypes.generated.Uint256(val)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> size() {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_SIZE,
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> initialize(String newOwner) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_INITIALIZE,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> upgradeTo(String newImplementation) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_UPGRADETO,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, newImplementation)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static IdentityContract load(String contractAddress, Storm3j storm3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IdentityContract(contractAddress, storm3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static IdentityContract load(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IdentityContract(contractAddress, storm3j, transactionManager, gasPrice, gasLimit);
    }

    public static IdentityContract load(String contractAddress, Storm3j storm3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new IdentityContract(contractAddress, storm3j, credentials, contractGasProvider);
    }

    public static IdentityContract load(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new IdentityContract(contractAddress, storm3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<IdentityContract> deploy(Storm3j storm3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IdentityContract.class, storm3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IdentityContract> deploy(Storm3j storm3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IdentityContract.class, storm3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<IdentityContract> deploy(Storm3j storm3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(IdentityContract.class, storm3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<IdentityContract> deploy(Storm3j storm3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(IdentityContract.class, storm3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class UpgradedEventResponse extends BaseEventResponse {
        public String implementation;
    }

    public static class AddIdentityEventEventResponse extends BaseEventResponse {
        public byte[] account;
    }

    public static class LoseEfficacyEventEventResponse extends BaseEventResponse {
        public byte[] account;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
