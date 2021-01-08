package com.module.CaseManager.Services;

import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.DTO.SysUserDTO;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/8 15:18
 * @describe
 */
public interface CaseManagerService {
     /**
     * 根据id 查询用户
     * @author MrLu
     * @param id)
     * @createTime  2021/1/8 15:49
     * @return    |
      */
    SysUserDTO selectSysUserDtoById(Integer id);


     /**
     * 根据id查询案件信息
     * @author MrLu
     * @param 
     * @createTime  2021/1/8 15:55
     * @return    |  
      */
    FunCaseInfoDTO selectCaseInfoById(Integer id);

    /**
     * 新建案件
     * @author MrLu
     * @param
     * @createTime  2021/1/5 9:24
     * @return    |
     */
     void insertCaseinfo(FunCaseInfoDTO record);


      /**
      * 根据案件表id查询所有的关系
      * @author MrLu
      * @param caseinfoid 案件id
      * @createTime  2021/1/8 16:22
      * @return    |
       */
    List<FunCasePeoplecaseDTO> selectRelationByCaseid (Integer caseinfoid);

    /**
     * 新建人案关系表
     * @author MrLu
     * @param
     * @createTime  2021/1/5 10:09
     * @return    |
     */
      void insertCasePeopleCase(FunCasePeoplecaseDTO record);
}
