package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunSuspectRecordDTO;

import java.util.List;
import java.util.Map;

public interface FunSuspectRecordDTOMapper {

    int insert(FunSuspectRecordDTO record);

    int insertSelective(FunSuspectRecordDTO record);

    FunSuspectRecordDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunSuspectRecordDTO record);

    /**
     * 查询一个人的对应文书
     *
     * @param suspectid 嫌疑人id
     * @return List<FunSuspectRecordDTO>  |
     * @author MrLu
     * @createTime 2020/11/26 15:27
     */
    List<FunSuspectRecordDTO> selectRecordBySuspectid(int suspectid, int archiveseqid);


    /**
     * 查询文书对应的嫌疑人信息
     *
     * @param recordid 文书id
     * @return |
     * @author Mrlu
     * @createTime 2021/2/19 14:43
     */
    List<FunSuspectRecordDTO> selectSuspectsByRid(int recordid);

    /**
     * 查询该文书对嫌疑人的关联信息
     * 这里限制了RECORDTYPE = 1 其实没必要 因为调用此方法时的文书id一定时对单个人的
     *
     * @param recordid
     * @return |
     * @author MrLu
     * @createTime 2020/12/24 23:03
     */
    List<FunSuspectRecordDTO> selectSuspectRecordByRid(int recordid);

     /**
     * 判断这个文书是否对应某些的嫌疑人
     * @author Mrlu
     * @param map {recordid int,array in[]}
     * @createTime  2021/3/10 18:11
     * @return    |
      */
     FunSuspectRecordDTO selectRecordBoolSuspect(Map<String, Object> map);


}