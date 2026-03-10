package com.minbing.common.lang;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 加密货币实现。
 */
public class CryptoCurrency implements CurrencyUnit, Serializable {
    private static final long serialVersionUID = 1L;

    private static final Map<String, CryptoCurrency> CRYPTO_CURRENCIES = Maps.newHashMap();

    static {
        CRYPTO_CURRENCIES.put("BTC", new CryptoCurrency("BTC", 8, "₿"));
        CRYPTO_CURRENCIES.put("ETH", new CryptoCurrency("ETH", 18, "Ξ"));
        CRYPTO_CURRENCIES.put("LTC", new CryptoCurrency("LTC", 8, "Ł"));
        CRYPTO_CURRENCIES.put("XRP", new CryptoCurrency("XRP", 6, "XRP"));
        CRYPTO_CURRENCIES.put("BCH", new CryptoCurrency("BCH", 8, "BCH"));
        CRYPTO_CURRENCIES.put("EOS", new CryptoCurrency("EOS", 4, "EOS"));
        CRYPTO_CURRENCIES.put("USDT", new CryptoCurrency("USDT", 2, "USDT"));
        CRYPTO_CURRENCIES.put("XLM", new CryptoCurrency("XLM", 7, "XLM"));
        CRYPTO_CURRENCIES.put("TRX", new CryptoCurrency("TRX", 6, "TRX"));
        CRYPTO_CURRENCIES.put("DOGE", new CryptoCurrency("DOGE", 8, "Ð"));
    }

    private final String code;
    private final int fractionDigits;
    private final String symbol;

    /**
     * @param code          货币代码（如 BTC）
     * @param fractionDigits 小数位数（如比特币为8，以太坊为18）
     * @param symbol        货币符号（如 ₿, Ξ），可为 null，默认使用 code
     */
    private CryptoCurrency(String code, int fractionDigits, String symbol) {
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.fractionDigits = fractionDigits;
        this.symbol = symbol != null ? symbol : code;
    }

    private CryptoCurrency(String code, int fractionDigits) {
        this(code, fractionDigits, code);
    }

    /**
     * 跟进数币获取货币对象
     *
     * @param code 货币编码
     * @return 货币对象
     */
    public static CryptoCurrency getInstance(String code) {
        return CRYPTO_CURRENCIES.get(code);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int getDefaultFractionDigits() {
        return fractionDigits;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoCurrency that = (CryptoCurrency) o;
        return fractionDigits == that.fractionDigits && code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, fractionDigits);
    }

    @Override
    public String toString() {
        return code;
    }

}