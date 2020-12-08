package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysLogsLoginDTO;

public interface SysLogsLoginDTOMapper {

    int insert(SysLogsLoginDTO record);

    SysLogsLoginDTO selectByPrimaryKey(Integer id);

     /**
     * 将一名用户下的所有登录日志都标记为过去
     * @author MrLu
     * @param sysuserid 用户id
     * @createTime  2020/12/8 15:26
     * @return    |
      */
    void updateHistoryLog(Integer sysuserid);

     /**
     * 查询一名用户最后的登录日志
     * @author MrLu
     * @param sysuserid 用户id
     * @createTime  2020/12/8 15:26
     * @return    |
      */
    SysLogsLoginDTO selectPrevLogHistory(Integer sysuserid);

}