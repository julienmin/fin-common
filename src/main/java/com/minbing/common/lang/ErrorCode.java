package com.minbing.common.lang;

/**
 * 错误码定义
 */
public interface ErrorCode {

    String name();

    default String getCode() {
        return name();
    }

    String getMessage();

    ErrorType errorType();

    enum ErrorType {
        /** COMMON */
        COMMON,

        /** 清算 */
        CLEARING
    }
}
