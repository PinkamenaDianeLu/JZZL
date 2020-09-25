package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;

public interface FunArchiveSeqDTOMapper {

    int insert(FunArchiveSeqDTO record);

    int insertSelective(FunArchiveSeqDTO record);

    FunArchiveSeqDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveSeqDTO record);

}