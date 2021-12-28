package com.gaoshan.chain.service;

import com.gaoshan.chain.constant.ResponseData;
import com.gaoshan.chain.constract.engine.SealsRouterContractEngine;
import com.gaoshan.chain.entity.SignatureAuth;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.storm3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;

@AllArgsConstructor
@Component
@Slf4j
public class SealsServiceImpl {
    private final SealsRouterContractEngine sealsRouterContractEngine;

    public String createSeal(String legalDid, String creditCode) {
        return null;
    }

    /**
     * @param legalDid
     * @param creditCode
     * @return
     */
    public String calculateSeal(String legalDid, String creditCode) {
        String address = GIdUtils.convertGIdToAddress(legalDid);
        ResponseData<String> stringResponseData = sealsRouterContractEngine.calculateSeal(address, creditCode);
        if (stringResponseData.getResult() == null) {
            log.info(stringResponseData.toString());
            throw new RuntimeException("calculateSeal error");
        }
        return stringResponseData.getResult();
    }

    public Boolean approveSeal(SignatureAuth signatureAuth) throws SignatureException {

        ResponseData<Boolean> responseData = sealsRouterContractEngine.querySealApprovl(signatureAuth.getContractAddress(),
                BigInteger.valueOf(signatureAuth.getType()),
                GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid())
        );
        if (responseData.getResult()) {
            log.info("已授权");
            return true;
        } else {
            //本地验证签名
            ResponseData<String> encodePacked = sealsRouterContractEngine.encodePacked(signatureAuth.getContractAddress(),
                    BigInteger.valueOf(signatureAuth.getType()), GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid()),
                    BigInteger.valueOf(signatureAuth.getAuthTime()));
            boolean verifyRes = DataToolUtils.verifyPrefixedSignatureFromWeId(Numeric.hexStringToByteArray(encodePacked.getResult()),
                    signatureAuth.getAuthSign(), signatureAuth.getAuthDid());
            if (!verifyRes) {
                log.info("非法签名");
                return false;
            }
            //验证签名地址是否是owner()
            ResponseData<Boolean> sealOwner = sealsRouterContractEngine.isSealOwner(signatureAuth.getContractAddress(), signatureAuth.getAuthDid());
            if (!sealOwner.getResult()) {
                log.info("签名地址没有权限授权");
                return false;
            }
            //上链
            ResponseData<Boolean> responseData2 = sealsRouterContractEngine.approvalDelegate(signatureAuth.getContractAddress(),
                    BigInteger.valueOf(signatureAuth.getType()),
                    GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid()),
                    BigInteger.valueOf(signatureAuth.getAuthTime()),
                    signatureAuth.getAuthSign()
            );
            if (responseData2.getResult()) {
                return true;
            } else {
                return false;
            }
        }

    }

    public void revokeSeal(SignatureAuth signatureAuth) throws SignatureException {

        ResponseData<Boolean> responseData = sealsRouterContractEngine.querySealApprovl(signatureAuth.getContractAddress(),
                BigInteger.valueOf(signatureAuth.getType()),
                GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid())
        );
        if (!responseData.getResult()) {
            log.info("已吊销授权");
            return;
        } else {
            //本地验证签名
            ResponseData<String> encodePacked = sealsRouterContractEngine.encodePacked(signatureAuth.getContractAddress(),
                    BigInteger.valueOf(signatureAuth.getType()), GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid()),
                    BigInteger.valueOf(signatureAuth.getAuthTime()));
            boolean verifyRes = DataToolUtils.verifyPrefixedSignatureFromWeId(Numeric.hexStringToByteArray(encodePacked.getResult()),
                    signatureAuth.getAuthSign(), signatureAuth.getAuthDid());
            if (!verifyRes) {
                log.info("签名本地验证失败");

                return;
            }
            //上链
            ResponseData<Boolean> responseData2 = sealsRouterContractEngine.revokeDelegate(signatureAuth.getContractAddress(),
                    BigInteger.valueOf(signatureAuth.getType()),
                    GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid()),
                    BigInteger.valueOf(signatureAuth.getAuthTime()),
                    signatureAuth.getAuthSign()
            );
            if (responseData2.getResult()) {

            } else {

            }
        }

    }

}
