package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SFCensorshipService {

    /**
     * 分页查询送检记录表
     * @author MrLu
     * @param map 分页参数
     * @createTime  2020/9/27 15:48
     * @return    |
     */
    List<FunArchiveSFC> selectArchiveSFCPage(Map<String,Object> map);
    int selectArchiveSFCPageCount(Map<String,Object> map);

     /**
     * 新建送检整理次序
     * @author MrLu
     * @param record
     * @createTime  2020/10/8 13:58
     * @return  void  |
      */
    void insertFunArchiveSeq(FunArchiveSeq record);
    void insertFunArchiveSeq(FunArchiveSeqDTO record);
    void insertFunArchiveSFC(FunArchiveSFC funArchiveSFC);
     /**
     * 通过id查询人案表
     * @author MrLu
     * @param id 表id
     * @createTime  2020/10/4 16:19
     * @return  FunCaseInfo  |
      */
     FunCaseInfo getFunCaseInfoById(Integer id);
    /**
     *  查询案件的基础卷
     * @author MrLu
     * @param
     * @createTime  2020/12/17 18:00
     * @return    |
     */
    FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid);
    /**
     * 得到某个案件最后的送检次序
     * @author MrLu
     * @param caseinfoid 案件表id
     * @createTime  2020/10/4 16:31
     * @return   int  |
     */
    int getLastSFCSeq(int caseinfoid);

    /**
     * 根据typeid查找文书  根据thisorder排序
     * @author MrLu
     * @param archivetypeid
     * @createTime  2020/10/9 11:33
     * @return   List<FunArchiveRecordsDTO> |
     */
    List<FunArchiveRecordsDTO>  selectRecordsByTypeid(int archivetypeid);

    /**
     * 查询卷类型表
     * @author MrLu
     * @param archiveseqid 送检次序id
     * @createTime  2020/10/8 11:05
     * @return  List<FunArchiveTypeDTO>  |
     */
    List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(int archiveseqid);


     /**
     * 新建卷类型
     * @author MrLu
     * @param funArchiveTypeDTO
     * @createTime  2020/10/8 11:18
     * @return   void |
      */
    void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO);


     /**
     * 新建文书目录 封皮 封底
     * @author MrLu
     * @param record
      *@param  type
     * @createTime  2020/10/8 11:38
     * @return  void  |
      */
   void insertZlRecords(FunArchiveRecordsDTO record,FunArchiveTypeDTO type);
    /**
    * 新建文书图片
    * @author MrLu
    * @param record
    * @createTime  2020/10/24 11:57
    * @return   void |
     */
     void insertFunArchiveFilesDTO(FunArchiveFilesDTO record) ;


       /**
       * 通过id更新文书表
       * @author MrLu
       * @param record
       * @createTime  2020/12/5 15:49
       * @return    |
        */
      void  updateFunArchiveRecordById(FunArchiveRecordsDTO record);
    /**
     * 按照seqId查询对应案件下的嫌疑人
     * @author MrLu
     * @param  id  eqId
     * @createTime  2020/12/8 18:47
     * @return  List<FunSuspectDTO>  |
     */
    List<FunSuspectDTO>   selectSuspectById(Integer id);

    /**
     * 查询某个案件正在活跃的基础卷
     * @author MrLu
     * @param  caseinfoid 案件表id
     * @createTime  2020/11/27 9:22
     * @return  FunArchiveSeqDTO  |
     */
    FunArchiveSeqDTO  selectActiveSeqByCaseId(int caseinfoid);

    /**
     * 更新一个送检卷下的整理次序都是不活跃状态
     * @author MrLu
     * @param archivesfcid 送检卷id
     * @createTime  2020/12/10 9:27
     * @return    |
     */
    void updateBaseSeqIsNotActive(int archivesfcid);

    /**
     * 新建文书
     * @author MrLu
     * @param record
     * @createTime  2020/10/13 17:58
     * @return    |
     */
    void insertFunArchiveRecords(FunArchiveRecordsDTO record);
    /**
     * 通过文书id查找其文书图片（最新版本的）
     * @author MrLu
     * @param archiverecordid 文书id
     * @createTime  2020/10/15 17:58
     * @return  FunArchiveFilesDTO  |
     */
    List<FunArchiveFilesDTO>  selectRecordFilesByRecordId(int archiverecordid,Integer isdelete);
    /**
     * 更新嫌疑人在案件中的优先级顺序
     * @author MrLu
     * @param
     * @createTime  2020/11/22 18:41
     * @return    |
     */
    void updateSuspectDefaultOrder(FunSuspectDTO record);

    /**
     * 查询某种类型的卷的文书顺序
     * @author MrLu
     * @param archivetype 0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     * @createTime  2020/12/15 11:11
     * @return    |
     */
    List<SysRecordorderDTO> selectSysRecordOrderByArchiveType(int archivetype);

    /**
     * 查询某一整理次序下对应文书代码的文书
     * @author MrLu
     * @param archiveseqid
     * @param recordscode 文书代码
     * @createTime  2020/12/15 14:31
     * @return    |
     */
    List<FunArchiveRecordsDTO>  selectReocrdBySeqRcode(Integer archiveseqid,
                                                       String recordscode,Integer recordtype);

    /**
     * 查询一个案件下所有活跃的且未被送检的文书整理次序
     * @author MrLu
     * @param caseinfoid 案件id
     * @createTime  2020/12/15 15:09
     * @return    |
     */
    List<FunArchiveSeqDTO> selectActiveSeqByCaseInfoId(int caseinfoid);

    /**
     * 根据卷类型查找文书类型的应有排序
     * @author MrLu
     * @param
     * @createTime  2020/12/17 11:14
     * @return    |
     */
    List<SysRecordtypeorderDTO> selectRecordtypeorderByArchivetype(Integer archivetype);

    /**
     * 将案件id下的基础卷标记为已为嫌疑人排序
     * @author MrLu
     * @param
     * @createTime  2020/12/17 17:43
     * @return    |
     */
    void updateIssuspectorderBySfcId(Integer issuspectorder,Integer seqid);



    /**
     * 查某个卷下某个类型对某个人的文书  按照顺序排序
     * @author MrLu
     * @param suspectid 嫌疑人id
     * @param archivetype 0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     * @param recordtype 1诉讼文书卷  2证据材料卷  3补充侦查卷
     * @param archiveseqid seqid
     * @createTime  2020/12/21 9:38
     * @return    |
     */
    List<FunArchiveRecordsDTO> selectRecordOrderForSuspect(int suspectid, int archivetype, int recordtype, int archiveseqid);
    /**
     * 查询该文书对嫌疑人的关联信息
     * @author MrLu
     * @param recordid
     * @createTime  2020/12/24 23:03
     * @return    |
     */
    FunSuspectRecordDTO selectSuspectRecordByRid (int recordid);
     /**
     * 插入嫌疑人文书关联表
     * @author MrLu
     * @param  record
     * @createTime  2020/12/24 23:19
     * @return    |  
      */
    void insertSuspectRecord(FunSuspectRecordDTO record);
}
