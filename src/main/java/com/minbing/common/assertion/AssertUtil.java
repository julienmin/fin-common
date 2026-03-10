package com.minbing.common.assertion;

import com.minbing.common.lang.CommonErrorCode;
import com.minbing.common.lang.ErrorCode;
import com.minbing.common.lang.FinException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class AssertUtil {

    private static final ErrorCode DEFAULT_ERROR_CODE = CommonErrorCode.PARAMETER_ILLEGAL;

    private AssertUtil() {

    }


    public static void isTrue(boolean expression) {
        if (!expression) {
            throw FinException.build("Parameter is not true.", DEFAULT_ERROR_CODE);
        }
    }

    public static void isTrue(boolean expression, String message, Object... params) {
        if (!expression) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void isFalse(boolean expression) {
        if (expression) {
            throw FinException.build("Parameter is true.", DEFAULT_ERROR_CODE);
        }
    }

    public static void isFalse(boolean expression, String message, Object... params) {
        if (expression) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void notNull(Object obj) {
        if (obj == null) {
            throw FinException.build("Parameter is null.", DEFAULT_ERROR_CODE);
        }
    }

    public static void notNull(Object obj, String message, Object... params) {
        if (obj == null) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void isNull(Object obj) {
        if (obj != null) {
            throw FinException.build("Parameter is not null.", DEFAULT_ERROR_CODE);
        }
    }

    public static void isNull(Object obj, String message, Object... params) {
        if (obj != null) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void isBlank(String text) {
        if (StringUtils.isNotBlank(text)) {
            throw FinException.build("Text is not blank", DEFAULT_ERROR_CODE);
        }
    }

    public static void isBlank(String text, String message, Object... params) {
        if (StringUtils.isNotBlank(text)) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void notBlank(String text) {
        if (StringUtils.isBlank(text)) {
            throw FinException.build("Text is blank", DEFAULT_ERROR_CODE);
        }
    }

    public static void notBlank(String text, String message, Object... params) {
        if (StringUtils.isBlank(text)) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void isEmpty(Object[] array) {
        if (array != null && array.length > 0) {
            throw FinException.build("Array is not empty", DEFAULT_ERROR_CODE);
        }
    }

    public static void isEmpty(Object[] array, String message, Object... params) {
        if (array != null && array.length > 0) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void notEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            throw FinException.build("Array is not empty", DEFAULT_ERROR_CODE);
        }
    }

    public static void notEmpty(Object[] array, String message, Object... params) {
        if (array == null || array.length == 0) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }



    public static void isEmpty(Collection<?> coll) {
        if (!CollectionUtils.isEmpty(coll)) {
            throw FinException.build("Collection is not empty", DEFAULT_ERROR_CODE);
        }
    }

    public static void isEmpty(Collection<?> coll, String message, Object... params) {
        if (!CollectionUtils.isEmpty(coll)) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void notEmpty(Collection<?> coll) {
        if (CollectionUtils.isEmpty(coll)) {
            throw FinException.build("Collection is empty", DEFAULT_ERROR_CODE);
        }
    }

    public static void notEmpty(Collection<?> coll, String message, Object... params) {
        if (CollectionUtils.isEmpty(coll)) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }
    public static void isEmpty(Map<?,?> map) {
        if (map != null && !map.isEmpty()) {
            throw FinException.build("Map is not empty", DEFAULT_ERROR_CODE);
        }
    }

    public static void isEmpty(Map<?,?> map, String message, Object... params) {
        if (map != null && !map.isEmpty()) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }

    public static void notEmpty(Map<?,?> map) {
        if (map == null || map.isEmpty()) {
            throw FinException.build("Collection is empty", DEFAULT_ERROR_CODE);
        }
    }

    public static void notEmpty(Map<?,?> map, String message, Object... params) {
        if (map == null || map.isEmpty()) {
            throw FinException.build(message, DEFAULT_ERROR_CODE, params);
        }
    }


    public static void equals(Object expect, Object actual) {
        if (!Objects.equals(expect, actual)) {
            throw FinException.build("Not equals, expect={}, actual={}", DEFAULT_ERROR_CODE, expect, actual);
        }
    }

    public static void notEquals(Object expect, Object actual) {
        if (Objects.equals(expect, actual)) {
            throw FinException.build("But equals, expect={}, actual={}", DEFAULT_ERROR_CODE, expect, actual);
        }
    }

}
