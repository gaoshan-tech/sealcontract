

package com.gaoshan.chain.util;

import cn.hutool.core.util.StrUtil;
import com.gaoshan.chain.constant.*;
import com.gaoshan.chain.entity.*;
import org.apache.commons.beanutils.BeanUtils;
import org.storm3j.crypto.ECKeyPair;
import org.storm3j.crypto.Sign;
import org.storm3j.utils.Numeric;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;


public final class CredentialUtils {

    /**
     *
     * @param credential  target Credential object
     * @param disclosures disclosures of the credential
     * @return Hash value in String.
     */
    public static String getCredentialThumbprintWithoutSig(
            Credential credential,
            Map<String, Object> disclosures) {
        try {
            Credential rawCredential = copyCredential(credential);
            rawCredential.setProof(null);
            return getCredentialThumbprint(rawCredential, disclosures);
        } catch (Exception e) {
            return StrUtil.EMPTY;
        }
    }

    public static String getContractThumbprintWithoutSig(
            ContractEntity contractEntity) {
        try {
            ContractEntity rawContract = new ContractEntity();
            BeanUtils.copyProperties(rawContract, contractEntity);
            rawContract.setProof(null);
            return getContractThumbprint(rawContract);
        } catch (Exception e) {
            return StrUtil.EMPTY;
        }
    }

    /**
     * Build the credential Proof.
     *
     * @param credential    the credential
     * @param privateKey    the privatekey
     * @param disclosureMap the disclosureMap
     * @return the Proof structure
     */
    public static Map<String, String> buildCredentialProof(
            Credential credential,
            String privateKey,
            Map<String, Object> disclosureMap) {
        Map<String, String> proof = new HashMap<>();
        proof.put(ParamKeyConstant.PROOF_CREATED, credential.getIssuanceDate().toString());
        proof.put(ParamKeyConstant.PROOF_CREATOR, credential.getIssuer());
        proof.put(ParamKeyConstant.PROOF_TYPE, getDefaultCredentialProofType());
        proof.put(ParamKeyConstant.CREDENTIAL_SIGNATURE,
                getCredentialSignature(credential, privateKey, disclosureMap));
        return proof;
    }

    public static Map<String, String> buildContractProof(
            ContractEntity entity,
            String privateKey) {
        Map<String, String> proof = new HashMap<>();
        proof.put(ParamKeyConstant.PROOF_CREATED, entity.getSignDate().toString());
        ECKeyPair keyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        String address = org.web3j.crypto.Keys.getAddress(keyPair.getPublicKey());
        proof.put(ParamKeyConstant.PROOF_CREATOR, GIdUtils.convertAddressToGId(address));
        proof.put(ParamKeyConstant.PROOF_TYPE, getDefaultCredentialProofType());
        proof.put(ParamKeyConstant.CREDENTIAL_SIGNATURE,
                getContractSignature(entity, privateKey));
        return proof;
    }

    /**
     * A clean deep copy method of a Credential which pays special attention on Map object. todo:
     * preserve the claim key order
     *
     * @param credential target Credential object
     * @return new credential
     */
    public static Credential copyCredential(Credential credential) {
        Credential ct = new Credential();
        ct.setContext(credential.getContext());

        Map<String, String> originalProof = credential.getProof();
        if (originalProof != null) {
            Map<String, String> proof = DataToolUtils
                    .deserialize(DataToolUtils.serialize(originalProof), HashMap.class);
            ct.setProof(proof);
        }
        Map<String, Object> originalClaim = credential.getClaim();
        if (originalClaim != null) {
            Map<String, Object> claim = DataToolUtils
                    .deserialize(DataToolUtils.serialize(originalClaim), HashMap.class);
            ct.setClaim(claim);
        }

        ct.setIssuanceDate(credential.getIssuanceDate());
//        ct.setCptId(credential.getCptId());
        ct.setExpirationDate(credential.getExpirationDate());
        ct.setIssuer(credential.getIssuer());
        ct.setId(credential.getId());
        return ct;
    }

    /**
     *
     * @param credential  target Credential object
     * @param disclosures the disclosure map
     * @return Hash value in String.
     */
    public static String getCredentialThumbprint(
            Credential credential,
            Map<String, Object> disclosures) {
        try {
            Map<String, Object> credMap = DataToolUtils.objToMap(credential);
            String claimHash = getClaimHash(credential, disclosures);
            credMap.put(ParamKeyConstant.CLAIM, claimHash);
            return DataToolUtils.mapToCompactJson(credMap);
        } catch (Exception e) {
            return StrUtil.EMPTY;
        }
    }

    public static String getContractThumbprint(
            ContractEntity contract) {
        try {
            Map<String, Object> credMap = DataToolUtils.objToMap(contract);
            return DataToolUtils.mapToCompactJson(credMap);
//            return DataToolUtils.objToJsonStrWithNoPretty(contract);
        } catch (Exception e) {
            return StrUtil.EMPTY;
        }
    }

    /**
     * Get the claim hash. This is irrelevant to selective disclosure.
     *
     * @param credential  Credential
     * @param disclosures Disclosure Map
     * @return the unique claim hash value
     */
    public static String getClaimHash(Credential credential, Map<String, Object> disclosures) {

        Map<String, Object> claim = credential.getClaim();
        Map<String, Object> claimHashMap = new HashMap<>(claim);
        Map<String, Object> disclosureMap;

        if (disclosures == null) {
            disclosureMap = new HashMap<>(claim);
            for (Entry<String, Object> entry : disclosureMap.entrySet()) {
                disclosureMap.put(
                        entry.getKey(),
                        CredentialFieldDisclosureValue.DISCLOSED.getStatus()
                );
            }
        } else {
            disclosureMap = disclosures;
        }

        for (Entry<String, Object> entry : disclosureMap.entrySet()) {
            claimHashMap.put(entry.getKey(), getFieldHash(claimHashMap.get(entry.getKey())));
        }

        List<Entry<String, Object>> list = new ArrayList<Entry<String, Object>>(
                claimHashMap.entrySet()
        );
        Collections.sort(list, new Comparator<Entry<String, Object>>() {

            @Override
            public int compare(Entry<String, Object> o1, Entry<String, Object> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        StringBuffer hash = new StringBuffer();
        for (Entry<String, Object> en : list) {
            hash.append(en.getKey()).append(en.getValue());
        }
        return hash.toString();
    }

    /**
     * convert a field to hash.
     *
     * @param field which will be converted to hash.
     * @return hash value.
     */
    public static String getFieldHash(Object field) {
        return DataToolUtils.sha3(String.valueOf(field));
    }

    /**
     * Get default Credential Context String.
     *
     * @return Context value in String.
     */
    public static String getDefaultCredentialContext() {
        return CredentialConstant.DEFAULT_CREDENTIAL_CONTEXT;
    }

    /**
     * Extract GenerateCredentialArgs from Credential.
     *
     * @param arg the arg
     * @return GenerateCredentialArgs
     */
    public static CreateCredentialArgs extractCredentialMetadata(Credential arg) {
        if (arg == null) {
            return null;
        }
        CreateCredentialArgs generateCredentialArgs = new CreateCredentialArgs();
        generateCredentialArgs.setIssuer(arg.getIssuer());
        generateCredentialArgs.setIssuanceDate(arg.getIssuanceDate());
        generateCredentialArgs.setExpirationDate(arg.getExpirationDate());
        generateCredentialArgs.setClaim(arg.getClaim());
        return generateCredentialArgs;
    }

    /**

     *
     * @param credential    target credential object
     * @param privateKey    the privatekey for signing
     * @param disclosureMap the disclosure map
     * @return the String signature value
     */
    public static String getCredentialSignature(Credential credential, String privateKey,
                                                Map<String, Object> disclosureMap) {
        String rawData = CredentialUtils
                .getCredentialThumbprintWithoutSig(credential, disclosureMap);
        Sign.SignatureData sigData = DataToolUtils.signMessage(rawData, privateKey);
        return new String(
                DataToolUtils.base64Encode(DataToolUtils.simpleSignatureSerialization(sigData)),
                StandardCharsets.UTF_8);
    }

    public static String getContractSignature(ContractEntity entity, String privateKey) {
        String rawDataHash = entity.getHashWithoutSig();
        System.out.println("rawDataHash= " + rawDataHash);
        Sign.SignatureData sigData = DataToolUtils.signPrefixedMessage(rawDataHash, privateKey);
        String base64EncodeStr = new String(
                DataToolUtils.base64Encode(DataToolUtils.simpleSignatureSerialization(sigData)),
                StandardCharsets.UTF_8);
        System.out.println("base64EncodeStr= " + base64EncodeStr);
        return base64EncodeStr;
    }

    /**
     *
     * @param arg the args
     * @return Hash in byte array
     */
    public static String getCredentialHash(Credential arg) {
        String rawData = getCredentialThumbprint(arg, null);
        if (StrUtil.isEmpty(rawData)) {
            return StrUtil.EMPTY;
        }
        return DataToolUtils.sha3(rawData);
    }

    public static String getContractHash(ContractEntity entity) {
        String rawData = getContractThumbprintWithoutSig(entity);
        System.out.println("rawData: " + rawData);
        if (StrUtil.isEmpty(rawData)) {
            return StrUtil.EMPTY;
        }
        return DataToolUtils.sha3(rawData);
    }

    /**
     * Create a full CredentialWrapper Hash for a Credential based on all its fields, which is
     * resistant to selective disclosure.
     *
     * @param arg the args
     * @return Hash in byte array
     */
    public static String getCredentialWrapperHash(CredentialWrapper arg) {
        String rawData = getCredentialThumbprint(arg.getCredential(), arg.getDisclosure());
        if (StrUtil.isEmpty(rawData)) {
            return StrUtil.EMPTY;
        }
        return DataToolUtils.sha3(rawData);
    }



    /**
     * Check whether the given String is a valid UUID.
     *
     * @param id the Credential id
     * @return true if yes, false otherwise
     */
    public static boolean isValidUuid(String id) {
        Pattern p = Pattern.compile(GIdConstant.UUID_PATTERN);
        return p.matcher(id).matches();
    }

    /**
     * Check the given CreateCredentialArgs validity based on its input params.
     *
     * @param args CreateCredentialArgs
     * @return true if yes, false otherwise
     */
    public static ErrorCode isCreateCredentialArgsValid(
            CreateCredentialArgs args) {
        if (args == null) {
            return ErrorCode.FAIL;
        }
//        if (args.getCptId() == null || args.getCptId().intValue() < 0) {
//            return ErrorCode.CPT_ID_ILLEGAL;
//        }
        if (!GIdUtils.isGIdValid(args.getIssuer())) {
            return ErrorCode.FAIL;
        }
        Long issuanceDate = args.getIssuanceDate();
        if (issuanceDate != null && issuanceDate <= 0) {
            return ErrorCode.FAIL;
        }
        Long expirationDate = args.getExpirationDate();
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
        if (args.getClaim() == null || args.getClaim().isEmpty()) {
            return ErrorCode.FAIL;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Check the given Credential validity based on its input params.
     *
     * @param args Credential
     * @return true if yes, false otherwise
     */
    public static ErrorCode isCredentialValid(Credential args) {
        if (args == null) {
            return ErrorCode.FAIL;
        }
        CreateCredentialArgs createCredentialArgs = extractCredentialMetadata(args);
        ErrorCode metadataResponseData = isCreateCredentialArgsValid(createCredentialArgs);
        if (ErrorCode.SUCCESS.getCode() != metadataResponseData.getCode()) {
            return metadataResponseData;
        }
        ErrorCode contentResponseData = isCredentialContentValid(args);
        if (ErrorCode.SUCCESS.getCode() != contentResponseData.getCode()) {
            return contentResponseData;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Check the given Credential validity based on its input params.
     *
     * @param args Credential
     * @return true if yes, false otherwise
     */
    public static ErrorCode isContractValid(ContractEntity args) {
        try {
            if (args == null) {
                return ErrorCode.FAIL;
            }
            if (isCredentialValid(args.getSignerVC()).getCode() != ErrorCode.SUCCESS.getCode()) {
                return ErrorCode.FAIL;
            }
            if (args.getType() == SignatureType.PERSON.ordinal()) {
                if (!StrUtil.equalsIgnoreCase(args.getProofCreator(), args.getSignerVC().getClaim().get(ParamKeyConstant.COMPANY_DID).toString())) {
                    return ErrorCode.FAIL;
                }
            }
            Long issuanceDate = args.getSignDate();
            if (issuanceDate != null && issuanceDate <= 0) {
                return ErrorCode.FAIL;
            }
            if (args.getSealsClaim() == null || args.getSealsClaim().isEmpty()) {
                return ErrorCode.FAIL;
            }
            String id = args.getId();
            if (StrUtil.isEmpty(id)) {
                return ErrorCode.FAIL;
            }
            String context = args.getContext();
            if (StrUtil.isEmpty(context)) {
                return ErrorCode.FAIL;
            }
            Map<String, String> proof = args.getProof();
            String type = proof.get(ParamKeyConstant.PROOF_TYPE);
            if (!isCredentialProofTypeValid(type)) {
                return ErrorCode.FAIL;
            }
            // Created is not obligatory
            Long created = Long.valueOf(proof.get(ParamKeyConstant.PROOF_CREATED));
            if (created.longValue() <= 0) {
                return ErrorCode.FAIL;
            }
            // Creator is not obligatory either
            String creator = proof.get(ParamKeyConstant.PROOF_CREATOR);
            if (!StrUtil.isEmpty(creator) && !GIdUtils.isGIdValid(creator)) {
                return ErrorCode.FAIL;
            }
            // If the Proof type is ECDSA or other signature based scheme, check signature
            if (type.equalsIgnoreCase(CredentialConstant.CredentialProofType.ECDSA.getTypeName())) {
                String signature = proof.get(ParamKeyConstant.CREDENTIAL_SIGNATURE);
                if (StrUtil.isEmpty(signature) || !DataToolUtils.isValidBase64String(signature)) {
                    return ErrorCode.FAIL;
                }
            }
        } catch (Exception e) {
            return ErrorCode.FAIL;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Check the given Credential content fields validity excluding metadata, based on its input.
     *
     * @param args Credential
     * @return true if yes, false otherwise
     */
    public static ErrorCode isCredentialContentValid(Credential args) {
        String credentialId = args.getId();
        if (StrUtil.isEmpty(credentialId) || !CredentialUtils.isValidUuid(credentialId)) {
            return ErrorCode.FAIL;
        }
        String context = args.getContext();
        if (StrUtil.isEmpty(context)) {
            return ErrorCode.FAIL;
        }
        Long issuanceDate = args.getIssuanceDate();
        if (issuanceDate == null) {
            return ErrorCode.FAIL;
        }
        if (issuanceDate.longValue() > args.getExpirationDate().longValue()) {
            return ErrorCode.FAIL;
        }
        Map<String, String> proof = args.getProof();
        return isCredentialProofValid(proof);
    }

    private static ErrorCode isCredentialProofValid(Map<String, String> proof) {
        if (proof == null) {
            return ErrorCode.FAIL;
        }
        String type = proof.get(ParamKeyConstant.PROOF_TYPE);
        if (!isCredentialProofTypeValid(type)) {
            return ErrorCode.FAIL;
        }
        // Created is not obligatory
        Long created = Long.valueOf(proof.get(ParamKeyConstant.PROOF_CREATED));
        if (created.longValue() <= 0) {
            return ErrorCode.FAIL;
        }
        // Creator is not obligatory either
        String creator = proof.get(ParamKeyConstant.PROOF_CREATOR);
        if (!StrUtil.isEmpty(creator) && !GIdUtils.isGIdValid(creator)) {
            return ErrorCode.FAIL;
        }
        // If the Proof type is ECDSA or other signature based scheme, check signature
        if (type.equalsIgnoreCase(CredentialConstant.CredentialProofType.ECDSA.getTypeName())) {
            String signature = proof.get(ParamKeyConstant.CREDENTIAL_SIGNATURE);
            if (StrUtil.isEmpty(signature) || !DataToolUtils.isValidBase64String(signature)) {
                return ErrorCode.FAIL;
            }
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * Get default Credential Credential Proof Type String.
     *
     * @return Context value in String.
     */
    public static String getDefaultCredentialProofType() {
        return CredentialConstant.CredentialProofType.ECDSA.getTypeName();
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
