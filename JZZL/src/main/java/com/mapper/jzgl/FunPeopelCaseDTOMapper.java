package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;

import java.util.List;

public interface FunPeopelCaseDTOMapper {


    int insertSelective(FunPeopelCaseDTO record);

    FunPeopelCaseDTO selectByPrimaryKey(Integer id);
    List<FunPeopelCaseDTO> selectAll();

    int updateByPrimaryKeySelective(FunPeopelCaseDTO record);

}