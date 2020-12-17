package com.mapper.jzgl;


import com.bean.jzgl.DTO.SysRecordorderDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysRecordorderDTOMapper {

    int insert(SysRecordorderDTO record);

    int insertSelective(SysRecordorderDTO record);

    SysRecordorderDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRecordorderDTO record);

    int updateByPrimaryKey(SysRecordorderDTO record);

    /**
     * 按照送检卷类型和卷类型查询默认顺序
     *
     * @param map {recordcode,archivetype,recordtype} 文书代码、送检卷卷类型、文书卷类型
     * @return List<SysRecordorderDTO>    |
     * @author MrLu
     * @createTime 2020/11/26 15:23
     */
    SysRecordorderDTO selectRecordOrderByTypes(Map<String,Object> map);
     /**
     * 查询某种类型的卷的文书顺序
     * @author MrLu
     * @param archivetype 0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     * @createTime  2020/12/15 11:11
     * @return    |
      */
    List<SysRecordorderDTO> selectSysRecordOrderByArchiveType(int archivetype);

     /**
     * 查询某种类型的卷的中的文书类型
     * @author MrLu
     * @param  archivetype 0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     * @createTime  2020/12/17 9:59
     * @return    |
      */
    List<Integer> selectRecordTypeByArchiveType(int archivetype);
}