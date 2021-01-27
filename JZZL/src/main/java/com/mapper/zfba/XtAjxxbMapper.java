package com.mapper.zfba;

import com.bean.zfba.XtAjxxb;

import java.util.Date;
import java.util.List;

public interface XtAjxxbMapper {


    XtAjxxb selectByPrimaryKey(Short id);


    /**
     * 查询新建的案件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    List<XtAjxxb> selectNewCase(Integer id);

    /**
     * 查询更新的案件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    List<XtAjxxb> selectUpdateCase(Date gxsj);


}