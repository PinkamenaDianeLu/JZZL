package com.mapper.thkjdmtjz;


import com.bean.thkjdmtjz.SysUserCity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author       leewe
 * @createTime   2020/10/26 15:44
 * @Description  用户信息表数据库访问层
 */
public interface SysUserCityMapper {

    /**
     * @describe     查询用户基本信息表
     * @param        map<String,Object> map  查询条件键值对
     * @author       leewe
     * @createTime   2020/10/26 15:41
     * @version 1.0
     */
    List<SysUserCity> selectBySysUser(Map<String, Object> map);

    /**
     * @describe     插入用户信息表
     * @param        map<String,Object> map  插入用户对象实体类
     * @author       leewe
     * @createTime   2020/10/26 15:44
     * @version 1.0
     */
    int insertBySysUser(Map<String, Object> map);

    /**
     * @describe     修改用户信息表
     * @param        map<String,Object> map  修改用户对象实体类
     * @author       leewe
     * @createTime   2020/10/2          6 15:45
     * @version 1.0
     *
     */
    int updateBySysUser(Map<String, Object> map);



    /**
     * @describe     查询此角色下有什么用户
     * @param          map  查询条件键值对
     * @author       YZQ
     * @createTime   2020/11/13 12:52
     * @version 1.0
     */
    List<SysUserCity> listRoleUserPage(Map<String, Object> map);


     /**
     * 查询新增的用户
     * @author MrLu
     * @param userId 用户id
     * @createTime  2021/8/17 9:52
     * @return    |
      */
    List<SysUserCity>  selectNewSysuser(Integer userId);


     /**
     * 查询更新的用户
     * @author MrLu
     * @param
     * @createTime  2021/8/17 10:40
     * @return    |
      */
    List<SysUserCity> selectUpdateSysuser(Date gxsj);

}