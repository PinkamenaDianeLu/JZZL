package com.module.ArchiveManager.Services;

import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunCaseInfo;
import com.bean.jzgl.Source.selectObj;

import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:47
 * @describe
 */
public interface RecordsService {

    FunArchiveRecords getFunArchiveRecordsById(Integer id);

    /**
     * 通过警情编号分页查询警情对应文书
     * @author MrLu
     * @param  map (pageStart、pageEnd、jqbh)
     * @createTime  2020/10/4 14:02
     * @return   List<Object> |
     */
    List<Object> selectRecordsByJqbhPage(Map<String,Object> map) throws Exception;
    int selectRecordsByJqbhCount(Map<String,Object> map);
     /**
     * 通过id获取案件表
     * @author MrLu
     * @param id 案件表id
     * @createTime  2020/10/4 17:48
     * @return  FunPeopelCase  |
      */
     FunCaseInfo getFunCaseInfoById(Integer id);

    /**
     * 查询一个案件最原始的基础卷
     * @author MrLu
     * @param caseInfoId 案件信息表id
     * @createTime  2020/11/20 10:51
     * @return  FunArchiveSeqDTO  |
     */
    FunArchiveSeqDTO selectBaseArchive(int caseInfoId);
    FunArchiveSeqDTO  selectBaseArchiveBySeqId(int seqid);
    /**
     * 查询一个类型卷中该出现的文书代码
     * @author MrLu
     * @param
     * @createTime  2020/12/22 19:03
     * @return    |
     */
    List<selectObj>  selectRecordCodesByAtype(Map<String,Object> map);

    Integer selectRecordCodesByAtypeCount(Map<String,Object> map);

    /**
     * 根据id查找整理记录
     * @author MrLu
     * @param  id
     * @createTime  2020/10/13 16:30
     * @return  FunArchiveSeqDTO  |
     */
    FunArchiveSeqDTO selectFunArchiveSeqById(Integer id);


     /**
     * 通过id查询文书顺序表
     * @author MrLu
     * @param id
     * @createTime  2020/12/23 14:03
     * @return    |
      */
    SysRecordorderDTO selectSysRecordorderDTOById(Integer id);

    /**
     * 通过seqid查询该案件的嫌疑人
     * @author MrLu
     * @param seqid
     * @createTime  2020/12/23 15:00
     * @return    |
     */
    List<FunSuspectDTO> selectSuspectBySeqId(Integer seqid);
    /**
     * 查询该类型文书在某本卷中的某个类型的上一个文书
     * @author MrLu
     * @param
     * @createTime  2020/12/23 18:38
     * @return    |
     */
    FunArchiveRecordsDTO selectPriveRecord(int recordtype ,int archivetype  ,int defaultorder  ,int archiveseqid,Integer suspectid);

    /**
     * 把某个文书之后的文书顺序+1
     * @author MrLu
     * @param
     * @createTime  2020/12/23 18:36
     * @return    |
     */
    void updateOrderAdd(int archiveseqid,int archivetypeid,int thisorder);

     /**
     * 插入文书与嫌疑人关系表
     * @author MrLu
     * @param
     * @createTime  2020/12/23 21:26
     * @return    |
      */
    void insertSuspectRecord(FunSuspectRecordDTO record);

    /**
     * 新建文书
     * @author MrLu
     * @param record
     * @createTime  2020/10/13 17:58
     * @return    |
     */
    void insertFunArchiveRecords(FunArchiveRecordsDTO record);


    /**
     * 通过文书id查询文书的相关人
     * @author MrLu
     * @param recordid 文书id
     * @createTime  2020/11/25 17:40
     * @return List<FunSuspectRecordDTO>   |
     */
    List<FunSuspectDTO> selectSuspectByRecordid(Integer recordid);


}
