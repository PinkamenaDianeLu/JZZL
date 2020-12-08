package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveTagsDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveTagsDTOMapper {

    int insert(FunArchiveTagsDTO record);

    FunArchiveTagsDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveTagsDTO record);


     /**
     * 根据seqid和文件filecode查询文件对应的标签
     * @author MrLu
     * @param map Map{ARCHIVESEQID,FILECODE}
     * @createTime  2020/12/8 9:24
     * @return    |
      */
    List<FunArchiveTagsDTO> selectArchiveTagsById (Map<String,Object> map);

}