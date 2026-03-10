package com.minbing.common.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 金额类，以最小货币单位（如分、聪）存储，类型为 long。
 * <p>
 * 特性：
 * <ul>
 *   <li>金额以最小单位（unit）存储，保证整数运算无精度损失。</li>
 *   <li>货币通过 {@link CurrencyUnit} 接口抽象，可扩展支持任意货币。</li>
 *   <li>内置常用法币（USD、CNY、EUR）和加密货币（BTC、ETH）的预定义实例。</li>
 *   <li>提供从主单位（如元、BTC）创建的方法，自动转换为最小单位。</li>
 *   <li>加、减、乘、除、取反等运算均返回新的不可变对象。</li>
 *   <li>乘法/除法使用 {@link BigDecimal} 计算后按货币小数位数舍入为最小单位整数。</li>
 *   <li>重写 {@code equals}、{@code hashCode} 和 {@code toString}。</li>
 * </ul>
 * <p>
 * 注意：对于小数位数较多的货币（如 ETH 为 18 位），最小单位（wei）的 long 表示范围有限（最大约 9.22 ETH），
 * 请确保业务金额不超过此限制。若需更大范围，可考虑使用 {@link java.math.BigInteger} 存储。
 *
 * @author example
 */
public final class Money implements Comparable<Money> {

    /** 最小单位金额，如分、聪、wei */
    private final long amount;

    /** 货币单位 */
    private final CurrencyUnit currency;

    // ========================== 构造方法 ==========================

    /**
     * 私有构造器，通过工厂方法创建实例。
     *
     * @param amount   最小单位金额
     * @param currency 货币单位
     */
    private Money(long amount, CurrencyUnit currency) {
        this.amount = amount;
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
    }

    /**
     * 从最小单位金额创建 Money 对象。
     *
     * @param unitAmount 最小单位金额（如 100 表示 1 美元）
     * @param currency      货币单位
     * @return Money 实例
     */
    public static Money ofUnit(long unitAmount, CurrencyUnit currency) {
        return new Money(unitAmount, currency);
    }

    /**
     * 从主单位金额（字符串形式）创建 Money 对象。
     * <p>
     * 例如：{@code Money.ofMajor("123.45", FiatCurrency.USD)} 会转换为 12345 分存储。
     *
     * @param majorAmount 主单位金额字符串（如 "123.45"）
     * @param currency    货币单位
     * @return Money 实例
     * @throws ArithmeticException 如果转换过程中出现溢出或精度丢失
     */
    public static Money ofMajor(String majorAmount, CurrencyUnit currency) {
        BigDecimal major = new BigDecimal(majorAmount);
        return ofMajor(major, currency);
    }

    /**
     * 从主单位金额（BigDecimal）创建 Money 对象。
     *
     * @param majorAmount 主单位金额
     * @param currency    货币单位
     * @return Money 实例
     * @throws ArithmeticException 如果转换过程中出现溢出或精度丢失
     */
    public static Money ofMajor(BigDecimal majorAmount, CurrencyUnit currency) {
        int fractionDigits = currency.getDefaultFractionDigits();
        if (fractionDigits < 0) {
            throw new IllegalArgumentException("Currency " + currency.getCode() + " has no fixed fraction digits");
        }
        BigDecimal unitFactor = BigDecimal.TEN.pow(fractionDigits);
        BigDecimal unitAmount = majorAmount.multiply(unitFactor);
        // 检查小数部分，确保没有超出 fractionDigits 的精度
        try {
            long amount = unitAmount.longValueExact(); // 精确转换，如果有小数部分会抛异常
            return new Money(amount, currency);
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Cannot convert " + majorAmount + " to unit of " + currency.getCode() +
                    ": " + e.getMessage());
        }
    }

    /**
     * 从主单位金额（double）创建 Money 对象（不推荐用于精确计算）。
     *
     * @param majorAmount 主单位金额
     * @param currency    货币单位
     * @return Money 实例
     * @throws ArithmeticException 如果转换过程中出现溢出或精度丢失
     */
    public static Money ofMajor(double majorAmount, CurrencyUnit currency) {
        return ofMajor(BigDecimal.valueOf(majorAmount), currency);
    }

    /**
     * 获取最小单位金额。
     *
     * @return 最小单位金额（如分、聪）
     */
    public long getUnitAmount() {
        return amount;
    }

    /**
     * 获取主单位金额（BigDecimal）。
     *
     * @return 主单位金额（如美元、BTC）
     */
    public BigDecimal getMajorAmount() {
        int fractionDigits = currency.getDefaultFractionDigits();
        if (fractionDigits < 0) {
            // 无固定小数位数，直接返回 BigDecimal 值（以最小单位作为整数）
            return BigDecimal.valueOf(amount);
        }
        BigDecimal unitFactor = BigDecimal.TEN.pow(fractionDigits);
        return BigDecimal.valueOf(amount).divide(unitFactor, fractionDigits, RoundingMode.UNNECESSARY);
    }

    /**
     * 加法，要求货币相同。
     *
     * @param other 另一个金额
     * @return 新金额
     * @throws IllegalArgumentException 如果货币不同
     * @throws ArithmeticException 如果结果溢出 long 范围
     */
    public Money add(Money other) {
        checkSameCurrency(other);
        long result = Math.addExact(this.amount, other.amount);
        return new Money(result, this.currency);
    }

    /**
     * 减法，要求货币相同。
     *
     * @param other 另一个金额
     * @return 新金额
     * @throws IllegalArgumentException 如果货币不同
     * @throws ArithmeticException 如果结果溢出 long 范围
     */
    public Money subtract(Money other) {
        checkSameCurrency(other);
        long result = Math.subtractExact(this.amount, other.amount);
        return new Money(result, this.currency);
    }

    /**
     * 乘法（乘以一个整数因子，直接对最小单位进行乘法，不引入小数）。
     *
     * @param multiplier 整数乘数
     * @return 新金额
     * @throws ArithmeticException 如果结果溢出 long 范围
     */
    public Money multiply(long multiplier) {
        long result = Math.multiplyExact(this.amount, multiplier);
        return new Money(result, this.currency);
    }

    /**
     * 乘法（乘以一个 BigDecimal 因子，结果按货币小数位数舍入为最小单位整数）。
     *
     * @param multiplier  乘数（BigDecimal）
     * @param roundingMode 舍入模式
     * @return 新金额
     */
    public Money multiply(BigDecimal multiplier, RoundingMode roundingMode) {
        BigDecimal current = BigDecimal.valueOf(amount);
        BigDecimal result = current.multiply(multiplier);
        long rounded = roundToUnit(result, roundingMode);
        return new Money(rounded, this.currency);
    }

    public Money multiply(double multiplier, RoundingMode roundingMode) {
        return multiply(BigDecimal.valueOf(multiplier), roundingMode);
    }

    /**
     * 除法（除以一个整数因子，结果按货币小数位数舍入）。
     *
     * @param divisor      除数（整数）
     * @param roundingMode 舍入模式
     * @return 新金额
     * @throws ArithmeticException 如果除数为0
     */
    public Money divide(long divisor, RoundingMode roundingMode) {
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        BigDecimal current = BigDecimal.valueOf(amount);
        BigDecimal result = current.divide(BigDecimal.valueOf(divisor), 20, roundingMode); // 保留足够精度
        long rounded = roundToUnit(result, roundingMode);
        return new Money(rounded, this.currency);
    }

    /**
     * 除法（除以一个 BigDecimal 因子，结果按货币小数位数舍入）。
     *
     * @param divisor      除数
     * @param roundingMode 舍入模式
     * @return 新金额
     */
    public Money divide(BigDecimal divisor, RoundingMode roundingMode) {
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Division by zero");
        }
        BigDecimal current = BigDecimal.valueOf(amount);
        // 计算时保留足够的小数位数，避免精度丢失
        BigDecimal result = current.divide(divisor, 20, roundingMode);
        long rounded = roundToUnit(result, roundingMode);
        return new Money(rounded, this.currency);
    }

    public Money divide(double divisor, RoundingMode roundingMode) {
        return divide(BigDecimal.valueOf(divisor), roundingMode);
    }

    /**
     * 取反（变号）。
     *
     * @return 新金额
     */
    public Money negate() {
        return new Money(-this.amount, this.currency);
    }

    /**
     * 取绝对值。
     *
     * @return 新金额
     */
    public Money abs() {
        return this.amount >= 0 ? this : negate();
    }

    // ========================== 辅助舍入方法 ==========================

    /**
     * 将 BigDecimal 金额（以最小单位表示）按货币小数位数舍入为 long。
     * 注意：输入值应为以最小单位表示的数值（如 12345.678 表示 12345.678 分）。
     */
    private long roundToUnit(BigDecimal unitAmount, RoundingMode roundingMode) {
        // 根据货币小数位数确定目标精度：小数部分超过 fractionDigits 的需要舍入
        int fractionDigits = currency.getDefaultFractionDigits();
        BigDecimal rounded;
        if (fractionDigits < 0) {
            // 无固定小数位数，直接保留所有小数（但需舍入到 long）
            rounded = unitAmount.setScale(0, roundingMode);
        } else {
            // 保留到整数（因为最小单位是整数）
            rounded = unitAmount.setScale(0, roundingMode);
        }
        try {
            return rounded.longValueExact();
        } catch (ArithmeticException e) {
            throw new ArithmeticException("Result out of long range or has non-zero fractional part after rounding: " + rounded);
        }
    }

    // ========================== 比较与相等 ==========================

    @Override
    public int compareTo(Money other) {
        checkSameCurrency(other);
        return Long.compare(this.amount, other.amount);
    }

    /**
     * 检查两个金额是否相等（包括货币和数值）。
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount == money.amount && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        // 显示格式：符号 + 主单位金额（小数位数按货币标准，如 123.45 USD）
        BigDecimal major = getMajorAmount();
        int fractionDigits = currency.getDefaultFractionDigits();
        if (fractionDigits >= 0) {
            major = major.setScale(fractionDigits, RoundingMode.HALF_EVEN);
        }
        return currency.getSymbol() + " " + major.toPlainString();
    }

    // ========================== 私有辅助方法 ==========================

    private void checkSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch: " + this.currency.getCode() + " vs " + other.currency.getCode());
        }
    }

    public static void main(String[] args) {
        // 从主单位创建
        Money usd = Money.ofMajor("100.50", FiatCurrency.getInstance("USD"));       // 存储为 10050 分
        Money btc = Money.ofMajor("0.12345678", CryptoCurrency.getInstance("BTC")); // 存储为 12345678 聪
        Money eth = Money.ofMajor("5.0", CryptoCurrency.getInstance("ETH"));        // 存储为 5 * 10^18 wei (注意 long 限制)

        // 从最小单位创建
        Money usd2 = Money.ofUnit(5025, FiatCurrency.getInstance("USD")); // 50.25 美元

        // 加法
        Money totalUsd = usd.add(usd2);
        System.out.println("Total USD: " + totalUsd); // USD 150.75

        // 乘法（整数因子）
        Money doubledBtc = btc.multiply(2);
        System.out.println("Double BTC: " + doubledBtc); // BTC 0.24691356

        // 乘法（小数因子，需舍入）
        Money halfBtc = btc.multiply(0.5, RoundingMode.HALF_UP);
        System.out.println("Half BTC: " + halfBtc);

        // 除法
        Money dividedUsd = usd.divide(3, RoundingMode.HALF_EVEN);
        System.out.println("USD/3: " + dividedUsd); // USD 33.50 (100.50/3 ≈ 33.5，舍入到分)

        // 比较
        Money moreBtc = Money.ofMajor("0.2", CryptoCurrency.getInstance("BTC"));
        System.out.println(btc.compareTo(moreBtc) < 0); // true

        // 获取主单位金额
        System.out.println(btc.getMajorAmount()); // 0.12345678

        // 获取最小单位金额
        System.out.println(btc.getUnitAmount()); // 12345678

        // 不同货币运算会抛出异常
        try {
            usd.add(btc);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // 自定义加密货币
        Money dogeAmount = Money.ofMajor("1000", CryptoCurrency.getInstance("DOGE"));
        System.out.println("Doge: " + dogeAmount); // Ð 1000.00000000
    }

    private static void test() {
        // 法币，从主单位（原）创建
        Money usd = Money.ofMajor("100.50", FiatCurrency.getInstance("USD"));       // 存储为 10050 分
        // 法币，从最小货币单位创建
        Money usd2 = Money.ofUnit(5025, FiatCurrency.getInstance("USD")); // 50.25 美元

        // 加法
        Money totalUsd = usd.add(usd2);
        // USD 150.75
        System.out.println("Total USD: " + totalUsd);

        // 存储为 12345678 聪
        Money btc = Money.ofMajor("0.12345678", CryptoCurrency.getInstance("BTC"));
        // 乘法（小数因子，需舍入）
        Money halfBtc = btc.multiply(0.5, RoundingMode.HALF_UP);
        // BTC 0.06172839
        System.out.println("Half BTC: " + halfBtc);
    }
}