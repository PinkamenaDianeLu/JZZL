package com.module.CaseSearch.Services.Impl;

import com.factory.BaseFactory;
import com.mapper.jzgl.FunCasePeoplecaseDTOMapper;
import com.module.CaseSearch.Services.CaseSearchService;
import com.util.MapFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:03
 * @describe
 */
@Service
public class CaseSearchImpl extends BaseFactory implements CaseSearchService {


    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;



    @Override
    public List<Object>selectPeopleCasePage(Map<String, Object> map) throws Exception {
        return MapFactory.mapToListBean(funCasePeoplecaseDTOMapper.selectPeopleCaseByUseridPage(map));
    }
    @Override
    public int selectPeopleCasePageCount(Map<String, Object> map) throws Exception {
        return funCasePeoplecaseDTOMapper.selectPeopleCaseByUseridCount(map);
    }
}
