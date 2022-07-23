package com.mapper.thkjdmtjz;

import com.bean.thkjdmtjz.JzFjzb;

import java.util.Date;
import java.util.List;


public interface JzFjzbMapper {


    JzFjzb selectByPrimaryKey(Integer id);

     /**
     * 查看更新的附件
     * @author MrLu
     * @param
     * @createTime  2021/6/17 14:48
     * @return    |
      */
    List<JzFjzb> selectFjzbGtFxsj(Date gxsj);
}