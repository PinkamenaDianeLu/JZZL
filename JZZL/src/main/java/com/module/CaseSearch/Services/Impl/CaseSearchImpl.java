package com.module.CaseSearch.Services.Impl;

import com.bean.jzgl.Converter.FunPeopelCaseMapper;
import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.mapper.jzgl.FunPeopelCaseDTOMapper;
import com.module.CaseSearch.Services.CaseSearchService;
import com.util.EnumsUtil;
import com.util.MapFactory;
import net.sf.cglib.beans.BeanMap;
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
    FunPeopelCaseDTOMapper funPeopelCaseTDOMapper;

    @Override
    public void testInsert() {
        FunPeopelCase FunPeopelCase = new FunPeopelCase();

        FunPeopelCase.setIdcard("230105199810211311");
        FunPeopelCase.setName("lu");
        FunPeopelCase.setJqbh("asd");
        FunPeopelCase.setAjbh("123415");
        FunPeopelCase.setPersontype(Enums.PersonType.SECONDARY);
        FunPeopelCaseDTO FunPeopelCaseTDO = FunPeopelCaseMapper.INSTANCE.pcToPcDTO(FunPeopelCase);
        funPeopelCaseTDOMapper.insertSelective(FunPeopelCaseTDO);
    }

    @Override
    public List<FunPeopelCase> testSearchList() {
        return FunPeopelCaseMapper.INSTANCE.pcDTOToPcs(funPeopelCaseTDOMapper.selectAll());

    }

    @Override
    public FunPeopelCase testSearch() throws Exception {
        Class<?> beanClass = FunPeopelCase.class;
        System.out.println(beanClass.getName());
        System.out.println(beanClass.getClassLoader());
        Object object = beanClass.newInstance();
        BeanMap beanMap = BeanMap.create(object);
        Map<String, Object> map = funPeopelCaseTDOMapper.selecTest(21);
        map.put("persontype", EnumsUtil.getEnumByValue(Enums.PersonType.class,map.get("persontype")));
        beanMap.putAll(map);
        return (FunPeopelCase) object;
    }

    @Override
    public List<Object>selectPeopleCasePage(Map<String, Object> map) throws Exception {
        return MapFactory.mapToListBean(funPeopelCaseTDOMapper.selectPeopleCasePage(map));
    }
    @Override
    public int selectPeopleCasePageCount(Map<String, Object> map) throws Exception {
        return funPeopelCaseTDOMapper.selectPeopleCasePageCount(map);
    }
}
