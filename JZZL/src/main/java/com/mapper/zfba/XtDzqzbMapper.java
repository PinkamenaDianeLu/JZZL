package com.mapper.zfba;

import com.bean.zfba.XtDzqzb;

import java.util.Map;


public interface XtDzqzbMapper {



    XtDzqzb selectByPrimaryKey(Integer id);

     /**
     * 查询某个文书是否盖章
     * @author MrLu
     * @param map ｛wjbm，wjbid｝
     * @createTime  2021/3/24 9:35
     * @return    |
      */
    Integer selectSealByRecord(Map<String,Object> map);

}