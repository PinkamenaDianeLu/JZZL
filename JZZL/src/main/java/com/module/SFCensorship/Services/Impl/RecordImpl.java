package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunCaseInfoMapper;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunCaseInfo;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.mapper.jzgl.FunCaseInfoDTOMapper;
import com.module.SFCensorship.Services.RecordsService;
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
public class RecordImpl implements RecordsService {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;

    @Override
    public FunArchiveRecords getFunArchiveRecordsById(Integer id) {
        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPc(funArchiveRecordsDTOMapper.selectByPrimaryKey(id));
    }
    @Override
    public List<FunArchiveRecords> selectRecordsByJqbhPage(Map<String,Object> map){
        return  FunArchiveRecordsMapper.INSTANCE.pcDTOToPcs(funArchiveRecordsDTOMapper.selectRecordsByJqbhPage(map));
    }



    @Override
    public int selectRecordsByJqbhCount(Map<String,Object> map) {
        return funArchiveRecordsDTOMapper.selectRecordsByJqbhCount(map);
    }

    @Override
    public FunCaseInfo getFunCaseInfoById(Integer id) {
        return FunCaseInfoMapper.INSTANCE.pcDTOToPc(funCaseInfoDTOMapper.selectByPrimaryKey(id));
    }
}
