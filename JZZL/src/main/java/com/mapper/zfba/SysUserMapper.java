package com.mapper.zfba;

import com.bean.zfba.SysUser;

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
   List<SysUser> selectNewSysuser(Integer userId);
    /**
    * 查询更新的
    * @author MrLu
    * @param 
    * @createTime  2021/1/4 14:26
    * @return    |  
     */
    List<SysUser> selectUpdateSysuser(Date gxsj);


     /**
     * 查询这个单位的当权者
     * @author MrLu
     * @param 
     * @createTime  2021/1/5 9:32
     * @return    |  
      */
    List<SysUser> selectLeaderByDwdm(String dwmcdm);
}