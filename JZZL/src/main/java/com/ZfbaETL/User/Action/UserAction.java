package com.ZfbaETL.User.Action;

import com.ZfbaETL.BaseServer.BaseServer;
import com.ZfbaETL.User.Server.UserCityServer;
import com.ZfbaETL.User.Server.UserServer;
import com.bean.jzgl.DTO.EtlLogsDTO;
import com.bean.jzgl.DTO.EtlTablelogDTO;
import com.bean.jzgl.DTO.SysRoleUserDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.thkjdmtjz.SysUserCity;
import com.bean.thkjdmtjz.SysUserRoleCity;
import com.bean.zfba.SysUser;
import com.bean.zfba.SysUserRole;
import com.enums.EnumSoft;
import com.util.GlobalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private UserCityServer userCityServer;
    @Autowired
    private BaseServer baseServer;

    @Override
    public void run(String... args) throws Exception {
//        ImportUsers();
//        ImportUsers();
  /*      String groupcode = GlobalUtil.getGlobal("groupcode");//查询的单位代码

        UpdateUsers();*/
//        updateUserRole(groupcode);


  /*      String version = GlobalUtil.getGlobal("version");//查询版本
        //判断版本
        if("province".equals(version)){
            //省厅版本   以案宗的用户为主
            ImportUsers();
            UpdateUsers();
        }else {
            //地市版本
            ImportUsers_City();
            UpdateUsers_City();
        }*/
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
  //  @Scheduled(cron = "0 1/1 * * * ?")
    //或直接指定时间间隔，例如：5秒
   // @Scheduled(fixedRate=3600000)//每小时
    public void ImportUsers() {
        if ("1".equals(GlobalUtil.getGlobal("startEtl"))){
            return;
        }
        String groupCode = GlobalUtil.getGlobal("groupcode");
        EtlTablelogDTO lastV = baseServer.selectLastValue("SYS_USER", "ID");
        List<SysUser> newUsers = userServer.selectNewSysuser(lastV.getLastpknumvalue(), groupCode);
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
                    //确实用户确实没有 需要新增
                    SysUserDTO newUser = userServer.selectByOriId(thisUser.getId());
                    if(null==newUser){
                        newUser=new  SysUserDTO();//系统没有 确实新增
                    }else{
                        continue;//用户已存在 下一位！
                    }
                    newUser.setUsername(thisUser.getUsername());
                    newUser.setPassword("MjAyY2I5NjJhYzU5MDc1Yjk2NGIwNzE1MmQyMzRiNzA=");//md5的123
                    newUser.setOrigin("案宗导入");
                    newUser.setPhone(thisUser.getDwlxfs());
                    newUser.setIdcardnumber(thisUser.getUsername());
                    newUser.setXm(thisUser.getXm());
                    newUser.setAgencycode(thisUser.getDwmcdm());
                    newUser.setAgencyname(thisUser.getDwmcdm());
                    newUser.setOriid(thisUser.getId());
                    userServer.insertNewUser(newUser);

                    List<SysUserRole> userRoles = userServer.selectUserRole(thisUser.getId());//查询用户的权限
                    if (null != userRoles && userRoles.size() > 0) {
                        //更新用户的信息

                        //更新权限信息
                        String roles = userRoles.stream().map(SysUserRole::getRoleid).map(String::valueOf).collect(Collectors.joining(",", ",", ","));//将用户的权限转换成一个以，分割的字符串
                        //删除原有的权限
                        userServer.deleteRoles(newUser.getId());
                        SysRoleUserDTO newUserRole = new SysRoleUserDTO();
                        newUserRole.setSysuserid(newUser.getId());//用户表id
                        newUserRole.setUsername(newUser.getUsername());//用户名

                        if (roles.contains(",81,") || roles.contains(",82,")) {
                            //添加法制员权限
                            newUserRole.setRolecode(EnumSoft.sysRole.B1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.B1.getName());
                            userServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",41,") || roles.contains(",25,")) {
                            //添加领导权限
                            newUserRole.setRolecode(EnumSoft.sysRole.A1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.A1.getName());
                            userServer.insertUserRole(newUserRole);
                        }
                        //所有人都是办案人  都拥有办案人权限
                        newUserRole.setRolecode(EnumSoft.sysRole.D1.getValue());
                        newUserRole.setRolename(EnumSoft.sysRole.D1.getName());
                        userServer.insertUserRole(newUserRole);
                    }
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

     /**
     * 地市版本新增用户
     * @author MrLu
     * @param
     * @createTime  2021/8/17 9:15
     * @return    |
      */
//     @Scheduled(cron = "0 1/1 * * * ?")
    public  void ImportUsers_City(){
         if ("1".equals(GlobalUtil.getGlobal("startEtl"))){
             return;
         }
        EtlTablelogDTO lastV = baseServer.selectLastValue("SYS_USER", "ID");
        List<SysUserCity> newUsers=  userCityServer.selectNewSysuser(lastV.getLastpknumvalue());

        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());
        record.setLastpkstrvalue(lastV.getLastpknumvalue() + "");
        int insertCount = 0;
        if (null != newUsers && newUsers.size() > 0) {
            for (SysUserCity thisUser :
                    newUsers) {
                try {

                    //根据身份证号查询是否有这个用户
                    SysUserDTO newUser=userCityServer.selectJzUserByIdCard(thisUser.getUsername());
                    //如果有这个用户已经有了就不抽了
                    if (null!=newUser&& newUser.getId()!=null){
                        continue;
                    }else {
                        newUser=new SysUserDTO();
                    }
                    final  Integer oriid=Integer.parseInt(thisUser.getId().toString());
                    newUser.setUsername(thisUser.getUsername());
                    newUser.setPassword("MjAyY2I5NjJhYzU5MDc1Yjk2NGIwNzE1MmQyMzRiNzA=");//md5的123
                    newUser.setOrigin("地市系统导入");
                    newUser.setPhone(thisUser.getPhone());
                    newUser.setIdcardnumber(thisUser.getUsername());
                    newUser.setXm(thisUser.getXm());
                    newUser.setAgencycode(thisUser.getDwmcdm());
                    newUser.setAgencyname(thisUser.getDwmcdm());
                    newUser.setOriid(oriid);//这里存的是抽取系统的id 并不是案宗的id
                    userCityServer.insertNewUser(newUser);

                    List<SysUserRoleCity> userRoles = userCityServer.selectUserRole(oriid);//查询用户的权限
                    if (null != userRoles && userRoles.size() > 0) {
                        //更新用户的信息

                        //更新权限信息
                        String roles = userRoles.stream().map(SysUserRoleCity::getRoleid).map(String::valueOf).collect(Collectors.joining(",", ",", ","));//将用户的权限转换成一个以，分割的字符串
                        //删除原有的权限
                        userCityServer.deleteRoles(newUser.getId());
                        SysRoleUserDTO newUserRole = new SysRoleUserDTO();
                        newUserRole.setSysuserid(newUser.getId());//用户表id
                        newUserRole.setUsername(newUser.getUsername());//用户名

                        if (roles.contains(",2,")) {
                            //添加法制员权限
                            newUserRole.setRolecode(EnumSoft.sysRole.B1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.B1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",9,")||roles.contains(",10,")|roles.contains(",11,")) {
                            //添加安管员权限
                            newUserRole.setRolecode(EnumSoft.sysRole.F1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.F1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",5,")) {
                            //添加副队长权限
                            newUserRole.setRolecode(EnumSoft.sysRole.C1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.C1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",6,") ) {
                            //添加领导权限
                            newUserRole.setRolecode(EnumSoft.sysRole.A1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.A1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        //所有人都是办案人  都拥有办案人权限
                        newUserRole.setRolecode(EnumSoft.sysRole.D1.getValue());
                        newUserRole.setRolename(EnumSoft.sysRole.D1.getName());
                        userCityServer.insertUserRole(newUserRole);
                    }
                    //更新最后更新的值
                    lastV.setLastpknumvalue(oriid);
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

    /**
     * 更新用户
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 14:52
     */
  //  @Scheduled(cron =  "0 15 10 * * ?")
    public void UpdateUsers() {

        if ("1".equals(GlobalUtil.getGlobal("startEtl"))){
            return;
        }
        String groupCode = GlobalUtil.getGlobal("groupcode");
        EtlTablelogDTO lastV = baseServer.selectLastValue("SYS_USER", "GXSJ");
        List<SysUser> updateUsers = userServer.selectUpdateSysuser(lastV.getLastpkdatevalue(), groupCode);
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());
        record.setLastpkstrvalue(lastV.getLastpknumvalue() + "");
        if (null != updateUsers && updateUsers.size() > 0) {
            int insertCount = 0;

            for (SysUser thisUser :
                    updateUsers) {
                try {
                    SysUserDTO newUser = userServer.selectByOriId(thisUser.getId());
                    List<SysUserRole> userRoles = userServer.selectUserRole(thisUser.getId());//查询用户的权限
                    if (null != newUser && null != userRoles && userRoles.size() > 0) {
                        //更新用户的信息
                        newUser.setUsername(thisUser.getUsername());
                        newUser.setPassword(thisUser.getUsername());
                        newUser.setPhone(thisUser.getDwlxfs());
                        newUser.setIdcardnumber(thisUser.getUsername());
                        newUser.setXm(thisUser.getXm());
                        newUser.setAgencycode(thisUser.getDwmcdm());
                        newUser.setAgencyname(thisUser.getDwmcdm());
                        userServer.updateUser(newUser);


                        //更新权限信息
                        String roles = userRoles.stream().map(SysUserRole::getRoleid).map(String::valueOf).collect(Collectors.joining(",", ",", ","));//将用户的权限转换成一个以，分割的字符串
                        //删除原有的权限
                        userServer.deleteRoles(newUser.getId());
                        SysRoleUserDTO newUserRole = new SysRoleUserDTO();
                        newUserRole.setSysuserid(newUser.getId());//用户表id
                        newUserRole.setUsername(newUser.getUsername());//用户名

                        if (roles.contains(",81,") || roles.contains(",82,")) {
                            //添加法制员权限
                            newUserRole.setRolecode(EnumSoft.sysRole.B1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.B1.getName());
                            userServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",41,") || roles.contains(",25,")) {
                            //添加领导权限
                            newUserRole.setRolecode(EnumSoft.sysRole.A1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.A1.getName());
                            userServer.insertUserRole(newUserRole);
                        }
                        //所有人都是办案人  都拥有办案人权限
                        newUserRole.setRolecode(EnumSoft.sysRole.D1.getValue());
                        newUserRole.setRolename(EnumSoft.sysRole.D1.getName());
                        userServer.insertUserRole(newUserRole);
                        //更新最后更新的值
                        lastV.setLastpknumvalue(thisUser.getId());
                        lastV.setLastpkdatevalue(new Date());
                        baseServer.updateLastValue(lastV);
                        insertCount++;
                    }
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

     /**
     * 地市版本更新用户
     * @author MrLu
     * @param
     * @createTime  2021/8/17 10:37
     * @return    |
      */
//     @Scheduled(cron =  "0 15 10 * * ?")
    public void UpdateUsers_City() {
        if ("1".equals(GlobalUtil.getGlobal("startEtl"))){
            return;
        }
        EtlTablelogDTO lastV = baseServer.selectLastValue("SYS_USER", "GXSJ");
        List<SysUserCity> updateUsers = userCityServer.selectUpdateSysuser(lastV.getLastpkdatevalue());
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());
        record.setLastpkstrvalue(lastV.getLastpknumvalue() + "");
        if (null != updateUsers && updateUsers.size() > 0) {
            int insertCount = 0;

            for (SysUserCity thisUser :
                    updateUsers) {
                try {
                    final  Integer oriid=Integer.parseInt(thisUser.getId().toString());
                    SysUserDTO newUser = userCityServer.selectByOriId(oriid);
                    List<SysUserRoleCity> userRoles = userCityServer.selectUserRole(oriid);//查询用户的权限
                    if (null != newUser && null != userRoles && userRoles.size() > 0) {
                        //更新用户的信息
                        newUser.setUsername(thisUser.getUsername());
                        newUser.setPassword(thisUser.getUsername());
                        newUser.setPhone(thisUser.getPhone());
                        newUser.setIdcardnumber(thisUser.getUsername());
                        newUser.setXm(thisUser.getXm());
                        newUser.setAgencycode(thisUser.getDwmcdm());
                        newUser.setAgencyname(thisUser.getDwmcdm());
                        userServer.updateUser(newUser);


                        //更新权限信息
                        String roles = userRoles.stream().map(SysUserRoleCity::getRoleid).map(String::valueOf).collect(Collectors.joining(",", ",", ","));//将用户的权限转换成一个以，分割的字符串
                        //删除原有的权限
                        userServer.deleteRoles(newUser.getId());
                        SysRoleUserDTO newUserRole = new SysRoleUserDTO();
                        newUserRole.setSysuserid(newUser.getId());//用户表id
                        newUserRole.setUsername(newUser.getUsername());//用户名
                        if (roles.contains(",2,")) {
                            //添加法制员权限
                            newUserRole.setRolecode(EnumSoft.sysRole.B1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.B1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",9,")||roles.contains(",10,")|roles.contains(",11,")) {
                            //添加安管员权限
                            newUserRole.setRolecode(EnumSoft.sysRole.F1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.F1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",5,")) {
                            //添加副队长权限
                            newUserRole.setRolecode(EnumSoft.sysRole.C1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.C1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        if (roles.contains(",6,") ) {
                            //添加领导权限
                            newUserRole.setRolecode(EnumSoft.sysRole.A1.getValue());
                            newUserRole.setRolename(EnumSoft.sysRole.A1.getName());
                            userCityServer.insertUserRole(newUserRole);
                        }
                        //所有人都是办案人  都拥有办案人权限
                        newUserRole.setRolecode(EnumSoft.sysRole.D1.getValue());
                        newUserRole.setRolename(EnumSoft.sysRole.D1.getName());
                        userServer.insertUserRole(newUserRole);
                        //更新最后更新的值
                        lastV.setLastpknumvalue(oriid);
                        lastV.setLastpkdatevalue(new Date());
                        baseServer.updateLastValue(lastV);
                        insertCount++;
                    }
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
