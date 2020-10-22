package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveCoverDTO;

public interface FunArchiveCoverDTOMapper {


    int insertSelective(FunArchiveCoverDTO record);

    FunArchiveCoverDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveCoverDTO record);

}