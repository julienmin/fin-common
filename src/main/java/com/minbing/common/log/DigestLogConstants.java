package com.minbing.common.log;

/**
 * 摘要日志常量
 */
public interface DigestLogConstants {

    /**
     * 摘要日志格式: <p/>
     * ({},{},{},{},{},{},{}):(traceId,调用方ip,租户,产品码,子产品码,事件码,主体) <p/>
     * ({},{},{},{}):(类#方法,{},{},{})，留3位预留位 <p/>
     * ({},{},{}ms):(结果,返回码,耗时ms)逗号为预留位 <p/>
     * ({}):业务自定义digest模版 <p/>
     * ({}):仅在抛异常时记录异常errorMessage <p/>
     */
    String DIGEST_FORMAT = "({},{},{},{},{})({},{},{},{},{})({},{},{}ms)({})({})";
}
