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
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    List<XtAjxxb> selectNewCase(@Param("id") Integer id,@Param("groupcode")  String groupcode);

    /**
     * 查询更新的案件
     *J2306235300002020120002
     *  and  ID  &gt; #{id,jdbcType=DECIMAL}
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    List<XtAjxxb> selectUpdateCase(Date gxsj);


}