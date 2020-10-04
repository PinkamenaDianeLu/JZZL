package com.util;

import com.enums.Enums;
import net.sf.cglib.beans.BeanMap;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author MrLu
 * @createTime 2020/9/24 15:27
 * @describe map操作util  依赖于cglib
 */
public class MapFactory {

    /**
     * 将一个map转成对应的对象，枚举类需要进行定义
     *
     * @param map       需要转化的map
     * @param beanClass beanClass 如 XXX.class
     * @return Object |   转化后的object
     * @author MrLu
     * @createTime 2020/9/24 16:55
     */
    @NotNull
    public static Object mapToBean(Map<String, Object> map, Class<?> beanClass) throws Exception {
        Object object = beanClass.newInstance();
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变量
        for (Field thisField :
                fields) {
            thisField.setAccessible(true);
            //判断泛型等特殊类型
            if (thisField.getType().toString().indexOf("com.enums.Enums") > -1) {
                String fieldName = thisField.getName();
                //注意class后面有个空格！
                Class b = Class.forName(String.valueOf(thisField.getType()).replace("class ",""));
                map.put(fieldName, Objects.requireNonNull(EnumsUtil.getEnumByValue(b, map.get(fieldName))).toString());
            }
        }
        return map;
    }

    /**
     * 转化List<Map<String, Object>>为object数组
     *
     * @param maps      需要转化的mapList
     * @param beanClass beanClass 如 XXX.class
     * @return List<Object> | 转化后的List
     * @author MrLu
     * @createTime 2020/9/24 16:57
     */
    public static List<Object> mapToListBean(List<Map<String, Object>> maps, Class<?> beanClass) throws Exception {
        if (maps == null) {
            return null;
        } else {
            List<Object> list = new ArrayList(maps.size());
            Iterator<Map<String, Object>> var3 = maps.iterator();

            while (var3.hasNext()) {
                Map<String, Object> map = var3.next();
                list.add(mapToBean(map, beanClass));
            }
            return list;
        }
    }
}
