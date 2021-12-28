

package com.gaoshan.chain.util;

import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.constant.CredentialConstant;
import com.gaoshan.chain.constant.CredentialFieldDisclosureValue;
import com.gaoshan.chain.constant.ErrorCode;
import com.gaoshan.chain.constant.ParamKeyConstant;
import com.gaoshan.chain.entity.CredentialPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * The Class CredentialUtils.
 *
 */
public final class CredentialPojoUtils {

    /**
     * log4j object, for recording log.
     */
    private static final Logger logger = LoggerFactory.getLogger(CredentialPojoUtils.class);

    private static Integer NOT_DISCLOSED =
        CredentialFieldDisclosureValue.NOT_DISCLOSED.getStatus();

    /**
     * @param credential target Credential object
     * @param salt Salt Map
     * @param disclosures Disclosure Map
     * @return Hash value in String.
     */
    public static String getCredentialThumbprintWithoutSig(
        CredentialPojo credential,
        Map<String, Object> salt,
        Map<String, Object> disclosures) {
        try {
            Map<String, Object> credMap = DataToolUtils.objToMap(credential);
            // Preserve the same behavior as in CredentialUtils - will merge later
            credMap.remove(ParamKeyConstant.PROOF);
            credMap.put(ParamKeyConstant.PROOF, null);
            String claimHash = getClaimHash(credential, salt, disclosures);
            credMap.put(ParamKeyConstant.CLAIM, claimHash);
            return DataToolUtils.mapToCompactJson(credMap);
        } catch (Exception e) {
            logger.error("get Credential Thumbprint WithoutSig error.", e);
            return StrUtil.EMPTY;
        }
    }

    /**
     * Check if the two credentials are equal. Will traverse each field.
     *
     * @param credOld first credential
     * @param credNew second credential
     * @return true if yes, false otherwise
     */
    public static boolean isEqual(CredentialPojo credOld, CredentialPojo credNew) {
        if (credOld == null && credNew == null) {
            return true;
        }
        if (credOld == null || credNew == null) {
            return false;
        }
        return isProofContentEqual(credOld.getProof(), credNew.getProof())
            && credOld.getCptId().equals(credNew.getCptId())
            && credOld.getExpirationDate().equals(credNew.getExpirationDate())
            && credOld.getType().equals(credNew.getType())
            && credOld.getHash().equalsIgnoreCase(credNew.getHash())
            && credOld.getContext().equalsIgnoreCase(credNew.getContext())
            && credOld.getId().equalsIgnoreCase(credNew.getId())
            && credOld.getIssuanceDate().equals(credNew.getIssuanceDate())
            && credOld.getIssuer().equalsIgnoreCase(credNew.getIssuer());
    }

    private static boolean isProofContentEqual(Object a, Object b) {
        if (a instanceof Map && b instanceof Map) {
            Set<String> keySet = ((Map) a).keySet();
            Set<String> keySetb = ((Map) b).keySet();
            for (String key : keySet) {
                if (!keySetb.contains(key)) {
                    return false;
                }
            }
            for (String key : keySetb) {
                if (!keySet.contains(key)) {
                    return false;
                }
            }
            boolean equals = true;
            for (String key : keySet) {
                equals = isProofContentEqual(((Map) a).get(key), ((Map) b).get(key));
                if (!equals) {
                    return false;
                }
            }
        } else if (a instanceof String && b instanceof String) {
            return ((String) a).equalsIgnoreCase((String) b);
        } else if (a instanceof Number && b instanceof Number) {
            return ((Number) a).intValue() == ((Number) b).intValue();
        }
        return true;
    }

    /**
     * Concat all fields of Credential info, with signature. This should be invoked when calculating
     * Credential Evidence. Return null if credential format is illegal.
     *
     * @param credential target Credential object
     * @param salt Salt Map
     * @param disclosures Disclosure Map
     * @return Hash value in String.
     */
    public static String getCredentialPojoThumbprint(
        CredentialPojo credential,
        Map<String, Object> salt,
        Map<String, Object> disclosures
    ) {
        try {
            Map<String, Object> credMap = DataToolUtils.objToMap(credential);
            // Replace the Claim value object with claim hash value to preserve immutability
            String claimHash = getClaimHash(credential, salt, disclosures);
            credMap.put(ParamKeyConstant.CLAIM, claimHash);
            // Remove the whole Salt field to preserve immutability
            Map<String, Object> proof = (Map<String, Object>) credMap.get(ParamKeyConstant.PROOF);
            proof.remove(ParamKeyConstant.PROOF_SALT);
            proof.put(ParamKeyConstant.PROOF_SALT, null);
            credMap.remove(ParamKeyConstant.PROOF);
            credMap.put(ParamKeyConstant.PROOF, proof);
            return DataToolUtils.mapToCompactJson(credMap);
        } catch (Exception e) {
            logger.error("get Credential Thumbprint error.", e);
            return StrUtil.EMPTY;
        }
    }

    /**
     * Create a full CredentialPojo Hash for a Credential based on all its fields, which is
     * resistant to selective disclosure.
     *
     * @param credentialPojo target Credential object
     * @param disclosures Disclosure Map
     * @return Hash value in String.
     */
    public static String getCredentialPojoHash(CredentialPojo credentialPojo,
        Map<String, Object> disclosures) {
        String rawData = getCredentialPojoThumbprint(credentialPojo, credentialPojo.getSalt(),
            disclosures);
        if (StrUtil.isEmpty(rawData)) {
            return StrUtil.EMPTY;
        }
        return DataToolUtils.sha3(rawData);
    }


    /**
     * Check if the given CredentialPojo is selectively disclosed, or not.
     *
     * @param saltMap the saltMap
     * @return true if yes, false otherwise
     */
    public static boolean isSelectivelyDisclosed(Map<String, Object> saltMap) {
        if (saltMap == null) {
            return false;
        }
        for (Map.Entry<String, Object> entry : saltMap.entrySet()) {
            Object v = entry.getValue();
            if (v instanceof Map) {
                if (isSelectivelyDisclosed((HashMap) v)) {
                    return true;
                }
            } else if (v instanceof List) {
                if (isSelectivelyDisclosed((ArrayList<Object>) v)) {
                    return true;
                }
            }
            if (v == null) {
                throw new RuntimeException(ErrorCode.FAIL.getCodeDesc());
            }
            if ("0".equals(v.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the given CredentialPojo is selectively disclosed, or not.
     *
     * @param saltList the saltList
     * @return true if yes, false otherwise
     */
    public static boolean isSelectivelyDisclosed(List<Object> saltList) {
        if (saltList == null) {
            return false;
        }
        for (Object saltObj : saltList) {
            if (saltObj instanceof Map) {
                if (isSelectivelyDisclosed((HashMap) saltObj)) {
                    return true;
                }
            } else if (saltObj instanceof List) {
                if (isSelectivelyDisclosed((ArrayList<Object>) saltObj)) {
                    return true;
                }
            }
            if (saltObj == null) {
                throw new RuntimeException(ErrorCode.FAIL.getCodeDesc());
            }
            if ("0".equals(saltObj.toString())) {
                return true;
            }
        }
        return false;
    }


    /**
     * Get the claim hash. This is irrelevant to selective disclosure.
     *
     * @param credential Credential
     * @param salt Salt Map
     * @param disclosures Disclosure Map
     * @return the unique claim hash value
     */
    public static String getClaimHash(
        CredentialPojo credential,
        Map<String, Object> salt,
        Map<String, Object> disclosures
    ) {

        Map<String, Object> claim = credential.getClaim();
        Map<String, Object> newClaim = DataToolUtils.clone((HashMap) claim);
        addSaltAndGetHash(newClaim, salt, disclosures);
        try {
            String jsonData = DataToolUtils.mapToCompactJson(newClaim);
            return jsonData;
        } catch (Exception e) {
            logger.error("[getClaimHash] get claim hash failed. {}", e);
        }
        return StrUtil.EMPTY;
    }

    private static void addSaltAndGetHash(
        Map<String, Object> claim,
        Map<String, Object> salt,
        Map<String, Object> disclosures
    ) {
        for (Map.Entry<String, Object> entry : salt.entrySet()) {
            String key = entry.getKey();
            Object disclosureObj = null;
            if (disclosures != null) {
                disclosureObj = disclosures.get(key);
            }
            Object saltObj = salt.get(key);
            Object newClaimObj = claim.get(key);

            if (saltObj instanceof Map) {
                addSaltAndGetHash(
                    (HashMap) newClaimObj,
                    (HashMap) saltObj,
                    (HashMap) disclosureObj
                );
            } else if (saltObj instanceof List) {
                ArrayList<Object> disclosureObjList = null;
                if (disclosureObj != null) {
                    disclosureObjList = (ArrayList<Object>) disclosureObj;
                }
                addSaltAndGetHashForList(
                    (ArrayList<Object>) newClaimObj,
                    (ArrayList<Object>) saltObj,
                    disclosureObjList
                );
            } else {
                addSaltByDisclose(claim, key, disclosureObj, saltObj, newClaimObj);
            }
        }
    }

    private static void addSaltByDisclose(
        Map<String, Object> claim,
        String key,
        Object disclosureObj,
        Object saltObj,
        Object newClaimObj
    ) {
        if (disclosureObj == null) {
            if (!NOT_DISCLOSED.toString().equals(saltObj.toString())) {
                claim.put(
                    key,
                    getFieldSaltHash(String.valueOf(newClaimObj), String.valueOf(saltObj))
                );
            }
        } else if (NOT_DISCLOSED.toString().equals(disclosureObj.toString())) {
            claim.put(
                key,
                getFieldSaltHash(String.valueOf(newClaimObj), String.valueOf(saltObj))
            );
        }
    }

    private static void addSaltAndGetHashForList(
        List<Object> claim,
        List<Object> salt,
        List<Object> disclosures
    ) {
        for (int i = 0; claim != null && i < claim.size(); i++) {
            Object obj = claim.get(i);
            Object saltObj = salt.get(i);
            if (obj instanceof Map) {
                Object disclosureObj = null;
                if (disclosures != null) {
                    disclosureObj = disclosures.get(0);
                }
                addSaltAndGetHash((HashMap) obj, (HashMap) saltObj, (HashMap) disclosureObj);
            } else if (obj instanceof List) {
                ArrayList<Object> disclosureObjList = null;
                if (disclosures != null) {
                    Object disclosureObj = disclosures.get(i);
                    if (disclosureObj != null) {
                        disclosureObjList = (ArrayList<Object>) disclosureObj;
                    }
                }
                addSaltAndGetHashForList(
                    (ArrayList<Object>) obj,
                    (ArrayList<Object>) saltObj,
                    disclosureObjList
                );
            }
        }
    }

    /**
     * Set all the values in a map to be null while preserving its key structure recursively.
     *
     * @param map the map
     */
    public static void clearMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object mapObj = map.get(key);
            if (mapObj instanceof Map) {
                clearMap((HashMap<String, Object>) mapObj);
            } else if (mapObj instanceof List) {
                clearMapList((ArrayList<Object>) mapObj);
            } else {
                map.put(key, StrUtil.EMPTY);
            }
        }
    }

    private static void clearMapList(ArrayList<Object> listObj) {
        for (int i = 0; listObj != null && i < listObj.size(); i++) {
            Object obj = listObj.get(i);
            if (obj instanceof Map) {
                clearMap((HashMap<String, Object>) obj);
            } else if (obj instanceof List) {
                clearMapList((ArrayList<Object>) obj);
            } else {
                listObj.set(i, StrUtil.EMPTY);
            }
        }
    }



    /**
     * Get per-field salted hash value.
     *
     * @param field the field value
     * @param salt the salt value
     * @return the hash value
     */
    public static String getFieldSaltHash(String field, String salt) {
        return DataToolUtils.sha3(String.valueOf(field) + String.valueOf(salt));
    }



    private static void getDisclosureClaimData(
        Map<String, Object> saltMap,
        Map<String, Object> claim
    ) {
        for (Map.Entry<String, Object> entry : saltMap.entrySet()) {
            String saltKey = entry.getKey();
            Object saltV = entry.getValue();
            Object claimV = claim.get(saltKey);
            if (saltV instanceof Map) {
                getDisclosureClaimData((HashMap) saltV, (HashMap) claimV);
            } else if (saltV instanceof List) {
                getDisclosureClaimDataForList(
                    (ArrayList<Object>) saltV,
                    (ArrayList<Object>) claimV
                );
            } else {
                removeNotDisclosureData(claim, saltKey, saltV);
            }
        }
    }


    private static void removeNotDisclosureData(
        Map<String, Object> claim,
        String saltKey,
        Object saltV
    ) {
        if (!StrUtil.isBlank(saltV.toString())
            && (String.valueOf(saltV)).equals(NOT_DISCLOSED.toString())) {
            claim.remove(saltKey);
        }
    }

    private static void getDisclosureClaimDataForList(List<Object> salt, List<Object> claim) {
        for (int i = 0; claim != null && i < salt.size(); i++) {
            Object saltObj = salt.get(i);
            Object claimObj = claim.get(i);
            if (saltObj instanceof Map) {
                getDisclosureClaimData((HashMap) saltObj, (HashMap) claimObj);
            } else if (saltObj instanceof List) {
                getDisclosureClaimDataForList(
                    (ArrayList<Object>) saltObj,
                    (ArrayList<Object>) claimObj
                );
            }
        }
    }

    /**
     * valid claim and salt.
     *
     * @param claim claimMap
     * @param salt saltMap
     * @return boolean
     */
    public static boolean validClaimAndSaltForMap(
        Map<String, Object> claim,
        Map<String, Object> salt) {
        //检查是否为空
        if (claim == null || salt == null) {
            return false;
        }
        //检查每个map里的key个数是否相同
        Set<String> claimKeys = claim.keySet();
        Set<String> saltKeys = salt.keySet();
        if (claimKeys.size() != saltKeys.size()) {
            return false;
        }
        //检查key值是否一致
        for (Map.Entry<String, Object> entry : claim.entrySet()) {
            String k = entry.getKey();
            Object claimV = entry.getValue();
            Object saltV = salt.get(k);
            if (!salt.containsKey(k)) {
                return false;
            }
            if (claimV instanceof Map) {
                //递归检查
                if (!validClaimAndSaltForMap((HashMap) claimV, (HashMap) saltV)) {
                    return false;
                }
            } else if (claimV instanceof List) {
                ArrayList<Object> claimValue = (ArrayList<Object>) claimV;
                if (saltV instanceof ArrayList) {
                    ArrayList<Object> saltValue = (ArrayList<Object>) saltV;
                    if (!validClaimAndSaltForList(claimValue, saltValue)) {
                        return false;
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    private static boolean validClaimAndSaltForList(
        List<Object> claimList,
        List<Object> saltList) {
        //检查是否为空
        if (claimList == null || saltList == null) {
            return false;
        }
        for (int i = 0; i < claimList.size(); i++) {
            Object claimObj = claimList.get(i);
            Object saltObj = saltList.get(i);
            if (claimObj instanceof Map) {
                if (!(saltObj instanceof Map)) {
                    return false;
                }
                if (!validClaimAndSaltForMap((HashMap) claimObj, (HashMap) saltObj)) {
                    return false;
                }
            } else if (claimObj instanceof List) {
                if (!(saltObj instanceof List)) {
                    return false;
                }
                ArrayList<Object> claimObjV = (ArrayList<Object>) claimObj;
                ArrayList<Object> saltObjV = (ArrayList<Object>) saltObj;
                if (!validClaimAndSaltForList(claimObjV, saltObjV)) {
                    return false;
                }
            }
        }
        return true;
    }



    private static ErrorCode validDateExpired(Long issuanceDate, Long expirationDate) {
        if (issuanceDate != null && issuanceDate <= 0) {
            return ErrorCode.FAIL;
        }
        if (expirationDate == null
            || expirationDate.longValue() < 0
            || expirationDate.longValue() == 0) {
            return ErrorCode.FAIL;
        }
        if (!DateUtils.isAfterCurrentTime(expirationDate)) {
            return ErrorCode.FAIL;
        }
        if (issuanceDate != null && expirationDate < issuanceDate) {
            return ErrorCode.FAIL;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Check the given CredentialPojo validity based on its input params.
     *
     * @param args CredentialPojo
     * @return true if yes, false otherwise
     */
    public static ErrorCode isCredentialPojoValid(CredentialPojo args) {
        if (args == null) {
            return ErrorCode.FAIL;
        }
        if (args.getCptId() == null || args.getCptId().intValue() < 0) {
            return ErrorCode.FAIL;
        }
        if (!GIdUtils.isGIdValid(args.getIssuer())) {
            return ErrorCode.FAIL;
        }
        if (args.getClaim() == null) {
            return ErrorCode.FAIL;
        }
        if (args.getIssuanceDate() == null) {
            return ErrorCode.FAIL;
        }
        ErrorCode errorCode = validDateExpired(args.getIssuanceDate(), args.getExpirationDate());
        if (errorCode.getCode() != ErrorCode.SUCCESS.getCode()) {
            return errorCode;
        }
        ErrorCode contentResponseData = isCredentialContentValid(args);
        if (ErrorCode.SUCCESS.getCode() != contentResponseData.getCode()) {
            return contentResponseData;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Check the given CredentialPojo content fields validity excluding metadata, based on its
     * input.
     *
     * @param args CredentialPojo
     * @return true if yes, false otherwise
     */
    private static ErrorCode isCredentialContentValid(CredentialPojo args) {
        String credentialId = args.getId();
        if (StrUtil.isEmpty(credentialId) || !CredentialUtils.isValidUuid(credentialId)) {
            return ErrorCode.FAIL;
        }
        String context = args.getContext();
        if (StrUtil.isEmpty(context)) {
            return ErrorCode.FAIL;
        }
        if ((args.getType().isEmpty())) {
            return ErrorCode.FAIL;
        }
        Map<String, Object> proof = args.getProof();
        return isCredentialProofValid(proof);
    }

    private static ErrorCode isCredentialProofValid(Map<String, Object> proof) {
        if (proof == null) {
            return ErrorCode.FAIL;
        }

        String type = null;
        if (proof.get(ParamKeyConstant.PROOF_TYPE) == null) {
            return ErrorCode.FAIL;
        } else {
            type = String.valueOf(proof.get(ParamKeyConstant.PROOF_TYPE));
            if (!isCredentialProofTypeValid(type)) {
                return ErrorCode.FAIL;
            }
        }
        // Created is not obligatory
        if (proof.get(ParamKeyConstant.PROOF_CREATED) == null) {
            return ErrorCode.FAIL;
        } else {
            Long created = Long.valueOf(String.valueOf(proof.get(ParamKeyConstant.PROOF_CREATED)));
            if (created.longValue() <= 0) {
                return ErrorCode.FAIL;
            }
        }
        // Creator is not obligatory either
        if (proof.get(ParamKeyConstant.PROOF_CREATOR) == null) {
            return ErrorCode.FAIL;
        } else {
            String creator = String.valueOf(proof.get(ParamKeyConstant.PROOF_CREATOR));
            if (StrUtil.isEmpty(creator)) {
                return ErrorCode.FAIL;
            }
        }
        // If the Proof type is ECDSA or other signature based scheme, check signature
        if (type.equalsIgnoreCase(CredentialConstant.CredentialProofType.ECDSA.getTypeName())) {
            if (proof.get(ParamKeyConstant.PROOF_SIGNATURE) == null) {
                return ErrorCode.FAIL;
            } else {
                String signature = String.valueOf(proof.get(ParamKeyConstant.PROOF_SIGNATURE));
                if (StrUtil.isEmpty(signature)
                    || !DataToolUtils.isValidBase64String(signature)) {
                    return ErrorCode.FAIL;
                }
            }
        }
        return ErrorCode.SUCCESS;
    }

    private static boolean isCredentialProofTypeValid(String type) {
        // Proof type must be one of the pre-defined types.
        if (!StrUtil.isEmpty(type)) {
            for (CredentialConstant.CredentialProofType proofType : CredentialConstant.CredentialProofType.values()) {
                if (StrUtil.equalsIgnoreCase(type, proofType.getTypeName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
