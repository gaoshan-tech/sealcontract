

package com.gaoshan.chain.constant;

import java.math.BigInteger;


public final class GIdConstant {



    /**
     * The Constant Max authority issuer name length in Chars.
     */
    public static final Integer MAX_AUTHORITY_ISSUER_NAME_LENGTH = 32;

    /**
     * UUID Pattern.
     */
    public static final String UUID_PATTERN =
        "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";


    /**
     * The hash value pattern.
     */
    public static final String HASH_VALUE_PATTERN = "0x[a-fA-f0-9]{64}";


}
