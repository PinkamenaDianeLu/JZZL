package com.mapper.jzgl;


import com.bean.jzgl.DTO.EtlLogsDTO;

public interface EtlLogsDTOMapper {

    int insert(EtlLogsDTO record);

    int insertSelective(EtlLogsDTO record);

    EtlLogsDTO selectByPrimaryKey(Integer id);


}