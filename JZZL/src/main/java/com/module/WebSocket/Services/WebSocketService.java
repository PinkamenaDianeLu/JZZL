package com.module.WebSocket.Services;

import com.bean.jzgl.DTO.SysLogsWebsocketDTO;
import com.config.webSocket.WebSocketMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/12/18 10:03
 * @describe
 */
public interface WebSocketService {

     /**
     * 新建日志
     * @author MrLu
     * @param message 消息
      *   @param isSend 是否已发送
     * @createTime  2020/12/18 11:00
     * @return    |  
      */
      void insertWebSocketLog(WebSocketMessage message,boolean isSend);


       /**
       * 根据用户名查询未读的信息
       * @author MrLu
       * @param
       * @createTime  2020/12/18 15:02
       * @return    |
        */
      List<SysLogsWebsocketDTO> selectUnreadMessageByUsername(String username);

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
