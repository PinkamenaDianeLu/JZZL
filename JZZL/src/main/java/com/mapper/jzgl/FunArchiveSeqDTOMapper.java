package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveSeqDTOMapper {


    /**
     * 插入
     *
     * @param record (  JQBH,AJBH,RECORDSNUMBER,PEOPELCASEID,AUTHOR,AUTHORIDCARD,SFCNUMBER,ARCHIVETYPE,ARCHIVENAME,)
     * @return |
     * @author MrLu
     * @createTime 2020/10/4 14:57
     */
    void insertSelective(FunArchiveSeqDTO record);

    FunArchiveSeqDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveSeqDTO record);

    /**
     * 分页查询送检记录表
     *
     * @param map 分页参数 caseinfoid为必传项
     * @return |
     * @author MrLu
     * @createTime 2020/9/27 15:48
     */
    List<FunArchiveSeqDTO> selectArchiveSeqPage(Map<String, Object> map);

    int selectArchiveSeqPageCount(Map<String, Object> map);

    /**
     * 查询送检记录下最后一次整理
     *
     * @param archivesfcid 送检记录id
     * @return FunArchiveSeq  |
     * @author MrLu
     * @createTime 2020/10/11 16:13
     */
    FunArchiveSeqDTO selectLastSeqBySfc(int archivesfcid);

    /**
     * 得到某个案件最后的整理次序
     *
     * @param caseinfoid 案件表id
     * @return int  |
     * @author MrLu
     * @createTime 2020/10/4 16:31
     */
    int getLastSFCSeq(int caseinfoid);

    /**
     * 查询一个案件最原始的基础卷
     *
     * @param caseInfoId 人员案件表id
     * @return FunArchiveSeqDTO  |
     * @author MrLu
     * @createTime 2020/11/20 10:51
     */
    FunArchiveSeqDTO selectBaseArchive(int caseInfoId);

    /**
     * 通过seqid查询这个案件正在活跃的基础卷
     *
     * @param seqid
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 20:14
     */
    FunArchiveSeqDTO selectBaseArchiveBySeqId(int seqid);

    /**
     * 查询某个案件正在活跃的基础卷
     *
     * @param caseinfoid 案件表id
     * @return FunArchiveSeqDTO  |
     * @author MrLu
     * @createTime 2020/11/27 9:22
     */
    FunArchiveSeqDTO selectActiveSeqByCaseId(int caseinfoid);


    /**
     * 更新一个送检卷下的整理次序都是不活跃状态
     *
     * @param archivesfcid 送检卷id
     * @return |
     * @author MrLu
     * @createTime 2020/12/10 9:27
     */
    void updateBaseSeqIsNotActive(int archivesfcid);

    /**
     * 查询一个案件下所有活跃的且未被送检的文书整理次序
     *
     * @param caseinfoid 案件id
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 15:09
     */
    List<FunArchiveSeqDTO> selectActiveSeqByCaseInfoId(int caseinfoid);
}