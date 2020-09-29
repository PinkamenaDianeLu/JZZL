package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunSuspectDTO;

public interface FunSuspectDTOMapper {


    int insertSelective(FunSuspectDTO record);

    FunSuspectDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunSuspectDTO record);

}