package com.minbing.common.lang;

/**
 * 货币单位接口，定义货币的标识、小数位数和最小单位换算因子。
 */
public interface CurrencyUnit {
    /**
     * 返回货币代码（如 USD, BTC）。
     */
    String getCode();

    /**
     * 返回该货币的标准小数位数（例如美元为2，比特币为8）。
     * 小数位数决定了最小单位与主单位的换算关系：1 主单位 = 10^{fractionDigits} 最小单位。
     */
    int getDefaultFractionDigits();

    /**
     * 返回货币符号（可选，用于显示）。
     */
    default String getSymbol() {
        return getCode();
    }
}
