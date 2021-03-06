package com.module.CaseManager.Services;

import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:02
 * @describe
 */
public interface CaseSearchService {

    /**
     * 分页查询人员案件表
     * @author MrLu
     * @param
     * @createTime  2020/9/23 14:08
     * @return    |
     */
    List<Object>  selectPeopleCasePage(Map<String,Object> map) throws Exception;
    int selectPeopleCasePageCount(Map<String,Object> map)throws  Exception;

}
