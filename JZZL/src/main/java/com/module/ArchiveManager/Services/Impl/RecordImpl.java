package com.module.ArchiveManager.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunArchiveTypeMapper;
import com.bean.jzgl.Converter.FunCaseInfoMapper;
import com.bean.jzgl.Converter.SysRecordorderMapper;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveType;
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
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Override
    public FunArchiveRecordsDTO getFunArchiveRecordsById(Integer id) {
        return funArchiveRecordsDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsByJqbhPage(Map<String, Object> map) throws Exception {
        return funArchiveRecordsDTOMapper.selectRecordsByJqbhPage(map);
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
    public SysRecordorderDTO selectRecordOrderByTypes(String recordcode, Integer archivetype, Integer recordtype) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordcode", recordcode);
        map.put("archivetype", archivetype);
        map.put("recordtype", recordtype);
        return sysRecordorderDTOMapper.selectRecordOrderByTypes(map);
    }

    @Override
    public SysRecordorderDTO selectSysRecordorderDTOById(int id) {
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
        if (null!=suspectid){
            map.put("suspectid", suspectid);
        }
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

    @Override
    public List<FunArchiveType> selectArchiveTypeByJqSeq(int seqId) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archiveseqid", seqId);
        return FunArchiveTypeMapper.INSTANCE.pcDTOToPcs(funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap));
    }

    @Override
    public List<FunArchiveRecords> selectRecordsByTypeid(int archivetypeid, int isDelete,int notRecordstyle) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archivetypeid", archivetypeid);
        pMap.put("isdelete", isDelete);
        pMap.put("notRecordstyle", notRecordstyle);

        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPcs(funArchiveRecordsDTOMapper.selectRecordsByTypeid(pMap));
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid, Integer isdelete) {
        return funArchiveFilesDTOMapper.selectRecordFilesByRecordId(archiverecordid,isdelete);
    }
    @Override
    public void insertFunRecordFilesDTO(FunArchiveFilesDTO record){
        funArchiveFilesDTOMapper.insert(record);
    }

    @Override
    public FunArchiveFilesDTO selectFilesByFileCode(String filecode, int archiverecordid) {
        Map<String, Object> map = new HashMap<>();
        map.put("filecode", filecode);
        map.put("archiverecordid", archiverecordid);
        return funArchiveFilesDTOMapper.selectFilesByFileCode(map);
    }

    @Override
    public FunArchiveRecordsDTO selectRecordByUuidSeq(String recorduuid, Integer archiveseqid) {
        return funArchiveRecordsDTOMapper.selectRecordByUuidSeq(recorduuid,archiveseqid);
    }
    @Override
    public FunArchiveTypeDTO selectFunArchiveTypeById(Integer id) {
        return funArchiveTypeDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public FunSuspectRecordDTO selectSuspectRecordByRid(int recordid) {
        return funSuspectRecordDTOMapper.selectSuspectRecordByRid(recordid);
    }

    /**
     * 查询一个type中对嫌疑人文书的最大顺序（不针对某个嫌疑人）
     * @author MrLu
     * @param typeid
     * @createTime  2021/3/25 16:19
     * @return    |
     */
    @Override
    public int selectRsMaxOrderByTypeid(int typeid){
        return funArchiveRecordsDTOMapper.selectRsMaxOrderByTypeid(typeid);
    }
}
