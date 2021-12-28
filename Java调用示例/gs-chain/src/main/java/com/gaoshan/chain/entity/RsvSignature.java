

package com.gaoshan.chain.entity;

import lombok.Data;
import org.storm3j.abi.datatypes.generated.Bytes32;
import org.storm3j.abi.datatypes.generated.Uint8;


@Data
public class RsvSignature {

    /**
     * The v value.
     */
    private Uint8 v;

    /**
     * The r value.
     */
    private Bytes32 r;

    /**
     * The s value.
     */
    private Bytes32 s;
}
