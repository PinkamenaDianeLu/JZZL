package com.module.SFCensorship.Services;

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
     * @author MrLu
     * @param fileid
     * @createTime  2020/10/23 10:17
     * @return  FunArchiveCoverDTO  |
      */
    FunArchiveCoverDTO selectFunArchiveCoverDTOByFileId (Integer fileid);

     /**
     * 根绝案件id查找案件
     * @author MrLu
     * @param caseId 案件id
     * @createTime  2020/10/23 10:39
     * @return  FunCaseInfoDTO  |
      */
    FunCaseInfoDTO selectFunCaseInfoDTOById (Integer caseId);

     /**
     * 根据送检卷id查询送检卷
     * @author MrLu
     * @param
     * @createTime  2020/10/23 10:56
     * @return    |
      */
    FunArchiveSFCDTO selectFunArchiveSFCDTOById (Integer sfcId);
     /**
     * 根据文书id查找文书
     * @author MrLu
     * @param
     * @createTime  2020/10/23 10:57
     * @return    |
      */
    FunArchiveRecordsDTO selectFunArchiveRecordsDTOById (Integer recordId);

    /**
     * 根据文件代码查找文件列表
     * @author MrLu
     * @param filesCode
     * @param archiverecordid
     * @createTime  2020/10/21 11:24
     * @return    |
     */
    List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String[] filesCode,int archiverecordid);

     /**
     * 新建文书目录
     * @author MrLu
     * @param record
     * @createTime  2020/10/29 14:55
     * @return  void  |
      */
    void insertFunArchiveRecordindexDTO(FunArchiveRecordindexDTO record);

     /**
     * 更新文书目录
     * @author MrLu
     * @param record
     * @createTime  2020/10/29 15:05
     * @return void   |
      */
    void updateFunArchiveRecordindexDTO(FunArchiveRecordindexDTO record);
     /**
     * 根据id查询archivefile表
     * @author MrLu
     * @param id 表id
     * @createTime  2020/10/29 15:04
     * @return FunArchiveFilesDTO   |
      */
    FunArchiveFilesDTO selectFunArchiveFilesDTOById(Integer id);

    /**
     * 根据送检次序id和文书类型id查询这个文书类型的文书目录信息
     * @author MrLu
     * @param archiveseqid
     * @param archivetypeid
     * @createTime  2020/10/29 15:18
     * @return    |
     */
    FunArchiveRecordindexDTO selectRecordIndexByTypeId(Integer archiveseqid,Integer archivetypeid);

     /**
     * 更新卷宗封皮
     * @author MrLu
     * @param 
     * @createTime  2020/10/30 15:44
     * @return    |  
      */
    void  updateFunArchiveCoverById(FunArchiveCoverDTO record);
     /**
     * 新建卷宗封皮
     * @author MrLu
     * @param 
     * @createTime  2020/10/30 15:57
     * @return    |  
      */
    void  insertFunArchiveCover(FunArchiveCoverDTO record);


    /**
     * 分页查询文书
     * @author MrLu
     * @param
     * @createTime  2020/11/5 17:39
     * @return    |
     */
    List<Object> selectArchiveRecordPage(Map<String,Object> map) throws Exception;
    int selectArchiveRecordPageCount(Map<String,Object> map);
}
