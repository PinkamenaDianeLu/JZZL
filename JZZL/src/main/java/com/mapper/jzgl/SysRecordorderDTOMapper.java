package com.mapper.jzgl;


import com.bean.jzgl.DTO.SysRecordorderDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRecordorderDTOMapper {

    int insert(SysRecordorderDTO record);

    int insertSelective(SysRecordorderDTO record);

    SysRecordorderDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRecordorderDTO record);

    int updateByPrimaryKey(SysRecordorderDTO record);

    /**
     * 按照送检卷类型和卷类型查询默认顺序
     *
     * @param map {recordcode,archivetype,recordtype} 文书代码、送检卷卷类型、文书卷类型
     * @return List<SysRecordorderDTO>    |
     * @author MrLu
     * @createTime 2020/11/26 15:23
     */
    SysRecordorderDTO selectRecordOrderByTypes(Map<String,Object> map);
}