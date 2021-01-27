package com.mapper.zfba;

import com.bean.zfba.WjBjz;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjBjzMapper {

     /**
     * 根据id查询
     * @author Mrlu
     * @param
     * @createTime  2021/1/21 17:16
     * @return    |
      */
    WjBjz selectByPrimaryKey(Integer id);
    /**
     * 根据文件表名表id查询文件的图片地址
     * @author MrLu
     * @param
     * @createTime  2021/1/6 10:54
     * @return    |
     */
    List<WjBjz> selectWjdzByBmBid (@Param("wjbid") Integer wjbid, @Param("wjbm") String wjbm);
}