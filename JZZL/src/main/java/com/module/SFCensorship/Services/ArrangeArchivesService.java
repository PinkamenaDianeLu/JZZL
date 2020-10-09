package com.module.SFCensorship.Services;


import com.bean.jzgl.Source.FunArchiveRecords;
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
     * @createTime  2020/10/9 11:33
     * @return   List<FunArchiveRecordsDTO> |
     */
    List<FunArchiveRecords>  selectRecordsByTypeid(int archivetypeid);
}
