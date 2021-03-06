package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @log  2020/10/24 11:10  MrLu  已拆分为fun_case_info和fun_case_peoplecase两张表
 **/
@Mapper
public interface FunPeopelCaseMapper {

    FunPeopelCaseMapper INSTANCE = Mappers.getMapper(FunPeopelCaseMapper.class);

    @Mapping(target = "persontype", expression = "java(personTypeToInt(fpc.getPersontype()))")
    @Mapping(target = "casestate", expression = "java(casestateToInt(fpc.getCasestate()))")
    @Mapping(target = "casetype", expression = "java(casestypeToInt(fpc.getCasetype()))")
    FunPeopelCaseDTO pcToPcDTO(FunPeopelCase fpc);

    default int personTypeToInt(String personType) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.PersonType.class, personType)).getValue();
    }
    default int casestateToInt(String CaseState) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.CaseState.class, CaseState)).getValue();
    }
    default int casestypeToInt(String CaseType) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.CaseType.class, CaseType)).getValue();
    }
    @Mapping(target = "persontype", expression = "java(personTypeToEnum(fpc.getPersontype()))")
    @Mapping(target = "casestate", expression = "java(casestateToEnum(fpc.getCasestate()))")
    @Mapping(target = "casetype", expression = "java(casetypeToEnum(fpc.getCasetype()))")
    FunPeopelCase pcDTOToPc(FunPeopelCaseDTO fpc);

    default Enums.PersonType personTypeToEnum(int personType) {
        return EnumsUtil.getEnumByValue(Enums.PersonType.class, personType);
    }

    default Enums.CaseState casestateToEnum(int casestate) {
        return EnumsUtil.getEnumByValue(Enums.CaseState.class, casestate);
    }
    default Enums.CaseType casetypeToEnum(int casetype) {
        return EnumsUtil.getEnumByValue(Enums.CaseType.class, casetype);
    }

    List<FunPeopelCase> pcDTOToPcs(List<FunPeopelCaseDTO> fpc);



}
