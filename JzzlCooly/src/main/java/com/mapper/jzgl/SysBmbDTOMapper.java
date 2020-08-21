package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysBmbDTO;

public interface SysBmbDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysBmbDTO record);

    int insertSelective(SysBmbDTO record);

    SysBmbDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysBmbDTO record);

    int updateByPrimaryKey(SysBmbDTO record);
}