package com.minbing.common.lang;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FinResult<Module> implements Serializable {
    private static final long serialVersionUID = 1L;

    private FinResultCode resultCode;

    private Module module;

    private String errorCode;

    private String errorMessage;

    private FinResult() {

    }

    /**
     * 成功结果
     * @param module
     * @return
     * @param <Module>
     */
    public static <Module> FinResult<Module> success(Module module) {
        FinResult<Module> result = new FinResult<>();
        result.module = module;
        result.resultCode = FinResultCode.SUCCEEDED;
        return result;
    }

    public static <Module> FinResult<Module> success() {
        return success(null);
    }

    private static <Module> FinResult<Module> unknown(String errorCode, String errorMessage) {
        FinResult<Module> result = new FinResult<>();
        result.resultCode = FinResultCode.UNKNOWN;
        result.errorCode = errorCode;
        result.errorMessage = errorMessage;
        return result;
    }

    public static <Module> FinResult<Module> failed(String errorCode, String errorMessage) {
        FinResult<Module> result = new FinResult<>();
        result.resultCode = FinResultCode.FAILED;
        result.errorCode = errorCode;
        result.errorMessage = errorMessage;
        return result;
    }
}
