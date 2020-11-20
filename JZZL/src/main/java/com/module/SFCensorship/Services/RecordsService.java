package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunCaseInfo;
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
     * @param peoplecaseid 人员案件表id
     * @createTime  2020/11/20 10:51
     * @return  FunArchiveSeqDTO  |
     */
    FunArchiveSeqDTO selectBaseArchive(int peoplecaseid);


}
