
package com.gaoshan.chain.constant;

import lombok.Data;

/**
 * The internal base response result class.
 *
 */
@Data
public class ResponseData<T> {

    /**
     * The generic type result object.
     */
    private T result;

    /**
     * The error code.
     */
    private Integer errorCode;

    /**
     * The error message.
     */
    private String errorMessage;

    /**
     * The blockchain transaction info. Note that this transaction only becomes valid (not null nor
     * blank) when a successful transaction is sent to chain with a block generated.
     */

    /**
     * Instantiates a new response data.
     */
    public ResponseData() {
        this.setErrorCode(ErrorCode.SUCCESS);
    }

    /**
     * Instantiates a new response data. Transaction info is left null to avoid unnecessary boxing.
     *
     * @param result the result
     * @param errorCode the return code
     */
    public ResponseData(T result, ErrorCode errorCode) {
        this.result = result;
        if (errorCode != null) {
            this.errorCode = errorCode.getCode();
            this.errorMessage = errorCode.getCodeDesc();
        }
    }

    /**
     * Instantiates a new response data with transaction info.
     *
     * @param result the result
     * @param errorCode the return code
     * @param transactionInfo transactionInfo
     */

    /**
     * set a ErrorCode type errorCode.
     * 
     * @param errorCode the errorCode
     */
    public void setErrorCode(ErrorCode errorCode) {
        if (errorCode != null) {
            this.errorCode = errorCode.getCode();
            this.errorMessage = errorCode.getCodeDesc();
        }
    }

    /**
     * Instantiates a new Response data based on the error code and error message.
     * 
     * @param result the result
     * @param errorCode code number
     * @param errorMessage errorMessage
     */
    public ResponseData(T result, Integer errorCode, String errorMessage) {
        this.result = result;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
