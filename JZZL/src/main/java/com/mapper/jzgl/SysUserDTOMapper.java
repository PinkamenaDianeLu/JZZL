package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysUserDTO;
import org.apache.ibatis.annotations.Param;

public interface SysUserDTOMapper {


    int insertSelective(SysUserDTO record);

    SysUserDTO selectByPrimaryKey(Integer id);
    SysUserDTO selectJzUserByAzId(Integer oriid);
    SysUserDTO selectJzUserByIdCard(String idcard);
    SysUserDTO selectByOriId(Integer oriid);

    int updateByPrimaryKeySelective(SysUserDTO record);

    SysUserDTO login(@Param("username") String username, @Param("pwd") String password);

    SysUserDTO loginNoPasswd(String username);
    //查询法制员角色

}