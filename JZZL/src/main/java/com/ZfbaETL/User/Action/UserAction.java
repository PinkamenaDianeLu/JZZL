package com.ZfbaETL.User.Action;

import com.ZfbaETL.BaseServer.BaseServer;
import com.ZfbaETL.User.Server.UserServer;
import com.bean.jzgl.DTO.EtlLogsDTO;
import com.bean.jzgl.DTO.EtlTablelogDTO;
import com.bean.jzgl.DTO.SysBmbDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.zfba.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/4 14:22
 * @describe 用于同步用户机构等数据
 */
@Configuration
@EnableScheduling
public class UserAction implements CommandLineRunner {
    @Autowired
    private UserServer userServer;
    @Autowired
    private BaseServer baseServer;

    @Override
    public void run(String... args) throws Exception {
        ImportUsers();
//        UpdateUsers();
    }

    /**
     * 新建用户
     * 从一分钟开始 每分钟都触发
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 14:52
     */
//    @Scheduled(cron = "0 1/1 * * * ?")
    //或直接指定时间间隔，例如：5秒
//    @Scheduled(fixedRate=60000)
    public void ImportUsers() {
        EtlTablelogDTO lastV = baseServer.selectLastValue("SYS_USER", "ID");
        List<SysUser> newUsers = userServer.selectNewSysuser(lastV.getLastpknumvalue());
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());
        record.setLastpkstrvalue(lastV.getLastpknumvalue() + "");
        int insertCount = 0;
        if (null != newUsers && newUsers.size() > 0) {
            for (SysUser thisUser :
                    newUsers) {
                try {
                    SysUserDTO newUser = new SysUserDTO();
                    newUser.setUsername(thisUser.getUsername());
                    newUser.setPassword(thisUser.getUsername());
                    newUser.setOrigin("案宗导入");
                    newUser.setPhone(thisUser.getDwlxfs());
                    newUser.setIdcardnumber(thisUser.getUsername());
                    newUser.setXm(thisUser.getXm());
                    newUser.setAgencycode(thisUser.getDwmcdm());
                    newUser.setAgencyname(thisUser.getDwmcdm());
                    newUser.setOriid(thisUser.getId());
                    userServer.insertNewUser(newUser);
                    //更新最后更新的值
                    lastV.setLastpknumvalue(thisUser.getId());
                    baseServer.updateLastValue(lastV);
                    insertCount++;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    //插入失败日志
                    baseServer.insertErrorLog(record, ignored.getMessage(), thisUser.getId() + "");
                    continue;
                }

            }
            //插入成功日志
            baseServer.insertSuccessLog(record, insertCount);

        } else {
            System.out.println("没有可更新的值");
            baseServer.insertSuccessLog(record, 0);
        }

    }

    /**
     * 更新用户
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 14:52
     */
    @Scheduled(cron = "0 0 10 * * ?")
    public void UpdateUsers() {
        EtlTablelogDTO lastV = baseServer.selectLastValue("SYS_USER", "GXSJ");
        List<SysUser> updateUsers = userServer.selectUpdateSysuser(lastV.getLastpkdatevalue());
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setLastpkname(lastV.getLastpkname());
        if (null != updateUsers && updateUsers.size() > 0) {
            int insertCount = 0;

            for (SysUser thisUser :
                    updateUsers) {
                try {
                    SysUserDTO newUser = userServer.selectByOriId(thisUser.getId());
                    if (null != newUser) {
                        newUser.setUsername(thisUser.getUsername());
                        newUser.setPassword(thisUser.getUsername());
                        newUser.setPhone(thisUser.getDwlxfs());
                        newUser.setIdcardnumber(thisUser.getUsername());
                        newUser.setXm(thisUser.getXm());
                        newUser.setAgencycode(thisUser.getDwmcdm());
                        newUser.setAgencyname(thisUser.getDwmcdm());
                    }

                    userServer.updateUser(newUser);
                    //更新最后更新的值
                    lastV.setLastpknumvalue(thisUser.getId());
                    baseServer.updateLastValue(lastV);
                    insertCount++;
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    //插入失败日志
                    baseServer.insertErrorLog(record, ignored.getMessage(), thisUser.getId() + "");
                }
            }
            //插入成功日志
            baseServer.insertSuccessLog(record, insertCount);
        } else {
            System.out.println("没有可更新的值");
            baseServer.insertSuccessLog(record, 0);
        }
    }


}
