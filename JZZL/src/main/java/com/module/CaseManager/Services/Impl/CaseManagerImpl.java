package com.module.CaseManager.Services.Impl;

import com.bean.jzgl.DTO.*;
import com.factory.BaseFactory;
import com.mapper.jzgl.*;
import com.module.CaseManager.Services.CaseManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author MrLu
 * @createTime 2021/1/8 15:18
 * @describe
 */
@Service
public class CaseManagerImpl extends BaseFactory implements CaseManagerService {

    @Resource
    SysUserDTOMapper sysUserDTOMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;


    @Override
    public SysUserDTO selectSysUserDtoById(Integer id) {
        return sysUserDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public FunCaseInfoDTO selectCaseInfoById(Integer id) {
        return funCaseInfoDTOMapper.selectByPrimaryKey(id);
    }


    @Override
    public void insertCaseinfo(FunCaseInfoDTO record) {
        funCaseInfoDTOMapper.insertSelective(record);
    }

    @Override
    public List<FunCasePeoplecaseDTO> selectRelationByCaseid(Integer caseinfoid) {
        return funCasePeoplecaseDTOMapper.selectRelationByCaseid(caseinfoid);
    }

    @Override
    public List<FunCasePeoplecaseDTO> selectPeopleCaseForCombinationPage(Map<String, Object> map) throws Exception {
        return funCasePeoplecaseDTOMapper.selectPeopleCaseForCombinationPage(map);
    }

    @Override
    public int selectPeopleCaseForCombinationCount(Map<String, Object> map) throws Exception {
        return funCasePeoplecaseDTOMapper.selectPeopleCaseForCombinationCount(map);
    }


    /**
     * 新建人案关系表
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 10:09
     */
    @Override
    public void insertCasePeopleCase(FunCasePeoplecaseDTO record) {
        funCasePeoplecaseDTOMapper.insertSelective(record);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectByCaseinfoId(Integer caseinfoid) {
        return funSuspectDTOMapper.selectSuspectByCaseinfoId(caseinfoid);
    }

    @Override
    public FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid,Integer archivetype) {
        return funArchiveSFCDTOMapper.selectBaseSfcByCaseinfoid(caseinfoid,archivetype);
    }

    @Override
    public void createNewSfc(FunArchiveSFCDTO record) {
        funArchiveSFCDTOMapper.insertSelective(record);
    }

    @Override
    public FunArchiveSeqDTO selectActiveSeqByCaseId(int caseinfoid) {
        return funArchiveSeqDTOMapper.selectActiveSeqByCaseId(caseinfoid);
    }

    @Override
    public List<FunArchiveSeqDTO> selectSuspectsByCaseIds(String[] caseIds) {
        return funArchiveSeqDTOMapper.selectActiveSeqByCaseIds(caseIds);
    }

    @Override
    public void createNewSeq(FunArchiveSeqDTO record) {
        funArchiveSeqDTOMapper.insertSelective(record);
    }

    @Override
    public void createNewType(FunArchiveTypeDTO record) {
        funArchiveTypeDTOMapper.insertSelective(record);
    }

    @Override
    public List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(int archiveseqid) {
        Map<String, Object> pMap = new HashMap<>();//查询参数
        pMap.put("archiveseqid", archiveseqid);
        return funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsNotSuspectByType(int archivetypeid, String[] notsuspectids) {
        Map<String, Object> pMap = new HashMap<>();//查询参数
        pMap.put("archivetypeid", archivetypeid);
        pMap.put("notsuspectids", notsuspectids);
        return funArchiveRecordsDTOMapper.selectRecordsNotSuspectByType(pMap);
    }

    @Override
    public List<FunSuspectRecordDTO> selectSuspectRecordByRid(int recordid) {
        return funSuspectRecordDTOMapper.selectSuspectRecordByRid(recordid);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectsByIds(String[] ids) {
        return funSuspectDTOMapper.selectByPrimaryKeys(ids);
    }

    @Override
    public void insertNewSuspect(FunSuspectDTO record) {
        funSuspectDTOMapper.insertSelective(record);
    }

    @Override
    public void createNewSR(FunSuspectRecordDTO record) {
        funSuspectRecordDTOMapper.insert(record);
    }

    @Override
    public void createNewRecord(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid) {
        return funArchiveFilesDTOMapper.selectRecordFilesByRecordId(archiverecordid, 0);
    }

    @Override
    public Integer selectRepeatedlyFileCodeBySeqid(String filecode, int archiveseqid) {
        return funArchiveFilesDTOMapper.selectRepeatedlyFileCodeBySeqid(filecode, archiveseqid);
    }

    @Override
    public void createFils(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.insert(record);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsBySeqType(List<Integer> seqids, Integer recordtype) {
        Map<String, Object> map = new HashMap<>();
        //｛seqids,recordtype｝
        map.put("seqids", seqids);
        map.put("recordtype", recordtype);
        return funArchiveRecordsDTOMapper.selectRecordsBySeqType(map);
    }

    @Override
    public FunSuspectRecordDTO selectRecordBoolSuspect(Integer recordid,String []array) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordid", recordid);
        map.put("array", array);
        return funSuspectRecordDTOMapper.selectRecordBoolSuspect(map);
    }


    @Override
    public void insertZlRecords(FunArchiveRecordsDTO record,FunArchiveTypeDTO type){
        record.setRecorduuid(UUID.randomUUID().toString());
        record.setWjbid(0);
        record.setWjbm("0");
        funArchiveRecordsDTOMapper.insertSelective(record);
        if (record.getRecordscode().startsWith("ZL")) {
            //是文书封皮、目录、封底
            FunArchiveFilesDTO r = new FunArchiveFilesDTO();
            r.setJqbh(record.getJqbh());
            r.setBjzid(0);
            r.setAjbh(record.getAjbh());
            r.setThisorder(0);//封皮、目录、封底 他们只有一页，在文书中永远是第一页
            r.setArchiverecordid(record.getId());
            r.setArchivetypeid(record.getArchivetypeid());
            switch (record.getRecordscode()) {
                case "ZL001":
                    r.setFiletype(1);
                    break;
                case "ZL004":
                    r.setFiletype(5);
                    break;
                case "ZL002":
                    r.setFiletype(3);
                    break;
                case "ZL003":
                    r.setFiletype(2);
                    break;
            }
            r.setFileurl("/");
            r.setOriginurl("/");
            r.setIsdowland(1);
            r.setFilename(type.getRecordtypecn());
            r.setArchiveseqid(record.getArchiveseqid());
            r.setArchivesfcid(record.getArchivesfcid());
            r.setIsazxt(1);
            r.setAuthor(record.getAuthor());
            r.setAuthorid(record.getAuthorid());
            r.setIsshow(0);
            r.setIsdelete(0);
            r.setServerip("/");
            r.setFilecode("F" + record.getRecordscode() + "R" + record.getId() + "T" + type.getId());
            createFils(r);
        } else {
            //查询复制

        }
    }
}
