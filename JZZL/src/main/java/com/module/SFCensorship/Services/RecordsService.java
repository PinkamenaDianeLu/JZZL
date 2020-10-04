package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunPeopelCase;

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
     * @return  List<FunArchiveRecordsDTO>  |
     */
    List<FunArchiveRecords> selectRecordsByJqbhPage(Map<String,Object> map);
    int selectRecordsByJqbhCount(Map<String,Object> map);
     /**
     * 通过id获取案件表
     * @author MrLu
     * @param id 案件表id
     * @createTime  2020/10/4 17:48
     * @return  FunPeopelCase  |
      */
    FunPeopelCase getFunPeopleCaseById(Integer id);
}
