import com.bean.jzgl.Source.FunArchiveRecords;
import com.util.ThreeDesUtil;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Mrlu
 * @createTime 2020/8/22
 * @describe des3 加密测试
 */
public class Des3Test {
    @Test
    public void des3Example() throws Exception {
        String testLength = "165161651fdgrdgfgdijofe9834kldfshjsldfkm";
        byte[] data = testLength.getBytes(); //data必须是8的倍数位置

        System.out.println(testLength);
        System.out.println("data.length=" + data.length);
        System.out.println("CBC加密解密");
        //加密
        String str5 = ThreeDesUtil.des3EncodeCBC(testLength);
//        String byts = Base64.getEncoder().encodeToString(str5);
        System.out.println(str5);

        //解密
        String str6 = ThreeDesUtil.des3DecodeCBC(str5);
        System.out.println(str6);
    }
    @Test
    public  void Strongtest(){
        try {
            Object ot = FunArchiveRecords.class.newInstance();
            Class c = ot.getClass();
            Field[] fields = c.getDeclaredFields();//取得所有类成员变
            for (Field thisField:
            fields) {
                System.out.println(thisField.getName());
            }
            List<?> a=new ArrayList<>();
            List<FunArchiveRecords> b=new ArrayList<>();
            a=b;
        }catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println(FunArchiveRecords.class.getMethods().length);
//        Method[] methods=  FunArchiveRecords.class.getMethods();
//        for (Method thisMethod:
//                methods) {
//            System.out.println( thisMethod.getName());
//        }

    }
}
