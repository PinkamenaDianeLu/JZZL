package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunCaseInfoDTO;

public interface FunCaseInfoDTOMapper {


    int insertSelective(FunCaseInfoDTO record);

    FunCaseInfoDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunCaseInfoDTO record);

}