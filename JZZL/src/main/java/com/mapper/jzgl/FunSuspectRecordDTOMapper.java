package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunSuspectRecordDTO;

public interface FunSuspectRecordDTOMapper {

    int insert(FunSuspectRecordDTO record);

    int insertSelective(FunSuspectRecordDTO record);

    FunSuspectRecordDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunSuspectRecordDTO record);

}