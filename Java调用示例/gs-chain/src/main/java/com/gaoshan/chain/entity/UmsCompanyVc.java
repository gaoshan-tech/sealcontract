package com.gaoshan.chain.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 企业身份表
 * </p>
 *
 * @author Gaoshan
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="UmsCompanyVc对象", description="企业身份表")
public class UmsCompanyVc implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "企业id")
    private Long companyId;

    @ApiModelProperty(value = "vc身份")
    private String vc;

    @ApiModelProperty(value = "企业DID")
    private String did;

    @ApiModelProperty(value = "企业签章合约地址")
    private String contractAddress;

    @ApiModelProperty(value = "交易id")
    private String txid;

    @ApiModelProperty(value = "上链状态（0待上链，1进行中、2成功、3失败）")
    private Integer contractStatus;

    @ApiModelProperty(value = "吊销状态 0待执行 1未吊销 2已吊销")
    private Integer revoked;

    @ApiModelProperty(value = "删除标记")
    private Integer deleted;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
