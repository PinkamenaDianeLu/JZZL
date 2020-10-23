package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveCoverDTO;

public interface FunArchiveCoverDTOMapper {


    int insertSelective(FunArchiveCoverDTO record);

    FunArchiveCoverDTO selectByPrimaryKey(Integer id);
    /**
     * 通过fileid查询文书封皮
     * @author MrLu
     * @param fileid
     * @createTime  2020/10/23 10:17
     * @return  FunArchiveCoverDTO  |
     */
    FunArchiveCoverDTO selectFunArchiveCoverDTOByFileId(Integer fileid);

    int updateByPrimaryKeySelective(FunArchiveCoverDTO record);

}