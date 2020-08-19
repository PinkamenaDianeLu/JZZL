package com.module.SystemManagement.Services;


import com.bean.jzgl.DTO.SysLogsDTO;

/**
 * @author MrLu
 * @createTime 2020/5/19
 * @describe 日志操作
 */

public interface LogService {
     /**
     * @author MrLu
     * @param  record 日志对象
     * @createTime  2020/5/19 20:05
     * @describe 记录日志
     * @version 1.0
      */
     void insertLog(SysLogsDTO record);
}
