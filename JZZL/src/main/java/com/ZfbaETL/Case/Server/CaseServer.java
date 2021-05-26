package com.ZfbaETL.Case.Server;

import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.DTO.FunSuspectDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.zfba.*;
import com.mapper.jzgl.*;
import com.mapper.zfba.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    SysGroupMapper sysGroupMapper;

    /**
     * 查询新建的案件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    public List<XtAjxxb> selectNewCase(Integer id,String groupcode) {
        return xtAjxxbMapper.selectNewCase(id,groupcode);
    }

    /**
     * 新建案件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 9:24
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
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 9:31
     */
    public List<SysUser> selectLeaderByDwdm(String dwmcdm) {
        return zfbaSysUserMapper.selectLeaderByDwdm(dwmcdm);
    }

    ;

    /**
     * 通过案宗id查找卷整理系统的id
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 9:44
     */
    public SysUserDTO selectJzUserByAzId(Integer azid) {
        return jzglSysUserMapper.selectJzUserByAzId(azid);
    }

    /**
     * 通过身份证号查询用户
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 9:52
     */
    public SysUserDTO selectJzUserByIdCard(String sfzh) {
        return jzglSysUserMapper.selectJzUserByIdCard(sfzh);
    }

    /**
     * 新建人案关系表
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 10:09
     */
    public void insertCasePeopleCase(FunCasePeoplecaseDTO record) {
        funCasePeoplecaseDTOMapper.insertSelective(record);
    }

    /**
     * 查找这个案件的嫌疑人
     *
     * @param jjbh 警情编号
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 14:31
     */
    public List<XtXyrxxb> selectXyrByJqbh(String jjbh) {
        return xtXyrxxbMapper.selectXyrByJqbh(jjbh);
    }

    ;


    /**
     * 新建嫌疑人信息
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 16:19
     */
    public void insertNewSuspect(FunSuspectDTO record) {
        funSuspectDTOMapper.insertSelective(record);
    }

     /**
     * 判断这个文书是否是受案登记表
     * @author Mrlu
     * @param 
     * @createTime  2021/3/3 9:35
     * @return    |  
      */
    public boolean selectSahzBool(String jqbh, int id) {
        Map<String, Object> map = new HashMap<>();
        map.put("jqbh", jqbh);
        map.put("id", id);
        return xtWjflbMapper.selectSahzBool(map) > 0;
    }

    /**
     * 通过单位代码查询单位信息
     * @author MrLu
     * @param dwmcdm
     * @createTime  2021/3/13 15:45
     * @return    |
     */
    public  SysGroup selectGroupByDwdm(String dwmcdm){
return  sysGroupMapper.selectGroupByDwdm(dwmcdm);
    }
}
