package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;

import java.util.List;
import java.util.Map;

public interface FunArchiveRecordsDTOMapper {


     /**
     * 新建FunArchiveRecords
     * @author MrLu
     * @param  record (archivecode)
     * @createTime  2020/10/4 15:12
     * @return    |
      */
    int insertSelective(FunArchiveRecordsDTO record);

    FunArchiveRecordsDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordsDTO record);

     /**
     * 通过警情编号分页查询警情对应文书
     * @author MrLu
     * @param  map (pageStart、pageEnd、jqbh)
     * @createTime  2020/10/4 14:02
     * @return  List<FunArchiveRecordsDTO>  |
      */
    List<FunArchiveRecordsDTO> selectRecordsByJqbhPage(Map<String,Object> map);
    int selectRecordsByJqbhCount(Map<String,Object> map);

}