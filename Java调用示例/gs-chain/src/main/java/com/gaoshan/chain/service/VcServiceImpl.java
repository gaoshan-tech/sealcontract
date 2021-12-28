package com.gaoshan.chain.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.*;
import com.gaoshan.chain.constract.engine.IdentityContractEngine;
import com.gaoshan.chain.constract.engine.SealsRouterContractEngine;
import com.gaoshan.chain.entity.Credential;
import com.gaoshan.chain.entity.UmsCompanyVc;
import com.gaoshan.chain.util.CredentialUtils;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import com.gaoshan.common.entity.CompanyVcEntity;
import com.gaoshan.common.entity.PersonVcEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;
import org.storm3j.crypto.Sign;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class VcServiceImpl {

    //    @Autowired
    private final IssuerConfig issuer;
    private final IdentityContractEngine identityContractEngine;
    private final SealsServiceImpl sealsService;
//    private final UmsCompanyVcBaseService companyVcBaseService;
//    private final UmsCompanyBaseService companyBaseService;
//    private final GsSignatureBaseService signatureBaseService;
    private final SealsRouterContractEngine sealsRouterContractEngine;


    /**
     * 创建个人vc.
     * @param entity
     * @return
     */
     
    public Credential createPersonVC(PersonVcEntity entity) {
        String keystore = entity.getKeystore();
        if (StrUtil.isBlank(keystore)) {
            throw new RuntimeException("keystore 不能为空");
        }
        JSONObject jsonObject = JSONUtil.parseObj(keystore);
        String address = jsonObject.getStr("address");
        //did
        String did = GIdUtils.convertAddressToGId(address);
        //vc生成
        try {
            Credential credential = new Credential();
            String context = CredentialUtils.getDefaultCredentialContext();
            credential.setContext(context);
            credential.setId(UUID.randomUUID().toString());
//                result.setCptId(args.getCptId());
            credential.setIssuer(issuer.getDid());
            credential.setIssuanceDate(System.currentTimeMillis());
            credential.setExpirationDate(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10));
            HashMap<String, Object> cptJsonSchemaData = new HashMap<String, Object>(3);
            cptJsonSchemaData.put(ParamKeyConstant.PERSON_DID, did);
            cptJsonSchemaData.put(ParamKeyConstant.PERSON_TYPE, IssuerConfig.credentialType.IDCard);
            cptJsonSchemaData.put(ParamKeyConstant.PERSON_NAME, entity.getName());
//            cptJsonSchemaData.put(ParamKeyConstant.PERSON_NUM, entity.getIDNumber());
            credential.setClaim(cptJsonSchemaData);
            // Construct Credential Proof
            Map<String, String> credentialProof = CredentialUtils.buildCredentialProof(
                    credential,
                    issuer.getPrivateKey(),
                    null);
            credential.setProof(credentialProof);
//            //同步上链
//            ResponseData<Boolean> responseData = identityContractEngine.addIdentity(credential.getHash());
            return credential;
        } catch (Exception e) {
            log.error("Generate Credential failed due to system error. ", e);
            return null;
        }
    }
    /**
     * 创建个人vc.
     * @param entity
     * @return
     */
     
    public Credential createCompanyVc(CompanyVcEntity entity) {
        String companyAddress = sealsService.calculateSeal(entity.getLegalDid(), entity.getCreditCode());
        //did
        String companyDid = GIdUtils.convertAddressToGId(companyAddress);
        //vc生成
        try {
            Credential credential = new Credential();
            String context = CredentialUtils.getDefaultCredentialContext();
            credential.setContext(context);
            credential.setId(UUID.randomUUID().toString());
            credential.setIssuer(issuer.getDid());
            credential.setIssuanceDate(System.currentTimeMillis());
            credential.setExpirationDate(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10));
            HashMap<String, Object> cptJsonSchemaData = new HashMap<String, Object>(3);
            cptJsonSchemaData.put(ParamKeyConstant.COMPANY_DID, companyDid);
            cptJsonSchemaData.put(ParamKeyConstant.COMPANY_TYPE, CompanyType.of(entity.getType()).getMessage());
            cptJsonSchemaData.put(ParamKeyConstant.COMPANY_NAME, entity.getName());
            cptJsonSchemaData.put(ParamKeyConstant.COMPANY_CODE, entity.getCreditCode());
            cptJsonSchemaData.put(ParamKeyConstant.COMPANY_LEGAL, entity.getLegalName());
            cptJsonSchemaData.put(ParamKeyConstant.COMPANY_LEGAL_DID, entity.getLegalDid());
            credential.setClaim(cptJsonSchemaData);
            // Construct Credential Proof
            Map<String, String> credentialProof = CredentialUtils.buildCredentialProof(
                    credential,
                    issuer.getPrivateKey(),
                    null);
            credential.setProof(credentialProof);
            return credential;
        } catch (Exception e) {
            log.error("Generate Credential failed due to system error. ", e);
            return null;
        }
    }
     
    public Boolean verify(Credential credential) {
        //验证vc格式、有效期
        ErrorCode innerResponse = CredentialUtils.isCredentialValid(credential);
        if (ErrorCode.SUCCESS.getCode() != innerResponse.getCode()) {
            log.error("Credential input format error!");
            return false;
        }
        //验证链上issuer是否有效
        ResponseData<Boolean> responseData = verifyIssuerExistence(credential.getIssuer());
        if (!responseData.getResult()) {
            return responseData.getResult();
        }
        //验证签名
        responseData = verifySignature(credential);
        if (!responseData.getResult()) {
            return responseData.getResult();
        }
        //验证是否已经吊销
         responseData = identityContractEngine.queryIdentity(credential.getHash());
        return responseData.getResult();
    }
     
    public Boolean verifyByTime(Credential credential,BigInteger time) {
        //验证vc格式、有效期
        ErrorCode innerResponse = CredentialUtils.isCredentialValid(credential);
        if (ErrorCode.SUCCESS.getCode() != innerResponse.getCode()) {
            log.error("Credential input format error!");
            return false;
        }
        //验证签名
        ResponseData<Boolean> responseData = verifySignature(credential);
        if (!responseData.getResult()) {
            return responseData.getResult();
        }
        //验证是否已经吊销
        responseData = identityContractEngine.queryIdentity(credential.getHash(),time);
        return responseData.getResult();
    }

    private ResponseData<Boolean> verifyIssuerExistence(String issuerWeId) {
        ResponseData<Boolean> responseData = identityContractEngine.isOwner(issuerWeId);
        if (responseData == null || !responseData.getResult()) {
            return new ResponseData<>(false, ErrorCode.FAIL);
        }
        return responseData;
    }
    private ResponseData<Boolean> verifySignature(
            Credential credential) {
        try {
            Map<String, Object> disclosureMap = new HashMap<>(credential.getClaim());
            for (Map.Entry<String, Object> entry : disclosureMap.entrySet()) {
                disclosureMap.put(entry.getKey(), CredentialFieldDisclosureValue.DISCLOSED.getStatus());
            }
            String rawData = CredentialUtils
                    .getCredentialThumbprintWithoutSig(credential, disclosureMap);
            Sign.SignatureData signatureData =
                    DataToolUtils.simpleSignatureDeserialization(
                            DataToolUtils.base64Decode(
                                    credential.getSignature().getBytes(StandardCharsets.UTF_8)
                            )
                    );
            // Fetch public key from chain
            String issuerDid = credential.getIssuer();
            boolean res = DataToolUtils
                    .verifySignature(rawData, signatureData, issuerDid);
            return new ResponseData<>(res, ErrorCode.SUCCESS);

        } catch (SignatureException e) {
            log.error(
                    "Generic signatureException occurred during verify signature "
                            + "when verifyCredential: ", e);
            return new ResponseData<>(false, ErrorCode.FAIL);
        } catch (Exception e) {
            log.error(
                    "Generic exception occurred during verify signature when verifyCredential: ", e);
            return new ResponseData<>(false, ErrorCode.FAIL);
        }
    }
    public void approvalCompanyVc(Credential credential) {
            ResponseData<Boolean> responseData2 = identityContractEngine.queryIdentity(credential.getHash());
            if (responseData2.getResult()) {

            } else {
                //先创建企业签章合约
                ResponseData<Boolean> wresponseData1 = sealsRouterContractEngine.createSeal(GIdUtils.convertGIdToAddress(credential.getClaim().get(ParamKeyConstant.COMPANY_LEGAL_DID).toString())
                        , credential.getClaim().get(ParamKeyConstant.COMPANY_CODE).toString());
                if (wresponseData1.getResult()) {

                    //企业vc上链
                    ResponseData<Boolean> wresponseData2 = identityContractEngine.addIdentity(credential.getHash());
                    if (wresponseData2.getResult()) {
                    } else {
                    }
                } else {
                }
            }

    }

    public void revokeCompanyVc(UmsCompanyVc umsCompanyVc) {
        String vc = umsCompanyVc.getVc();
        Credential credential = JSONUtil.toBean(StringEscapeUtils.unescapeJava(vc), Credential.class);
        if (credential == null) {
            log.info("credential is null");
        } else {
            ResponseData<Boolean> responseData2 = identityContractEngine.queryIdentity(credential.getHash());
            if (!responseData2.getResult()) {
            } else {
                //企业vc上链
                ResponseData<Boolean> wresponseData2 = identityContractEngine.revokeIdentity(credential.getHash());
                if (wresponseData2.getResult()) {

                }
            }
        }
    }

    public void doMemberVc() {
        String vc = "";
        Credential credential = JSONUtil.toBean(StringEscapeUtils.unescapeJava(vc), Credential.class);
        ResponseData<Boolean> responseData2 = identityContractEngine.addIdentity(credential.getHash());
    }

    public void doRevokeMemberVc() {
        String vc = "umsMemberVc.getVc()";
        Credential credential = JSONUtil.toBean(StringEscapeUtils.unescapeJava(vc), Credential.class);
        ResponseData<Boolean> responseData2 = identityContractEngine.revokeIdentity(credential.getHash());
    }

}

