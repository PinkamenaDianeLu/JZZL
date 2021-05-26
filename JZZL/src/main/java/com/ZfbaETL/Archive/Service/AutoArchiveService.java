package com.ZfbaETL.Archive.Service;

import com.bean.jzgl.DTO.*;
import com.mapper.jzgl.*;
import com.mapper.zfba.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author MrLu
 * @createTime 2021/1/5 16:21
 * @describe  自动整理
 */
@Service
public class AutoArchiveService {
   @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    XtAjxxbMapper xtAjxxbMapper;
    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    XtWjflbMapper xtWjflbMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    XtXyrxxbMapper xtXyrxxbMapper;
    @Resource
    XtBarbMapper xtBarbMapper;
    @Resource
    SysUserMapper zfbaSysUserMapper;
    @Resource
    SysUserDTOMapper jzglSysUserMapper;
    @Resource
    SysBadwbMapper sysBadwbMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    SysRecordorderDTOMapper sysRecordorderDTOMapper;
    @Resource
    SysRecordtypeorderDTOMapper sysRecordtypeorderDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
   /**
     * 查询嫌疑人的文书
     * @author MrLu
     * @param
     * @createTime  2021/1/5 16:18
     * @return    |
     *//*
    List<XtWjflb> selectRecordBySuspect(String jqbh, String dydm){
        return  xtWjflbMapper.selectRecordBySuspect(jqbh,dydm);
    };*/

    /**
     * 查询新的原始卷
     * @author MrLu
     * @param id 大于某个id
     * @createTime  2021/1/6 14:12
     * @return    |
     */
   public List<FunArchiveSFCDTO>  selectNewOriginArchive(Integer id){
        return funArchiveSFCDTOMapper.selectNewOriginArchive(id);
    };
    /**
     * 根据案件信息表id查询嫌疑人
     * @author MrLu
     * @param caseinfoid 案件信息表id
     * @createTime  2020/11/22 18:24
     * @return  List<FunSuspectDTO>  |
     */
   public List<FunSuspectDTO> selectSuspectByCaseinfoId(Integer caseinfoid)throws  Exception{
        return funSuspectDTOMapper.selectSuspectByCaseinfoId(caseinfoid);
    };

    /**
     * 查询送检记录下最后一次整理
     *
     * @param archivesfcid 送检记录id
     * @return FunArchiveSeq  |
     * @author MrLu
     * @createTime 2020/10/11 16:13
     */
  public   FunArchiveSeqDTO selectLastSeqBySfc(int archivesfcid){
        return  funArchiveSeqDTOMapper.selectOriSeqBySfc(archivesfcid);
    };

    public List<SysRecordorderDTO> selectSysRecordOrderByArchiveType(int archivetype) throws  Exception{
        return sysRecordorderDTOMapper.selectSysRecordOrderByArchiveType(archivetype);
    }
    public List<SysRecordtypeorderDTO> selectRecordtypeorderByArchivetype(Integer archivetype) throws  Exception{
        return sysRecordtypeorderDTOMapper.selectRecordtypeorderByArchivetype(archivetype);
    }
    public  void createNewType(FunArchiveTypeDTO record){
        funArchiveTypeDTOMapper.insertSelective(record);
    }

    public  void  createNewSfc(FunArchiveSFCDTO record){
        funArchiveSFCDTOMapper.insertSelective(record);
    }
    public  void createNewSeq(FunArchiveSeqDTO record){
        funArchiveSeqDTOMapper.insertSelective(record);
    }
    public List<FunArchiveRecordsDTO> selectRecordOrderForSuspect(int suspectid, int archivetype, int recordtype, int archiveseqid) throws  Exception{
        Map<String, Integer> map = new HashMap<>();
        map.put("archivetype", archivetype);
        map.put("recordtype", recordtype);
        map.put("suspectid", suspectid);
        map.put("archiveseqid", archiveseqid);
        return funArchiveRecordsDTOMapper.selectRecordOrderForSuspect(map);
    }
    public FunSuspectRecordDTO selectSuspectRecordByRid(int recordid) {
        return funSuspectRecordDTOMapper.selectSuspectRecordByRid(recordid);
    }
    public  void createNewRecord(FunArchiveRecordsDTO record){
        funArchiveRecordsDTOMapper.insertSelective(record);
    }
    public  void createFiles(FunArchiveFilesDTO record){
        funArchiveFilesDTOMapper.insert(record);
    }
    public List<FunArchiveRecordsDTO> selectRecordBySeqRcode(Integer archiveseqid, String recordscode) {
        return funArchiveRecordsDTOMapper.selectRecordByOriRecord(archiveseqid, recordscode);
    }
    public  void createNewSR(FunSuspectRecordDTO record){
        funSuspectRecordDTOMapper.insert(record);
    }
    public List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid, Integer isdelete) {
        return funArchiveFilesDTOMapper.selectRecordFilesByRecordId(archiverecordid, isdelete);
    }
    public Integer selectRepeatedlyFileCodeBySeqid(String filecode, int archiveseqid) {
        return funArchiveFilesDTOMapper.selectRepeatedlyFileCodeBySeqid(filecode,archiveseqid);
    }

    public SysRecordorderDTO selectRecordOrderByTypes(String recordcode, Integer archivetype) {
        Map<String, Object> map = new HashMap<>();
        map.put("recordcode", recordcode);
        map.put("archivetype", archivetype);
        return sysRecordorderDTOMapper.selectRecordOrderByTypes(map);
    }
    /**
     * 新建文书目录 封皮 封底
     * @author MrLu
     * @param record
     *@param  type
     * @createTime  2020/10/8 11:38
     * @return  void  |
     */
    public void insertZlRecords(FunArchiveRecordsDTO record,FunArchiveTypeDTO type){
        record.setRecorduuid(UUID.randomUUID().toString());
        record.setWjbid(0);
        record.setWjbm("0");
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
            createFiles(r);
        } else {
            //查询复制

        }
    }
}
