import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunPeopelCase;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.CodeTableMapper;
import com.util.EnumsUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author MrLu
 * @createTime 2020/9/27 10:10
 * @describe
 */
public class testg {

    @Test
    public void timeTest() {
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("当前时间: " + currentTime.getHour() + currentTime.getSecond());
    }

    @Test
    public void ofNullable() {
        SysUser user = null;
        System.out.println(user.getId());
        user = Optional.ofNullable(user).orElse(new SysUser());
        System.out.println(user.getId());
        user = Optional.ofNullable(user).orElseGet(() -> new SysUser());
        System.out.println(user.getId());
    }

    @Test
    public void caseStart() {
        String name = "recordstyle_name";
        name = name.replace("_name", "");
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        System.out.println(String.valueOf(cs));
    }

    @Test
    public void indexof() {
        System.out.println("recordstyle_name".indexOf("_name"));
    }

    @Test
    public void replace() {
        System.out.println("asdasdadssdaasd_name".replace("_name", ""));
    }

    @Test
    public void ann() throws Exception {
        Class<?> ObjClass = FunArchiveRecords.class;
        Object object = ObjClass.newInstance();
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变
        Map<String, Map<String, String>> bmbMap = new HashMap<>();
        //循环所有成员变量 判断哪些是需要映射码表的
        for (Field thisField :
                fields) {
            //关闭安全监测
            thisField.setAccessible(true);
            if (Modifier.isFinal(thisField.getModifiers())) {
                continue;
            }
            CodeTableMapper annotation = thisField.getAnnotation(CodeTableMapper.class);
            if (null != annotation) {
                System.out.println(annotation.codeTableType());
                String FieldName = thisField.getName();
                //是在source中新添的与码表相关值
                System.out.println(FieldName);
            }
        }
    }

    @Test
    public void testType() throws Exception {
        Class<?> ObjClass = FunPeopelCase.class;
        Object object = ObjClass.newInstance();
        Class c = object.getClass();
        Field[] fields = c.getDeclaredFields();//取得所有类成员变
        //循环所有成员变量 判断哪些是需要映射码表的
        for (Field thisField :
                fields) {
            //关闭安全监测
            thisField.setAccessible(true);
            if (thisField.getType().toString().indexOf("com.enums.Enums") > -1) {
                System.out.println(thisField.getType());
                String fieldName = thisField.getName();
                System.out.println(thisField.toGenericString());
                Class b = Class.forName(String.valueOf(thisField.getType()).replace("class ", ""));
//                    Class b = thisField.getType().newInstance().getClass();
                System.out.println(EnumsUtil.getEnumByValue(b, 0));
            }
        }
    }

    @Test
    public void fs() {
        Class c = com.enums.Enums.class;
        Class<?>[] fields = c.getClasses();//取得所有类成员变
        System.out.println(fields.length);
        Map<String, Class<?>> map = new HashMap<>();
        for (Class<?> field : fields) {
            System.out.println(field.toString().replace("class com.enums.Enums$", "").toLowerCase());
            map.put(field.toString().replace("class com.enums.Enums$", "").toLowerCase(), field);
        }
        System.out.println(map.size());
    }

    @Test
    public void StringsInt() {
        String str = "asdsadasdgfefqf5151612345";

        //方法一：使用toCharArray()方法进行字符串拆分，循环遍历输出数字
        char[] demo = str.toCharArray();
        Stream<Character> cStream = IntStream.range(0, demo.length).mapToObj(i -> demo[i]);
        String collect = cStream
                .filter(Character::isDigit)
                .map(String::valueOf)
                .collect(Collectors.joining());
        System.out.println(collect);
        char[] cs = str.toCharArray();
        StringBuffer a = new StringBuffer();
        for (char c : cs) {
            if (Character.isDigit(c)) {
                a.append(c);
            }
        }
        System.out.println(a);
    }

    @Test
    public void md5() {
        String encodeStr = DigestUtils.md5Hex("text + key");
        System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
    }

    @Test
    public void foreachJSONObject() {
        JSONObject jsonObject = JSONObject.parseObject("{\"createtime\":1605577120000,\"dictlabel\":\"正常\",\"dictsort\":1,\"dicttype\":\"sys_role_status\",\"dictvalue\":\"0\",\"id\":123,\"isdefault\":\"Y\",\"remark\":\"角色正常状态\",\"scbj\":\"0\",\"status\":\"1\",\"updatetime\":1605577122000}");
        Set<String> setiterator = jsonObject.keySet();
        for (String thisKey :
                setiterator) {
            String value = jsonObject.getString(thisKey);
            System.out.println(thisKey + ":" + value);

        }
    }

    @Test
    public void aijiajia() {
        int i = 0;
        System.out.println(i++);
    }

    @Test
    public void uuidSfc() {
        //52516724
        //62cdcd78
        JSONArray a = new JSONArray();
        JSONArray b = new JSONArray();
        b.add(1);
        b.add(2);
        b.add(3);
        a.add(b);
        System.out.println(a.toJSONString());

        JSONArray c = JSONArray.parseArray(a.toJSONString());
        for (Object thisA :
                c) {
            System.out.println((JSONArray) thisA);
        }

        System.out.println("A23010600" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 1));
        System.out.println("E230106" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8));
    }

    @Test
    public void timeCalendarTest() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 24);
        System.out.println(calendar.getTime());
    }

}
