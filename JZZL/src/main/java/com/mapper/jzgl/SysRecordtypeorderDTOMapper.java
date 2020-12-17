package com.mapper.jzgl;


import com.bean.jzgl.DTO.SysRecordtypeorderDTO;

import java.util.List;

public interface SysRecordtypeorderDTOMapper {


    SysRecordtypeorderDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRecordtypeorderDTO record);

    int updateByPrimaryKey(SysRecordtypeorderDTO record);

     /**
     * 根据卷类型查找文书类型的应有排序
     * @author MrLu
     * @param
     * @createTime  2020/12/17 11:16
     * @return    |
      */
   List<SysRecordtypeorderDTO> selectRecordtypeorderByArchivetype(Integer archivetype);
}