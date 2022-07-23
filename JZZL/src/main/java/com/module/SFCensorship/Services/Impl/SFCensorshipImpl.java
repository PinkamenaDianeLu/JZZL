package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveSFCMapper;
import com.bean.jzgl.Converter.FunArchiveSeqMapper;
import com.bean.jzgl.Converter.FunCaseInfoMapper;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunCaseInfo;
import com.factory.BaseFactory;
import com.mapper.jzgl.*;
import com.module.SFCensorship.Services.SFCensorshipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/27 11:06
 * @describe 送检相关
 */
@Service
public class SFCensorshipImpl extends BaseFactory implements SFCensorshipService {
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    SysRecordorderDTOMapper sysRecordorderDTOMapper;
    @Resource
    SysRecordtypeorderDTOMapper sysRecordtypeorderDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    FunArchiveTagsDTOMapper funArchiveTagsDTOMapper;

    @Override
    public List<FunArchiveSFC> selectArchiveSFCPage(Map<String, Object> map) {
        return FunArchiveSFCMapper.INSTANCE.pcDTOToPcs(funArchiveSFCDTOMapper.selectArchiveSFCPage(map));
    }

    @Override
    public int selectArchiveSFCPageCount(Map<String, Object> map) {
        return funArchiveSFCDTOMapper.selectArchiveSFCPageCount(map);
    }

    /**
     * 新建送检记录表
     *
     * @param record
     * @author MrLu
     * @createTime 2020/10/4 15:18
     */
    @Override
    public void insertFunArchiveSeq(FunArchiveSeq record) {
        FunArchiveSeqDTO FunArchiveSeqDTO = FunArchiveSeqMapper.INSTANCE.pcToPcDTO(record);
        funArchiveSeqDTOMapper.insertSelective(FunArchiveSeqDTO);
        record.setId(FunArchiveSeqDTO.getId());
    }

    @Override
    public void insertFunArchiveSeq(FunArchiveSeqDTO record) {
        funArchiveSeqDTOMapper.insertSelective(record);
    }

    @Override
    public void insertFunArchiveSFC(FunArchiveSFC funArchiveSFC) {
        FunArchiveSFCDTO funArchiveSFCDTO = FunArchiveSFCMapper.INSTANCE.pcToPcDTO(funArchiveSFC);
        funArchiveSFCDTOMapper.insertSelective(funArchiveSFCDTO);
        funArchiveSFC.setId(funArchiveSFCDTO.getId());
    }

    @Override
    public FunCaseInfo getFunCaseInfoById(Integer id) {
        return FunCaseInfoMapper.INSTANCE.pcDTOToPc(funCaseInfoDTOMapper.selectByPrimaryKey(id));
    }

    @Override
    public FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid,Integer archivetype) {
        return funArchiveSFCDTOMapper.selectBaseSfcByCaseinfoid(caseinfoid,archivetype);
    }

    @Override
    public int getLastSFCSeq(int caseinfoid) {
        return funArchiveSeqDTOMapper.getLastSFCSeq(caseinfoid);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsByTypeid(int archivetypeid) {
        //archivetypeid
        Map<String, Object> map = new HashMap<>();
        map.put("archivetypeid", archivetypeid);
        map.put("notRecordstyle", 1);//对嫌疑人单选的文书不复制 而是单独查询复制
        return funArchiveRecordsDTOMapper.selectRecordsByTypeid(map);
    }


    @Override
    public List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(int archiveseqid) {
        Map<String, Object> pMap = new HashMap<>();//查询参数
        pMap.put("archiveseqid", archiveseqid);
        return funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap);
    }

    @Override
    public void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO) {
        funArchiveTypeDTOMapper.insertSelective(funArchiveTypeDTO);
    }

    @Override
    public void insertZlRecords(FunArchiveRecordsDTO record, FunArchiveTypeDTO type) {
        record.setIscoverimg(1);//此时未生成图片
        funArchiveRecordsDTOMapper.insertSelective(record);
        if (record.getRecordscode().startsWith("ZL")) {
            //是文书封皮、目录、封底
            FunArchiveFilesDTO r = new FunArchiveFilesDTO();
            r.setJqbh(record.getJqbh());
            r.setAjbh(record.getAjbh());
            r.setThisorder(0);//封皮、目录、封底 他们只有一页，在文书中永远是第一页
            r.setArchiverecordid(record.getId());
            r.setArchivetypeid(record.getArchivetypeid());
            switch (record.getRecordscode()) {
                case "ZL001":
                    r.setFiletype(1);
                    break;
                case "ZL002":
                    r.setFiletype(3);
                    break;
                case "ZL003":
                    r.setFiletype(2);
                    break;
                case "ZL004":
                    r.setFiletype(5);
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
            r.setBjzid(0);
            insertFunArchiveFilesDTO(r);
        } else {
            //查询复制

        }
    }

    @Override
    public void insertFunArchiveFilesDTO(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.insert(record);
    }

    @Override
    public Integer selectRepeatedlyFileCodeBySeqid(String filecode, int archiveseqid) {
        return funArchiveFilesDTOMapper.selectRepeatedlyFileCodeBySeqid(filecode, archiveseqid);
    }

    @Override
    public void updateFunArchiveRecordById(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectById(Integer id) {
        return funSuspectDTOMapper.selectSuspectById(id);
    }

    @Override
    public FunArchiveSeqDTO selectActiveSeqByCaseId(int caseinfoid) {
        return funArchiveSeqDTOMapper.selectActiveSeqByCaseId(caseinfoid);
    }

    @Override
    public void updateBaseSeqIsNotActive(int archivesfcid) {
        funArchiveSeqDTOMapper.updateBaseSeqIsNotActive(archivesfcid);
    }

    @Override
    public void insertFunArchiveRecords(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid, Integer isdelete) {
        return funArchiveFilesDTOMapper.selectRecordFilesByRecordId(archiverecordid, isdelete);
    }

    @Override
    public void updateSuspectDefaultOrder(FunSuspectDTO record) {
        funSuspectDTOMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<SysRecordorderDTO> selectSysRecordOrderByArchiveType(int archivetype) {
        return sysRecordorderDTOMapper.selectSysRecordOrderByArchiveType(archivetype);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectReocrdBySeqRcode(Integer archiveseqid, String recordscode, Integer recordtype) {
        return funArchiveRecordsDTOMapper.selectReocrdBySeqRcode(archiveseqid, recordscode, recordtype);
    }

    @Override
    public List<FunArchiveSeqDTO> selectActiveSeqByCaseInfoId(int caseinfoid) {
        return funArchiveSeqDTOMapper.selectActiveSeqByCaseInfoId(caseinfoid);
    }

    @Override
    public List<SysRecordtypeorderDTO> selectRecordtypeorderByArchivetype(Integer archivetype) {
        return sysRecordtypeorderDTOMapper.selectRecordtypeorderByArchivetype(archivetype);
    }

    @Override
    public void updateIssuspectorderBySfcId(Integer issuspectorder, Integer sfcid) {
        funArchiveSFCDTOMapper.updateIssuspectorderByCaseinfoid(issuspectorder, sfcid);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordOrderForSuspect(int suspectid, int archivetype, int recordtype, int archiveseqid) {
        Map<String, Integer> map = new HashMap<>();
        map.put("archivetype", archivetype);
        map.put("recordtype", recordtype);
        map.put("suspectid", suspectid);
        map.put("archiveseqid", archiveseqid);
        return funArchiveRecordsDTOMapper.selectRecordOrderForSuspect(map);
    }

    @Override
    public List<FunSuspectRecordDTO>  selectSuspectRecordByRid(int recordid) {
        return funSuspectRecordDTOMapper.selectSuspectRecordByRid(recordid);
    }

    @Override
    public void insertSuspectRecord(FunSuspectRecordDTO record) {
        funSuspectRecordDTOMapper.insert(record);
    }


    @Override
    public List<FunSuspectDTO> selectSuspectByRecordid(Integer recordid) {
        return funSuspectDTOMapper.selectSuspectByRecordid(recordid);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsBySuspects(Integer archivetypeid, List<Integer> suspectid) {
        Map<String, Object> map = new HashMap<>();
        map.put("archivetypeid", archivetypeid);
        map.put("suspectid", suspectid);
        return funArchiveRecordsDTOMapper.selectRecordsBySuspects(map);
    }

    @Override
    public List<FunArchiveTagsDTO> selectRecordByRecordId(Integer recordid) {
        return funArchiveTagsDTOMapper.selectRecordByRecordId(recordid);
    }

    @Override
    public void createNewTags(FunArchiveTagsDTO funArchiveTagsDTO) {
        funArchiveTagsDTOMapper.insert(funArchiveTagsDTO);
    }

    @Override
    public void approvalArchive(Integer approval, Integer id) {
        funArchiveSFCDTOMapper.approvalArchive(approval,id);
    }

    @Override
    public void updateSendTypeById(Integer issend, Integer sfcid) {
        funArchiveSFCDTOMapper.updateSendTypeById(issend,sfcid);
    }

    @Override
    public List<FunArchiveSFCDTO> selectSendErrorArchive(Integer caseinfoid) {
        return funArchiveSFCDTOMapper.selectSendErrorArchive(caseinfoid);
    }

}
