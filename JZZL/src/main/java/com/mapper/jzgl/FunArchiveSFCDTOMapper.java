package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveSFCDTOMapper {

    int insert(FunArchiveSFCDTO record);

    int insertSelective(FunArchiveSFCDTO record);

    FunArchiveSFCDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveSFCDTO record);


    /**
     * 分页查询送检记录表(基础卷总是在第一个)
     * @author MrLu
     * @param map 分页参数 peopelcaseid为必传项
     * @createTime  2020/9/27 15:48
     * @return    |
     */
    List<FunArchiveSFCDTO> selectArchiveSFCPage(Map<String,Object> map);
    int selectArchiveSFCPageCount(Map<String,Object> map);
    
     /**
     * 将送检卷标记为已为嫌疑人排序
     * @author MrLu
     * @param 
     * @createTime  2020/12/17 17:54
     * @return    |  
      */
    void updateIssuspectorderByCaseinfoid(Integer issuspectorder,Integer sfcid);

     /**
     *  查询案件的基础卷
     * @author MrLu
     * @param
     * @createTime  2020/12/17 18:00
     * @return    |
      */
    FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid,Integer archivetype);


     /**
     * 通过案件编号查询案件的基础卷
     * @author MrLu
     * @param ajbh 案件编号
     * @createTime  2021/6/17 16:40
     * @return    |
      */
    FunArchiveSFCDTO selectBaseSfcByAjbh(String ajbh);
     /**
     * 查询新的原始卷
      * --AND ID &gt; #{id,jdbcType=DECIMAL}
     * @author MrLu
     * @param id 大于某个id
     * @createTime  2021/1/6 14:12
     * @return    |
      */
    List<FunArchiveSFCDTO>  selectNewOriginArchive(Integer id);

     /**
     * 查询需要临时抽取整理的案件
     * @author MrLu
     * @param
     * @createTime  2021/8/24 14:55
     * @return    |
      */
    List<FunArchiveSFCDTO>  selectTempArchive( );


     /**
     * 查询案卷整理数
     * @author MrLu
     * @param
     * @createTime  2021/1/27 15:03
     * @return    |
      */
    Integer selectArchivesRearranged(Map<String,Object> map);


     /**
     * 通过seqid查询对应的SFC
     * @author MrLu
     * @param seqid
     * @createTime  2021/5/27 10:49
     * @return    |
      */
    FunArchiveSFCDTO selectArchiveSfcBySeqid(Integer seqid);

     /**
     * 审批卷宗
     * @author MrLu
     * @param
     * @createTime  2021/6/16 15:23
     * @return    |
      */
    void  approvalArchive(Integer approval,Integer id);


     /**
     * 查询一个案件除基础卷外最新操作的卷的状态
     * @author MrLu
     * @param  caseinfoid  案件id
     * @createTime  2021/7/2 10:06
     * @return    |
      */
    FunArchiveSFCDTO selectLastSfcByCaseId(Integer caseinfoid);


    /**
     * 查询一个案件的原始卷
     * @author MrLu
     * @param  caseinfoid  案件id
     * @createTime  2021/7/2 10:06
     * @return    |
     */
    FunArchiveSFCDTO selectOriArchiveByCaseId(Integer caseinfoid);


     /**
     * 更改一个sfc的发送状态
     * @author MrLu
     * @param
     * @createTime  2021/12/18 12:27
     * @return    |
      */
    void updateSendTypeById(Integer issend,Integer sfcid);
}