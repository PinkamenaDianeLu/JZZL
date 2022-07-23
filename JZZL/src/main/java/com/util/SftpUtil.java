package com.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

/**
 * 连接 上传ftp 操作
 *
 * @author MrLu
 * @createTime 2020/4/29
 * @describe 连接 上传ftp
 */
public class SftpUtil {


    final static private int timeout = 600000; //超时数,一分钟
//    private static Session session;

    /**
     * @author MrLu
     * @createTime 2020/4/29 23:00
     * @describe 创建channel
     * @version 1.0
     */
    private static ChannelSftp getChannel(String username, String password, String ip, int port) throws Exception {
        JSch jsch = new JSch(); // 创建JSch对象
        // 根据用户名，主机ip，端口获取一个Session对象
        Session session = jsch.getSession(username, ip, port);
        if (password != null) {
            session.setPassword(password); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        Channel channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(60*1000); // 建立SFTP通道的连接
//        session.disconnect();
        return (ChannelSftp) channel;
    }


    private static ChannelSftp getChannel() throws Exception {
        final String username = GlobalUtil.getGlobal("ftpUserNameForLinux");
        final String password = GlobalUtil.getGlobal("ftpPasswordForLinux");
        final String host = GlobalUtil.getGlobal("ftpHostForLinux");
        final int port = Integer.parseInt(GlobalUtil.getGlobal("ftpPortForLinux"));

        JSch jsch = new JSch(); // 创建JSch对象
        // 根据用户名，主机ip，端口获取一个Session对象
        Session session = jsch.getSession(username, host, port);
        if (password != null) {
            session.setPassword(password); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        Channel channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(60 * 1000); // 建立SFTP通道的连接
        return (ChannelSftp) channel;
    }

    /**
     * 关闭channel和session
     *
     * @throws Exception
     * @author MrLu
     */
    private static void closeChannel(ChannelSftp channelSftp) throws Exception {
        if (channelSftp != null) {
            channelSftp.disconnect();
            if (channelSftp.isClosed()) {
     //              System.out.println("关闭完成");
            }

            if (null != channelSftp.getSession() && channelSftp.getSession().isConnected()) {
                channelSftp.getSession().disconnect();
            }
        }

//        if (channel != null) {
//            channel.disconnect();
//        }
//        if (session != null) {
//            session.disconnect();
//        }
    }

    /**
     * @param fileName   文件名
     * @param dstDirPath 上传文件路径
     * @param fileType   上传的文件类型 不可为空
     * @param inputFile  上传的文件
     * @return rePath 文件在服务器的完整路径
     * @author MrLu
     * @createTime 2020/4/29 23:06
     * @describe
     * @version 1.0
     */
    public static String upLoad(String fileName, String dstDirPath, String fileType, InputStream inputFile) throws Exception {
        if (StringUtils.isEmpty(fileType)) throw new Exception("请指定上传文件类型");
        String rePath = "";//返回文件的完整路径
        String username = GlobalUtil.getGlobal("ftpUserNameForLinux");
        String password = GlobalUtil.getGlobal("ftpPasswordForLinux");
        String host = GlobalUtil.getGlobal("ftpHostForLinux");
        int port = Integer.parseInt(GlobalUtil.getGlobal("ftpPortForLinux"));
        String nginxUrl = "";
        if ("img".equals(fileType)) {
            nginxUrl = GlobalUtil.getGlobal("nginxImgUrl");
        } else if ("others".equals(fileType)) {
            nginxUrl = "/";
        } else {
            throw new Exception("请指定合法的上传文件类型");
        }
        rePath += nginxUrl;

     /*   JSch jsch = new JSch(); // 创建JSch对象
        // 根据用户名，主机ip，端口获取一个Session对象
        Session session = jsch.getSession(username, host, port);
        if (password != null) {
            session.setPassword(password); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(timeout); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        Channel channel = session.openChannel("sftp"); // 打开SFTP通道*/
        ChannelSftp channelSftp = null;
        try {
            channelSftp = getChannel(username, password, host, port);

//            channel.connect(60 * 10); // 建立SFTP通道的连接


//            channelSftp = (ChannelSftp) channel;
            channelSftp.cd(GlobalUtil.getGlobal("ftpFileUrlForLinux"));
            // 这个jb玩应竟然没有mkdir -p  气死我了
            String[] dst = dstDirPath.split("/");
            //循环判断生成目录
            for (String thisDst : dst) {
                try {
                    channelSftp.cd(thisDst);
                } catch (Exception e) {
                    channelSftp.mkdir(thisDst);
                    channelSftp.cd(thisDst);
                }
            }
            //上传文件
            channelSftp.put(inputFile, fileName);
            rePath += "" + dstDirPath + "/" + fileName;
  /*      } catch (Exception e) {
            //即使这里不输出也会在上面输出的jojo！
            inputFile.close();
            closeChannel(channelSftp);
            e.printStackTrace();
        }*/
            //没多线程这事  可以放心finally
            //释放连接


        } catch (Exception e) {
            e.printStackTrace();
            rePath=null;
        } finally {
            inputFile.close();
            closeChannel(channelSftp);
//            session.disconnect();
        }

        return rePath;
    }



    /**
     * 批量上传
     * @author MrLu
     * @param newFile 上传的文件
     * @createTime  2022/5/23 9:51
     * @version 1.0
     * @return    |
     */
    public static JSONObject uploadList(MultipartFile[] newFile) throws Exception {

        if (null == newFile || newFile.length == 0) {
            throw new Exception("无上传数据");
        }


        Map<String, imageUtil> reMap = new HashMap<>();
        String AbsolutePath = GlobalUtil.getGlobal("ftpFileUrlForLinux");//服务器的存放路径
        //根据不同的上传类型判断在服务器上不同的存放路径

        //
        //创建上传路径 年/月/日
        Calendar now = Calendar.getInstance();
        //相对路径
        String RelativePath
                = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH);
        int i= 0;
        for (MultipartFile thisFile :
                newFile) {
            String orgFileName = thisFile.getOriginalFilename();//图片名称
            String orgFileSuffix = orgFileName.substring(orgFileName.lastIndexOf("."));
            // 图片名称
            String fileName = UUID.randomUUID() + orgFileSuffix;
            imageUtil imageUtil = new imageUtil();
            imageUtil.setShunxu(i);
            i++;
            imageUtil.setInputStream(thisFile.getInputStream());
            reMap.put(fileName, imageUtil);
            //nginx读取文件为    nginxUrl+/年/月/日+文件名
        }
        //开传
        return upLoadList(AbsolutePath, RelativePath, reMap);

    }


    /**
     * 批量上传
     *
     * @param AbsolutePath 服务器绝对路径
     * @param RelativePath 服务器相对路径
     * @param fileMap 由uploadList方法处理的map
     * @return |
     * @author MrLu
     * @createTime 2022/5/20 14:13
     * @version 1.0
     */
    public static JSONObject upLoadList(final String AbsolutePath,final String RelativePath, Map<String, imageUtil> fileMap) throws Exception {
        JSONObject reObj = null;
        if (null == fileMap || fileMap.size() == 0) throw new Exception("无上传文件数据");
        ChannelSftp channelSftp = null;
        try {
            String[] dst = RelativePath.split("/");
            channelSftp = getChannel(); // 打开SFTP通道
            channelSftp.cd(AbsolutePath);
            // 这个jb玩应竟然没有mkdir -p  气死我了

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
            //开传
            reObj = startUploadList(channelSftp, AbsolutePath ,RelativePath, fileMap);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            closeChannel(channelSftp);
        }
        return reObj;
    }
    /**
     * 开传
     * @author MrLu
     * @param
     * @createTime  2022/5/23 9:54
     * @version 1.0
     * @return    |
     */
    public static JSONObject startUploadList(ChannelSftp channelSftp,String AbsolutePath ,String RelativePath, Map<String, imageUtil> fileMap) {
        final String host=GlobalUtil.getGlobal("nginxFinUrl");
        JSONObject reObj = new JSONObject();
        reObj.put("errno", 0);//默认没有异常
        JSONArray resAry = new JSONArray();

        Set<String> set = fileMap.keySet();
        for (String key : set) {
            JSONObject fileObj = new JSONObject();
            try (InputStream is = fileMap.get(key).getInputStream()) {
                fileObj.put("shunxu", fileMap.get(key).getShunxu());//文件名
                fileObj.put("name", key);//文件名
                fileObj.put("href", RelativePath + "/" + key);//图片路径
                fileObj.put("host",host);//图片服务器位置
                channelSftp.put(is, key);//开传
                fileObj.put("state", "success");
            } catch (Exception e) {
                fileObj.put("state", "error");
                reObj.put("errno", 1);//有异常
                e.printStackTrace();
            } finally {
                //关闭上传流  记录上传情况
                resAry.add(fileObj);
            }
        }
        reObj.put("data", resAry);
        return reObj;
    }
}
