package com.minbing.common.lang;

public enum CommonErrorCode implements ErrorCode {
    SYSTEM_ERROR("SYSTEM ERROR"),

    PARAMETER_ILLEGAL("PARAMETER ILLEGAL"),

    ENTITY_NOT_EXIST("ENTITY NOT EXIST"),
    ;

    private String message;

    CommonErrorCode(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public ErrorType errorType() {
        return ErrorType.COMMON;
    }

}
