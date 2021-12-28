

package com.gaoshan.chain.entity;

import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ParamKeyConstant;
import com.gaoshan.chain.util.CredentialPojoUtils;
import com.gaoshan.chain.util.DataToolUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The base data structure to handle Credential info.
 *
 * @author
 */
@Data
public class CredentialPojo implements IProof, JsonSerializer, Hashable {

    private static final Logger logger = LoggerFactory.getLogger(CredentialPojo.class);

    /**
     * the serialVersionUID.
     */
    private static final long serialVersionUID = 8197843857223846978L;

    /**
     * Required: The context field.
     */
    private String context;

    /**
     * Required: The ID.
     */
    private String id;

    /**
     * Required: The CPT type in standard integer format.
     */
    private Integer cptId;

    /**
     * Required: The issuer WeIdentity DID.
     */
    private String issuer;

    /**
     * Required: The create date.
     */
    private Long issuanceDate;

    /**
     * Required: The expire date.
     */
    private Long expirationDate;

    /**
     * Required: The claim data.
     */
    private Map<String, Object> claim;

    /**
     * Required: The credential proof data.
     */
    private Map<String, Object> proof;

    /**
     * Required: The credential type default is VerifiableCredential.
     */
    private List<String> type;

    /**
     * 添加type.
     *
     * @param typeValue the typeValue
     */
    public void addType(String typeValue) {
        if (type == null) {
            type = new ArrayList<String>();
        }
        type.add(typeValue);
    }

    /**
     * Directly extract the signature value from credential.
     *
     * @return signature value
     */
    public String getSignature() {
        return toString(getValueFromProof(proof, ParamKeyConstant.PROOF_SIGNATURE));
    }

    /**
     * Directly extract the proof type from credential.
     *
     * @return proof type
     */
    public String getProofType() {
        return toString(getValueFromProof(proof, ParamKeyConstant.PROOF_TYPE));
    }

    /**
     * Directly extract the salt from credential.
     *
     * @return salt
     */
    public Map<String, Object> getSalt() {
        return (Map<String, Object>) getValueFromProof(proof, ParamKeyConstant.PROOF_SALT);
    }

    /**
     * put the salt into proof.
     *
     * @param salt map of salt
     */
    public void setSalt(Map<String, Object> salt) {
        putProofValue(ParamKeyConstant.PROOF_SALT, salt);
    }

    /**
     * put the key-value into proof.
     *
     * @param key the key of proof
     * @param value the value of proof
     */
    public void putProofValue(String key, Object value) {
        if (proof == null) {
            proof = new HashMap<>();
        }
        proof.put(key, value);
    }

    /**
     * convert CredentialPojo to JSON String.
     *
     * @return CredentialPojo
     */
    @Override
    public String toJson() {
        String json = DataToolUtils.convertTimestampToUtc(DataToolUtils.serialize(this));
        return DataToolUtils.addTagFromToJson(json);
    }



    /**
     * Generate the unique hash of this CredentialPojo.
     *
     * @return hash value
     */
    @Override
    public String getHash() {
        if (CredentialPojoUtils.isCredentialPojoValid(this) != ErrorCode.SUCCESS) {
            return StrUtil.EMPTY;
        }
        return CredentialPojoUtils.getCredentialPojoHash(this, null);
    }

    /**
     * Get the signature thumbprint for re-signing.
     *
     * @return thumbprint
     */
    public String getSignatureThumbprint() {
        return CredentialPojoUtils.getCredentialThumbprintWithoutSig(this, this.getSalt(), null);
    }
}
