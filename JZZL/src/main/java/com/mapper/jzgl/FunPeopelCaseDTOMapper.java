package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;

import java.util.List;
import java.util.Map;

/**
* @description
* @log  2020/10/24 10:46  MrLu  已拆分为fun_case_info和fun_case_peoplecase两张表
**/
public interface FunPeopelCaseDTOMapper {


    int insertSelective(FunPeopelCaseDTO record);

    FunPeopelCaseDTO selectByPrimaryKey(Integer id);

    Map<String,Object> selecTest(Integer id);
    List<FunPeopelCaseDTO> selectAll();

    int updateByPrimaryKeySelective(FunPeopelCaseDTO record);

     /**
     * 分页查询人员案件表
     * @author MrLu
     * @param map
     * @createTime  2020/9/23 14:08
     * @return    |
      */
     List<Map<String, Object>>  selectPeopleCasePage(Map<String,Object> map);
    int selectPeopleCasePageCount(Map<String,Object> map);

}