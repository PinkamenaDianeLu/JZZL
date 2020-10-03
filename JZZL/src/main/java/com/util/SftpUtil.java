package com.util;


import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.Properties;

/**
 * 连接 上传ftp 操作
 * @author MrLu
 * @createTime 2020/4/29
 * @describe 连接 上传ftp
 */
public class SftpUtil {


    static private Session session = null;
    static private Channel channel = null;
    final static private int timeout = 60000; //超时数,一分钟

    /**
     *
     * @author MrLu
     * @createTime 2020/4/29 23:00
     * @describe 创建channel
     * @version 1.0
     */
    private static ChannelSftp getChannel(String username, String password, String ip, int port) throws JSchException {
        JSch jsch = new JSch(); // 创建JSch对象
        // 根据用户名，主机ip，端口获取一个Session对象
        session = jsch.getSession(username, ip, port);
        if (password != null) {
            session.setPassword(password); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        return (ChannelSftp) channel;
    }

    /**
     * 关闭channel和session
     * @author MrLu
     * @throws Exception
     */
    private static void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }

    /**
     * @param fileName   文件名
     * @param dstDirPath 上传文件路径
     * @param fileType   上传的文件类型 不可为空
     * @param inputFile  上传的文件
     * @return  rePath 文件在服务器的完整路径
     * @author MrLu
     * @createTime 2020/4/29 23:06
     * @describe
     * @version 1.0
     */
    public static String upLoad(String fileName, String dstDirPath, String fileType, InputStream inputFile) throws Exception {
        if (StringUtils.isEmpty(fileType)) throw new Exception("请指定上传文件类型");
        String rePath="";//返回文件的完整路径
        String username= GlobalUtil.getGlobal("ftpUserName");
        String password= GlobalUtil.getGlobal("ftpPassword");
        String host= GlobalUtil.getGlobal("ftpHost");
        Integer port=Integer.parseInt(GlobalUtil.getGlobal("ftpPort"));
        ChannelSftp channelSftp = getChannel(username, password, host, port);
        String nginxUrl = "";
        if ("img".equals(fileType)) {
            nginxUrl = GlobalUtil.getGlobal("nginxImgUrl") ;
        } else if ("others".equals(fileType)) {
            nginxUrl = "/TLJJ/attachments";
        } else {
            throw new Exception("请指定合法的上传文件类型");
        }
        rePath+=nginxUrl;
        channelSftp.cd(nginxUrl);
        try {
            // 这个jb玩应竟然没有mkdir -p  气死我了
            String dst[] = dstDirPath.split("/");
            //循环判断生成目录
            for (String thisDst :
                    dst) {
                try {
                    channelSftp.cd(thisDst);
                } catch (Exception e) {
                    channelSftp.mkdir(thisDst);
                    channelSftp.cd(thisDst);
                }
            }
            //上传文件
            channelSftp.put(inputFile, fileName);
            rePath+="/"+dstDirPath+"/"+fileName;
        } catch (Exception e) {
            //即使这里不输出也会在上面输出的jojo！
            e.printStackTrace();
        } finally {
            //没多线程这事  可以放心finally
            //释放连接
            closeChannel();
            return  rePath;
        }
    }


}
