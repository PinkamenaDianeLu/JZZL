package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;

import java.util.List;
import java.util.Map;

public interface FunCasePeoplecaseDTOMapper {


    int insertSelective(FunCasePeoplecaseDTO record);

    FunCasePeoplecaseDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunCasePeoplecaseDTO record);

     /**
     * 分页根据用户查询案件
     * @author MrLu
     * @param
     * @createTime  2020/10/24 10:20
     * @return    |
      */
     List<Map<String, Object>> selectPeopleCaseByUseridPage(Map<String,Object> map);

    int selectPeopleCaseByUseridCount(Map<String,Object> map);

}