package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;

import java.util.List;
import java.util.Map;

public interface FunPeopelCaseDTOMapper {


    int insertSelective(FunPeopelCaseDTO record);

    FunPeopelCaseDTO selectByPrimaryKey(Integer id);

    Map<String,Object> selecTest(Integer id);
    List<FunPeopelCaseDTO> selectAll();

    int updateByPrimaryKeySelective(FunPeopelCaseDTO record);

     /**
     * 分页查询人员案件表
     * @author MrLu
     * @param
     * @createTime  2020/9/23 14:08
     * @return    |
      */
    List<FunPeopelCaseDTO> selectPeopleCasePage(Map<String,Object> map);
    int selectPeopleCasePageCount(Map<String,Object> map);

    List<Map<String, Object>> selectPeopleCasePageTest(Map<String, Object> map);
}