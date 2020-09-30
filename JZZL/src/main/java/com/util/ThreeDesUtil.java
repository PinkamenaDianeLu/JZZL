package com.util;


import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

/**
 * des3 加密 操作
 * @Author MrLu
 * @createTime 2020/4/26 14:40
 * @describe des3 加密
 */
public class ThreeDesUtil {

    // 算法名称
    public static final String KEY_ALGORITHM = "desede";
    // 算法名称/加密模式/填充方式
    public static final String CIPHER_ALGORITHM = "desede/CBC/NoPadding";

    private static final byte[] key = "290CEAD10482CA1FF952892B3F67A24E290CEAD10482CA1F".getBytes();//必须是48位
    private static final byte[] keyiv = {9, 8, 7, 6, 5, 6, 7, 8};

    /**
     * CBC加密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  明文
     * @return Base64编码的密文
     * @throws Exception
     */
    private static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Key deskey = keyGenerator(new String(key));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        for (int k = 0; k < bOut.length; k++) {
            System.out.print(bOut[k] + " ");
        }
        System.out.println("");
        return bOut;
    }

    /**
     * 生成密钥key对象
     *
     * @param keyStr 密钥字符串
     * @return 密钥对象
     * @throws Exception
     */
    private static Key keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESedeKeySpec KeySpec = new DESedeKeySpec(input);
        SecretKeyFactory KeyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return ((Key) (KeyFactory.generateSecret(((java.security.spec.KeySpec) (KeySpec)))));
    }

    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    // 从十六进制字符串到字节数组转换
    private static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    /**
     * CBC解密
     *
     * @param key   密钥
     * @param keyiv IV
     * @param data  Base64编码的密文
     * @return 明文
     * @throws Exception
     */
    private static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = keyGenerator(new String(key));
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * @param
     * @author MrLu
     * @createTime 2020/4/26 15:09
     * @describe 加密字符串
     * @version 1.0
     */
    public static String des3EncodeCBC(byte[] data) throws Exception {
        byte[] str5 = des3EncodeCBC(key, keyiv, data);
        String reCode = Base64.getEncoder().encodeToString(str5);
        return reCode;
    }

    /**
     * @param
     * @author MrLu
     * @createTime 2020/4/26 15:10
     * @describe 解密字符串
     * @version 1.0
     */
    public static String des3DecodeCBC(String code) throws Exception {
        if(StringUtils.isEmpty(code)){
            return "";
        }
        byte[] str6 = des3DecodeCBC(key, keyiv, Base64.getDecoder().decode(code));
        return new String(str6, "UTF-8");
    }

     /**
     * 将任意字符串转的长度转化为8的倍数 并以@_填充
     * @author MrLu
     * @param oriStr 需要转化的字符串
     * @createTime  2020/8/20 10:35
     * @return  String 已经是8的倍数 |
      */
    public static String strTo8Length(String oriStr)throws Exception{
        oriStr=oriStr+"@";
        StringBuilder reString= new StringBuilder(oriStr);
        if (StringUtils.isEmpty(oriStr)){
            throw  new Exception("字符串为空");
        }
        int strLength=oriStr.length();//字符串原长度
        int strTo8=8-(strLength%8);
        while (strTo8>0){
            reString.append("_");
            strTo8--;
        }
        return reString.toString();
    }

    /**
    * @description
    * @log  2020/9/30 10:10  MrLu  移动至 BaseFactory.DecodeUrlP
    **/
    @Deprecated
    public static String DecodeUrlP(String p) throws Exception{
        String finSalt = GlobalUtil.getGlobal("salt") ;
        if (StringUtils.isEmpty(finSalt)){
            throw  new  Exception("salt获取异常！请重启项目并检查redis连接");
        }else {
            final BASE64Decoder decoder = new BASE64Decoder();
            return  new String(decoder.decodeBuffer(p), "UTF-8");
        }
    }

    public static void main(String[] args) throws Exception {
        byte[] key = "290CEAD10482CA1FF952892B3F67A24E290CEAD10482CA1F".getBytes();//必须是48位
        byte[] keyiv = {9, 8, 7, 6, 5, 6, 7, 8};
        String testLength=strTo8Length("165161651fdgrdgfgdijofe9834kldfshjsldfkm");
        byte[] data = testLength.getBytes(); //data必须是8的倍数位置

        System.out.println(testLength);
        System.out.println("data.length=" + data.length);
        System.out.println("CBC加密解密");
        //加密
        byte[] str5 = des3EncodeCBC(key, keyiv, data);
        String byts = Base64.getEncoder().encodeToString(str5);
        System.out.println(byts);

        System.out.println(str5);
        //解密
        byte[] str6 = des3DecodeCBC(key, keyiv, Base64.getDecoder().decode(byts));
        System.out.println(new String(str6, "UTF-8"));
    }

}
