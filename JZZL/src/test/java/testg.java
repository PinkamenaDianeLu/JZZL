import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunPeopelCase;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.CodeTableMapper;
import com.util.EnumsUtil;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        Class c =com.enums.Enums.class;
        Class<?>[] fields = c.getClasses();//取得所有类成员变
        System.out.println(fields.length);
        Map<String,Class<?>> map=new HashMap<>();
        for (Class<?> field : fields) {
            System.out.println(field.toString().replace("class com.enums.Enums$","").toLowerCase());
            map.put(field.toString().replace("class com.enums.Enums$","").toLowerCase(),field);
        }
        System.out.println(map.size());
    }
}
