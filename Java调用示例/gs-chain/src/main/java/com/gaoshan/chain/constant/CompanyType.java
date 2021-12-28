package com.gaoshan.chain.constant;

/**
 * @author: lijie
 * @date: 2021-10-14 10:34:10
 */

public enum CompanyType implements BaseEnum {
    //0企业;1个体工商户;2政府/事业单位;3其他组织',
    ENTERPRISE(0, "enterprise"),
    INDIVIDUAL(1, "individual"),
    GOVERNMENT(2, "government"),
    OTHER(3, "other");
    private Integer code;
    private String message;

     CompanyType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public String getMessage() {
        return message;
    }
    /**
     * 根据code获取当前的枚举对象
     * @param code
     * @return GenderColumn
     */
    public static CompanyType of(Integer code) {
        if (code == null) {
            return null;
        }
        for (CompanyType status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}