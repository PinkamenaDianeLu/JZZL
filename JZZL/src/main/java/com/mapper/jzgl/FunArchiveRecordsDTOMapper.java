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

     /**
     * 通过警情编号查询警情对应文书
     * @author MrLu
     * @param map （jqbh）
     * @createTime  2020/10/8 10:37
     * @return  List<FunArchiveRecordsDTO>  |
      */
    List<FunArchiveRecordsDTO>  selectRecordsByJqbh(Map<String,Object> map);

     /**
     * 根据typeid查找文书  根据thisorder排序
     * @author MrLu
     * @param map  (typeid)
     * @createTime  2020/10/9 11:33
     * @return   List<FunArchiveRecordsDTO> |
      */
    List<FunArchiveRecordsDTO>  selectRecordsByTypeid(Map<String,Object> map);

}