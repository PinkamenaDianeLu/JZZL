package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordindexDTO;
import org.apache.ibatis.annotations.Param;

public interface FunArchiveRecordindexDTOMapper {

    int insert(FunArchiveRecordindexDTO record);

    int insertSelective(FunArchiveRecordindexDTO record);

    FunArchiveRecordindexDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordindexDTO record);

     /**
     * 根据送检次序id和文书类型id查询这个文书类型的文书目录信息
     * @author MrLu
     * @param archiveseqid
      * @param archivetypeid
     * @createTime  2020/10/29 15:18
     * @return    |
      */
    FunArchiveRecordindexDTO selectRecordIndexByTypeId(@Param("archiveseqid") Integer archiveseqid, @Param("archivetypeid")Integer archivetypeid);


}