package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.SysRecordorderDTO;
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


     /**
     * 查询某一整理次序下对应文书代码的文书
     * @author MrLu
     * @param archiveseqid
      * @param recordscode 文书代码
      * @param  recordtype 文书类型
     * @createTime  2020/12/15 14:31
     * @return    |
      */
    List<FunArchiveRecordsDTO>  selectReocrdBySeqRcode(@Param("archiveseqid") Integer archiveseqid,
                                                       @Param("recordscode") String recordscode,
                                                       @Param("recordtype") Integer recordtype);
    /**
     *  查询原始卷次序下对应文书代码的文书
     * @author MrLu
     * @param archiveseqid
     * @param recordscode 文书代码
     * @createTime  2020/12/15 14:31
     * @return    |
     */
    List<FunArchiveRecordsDTO>  selectRecordByOriRecord(@Param("archiveseqid") Integer archiveseqid,
                            @Param("recordscode") String recordscode);
    /**
     * 查某个卷下某个类型对某个人的文书  按照顺序排序
     *
     * @param map {archivetype 0基础卷 7补充侦查工作卷 8提请批捕卷 9移送起诉卷
     *            recordtype 1诉讼文书卷  2证据材料卷  3补充侦查卷
     *            suspectid 嫌疑人id
     *            archiveseqid seqid}
     * @return |
     * @author MrLu
     * @createTime 2020/12/21 9:38
     */
    List<FunArchiveRecordsDTO> selectRecordOrderForSuspect(Map<String, Integer> map);


     /**
     * 查询该类型文书在某本卷中的某个类型的上一个文书（不能是封皮封底目录）
     * @author MrLu
     * @param map
     * @createTime  2020/12/23 18:40
     * @return    |
      */
    FunArchiveRecordsDTO selectPriveRecord(Map<String, Integer> map);

     /**
     * 把某个文书之后的文书顺序+1
     * @author MrLu
     * @param
     * @createTime  2020/12/23 18:36
     * @return    |
      */
    void updateOrderAdd(int archiveseqid,int archivetypeid,int thisorder);

    /**
     * 根据文书名称查询
     *
     * @param map {recordname 文书名 archiveseqid seqid}
     * @return |
     * @author MrLu
     * @createTime 2020/12/25 17:19
     */
    List<FunArchiveRecordsDTO> selectRecordbyName(Map<String, Object> map);


     /**
     * 根据uuid查询正在活跃的卷中的文书
     * @author MrLu
     * @param
     * @createTime  2020/12/28 14:37
     * @return    |
      */
    List<FunArchiveRecordsDTO> selectRecordByUuid(String recorduuid);

    FunArchiveRecordsDTO selectRecordByUuidSeq(@Param("recorduuid") String recorduuid,
                                                     @Param("archiveseqid") Integer archiveseqid);

     /**
     * 查询该文书的同代码的文书  （活跃的）
     * @author MrLu
     * @param  id
     * @createTime  2020/12/28 14:57
     * @return    |
      */
    List<FunArchiveRecordsDTO> selectSameRecordById(Integer id);

     /**
     * 根据uuid更新
     * @author MrLu
     * @param record
     * @createTime  2020/12/28 15:04
     * @return    |
      */
    void updateRecordByUuid(FunArchiveRecordsDTO record);


     /**
     * 查询含有某个文件代码的正在活跃的seq中的文书
     * @author MrLu
     * @param filecode
     * @createTime  2020/12/28 18:47
     * @return    |
      */
    List<FunArchiveRecordsDTO> selectActiveRecordByFilecode(String filecode);
}