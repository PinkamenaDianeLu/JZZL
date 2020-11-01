package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveTagsDTO;

public interface FunArchiveTagsDTOMapper {

    int insert(FunArchiveTagsDTO record);

    int insertSelective(FunArchiveTagsDTO record);

    FunArchiveTagsDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveTagsDTO record);

}