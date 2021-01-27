package com.mapper.zfba;

import com.bean.zfba.XtBarb;

import java.util.List;

public interface XtBarbMapper {


    XtBarb selectByPrimaryKey(Integer id);

     /**
     * 查询警情编号下的主办人
     * @author MrLu
     * @param
     * @createTime  2021/1/4 18:30
     * @return    |
      */
    XtBarb selectZbrByJqbh(String jqbh);

     /**
     * 查询警情编号下的辅办人
     * @author MrLu
     * @param
     * @createTime  2021/1/4 18:31
     * @return    |
      */
    List<XtBarb> selectFbrByJqbh(String jqbh);
}