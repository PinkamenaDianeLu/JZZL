package com.mapper.zfba;

import com.bean.zfba.WjWjdz;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WjWjdzMapper {

     /**
     * 根据文件表名表id查询文件的图片地址
     * @author MrLu
     * @param
     * @createTime  2021/1/6 10:54
     * @return    |
      */
    List<WjWjdz> selectWjdzByBmBid (@Param("wjbid") Integer wjbid, @Param("wjbm") String wjbm);
}