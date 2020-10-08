package com.module.SFCensorship.Services;

import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunPeopelCase;

import java.util.List;
import java.util.Map;

public interface SFCensorshipService {

    /**
     * 分页查询送检记录表
     * @author MrLu
     * @param map 分页参数
     * @createTime  2020/9/27 15:48
     * @return    |
     */
    List<FunArchiveSeq> selectArchiveSeqPage(Map<String,Object> map);
    int selectArchiveSeqPageCount(Map<String,Object> map);

    void insertSelective(FunArchiveSeq record);
     /**
     * 通过id查询人案表
     * @author MrLu
     * @param id 表id
     * @createTime  2020/10/4 16:19
     * @return  FunPeopelCase  |
      */
    FunPeopelCase getFunPeopelCaseById(Integer id);

    /**
     * 得到某个案件最后的送检次序
     * @author MrLu
     * @param peoplecaseid 案件表id
     * @createTime  2020/10/4 16:31
     * @return   int  |
     */
    int getLastSFCSeq(int peoplecaseid);

    /**
     * 通过警情编号查询警情对应文书
     * @author MrLu
     * @param jqbh 警情编号
     * @createTime  2020/10/8 10:37
     * @return  List<FunArchiveRecordsDTO>  |
     */
    List<FunArchiveRecords>  selectRecordsByJqbh(String jqbh);



}
