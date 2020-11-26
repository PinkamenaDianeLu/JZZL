package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunSuspectRecordDTO;

import java.util.List;

public interface FunSuspectRecordDTOMapper {

    int insert(FunSuspectRecordDTO record);

    int insertSelective(FunSuspectRecordDTO record);

    FunSuspectRecordDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunSuspectRecordDTO record);

     /**
     * 查询一个人的对应文书
     * @author MrLu
     * @param suspectid 嫌疑人id
      *    @param recordtype 文书类型
     * @createTime  2020/11/26 15:27
     * @return  List<FunSuspectRecordDTO>  |
      */
    List<FunSuspectRecordDTO> selectRecordBySuspectid(int suspectid,int recordtype);

}