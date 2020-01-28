package io.github.lmikoto.utils;

import java.math.BigDecimal;

public abstract class BigDecimalUtils {

    /**
     * 是否是一个正数
     *
     * @param src
     * @return
     */
    public static boolean isPositive(BigDecimal src) {
        return BigDecimal.ZERO.compareTo(src) < 0;
    }

    /**
     * 是否是0
     *
     * @param src
     * @return
     */
    public static boolean isZero(BigDecimal src) {
        return BigDecimal.ZERO.compareTo(src) == 0;
    }

    /**
     * 是否是负数
     *
     * @param src
     * @return
     */
    public static boolean isNegative(BigDecimal src) {
        return BigDecimal.ZERO.compareTo(src) > 0;
    }

    /**
     * 是否是非负数
     *
     * @param src
     * @return
     */
    public static boolean isNonNegative(BigDecimal src) {
        return !isNegative(src);
    }


    /**
     * 是否是一个非正数
     *
     * @param src
     * @return
     */
    public static boolean isNonPositive(BigDecimal src){
        return !isPositive(src);
    }

    /**
     * 是否相等
     * @param src
     * @param target
     * @return
     */
    public static boolean isEqual(BigDecimal src,BigDecimal target){
        return src.compareTo(target) == 0;
    }
}
