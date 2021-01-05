package com.ZfbaETL.Case.Server;

import com.ZfbaETL.User.Server.UserServer;
import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.DTO.FunSuspectDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.zfba.*;
import com.mapper.jzgl.*;
import com.mapper.zfba.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/4 17:37
 * @describe
 */
@Service
public class CaseServer {
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    XtAjxxbMapper xtAjxxbMapper;
    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    XtWjflbMapper xtWjflbMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    XtXyrxxbMapper xtXyrxxbMapper;
    @Resource
    XtBarbMapper xtBarbMapper;
    @Resource
    SysUserMapper zfbaSysUserMapper;
    @Resource
    SysUserDTOMapper jzglSysUserMapper;
    @Resource
    SysBadwbMapper sysBadwbMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    /**
     * 查询新建的案件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    public List<XtAjxxb> selectNewCase(Integer id) {
        return xtAjxxbMapper.selectNewCase(id);
    }

     /**
     * 新建案件
     * @author MrLu
     * @param 
     * @createTime  2021/1/5 9:24
     * @return    |  
      */
    public void insertCaseinfo(FunCaseInfoDTO record) {
        funCaseInfoDTOMapper.insertSelective(record);
    }

    ;

    /**
     * 查询警情编号下的主办人
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 18:30
     */
    public XtBarb selectZbrByJqbh(String jqbh) {
        return xtBarbMapper.selectZbrByJqbh(jqbh);
    }


    /**
     * 查询警情编号下的辅办人
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 18:31
     */
    public List<XtBarb> selectFbrByJqbh(String jqbh) {
        return xtBarbMapper.selectFbrByJqbh(jqbh);
    }

    /**
     * 查询警情编号的主办单位
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 18:47
     */
    public SysBadwb selectZbdwByJqbn(String jqbh) {
        return sysBadwbMapper.selectZbdwByJqbn(jqbh);
    }

    /**
     * 查询这个单位的当权者
     * @author MrLu
     * @param
     * @createTime  2021/1/5 9:31
     * @return    |
     */
    public List<SysUser> selectLeaderByDwdm(String dwmcdm){
        return zfbaSysUserMapper.selectLeaderByDwdm(dwmcdm);
    };

     /**
     * 通过案宗id查找卷整理系统的id
     * @author MrLu
     * @param
     * @createTime  2021/1/5 9:44
     * @return    |
      */
    public SysUserDTO selectJzUserByAzId(Integer azid){
        return  jzglSysUserMapper.selectJzUserByAzId(azid);
    }

     /**
     * 通过身份证号查询用户
     * @author MrLu
     * @param
     * @createTime  2021/1/5 9:52
     * @return    |
      */
    public SysUserDTO selectJzUserByIdCard(String sfzh){
        return  jzglSysUserMapper.selectJzUserByIdCard(sfzh);
    }

     /**
     * 新建人案关系表
     * @author MrLu
     * @param
     * @createTime  2021/1/5 10:09
     * @return    |
      */
    public  void insertCasePeopleCase(FunCasePeoplecaseDTO record){
        funCasePeoplecaseDTOMapper.insertSelective(record);
    }

    /**
     * 查找这个案件的嫌疑人
     * @author MrLu
     * @param jjbh 警情编号
     * @createTime  2021/1/5 14:31
     * @return    |
     */
   public List<XtXyrxxb> selectXyrByJqbh(String jjbh){
        return  xtXyrxxbMapper.selectXyrByJqbh(jjbh);
    };


    /**
    * 新建嫌疑人信息
    * @author MrLu
    * @param
    * @createTime  2021/1/5 16:19
    * @return    |
     */
   public void insertNewSuspect(FunSuspectDTO record){
     funSuspectDTOMapper.insertSelective(record);
   }

}
