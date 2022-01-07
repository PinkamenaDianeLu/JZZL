package com.mapper.jzgl;


import com.bean.jzgl.DTO.SysRoleUserDTO;
import com.bean.jzgl.Source.SysRoleUser;

import java.util.List;

public interface SysRoleUserMapper {

    int insert(SysRoleUserDTO record);


    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUserDTO record);


     /**
     * 删除某个用户的所有权限
     * @author MrLu
     * @param sysuserid 用户id
     * @createTime  2021/6/9 14:12
      */
    void deleteRoles(Integer sysuserid);


     /**
     * 查询用户的权限
     * @author MrLu
     * @param sysuserid 用户id
     * @createTime  2021/6/11 10:57
     * @return   List<SysRoleUser> |
      */
    List<SysRoleUser> selectRoleByUserid(Integer sysuserid);
}