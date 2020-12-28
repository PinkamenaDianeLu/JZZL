package com.module.ArchiveManager.Services;


import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunArchiveType;

import java.util.List;

public interface ArrangeArchivesService {
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
    List<FunArchiveRecords> selectRecordsByTypeid(int archivetypeid, int isDelete);


    List<FunArchiveRecordsDTO> selectRecordsDtoByTypeid(int archivetypeid, Integer isDelete);

    /**
     * 根据id查找整理记录
     *
     * @param id
     * @return FunArchiveSeqDTO  |
     * @author MrLu
     * @createTime 2020/10/13 16:30
     */
    FunArchiveSeqDTO selectFunArchiveSeqById(Integer id);

    /**
     * 查询送检记录下最后一次整理
     *
     * @param sfcId 送检记录id
     * @return FunArchiveSeq  |
     * @author MrLu
     * @createTime 2020/10/11 16:13
     */
    FunArchiveSeq selectLastSeqBySfc(int sfcId);

    /**
     * 根据id查询文书
     *
     * @param id
     * @return FunArchiveRecords |
     * @author MrLu
     * @createTime 2020/10/13 16:37
     */
    FunArchiveRecordsDTO selectFunArchiveRecordsById(Integer id);

    /**
     * 新建整理记录
     *
     * @param record
     * @return |
     * @author MrLu
     * @createTime 2020/10/13 17:35
     */
    void insertFunArchiveSeq(FunArchiveSeqDTO record);

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
     * 新建文书类型
     *
     * @param funArchiveTypeDTO
     * @author MrLu
     * @createTime 2020/10/14 10:07
     */
    void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO);


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
     * 通过文书id查找其文书图片（最新版本的）
     *
     * @param archiverecordid 文书id
     * @return FunArchiveFilesDTO  |
     * @author MrLu
     * @createTime 2020/10/15 17:58
     */
    List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid, Integer isdelete);

    /**
     * 根据文件代码查找文件列表
     *
     * @param filesCode 文件代码
     * @param recordId  文书id
     * @return |
     * @author MrLu
     * @createTime 2020/10/21 11:24
     */
    List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String[] filesCode, int recordId);

    /**
     * 根据文件代码查询该文件的历史版本
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/15 18:13
     */
    List<FunArchiveFilesDTO> selectFilesHistory(String filecode);

    /**
     * 根据文书代码查询显示的文书
     *
     * @param filecode        文件代码
     * @param archiverecordid 文书id
     * @return FunArchiveFilesDTO  |
     * @author MrLu
     * @createTime 2020/10/27 14:52
     */
    FunArchiveFilesDTO selectFilesByFileCode(String filecode, int archiverecordid);

    /**
     * 新建文件
     *
     * @param record
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/27 14:27
     */
    void insertFunArchiveFilesDTO(FunArchiveFilesDTO record);

    /**
     * 查询该文书的下一个版本
     *
     * @param id 文书od
     * @return FunArchiveRecordsDTO  |
     * @author MrLu
     * @createTime 2020/10/27 14:51
     */
    FunArchiveRecordsDTO selectNextRecord(Integer id);

    /**
     * 更新文书
     *
     * @param record
     * @return void   |
     * @author MrLu
     * @createTime 2020/10/27 14:57
     */
    void updateFunArchiveRecordsById(FunArchiveRecordsDTO record);

    /**
     * 根据文书id更新文件所属的文书类型
     *
     * @param record
     * @return void  |
     * @author MrLu
     * @createTime 2020/11/2 16:48
     */
    void updateFileByRecordId(FunArchiveFilesDTO record);

    /**
     * 根据文件代码更新文件所属的record
     *
     * @param record
     * @return void  |
     * @author MrLu
     * @createTime 2020/11/2 16:48
     */
    void updateFileByFileCode(FunArchiveFilesDTO record);

    /**
     * 将一个文书类型内顺序大于该值的文书顺序+1
     *
     * @param archivetypeid 文书类型id
     * @param thisorder     起始顺序
     * @return |
     * @author MrLu
     * @createTime 2020/11/2 17:59
     */
    void updateRecordOrderByTypeId(int archivetypeid, int id, int thisorder);

    /**
     * 将一个文书内顺序大于该值的文件顺序+1
     *
     * @param archiverecordid
     * @param thisorder
     * @param filecode        文件代码
     * @return void |
     * @author MrLu
     * @createTime 2020/11/2 16:47
     */
    void updateFileOrder(int archiverecordid, int thisorder, String filecode);

    /**
     * 查询一个文书中的最大顺序
     *
     * @param archiverecordid 文书id
     * @return int |
     * @author MrLu
     * @createTime 2020/11/5 14:59
     */
    int selectFileMaxOrder(int archiverecordid);

    /**
     * 查询一个文书类型中的最大顺序数
     *
     * @param id 文书id
     * @return int   |
     * @author MrLu
     * @createTime 2020/11/5 15:02
     */
    int selectRecordMaxOrder(int id);


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
     * 根据送检卷id查询送检卷
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/23 10:56
     */
    FunArchiveSFCDTO selectFunArchiveSFCDTOById(Integer sfcId);


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
     * 通过文书id查询文书的相关人
     *
     * @param recordid 文书id
     * @return List<FunSuspectRecordDTO>   |
     * @author MrLu
     * @createTime 2020/11/25 17:40
     */
    List<FunSuspectDTO> selectSuspectByRecordid(Integer recordid);


    /**
     * 查询该嫌疑人某类型的文书  按照sys_recordorder顺序排序
     *
     * @param suspectid 嫌疑人id
     * @return List<FunArchiveRecordsDTO>  |
     * @author MrLu
     * @createTime 2020/11/26 17:01
     */
    List<FunArchiveRecordsDTO> selectRecordsBySuspect(Integer suspectid, Integer recordtype, Integer archiveseqid, Integer archivetype);


    /**
     * 按照送检卷类型和卷类型查询默认顺序
     *
     * @param recordcode  文书代码
     * @param archivetype 送检卷卷类型
     * @param recordtype  文书卷类型
     * @return List<SysRecordorderDTO>    |
     * @author MrLu
     * @createTime 2020/11/26 15:23
     */
    SysRecordorderDTO selectRecordOrderByTypes(String recordcode, Integer archivetype, Integer recordtype);

    /**
     * 查询案件的基础卷
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/17 18:00
     */
    FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid);

    /**
     * 查询一个人的对应文书
     *
     * @param suspectid  嫌疑人id
     * @param recordtype 文书类型
     * @return List<FunSuspectRecordDTO>  |
     * @author MrLu
     * @createTime 2020/11/26 15:27
     */
    List<FunSuspectRecordDTO> selectRecordBySuspectid(int suspectid, int recordtype);


    /**
     * 根据id更新ArchiveSfc表
     *
     * @param funArchiveSFCDTO 得有id哦
     * @return |
     * @author MrLu
     * @createTime 2020/12/3 13:24
     */
    void updateArchiveSfcById(FunArchiveSFCDTO funArchiveSFCDTO);


    /**
     * 根据id查询FunArchiveSFC表
     *
     * @param id 表id
     * @return FunArchiveSFC  |
     * @author MrLu
     * @createTime 2020/12/3 13:33
     */
    FunArchiveSFC selectFunArchiveSFCById(int id);


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
     * 根据文书名称查询
     *
     * @param recordname   文书名
     * @param archiveseqid archiveseqid
     * @return |
     * @author MrLu
     * @createTime 2020/12/25 17:19
     */
    List<FunArchiveRecordsDTO> selectRecordbyName(String recordname, Integer archiveseqid);

}
