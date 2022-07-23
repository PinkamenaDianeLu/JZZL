package com.mapper.zfba;

import com.bean.zfba.XtWjflb;

import java.util.List;
import java.util.Map;

public interface XtWjflbMapper {


    XtWjflb selectByPrimaryKey(Integer id);

    /**
     * 查询嫌疑人的文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 16:19
     */
    List<XtWjflb> selectRecordBySuspect(String jqbh, String dydm);


    /**
     * 查询警情编号下所有不对人的文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 17:16
     */
    List<XtWjflb> selectRecordNoSuspect(String jqbh);

     /**
     * 查询这个表是否属于受案回执
     * @author Mrlu
     * @param  map
     * @createTime  2021/3/3 9:31
     * @return    |
      */
    Integer selectSahzBool(Map<String, Object> map);


     /**
     * 查询某单位某时间后新增的文书
     * @author MrLu
     * @param 
     * @createTime  2021/3/25 10:22
     * @return    |  
      */
    List<XtWjflb> selectNewRecordsAfterDate(Map<String, Object> map);


    /**
     * 查询某些警情编号的文书
     * @author MrLu
     * @param
     * @createTime  2021/3/25 10:22
     * @return    |
     */
    List<XtWjflb> selectNewRecordsForAjbh(String ajbhAry);

}