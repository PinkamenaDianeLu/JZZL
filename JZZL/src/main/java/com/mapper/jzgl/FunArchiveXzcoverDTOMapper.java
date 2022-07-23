package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveXzcoverDTO;

public interface FunArchiveXzcoverDTOMapper {



    int insertSelective(FunArchiveXzcoverDTO record);

    FunArchiveXzcoverDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveXzcoverDTO record);



    /**
     * 通过fileid查询文书封皮
     * @author MrLu
     * @param fileid
     * @createTime  2020/10/23 10:17
     * @return  FunArchiveXzcoverDTO  |
     */
    FunArchiveXzcoverDTO selectFunArchiveXzCoverDTOByFileId(Integer fileid);
}