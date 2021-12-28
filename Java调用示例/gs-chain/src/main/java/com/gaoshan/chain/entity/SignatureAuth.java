package com.gaoshan.chain.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 *
 * @author Gaoshan
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SignatureAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "签章地址")
    private String contractAddress;

    @ApiModelProperty(value = "授权类型：0取消授权1授权")
    private Integer type;

    @ApiModelProperty(value = "授权用户ID")
    private Long targetId;

    @ApiModelProperty(value = "授权用户DID")
    private String targetDid;

    @ApiModelProperty(value = "授权人id")
    private Long authId;

    @ApiModelProperty(value = "授权人DID")
    private String authDid;

    @ApiModelProperty(value = "授权签名")
    private String authSign;

    @ApiModelProperty(value = "交易id")
    private String txid;

    @ApiModelProperty(value = "上链状态（0待上链，1进行中、2成功、3失败）")
    private Integer contractStatus;


    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人")
    private Long createBy;

    @ApiModelProperty(value = "更新人")
    private Long updateBy;

    @ApiModelProperty(value = "授权时间")
    private Long authTime;


}
