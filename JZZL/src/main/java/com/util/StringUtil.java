package com.util;/**
 * @author Mrlu
 * @createTime 2020/10/3
 * @describe
 */

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

}
