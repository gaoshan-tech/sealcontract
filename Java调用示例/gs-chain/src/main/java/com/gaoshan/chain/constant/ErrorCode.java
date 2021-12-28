

package com.gaoshan.chain.constant;

/**
 * Define Error Code and the corresponding Error Message.
 *
 * @author
 */
public enum ErrorCode {

    /**
     * The success.
     */
    SUCCESS(0, "success"),

    /**
     * The success.
     */
    FAIL(-1, "fail"),
    /**
     * The success.
     */
    UNKNOW_ERROR(-2, "unknow_error");
    /**
     * No Permission to perform contract level tasks.
     */

    /**
     * error code.
     */
    private int code;

    /**
     * error message.
     */
    private String codeDesc;

    /**
     * Error Code Constructor.
     *
     * @param code The ErrorCode
     * @param codeDesc The ErrorCode Description
     */
    ErrorCode(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }
    /**
     * Get the Error Code.
     *
     * @return the ErrorCode
     */
    public int getCode() {
        return code;
    }

    /**
     * Set the Error Code.
     *
     * @param code the new ErrorCode
     */
    protected void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets the ErrorCode Description.
     *
     * @return the ErrorCode Description
     */
    public String getCodeDesc() {
        return codeDesc;
    }

    /**
     * Sets the ErrorCode Description.
     *
     * @param codeDesc the new ErrorCode Description
     */
    protected void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }
}
