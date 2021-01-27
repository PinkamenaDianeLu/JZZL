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
    FunArchiveSFCDTO selectBaseSfcByCaseinfoid(Integer caseinfoid);

     /**
     * 查询新的原始卷
     * @author MrLu
     * @param id 大于某个id
     * @createTime  2021/1/6 14:12
     * @return    |
      */
    List<FunArchiveSFCDTO>  selectNewOriginArchive(Integer id);


     /**
     * 查询案卷整理数
     * @author MrLu
     * @param
     * @createTime  2021/1/27 15:03
     * @return    |
      */
    Integer selectArchivesRearranged(Map<String,Object> map);
}