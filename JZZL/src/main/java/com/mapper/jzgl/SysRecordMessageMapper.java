package com.mapper.jzgl;


import com.bean.jzgl.Source.SysRecordMessage;
import org.apache.ibatis.annotations.Param;

public interface SysRecordMessageMapper {



    SysRecordMessage selectByPrimaryKey(Integer id);

     /**
     * 通过文件代码查询文书相关信息
     * @author MrLu
     * @param recordcode 文书代码 案宗
      * @param recordtype  文书卷类型 基础卷和原始卷为null
     * @createTime  2021/3/19 14:26
     * @return    |  
      */
   SysRecordMessage selectMessageByCode(@Param("recordcode") String recordcode, @Param("recordtype") Integer recordtype);


}