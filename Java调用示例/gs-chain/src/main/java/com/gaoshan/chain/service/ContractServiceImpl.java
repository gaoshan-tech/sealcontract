package com.gaoshan.chain.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ParamKeyConstant;
import com.gaoshan.chain.constant.ResponseData;
import com.gaoshan.chain.constant.SignatureType;
import com.gaoshan.chain.constract.engine.ContractsContractEngine;
import com.gaoshan.chain.constract.engine.IdentityContractEngine;
import com.gaoshan.chain.constract.engine.SealsRouterContractEngine;
import com.gaoshan.chain.entity.ContractEntity;
import com.gaoshan.chain.entity.Credential;
import com.gaoshan.chain.util.CredentialUtils;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import com.gaoshan.common.entity.CompanyVcEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.storm3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class ContractServiceImpl {

    private final IssuerConfig issuer;
    private final SealsRouterContractEngine sealsRouterContractEngine;
    private final ContractsContractEngine contractsContractEngine;
    private final IdentityContractEngine identityContractEngine;
    private final VcServiceImpl vcService;


    /**
     * 创建文件签名.
     * @return
     */
    public ContractEntity createSign(ContractEntity nestContractEntity) {
        try {
            CompanyVcEntity companyVcEntity = new CompanyVcEntity();
            companyVcEntity.setLegalDid(issuer.getDid());
            companyVcEntity.setName("测试科技有限公司");
            companyVcEntity.setCreditCode("2323" + System.currentTimeMillis());
            companyVcEntity.setLegalName("企业法人");
            companyVcEntity.setType(1);
            Credential companyVc = vcService.createCompanyVc(companyVcEntity);
            ResponseData<Boolean> sealResponseData = sealsRouterContractEngine.createSeal(GIdUtils.convertGIdToAddress(companyVcEntity.getLegalDid()), companyVcEntity.getCreditCode());
            ResponseData<Boolean> responseData = identityContractEngine.addIdentity(companyVc.getHash());
            log.info(responseData.getResult().toString());
            ContractEntity entity = new ContractEntity();
            String context = CredentialUtils.getDefaultCredentialContext();
            entity.setNestSignature(nestContractEntity);
            entity.setContext(context);
            entity.setId(UUID.randomUUID().toString());
            entity.setSignerVC(companyVc);
            entity.setSignDate(System.currentTimeMillis());
            entity.setType(SignatureType.COMPANY.ordinal());
            entity.setContractHash("464421b8ba40f261abbd8a81010607f6e4b2e0dd07bde5e108bea641e146ab00");
            HashMap<String, Object> sealsClaim = new HashMap<>();
            JSONObject jsonObject = JSONUtil.parseObj("{\"signType\":0,\"position\":0,\"sign\":{\"picUrl\":\"https://yinji.gaoshan.co/oss/gs2021/20211101/1-1455132664389701632.png\"},\"target\":{\"name\":\"安徽高山科技有限公司\",\"phone\":\"13476615091\"},\"signIndex\":0,\"x\":204.575,\"y\":198.73}");
            sealsClaim.put("0", jsonObject);
            entity.setSealsClaim(sealsClaim);
            log.info("entity{}", JSONUtil.toJsonStr(entity));
            // Construct Credential Proof
            Map<String, String> credentialProof = CredentialUtils.buildContractProof(
                    entity,
                    issuer.getPrivateKey());
            entity.setProof(credentialProof);
            return entity;
        } catch (Exception e) {
            log.error("Generate ContractEntity failed due to system error. ", e);
            return null;
        }
    }

    /**
     * 已上链文件校验
     *
     * @param contractEntity
     * @return
     */
    
    public Boolean verify(ContractEntity contractEntity) {
        //验证文件格式
        ErrorCode innerResponse = CredentialUtils.isContractValid(contractEntity);
        if (ErrorCode.SUCCESS.getCode() != innerResponse.getCode()) {
            log.error("contractEntity input format error!");
            return false;
        }
        //验证签名
        ResponseData<Boolean> responseData = verifySignature(contractEntity);
        if (!responseData.getResult()) {
            return responseData.getResult();
        }
        //验证签名是否已上链,且查询文件上链时间
        ResponseData<Boolean> exist = contractsContractEngine.isExist(contractEntity.getContractHash(), contractEntity.getHashWithoutSig(),
                contractEntity.getType() == 0 ? "0x0000000000000000000000000000000000000000" : GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())
//                GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())
                , BigInteger.valueOf(contractEntity.getType())
                , GIdUtils.convertGIdToAddress(contractEntity.getProofCreator()));
        if (exist.getResult() == null) {
            return false;
        }
        //查询签署时间
        ResponseData<BigInteger> query = contractsContractEngine.querySignTimel(contractEntity.getContractHash(), contractEntity.getHashWithoutSig(),
                GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())
                , BigInteger.valueOf(contractEntity.getType())
                , GIdUtils.convertGIdToAddress(contractEntity.getProofCreator()));
        if (exist.getResult() == null) {
            return false;
        }
        //验证链上issuerVc,在签署文件前是否有效
        Boolean verify = vcService.verifyByTime(contractEntity.getSignerVC(), query.getResult());
        if (!verify) {
            return verify;
        }
        return responseData.getResult();
    }

    /**
     * 上链前校验
     *
     * @param contractEntity
     * @return
     */
    
    public Boolean verifyBeforeChain(ContractEntity contractEntity) {
        //验证文件格式
        ErrorCode innerResponse = CredentialUtils.isContractValid(contractEntity);
        if (ErrorCode.SUCCESS.getCode() != innerResponse.getCode()) {
            log.error("验证文件格式错误");
            return false;
        }
        //验证链上issuerVc是否有效
        Boolean verify = vcService.verify(contractEntity.getSignerVC());
        if (!verify) {
            log.error("链上issuerVc无效");
            return verify;
        }
        //验证文件签名
        ResponseData<Boolean> responseData = verifySignature(contractEntity);
        if (!responseData.getResult()) {
            log.error("文件签名无效");
            return responseData.getResult();
        }
        //如果使用企业签章
        if (contractEntity.getType() != SignatureType.PERSON.ordinal()) {
            //验证签章合约是否有效
            responseData = sealsRouterContractEngine.querySealStatus(
                    GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString()));
            if (!responseData.getResult()) {
                log.error("签章合约无效");
                return responseData.getResult();
            }
            //验证签名人是否有此印章授权
            responseData = sealsRouterContractEngine.querySealApprovl(
                    GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())
                    , BigInteger.valueOf(contractEntity.getType())
                    , GIdUtils.convertGIdToAddress(contractEntity.getProofCreator()));
            if (!responseData.getResult()) {
                log.error("签名人无此印章授权");
            }
        }

        return responseData.getResult();
    }


    /**
     * 文件上链
     *
     * @param contractEntity
     * @return
     */
    
    public ResponseData<Boolean> toChain(ContractEntity contractEntity) {
        try {

            //预校验
            Boolean verify = verifyBeforeChain(contractEntity);
            if (!verify) {
                return new ResponseData(false, ErrorCode.FAIL.getCode(), "");
            }
            //判断是否已上链
            ResponseData<Boolean> responseData1 = contractsContractEngine.isExist(
                    contractEntity.getContractHash(),
                    contractEntity.getHashWithoutSig(),
                    contractEntity.getType() == 0 ? "0x0000000000000000000000000000000000000000" : GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())
                    , BigInteger.valueOf(contractEntity.getType())
                    , GIdUtils.convertGIdToAddress(contractEntity.getProofCreator()));
            if (responseData1.getResult()) {
                return responseData1;
            }
            //文件签名上链验证
            ResponseData<Boolean> responseData2 = contractsContractEngine.addSign(
                    contractEntity.getContractHash(),
                    contractEntity.getHashWithoutSig(),
                    contractEntity.getType() == 0 ? "0x0000000000000000000000000000000000000000" : GIdUtils.convertGIdToAddress(contractEntity.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())
                    , BigInteger.valueOf(contractEntity.getType())
                    , GIdUtils.convertGIdToAddress(contractEntity.getProofCreator()),
                    contractEntity.getSignature());
            return responseData2;
        }catch (Exception e){
            return new ResponseData(false, ErrorCode.FAIL.getCode(), "");
        }
    }

    private ResponseData<Boolean> verifySignature(
            ContractEntity contractEntity) {
        try {
            boolean res = DataToolUtils
                    .verifyPrefixedSignatureFromWeId(Numeric.hexStringToByteArray(contractEntity.getHashWithoutSig()), contractEntity.getSignature(), contractEntity.getProofCreator());
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



}

