package com.module.SFCensorship.Services;


import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunArchiveType;

import java.util.List;

public interface ArrangeArchivesService {
    /**
     * 查询卷类型表
     * @author MrLu
     * @param seqId 送检次序id
     * @createTime  2020/10/8 11:05
     * @return  List<FunArchiveTypeDTO>  |
     */
    List<FunArchiveType> selectArchiveTypeByJqSeq(int seqId);


    /**
     * 根据typeid查找文书  根据thisorder排序
     * @author MrLu
     * @param archivetypeid  typeid
     *                       @param isDelete  是否删除
     * @createTime  2020/10/9 11:33
     * @return   List<FunArchiveRecordsDTO> |
     */
    List<FunArchiveRecords>  selectRecordsByTypeid(int archivetypeid,int isDelete);

     /**
     * 根据id查找整理记录
     * @author MrLu
     * @param  id
     * @createTime  2020/10/13 16:30
     * @return  FunArchiveSeqDTO  |
      */
    FunArchiveSeqDTO selectFunArchiveSeqById(Integer id);

     /**
     * 查询送检记录下最后一次整理
     * @author MrLu
     * @param sfcId 送检记录id
     * @createTime  2020/10/11 16:13
     * @return  FunArchiveSeq  |
      */
    FunArchiveSeq selectLastSeqBySfc(int sfcId);
    /**
     *根据id查询文书
     * @author MrLu
     * @param id
     * @createTime  2020/10/13 16:37
     * @return   FunArchiveRecords |
     */
    FunArchiveRecordsDTO selectFunArchiveRecordsById(Integer id);

     /**
     * 新建整理记录
     * @author MrLu
     * @param  record
     * @createTime  2020/10/13 17:35
     * @return    |
      */
    void insertFunArchiveSeq(FunArchiveSeqDTO record);

     /**
     * 新建文书
     * @author MrLu
     * @param record
     * @createTime  2020/10/13 17:58
     * @return    |
      */
    void insertFunArchiveRecords(FunArchiveRecordsDTO record);


     /**
     * 新建文书类型
     * @author MrLu
     * @param funArchiveTypeDTO
     * @createTime  2020/10/14 10:07
      */
    void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO);


     /**
     * 根据id查询文书类型
     * @author MrLu
     * @param id id
     * @createTime  2020/10/14 10:09
     * @return  FunArchiveTypeDTO  |
      */
    FunArchiveTypeDTO selectFunArchiveTypeById(Integer id);

    /**
     * 通过文书id查找其文书图片（最新版本的）
     * @author MrLu
     * @param archiverecordid 文书id
     * @createTime  2020/10/15 17:58
     * @return  FunArchiveFilesDTO  |
     */
    List<FunArchiveFilesDTO>  selectRecordFilesByRecordId(int archiverecordid,Integer isdelete);
     /**
     * 根据文件代码查找文件列表
     * @author MrLu
      * @param  filesCode  文件代码
      * @param seqId  文书id
     * @createTime  2020/10/21 11:24
     * @return    |
      */
    List<FunArchiveFilesDTO>  selectRecordFilesByFileCodes(String[] filesCode,int seqId);

    /**
     * 根据文件代码查询该文件的历史版本
     * @author MrLu
     * @param
     * @createTime  2020/10/15 18:13
     * @return    |
     */
    List<FunArchiveFilesDTO> selectFilesHistory(String filecode);

     /**
     * 根据文书代码查询显示的文书
     * @author MrLu
     * @param  filecode  文件代码
      * @param archiverecordid  文书id
     * @createTime  2020/10/27 14:52
     * @return  FunArchiveFilesDTO  |
      */
    FunArchiveFilesDTO  selectFilesByFileCode(String filecode,int archiverecordid);
     /**
     * 新建文件
     * @author MrLu
     * @param record
     * @createTime  2020/10/27 14:27
     * @return  void  |
      */
    void insertFunArchiveFilesDTO(FunArchiveFilesDTO record);

    /**
     * 查询该文书的下一个版本
     * @author MrLu
     * @param id 文书od
     * @createTime  2020/10/27 14:51
     * @return  FunArchiveRecordsDTO  |
     */
    FunArchiveRecordsDTO selectNextRecord(Integer id);

     /**
     * 更新文书
     * @author MrLu
     * @param record
     * @createTime  2020/10/27 14:57
     * @return void   |
      */
    void updateFunArchiveRecordsById(FunArchiveRecordsDTO record);

    /**
     * 根据文书id更新文件所属的文书类型
     * @author MrLu
     * @param record
     * @createTime  2020/11/2 16:48
     * @return  void  |
     */
    void updateFileByRecordId(FunArchiveFilesDTO record);

    /**
     * 根据文件代码更新文件所属的record
     * @author MrLu
     * @param record
     * @createTime  2020/11/2 16:48
     * @return  void  |
     */
    void updateFileByFileCode(FunArchiveFilesDTO record);

    /**
     * 将一个文书类型内顺序大于该值的文书顺序+1
     * @author MrLu
     * @param archivetypeid 文书类型id
     *@param thisorder 起始顺序
     * @createTime  2020/11/2 17:59
     * @return    |
     */
    void updateRecordOrderByTypeId(int archivetypeid,int thisorder);

    /**
     * 将一个文书内顺序大于该值的文件顺序+1
     * @author MrLu
     * @param archiverecordid
     * @param  thisorder
     * @createTime  2020/11/2 16:47
     * @return   void |
     */
    void updateOrderByRecordId(int archiverecordid,int thisorder);
}
