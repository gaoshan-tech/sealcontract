package com.gaoshan.chain.config;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * Swagger IssuerConfig相关配置
 * Created by macro on 2018/4/26.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "chain")
public class ChainConfig {
    @ApiModelProperty(value = "node rpc")
    private String nodeRpc;
    @ApiModelProperty(value = "gasLimit")
    private BigInteger gasLimit;
    @ApiModelProperty(value = "gasPrice")
    private BigInteger gasPrice;
    @ApiModelProperty(value = "身份合约地址")
    private String identityContract;
    @ApiModelProperty(value = "签章工厂合约地址")
    private String sealsRouterContract;
    @ApiModelProperty(value = "文件地址")
    private String contractsContract;
}
