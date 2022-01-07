package com.module.ArchiveManager.Services;

import com.bean.jzgl.DTO.*;

import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/10/23 10:01
 * @describe
 */
public interface FileManipulationService {

    /**
     * 通过fileid查询文书封皮
     *
     * @param fileid
     * @return FunArchiveCoverDTO  |
     * @author MrLu
     * @createTime 2020/10/23 10:17
     */
    FunArchiveCoverDTO selectFunArchiveCoverDTOByFileId(Integer fileid);
    //行政
    FunArchiveXzcoverDTO selectFunArchiveXzCoverDTOByFileId (Integer fileid);
    /**
     * 通过fileid查询文书封底
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/25 16:25
     */
    FunArchiveBackcoverDTO selectFunArchiveBackCoverDTOByFileId(Integer fileid);

    /**
     * 根绝案件id查找案件
     *
     * @param caseId 案件id
     * @return FunCaseInfoDTO  |
     * @author MrLu
     * @createTime 2020/10/23 10:39
     */
    FunCaseInfoDTO selectFunCaseInfoDTOById(Integer caseId);

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
     * 根据文书id查找文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/23 10:57
     */
    FunArchiveRecordsDTO selectFunArchiveRecordsDTOById(Integer recordId);

    /**
     * 根据uuid查询相关文书
     *
     * @param recordUuid uuid
     * @return |
     * @author MrLu
     * @createTime 2021/3/31 14:19
     */
    List<FunArchiveRecordsDTO> selectFunArchiveRecordsByUUID(String recordUuid);

    /**
     * 根据文件代码查找文件列表
     *
     * @param filesCode
     * @param archiverecordid
     * @return |
     * @author MrLu
     * @createTime 2020/10/21 11:24
     */
    List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String[] filesCode, int archiverecordid);

    /**
     * 新建文书目录
     *
     * @param record
     * @return void  |
     * @author MrLu
     * @createTime 2020/10/29 14:55
     */
    void insertFunArchiveRecordindexDTO(FunArchiveRecordindexDTO record);

    /**
     * 更新文书目录
     *
     * @param record
     * @return void   |
     * @author MrLu
     * @createTime 2020/10/29 15:05
     */
    void updateFunArchiveRecordindexDTO(FunArchiveRecordindexDTO record);

    /**
     * 根据id查询archivefile表
     *
     * @param id 表id
     * @return FunArchiveFilesDTO   |
     * @author MrLu
     * @createTime 2020/10/29 15:04
     */
    FunArchiveFilesDTO selectFunArchiveFilesDTOById(Integer id);

    /**
     * 根据送检次序id和文书类型id查询这个文书类型的文书目录信息
     *
     * @param archiveseqid
     * @param archivetypeid
     * @return |
     * @author MrLu
     * @createTime 2020/10/29 15:18
     */
    FunArchiveRecordindexDTO selectRecordIndexByTypeId(Integer archiveseqid, Integer archivetypeid);

    /**
     * 更新卷宗封皮
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/30 15:44
     */
    void updateFunArchiveCoverById(FunArchiveCoverDTO record);
    void updateFunArchiveCoverById(FunArchiveXzcoverDTO record);
    /**
     * 更新卷宗封底
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/25 16:14
     */
    void updateFunArchiveBackCoverById(FunArchiveBackcoverDTO FunArchiveBackcoverDTO);

    /**
     * 新建卷宗封皮
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/30 15:57
     */
    void insertFunArchiveCover(FunArchiveCoverDTO record);
    void insertFunArchiveCover(FunArchiveXzcoverDTO record);//行政
    /**
     * 新建卷宗封底
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/25 16:10
     */
    void insertFunArchiveBackCover(FunArchiveBackcoverDTO FunArchiveBackcoverDTO);


    /**
     * 分页查询文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/11/5 17:39
     */
    List<Object> selectArchiveRecordPage(Map<String, Object> map) throws Exception;

    int selectArchiveRecordPageCount(Map<String, Object> map);

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
     * 根据文件代码和整理次序id更新文件
     *
     * @param record
     * @return void  |
     * @author MrLu
     * @createTime 2020/11/7 12:17
     */
    void updateFileByFileCode(FunArchiveFilesDTO record);

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
     * 按照id更新文书文件
     *
     * @param record
     * @return |
     * @author MrLu
     * @createTime 2020/12/25 15:43
     */
    void updateFunArchiveFileDTO(FunArchiveFilesDTO record);

    /**
     * 将一个文书内顺序大于该值的文件顺序+1
     *
     * @return void |
     * @author MrLu
     * @createTime 2020/11/2 16:47
     */
    void updateOrderByRecordId(int archiverecordid, int thisorder, String filecode);

    /**
     * 根据文书代码找到正在显示的文书
     *
     * @param filecode:文件代码,
     * @param archiverecordid:文书id}
     * @return FunArchiveFilesDTO  |
     * @author MrLu
     * @createTime 2020/10/22 9:32
     */
    FunArchiveFilesDTO selectFilesByFileCode(int archiverecordid, String filecode);

    /**
     * 按照seqid查询本次送检的嫌疑人
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/20 16:11
     */
    List<FunSuspectDTO> selectSuspectByArchiveseqid(Integer archiveseqid);

    /**
     * 查询一个文书卷下有多少页
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/23 11:08
     */
    Integer selectFilesCountByTypeid(Integer archivetypeid);


    /**
     * 查询一个文书下有多少页
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/23 15:39
     */
    Integer selectFilecCountByRecordid(Integer recordid);

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
     * 查询不同seq下的相同type
     *
     * @param archiveseqid seqid
     * @param archiveseqid id
     * @return |
     * @author MrLu
     * @createTime 2021/3/31 14:06
     */
    FunArchiveTypeDTO selectSameTypeWithSeq(Integer archiveseqid, Integer id);

    /**
     * 文件代码查询未发送的文书图片
     *
     * @param filecode
     * @return |
     * @author MrLu
     * @createTime 2021/4/7 16:29
     */
    List<FunArchiveFilesDTO> selectFilesByCodeNotSend(String filecode);
}
