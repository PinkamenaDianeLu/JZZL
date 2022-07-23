package com.mapper.jzgl;


import com.bean.jzgl.DTO.SysLogsWebsocketDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLogsWebsocketDTOMapper {


    int insertSelective(SysLogsWebsocketDTO record);

    SysLogsWebsocketDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogsWebsocketDTO record);
     /**
     * 根据用户查询消息
     * @author MrLu
     * @param isreceived 是否已读
      * @param receiver username
     * @createTime  2020/12/18 15:00
     * @return    |
      */
    List<SysLogsWebsocketDTO> selectMessageByUsername(int isreceived,String receiver);

     /**
     * 将信息标注为已读
     * @author MrLu
     * @param  ids
     * @createTime  2020/12/18 16:33
     * @return    |
      */
    void readMessage(@Param("ids") String[] ids);


     /**
     * 全部已读
     * @author MrLu
     * @param
     * @createTime  2021/12/17 16:28
     * @return    |
      */
    void readMessageAll(String receiver);
}