package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveFilesDTO;

import java.util.List;

public interface FunArchiveFilesDTOMapper {

    int insert(FunArchiveFilesDTO record);

    int insertSelective(FunArchiveFilesDTO record);

    FunArchiveFilesDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveFilesDTO record);

     /**
     * 通过文书id查找其文书图片（最新版本的）
     * @author MrLu
     * @param archiverecordid 文书id
     * @createTime  2020/10/15 17:58
     * @return  FunArchiveFilesDTO  |
      */
     List<FunArchiveFilesDTO>  selectRecordFilesByRecordId(int archiverecordid);


    List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String []filesCode);
     /**
     * 根据文件代码查询该文件的历史版本
     * @author MrLu
     * @param
     * @createTime  2020/10/15 18:13
     * @return    |
      */
    List<FunArchiveFilesDTO> selectFilesHistory(String filecode);

}