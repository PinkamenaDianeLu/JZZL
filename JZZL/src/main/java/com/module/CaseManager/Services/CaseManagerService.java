package com.module.CaseManager.Services;

import com.bean.jzgl.DTO.*;

import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2021/1/8 15:18
 * @describe
 */
public interface CaseManagerService {
    /**
     * 根据id 查询用户
     *
     * @param id)
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 15:49
     */
    SysUserDTO selectSysUserDtoById(Integer id);



     /**
     * 查询可用于合案的案件
     * @author Mrlu
     * @param 
     * @createTime  2021/3/10 16:21
     * @return    |  
      */
    List<FunCasePeoplecaseDTO>selectPeopleCaseForCombinationPage(Map<String, Object> map) throws Exception;
    int selectPeopleCaseForCombinationCount(Map<String, Object> map) throws Exception;
    /**
     * 根据id查询案件信息
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 15:55
     */
    FunCaseInfoDTO selectCaseInfoById(Integer id);

    /**
     * 新建案件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 9:24
     */
    void insertCaseinfo(FunCaseInfoDTO record);


    /**
     * 根据案件表id查询所有的关系
     *
     * @param caseinfoid 案件id
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 16:22
     */
    List<FunCasePeoplecaseDTO> selectRelationByCaseid(Integer caseinfoid);

    /**
     * 新建人案关系表
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 10:09
     */
    void insertCasePeopleCase(FunCasePeoplecaseDTO record);

    /**
     * 根据案件信息表id查询嫌疑人
     *
     * @param caseinfoid 案件信息表id
     * @return List<FunSuspectDTO>  |
     * @author MrLu
     * @createTime 2020/11/22 18:24
     */
    List<FunSuspectDTO> selectSuspectByCaseinfoId(Integer caseinfoid);


    /**
     * 根据案件id查询案件的基础卷sfc
     *
     * @param caseinfoid 案件id
     * @return |
     * @author Mrlu
     * @createTime 2021/3/5 16:09
     */
    FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid);

    /**
     * 新建sfc
     *
     * @param record
     * @return |
     * @author Mrlu
     * @createTime 2021/3/5 16:26
     */
    void createNewSfc(FunArchiveSFCDTO record);

    /**
     * 查询某个案件正在活跃的基础卷
     *
     * @param caseinfoid 案件表id
     * @return FunArchiveSeqDTO  |
     * @author MrLu
     * @createTime 2020/11/27 9:22
     */
    FunArchiveSeqDTO selectActiveSeqByCaseId(int caseinfoid);
    /**
     * 根据id查询嫌疑人信息
     * @author Mrlu
     * @param  caseIds
     * @createTime  2021/3/8 11:09
     * @return    |
     */
    List<FunArchiveSeqDTO>  selectSuspectsByCaseIds(String [] caseIds);
    /**
     * 新建seq
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/3/8 8:58
     */
    void createNewSeq(FunArchiveSeqDTO record);

    void createNewType(FunArchiveTypeDTO record);
    /**
     * 通过seq查询type
     *
     * @param archiveseqid
     * @return |
     * @author Mrlu
     * @createTime 2021/3/8 9:07
     */
    List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(int archiveseqid);

    /**
     * 查询一个type中不带指定嫌疑人的文书
     *
     * @param archivetypeid
     * @param notsuspectids
     * @return |
     * @author Mrlu
     * @createTime 2021/3/8 10:09
     */
    List<FunArchiveRecordsDTO> selectRecordsNotSuspectByType(int archivetypeid,String [] notsuspectids);


     /**
     * 根据文书id查询该文书的关系表
     * @author Mrlu
     * @param recordid
     * @createTime  2021/3/8 10:34
     * @return    |
      */
    FunSuspectRecordDTO selectSuspectRecordByRid(int recordid);

    /**
     * 根据id查询嫌疑人信息
     * @author Mrlu
     * @param  ids
     * @createTime  2021/3/8 11:09
     * @return    |
     */
    List<FunSuspectDTO>  selectSuspectsByIds(String [] ids);

    /**
     * 新建嫌疑人信息
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 16:19
     */
     void insertNewSuspect(FunSuspectDTO record);

      /**
      * 新建嫌疑人文书关系
      * @author Mrlu
      * @param
      * @createTime  2021/3/8 14:16
      * @return    |
       */
    void createNewSR(FunSuspectRecordDTO record);

     /**
     * 新建文书
     * @author Mrlu
     * @param
     * @createTime  2021/3/8 14:19
     * @return    |
      */
    void createNewRecord(FunArchiveRecordsDTO record);


     /**
     * 查询文书的文件
     * @author Mrlu
     * @param
     * @createTime  2021/3/8 14:30
     * @return    |
      */
    List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid);

     /**
     * 查询文件是否有重复的文件代码
     * @author Mrlu
     * @param
     * @createTime  2021/3/8 14:34
     * @return    |
      */
    Integer selectRepeatedlyFileCodeBySeqid(String filecode, int archiveseqid);

     /**
     * 新建文件
     * @author Mrlu
     * @param
     * @createTime  2021/3/8 14:36
     * @return    |
      */
    void createFils(FunArchiveFilesDTO record);


    /**
     * 查询多个seq下不同type的文书  不包含系统文书
     * @param seqids
     * @param recordtype
     * @return |
     * @author Mrlu
     * @createTime 2021/3/10 17:46
     */

    List<FunArchiveRecordsDTO> selectRecordsBySeqType(List<Integer> seqids, Integer recordtype);

    /**
     * 判断这个文书是否对应某些的嫌疑人
     * @author Mrlu
     * @param recordid 文书id
     * @param array 嫌疑人id
     * @createTime  2021/3/10 18:11
     * @return    |
     */
    FunSuspectRecordDTO selectRecordBoolSuspect(Integer recordid,String []array);
    /**
     * 新建文书目录 封皮 封底
     * @author MrLu
     * @param record
     *@param  type
     * @createTime  2020/10/8 11:38
     * @return  void  |
     */
     void insertZlRecords(FunArchiveRecordsDTO record,FunArchiveTypeDTO type);
}
