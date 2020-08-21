package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysLogsSendDTO;

public interface SysLogsSendDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogsSendDTO record);

    int insertSelective(SysLogsSendDTO record);

    SysLogsSendDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogsSendDTO record);

    int updateByPrimaryKey(SysLogsSendDTO record);
}