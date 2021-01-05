package com.mapper.jzgl;


import com.bean.jzgl.DTO.EtlTablelogDTO;

import java.util.List;

public interface EtlTablelogDTOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EtlTablelogDTO record);

    int insertSelective(EtlTablelogDTO record);

    EtlTablelogDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EtlTablelogDTO record);

    int updateByPrimaryKey(EtlTablelogDTO record);

    /**
     * 查询某个值 （哎我不想取名了，自己明白就行了 d=====(￣▽￣*)b）
     *
     * @param tablename  表名
     * @param lastpkname 唯一标识值
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 14:38
     */
    EtlTablelogDTO selectLastValue(String tablename, String lastpkname);
}