
package com.gaoshan.chain.constant;


public final class CredentialConstant {

    /**
     * The Constant default Credential Context.
     */
    public static final String DEFAULT_CREDENTIAL_CONTEXT =
        "https://gaoshan.co/gsidentity/credentials/v1";


    /**
     * The Credential Proof Type Enumerate.
     */
    public static enum CredentialProofType {
        ECDSA("Secp256k1"),
        ECDSA_R("Secp256r1");

        /**
         * The Type Name of the Credential Proof.
         */
        private String typeName;

        /**
         * Constructor.
         */
        CredentialProofType(String typeName) {
            this.typeName = typeName;
        }

        /**
         * Getter.
         *
         * @return typeName
         */
        public String getTypeName() {
            return typeName;
        }
    }
}
