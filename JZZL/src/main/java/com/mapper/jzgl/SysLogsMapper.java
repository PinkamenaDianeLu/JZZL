package com.mapper.jzgl;

import com.bean.jzgl.SysLogs;

public interface SysLogsMapper {
     /**
     * 插入日志
     * @author Mrlu
     * @param  record
     * @createTime  2020/8/14 10:40
     * @return  int  |
      */
    int insertSelective(SysLogs record);

     /**
     * 根据主键查询日志
     * @author Mrlu
     * @param id 主键id
     * @createTime  2020/8/14 10:40
     * @return  SysLogs  |
      */
    SysLogs selectByPrimaryKey(Integer id);
}