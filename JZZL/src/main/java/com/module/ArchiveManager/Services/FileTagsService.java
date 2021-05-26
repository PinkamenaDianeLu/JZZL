package com.module.ArchiveManager.Services;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveTagsDTO;

import java.util.List;

public interface FileTagsService {

     /**
     * 创建新的标签
     * @author MrLu
     * @param funArchiveTagsDTO
     * @createTime  2020/12/7 19:25
     * @return    |
      */
    void createNewTags(FunArchiveTagsDTO funArchiveTagsDTO);


    /**
     * 根据seqid和文件filecode查询文件对应的标签
     * @author MrLu
     * @param archiveseqid
     * @param filecode
     * @createTime  2020/12/8 9:24
     * @return    |
     */
    List<FunArchiveTagsDTO> selectArchiveTagsById (int archiveseqid,String filecode);


     /**
     * 按照id查询FunArchiveRecords
     * @author MrLu
     * @param recordId 文书id
     * @createTime  2020/12/8 10:04
     * @return    |
      */
    FunArchiveRecordsDTO selectFunArchiveRecordsDTOById (Integer recordId);
    
     /**
     * 删除标签
     * @author Mrlu
     * @param 
     * @createTime  2021/3/2 16:45
     * @return    |  
      */
    void delTagById(Integer id);


     /**
     * 更改标签的颜色
     * @author MrLu
     * @param id id
     * @param colour 颜色
     * @createTime  2021/3/15 14:38
     * @return    |
      */
    void changeTagColor(Integer id,String colour);
}
