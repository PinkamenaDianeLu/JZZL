package com.module.CaseSearch.Services;

import com.bean.jzgl.Source.FunPeopelCase;

import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:02
 * @describe
 */
public interface CaseSearchService {

    void testInsert();

    List<FunPeopelCase> testSearchList();

    FunPeopelCase testSearch() throws Exception;
    /**
     * 分页查询人员案件表
     * @author MrLu
     * @param
     * @createTime  2020/9/23 14:08
     * @return    |
     */
    List<FunPeopelCase>  selectPeopleCasePage(Map<String,Object> map) throws Exception;
    int selectPeopleCasePageCount(Map<String,Object> map)throws  Exception;

    List<Object> selectPeopleCasePageTest(Map<String, Object> map) throws Exception ;
}
