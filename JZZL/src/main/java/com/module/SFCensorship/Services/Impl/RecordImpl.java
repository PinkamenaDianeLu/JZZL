package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunCaseInfoMapper;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunCaseInfo;
import com.factory.BaseFactory;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.mapper.jzgl.FunArchiveSeqDTOMapper;
import com.mapper.jzgl.FunCaseInfoDTOMapper;
import com.module.SFCensorship.Services.RecordsService;
import com.util.MapFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:50
 * @describe
 */
@Service
public class RecordImpl extends BaseFactory implements RecordsService {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;


    @Override
    public FunArchiveRecords getFunArchiveRecordsById(Integer id) {
        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPc(funArchiveRecordsDTOMapper.selectByPrimaryKey(id));
    }
    @Override
    public  List<Object> selectRecordsByJqbhPage(Map<String,Object> map) throws Exception {
        return MapFactory.mapToListBean(funArchiveRecordsDTOMapper.selectRecordsByJqbhPage(map));
    }



    @Override
    public int selectRecordsByJqbhCount(Map<String,Object> map) {
        return funArchiveRecordsDTOMapper.selectRecordsByJqbhCount(map);
    }

    @Override
    public FunCaseInfo getFunCaseInfoById(Integer id) {
        return FunCaseInfoMapper.INSTANCE.pcDTOToPc(funCaseInfoDTOMapper.selectByPrimaryKey(id));
    }

    @Override
    public FunArchiveSeqDTO selectBaseArchive(int caseInfoId) {
        return funArchiveSeqDTOMapper.selectBaseArchive(caseInfoId);
    }

}
