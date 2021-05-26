package com.mapper.jzgl;


import com.bean.jzgl.Source.SysRecordMessage;

public interface SysRecordMessageMapper {



    SysRecordMessage selectByPrimaryKey(Integer id);

     /**
     * 通过文件代码查询文书相关信息
     * @author MrLu
     * @param 
     * @createTime  2021/3/19 14:26
     * @return    |  
      */
   SysRecordMessage selectMessageByCode(String recordcode);


}