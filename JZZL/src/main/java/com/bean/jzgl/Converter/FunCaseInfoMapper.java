package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.Source.FunCaseInfo;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * @author Mrlu
 * @createTime 2020/10/24
 * @describe
 */
@Mapper
public interface FunCaseInfoMapper {

    FunCaseInfoMapper INSTANCE = Mappers.getMapper(FunCaseInfoMapper.class);

    @Mapping(target = "casestate", expression = "java(casestateToInt(fpc.getCasestate()))")
    @Mapping(target = "casetype", expression = "java(casestypeToInt(fpc.getCasetype()))")
    FunCaseInfoDTO pcToPcDTO(FunCaseInfo fpc);

    default int casestateToInt(String CaseState) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.CaseState.class, CaseState)).getValue();
    }
    default int casestypeToInt(String CaseType) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.CaseType.class, CaseType)).getValue();
    }
    @Mapping(target = "casestate", expression = "java(casestateToEnum(fpc.getCasestate()))")
    @Mapping(target = "casetype", expression = "java(casetypeToEnum(fpc.getCasetype()))")
    FunCaseInfo pcDTOToPc(FunCaseInfoDTO fpc);


    default Enums.CaseState casestateToEnum(int casestate) {
        return EnumsUtil.getEnumByValue(Enums.CaseState.class, casestate);
    }
    default Enums.CaseType casetypeToEnum(int casetype) {
        return EnumsUtil.getEnumByValue(Enums.CaseType.class, casetype);
    }

    List<FunCaseInfo> pcDTOToPcs(List<FunCaseInfoDTO> fpc);

}
