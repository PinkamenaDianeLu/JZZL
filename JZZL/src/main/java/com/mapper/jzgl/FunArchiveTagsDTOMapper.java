package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveTagsDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveTagsDTOMapper {

    int insert(FunArchiveTagsDTO record);

    FunArchiveTagsDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveTagsDTO record);


     /**
     * 删除标签（更新scbj=1）
     * @author Mrlu
     * @param id 标签id
     * @createTime  2021/3/2 10:11
     * @return    |
      */
    void delTagById(Integer id);
     /**
     * 根据seqid和文件filecode查询文件对应的标签
     * @author MrLu
     * @param map Map{ARCHIVESEQID,FILECODE}
     * @createTime  2020/12/8 9:24
     * @return    |
      */
    List<FunArchiveTagsDTO> selectArchiveTagsById (Map<String,Object> map);


     /**
     * 根据文书id查询对应的标签
     * @author MrLu
     * @param recordid 文书id
     * @createTime  2021/4/8 14:22
     * @return    |
      */
    List<FunArchiveTagsDTO>  selectRecordByRecordId(Integer recordid);

}