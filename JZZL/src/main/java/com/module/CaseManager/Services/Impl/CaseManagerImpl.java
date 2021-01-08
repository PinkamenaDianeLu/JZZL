package com.module.CaseManager.Services.Impl;

import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.factory.BaseFactory;
import com.mapper.jzgl.FunCaseInfoDTOMapper;
import com.mapper.jzgl.FunCasePeoplecaseDTOMapper;
import com.mapper.jzgl.SysUserDTOMapper;
import com.module.CaseManager.Services.CaseManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/8 15:18
 * @describe
 */
@Service
public class CaseManagerImpl extends BaseFactory implements CaseManagerService {

    @Resource
    SysUserDTOMapper sysUserDTOMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;

    @Override
    public SysUserDTO selectSysUserDtoById(Integer id) {
        return sysUserDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public FunCaseInfoDTO selectCaseInfoById(Integer id){
        return  funCaseInfoDTOMapper.selectByPrimaryKey(id);
    }



    @Override
    public void insertCaseinfo(FunCaseInfoDTO record) {
        funCaseInfoDTOMapper.insertSelective(record);
    }
    @Override
    public List<FunCasePeoplecaseDTO> selectRelationByCaseid (Integer caseinfoid){
        return funCasePeoplecaseDTOMapper.selectRelationByCaseid(caseinfoid);
    }

    /**
     * 新建人案关系表
     * @author MrLu
     * @param
     * @createTime  2021/1/5 10:09
     * @return    |
     */
    @Override
    public  void insertCasePeopleCase(FunCasePeoplecaseDTO record){
        funCasePeoplecaseDTOMapper.insertSelective(record);
    }
}
