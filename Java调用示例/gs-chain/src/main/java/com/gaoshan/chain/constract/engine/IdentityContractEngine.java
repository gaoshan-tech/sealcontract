

package com.gaoshan.chain.constract.engine;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.config.ChainConfig;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.AddressGasProvider;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ResponseData;
import com.gaoshan.chain.constract.ContractsContract;
import com.gaoshan.chain.constract.IdentityContract;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.storm3j.abi.EventEncoder;
import org.storm3j.abi.FunctionEncoder;
import org.storm3j.abi.TypeReference;
import org.storm3j.abi.datatypes.Type;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.core.DefaultBlockParameterNumber;
import org.storm3j.protocol.core.methods.response.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * IdentityContractEngine
 *
 * @author lijie 2020年10月21日
 */
@Component
@Slf4j
public class IdentityContractEngine extends BaseEngine {

    /**
     * WeIdentity DID contract object, for calling weIdentity DID contract.
     */
    private static IdentityContract identityContract;

    public IdentityContractEngine(ChainConfig chainConfig, IssuerConfig issuerConfig, AddressGasProvider addressGasProvider ) {
        super(chainConfig, issuerConfig, addressGasProvider);
    }

//    public IdentityContractEngine(ChainConfig chainConfig, AddressGasProvider addressGasProvider) {
//        super(chainConfig, addressGasProvider);
//    }
    /**
     * 构造函数.
     */
    public IdentityContract getEngine() {
        if (identityContract == null) {
            identityContract = getContractService(getConfig().getIdentityContract(), IdentityContract.class);
        }
        return identityContract;
    }

    public ResponseData<Boolean> queryIdentity(String identityHash, BigInteger endTime) {
        try {
            boolean isValid = getEngine()
                    .queryIdentity(DataToolUtils.stringToByteArray(identityHash), endTime).send().booleanValue();
            return new ResponseData<>(isValid, ErrorCode.SUCCESS);
        } catch (Exception e) {
            log.error("[queryIdentity] execute failed. Error message :{}", e);
            return new ResponseData<>(false, ErrorCode.UNKNOW_ERROR);
        }
    }

    public ResponseData<Boolean> queryIdentity(String identityHash) {
        return queryIdentity(identityHash, BigInteger.valueOf(System.currentTimeMillis() / 1000));
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

    public ResponseData<Boolean> addIdentity(String identityHash) {
//        String created = DateUtils.getNoMillisecondTimeStampString();
        try {
            TransactionReceipt receipt = getEngine().addIdentity(
                    DataToolUtils.stringToByteArray(identityHash)
            ).send();
            List<IdentityContract.AddIdentityEventEventResponse> addIdentityEventEvents = getEngine().getAddIdentityEventEvents(receipt);
            if (CollUtil.isEmpty(addIdentityEventEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), addIdentityEventEvents.toString());
        } catch (Exception e) {
            log.error("[addIdentity] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }

    public ResponseData<Boolean> revokeIdentity(String identityHash) {
        try {
            TransactionReceipt receipt = getEngine().revokeIdentity(
                    DataToolUtils.stringToByteArray(identityHash)
            ).send();
            List<IdentityContract.LoseEfficacyEventEventResponse> loseEfficacyEventEvents = getEngine().getLoseEfficacyEventEvents(receipt);
            if (CollUtil.isEmpty(loseEfficacyEventEvents)) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), receipt.toString());
            }
            return new ResponseData(true, ErrorCode.SUCCESS.getCode(), loseEfficacyEventEvents.toString());
        } catch (Exception e) {
            log.error("[addIdentity] has error, Error Message：{}", e);
            return new ResponseData(false, ErrorCode.FAIL);
        }
    }
    public ResponseData<Boolean> upgradeTo(String newImplementation) {
        try {
            TransactionReceipt receipt = getEngine()
                    .upgradeTo(newImplementation).send();
            List<IdentityContract.UpgradedEventResponse> upgradedEvents = getEngine().getUpgradedEvents(receipt);
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
