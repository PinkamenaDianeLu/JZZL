package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;

public interface FunArchiveRecordsDTOMapper {


    int insertSelective(FunArchiveRecordsDTO record);

    FunArchiveRecordsDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordsDTO record);

}