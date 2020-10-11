package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.enums.Enums;
import com.util.EnumsUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

/**
 * @author MrLu
 * @createTime 2020/8/19 11:05
 * @describe 案件提交记录
 */
@Mapper
public interface FunArchiveSeqMapper {
    FunArchiveSeqMapper INSTANCE = Mappers.getMapper(FunArchiveSeqMapper.class);

    @Mapping(target = "isfinal", expression = "java(isfinalToInt(fpc.getIsfinal()))")
    FunArchiveSeqDTO pcToPcDTO(FunArchiveSeq fpc);
    default int isfinalToInt(String isfinal) {
        return Objects.requireNonNull(EnumsUtil.getEnumByName(Enums.IsFinal.class, isfinal)).getValue();
    }
    @Mapping(target = "isfinal", expression = "java(isfinalToEnum(fpc.getIsfinal()))")
    FunArchiveSeq pcDTOToPc(FunArchiveSeqDTO fpc);

    default Enums.IsFinal isfinalToEnum(int isfinal) {
        return EnumsUtil.getEnumByValue(Enums.IsFinal.class, isfinal);
    }
    List<FunArchiveSeq> pcDTOToPcs(List<FunArchiveSeqDTO> fpc);
}
