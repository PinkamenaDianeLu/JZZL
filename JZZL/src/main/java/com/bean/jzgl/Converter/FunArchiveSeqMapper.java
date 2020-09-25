package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/8/19 11:05
 * @describe 案件提交记录
 */
@Mapper
public interface FunArchiveSeqMapper {
    FunArchiveSeqMapper INSTANCE = Mappers.getMapper(FunArchiveSeqMapper.class);

    @Mapping(target = "issend", expression = "java(issendToInt(fpc.getIssend()))")
    @Mapping(target = "isfinal", expression = "java(isfinalToInt(fpc.getIsfinal()))")
    FunArchiveSeqDTO pcToPcDTO(FunArchiveSeq fpc);

    default int issendToInt(Enums.IsSend issend) {
        return issend.getValue();
    }
    default int isfinalToInt(Enums.IsFinal isfinal) {
        return isfinal.getValue();
    }
    @Mapping(target = "issend", expression = "java(issendToEnum(fpc.getIssend()))")
    @Mapping(target = "isfinal", expression = "java(isfinalToEnum(fpc.getIsfinal()))")
    FunArchiveSeq pcDTOToPc(FunArchiveSeqDTO fpc);

    default Enums.IsSend issendToEnum(int issend) {
        return EnumsUtil.getEnumByValue(Enums.IsSend.class, issend);
    }
    default Enums.IsFinal isfinalToEnum(int isfinal) {
        return EnumsUtil.getEnumByValue(Enums.IsFinal.class, isfinal);
    }
    List<FunArchiveSeq> pcDTOToPcs(List<FunArchiveSeqDTO> fpc);
}
