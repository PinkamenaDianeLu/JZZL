package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysLogsDTO;

public interface SysLogsMapper {
     /**
     * 插入日志
     * @author MrLu
     * @param  record
     * @createTime  2020/8/14 10:40
     * @return  int  |
      */
    int insertSelective(SysLogsDTO record);

     /**
     * 根据主键查询日志
     * @author MrLu
     * @param id 主键id
     * @createTime  2020/8/14 10:40
     * @return  SysLogs  |
      */
    SysLogsDTO selectByPrimaryKey(Integer id);
}