package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.module.SFCensorship.Services.RecordsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:50
 * @describe
 */
@Service
public class RecordImpl implements RecordsService {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;


    @Override
    public FunArchiveRecords getFunArchiveRecordsById(Integer id) {
        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPc(funArchiveRecordsDTOMapper.selectByPrimaryKey(id));
    }
}
