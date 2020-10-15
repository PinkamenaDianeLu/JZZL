package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveFilesDTO;

public interface FunArchiveFilesDTOMapper {

    int insert(FunArchiveFilesDTO record);

    int insertSelective(FunArchiveFilesDTO record);

    FunArchiveFilesDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveFilesDTO record);

}