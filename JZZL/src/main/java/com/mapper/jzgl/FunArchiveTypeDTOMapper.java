package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveTypeDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveTypeDTOMapper {


    int insertSelective(FunArchiveTypeDTO record);

    FunArchiveTypeDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveTypeDTO record);
     /**
     * 查询卷类型表
     * @author MrLu
     * @param map（ARCHIVESEQID，jqbh）
     * @createTime  2020/10/8 11:05
     * @return  List<FunArchiveTypeDTO>  |
      */
   List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(Map<String,Object> map);

}