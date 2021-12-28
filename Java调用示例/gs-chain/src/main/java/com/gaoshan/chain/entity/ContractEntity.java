/*
 *       Copyright© (2018) WeBank Co., Ltd.
 *
 *       This file is part of weid-java-sdk.
 *
 *       weid-java-sdk is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       weid-java-sdk is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with weid-java-sdk.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.gaoshan.chain.entity;

import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.constant.ParamKeyConstant;
import com.gaoshan.chain.util.CredentialUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

@Data
public class ContractEntity implements Hashable,Cloneable {

    /**
     * Required: The context field.
     */
    @ApiModelProperty(value = "文件执行规范")
    private String context;

    /**
     * Required: The ID.
     */
    @ApiModelProperty(value = "签名id")
    private String id;
    /**
     * Required: The context field.
     */
    @ApiModelProperty(value = "文件hash")
    private String contractHash;

    /**
     * Required: The context field.
     */
    @ApiModelProperty(value = "嵌套的签名")
    private ContractEntity nestSignature;



    @ApiModelProperty(value = "签张类型（0个人、1公司章、2财务章、3文件章，。。。100以后为自定义章）")
    private Integer type;


    @ApiModelProperty(value = "文件签名者/签章")
    private Credential signerVC;
    /**
     * Required: The create date.
     */
    @ApiModelProperty(value = "签名日期")
    private Long signDate;


    @ApiModelProperty(value = "签字签章内容")
    private Map<String, Object> sealsClaim;


    @ApiModelProperty(value = "签名相关")
    private Map<String, String> proof;

    /**
     * Directly extract the signature value from credential.
     *
     * @return signature value
     */
    @ApiModelProperty(value = "证书签名结果")
    public String getSignature() {
        if (proof != null) {
            return proof.get(ParamKeyConstant.CREDENTIAL_SIGNATURE);
        }
        return StrUtil.EMPTY;
    }
    @ApiModelProperty(value = "证书签名人")
    public String getProofCreator() {
        if (proof != null) {
            return proof.get(ParamKeyConstant.PROOF_CREATOR);
        }
        return StrUtil.EMPTY;
    }
    /**
     * Get the unique hash value of this Credential.
     *
     * @return hash value
     */
    @ApiModelProperty(value = "待签名文件hash")
    public String getHashWithoutSig() {
        return CredentialUtils.getContractHash(this);
    }

    /**
     * Get the signature thumbprint for re-signing.
     *
     * @return thumbprint
     */
    @ApiModelProperty(value = "证书签名内容")
    public String getSignatureThumbprint() {
        return CredentialUtils.getContractThumbprint(this);
    }

    @Override
    public String getHash() {
        return null;
    }
}
