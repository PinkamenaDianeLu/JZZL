package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.SysLogsWebsocketDTO;
import com.bean.jzgl.Source.SysLogsWebsocket;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-08-25T09:30:32+0800",
    comments = "version: 1.3.1.Final, compiler: BatchProcessingEnvImpl from aspectjtools-1.8.13.jar, environment: Java 1.8.0_251 (Oracle Corporation)"
)
public class SysLogsWebsocketMapperImpl implements SysLogsWebsocketMapper {

    @Override
    public SysLogsWebsocketDTO SourceToDTO(SysLogsWebsocket source) {
        if ( source == null ) {
            return null;
        }

        SysLogsWebsocketDTO sysLogsWebsocketDTO = new SysLogsWebsocketDTO();

        sysLogsWebsocketDTO.setId( source.getId() );
        sysLogsWebsocketDTO.setScbj( source.getScbj() );
        sysLogsWebsocketDTO.setState( source.getState() );
        sysLogsWebsocketDTO.setCreatetime( source.getCreatetime() );
        sysLogsWebsocketDTO.setUpdatetime( source.getUpdatetime() );
        sysLogsWebsocketDTO.setSender( source.getSender() );
        sysLogsWebsocketDTO.setReceiver( source.getReceiver() );
        sysLogsWebsocketDTO.setServerip( source.getServerip() );
        sysLogsWebsocketDTO.setClientip( source.getClientip() );
        sysLogsWebsocketDTO.setMessage( source.getMessage() );

        sysLogsWebsocketDTO.setMessagetype( Enum1ToInt(source.getMessagetype()) );

        return sysLogsWebsocketDTO;
    }

    @Override
    public SysLogsWebsocket DTOToSource(SysLogsWebsocketDTO dto) {
        if ( dto == null ) {
            return null;
        }

        SysLogsWebsocket sysLogsWebsocket = new SysLogsWebsocket();

        sysLogsWebsocket.setId( dto.getId() );
        sysLogsWebsocket.setScbj( dto.getScbj() );
        sysLogsWebsocket.setState( dto.getState() );
        sysLogsWebsocket.setCreatetime( dto.getCreatetime() );
        sysLogsWebsocket.setUpdatetime( dto.getUpdatetime() );
        sysLogsWebsocket.setSender( dto.getSender() );
        sysLogsWebsocket.setReceiver( dto.getReceiver() );
        sysLogsWebsocket.setServerip( dto.getServerip() );
        sysLogsWebsocket.setClientip( dto.getClientip() );
        sysLogsWebsocket.setMessage( dto.getMessage() );

        sysLogsWebsocket.setMessagetype( messagetypeToEnum(dto.getMessagetype()) );

        return sysLogsWebsocket;
    }

    @Override
    public List<SysLogsWebsocket> DTOToSources(List<SysLogsWebsocketDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<SysLogsWebsocket> list = new ArrayList<SysLogsWebsocket>( dtos.size() );
        for ( SysLogsWebsocketDTO sysLogsWebsocketDTO : dtos ) {
            list.add( DTOToSource( sysLogsWebsocketDTO ) );
        }

        return list;
    }
}
