package com.module.SFCensorship.Services;


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
}
