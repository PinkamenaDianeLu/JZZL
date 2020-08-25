package com.aop;

import com.sun.istack.internal.NotNull;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
/**
 * @author MrLu
 * @createTime 2020/8/24 14:52
 * @describe
 */
public class LogsRecorder {
    /**
     * @param paramMap request获取的参数数组
     * @author MrLu
     * @createTime 2020/4/27 0:00
     * @describe 转换request 请求参数
     * @version 1.0
     */
    public Map<String, String> converMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<String, String>();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }


     /**
     * 获取参数
     * @author MrLu
     * @param 
     * @createTime  2020/8/25 15:44
     * @return    |  
      */
     @NotNull
    private Map<String, String> getMethodParameters (JoinPoint jp){
        Map<String, String> reMap=new HashMap<>();
        Object[] args = jp.getArgs();//参数
        String[] names = ((CodeSignature)jp.getSignature()).getParameterNames();//参数名
        Class[] types = ((CodeSignature)jp.getSignature()).getParameterTypes();//参数类型
        for (int i = 0; i < args.length; i++) {
            reMap.put(types[i].getName()+"=>"+names[i],String.valueOf(null==args[i]?"":args[i]));
        }
        return  reMap;
        
    }
    
     /**
     * 记录日志
     * @author MrLu
     * @param 
     * @createTime  2020/8/25 15:43
     * @return    |  
      */
    public void insertLogs(){
        
    }

    /**
     * @author MrLu
     * @param
     * @createTime  2020/5/19 21:20
     * @describe 获取用的真实ip地址
     * @version 1.0
     */
    static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ip= inet.getHostAddress();
            }
        }
        return ip;
    }
}
