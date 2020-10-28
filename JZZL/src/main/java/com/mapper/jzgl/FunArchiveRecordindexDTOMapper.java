package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordindexDTO;

public interface FunArchiveRecordindexDTOMapper {

    int insert(FunArchiveRecordindexDTO record);

    int insertSelective(FunArchiveRecordindexDTO record);

    FunArchiveRecordindexDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordindexDTO record);


}