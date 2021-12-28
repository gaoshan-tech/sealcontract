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
public class PersonVcEntity implements Serializable {

//    @ApiModelProperty(value = "用户id")
//    private String memberId;
//    @ApiModelProperty(value = "链上地址")
//    private String address;
//    @ApiModelProperty(value = "did")
//    private String did;
    @ApiModelProperty(value = "keystore")
    private String keystore;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "身份证号")
    private String IDNumber;
//    @ApiModelProperty(value = "vc身份")
//    private String vc;
}
