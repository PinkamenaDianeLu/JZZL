package com.ZfbaETL.User.Server;

import com.bean.jzgl.DTO.SysRoleUserDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.jzgl.Source.SysRoleUser;
import com.bean.zfba.SysUser;
import com.bean.zfba.SysUserRole;
import com.mapper.jzgl.SysRoleUserMapper;
import com.mapper.jzgl.SysUserDTOMapper;
import com.mapper.zfba.SysUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/4 14:24
 * @describe
 */
@Service
public class UserServer {
    @Resource
    SysUserMapper zfbaSysUserMapper;
    @Resource
    SysUserDTOMapper jzglSysUserMapper;
    @Resource
    SysRoleUserMapper sysRoleUserMapper;

    public List<SysUser> selectNewSysuser(Integer userId,String groupCode) {
        return zfbaSysUserMapper.selectNewSysuser(userId,groupCode);
    }

    public List<SysUser> selectUpdateSysuser(Date gxsj,String groupCode) {
        return zfbaSysUserMapper.selectUpdateSysuser(gxsj,groupCode);
    }

    public void insertNewUser(SysUserDTO record) {
        jzglSysUserMapper.insertSelective(record);
    }

    public SysUserDTO selectByOriId(Integer oriId) {
        return jzglSysUserMapper.selectByOriId(oriId);
    }


    public void updateUser(SysUserDTO record) {
        jzglSysUserMapper.updateByPrimaryKeySelective(record);
    }


    /**
     * 查询用户的权限
     * @author MrLu
     * @param
     * @createTime  2021/6/9 10:51
     * @return    |
     */
    public List<SysUserRole> selectUserRole(Integer userid){
        return zfbaSysUserMapper.selectUserRole(userid);
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
}
