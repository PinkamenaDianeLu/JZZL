package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysLogsWebsocketDTO;

public interface SysLogsWebsocketDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogsWebsocketDTO record);

    int insertSelective(SysLogsWebsocketDTO record);

    SysLogsWebsocketDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogsWebsocketDTO record);

    int updateByPrimaryKey(SysLogsWebsocketDTO record);
}