package com.module.ArchiveManager.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunCaseInfoMapper;
import com.bean.jzgl.Converter.SysRecordorderMapper;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunCaseInfo;
import com.bean.jzgl.Source.selectObj;
import com.factory.BaseFactory;
import com.mapper.jzgl.*;
import com.module.ArchiveManager.Services.RecordsService;
import com.util.MapFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
    @Resource
    SysRecordorderDTOMapper sysRecordorderDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;

    @Override
    public FunArchiveRecords getFunArchiveRecordsById(Integer id) {
        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPc(funArchiveRecordsDTOMapper.selectByPrimaryKey(id));
    }

    @Override
    public List<Object> selectRecordsByJqbhPage(Map<String, Object> map) throws Exception {
        return MapFactory.mapToListBean(funArchiveRecordsDTOMapper.selectRecordsByJqbhPage(map));
    }


    @Override
    public int selectRecordsByJqbhCount(Map<String, Object> map) {
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

    @Override
    public FunArchiveSeqDTO selectBaseArchiveBySeqId(int seqid) {
        return funArchiveSeqDTOMapper.selectBaseArchiveBySeqId(seqid);
    }

    @Override
    public List<selectObj> selectRecordCodesByAtype(Map<String, Object> map) {
        return SysRecordorderMapper.INSTANCE.pcDTOToSelectObj(sysRecordorderDTOMapper.selectRecordCodesByAtype(map));
    }

    @Override
    public Integer selectRecordCodesByAtypeCount(Map<String, Object> map) {
        return sysRecordorderDTOMapper.selectRecordCodesByAtypeCount(map);
    }

    @Override
    public FunArchiveSeqDTO selectFunArchiveSeqById(Integer id) {
        return funArchiveSeqDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public SysRecordorderDTO selectSysRecordorderDTOById(Integer id) {
        return sysRecordorderDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectBySeqId(Integer seqid) {
        return funSuspectDTOMapper.selectSuspectBySeqId(seqid);
    }

    @Override
    public FunArchiveRecordsDTO selectPriveRecord(int recordtype, int archivetype, int defaultorder, int archiveseqid,Integer suspectid) {
        Map<String, Integer> map = new HashMap<>();
        map.put("recordtype", recordtype);
        map.put("archivetype", archivetype);
        map.put("defaultorder", defaultorder);//suspectid
        map.put("archiveseqid", archiveseqid);
        map.put("suspectid", suspectid);
        return funArchiveRecordsDTOMapper.selectPriveRecord(map);
    }

    @Override
    public void updateOrderAdd(int archiveseqid, int archivetypeid, int thisorder) {
        funArchiveRecordsDTOMapper.updateOrderAdd(archiveseqid, archivetypeid, thisorder);
    }

    @Override
    public void insertSuspectRecord(FunSuspectRecordDTO record) {
        funSuspectRecordDTOMapper.insert(record);
    }

    @Override
    public void insertFunArchiveRecords(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectByRecordid(Integer recordid) {
        return funSuspectDTOMapper.selectSuspectByRecordid(recordid);
    }




}
