package com.mapper.zfba;

import com.bean.zfba.SysBadwb;

public interface SysBadwbMapper {



    SysBadwb selectByPrimaryKey(Integer id);

     /**
     * 查询警情编号的主办单位
     * @author MrLu
     * @param
     * @createTime  2021/1/4 18:47
     * @return    |
      */
    SysBadwb selectZbdwByJqbn(String jqbh);
}