

package com.gaoshan.chain.constract.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.config.ChainConfig;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.AddressGasProvider;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ResponseData;
import com.gaoshan.chain.constract.ContractsContract;
import com.gaoshan.chain.constract.IdentityContract;
import com.gaoshan.chain.constract.SealsRouterContract;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.storm3j.abi.TypeReference;
import org.storm3j.abi.datatypes.Address;
import org.storm3j.abi.datatypes.Type;
import org.storm3j.crypto.Sign;
import org.storm3j.protocol.core.RemoteFunctionCall;
import org.storm3j.protocol.core.methods.response.TransactionReceipt;
import org.storm3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
/**
 * IdentityContractEngine
 *
 * @author lijie 2020年10月21日
 */
@Component
@Slf4j
public class SealsRouterContractEngine extends BaseEngine {



    private static SealsRouterContract sealsRouterContract;

    public SealsRouterContractEngine(ChainConfig chainConfig, IssuerConfig issuerConfig, AddressGasProvider addressGasProvider ) {
        super(chainConfig, issuerConfig, addressGasProvider);
    }

//    public SealsRouterContractEngine(ChainConfig chainConfig, AddressGasProvider addressGasProvider) {
//        super(chainConfig, addressGasProvider);
//    }

    /**
     * 构造函数.
     */
    public SealsRouterContract getEngine() {
        if (sealsRouterContract == null) {
            sealsRouterContract = getContractService(getConfig().getSealsRouterContract(), SealsRouterContract.class);
        }
        return sealsRouterContract;
    }

    public ResponseData<String> calculateSeal(String legalAddr, String creditCode) {
        try {
            String res = getEngine()
                    .calculateSeal(legalAddr, DataToolUtils.stringToByteArray(creditCode)).send();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[calculateSeal] execute failed. Error message :{}", e);
            return new ResponseData<>(null, ErrorCode.UNKNOW_ERROR);
        }
    }
    public ResponseData<String> getSeal(String legalAddr, String creditCode) {
        try {
            String res = getEngine()
                    .getSeal(legalAddr, DataToolUtils.stringToByteArray(creditCode)).send();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[calculateSeal] execute failed. Error message :{}", e);
            return new ResponseData<>(null, ErrorCode.UNKNOW_ERROR);
        }
    }
    public ResponseData<String> encodePacked(String sealAddr, BigInteger sealType, String approvalAddr, BigInteger signTime) {
        try {
            byte[] send = getEngine()
                    .encodePacked(sealAddr, sealType, approvalAddr, signTime).send();
            String hexString = Numeric.toHexString(send);
//            String hexString=new String(
//                    send,
//                    StandardCharsets.UTF_8
//            );
            return new ResponseData<>(hexString, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[calculateSeal] execute failed. Error message :{}", e);
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
    public ResponseData<Boolean> isSealOwner(String sealAddr,String sendDId) {
        try {
            String owner = getEngine().querySealOwner(sealAddr).send();
            return new ResponseData<>(StrUtil.equals(sendDId, GIdUtils.convertAddressToGId(owner)), ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[isSealOwner] execute failed. Error message :{}", e);
            return new ResponseData<>(false, ErrorCode.UNKNOW_ERROR);
        }
    }
    public ResponseData<Boolean> querySealApprovl(String sealAddr, BigInteger _sealType, String addr){
        try {
            Boolean res = getEngine().querySealApprovl(sealAddr, _sealType, addr).send().booleanValue();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[querySealApprovl] execute failed. Error message :{}", e);
            return new ResponseData<>(false, ErrorCode.UNKNOW_ERROR);
        }
    }
    public ResponseData<Boolean> querySealStatus(String sealAddr){
        try {
            Boolean res = getEngine().querySealStatus(sealAddr).send().booleanValue();
            return new ResponseData<>(res, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[querySealStatus] execute failed. Error message :{}", e);
            return new ResponseData<>(false, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<Boolean> createSeal(String legalAddr, String creditCode) {
        try {
            TransactionReceipt receipt = getEngine()
                    .createSeal(legalAddr, DataToolUtils.stringToByteArray(creditCode)).send();
            List<SealsRouterContract.SealsCreatedEventResponse> sealsCreatedEvents = getEngine().getSealsCreatedEvents(receipt);
            if (CollUtil.isEmpty(sealsCreatedEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), sealsCreatedEvents.toString());
        } catch (Exception e) {
            log.error("[addIdentity] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }

    public ResponseData<Boolean> approvalDelegate(String sealAddr, BigInteger sealType, String approvalAddr, BigInteger signTime, String base64Sign) {
        try {
            Sign.SignatureData signatureData = DataToolUtils.convertBase64StringToSignatureData(base64Sign);
            TransactionReceipt receipt = getEngine()
                    .approvalDelegate(sealAddr, sealType, approvalAddr, signTime, BigInteger.valueOf(((int) signatureData.getV()[0])), signatureData.getR(), signatureData.getS()).send();
            List<SealsRouterContract.ApprovalDelegateEventResponse> approvalDelegateEvents = getEngine().getApprovalDelegateEvents(receipt);
            if (CollUtil.isEmpty(approvalDelegateEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), approvalDelegateEvents.toString());
        } catch (Exception e) {
            log.error("[addIdentity] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }

    public ResponseData<Boolean> revokeDelegate(String sealAddr, BigInteger sealType, String approvalAddr, BigInteger signTime, String base64Sign) {
        try {
            Sign.SignatureData signatureData = DataToolUtils.convertBase64StringToSignatureData(base64Sign);
            TransactionReceipt receipt = getEngine()
                    .revokeDelegate(sealAddr, sealType, approvalAddr, signTime, BigInteger.valueOf(((int) signatureData.getV()[0])), signatureData.getR(), signatureData.getS()).send();
            List<SealsRouterContract.RevokeDelegateEventResponse> revokeDelegateEvents = getEngine().getRevokeDelegateEvents(receipt);
            if (CollUtil.isEmpty(revokeDelegateEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), revokeDelegateEvents.toString());
        } catch (Exception e) {
            log.error("[addIdentity] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }
    public ResponseData<Boolean> upgradeTo(String newImplementation) {
        try {
            TransactionReceipt receipt = getEngine()
                    .upgradeTo(newImplementation).send();
            List<SealsRouterContract.UpgradedEventResponse> upgradedEvents = getEngine().getUpgradedEvents(receipt);
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
}
