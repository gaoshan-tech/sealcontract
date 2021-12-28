package com.gaoshan.chain.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.gaoshan.chain.config.ChainConfig;
import com.gaoshan.chain.config.IssuerConfig;
import com.gaoshan.chain.constant.AddressGasProvider;
import com.gaoshan.chain.constant.ResponseData;
import com.gaoshan.chain.constract.ContractsContract;
import com.gaoshan.chain.constract.IdentityContract;
import com.gaoshan.chain.constract.SealsRouterContract;
import com.gaoshan.chain.constract.engine.ContractsContractEngine;
import com.gaoshan.chain.constract.engine.IdentityContractEngine;
import com.gaoshan.chain.constract.engine.SealsRouterContractEngine;
import com.gaoshan.chain.entity.ContractEntity;
import com.gaoshan.chain.entity.Credential;
import com.gaoshan.chain.entity.SignatureAuth;
import com.gaoshan.chain.util.DataToolUtils;
import com.gaoshan.chain.util.GIdUtils;
import com.gaoshan.common.entity.CompanyVcEntity;
import com.gaoshan.common.entity.PersonVcEntity;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.StringEscapeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.storm3j.crypto.Credentials;
import org.storm3j.crypto.ECKeyPair;
import org.storm3j.crypto.Sign;
import org.storm3j.protocol.Storm3j;
import org.storm3j.protocol.core.RemoteFunctionCall;
import org.storm3j.protocol.core.methods.response.TransactionReceipt;
import org.storm3j.protocol.http.HttpService;
import org.storm3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.List;


@SpringBootTest
@Slf4j
class ChainTest {
    @Autowired
    private IdentityContractEngine identityContractEngine;
    @Autowired
    private SealsRouterContractEngine sealsRouterContractEngine;
    @Autowired
    private ContractsContractEngine contractsContractEngine;
    @Autowired
    private ChainConfig chainConfig;
    @Autowired
    private IssuerConfig issuerConfig;
    @Autowired
    private AddressGasProvider addressGasProvider;
    @Autowired
    private VcServiceImpl vcServiceImpl;
    @Autowired
    private SealsServiceImpl sealsServiceImpl;
    @Autowired
    private ContractServiceImpl contractServiceImpl;
    /***
     * *****************测试前-特别说明********************
     * 1.请修改application.yaml
     * issuer:
     *   did: did:gid:86f3c54028d6183add4aff1f601e5c229e58d298
     *   private-key: "0x4e88da1fe676a30bce1d6c0a7c26d79e5426f26e16f8626210f2e9da228bf60d"
     * 2.首次部署完成（deploy()）后，请application.yaml中的以下内容
     * chain:
     *  identity-contract: "0x5df04283344ee34b5825bab69f5bff1de55c3f02"
     *  seals-router-contract: "0x7215ad9656795660796a910fd7992ef90ca07f3e"
     *  contracts-contract: "0xa05f40fe33ce4a60a8beff03fddc1a26c3ee6d58"
     */

    /**
     * 部署身份、签章、合同三个合约
     */
    @Test
    void deploy() throws Exception {
        Storm3j storm3j = Storm3j.build(new HttpService(chainConfig.getNodeRpc()));
        Credentials credentials = Credentials.create(issuerConfig.getPrivateKey());
        //身份合约
        IdentityContract identityContract = IdentityContract.deploy(storm3j, credentials, addressGasProvider).send();
        log.info("identityContract 部署成功，地址：{}", identityContract.getContractAddress());
        TransactionReceipt initializeReceipt = identityContract.initialize(credentials.getAddress()).send();
        List<IdentityContract.OwnershipTransferredEventResponse> ownershipTransferredEvents = identityContract.getOwnershipTransferredEvents(initializeReceipt);
        Assert.isTrue(CollUtil.isNotEmpty(ownershipTransferredEvents),"IdentityContract initialize 失败");
        log.info("identityContract initialize 事件：{}", ownershipTransferredEvents.get(0).toString());
        //签章合约
        SealsRouterContract sealsRouterContract = SealsRouterContract.deploy(storm3j, credentials, addressGasProvider).send();
        log.info("sealsRouterContract 部署成功，地址：{}", sealsRouterContract.getContractAddress());
        TransactionReceipt sealsReceipt = sealsRouterContract.initialize(credentials.getAddress()).send();
        List<SealsRouterContract.OwnershipTransferredEventResponse> ownershipTransferredEvents2 = sealsRouterContract.getOwnershipTransferredEvents(sealsReceipt);
        Assert.isTrue(CollUtil.isNotEmpty(ownershipTransferredEvents2),"sealsRouterContract initialize 失败");
        log.info("sealsRouterContract initialize 事件：{}", ownershipTransferredEvents2.get(0).toString());
        //合同合约
        ContractsContract contractsContract = ContractsContract.deploy(storm3j, credentials, addressGasProvider).send();
        log.info("contractsContract 部署成功，地址：{}", contractsContract.getContractAddress());
        TransactionReceipt contractsReceipt = contractsContract.initialize(credentials.getAddress()).send();
        List<ContractsContract.OwnershipTransferredEventResponse> ownershipTransferredEvents3 = contractsContract.getOwnershipTransferredEvents(contractsReceipt);
        Assert.isTrue(CollUtil.isNotEmpty(ownershipTransferredEvents3),"contractsContract initialize 失败");
        log.info("contractsContract initialize 事件：{}", ownershipTransferredEvents3.get(0).toString());
        TransactionReceipt RouterAddrReceipt = contractsContract.setAsealsRouterAddr(sealsRouterContract.getContractAddress()).send();
        List<ContractsContract.SetAddrEventResponse> setAddrEvents = contractsContract.getSetAddrEvents(RouterAddrReceipt);
        Assert.isTrue(CollUtil.isNotEmpty(setAddrEvents),"contractsContract setAsealsRouterAddr 失败");
        log.info("chain:\n" +
                "   identity-contract: {}\n" +
                "   seals-router-contract: {}\n" +
                "   contracts-contract: {}", identityContract.getContractAddress(), sealsRouterContract.getContractAddress(), contractsContract.getContractAddress());
    }

    /**
     * 创建个人vc
     * 链上核验
     */
    @Test
     void createPersionVc() {
        String keystore = "{\n" +
                "            version: 3,\n" +
                "            id: '45375b74-2a4e-47f0-848e-fe1615376626',\n" +
                "            address: '79b611508bc620636aaacfd51d7ad40a5ab406c4',\n" +
                "          }";

        final PersonVcEntity personVcEntity = new PersonVcEntity();
        personVcEntity.setKeystore(keystore);
        personVcEntity.setName("张三" + System.currentTimeMillis());
        personVcEntity.setIDNumber("340123202112120120");
        Credential personVCCredential = vcServiceImpl.createPersonVC(personVcEntity);
        log.info("personVC:{}", JSONUtil.toJsonStr(personVCCredential));
        log.info("vc上链---开始--");
        ResponseData<Boolean> responseData = identityContractEngine.addIdentity(personVCCredential.getHash());
        Assert.isTrue(responseData.getResult(),"vc上链失败");
        log.info("vc核验----开始----");
        Boolean verify = vcServiceImpl.verify(personVCCredential);
        Assert.isTrue(verify,"vc核验失败");
    }

    /**
     * 创建企业vc
     * 部署企业签章合约
     * 核验企业vc
     */
    @Test
     void createCompanyVc() {
        final CompanyVcEntity companyVcEntity = new CompanyVcEntity();
        companyVcEntity.setLegalDid(issuerConfig.getDid());
        companyVcEntity.setName("安徽测试有限公司");
        companyVcEntity.setCreditCode("9527" + System.currentTimeMillis());
        companyVcEntity.setLegalName("张三" + System.currentTimeMillis());
        companyVcEntity.setType(1);
        Credential companyVcCredential = vcServiceImpl.createCompanyVc(companyVcEntity);
        log.info("创建企业签章---开始--");
        ResponseData<Boolean> sealResponseData = sealsRouterContractEngine.createSeal(GIdUtils.convertGIdToAddress(companyVcEntity.getLegalDid()), companyVcEntity.getCreditCode());
        Assert.isTrue(sealResponseData.getResult(),"创建企业签章失败");
        log.info("企业vc上链---开始--");
        ResponseData<Boolean> responseData = identityContractEngine.addIdentity(companyVcCredential.getHash());
        Assert.isTrue(responseData.getResult(),"企业vc上链失败");
        log.info("企业vc核验----开始----");
        Boolean verify = vcServiceImpl.verify(companyVcCredential);
        Assert.isTrue(verify,"企业vc核验失败");
        log.info("企业Vc:{}", JSONUtil.toJsonStr(companyVcCredential));
        log.info("企业签章地址:{}", sealsServiceImpl.calculateSeal(companyVcEntity.getLegalDid(), companyVcEntity.getCreditCode()));
    }

    /**
     * 签章授权
     */
    @Test
     void approvl() throws SignatureException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Credentials credentials = Credentials.create(issuerConfig.getPrivateKey());
        SignatureAuth signatureAuth = new SignatureAuth();
        signatureAuth.setContractAddress("0x2c3b1b623722ad3c28d304f2ab1dea36525ae99f");//先执行createCompanyVc()，获得企业签章地址，修改此值
        signatureAuth.setType(1);
        signatureAuth.setTargetDid(
                GIdUtils.convertAddressToGId(Credentials.create(DataToolUtils.createKeyPair()).getAddress()));
        signatureAuth.setAuthDid(GIdUtils.convertAddressToGId(credentials.getAddress()));
        signatureAuth.setAuthTime(System.currentTimeMillis() / 1000);
        String encodePacked = sealsRouterContractEngine.encodePacked(signatureAuth.getContractAddress(),
                BigInteger.valueOf(signatureAuth.getType()), GIdUtils.convertGIdToAddress(signatureAuth.getTargetDid()),
                BigInteger.valueOf(signatureAuth.getAuthTime())).getResult();
        //签名
        Sign.SignatureData authSignatureData = Sign.signPrefixedMessage(
                DataToolUtils.sha3(Numeric.hexStringToByteArray(encodePacked)), credentials.getEcKeyPair());
        //base64编码
        String base64Signature = new String(
                DataToolUtils.base64Encode(DataToolUtils.simpleSignatureSerialization(authSignatureData)),
                StandardCharsets.UTF_8
        );
        signatureAuth.setAuthSign(base64Signature);
        Boolean aBoolean = sealsServiceImpl.approveSeal(signatureAuth);
        Assert.isTrue(aBoolean,"签章授权失败");
    }
    /**
     * 合同文件签署
     * 合同文件签名数据上链
     */
    @Test
     void signFile(){
        ContractEntity src = contractServiceImpl.createSign(new ContractEntity());
        log.info(JSONUtil.toJsonStr(src));
        ResponseData<Boolean> booleanResponseData = contractServiceImpl.toChain(src);
        Assert.isTrue(booleanResponseData.getResult(),"合同签名上链失败");
    }
    
}