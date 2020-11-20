package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveSeqDTOMapper {



     /**
     *  插入
     * @author MrLu
     * @param record (  JQBH,AJBH,RECORDSNUMBER,PEOPELCASEID,AUTHOR,AUTHORIDCARD,SFCNUMBER,ARCHIVETYPE,ARCHIVENAME,)
     * @createTime  2020/10/4 14:57
     * @return    |
      */
    void insertSelective(FunArchiveSeqDTO record);

    FunArchiveSeqDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveSeqDTO record);

     /**
     * 分页查询送检记录表
     * @author MrLu
     * @param map 分页参数 peopelcaseid为必传项
     * @createTime  2020/9/27 15:48
     * @return    |
      */
    List<FunArchiveSeqDTO> selectArchiveSeqPage(Map<String,Object> map);
    int selectArchiveSeqPageCount(Map<String,Object> map);

    /**
     * 查询送检记录下最后一次整理
     * @author MrLu
     * @param archivesfcid 送检记录id
     * @createTime  2020/10/11 16:13
     * @return  FunArchiveSeq  |
     */
    FunArchiveSeqDTO selectLastSeqBySfc(int archivesfcid);
     /**
     * 得到某个案件最后的整理次序
     * @author MrLu
     * @param peoplecaseid 案件表id
     * @createTime  2020/10/4 16:31
     * @return   int  |
      */
    int getLastSFCSeq(int peoplecaseid);

     /**
     * 查询一个案件最原始的基础卷
     * @author MrLu
     * @param peoplecaseid 人员案件表id
     * @createTime  2020/11/20 10:51
     * @return  FunArchiveSeqDTO  |
      */
    FunArchiveSeqDTO selectBaseArchive(int peoplecaseid);
}