package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunCaseInfo;

import java.util.List;
import java.util.Map;

public interface SFCensorshipService {

    /**
     * 分页查询送检记录表
     *
     * @param map 分页参数
     * @return |
     * @author MrLu
     * @createTime 2020/9/27 15:48
     */
    List<FunArchiveSFC> selectArchiveSFCPage(Map<String, Object> map);

    int selectArchiveSFCPageCount(Map<String, Object> map);

    /**
     * 新建送检整理次序
     *
     * @param record
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/8 13:58
     */
    void insertFunArchiveSeq(FunArchiveSeq record);

    void insertFunArchiveSeq(FunArchiveSeqDTO record);

    void insertFunArchiveSFC(FunArchiveSFC funArchiveSFC);

    /**
     * 通过id查询人案表
     *
     * @param id 表id
     * @return FunCaseInfo  |
     * @author MrLu
     * @createTime 2020/10/4 16:19
     */
    FunCaseInfo getFunCaseInfoById(Integer id);

    /**
     * 查询案件的基础卷
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/17 18:00
     */
    FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid,Integer archivetype);

    /**
     * 得到某个案件最后的送检次序
     *
     * @param caseinfoid 案件表id
     * @return int  |
     * @author MrLu
     * @createTime 2020/10/4 16:31
     */
    int getLastSFCSeq(int caseinfoid);

    /**
     * 根据typeid查找文书  根据thisorder排序
     *
     * @param archivetypeid
     * @return List<FunArchiveRecordsDTO> |
     * @author MrLu
     * @createTime 2020/10/9 11:33
     */
    List<FunArchiveRecordsDTO> selectRecordsByTypeid(int archivetypeid);

    /**
     * 查询卷类型表
     *
     * @param archiveseqid 送检次序id
     * @return List<FunArchiveTypeDTO>  |
     * @author MrLu
     * @createTime 2020/10/8 11:05
     */
    List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(int archiveseqid);


    /**
     * 新建卷类型
     *
     * @param funArchiveTypeDTO
     * @return void |
     * @author MrLu
     * @createTime 2020/10/8 11:18
     */
    void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO);


    /**
     * 新建文书目录 封皮 封底
     *
     * @param record
     * @param type
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/8 11:38
     */
    void insertZlRecords(FunArchiveRecordsDTO record, FunArchiveTypeDTO type);

    /**
     * 新建文书图片
     *
     * @param record
     * @return void |
     * @author MrLu
     * @createTime 2020/10/24 11:57
     */
    void insertFunArchiveFilesDTO(FunArchiveFilesDTO record);

    /**
     * 查询一个seq下是否有重复的filecode
     *
     * @param filecode
     * @param archiveseqid
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 18:19
     */
    Integer selectRepeatedlyFileCodeBySeqid(String filecode, int archiveseqid);

    /**
     * 通过id更新文书表
     *
     * @param record
     * @return |
     * @author MrLu
     * @createTime 2020/12/5 15:49
     */
    void updateFunArchiveRecordById(FunArchiveRecordsDTO record);

    /**
     * 按照seqId查询对应案件下的嫌疑人
     *
     * @param id eqId
     * @return List<FunSuspectDTO>  |
     * @author MrLu
     * @createTime 2020/12/8 18:47
     */
    List<FunSuspectDTO> selectSuspectById(Integer id);

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
     * 更新一个送检卷下的整理次序都是不活跃状态
     *
     * @param archivesfcid 送检卷id
     * @return |
     * @author MrLu
     * @createTime 2020/12/10 9:27
     */
    void updateBaseSeqIsNotActive(int archivesfcid);

    /**
     * 新建文书
     *
     * @param record
     * @return |
     * @author MrLu
     * @createTime 2020/10/13 17:58
     */
    void insertFunArchiveRecords(FunArchiveRecordsDTO record);

    /**
     * 通过文书id查找其文书图片（最新版本的）
     *
     * @param archiverecordid 文书id
     * @return FunArchiveFilesDTO  |
     * @author MrLu
     * @createTime 2020/10/15 17:58
     */
    List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid, Integer isdelete);

    /**
     * 更新嫌疑人在案件中的优先级顺序
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/11/22 18:41
     */
    void updateSuspectDefaultOrder(FunSuspectDTO record);

    /**
     * 查询某种类型的卷的文书顺序
     *
     * @param archivetype 0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 11:11
     */
    List<SysRecordorderDTO> selectSysRecordOrderByArchiveType(int archivetype);

    /**
     * 查询某一整理次序下对应文书代码的文书
     *
     * @param archiveseqid
     * @param recordscode  文书代码
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 14:31
     */
    List<FunArchiveRecordsDTO> selectReocrdBySeqRcode(Integer archiveseqid,
                                                      String recordscode, Integer recordtype);

    /**
     * 查询一个案件下所有活跃的且未被送检的文书整理次序
     *
     * @param caseinfoid 案件id
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 15:09
     */
    List<FunArchiveSeqDTO> selectActiveSeqByCaseInfoId(int caseinfoid);

    /**
     * 根据卷类型查找文书类型的应有排序
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/17 11:14
     */
    List<SysRecordtypeorderDTO> selectRecordtypeorderByArchivetype(Integer archivetype);

    /**
     * 将案件id下的基础卷标记为已为嫌疑人排序
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/17 17:43
     */
    void updateIssuspectorderBySfcId(Integer issuspectorder, Integer seqid);


    /**
     * 查某个卷下某个类型对某个人的文书  按照顺序排序
     *
     * @param suspectid    嫌疑人id
     * @param archivetype  0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     * @param recordtype   1诉讼文书卷  2证据材料卷  3补充侦查卷
     * @param archiveseqid seqid
     * @return |
     * @author MrLu
     * @createTime 2020/12/21 9:38
     */
    List<FunArchiveRecordsDTO> selectRecordOrderForSuspect(int suspectid, int archivetype, int recordtype, int archiveseqid);

    /**
     * 查询该文书对嫌疑人的关联信息
     *
     * @param recordid
     * @return |
     * @author MrLu
     * @createTime 2020/12/24 23:03
     */
    List<FunSuspectRecordDTO>  selectSuspectRecordByRid(int recordid);

    /**
     * 插入嫌疑人文书关联表
     *
     * @param record
     * @return |
     * @author MrLu
     * @createTime 2020/12/24 23:19
     */
    void insertSuspectRecord(FunSuspectRecordDTO record);


    /**
     * 查询文书对应的嫌疑人信息
     *
     * @param recordid 文书id
     * @return |
     * @author Mrlu
     * @createTime 2021/2/19 14:43
     */
    List<FunSuspectDTO> selectSuspectByRecordid(Integer recordid);


    /**
     * 查询该嫌疑人对应的文书
     * @author Mrlu
     * @param archivetypeid
     * @param suspectid
     * @createTime  2021/2/19 16:46
     * @return    |
     */
    List<FunArchiveRecordsDTO>  selectRecordsBySuspects(Integer archivetypeid,List<Integer> suspectid);


    /**
     * 根据文书id查询对应的标签
     * @author MrLu
     * @param recordid 文书id
     * @createTime  2021/4/8 14:22
     * @return    |
     */
    List<FunArchiveTagsDTO>  selectRecordByRecordId(Integer recordid);
    /**
     * 创建新的标签
     * @author MrLu
     * @param funArchiveTagsDTO
     * @createTime  2020/12/7 19:25
     * @return    |
     */
    void createNewTags(FunArchiveTagsDTO funArchiveTagsDTO);

    /**
     * 审批卷宗
     * @author MrLu
     * @param
     * @createTime  2021/6/16 15:23
     * @return    |
     */
    void  approvalArchive(Integer approval,Integer id);

    /**
     * 更改一个sfc的发送状态
     * @author MrLu
     * @param
     * @createTime  2021/12/18 12:26
     * @return    |
     */
    void updateSendTypeById(Integer issend,Integer sfcid);
}
