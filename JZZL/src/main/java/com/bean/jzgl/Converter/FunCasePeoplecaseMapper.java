package com.bean.jzgl.Converter;/**
 * @author Mrlu
 * @createTime 2020/10/24
 * @describe
 */

import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.Source.FunCasePeoplecase;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * @author MrLu
 * @createTime 2020/10/24 10:37
 * @describe
 */
@Mapper
public interface FunCasePeoplecaseMapper {


    FunCasePeoplecaseMapper INSTANCE = Mappers.getMapper(FunCasePeoplecaseMapper.class);

    @Mapping(target = "persontype", expression = "java(personTypeToInt(fpc.getPersontype()))")
    FunCasePeoplecaseDTO pcToPcDTO(FunCasePeoplecase fpc);

    default int personTypeToInt(String personType) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.PersonType.class, personType)).getValue();
    }
    @Mapping(target = "persontype", expression = "java(personTypeToEnum(fpc.getPersontype()))")
    FunCasePeoplecase pcDTOToPc(FunCasePeoplecaseDTO fpc);

    default Enums.PersonType personTypeToEnum(int personType) {
        return EnumsUtil.getEnumByValue(Enums.PersonType.class, personType);
    }

    List<FunCasePeoplecase> pcDTOToPcs(List<FunCasePeoplecaseDTO> fpc);

}
