package com.ZfbaETL.User.Server;

import com.bean.jzgl.DTO.EtlLogsDTO;
import com.bean.jzgl.DTO.EtlTablelogDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.zfba.SysUser;
import com.mapper.jzgl.EtlTablelogDTOMapper;
import com.mapper.jzgl.SysUserDTOMapper;
import com.mapper.zfba.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
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

    public List<SysUser> selectNewSysuser(Integer userId) {
        return zfbaSysUserMapper.selectNewSysuser(userId);
    }

    public List<SysUser> selectUpdateSysuser(Date gxsj) {
        return zfbaSysUserMapper.selectUpdateSysuser(gxsj);
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
}
