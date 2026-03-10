package com.minbing.common.lang;

import java.io.Serializable;
import java.util.Currency;

/**
 * 法币实现，基于 java.util.Currency 封装。
 */
public class FiatCurrency implements CurrencyUnit, Serializable {
    private static final long serialVersionUID = 1L;

    private final Currency currency;

    private FiatCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * 跟进法币编码获取法币对象
     * @param code
     * @return
     */
    public static FiatCurrency getInstance(String code) {
        Currency currency = Currency.getInstance(code);
        return new FiatCurrency(currency);
    }

    @Override
    public String getCode() {
        return currency.getCurrencyCode();
    }

    @Override
    public int getDefaultFractionDigits() {
        return currency.getDefaultFractionDigits();
    }

    @Override
    public String getSymbol() {
        return currency.getSymbol();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FiatCurrency that = (FiatCurrency) o;
        return currency.equals(that.currency);
    }

    @Override
    public int hashCode() {
        return currency.hashCode();
    }

    @Override
    public String toString() {
        return currency.toString();
    }

}