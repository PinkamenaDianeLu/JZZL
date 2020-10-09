package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.FunArchiveType;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/10/9 9:21
 * @describe
 */
@Mapper
public interface FunArchiveTypeMapper {
    FunArchiveTypeMapper INSTANCE = Mappers.getMapper(FunArchiveTypeMapper.class);
    @Mapping(target = "isazxt", expression = "java(isazxtToInt(fpc.getIsazxt()))")
    FunArchiveType pcDTOToPc(FunArchiveTypeDTO fpc);
    default Enums.IsAzxt isazxtToInt(int isazxt) {
        return EnumsUtil.getEnumByValue(Enums.IsAzxt.class, isazxt);
    }
    List<FunArchiveType> pcDTOToPcs(List<FunArchiveTypeDTO> fpc);
}
