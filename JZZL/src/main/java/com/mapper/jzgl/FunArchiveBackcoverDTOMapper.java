package com.mapper.jzgl;


import com.bean.jzgl.DTO.FunArchiveBackcoverDTO;

public interface FunArchiveBackcoverDTOMapper {


    int insertSelective(FunArchiveBackcoverDTO record);

    FunArchiveBackcoverDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveBackcoverDTO record);

    int updateByPrimaryKey(FunArchiveBackcoverDTO record);

    FunArchiveBackcoverDTO selectFunArchiveBackCoverDTOByFileId(Integer archivefileid);
}