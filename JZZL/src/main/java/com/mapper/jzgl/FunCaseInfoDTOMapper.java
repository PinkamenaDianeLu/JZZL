package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunCaseInfoDTO;

import java.util.Map;

public interface FunCaseInfoDTOMapper {


    int insertSelective(FunCaseInfoDTO record);

    FunCaseInfoDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunCaseInfoDTO record);

    FunCaseInfoDTO selectCaseInfoByCaseInfoId(int caseinfoid);
    
     /**
     * 查询案件数
     * @author MrLu
     * @param 
     * @createTime  2021/1/26 16:52
     * @return    |  
      */
    Integer selectCaseCount(Map<String,Object> map);

}