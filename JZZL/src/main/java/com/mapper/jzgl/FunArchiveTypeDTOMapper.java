package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveTypeDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveTypeDTOMapper {


    int insertSelective(FunArchiveTypeDTO funArchiveTypeDTO);

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


    /**
    * 查询seq下某个type
    * @author MrLu
    * @param archiveseqid
     * @param recordtype
    * @createTime  2021/3/25 14:55
    * @return    |
     */
    FunArchiveTypeDTO  selectTypeBySeqType(Integer archiveseqid,Integer recordtype);


     /**
     * 查询不同seq下的相同type
     * @author MrLu
     * @param archiveseqid seqid
     * @param archiveseqid id
     * @createTime  2021/3/31 14:06
     * @return    |
      */
    FunArchiveTypeDTO  selectSameTypeWithSeq(Integer archiveseqid,Integer id);

}