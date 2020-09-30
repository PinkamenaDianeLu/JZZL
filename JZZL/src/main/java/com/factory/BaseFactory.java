package com.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import sun.misc.BASE64Decoder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author MrLu
 * @createTime 2020/9/29 10:40
 * @describe
 */
public class BaseFactory {
    @Autowired
    RedisTemplate<String, Object> redisCCTemplate;

    /**
     * 解码添加了salt的base64
     *
     * @param p base64
     * @return String  |  解码完的字符串
     * @author MrLu
     * @createTime 2020/9/30 9:55
     */
    public String DecodeUrlP(String p) throws Exception {

        String finSalt = (String) redisCCTemplate.opsForValue().get("salt");
        if (StringUtils.isEmpty(finSalt)) {
            throw new Exception("salt获取异常！请重启项目并检查redis连接");
        } else {
            final BASE64Decoder decoder = new BASE64Decoder();
            return new String(decoder.decodeBuffer(p), "UTF-8").replace(finSalt, "");
        }
    }

    /**
     * 返回对应type的编码表Map
     *
     * @param type 类型
     * @return Map<String, String>   |
     * @author MrLu
     * @createTime 2020/9/30 10:12
     */
    public Map<String, String> getBmb(String type) throws Exception {
        RedisTemplate<String, Object> redisCCTemplate = new RedisTemplate<String, Object>();
        Map<String, String> finSalt = (Map<String, String>) redisCCTemplate.opsForValue().get(type);
        finSalt = Optional.ofNullable(finSalt).orElse(new HashMap<>());
        return finSalt;
    }

    /**
     * 通过code查找码表中的值
     *
     * @param type       类型
     * @param code       代码
     * @param nullString 没有匹配的值时的返回字符串
     * @return String  |
     * @author MrLu
     * @createTime 2020/9/30 10:27
     */
    public String getBmName(String type, String code, String nullString) {
        String reString = "";
        try {
            reString = Optional.ofNullable(getBmb(type).get(code)).orElse(nullString);
            ;
        } catch (Exception e) {
            reString = nullString;
            e.printStackTrace();
        }
        return reString;
    }

    //TODO MrLu 2020/9/30  反射将对应名字的值setValue
    public List<?> setBmbName(final Class<?> ObjClass, List<T> listObj) throws Exception {
        Object object = ObjClass.newInstance();
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变
        Map<String, Map<String, String>> bmbMap = new HashMap<>();
        for (Field thisField :
                fields) {
            String FieldName = thisField.getName();
            //是在source中新添的与码表相关值
            if (FieldName.indexOf("_name") > 0) {
                Map<String, String> bmb = getBmb(sourceBmbCoulmn(FieldName));
                bmbMap.put(FieldName, bmb);
            }
        }

        for (T a :
                listObj) {
            for (Map.Entry<String, Map<String, String>> entry : bmbMap.entrySet()) {
                //获取code
//                a.getMethod("set" + entry.getKey()).invoke("111");
            }

        }
        return listObj;
    }

    private String sourceBmbCoulmn(String name) {
        name = name.replace("_name", "");
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }
}
