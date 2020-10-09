package com.factory;

import com.config.annotations.CodeTableMapper;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import sun.misc.BASE64Decoder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        if (StringUtils.isEmpty(p)) {
            throw new Exception("加密字段为空！wdnmd");
        } else if (StringUtils.isEmpty(finSalt)) {
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
        //这里没有对object是否能转成Map做检查处理，是因为我希望这世上没有sb
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
        } catch (Exception e) {
            reString = nullString;
            e.printStackTrace();
        }
        return reString;
    }

    /**
     * 反射将对应名字的码表字段的值setValue
     * 注意： 这里将bean中带有_name的字段判断为需要与码表映射的字段，值来源为去掉_name的字段
     * 如： recordstyle_name 将寻找 recordstyle 字段的值来映射码表的字段
     * 如果 recordstyle的值为null，那么recordstyle_name 的值对应也会为null
     *
     * @param listObj  对象List
     * @param ObjClass 对象Class
     * @return List<Object>  |  处理完了的
     * @author MrLu
     * @createTime 2020/10/3 14:16
     */

    //TODO MrLu 2020/10/4    ObjClass必须要跟list是一个类型，这个可以直接获取泛型的类型不用穿了
    protected List<?> transformBmField(List<?> listObj, final Class<?> ObjClass) throws Exception {
        Object object = ObjClass.newInstance();
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变
        //循环所有成员变量 判断哪些是需要映射码表的
        for (Field thisField :
                fields) {
            thisField.setAccessible(true);
            //获取CodeTableMapper注解
            CodeTableMapper annotation = thisField.getAnnotation(CodeTableMapper.class);

            //是在source中新添的与码表相关值
            if (null != annotation) {
                //映射字段名
                String sourceFieldName = annotation.sourceFiled();
                //在redis获取码表缓存
                Map<String, String> bmb = getBmb(annotation.codeTableType());
                //get方法  get码表字段的值
                Method getMethod = ObjClass.getDeclaredMethod("get" + StringUtil.UpCaseFirst(sourceFieldName));
                for (Object thisObj :
                        listObj) {
                    String keyValue = Optional.ofNullable(getMethod.invoke(thisObj)).orElse("-").toString();
                    thisField.set(thisObj, bmb.get(keyValue));
                }
            }
        }


        return listObj;
    }

    /**
     * 反射将对应名字的码表字段的值setValue
     * 注意： 这里将bean中带有_name的字段判断为需要与码表映射的字段，值来源为去掉_name的字段
     * 如： recordstyle_name 将寻找 recordstyle 字段的值来映射码表的字段
     * 如果 recordstyle的值为null，那么recordstyle_name 的值对应也会为null
     *
     * @param Obj      对象
     * @param ObjClass 对象Class
     * @return Object |
     * @createTime 2020/10/3 14:39
     * @author MrLu
     */
    protected Object transformBmField(Object Obj, final Class<?> ObjClass) throws Exception {
        Class c = ObjClass.newInstance().getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变
        //循环所有成员变量 判断哪些是需要映射码表的
        for (Field thisField :
                fields) {
            //关闭安全监测
            thisField.setAccessible(true);
            //获取CodeTableMapper注解
            CodeTableMapper annotation = thisField.getAnnotation(CodeTableMapper.class);
            //是在source中新添的与码表相关值
            if (null != annotation) {//该字段拥有此注解
                //映射字段名
                String sourceFieldName = annotation.sourceFiled();
                //获取缓存
                Map<String, String> bmb = getBmb(annotation.codeTableType());
                //get方法  get码表字段的值
                Method getMethod = ObjClass.getDeclaredMethod("get" + StringUtil.UpCaseFirst(sourceFieldName));
                thisField.set(Obj, bmb.get(getMethod.invoke(Obj)));
            }
        }
        return Obj;
    }


}
