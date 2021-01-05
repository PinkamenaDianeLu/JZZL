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
}