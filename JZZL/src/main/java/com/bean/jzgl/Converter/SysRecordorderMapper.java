package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.SysRecordorderDTO;
import com.bean.jzgl.Source.SysRecordorder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SysRecordorderMapper {
    SysRecordorderMapper INSTANCE = Mappers.getMapper(SysRecordorderMapper.class);


    SysRecordorderDTO pcToPcDTO(SysRecordorder fpc);
    SysRecordorder pcDTOToPc(SysRecordorderDTO fpc);
    List<SysRecordorder> pcDTOToPcs(List<SysRecordorderDTO> fpc);
}
