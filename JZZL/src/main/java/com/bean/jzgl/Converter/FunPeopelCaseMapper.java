package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/8/19 11:05
 * @describe  案件
 */
@Mapper
public interface FunPeopelCaseMapper {

    FunPeopelCaseMapper INSTANCE = Mappers.getMapper(FunPeopelCaseMapper.class);

    @Mapping(target = "persontype", expression = "java(personTypeToInt(fpc.getPersontype()))")
    @Mapping(target = "casetype", expression = "java(casetypeToInt(fpc.getCasetype()))")
    FunPeopelCaseDTO pcToPcDTO(FunPeopelCase fpc);

    default int personTypeToInt(Enums.PersonType personType) {
        return personType.getValue();
    }
    default int casetypeToInt(Enums.CaseType CaseType) {
        return CaseType.getValue();
    }
    @Mapping(target = "persontype", expression = "java(personTypeToEnum(fpc.getPersontype()))")
    @Mapping(target = "casetype", expression = "java(casetypeToEnum(fpc.getCasetype()))")
    FunPeopelCase pcDTOToPc(FunPeopelCaseDTO fpc);

    default Enums.PersonType personTypeToEnum(int personType) {
        return EnumsUtil.getEnumByValue(Enums.PersonType.class, personType);
    }

    default Enums.CaseType casetypeToEnum(int casetype) {
        return EnumsUtil.getEnumByValue(Enums.CaseType.class, casetype);
    }

    List<FunPeopelCase> pcDTOToPcs(List<FunPeopelCaseDTO> fpc);



}
