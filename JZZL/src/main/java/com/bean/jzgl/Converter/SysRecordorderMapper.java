package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.DTO.SysRecordorderDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import com.bean.jzgl.Source.SysRecordorder;
import com.bean.jzgl.Source.selectObj;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper
public interface SysRecordorderMapper {
    SysRecordorderMapper INSTANCE = Mappers.getMapper(SysRecordorderMapper.class);


    SysRecordorderDTO pcToPcDTO(SysRecordorder fpc);
    @Mapping(target = "name", source = "recordname")
    @Mapping(target = "value", source = "id")
    selectObj pcDTOToSelectObj(SysRecordorderDTO fpc);
    SysRecordorder pcDTOToPc(SysRecordorderDTO fpc);
    List<SysRecordorder> pcDTOToPcs(List<SysRecordorderDTO> fpc);

    List<selectObj> pcDTOToSelectObj(List<SysRecordorderDTO> fpc);

}
