package com.mapper.thkjdmtjz;

import com.bean.thkjdmtjz.JzFjcb;

import java.util.List;

public interface JzFjcbMapper {


    JzFjcb selectByPrimaryKey(Integer id);

     /**
     * 根据附件主表id查询从表内容
     * @author MrLu
     * @param  fjzbid Integer
     * @createTime  2021/6/18 10:21
     * @return    |
      */
    List<JzFjcb> selectFjcbByzbid(Integer fjzbid);
}