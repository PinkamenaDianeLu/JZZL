package com.mapper.thkjdmtjz;


import com.bean.thkjdmtjz.SysUserRoleCity;

import java.util.List;

/**
 * @Author       leewe
 * @createTime   2020/10/26 15:44
 * @Description  角色关联表数据库访问层
 */
public interface SysUserRoleCityMapper {

     /**
     * 查询一个用户所拥有的权限
     * @author MrLu
     * @param userid
     * @createTime  2021/8/17 10:12
     * @return    |
      */
   List<SysUserRoleCity> selectUserRoleByUserid(Integer userid);
}