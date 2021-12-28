
package com.gaoshan.chain.entity;

import lombok.Data;

import java.util.Map;


@Data
public class CreateCredentialArgs {


    private String issuer;

    /**
     * Required: The expire date.
     */
    private Long expirationDate;

    /**
     * Required: The claim data.
     */
    private Map<String, Object> claim;

    /**
     * Optional: The issuance date of the credential.
     */
    private Long issuanceDate = null;
}
