package com.module.ArchiveManager.Services;

import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveType;
import com.bean.jzgl.Source.FunCaseInfo;
import com.bean.jzgl.Source.selectObj;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:47
 * @describe
 */
public interface RecordsService {

    FunArchiveRecordsDTO getFunArchiveRecordsById(Integer id);

    /**
     * 通过警情编号分页查询警情对应文书
     * @author MrLu
     * @param  map (pageStart、pageEnd、jqbh)
     * @createTime  2020/10/4 14:02
     * @return   List<Object> |
     */
    List<FunArchiveRecordsDTO> selectRecordsByJqbhPage(Map<String,Object> map) throws Exception;
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
    /**
     * 通过seqid查询这个案件正在活跃的基础卷
     *
     * @param seqid
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 20:14
     */
    FunArchiveSeqDTO selectBaseArchiveBySeqId(int seqid);
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
     * 按照送检卷类型和卷类型查询默认顺序
     *
     * @param  recordcode 文书代码
     * @param  archivetype 送检卷卷类型
     * @param  recordtype 文书卷类型
     * @return List<SysRecordorderDTO>    |
     * @author MrLu
     * @createTime 2020/11/26 15:23
     */
    SysRecordorderDTO selectRecordOrderByTypes(String recordcode, Integer archivetype, Integer recordtype);
     /**
     *
     * @author MrLu
     * @param
     * @createTime  2021/1/8 10:12
     * @return    |
      */
    SysRecordorderDTO selectSysRecordorderDTOById(int id);
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

    /**
     * 查询卷类型表
     *
     * @param seqId 送检次序id
     * @return List<FunArchiveTypeDTO>  |
     * @author MrLu
     * @createTime 2020/10/8 11:05
     */
    List<FunArchiveType> selectArchiveTypeByJqSeq(int seqId);
    /**
     * 根据typeid查找文书  根据thisorder排序
     *
     * @param archivetypeid typeid
     * @param isDelete      是否删除
     * @return List<FunArchiveRecordsDTO> |
     * @author MrLu
     * @createTime 2020/10/9 11:33
     */
    List<FunArchiveRecords> selectRecordsByTypeid(int archivetypeid, int isDelete,int notRecordstyle);


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
     * 插入文件
     * @author MrLu
     * @param
     * @createTime  2021/1/7 18:36
     * @return    |
      */
      void insertFunRecordFilesDTO(FunArchiveFilesDTO record);
    /**
     * 根据文书代码找到正在显示的文书
     * @author MrLu
     * @createTime  2020/10/22 9:32
     * @return  FunArchiveFilesDTO  |
     */
    FunArchiveFilesDTO selectFilesByFileCode(String filecode, int archiverecordid);

    /**
     * 根据文书的唯一标识码查询该seq中的文书
     * @author MrLu
     * @param recorduuid
     *  @param archiveseqid
     * @createTime  2021/1/8 9:41
     * @return    |
     */
    FunArchiveRecordsDTO selectRecordByUuidSeq(String recorduuid,
                                             Integer archiveseqid);

    /**
     * 根据id查询文书类型
     *
     * @param id id
     * @return FunArchiveTypeDTO  |
     * @author MrLu
     * @createTime 2020/10/14 10:09
     */
    FunArchiveTypeDTO selectFunArchiveTypeById(Integer id);

    /**
     * 查询该文书对嫌疑人的关联信息
     * @author MrLu
     * @param recordid
     * @createTime  2020/12/24 23:03
     * @return    |
     */
    FunSuspectRecordDTO selectSuspectRecordByRid (int recordid);

    /**
     * 查询一个type中对嫌疑人文书的最大顺序（不针对某个嫌疑人）
     * @author MrLu
     * @param typeid
     * @createTime  2021/3/25 16:19
     * @return    |
     */
    public int selectRsMaxOrderByTypeid(int typeid);
}
