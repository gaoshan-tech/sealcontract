package com.gaoshan.common.api;

import java.io.Serializable;

/**
 * 封装API的错误码
 * Created by macro on 2019/4/19.
 */
public interface IErrorCode extends Serializable {
    long getCode();

    String getMessage();
}
