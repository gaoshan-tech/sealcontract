
package com.gaoshan.chain.constract.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.config.ChainConfig;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.AddressGasProvider;
import com.gaoshan.chain.constant.ContractsContractExt;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ResponseData;
import com.gaoshan.chain.constract.ContractsContract;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.storm3j.crypto.Credentials;
import org.storm3j.crypto.Sign;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.core.DefaultBlockParameter;
import org.storm3j.protocol.core.DefaultBlockParameterName;
import org.storm3j.protocol.core.methods.response.TransactionReceipt;
import org.storm3j.protocol.http.HttpService;
import org.storm3j.tuples.generated.Tuple5;
import org.storm3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;


/**
 * ContractsContractEngine
 * @author lijie 2020年6月21日
 */
@Component
@Slf4j
public class ContractsContractEngine extends BaseEngine {


    /**
     * WeIdentity DID contract object, for calling weIdentity DID contract.
     */
    private static ContractsContract contractsContract;

//    public static ConcurrentHashMap<String, ContractsContract> CONTRACT_LIST = new ConcurrentHashMap<>();

    public ContractsContractEngine(ChainConfig chainConfig, IssuerConfig issuerConfig, AddressGasProvider addressGasProvider ) {
        super(chainConfig, issuerConfig, addressGasProvider);
    }



    /**
     * 构造函数.
     */
    public ContractsContractExt getEngine(Credentials credentials) throws IOException {
        log.info("ContractsContract address: {}", credentials.getAddress());
        Storm3j storm3j = Storm3j.build(new HttpService(getConfig().getNodeRpc()));
        BigInteger balance = storm3j.fstGetBalance(credentials.getAddress(), DefaultBlockParameterName.PENDING).send().getBalance();
        log.info("fstGetBalance address 余额: {}，{}（*0.01）", credentials.getAddress(), balance.divide(new BigInteger("10000000000000000")));

        if (credentials == null) {
            throw new RuntimeException("暂无可用账号，请稍后");
        }
        ContractsContract contractService = getContractService(getConfig().getContractsContract(), credentials, ContractsContract.class);
        ContractsContractExt contractsContractExt = new ContractsContractExt();
        contractsContractExt.setContractsContract(contractService);
        contractsContractExt.setAddress(credentials.getAddress());
        return contractsContractExt;
    }

    public ContractsContract getEngine() throws IOException {
        if (contractsContract == null) {
            contractsContract = getContractService(getConfig().getContractsContract(), ContractsContract.class);
        }
        return contractsContract;

    }

    public ResponseData<Boolean> isExist(String _contractHash, String _signHash, String _sealaddr, BigInteger sealType, String _signAddr) {
        try {
            Boolean res = getEngine()
                    .isExist(DataToolUtils.stringToByteArray(_contractHash),
                            Numeric.hexStringToByteArray(_signHash), _sealaddr,
                            sealType, _signAddr).send();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[isExist] execute failed. Error message :{}", e);
            return new ResponseData<>(null, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<BigInteger> query(String _contractHash, String _signHash, String _sealaddr, BigInteger sealType, String _signAddr) {
        try {
            BigInteger res = getEngine()
                    .query(DataToolUtils.stringToByteArray(_contractHash),
                            Numeric.hexStringToByteArray(_signHash), _sealaddr,
                            sealType, _signAddr).send();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[query] execute failed. Error message :{}", e);
            return new ResponseData<>(null, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<BigInteger> querySignTimel(String _contractHash, String _signHash, String _sealaddr, BigInteger sealType, String _signAddr) {
        try {
            BigInteger res = getEngine()
                    .querySignTimel(DataToolUtils.stringToByteArray(_contractHash),
                            Numeric.hexStringToByteArray(_signHash), _sealaddr,
                            sealType, _signAddr).send();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[querySignTimel] execute failed. Error message :{}", e);
            return new ResponseData<>(null, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<String> queryDetail(String _contractHash, BigInteger _index) {
        try {
            Tuple5<byte[], String, BigInteger, String, BigInteger> send = getEngine()
                    .queryDetail(DataToolUtils.stringToByteArray(_contractHash), _index).send();
            String res = send.component2();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[queryDetail] execute failed. Error message :{}", e);
            return new ResponseData<>(null, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<Boolean> isOwner(String gId) {
        try {
            String owner = getEngine().owner().send();
            return new ResponseData<>(StrUtil.equals(gId, GIdUtils.convertAddressToGId(owner)), ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[isOwner] execute failed. Error message :{}", e);
            return new ResponseData<>(false, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<Boolean> addSign(String _contractHash, String _signHash, String _sealaddr, BigInteger sealType, String _signAddr, String base64Sign) {
        ContractsContract engine = null;
        try {
            Sign.SignatureData signatureData = DataToolUtils.convertBase64StringToSignatureData(base64Sign);
//            logger.info(Numeric.toHexString(DataToolUtils.stringToByteArray(_contractHash)));
//            logger.info(Numeric.toHexString(DataToolUtils.stringToByteArray(_signHash)));
//            logger.info(_sealaddr);
//            logger.info(sealType.toString());
//            logger.info(_signAddr);
//            logger.info( BigInteger.valueOf(((int) signatureData.getV()[0])).toString());
//            logger.info(Numeric.toHexString(signatureData.getR()));
//            logger.info(Numeric.toHexString(signatureData.getS()));
            engine = getEngine();
            TransactionReceipt receipt = engine
                    .addSign(DataToolUtils.stringToByteArray(_contractHash),
                            Numeric.hexStringToByteArray(_signHash), _sealaddr,
                            sealType, _signAddr, BigInteger.valueOf(((int) signatureData.getV()[0])), signatureData.getR(), signatureData.getS()).send();
            List<ContractsContract.AddSignEventResponse> addSignEvents = getEngine().getAddSignEvents(receipt);
            if (CollUtil.isEmpty(addSignEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.getTransactionHash());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), receipt.getTransactionHash());
        } catch (Exception e) {
            log.error("[addSign] has error, Error Message：{}", e);
        }
        return new ResponseData(false, ErrorCode.FAIL);
    }

    public ResponseData<Boolean> upgradeTo(String newImplementation) {
        try {
            TransactionReceipt receipt = getEngine()
                    .upgradeTo(newImplementation).send();
            List<ContractsContract.UpgradedEventResponse> upgradedEvents = getEngine().getUpgradedEvents(receipt);
            log.info(receipt.toString());
            log.info(upgradedEvents.toString());
            if (CollUtil.isEmpty(upgradedEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), upgradedEvents.toString());
        } catch (Exception e) {
            log.error("[upgradeTo] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }

    public ResponseData<Boolean> setAsealsRouterAddr(String sealsRouterAddr) {
        try {
            TransactionReceipt receipt = getEngine()
                    .setAsealsRouterAddr(sealsRouterAddr).send();
            List<ContractsContract.SetAddrEventResponse> setAddrEvents = getEngine().getSetAddrEvents(receipt);
            log.info(receipt.toString());
            log.info(setAddrEvents.toString());
            if (CollUtil.isEmpty(setAddrEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), setAddrEvents.toString());
        } catch (Exception e) {
            log.error("[setAsealsRouterAddr] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }
}
