package com.gaoshan.chain.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.gaoshan.common.config.BaseSwaggerConfig;
import com.gaoshan.common.domain.SwaggerProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger IssuerConfig相关配置
 * Created by macro on 2018/4/26.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "issuer")
public class IssuerConfig {
    @ApiModelProperty(value = "规范说明链接")
    private String context;
    @ApiModelProperty(value = "Issuer信息链接")
    private String issuersInfo;
    @ApiModelProperty(value = "签名方法")
    private String signType;
    @ApiModelProperty(value = "验证方法链接")
    private String verificationMethod;
    @ApiModelProperty(value = "did")
    private String did;
    @ApiModelProperty(value = "Issuer私钥")
    private String privateKey;
//    @ApiModelProperty(value = "加密后助记词")
//    private String encryptMnemonic;
//    @ApiModelProperty(value = "加密私钥")
//    private String encryptKey;
//    @ApiModelProperty(value = "超级管理员地址")
//    private String adminAddr;
//    @ApiModelProperty(value = "管理员地址")
//    private String ownerAddr;
//    @ApiModelProperty(value = "hd钱包最大索引")
//    private Integer maxHdWalletIndex;

    public String getId() {
        return IdUtil.randomUUID();
    }

    public String getIssuanceDate() {
        DateTime now = DateTime.now();
        return now.toDateStr() + "T" + now.toTimeStr() + "Z";
    }
    public class Type{
        public static final String PersonalIdentity="PersonalIdentity";
        public static final String CorporateIdentity="CorporateIdentity";
    }
    public class credentialType{
        public static final String IDCard="IDCard";
        public static final String Passport="passport";
        public static final String CompanyInformation="companyInformation";
    }

    public static void main(String[] args) {
        DateTime now = DateTime.now();
        System.out.printf(now.toDateStr() + "T" + now.toTimeStr() + "Z");
    }
}
