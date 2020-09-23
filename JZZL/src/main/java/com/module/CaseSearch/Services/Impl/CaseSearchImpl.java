package com.module.CaseSearch.Services.Impl;

import com.bean.jzgl.Converter.FunPeopelCaseMapper;
import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import com.enums.Enums;
import com.mapper.jzgl.FunPeopelCaseDTOMapper;
import com.module.CaseSearch.Services.CaseSearchService;
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
public class CaseSearchImpl implements CaseSearchService {

    @Resource
    FunPeopelCaseDTOMapper funPeopelCaseTDOMapper;
    @Override
    public void testInsert() {
        FunPeopelCase FunPeopelCase=new FunPeopelCase();

        FunPeopelCase.setIdcard("230105199810211311");
        FunPeopelCase.setName("lu");
        FunPeopelCase.setJqbh("asd");
        FunPeopelCase.setAjbh("123415");
        FunPeopelCase.setPersontype(Enums.PersonType.SECONDARY);
        FunPeopelCaseDTO FunPeopelCaseTDO= FunPeopelCaseMapper.INSTANCE.pcToPcDTO(FunPeopelCase);
        funPeopelCaseTDOMapper.insertSelective(FunPeopelCaseTDO);
    }

    @Override
    public List<FunPeopelCase> testSearch() {
        return   FunPeopelCaseMapper.INSTANCE.pcDTOToPcs(funPeopelCaseTDOMapper.selectAll());

    }

    @Override
    public List<FunPeopelCase>  selectPeopleCasePage(Map<String,Object> map) throws Exception{
        return   FunPeopelCaseMapper.INSTANCE.pcDTOToPcs(funPeopelCaseTDOMapper.selectPeopleCasePage(map));
    }
    @Override
    public int selectPeopleCasePageCount(Map<String,Object> map)throws  Exception  {
        return funPeopelCaseTDOMapper.selectPeopleCasePageCount(map);
    }
}
