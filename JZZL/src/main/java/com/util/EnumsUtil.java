package com.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author MrLu
 * @createTime 2020/8/19 16:27
 * @describe
 */
public class EnumsUtil {
    /**
     * 通过某方法和值获取枚举对象
     *
     * @param enumClass 枚举.class
     * @param value     值
     * @param method    方法名
     * @return Enum |
     * @author MrLu
     * @createTime 2020/8/19 16:38
     */
    public static <E extends Enum<?>> E valueOf(Class<E> enumClass, Object value, Method method) {
        E[] es = enumClass.getEnumConstants();
        try {
            for (E e : es) {
                Object evalue;
                method.setAccessible(true);
                evalue = method.invoke(e);
                if (value instanceof Number && evalue instanceof Number
                        && new BigDecimal(String.valueOf(value)).compareTo(new BigDecimal(String.valueOf(evalue))) == 0) {
                    return e;
                }
                if (Objects.equals(evalue, value)) {
                    return e;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

     /**
     * 同过值和枚举对象的getValue方法获取枚举对象
     * @author MrLu
      * @param enumClass 枚举.class
      * @param value     值
     * @createTime  2020/8/19 16:39
     * @return   Enum |
      */
    public static <E extends Enum<E>> E getEnumByValue(final Class<E> enumClass, Object value) {
        try {
            return valueOf(enumClass, value, enumClass.getMethod("getValue"));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
