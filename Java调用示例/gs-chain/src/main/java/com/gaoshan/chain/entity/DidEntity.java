package com.gaoshan.chain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.io.Serializable;
import java.util.Date;

/**
 * @author lijie
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DidEntity implements Serializable {
    @ApiModelProperty(value = "did")
    private String did;
    @ApiModelProperty(value = "用户标识")
    private String personId;
    @ApiModelProperty(value = "公钥")
    private String publicKey;

    @JsonIgnore
    @ApiModelProperty(value = "私钥")
    private String privateKey;

    @ApiModelProperty(value = "Did创建者")
    @JsonIgnore
    private DidEntity issuer;

    @ApiModelProperty(value = "did状态")
    private int status;

    @CreatedDate
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @LastModifiedDate
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "账户类型")
    private int type;

    @ApiModelProperty(value = "机构名称")
    private String issuerName;
}
