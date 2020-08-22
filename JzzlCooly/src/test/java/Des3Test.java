import com.util.ThreeDesUtil;
import org.junit.Test;

import java.util.Base64;

import static com.util.ThreeDesUtil.strTo8Length;

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
}
