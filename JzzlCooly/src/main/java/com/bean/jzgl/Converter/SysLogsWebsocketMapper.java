package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.SysLogsWebsocketDTO;
import com.bean.jzgl.Source.SysLogsWebsocket;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * 
 * @author MrLu
 * @createTime  2020/8/24 11:18
  * @describe  webSocket日志映射
  */
@Mapper
public interface SysLogsWebsocketMapper {
    SysLogsWebsocketMapper INSTANCE = Mappers.getMapper(SysLogsWebsocketMapper.class);


    @Mapping(target = "messagetype", expression = "java(Enum1ToInt(source.getMessagetype()))")
    SysLogsWebsocketDTO  SourceToDTO(SysLogsWebsocket source);

    default int Enum1ToInt(Enums.WebSocketMessageType webSocketMessageType) {
        return webSocketMessageType.getValue();
    }

    @Mapping(target = "messagetype", expression = "java(messagetypeToEnum(dto.getMessagetype()))")
    SysLogsWebsocket DTOToSource(SysLogsWebsocketDTO dto);

    default Enums.WebSocketMessageType messagetypeToEnum(int messagetype) {
        return Objects.requireNonNull(EnumsUtil.getEnumByValue(Enums.WebSocketMessageType.class, messagetype ));
    }

    List<SysLogsWebsocket> DTOToSources(List<SysLogsWebsocketDTO> dtos);
}
