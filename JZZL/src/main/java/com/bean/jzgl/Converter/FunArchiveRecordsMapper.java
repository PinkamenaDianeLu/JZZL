package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FunArchiveRecordsMapper  {

    FunArchiveRecordsMapper INSTANCE = Mappers.getMapper(FunArchiveRecordsMapper.class);

    FunArchiveRecordsDTO pcToPcDTO(FunArchiveRecords fpc);
    FunArchiveRecords pcDTOToPc(FunArchiveRecordsDTO fpc);
    List<FunArchiveRecords> pcDTOToPcs(List<FunArchiveRecordsDTO> fpc);

}
