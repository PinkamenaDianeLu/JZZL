package com.bean.zfba.Converter;/**
 * @author Mrlu
 * @createTime 2021/6/17
 * @describe
 */


import com.bean.thkjdmtjz.JzFjzb;
import com.bean.zfba.XtWjflb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author MrLu
 * @createTime 2021/6/17 17:27
 * @describe
 */
@Mapper
public interface XtWjflbMapper {

    XtWjflbMapper INSTANCE = Mappers.getMapper(XtWjflbMapper.class);

    @Mapping(target = "dydm", source = "xyrbh")
    @Mapping(target = "jlsj", source = "createtime")
    @Mapping(target = "gxsj", source = "updatetime")
    @Mapping(target = "wjbid", source = "id")
    XtWjflb fjzbToXtWjflb(JzFjzb pc);
}
