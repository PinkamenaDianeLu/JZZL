package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveSFCDTOMapper {

    int insert(FunArchiveSFCDTO record);

    int insertSelective(FunArchiveSFCDTO record);

    FunArchiveSFCDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveSFCDTO record);


    /**
     * 分页查询送检记录表(基础卷总是在第一个)
     * @author MrLu
     * @param map 分页参数 peopelcaseid为必传项
     * @createTime  2020/9/27 15:48
     * @return    |
     */
    List<FunArchiveSFCDTO> selectArchiveSFCPage(Map<String,Object> map);
    int selectArchiveSFCPageCount(Map<String,Object> map);
}