package com.gaoshan.common.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyVcEntity implements Serializable {

//    @ApiModelProperty(value = "企业id")
//    private String companyId;
//    @ApiModelProperty(value = "链上地址")
//    private String address;
    @ApiModelProperty(value = "法人did")
    private String legalDid;
    @ApiModelProperty(value = "企业名称")
    private String name;
    @ApiModelProperty(value = "企业编号")
    private String creditCode;
    @ApiModelProperty(value = "法人姓名")
    private String legalName;
    @ApiModelProperty(value = "企业类型")
    private Integer type;
//    @ApiModelProperty(value = "企业vc")
//    private String vc;
}
