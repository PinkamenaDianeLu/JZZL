package com.module.ArchiveManager.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunArchiveSFCMapper;
import com.bean.jzgl.Converter.FunArchiveSeqMapper;
import com.bean.jzgl.Converter.FunArchiveTypeMapper;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunArchiveType;
import com.mapper.jzgl.*;
import com.module.ArchiveManager.Services.ArrangeArchivesService;
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
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    SysRecordorderDTOMapper sysRecordorderDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;

    @Override
    public List<FunArchiveType> selectArchiveTypeByJqSeq(int seqId) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archiveseqid", seqId);
        return FunArchiveTypeMapper.INSTANCE.pcDTOToPcs(funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap));
    }

    @Override
    public List<FunArchiveRecords> selectRecordsByTypeid(int archivetypeid, int isDelete) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archivetypeid", archivetypeid);
        pMap.put("isdelete", isDelete);

        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPcs(funArchiveRecordsDTOMapper.selectRecordsByTypeid(pMap));
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsDtoByTypeid(int archivetypeid, Integer isDelete) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archivetypeid", archivetypeid);
        pMap.put("isdelete", isDelete);
        return funArchiveRecordsDTOMapper.selectRecordsByTypeid(pMap);
    }
    @Override
    public FunArchiveSeq selectLastSeqBySfc(int sfcId) {
        return FunArchiveSeqMapper.INSTANCE.pcDTOToPc(funArchiveSeqDTOMapper.selectLastSeqBySfc(sfcId));
    }

    @Override
    public FunArchiveSeqDTO selectFunArchiveSeqById(Integer id) {
        return funArchiveSeqDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public FunArchiveRecordsDTO selectFunArchiveRecordsById(Integer id) {
        return funArchiveRecordsDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insertFunArchiveSeq(FunArchiveSeqDTO record) {
        funArchiveSeqDTOMapper.insertSelective(record);
    }

    @Override
    public void insertFunArchiveRecords(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }

    @Override
    public void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO) {
        funArchiveTypeDTOMapper.insertSelective(funArchiveTypeDTO);
    }

    @Override
    public FunArchiveTypeDTO selectFunArchiveTypeById(Integer id) {
        return funArchiveTypeDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid, Integer isdelete) {
        return funArchiveFilesDTOMapper.selectRecordFilesByRecordId(archiverecordid, isdelete);
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String[] filesCode, int recordId) {
        Map<String, Object> map = new HashMap<>();
        map.put("filecode", filesCode);
        map.put("archiverecordid", recordId);
        return funArchiveFilesDTOMapper.selectRecordFilesByFileCodes(map);
    }

    @Override
    public List<FunArchiveFilesDTO> selectFilesHistory(String filecode) {
        return funArchiveFilesDTOMapper.selectFilesHistory(filecode);
    }

    @Override
    public FunArchiveFilesDTO selectFilesByFileCode(String filecode, int archiverecordid) {
        Map<String, Object> map = new HashMap<>();
        map.put("filecode", filecode);
        map.put("archiverecordid", archiverecordid);
        return funArchiveFilesDTOMapper.selectFilesByFileCode(map);
    }

    @Override
    public void insertFunArchiveFilesDTO(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.insert(record);
    }

    @Override
    public FunArchiveRecordsDTO selectNextRecord(Integer id) {
        return funArchiveRecordsDTOMapper.selectNextRecord(id);
    }

    @Override
    public void updateFunArchiveRecordsById(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void updateFileByRecordId(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.updateFileByRecordId(record);
    }

    @Override
    public void updateFileByFileCode(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.updateFileBySeqIdFileCode(record);
    }

    @Override
    public void updateRecordOrderByTypeId(int archivetypeid, int id, int thisorder) {
        funArchiveRecordsDTOMapper.updateRecordOrderByTypeId(archivetypeid, id, thisorder);
    }

    @Override
    public void updateFileOrder(int archiverecordid, int thisorder, String filecode) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archiverecordid", archiverecordid);
        pMap.put("thisorder", thisorder);
        pMap.put("filecode", filecode);
        funArchiveFilesDTOMapper.updateOrderByRecordId(pMap);
    }

    @Override
    public int selectFileMaxOrder(int archiverecordid) {
        return funArchiveFilesDTOMapper.selectFileMaxOrder(archiverecordid);
    }

    @Override
    public int selectRecordMaxOrder(int id) {
        return funArchiveRecordsDTOMapper.selectRecordMaxOrder(id);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectByCaseinfoId(Integer caseinfoid) {
        return funSuspectDTOMapper.selectSuspectByCaseinfoId(caseinfoid);
    };

    @Override
    public FunArchiveSFCDTO selectFunArchiveSFCDTOById(Integer sfcId) {
        return funArchiveSFCDTOMapper.selectByPrimaryKey(sfcId);
    }


    @Override
    public List<FunArchiveRecordsDTO> selectRecordsBySuspect(Integer suspectid,Integer recordtype,Integer archiveseqid,Integer archivetype) {
        return funArchiveRecordsDTOMapper.selectRecordsBySuspectAType(suspectid,recordtype,archiveseqid,archivetype);
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
    public FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid,Integer archivetype) {
        return funArchiveSFCDTOMapper.selectBaseSfcByCaseinfoid(caseinfoid,archivetype);
    }




    @Override
    public void updateArchiveSfcById(FunArchiveSFCDTO funArchiveSFCDTO) {
        funArchiveSFCDTOMapper.updateByPrimaryKeySelective(funArchiveSFCDTO);
    }

    @Override
    public FunArchiveSFC selectFunArchiveSFCById(int id) {
        return FunArchiveSFCMapper.INSTANCE.pcDTOToPc(funArchiveSFCDTOMapper.selectByPrimaryKey(id));
    }


    @Override
    public void updateBaseSeqIsNotActive(int archivesfcid) {
        funArchiveSeqDTOMapper.updateBaseSeqIsNotActive(archivesfcid);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordbyName(String recordname ,Integer archiveseqid) {
        Map<String, Object> map=new HashMap<>();
        map.put("recordname",recordname);
        map.put("archiveseqid",archiveseqid);
        return funArchiveRecordsDTOMapper.selectRecordbyName(map);
    }

    @Override
    public void updateSuspectDefaultOrder(FunSuspectDTO record) {
        funSuspectDTOMapper.updateByPrimaryKeySelective(record);
    }


}
