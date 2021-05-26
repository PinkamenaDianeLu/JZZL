package com.util;/**
 * @author Mrlu
 * @createTime 2021/3/29
 * @describe
 */

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author MrLu
 * @createTime 2021/3/29 15:46
 * @describe
 */
public class FtpUtil {
    ChannelSftp sftp = null;

    /**
     * 连接sftp服务器
     *
     * @param host     主机
     * @param port     端口
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public ChannelSftp connect(String host, int port, String username,
                               String password) {

        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session session = jsch.getSession(username, host, port);
            //	System.out.println("Session created.");
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
            //	System.out.println("Session connected.");
            //	System.out.println("Opening Channel.");
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            //	System.out.println("Connected to " + host + ".");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return sftp;
    }

    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传文件
     *
     * @param directory  上传的目录
     * @param uploadFile 要上传的文件
     * @param sftp
     */
    public void upload(String directory, InputStream uploadFile, String fileName, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            //FileInputStream fis = new FileInputStream(uploadFile);
            sftp.put(uploadFile, fileName);
            //fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//	/**
//	 * 下载文件
//	 *
//	 * @param directory
//	 *            下载目录
//	 * @param downloadFile
//	 *            下载的文件
//	 * @param saveFile
//	 *            存在本地的路径
//	 * @param sftp
//	 */
//	public void download(String directory, String downloadFile,
//			String saveFile, ChannelSftp sftp) {
//		InputStream is = null;
//		try {
//			sftp.cd(directory);
//			File file = new File(saveFile);
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//			sftp.get(downloadFile, new FileOutputStream(file));
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}finally{
//			try {
//				is.close();
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//		}
//	}

    public void download(String directory, String downloadFile,
                         String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            sftp.get(downloadFile, new FileOutputStream(file));
        } catch (Exception e) {
            System.out.println("异常：" + downloadFile + e.getMessage());
            //e.printStackTrace();
        }
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    @SuppressWarnings("rawtypes")
    public Vector listFiles(String directory, ChannelSftp sftp)
            throws SftpException {
        //System.out.println("---"+directory);
        return sftp.ls(directory);
    }

    /**
     * 判断目录是否存在
     *
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    public boolean isDirExist(String directory, ChannelSftp sftp)
            throws SftpException {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            isDirExistFlag = false;
        }
        return isDirExistFlag;
    }

    public void disconnect() {
        try {
            if (null != sftp) {
                sftp.disconnect();
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sftp链接
     *
     * @param sf
     * @return
     * @throws
     */
    public static ChannelSftp getSftpConn(FtpUtil sf, String host, String port, String username, String pwd) {
        try {
            return sf.connect(host, Integer.parseInt(port), username, pwd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //throw new ServiceException("508");
        }
    }

    public static String uploadImage(String fileName, InputStream inputFile) {
        //ip:8080/ftpPath
        String rePath = "";
        try {
            Calendar now = Calendar.getInstance();

            FtpUtil sf = new FtpUtil();
            String username = GlobalUtil.getGlobal("ftpUserNameForWindow");//root
            String password = GlobalUtil.getGlobal("ftpPasswordForWindow");//123123
            String host = GlobalUtil.getGlobal("ftpHostForWindow");//10.112.57.160
            String port = GlobalUtil.getGlobal("ftpPortForWindow");
            String nginxUrl = GlobalUtil.getGlobal("ftpFileUrlForWindow");
            ChannelSftp sftp = FtpUtil.getSftpConn(sf, host, port, username, password);
            sftp.cd(nginxUrl);
            String[] fileUrls = { now.get(Calendar.YEAR) + "", ((now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DAY_OF_MONTH))};
            for (String thisUrl :
                    fileUrls) {
                boolean isExist = sf.isDirExist(thisUrl, sftp);//目录是否存在
                if (!isExist) {
                    //不存在就创建
                    sftp.mkdir(thisUrl);
                }
                sftp.cd(thisUrl);
            }
            sftp.put(inputFile, fileName);
            String completeUrl = Arrays.stream(fileUrls).collect(Collectors.joining("/", "", ""));
//            sf.upload("/", inputFile, fileName, sftp);
            rePath = completeUrl + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rePath;
    }

}
