package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;

public interface FunArchiveRecordsDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FunArchiveRecordsDTO record);

    int insertSelective(FunArchiveRecordsDTO record);

    FunArchiveRecordsDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordsDTO record);

    int updateByPrimaryKey(FunArchiveRecordsDTO record);
}