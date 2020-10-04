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
    private static Map<String, Class<?>> ENUMMAP = new HashMap<>();

    private static void loadENUMMAP() {
        Class c = com.enums.Enums.class;
        Class<?>[] fields = c.getClasses();//取得所有类成员变
        for (Class<?> field : fields) {
            ENUMMAP.put(field.toString().replace("class com.enums.Enums$", "").toLowerCase(), field);
        }
    }

    /**
     * 将一个map转成对应的对象
     *
     * @param map       需要转化的map
     * @param beanClass beanClass 如 XXX.class
     * @return Object |   转化后的object
     * @author MrLu
     * @createTime 2020/9/24 16:55
     */
    @NotNull
    @Deprecated
    public static Object mapToBeaByBeanClass(Map<String, Object> map, Class<?> beanClass) throws Exception {
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
                Class b = Class.forName(String.valueOf(thisField.getType()).replace("class ", ""));
                map.put(fieldName, Objects.requireNonNull(EnumsUtil.getEnumByValue(b, map.get(fieldName.toUpperCase()))).toString());
            }
        }
        return map;
    }

     /**
     * 将map中的所有与com.enums.Enums定义的枚举同名的变为对应枚举的name值
     * @author MrLu
     * @param  map
     * @createTime  2020/10/4 21:26
     * @return   Object  |
      */
    private static Object mapToBean(Map<String, Object> map) throws Exception {
        Map<String, Object> newmap=new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey().toLowerCase();
            if (null != ENUMMAP.get(key)) {
                System.out.println(entry.getKey());
                Class cla = ENUMMAP.get(key);
                //此处报错说明你很有可能值枚举不到
                newmap.put(key, Objects.requireNonNull(EnumsUtil.getEnumByValue(cla, entry.getValue())).toString());
            } else {
                newmap.put(key, entry.getValue());
            }
        }

        return newmap;
    }

    /**
     * 转化List<Map<String, Object>>为object数组
     *
     * @param maps      需要转化的mapList
     * @return List<Object> | 转化后的List
     * @author MrLu
     * @createTime 2020/9/24 16:57
     */
    public static List<Object> mapToListBean(List<Map<String, Object>> maps) throws Exception {
        if (maps == null) {
            return null;
        } else {
            loadENUMMAP();

            List<Object> list = new ArrayList(maps.size());
            for (Map<String, Object> thisMap:
            maps) {
                list.add(mapToBean(thisMap));
            }
            return list;
        }
    }
}
