package com.mapper.zfba;

import com.bean.zfba.XtXyrxxb;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface XtXyrxxbMapper {

     /**
     * 查找这个案件的嫌疑人
     * @author MrLu
     * @param jqbh 警情编号
     * @createTime  2021/1/5 14:31
     * @return    |
      */
    List<XtXyrxxb> selectXyrByJqbh(@Param("jqbh") String jqbh);


     /**
     * 通过嫌疑人编号会查询嫌疑人信息
     * @author MrLu
     * @param
     * @createTime  2021/3/25 15:33
     * @return    |
      */
    XtXyrxxb selectXyrByXyrbh(String xyrbh);
}