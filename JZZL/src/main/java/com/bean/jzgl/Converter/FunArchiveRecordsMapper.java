package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface FunArchiveRecordsMapper {

    FunArchiveRecordsMapper INSTANCE = Mappers.getMapper(FunArchiveRecordsMapper.class);
    @Mapping(target = "recordstyle", expression = "java(recordstyleToInt(fpc.getRecordstyle()))")
    FunArchiveRecordsDTO pcToPcDTO(FunArchiveRecords fpc);
    default int recordstyleToInt(String recordstyle) {
        return 0;
    }

    @Mapping(target = "recordstyle", expression = "java(recordstyleToString(fpc.getRecordstyle()))")
    FunArchiveRecords pcDTOToPc(FunArchiveRecordsDTO fpc);
    default String recordstyleToString(int recordstyle) {
        return "";
    }
    List<FunArchiveRecords> pcDTOToPcs(List<FunArchiveRecordsDTO> fpc);
}
