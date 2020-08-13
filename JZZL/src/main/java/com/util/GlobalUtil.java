package com.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Properties;

/**
 * 用于读取配置文件
 * @author Mrlu
 * @createTime 2020/6/28
 * @describe 用于读取配置文件
 */
public class GlobalUtil {
    private static Properties globalMap = new Properties();
    static{
        //加载通用配置配置文件
        loadGlobalConfig();
    }
    public static void loadGlobalConfig(){
        //加载错误信息配置文件
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(Objects.requireNonNull(GlobalUtil.class.getClassLoader().getResourceAsStream("global.properties")), "UTF-8");
            globalMap.load(isr);
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        } finally{
            try {
                isr.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取通用配置信息
     * @param globalCode
     * @return
     */
    public static String getGlobal(String globalCode){
        return globalMap.get(globalCode).toString();
    }

}
