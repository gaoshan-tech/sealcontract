package com.gaoshan.chain.constract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.storm3j.abi.EventEncoder;
import org.storm3j.abi.TypeReference;
import org.storm3j.abi.datatypes.Address;
import org.storm3j.abi.datatypes.Bool;
import org.storm3j.abi.datatypes.DynamicBytes;
import org.storm3j.abi.datatypes.Event;
import org.storm3j.abi.datatypes.Type;
import org.storm3j.abi.datatypes.generated.Int256;
import org.storm3j.abi.datatypes.generated.Uint256;
import org.storm3j.abi.datatypes.generated.Uint8;
import org.storm3j.crypto.Credentials;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.core.DefaultBlockParameter;
import org.storm3j.protocol.core.RemoteCall;
import org.storm3j.protocol.core.RemoteFunctionCall;
import org.storm3j.protocol.core.methods.request.FstFilter;
import org.storm3j.protocol.core.methods.response.BaseEventResponse;
import org.storm3j.protocol.core.methods.response.Log;
import org.storm3j.protocol.core.methods.response.TransactionReceipt;
import org.storm3j.tuples.generated.Tuple5;
import org.storm3j.tx.Contract;
import org.storm3j.tx.TransactionManager;
import org.storm3j.tx.gas.ContractGasProvider;
import org.web3j.protocol.core.methods.request.EthFilter;

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
public class ContractsContract extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50612c63806100206000396000f3fe608060405234801561001057600080fd5b50600436106100b45760003560e01c806384c284801161007157806384c284801461078f5780638da5cb5b146109475780639bda4f14146109915780639d2a449414610b6a578063c4d66de814610d1d578063f2fde38b14610d61576100b4565b80630b03ea30146100b95780630e445722146100fd578063382dab96146102b05780633d92b594146104165780636417c8b5146105ce578063715018a614610785575b600080fd5b6100fb600480360360208110156100cf57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610da5565b005b61029a600480360360a081101561011357600080fd5b810190808035906020019064010000000081111561013057600080fd5b82018360208201111561014257600080fd5b8035906020019184600183028401116401000000008311171561016457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803590602001906401000000008111156101c757600080fd5b8201836020820111156101d957600080fd5b803590602001918460018302840111640100000000831117156101fb57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610f99565b6040518082815260200191505060405180910390f35b610400600480360360408110156102c657600080fd5b81019080803590602001906401000000008111156102e357600080fd5b8201836020820111156102f557600080fd5b8035906020019184600183028401116401000000008311171561031757600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561037a57600080fd5b82018360208201111561038c57600080fd5b803590602001918460018302840111640100000000831117156103ae57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929050505061124c565b6040518082815260200191505060405180910390f35b6104d96004803603604081101561042c57600080fd5b810190808035906020019064010000000081111561044957600080fd5b82018360208201111561045b57600080fd5b8035906020019184600183028401116401000000008311171561047d57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019092919050505061129d565b60405180806020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825287818151815260200191508051906020019080838360005b8381101561058f578082015181840152602081019050610574565b50505050905090810190601f1680156105bc5780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b61076b600480360360a08110156105e457600080fd5b810190808035906020019064010000000081111561060157600080fd5b82018360208201111561061357600080fd5b8035906020019184600183028401116401000000008311171561063557600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019064010000000081111561069857600080fd5b8201836020820111156106aa57600080fd5b803590602001918460018302840111640100000000831117156106cc57600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506115c4565b604051808215151515815260200191505060405180910390f35b61078d6115f1565b005b610852600480360360408110156107a557600080fd5b81019080803590602001906401000000008111156107c257600080fd5b8201836020820111156107d457600080fd5b803590602001918460018302840111640100000000831117156107f657600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505091929192908035906020019092919050505061177b565b60405180806020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825287818151815260200191508051906020019080838360005b838110156109085780820151818401526020810190506108ed565b50505050905090810190601f1680156109355780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390f35b61094f6118c6565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610b5060048036036101008110156109a857600080fd5b81019080803590602001906401000000008111156109c557600080fd5b8201836020820111156109d757600080fd5b803590602001918460018302840111640100000000831117156109f957600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190640100000000811115610a5c57600080fd5b820183602082011115610a6e57600080fd5b80359060200191846001830284011164010000000083111715610a9057600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff16906020019092919080359060200190929190803590602001909291905050506118ef565b604051808215151515815260200191505060405180910390f35b610d07600480360360a0811015610b8057600080fd5b8101908080359060200190640100000000811115610b9d57600080fd5b820183602082011115610baf57600080fd5b80359060200191846001830284011164010000000083111715610bd157600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f82011690508083019250505050505050919291929080359060200190640100000000811115610c3457600080fd5b820183602082011115610c4657600080fd5b80359060200191846001830284011164010000000083111715610c6857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff169060200190929190803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061227e565b6040518082815260200191505060405180910390f35b610d5f60048036036020811015610d3357600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050612445565b005b610da360048036036020811015610d7757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506125fa565b005b610dad61280a565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614610e6f576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415610f12576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600a8152602001807f5f6164647220697320300000000000000000000000000000000000000000000081525060200191505060405180910390fd5b80600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508073ffffffffffffffffffffffffffffffffffffffff167f2f75ca815bd84b784dc14bf0e44146ca33760aa65a4c5eb4f041ab85ac1ea58160405160405180910390a250565b6000610fa886868686866115c4565b61101a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f5f7369676e206973206e6f74206578697374000000000000000000000000000081525060200191505060405180910390fd5b6000611029878787878761227e565b9050611033612a8a565b6002886040518082805190602001908083835b602083106110695780518252602082019150602081019050602083039250611046565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902060018303815481106110aa57fe5b90600052602060002090600402016040518060a0016040529081600082018054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561115c5780601f106111315761010080835404028352916020019161115c565b820191906000526020600020905b81548152906001019060200180831161113f57829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160149054906101000a900460ff1660ff1660ff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600382015481525050905080608001519250505095945050505050565b60038280516020810182018051848252602083016020850120818352809550505050505081805160208101820180518482526020830160208501208183528095505050505050600091509150505481565b6060600080600080856002886040518082805190602001908083835b602083106112dc57805182526020820191506020810190506020830392506112b9565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020805490501015611387576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601a8152602001807f5f696e6465782067726561746572207468656e206c656e67746800000000000081525060200191505060405180910390fd5b61138f612a8a565b6002886040518082805190602001908083835b602083106113c557805182526020820191506020810190506020830392506113a2565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020878154811061140357fe5b90600052602060002090600402016040518060a0016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156114b55780601f1061148a576101008083540402835291602001916114b5565b820191906000526020600020905b81548152906001019060200180831161149857829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016001820160149054906101000a900460ff1660ff1660ff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200160038201548152505090508060000151816020015182604001518360600151846080015184945095509550955095509550509295509295909350565b6000806115d4878787878761227e565b11156115e357600190506115e8565b600090505b95945050505050565b6115f961280a565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146116bb576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a360008060026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550565b60028280516020810182018051848252602083016020850120818352809550505050505081815481106117aa57fe5b906000526020600020906004020160009150915050806000018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156118575780601f1061182c57610100808354040283529160200191611857565b820191906000526020600020905b81548152906001019060200180831161183a57829003601f168201915b5050505050908060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060010160149054906101000a900460ff16908060020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff16908060030154905085565b60008060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff16905090565b60006118fe89898989896115c4565b15611971576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f5f7369676e20697320657869737400000000000000000000000000000000000081525060200191505060405180910390fd5b60008660ff161415611a2457600073ffffffffffffffffffffffffffffffffffffffff168773ffffffffffffffffffffffffffffffffffffffff1614611a1f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f5f7365616c61646472206572726f72000000000000000000000000000000000081525060200191505060405180910390fd5b611cb5565b6000600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16638c7b01d3896040518263ffffffff1660e01b8152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060206040518083038186803b158015611ac557600080fd5b505afa158015611ad9573d6000803e3d6000fd5b505050506040513d6020811015611aef57600080fd5b8101908080519060200190929190505050905080611b75576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f7365616c206973207265766f6b6564000000000000000000000000000000000081525060200191505060405180910390fd5b60008873ffffffffffffffffffffffffffffffffffffffff16630e800b9f89896040518363ffffffff1660e01b8152600401808360ff1660ff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060206040518083038186803b158015611c0257600080fd5b505afa158015611c16573d6000803e3d6000fd5b505050506040513d6020811015611c2c57600080fd5b8101908080519060200190929190505050905080611cb2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601c8152602001807f5f7369676e4164647220686173206e6f74207065726d697373696f6e0000000081525060200191505060405180910390fd5b50505b6000611cc38585858c612812565b90508573ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614611d66576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f5f7369676e206973206572726f7200000000000000000000000000000000000081525060200191505060405180910390fd5b600060028b6040518082805190602001908083835b60208310611d9e5780518252602082019150602081019050602083039250611d7b565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390209050806040518060a001604052808c81526020018b73ffffffffffffffffffffffffffffffffffffffff1681526020018a60ff1681526020018973ffffffffffffffffffffffffffffffffffffffff1681526020014281525090806001815401808255809150506001900390600052602060002090600402016000909190919091506000820151816000019080519060200190611e6f929190612ae8565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160010160146101000a81548160ff021916908360ff16021790555060608201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550608082015181600301555050808054905060038c6040518082805190602001908083835b60208310611f665780518252602082019150602081019050602083039250611f43565b6001836020036101000a03801982511681845116808217855250505050505090500191505090815260200160405180910390208b8b8b8b6040516020018085805190602001908083835b60208310611fd35780518252602082019150602081019050602083039250611fb0565b6001836020036101000a0380198251168184511680821785525050505050509050018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014018360ff1660ff1660f81b81526001018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014019450505050506040516020818303038152906040526040518082805190602001908083835b602083106120b65780518252602082019150602081019050602083039250612093565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020819055507f9dc139aa4321e8e30b5a9daad5e9d58796c4c587cc72e58e701b3843fe677f308b8b8b8b8b6040518080602001806020018673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018560ff1660ff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838103835288818151815260200191508051906020019080838360005b838110156121c75780820151818401526020810190506121ac565b50505050905090810190601f1680156121f45780820380516001836020036101000a031916815260200191505b50838103825287818151815260200191508051906020019080838360005b8381101561222d578082015181840152602081019050612212565b50505050905090810190601f16801561225a5780820380516001836020036101000a031916815260200191505b5097505050505050505060405180910390a160019250505098975050505050505050565b60006003866040518082805190602001908083835b602083106122b65780518252602082019150602081019050602083039250612293565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020858585856040516020018085805190602001908083835b602083106123235780518252602082019150602081019050602083039250612300565b6001836020036101000a0380198251168184511680821785525050505050509050018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014018360ff1660ff1660f81b81526001018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1660601b81526014019450505050506040516020818303038152906040526040518082805190602001908083835b6020831061240657805182526020820191506020810190506020830392506123e3565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051809103902054905095945050505050565b600060019054906101000a900460ff168061246c57506000809054906101000a900460ff16155b6124c1576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e815260200180612bda602e913960400191505060405180910390fd5b60008060019054906101000a900460ff161590508015612511576001600060016101000a81548160ff02191690831515021790555060016000806101000a81548160ff0219169083151502179055505b61251961280a565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a381600060026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080156125f65760008060016101000a81548160ff0219169083151502179055505b5050565b61260261280a565b73ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16146126c4576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260208152602001807f4f776e61626c653a2063616c6c6572206973206e6f7420746865206f776e657281525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16141561274a576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180612bb46026913960400191505060405180910390fd5b8073ffffffffffffffffffffffffffffffffffffffff16600060029054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a380600060026101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600033905090565b600080828051906020012060405160200180807f19457468657265756d205369676e6564204d6573736167653a0a333200000000815250601c0182815260200191505060405160208183030381529060405280519060200120905061287981878787612884565b915050949350505050565b60007f7fffffffffffffffffffffffffffffff5d576e7357a4501ddfe92f46681b20a08260001c1115612902576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180612c086026913960400191505060405180910390fd5b601b8460ff161415801561291a5750601c8460ff1614155b15612970576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401808060200182810382526026815260200180612b8e6026913960400191505060405180910390fd5b600060018686868660405160008152602001604052604051808581526020018460ff1660ff1681526020018381526020018281526020019450505050506020604051602081039080840390855afa1580156129cf573d6000803e3d6000fd5b505050602060405103519050600073ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161415612a7e576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601c8152602001807f45435265636f7665723a20696e76616c6964207369676e61747572650000000081525060200191505060405180910390fd5b80915050949350505050565b6040518060a0016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600060ff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10612b2957805160ff1916838001178555612b57565b82800160010185558215612b57579182015b82811115612b56578251825591602001919060010190612b3b565b5b509050612b649190612b68565b5090565b612b8a91905b80821115612b86576000816000905550600101612b6e565b5090565b9056fe45435265636f7665723a20696e76616c6964207369676e6174757265202776272076616c75654f776e61626c653a206e6577206f776e657220697320746865207a65726f2061646472657373496e697469616c697a61626c653a20636f6e747261637420697320616c726561647920696e697469616c697a656445435265636f7665723a20696e76616c6964207369676e6174757265202773272076616c7565a2646970667358221220929eb6b8e2a9dedc8493ef7c8a2b802c3424d774bc75c8bd32565a6f77b8660264736f6c63430006000033";

    public static final String FUNC_ADDSIGN = "addSign";

    public static final String FUNC_CONTRACTMAP = "contractMap";

    public static final String FUNC_GET = "get";

    public static final String FUNC_GETINDEXOFKEY = "getIndexOfKey";

    public static final String FUNC_GETKEYATINDEX = "getKeyAtIndex";

    public static final String FUNC_ISEXIST = "isExist";

    public static final String FUNC_INITIALIZE = "initialize";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_QUERY = "query";

    public static final String FUNC_QUERYDETAIL = "queryDetail";

    public static final String FUNC_QUERYSIGNTIMEL = "querySignTimel";

    public static final String FUNC_REMOVE = "remove";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_SET = "set";

    public static final String FUNC_SIGNATUREMAP = "signatureMap";

    public static final String FUNC_SIZE = "size";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_UPGRADETO = "upgradeTo";

    public static final String FUNC_SETASEALSROUTERADDR = "setAsealsRouterAddr";

    public static final Event SETADDR_EVENT = new Event("SetAddr",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}));
    ;

    public static final Event ADDSIGN_EVENT = new Event("AddSign",
            Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {
            }, new TypeReference<DynamicBytes>() {
            }, new TypeReference<Address>() {
            }, new TypeReference<Uint8>() {
            }, new TypeReference<Address>() {
            }));
    ;

    public static final Event CONTRACTEVENT_EVENT = new Event("ContractEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Bool>(true) {
            }));
    ;


    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Address>(true) {
            }));
    ;

    public static final Event REVOKEEVENT_EVENT = new Event("RevokeEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }, new TypeReference<Bool>(true) {
            }));
    ;

    public static final Event UPGRADED_EVENT = new Event("Upgraded",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
            }));
    ;

    @Deprecated
    protected ContractsContract(String contractAddress, Storm3j storm3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, storm3j, credentials, gasPrice, gasLimit);
    }

    protected ContractsContract(String contractAddress, Storm3j storm3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, storm3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ContractsContract(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, storm3j, transactionManager, gasPrice, gasLimit);
    }

    protected ContractsContract(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, storm3j, transactionManager, contractGasProvider);
    }

    public List<SetAddrEventResponse> getSetAddrEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SETADDR_EVENT, transactionReceipt);
        ArrayList<SetAddrEventResponse> responses = new ArrayList<SetAddrEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SetAddrEventResponse typedResponse = new SetAddrEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sealsRouterAddr = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SetAddrEventResponse> setAddrEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, SetAddrEventResponse>() {
            @Override
            public SetAddrEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SETADDR_EVENT, log);
                SetAddrEventResponse typedResponse = new SetAddrEventResponse();
                typedResponse.log = log;
                typedResponse.sealsRouterAddr = (String) eventValues.getIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }
    public List<AddSignEventResponse> getAddSignEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADDSIGN_EVENT, transactionReceipt);
        ArrayList<AddSignEventResponse> responses = new ArrayList<AddSignEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AddSignEventResponse typedResponse = new AddSignEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.contractHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.signHash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.sealaddr = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.sealType = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.signAddr = (String) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }
    public List<ContractEventEventResponse> getContractEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRACTEVENT_EVENT, transactionReceipt);
        ArrayList<ContractEventEventResponse> responses = new ArrayList<ContractEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContractEventEventResponse typedResponse = new ContractEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.status = (Boolean) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<UpgradedEventResponse> getUpgradedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(UPGRADED_EVENT, transactionReceipt);
        ArrayList<UpgradedEventResponse> responses = new ArrayList<UpgradedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            UpgradedEventResponse typedResponse = new UpgradedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.implementation = (String) eventValues.getIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContractEventEventResponse> contractEventEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, ContractEventEventResponse>() {
            @Override
            public ContractEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRACTEVENT_EVENT, log);
                ContractEventEventResponse typedResponse = new ContractEventEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.status = (Boolean) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddSignEventResponse> addSignEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, AddSignEventResponse>() {
            @Override
            public AddSignEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADDSIGN_EVENT, log);
                AddSignEventResponse typedResponse = new AddSignEventResponse();
                typedResponse.log = log;
                typedResponse.contractHash = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.signHash = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.sealaddr = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.sealType = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.signAddr = (String) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AddSignEventResponse> addSignEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        FstFilter filter = new FstFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADDSIGN_EVENT));
        return addSignEventFlowable(filter);
    }

    public Flowable<ContractEventEventResponse> contractEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        FstFilter filter = new FstFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRACTEVENT_EVENT));
        return contractEventEventFlowable(filter);
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

    public List<RevokeEventEventResponse> getRevokeEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REVOKEEVENT_EVENT, transactionReceipt);
        ArrayList<RevokeEventEventResponse> responses = new ArrayList<RevokeEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RevokeEventEventResponse typedResponse = new RevokeEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.status = (Boolean) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RevokeEventEventResponse> revokeEventEventFlowable(FstFilter filter) {
        return storm3j.fstLogFlowable(filter).map(new Function<Log, RevokeEventEventResponse>() {
            @Override
            public RevokeEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REVOKEEVENT_EVENT, log);
                RevokeEventEventResponse typedResponse = new RevokeEventEventResponse();
                typedResponse.log = log;
                typedResponse.account = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.status = (Boolean) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RevokeEventEventResponse> revokeEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        FstFilter filter = new FstFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REVOKEEVENT_EVENT));
        return revokeEventEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> setAsealsRouterAddr(String _addr) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_SETASEALSROUTERADDR,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, _addr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }


    public RemoteFunctionCall<TransactionReceipt> addSign(byte[] _contractHash, byte[] _signHash, String _sealaddr, BigInteger _sealType, String _signAddr, BigInteger v, byte[] r, byte[] s) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_ADDSIGN,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(_contractHash),
                        new org.storm3j.abi.datatypes.DynamicBytes(_signHash),
                        new org.storm3j.abi.datatypes.Address(160, _sealaddr),
                        new org.storm3j.abi.datatypes.generated.Uint8(_sealType),
                        new org.storm3j.abi.datatypes.Address(160, _signAddr),
                        new org.storm3j.abi.datatypes.generated.Uint8(v),
                        new org.storm3j.abi.datatypes.generated.Bytes32(r),
                        new org.storm3j.abi.datatypes.generated.Bytes32(s)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple5<byte[], String, BigInteger, String, BigInteger>> contractMap(byte[] param0, BigInteger param1) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_CONTRACTMAP,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(param0),
                        new org.storm3j.abi.datatypes.generated.Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint8>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }));
        return new RemoteFunctionCall<Tuple5<byte[], String, BigInteger, String, BigInteger>>(function,
                new Callable<Tuple5<byte[], String, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple5<byte[], String, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<byte[], String, BigInteger, String, BigInteger>(
                                (byte[]) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (String) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> get(String key) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_GET,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.Address(160, key)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }


    public RemoteFunctionCall<Boolean> isExist(byte[] _contractHash, byte[] _signHash, String _sealaddr, BigInteger _sealType, String _signAddr) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_ISEXIST,
                Arrays.<Type>asList(new DynamicBytes(_contractHash),
                        new DynamicBytes(_signHash),
                        new Address(160, _sealaddr),
                        new Uint8(_sealType),
                        new Address(160, _signAddr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> query(byte[] _contractHash, byte[] _signHash, String _sealaddr, BigInteger _sealType, String _signAddr) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_QUERY,
                Arrays.<Type>asList(new DynamicBytes(_contractHash),
                        new DynamicBytes(_signHash),
                        new Address(160, _sealaddr),
                        new Uint8(_sealType),
                        new Address(160, _signAddr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple5<byte[], String, BigInteger, String, BigInteger>> queryDetail(byte[] _contractHash, BigInteger _index) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_QUERYDETAIL,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(_contractHash),
                        new org.storm3j.abi.datatypes.generated.Uint256(_index)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint8>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }));
        return new RemoteFunctionCall<Tuple5<byte[], String, BigInteger, String, BigInteger>>(function,
                new Callable<Tuple5<byte[], String, BigInteger, String, BigInteger>>() {
                    @Override
                    public Tuple5<byte[], String, BigInteger, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<byte[], String, BigInteger, String, BigInteger>(
                                (byte[]) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (String) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> querySignTimel(byte[] _contractHash, byte[] _signHash, String _sealaddr, BigInteger _sealType, String _signAddr) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_QUERYSIGNTIMEL,
                Arrays.<Type>asList(new DynamicBytes(_contractHash),
                        new DynamicBytes(_signHash),
                        new Address(160, _sealaddr),
                        new Uint8(_sealType),
                        new Address(160, _signAddr)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }


    public RemoteFunctionCall<Boolean> signatureMap(byte[] param0, BigInteger param1) {
        final org.storm3j.abi.datatypes.Function function = new org.storm3j.abi.datatypes.Function(FUNC_SIGNATUREMAP,
                Arrays.<Type>asList(new org.storm3j.abi.datatypes.DynamicBytes(param0),
                        new org.storm3j.abi.datatypes.generated.Uint256(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
                }));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
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
    public static ContractsContract load(String contractAddress, Storm3j storm3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ContractsContract(contractAddress, storm3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ContractsContract load(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ContractsContract(contractAddress, storm3j, transactionManager, gasPrice, gasLimit);
    }

    public static ContractsContract load(String contractAddress, Storm3j storm3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ContractsContract(contractAddress, storm3j, credentials, contractGasProvider);
    }

    public static ContractsContract load(String contractAddress, Storm3j storm3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ContractsContract(contractAddress, storm3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<ContractsContract> deploy(Storm3j storm3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ContractsContract.class, storm3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ContractsContract> deploy(Storm3j storm3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ContractsContract.class, storm3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ContractsContract> deploy(Storm3j storm3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(ContractsContract.class, storm3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<ContractsContract> deploy(Storm3j storm3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ContractsContract.class, storm3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class ContractEventEventResponse extends BaseEventResponse {
        public String account;

        public Boolean status;
    }

    public static class UpgradedEventResponse extends BaseEventResponse {
        public String implementation;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }

    public static class RevokeEventEventResponse extends BaseEventResponse {
        public String account;

        public Boolean status;
    }

    public static class AddSignEventResponse extends BaseEventResponse {
        public byte[] contractHash;

        public byte[] signHash;

        public String sealaddr;

        public BigInteger sealType;

        public String signAddr;
    }
    public static class SetAddrEventResponse extends BaseEventResponse {
        public String sealsRouterAddr;
    }
}
