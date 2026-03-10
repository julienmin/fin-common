package com.minbing.common.utils;

import com.google.common.collect.Maps;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 基于BeanCopier包装，用于对象深拷贝
 */
public class BeanCopierUtil {

    private static final String KEY_TEMPLATE = "%s_%s";

    private static final Map<String, BeanCopier> COPIERS_CACHES = Maps.newConcurrentMap();

    public static <T> T copy(Object sourceObj, Class<T> targetClass) {
        return copy(sourceObj, targetClass, null);
    }

    public static <T> T copy(Object sourceObj, Class<T> targetClass, Consumer<T> callback) {
        if (sourceObj == null) {
            return null;
        }

        try {
            T targetObj = targetClass.newInstance();

            copyObject(sourceObj, targetObj, callback);

            return targetObj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> void copyObject(Object sourceObj, T targetObj, Consumer<T> callback) {
        BeanCopier beanCopier = getBeanCopier(sourceObj.getClass(), targetObj.getClass());
        // 拷贝
        beanCopier.copy(sourceObj, targetObj, null);

        if (callback != null) {
            callback.accept(targetObj);
        }
    }

    private static <T> BeanCopier getBeanCopier(Class<?> sourceClass, Class<T> targetClass) {
        String cacheKey = String.format(KEY_TEMPLATE, sourceClass.getName(), targetClass.getName());

        BeanCopier beanCopier = COPIERS_CACHES.get(cacheKey);
        if (beanCopier == null) {
            beanCopier = BeanCopier.create(sourceClass, targetClass, false);
            COPIERS_CACHES.put(cacheKey, beanCopier);
        }

        return beanCopier;
    }

}
