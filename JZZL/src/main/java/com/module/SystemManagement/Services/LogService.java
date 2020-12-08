package com.module.SystemManagement.Services;


import com.bean.jzgl.DTO.SysLogsDTO;
import com.bean.jzgl.DTO.SysLogsLoginDTO;

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

      /**
      * 新建登录日志
      * @author MrLu
      * @param sysLogsLoginDTO
      * @createTime  2020/10/8 15:13
      * @return void   |
       */
     void insertLogLogin(SysLogsLoginDTO sysLogsLoginDTO);

    /**
     * 查询一名用户最后的登录日志
     * @author MrLu
     * @param sysuserid 用户id
     * @createTime  2020/12/8 15:26
     * @return    |
     */
    SysLogsLoginDTO selectPrevLogHistory(Integer sysuserid);

    /**
     * 将一名用户下的所有登录日志都标记为过去
     * @author MrLu
     * @param sysuserid 用户id
     * @createTime  2020/12/8 15:26
     * @return    |
     */
    void updateHistoryLog(Integer sysuserid);

}
