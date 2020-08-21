package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysLogsJzchnageDTO;

public interface SysLogsJzchnageDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogsJzchnageDTO record);

    int insertSelective(SysLogsJzchnageDTO record);

    SysLogsJzchnageDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogsJzchnageDTO record);

    int updateByPrimaryKey(SysLogsJzchnageDTO record);
}