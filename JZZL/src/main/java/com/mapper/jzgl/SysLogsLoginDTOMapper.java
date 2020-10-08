package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysLogsLoginDTO;

public interface SysLogsLoginDTOMapper {

    int insert(SysLogsLoginDTO record);

    SysLogsLoginDTO selectByPrimaryKey(Integer id);


}