package com.mapper.zfba;

import com.bean.zfba.XtAjxxb;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface XtAjxxbMapper {


    XtAjxxb selectByPrimaryKey(Short id);


    /**
     * 查询新建的案件
     *      AND EXISTS (SELECT 1 FROM xt_badwb dw WHERE badwdm LIKE #{id,jdbcType=DECIMAL}||'%' AND aj.jqbh=dw.jqbh)
     *      and  ID &gt; #{id,jdbcType=DECIMAL}
     *      and  larq &gt; to_date('2020-01-01','yyyy-MM-dd')
     *
     *    where rownum &lt; 50
     *      &gt; #{id,jdbcType=DECIMAL}
     * @param isXz boolean 是否抽取行政案件
     * @param groupcode 单位代码
     * @param id 开始id
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    List<XtAjxxb> selectNewCase(@Param("id") Integer id,@Param("groupcode")  String groupcode,@Param("isXz")  boolean isXz);

    /**
     * 查询更新的案件
     * @param gxsj Date
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    List<XtAjxxb> selectUpdateCase(Date gxsj);


     /**
     * 查看需要抽取的案件
     * @author MrLu
     *  TEMPAJBH 表 只有省厅版才有
     * @createTime  2021/8/18 10:35
     * @return    |
      */
     List<XtAjxxb>  selectNeededAj();

      /**
      * 按照案件编号查询
      * @author MrLu
      * @param
      * @createTime  2021/12/29 16:16
      * @return    |
       */
    XtAjxxb selectCaseByJqbh(String jqbh);


}