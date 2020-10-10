package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunArchiveTypeMapper;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveType;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.mapper.jzgl.FunArchiveTypeDTOMapper;
import com.module.SFCensorship.Services.ArrangeArchivesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/10/8 15:40
 * @describe 卷整理
 */
@Service
public class ArrangeArchivesImpl implements ArrangeArchivesService {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Override
    public List<FunArchiveType> selectArchiveTypeByJqSeq(int seqId) {
        Map<String,Object> pMap=new HashMap<>();
        pMap.put("archiveseqid",seqId);
        return FunArchiveTypeMapper.INSTANCE.pcDTOToPcs(funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap));
    }

    @Override
    public List<FunArchiveRecords> selectRecordsByTypeid(int archivetypeid) {
        List<FunArchiveRecordsDTO> a=funArchiveRecordsDTOMapper.selectRecordsByTypeid(archivetypeid);
        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPcs(a);
    }
}
