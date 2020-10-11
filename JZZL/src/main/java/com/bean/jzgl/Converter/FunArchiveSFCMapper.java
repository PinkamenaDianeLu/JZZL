package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * @author Mrlu
 * @createTime 2020/10/11
 * @describe
 */
@Mapper
public interface FunArchiveSFCMapper {

    FunArchiveSFCMapper INSTANCE = Mappers.getMapper(FunArchiveSFCMapper.class);

    @Mapping(target = "issend", expression = "java(issendToInt(fpc.getIssend()))")
    FunArchiveSFCDTO pcToPcDTO(FunArchiveSFC fpc);
    default int issendToInt(String issend) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.IsSend.class, issend)).getValue();
    }
    @Mapping(target = "issend", expression = "java(issendToEnum(fpc.getIssend()))")
    FunArchiveSFC pcDTOToPc(FunArchiveSFCDTO fpc);

    default Enums.IsSend issendToEnum(int issend) {
        return EnumsUtil.getEnumByValue(Enums.IsSend.class, issend);
    }
    List<FunArchiveSFC> pcDTOToPcs(List<FunArchiveSFCDTO> fpc);
}
