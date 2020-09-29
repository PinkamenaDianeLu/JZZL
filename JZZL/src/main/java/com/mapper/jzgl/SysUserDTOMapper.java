package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysUserDTO;
import org.apache.ibatis.annotations.Param;

public interface SysUserDTOMapper {


    int insertSelective(SysUserDTO record);

    SysUserDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUserDTO record);

    SysUserDTO login(@Param("username") String username, @Param("pwd") String password);

}