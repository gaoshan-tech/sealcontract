
package com.gaoshan.chain.constant;

import cn.hutool.json.JSONObject;
import com.gaoshan.chain.config.IssuerConfig;

/**
 */
public final class ParamKeyConstant {


    /**
     * WeIdService related param names.
     */
    public static final String PUBLIC_KEY = "publicKey";

    /**
     * AuthorityIssuer related param names.
     */
    public static final String AUTHORITY_ISSUER_NAME = "name";

    /**
     * CptService related param names.
     */
    public static final String CPT_JSON_SCHEMA = "cptJsonSchema";
    public static final String CPT_SIGNATURE = "cptSignature";
    public static final String CPT = "Cpt";

    /**
     * CredentialService related param names.
     */
    public static final String CPT_ID = "cptId";
    public static final String ISSUER = "issuer";
    public static final String CLAIM = "claim";
    public static final String EXPIRATION_DATE = "expirationDate";
    public static final String CREDENTIAL_SIGNATURE = "signature";
    public static final String CONTEXT = "context";
    public static final String CREDENTIAL_ID = "id";
    public static final String ISSUANCE_DATE = "issuanceDate";
    public static final String POLICY = "Policy";
    public static final String POLICY_PACKAGE = "com.webank.weid.cpt.policy.";

    /**
     * Person sonSchemaData.
     */
    public static final String PERSON_DID = "did";
    public static final String PERSON_TYPE = "type";
    public static final String PERSON_NAME = "name";
    public static final String PERSON_NUM = "IDNumber";

    /**
     * Person sonSchemaData.
     */
    public static final String COMPANY_DID = "did";
    public static final String COMPANY_TYPE = "type";
    public static final String COMPANY_NAME = "name";
    public static final String COMPANY_CODE = "code";
    public static final String COMPANY_LEGAL = "legalName";
    public static final String COMPANY_LEGAL_DID = "legalDid";
    /**
     * proof key.
     */
    public static final String PROOF = "proof";
    public static final String PROOF_SIGNATURE = "signatureValue";
    public static final String PROOF_TYPE = "type";
    public static final String PROOF_CREATED = "created";
    public static final String PROOF_CREATOR = "creator";
    public static final String PROOF_SALT = "salt";


}
