package com.util;/**
 * @author Mrlu
 * @createTime 2020/10/3
 * @describe
 */

import org.apache.commons.lang3.StringUtils;

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
     * @author MrLu
     * @param s
     * @createTime  2020/10/3 13:49
     * @return   String |
      */
    public static String UpCaseFirst(String s)throws  Exception{
        char[] cs = s.toCharArray();
        if (Character.isLetter( cs[0])){
            cs[0] -= 32;
        }else {
            //第一个字节非字母，无法转化
            throw new Exception("第一个字节非字母，无法转化！");
        }
        return String.valueOf(cs);
    }

     /**
     * 提取字符串中的数字转换为数字  （12ww2->122）
     * @author MrLu
     * @param str 要提取的字符串
     * @createTime  2020/10/27 16:41
     * @return Integer   |
      */
    public static Integer StringToInteger(String str){
        if (StringUtils.isEmpty(str)){
            return  null;
        }
        char[] demo = str.toCharArray();
        Stream<Character> cStream = IntStream.range(0, demo.length).mapToObj(i -> demo[i]);
        String collect = cStream
                .filter(Character::isDigit)
                .map(String::valueOf)
                .collect(Collectors.joining());
        return  Integer.parseInt(collect);
    }

}
