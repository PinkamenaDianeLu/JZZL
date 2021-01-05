package com.mapper.zfba;

import com.bean.zfba.XtWjflb;

import java.util.List;

public interface XtWjflbMapper {


    XtWjflb selectByPrimaryKey(Integer id);

     /**
     * 查询嫌疑人的文书
     * @author MrLu
     * @param 
     * @createTime  2021/1/5 16:19
     * @return    |  
      */
   List<XtWjflb>  selectRecordBySuspect(String jqbh,String dydm);


    /**
    * 查询警情编号下所有不对人的文书
    * @author MrLu
    * @param 
    * @createTime  2021/1/5 17:16
    * @return    |  
     */
    List<XtWjflb>   selectRecordNoSuspect(String jqbh);

}