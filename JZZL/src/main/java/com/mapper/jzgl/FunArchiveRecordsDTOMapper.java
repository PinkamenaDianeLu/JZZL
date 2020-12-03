package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import org.apache.ibatis.annotations.Param;

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

     /**
     * 查询该文书的下一个版本
     * @author MrLu
     * @param id 文书od
     * @createTime  2020/10/27 14:51
     * @return  FunArchiveRecordsDTO  |
      */
    FunArchiveRecordsDTO selectNextRecord(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordsDTO record);

     /**
     * 通过警情编号分页查询警情对应文书
     * @author MrLu
     * @param  map (pageStart、pageEnd、jqbh)
     * @createTime  2020/10/4 14:02
     * @return  List<Map<String,Object>>  |
      */
    List<Map<String,Object>> selectRecordsByJqbhPage(Map<String,Object> map);
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

     /**
     * 将一个文书类型内顺序大于该值的文书顺序+1
     * @author MrLu
     * @param archivetypeid 文书类型id
      *@param thisorder 起始顺序
     * @createTime  2020/11/2 17:59
     * @return    |
      */
   void updateRecordOrderByTypeId(@Param("archivetypeid") int archivetypeid, @Param("id") int id, @Param("thisorder") int thisorder);


    /**
    * 查询一个文书类型中的最大顺序数
    * @author MrLu
    * @param id 文书id
    * @createTime  2020/11/5 15:02
    * @return int   |
     */
    int selectRecordMaxOrder(int id);


     /**
     * 分页查询文书
     * @author MrLu
     * @param 
     * @createTime  2020/11/5 17:55
     * @return    |  
      */
     List<Map<String, Object>>selectArchiveRecordPage(Map<String,Object> map);
    int selectArchiveRecordPageCount(Map<String,Object> map);



     /**
     * 查询该嫌疑人某类型的文书  按照sys_recordorder顺序排序
     * @author MrLu
     * @param suspectid 嫌疑人id
      * @param recordtype 文书类型
      * @param archiveseqid 整理次序id
     * @createTime  2020/11/26 17:01
     * @return  List<FunArchiveRecordsDTO>  |
      */
    List<FunArchiveRecordsDTO> selectRecordsBySuspectAType(@Param("suspectid") Integer suspectid,
                                                           @Param("recordtype") Integer recordtype,
                                                           @Param("archiveseqid") Integer archiveseqid,
                                                           @Param("archivetype")Integer archivetype);
}