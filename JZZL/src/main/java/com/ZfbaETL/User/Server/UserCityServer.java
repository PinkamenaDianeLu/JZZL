package com.ZfbaETL.User.Server;

import com.bean.jzgl.DTO.SysRoleUserDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.thkjdmtjz.SysUserCity;
import com.bean.thkjdmtjz.SysUserRoleCity;
import com.mapper.jzgl.SysRoleUserMapper;
import com.mapper.jzgl.SysUserDTOMapper;
import com.mapper.thkjdmtjz.SysUserCityMapper;
import com.mapper.thkjdmtjz.SysUserRoleCityMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/8/17 9:22
 * @describe 地市版卷整理抽取用户
 */

@Service
public class UserCityServer {
    @Resource
    SysUserDTOMapper jzglSysUserMapper;
    @Resource
    SysUserCityMapper sysUserCityMapper;
    @Resource
    SysRoleUserMapper sysRoleUserMapper;
    @Resource
    SysUserRoleCityMapper sysUserRoleCityMapper;

    //selectUserRoleByUserid

     /**
     * 查询新增的用户
     * @author MrLu
     * @param userId 最后更新的用户id
     * @createTime  2021/8/17 10:14
     * @return    |
      */
    public List<SysUserCity> selectNewSysuser(Integer userId) {
        return sysUserCityMapper.selectNewSysuser(userId);
    }

    /**
     * 查询用户的权限
     * @author MrLu
     * @param
     * @createTime  2021/6/9 10:51
     * @return    |
     */
    public List<SysUserRoleCity> selectUserRole(Integer userid){
        return sysUserRoleCityMapper.selectUserRoleByUserid(userid);
    }

    public void insertNewUser(SysUserDTO record) {
        jzglSysUserMapper.insertSelective(record);
    }


    public SysUserDTO selectJzUserByIdCard(String sfzh) {
      return   jzglSysUserMapper.selectJzUserByIdCard(sfzh);
    }
    /**
     * 删除用户的权限
     * @author MrLu
     * @param
     * @createTime  2021/6/9 14:32
     */
    public void deleteRoles(Integer sysuserid){
        sysRoleUserMapper.deleteRoles(sysuserid);
    };

    /**
     * 插入权限
     * @author MrLu
     * @param record
     * @createTime  2021/6/9 14:33
     */
    public void insertUserRole(SysRoleUserDTO record){
        sysRoleUserMapper.insert(record);
    }


     /**
     * 查询更新的用户
     * @author MrLu
     * @param
     * @createTime  2021/8/17 10:40
     * @return    |
      */
    public List<SysUserCity> selectUpdateSysuser(Date gxsj) {
        return sysUserCityMapper.selectUpdateSysuser(gxsj);
    }

    public SysUserDTO selectByOriId(Integer oriId) {
        return jzglSysUserMapper.selectByOriId(oriId);
    }
}
