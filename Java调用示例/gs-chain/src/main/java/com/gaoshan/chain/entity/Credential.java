
package com.gaoshan.chain.entity;

import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ParamKeyConstant;
import com.gaoshan.chain.util.CredentialUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * The base data structure to handle Credential info.
 *
 * @author
 */
@Data
public class Credential implements Hashable {

    /**
     * Required: The context field.
     */
    @ApiModelProperty(value = "证书执行规范")
    private String context;

    /**
     * Required: The ID.
     */
    @ApiModelProperty(value = "证书id")
    private String id;

    /**
     * Required: The CPT type in standard integer format.
     */
//    @ApiModelProperty(value = "证书模板id")
//    private Integer cptId;

    /**
     * Required: The issuer WeIdentity DID.
     */
    @ApiModelProperty(value = "证书发行者")
    private String issuer;

    /**
     * Required: The create date.
     */
    @ApiModelProperty(value = "证书发行日期")
    private Long issuanceDate;

    /**
     * Required: The expire date.
     */
    @ApiModelProperty(value = "证书过期日期")
    private Long expirationDate;

    /**
     * Required: The claim data.
     */
    @ApiModelProperty(value = "证书内容")
    private Map<String, Object> claim;

    /**
     * Required: The credential proof data.
     */
    @ApiModelProperty(value = "发行者签名相关")
    private Map<String, String> proof;

    /**
     * Directly extract the signature value from credential.
     *
     * @return signature value
     */
    @ApiModelProperty(value = "证书签名结果")
    public String getSignature() {
        return getValueFromProof(ParamKeyConstant.CREDENTIAL_SIGNATURE);
    }

    /**
     * Directly extract the proof type from credential.
     *
     * @return proof type
     */
    @ApiModelProperty(value = "椭圆曲线算法")
    public String getProofType() {
        return getValueFromProof(ParamKeyConstant.PROOF_TYPE);
    }

    private String getValueFromProof(String key) {
        if (proof != null) {
            return proof.get(key);
        }
        return StrUtil.EMPTY;
    }

    /**
     * Get the unique hash value of this Credential.
     *
     * @return hash value
     */
    @Override
    @ApiModelProperty(value = "证书hash")
    public String getHash() {
        if (CredentialUtils.isCredentialValid(this) != ErrorCode.SUCCESS) {
            return StrUtil.EMPTY;
        }
        return CredentialUtils.getCredentialHash(this);
    }

    /**
     * Get the signature thumbprint for re-signing.
     *
     * @return thumbprint
     */
    @ApiModelProperty(value = "证书签名内容")
    public String getSignatureThumbprint() {
        return CredentialUtils.getCredentialThumbprint(this, null);
    }
}
