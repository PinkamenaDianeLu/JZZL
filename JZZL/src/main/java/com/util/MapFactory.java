package com.util;

import com.enums.Enums;
import net.sf.cglib.beans.BeanMap;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        Object object =  beanClass.newInstance();
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变量
        for (Field thisField :
                fields) {
            //此位置中case的将判断泛型等特殊类型
            //TODO MrLu 2020/9/28  反射枚举类判断不就行了吗？！！
            switch (thisField.getName()) {
                case "persontype":
                    map.put("persontype", EnumsUtil.getEnumByValue(Enums.PersonType.class, map.get("persontype")));
                    break;
                case "issend":
                    map.put("issend", EnumsUtil.getEnumByValue(Enums.IsSend.class, map.get("issend")));
                    break;
                case "isfinal":
                    map.put("isfinal", EnumsUtil.getEnumByValue(Enums.IsFinal.class, map.get("isfinal")));
                    break;
                case "casestate":
                    map.put("casestate", EnumsUtil.getEnumByValue(Enums.CaseState.class, map.get("casestate")));
                    break;
                case "casetype":
                    map.put("casetype", EnumsUtil.getEnumByValue(Enums.CaseType.class, map.get("casetype")));
                    break;
                case "isazxt":
                    map.put("isazxt", EnumsUtil.getEnumByValue(Enums.IsAzxt.class, map.get("isazxt")));
                    break;

            }
        }
        BeanMap beanMap = BeanMap.create(object);
        beanMap.putAll(map);
        return object;
    }

    /**
     * 转化List<Map<String, Object>>为object数组
     *
     * @param maps       需要转化的mapList
     * @param beanClass beanClass 如 XXX.class
     * @return  List<Object> | 转化后的List
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
