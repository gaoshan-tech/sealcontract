package com.gaoshan.chain.util;

import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;
import java.math.BigInteger;

/**
 * The gaoshan DID Utils.
 *
 */
public final class GIdUtils {

    /**
     * log4j object, for recording log.
     */
    private static final Logger logger = LoggerFactory.getLogger(GIdUtils.class);
    private static final String GID_PREFIX = "did:gid:";
    private static final String ADDRESS_PREFIX = "0x";

    /**
     * Convert a gaoShan DID to a  account address.
     *
     * @param gId the gaoShan DID
     * @return gId related address, empty if input gaoShan DID is illegal
     */
    public static String convertGIdToAddress(String gId) {
        if (!StrUtil.startWith(gId, GID_PREFIX)) {
            return StrUtil.EMPTY;
        }
        String addr = StrUtil.removePrefix(gId, GID_PREFIX);
        return StrUtil.addPrefixIfNot(addr,ADDRESS_PREFIX);
    }

    /**
     * Convert an account address to gaoshan DID.
     * 
     * @param address the address
     * @return a related gaoshan DID, or empty string if the input is illegal.
     */
    public static String convertAddressToGId(String address) {
        if (StrUtil.isEmpty(address)) {
            return StrUtil.EMPTY;
        }
        if (StrUtil.contains(address, ADDRESS_PREFIX)) {
            address=StrUtil.removePrefix(address,ADDRESS_PREFIX);
        }
        return buildGIdByAddress(address);
    }

    /**
     * Check if the input gaoshan DID is legal or not.
     *
     * @param gId the gaoshan DID
     * @return true if the gaoshan DID is legal, false otherwise.
     */
    public static boolean isGIdValid(String gId) {
        return StrUtil.isNotEmpty(gId)
            && StrUtil.startWith(gId,GID_PREFIX)
            && isValidAddress(convertGIdToAddress(gId));
    }

    /**
     * Convert a public key to a gaoshan DID.
     *
     * @param publicKey the public key
     * @return gaoshan DID
     */
    public static String convertPublicKeyToGId(String publicKey) {
        try {
            String address = Keys.getAddress(new BigInteger(publicKey));
            return buildGIdByAddress(address);
        } catch (Exception e) {
            logger.error("convert publicKey to gId error.", e);
            return StrUtil.EMPTY;
        }
    }
    
    private static String buildGIdByAddress(String address) {
        StringBuffer gId = new StringBuffer();
        gId.append(GID_PREFIX);
        if (StrUtil.contains(address, ADDRESS_PREFIX)) {
            address=StrUtil.removePrefix(address,ADDRESS_PREFIX);
        }
        gId.append(address);
        return gId.toString();
    }



    public static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == 40;
    }

    

}
