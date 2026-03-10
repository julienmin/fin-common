package com.minbing.common.lang;

/**
 * 交互结果码<p/>
 * 不表示业务结果：如调用支付接口，SUCCESS表示调用成功，但业务结果可能是支付失败
 */
public enum FinResultCode {
    /**
     * 成功
     */
    SUCCEEDED,

    /**
     * 未知。未知不能直接当成功或失败处理，否则有资损风险
     */
    UNKNOWN,

    /**
     * 失败
     */
    FAILED
}
