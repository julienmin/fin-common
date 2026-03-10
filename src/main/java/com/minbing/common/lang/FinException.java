package com.minbing.common.lang;

import com.minbing.common.assertion.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

/**
 * 通用异常
 */
public class FinException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;

    private FinException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    private FinException(Throwable cause, ErrorCode errorCode, String message) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public static FinException build(String messageFormat, ErrorCode errorCode, Object... paramms) {
        return build(null, messageFormat, errorCode, paramms);
    }

    public static FinException build(Throwable cause, String messageFormat, ErrorCode errorCode, Object... paramms) {
        AssertUtil.notNull(errorCode);

        if (cause instanceof FinException) {
            return (FinException) cause;
        }

        String errorMessage = errorCode.getMessage();
        if (StringUtils.isNotBlank(messageFormat)) {
            errorMessage = paramms == null ? messageFormat : MessageFormatter.arrayFormat(messageFormat, paramms).getMessage();
        }

        if (cause == null) {
            return new FinException(errorCode, errorMessage);
        } else {
            return new FinException(cause, errorCode, errorMessage);
        }
    }

}
