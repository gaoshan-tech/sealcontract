

package com.gaoshan.chain.entity;

import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.util.CredentialUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * Credential response.
 *
 * @author
 */
@Data
public class CredentialWrapper implements Hashable {

    /**
     * Required: The Credential.
     */
    @ApiModelProperty(value = "证书信息")
    private Credential credential;

    /**
     * Required: key is the credential field, and value "1" for disclosure to the third party, "0"
     * for no disclosure.
     */
    @ApiModelProperty(value = "证书选择披露")
    private Map<String, Object> disclosure;

    /**
     * Generate the unique hash of this CredentialWrapper.
     *
     * @return hash value
     */
    @Override
    @ApiModelProperty(value = "证书hash")
    public String getHash() {
        if (this == null) {
            return StrUtil.EMPTY;
        }
        if (this.getDisclosure() == null || this.getDisclosure().size() == 0) {
            return this.getCredential().getHash();
        }
        Credential credential = this.getCredential();
        if (ErrorCode.SUCCESS != CredentialUtils.isCredentialValid(credential)) {
            return StrUtil.EMPTY;
        }
        return CredentialUtils.getCredentialWrapperHash(this);
    }

    /**
     * Directly extract the signature value from credential.
     *
     * @return signature value
     */
    @ApiModelProperty(value = "证书签名结果")
    public String getSignature() {
        return credential.getSignature();
    }

    /**
     * Get the signature thumbprint for re-signing.
     *
     * @return thumbprint
     */
    @ApiModelProperty(value = "证书签名内容")
    public String getSignatureThumbprint() {
        return CredentialUtils.getCredentialThumbprint(this.getCredential(), this.getDisclosure());
    }
}
