package com.mapper.zfba;

import com.bean.zfba.Wh;
import com.bean.zfba.WjBjz;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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


     /**
     * 查询该文书的文号信息
     * @author MrLu
     * @param map ｛String wjbm, Integer id｝
     * @createTime  2021/3/19 14:33
     * @return    |
      */
    Wh selectWhByBmBid(Map<String,Object> map);
    /**
     * 查询该文书的文号信息
     * @author MrLu
     * @param map ｛String wjbm, Integer id｝
     * @createTime  2021/3/19 14:33
     * @return    |
     */
    Map<String,Object> selectObjectByBmBid(Map<String,Object> map);


     /**
     * 查询某单位一段时间之后新添加或有更新的文件
     * @author MrLu
     * @param ｛groupcode，lastpkdatevalue｝
     * @createTime  2021/3/24 10:46
     * @return    |
      */
    List<WjBjz> selectNewFilesAfterDate(Map<String,Object> map);
}