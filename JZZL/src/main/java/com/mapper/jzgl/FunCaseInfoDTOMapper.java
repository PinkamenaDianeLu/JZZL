package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunCaseInfoDTO;

import java.util.Map;

public interface FunCaseInfoDTOMapper {


    int insertSelective(FunCaseInfoDTO record);

    FunCaseInfoDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunCaseInfoDTO record);

    FunCaseInfoDTO selectCaseInfoByCaseInfoId(int caseinfoid);

     /**
     * 根据警情编号查询案件信息
     * @author MrLu
     * @param jqbh 警情编号
     * @createTime  2021/8/17 11:33
     * @return    |
      */
    FunCaseInfoDTO selectCaseInfoByJqbh(String jqbh);

    
     /**
     * 查询案件数
     * @author MrLu
     * @param 
     * @createTime  2021/1/26 16:52
     * @return    |  
      */
    Integer selectCaseCount(Map<String,Object> map);

     /**
     * 通过警情编号查询是否有案件
     * @author MrLu
     * @param jqbh String
     * @createTime  2021/6/18 17:25
     * @return    |
      */
    Integer selectCaseCountByJqbh(String jqbh);

     /**
     * 标记为已整理
     * @author MrLu
     * @param
     * @createTime  2021/7/2 11:22
     * @return    |
      */
    void updateCaseIsSorted(Integer id);

}