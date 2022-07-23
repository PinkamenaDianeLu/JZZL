package com.mapper.zfba;

import com.bean.zfba.SysUser;
import com.bean.zfba.SysUserRole;

import java.util.Date;
import java.util.List;

public interface SysUserMapper {


    SysUser selectByPrimaryKey(Short userId);


 /**
 * 查询新增的
 * @author MrLu
 * @param 
 * @createTime  2021/1/4 14:19
 * @return    |  --  WHERE  ROWNUM &lt; 100
  */
   List<SysUser> selectNewSysuser(Integer userId,String dwmcdm);
    /**
    * 查询更新的
    * @author MrLu
    * @param 
    * @createTime  2021/1/4 14:26
    * @return    |  
     */
    List<SysUser> selectUpdateSysuser(Date gxsj,String dwmcdm);


     /**
     * 查询这个单位的当权者和法制科成员
     * @author MrLu
     * @param 
     * @createTime  2021/1/5 9:32
     * @return    |  
      */
    List<SysUser> selectLeaderByDwdm(String dwmcdm);


     /**
     * 查询用户的权限
     * @author MrLu
     * @param
     * @createTime  2021/6/9 10:51
     * @return    |
      */
    List<SysUserRole> selectUserRole(Integer userid);



}