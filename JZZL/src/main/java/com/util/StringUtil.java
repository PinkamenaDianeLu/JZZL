package com.util;/**
 * @author Mrlu
 * @createTime 2020/10/3
 * @describe
 */

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author MrLu
 * @createTime 2020/10/3 13:49
 * @describe
 */
public class StringUtil {

    /**
     * 大写字符串的第一个字母
     *
     * @param s
     * @return String |
     * @author MrLu
     * @createTime 2020/10/3 13:49
     */
    public static String UpCaseFirst(String s) throws Exception {
        char[] cs = s.toCharArray();
        if (Character.isLetter(cs[0])) {
            cs[0] -= 32;
        } else {
            //第一个字节非字母，无法转化
            throw new Exception("第一个字节非字母，无法转化！");
        }
        return String.valueOf(cs);
    }

    /**
     * 提取字符串中的数字转换为数字  （12ww2->122）
     *
     * @param str 要提取的字符串
     * @return Integer   |
     * @author MrLu
     * @createTime 2020/10/27 16:41
     */
    public static Integer StringToInteger(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        char[] demo = str.toCharArray();
        Stream<Character> cStream = IntStream.range(0, demo.length).mapToObj(i -> demo[i]);
        String collect = cStream
                .filter(Character::isDigit)
                .map(String::valueOf)
                .collect(Collectors.joining());
        return Integer.parseInt(collect);
    }


     /**
     * 判断是否有为null的参数
     * @author MrLu
     * @param ss 不定参 String
     * @createTime  2020/12/8 9:32
     * @return  boolean  |
      */
    public static boolean isEmptyAll(String... ss) {
        boolean t = false;
        for (String thisSS :
                ss) {
            if (StringUtils.isEmpty(thisSS)) {
                t = true;
                break;
            }
        }
        return t;
    }

    public static  String createSfcNumber(String dw){

        return "E"+dw+System.currentTimeMillis()+UUID.randomUUID().toString().substring(0,8);

    }
}
