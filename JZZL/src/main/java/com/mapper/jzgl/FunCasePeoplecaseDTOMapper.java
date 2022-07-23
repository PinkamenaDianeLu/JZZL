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
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/24 10:20
     */
    List<Map<String, Object>> selectPeopleCaseByUseridPage(Map<String, Object> map);

    int selectPeopleCaseByUseridCount(Map<String, Object> map);


    List<FunCasePeoplecaseDTO> selectPeopleCaseForCombinationPage(Map<String, Object> map);

    int selectPeopleCaseForCombinationCount(Map<String, Object> map);

    /**
     * 根据案件表id查询所有的关系
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 16:21
     */
    List<FunCasePeoplecaseDTO> selectRelationByCaseid(Integer caseinfoid);


    /**
     * 通过警情编号和身份证号查询案件
     *
     * @param jqbh   警情编号
     * @param idcard 身份证好
     * @return |
     * @author MrLu
     * @createTime 2021/4/8 16:03
     */
    List<FunCasePeoplecaseDTO> selectCaseByJqIDCard(String jqbh);

}