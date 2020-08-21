package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveTypeDTO;

public interface FunArchiveTypeDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FunArchiveTypeDTO record);

    int insertSelective(FunArchiveTypeDTO record);

    FunArchiveTypeDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveTypeDTO record);

    int updateByPrimaryKey(FunArchiveTypeDTO record);
}