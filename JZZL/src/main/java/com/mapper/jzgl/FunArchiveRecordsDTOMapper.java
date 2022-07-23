package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FunArchiveRecordsDTOMapper {


    /**
     * 新建FunArchiveRecords
     *
     * @param record (archivecode)
     * @return |
     * @author MrLu
     * @createTime 2020/10/4 15:12
     */
    int insertSelective(FunArchiveRecordsDTO record);

    FunArchiveRecordsDTO selectByPrimaryKey(Integer id);

    /**
     * 查询该文书的下一个版本
     *
     * @param id 文书od
     * @return FunArchiveRecordsDTO  |
     * @author MrLu
     * @createTime 2020/10/27 14:51
     */
    FunArchiveRecordsDTO selectNextRecord(Integer id);

    int updateByPrimaryKeySelective(FunArchiveRecordsDTO record);

    /**
     * 通过警情编号分页查询警情对应文书
     * ,(SELECT LISTAGG(suspect.SUSPECTNAME, ',') WITHIN GROUP(ORDER BY suspect.SUSPECTNAME)
     *         FROM FUN_SUSPECT suspect WHERE scbj=0
     *         AND EXISTS (SELECT 1
     *         FROM FUN_SUSPECT_RECORD RE
     *         WHERE RE.scbj=0 AND RE.SUSPECTID = SUSPECT.ID
     *         AND RE.RECORDID = records.ID) ) AS SUSPECTNAME
     *         ,'你妈' AS SUSPECTNAME
     * @param map (pageStart、pageEnd、jqbh)
     * @return List<Map < String, Object>>  |
     * @author MrLu
     * @createTime 2020/10/4 14:02
     */
    List<FunArchiveRecordsDTO> selectRecordsByJqbhPage(Map<String, Object> map);

    int selectRecordsByJqbhCount(Map<String, Object> map);

    /**
     * 通过警情编号查询警情对应文书
     *
     * @param map （jqbh）
     * @return List<FunArchiveRecordsDTO>  |
     * @author MrLu
     * @createTime 2020/10/8 10:37
     */
    List<FunArchiveRecordsDTO> selectRecordsByJqbh(Map<String, Object> map);

    /**
     * 根据typeid查找文书  根据thisorder排序
     *
     * @param map (typeid)
     * @return List<FunArchiveRecordsDTO> |
     * @author MrLu
     * @createTime 2020/10/9 11:33
     */
    List<FunArchiveRecordsDTO> selectRecordsByTypeid(Map<String, Object> map);


    /**
     * 查询该嫌疑人对应的文书
     *
     * @param map （archivetypeid，suspectid）
     * @return |
     * @author Mrlu
     * @createTime 2021/2/19 16:46
     */
    List<FunArchiveRecordsDTO> selectRecordsBySuspects(Map<String, Object> map);

    /**
     * 将一个文书类型内顺序大于该值的文书顺序+1
     *
     * @param archivetypeid 文书类型id
     * @param thisorder     起始顺序
     * @return |
     * @author MrLu
     * @createTime 2020/11/2 17:59
     */
    void updateRecordOrderByTypeId(@Param("archivetypeid") int archivetypeid, @Param("id") int id, @Param("thisorder") int thisorder);


    /**
     * 查询一个文书类型中的最大顺序数
     *
     * @param id 文书id
     * @return int   |
     * @author MrLu
     * @createTime 2020/11/5 15:02
     */
    int selectRecordMaxOrder(int id);

     /**
     * 查询一个type中对嫌疑人文书的最大顺序（不针对某个嫌疑人）
     * @author MrLu
     * @param typeid
     * @createTime  2021/3/25 16:19
     * @return    |
      */
    Integer selectRsMaxOrderByTypeid(int typeid);
     /**
     * 查询一个type中文书的最大顺序
     * @author MrLu
     * @param typeid int
     * @createTime  2021/6/18 15:44
     * @return    Integer|
      */
    Integer selectMaxOrderByTypeid(int typeid);
    /**
     * 分页查询文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/11/5 17:55
     */
    List<Map<String, Object>> selectArchiveRecordPage(Map<String, Object> map);

    int selectArchiveRecordPageCount(Map<String, Object> map);


    /**
     * 查询该嫌疑人某类型的文书  按照sys_recordorder顺序排序
     *
     * @param suspectid    嫌疑人id
     * @param recordtype   文书类型
     * @param archiveseqid 整理次序id
     * @return List<FunArchiveRecordsDTO>  |
     * @author MrLu
     * @createTime 2020/11/26 17:01
     */
    List<FunArchiveRecordsDTO> selectRecordsBySuspectAType(@Param("suspectid") Integer suspectid,
                                                           @Param("recordtype") Integer recordtype,
                                                           @Param("archiveseqid") Integer archiveseqid,
                                                           @Param("archivetype") Integer archivetype);


    /**
     * 查询某一整理次序下对应文书代码的文书
     *
     * @param archiveseqid
     * @param recordscode  文书代码
     * @param recordtype   文书类型
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 14:31
     */
    List<FunArchiveRecordsDTO> selectReocrdBySeqRcode(@Param("archiveseqid") Integer archiveseqid,
                                                      @Param("recordscode") String recordscode,
                                                      @Param("recordtype") Integer recordtype);

    /**
     * 查询原始卷次序下对应文书代码的文书
     *
     * @param archiveseqid
     * @param recordscode  文书代码
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 14:31
     */
    List<FunArchiveRecordsDTO> selectRecordByOriRecord(@Param("archiveseqid") Integer archiveseqid,
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
     *
     * @param map
     * @return |
     * @author MrLu
     * @createTime 2020/12/23 18:40
     */
    FunArchiveRecordsDTO selectPriveRecord(Map<String, Integer> map);

    /**
     * 把某个文书之后的文书顺序+1
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/23 18:36
     */
    void updateOrderAdd(int archiveseqid, int archivetypeid, int thisorder);

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
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 14:37
     */
    List<FunArchiveRecordsDTO> selectRecordByUuid(String recorduuid);

    /**
     * 根据文书的唯一标识码查询该seq中的文书
     *
     * @param recorduuid
     * @param archiveseqid
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 9:41
     */
    FunArchiveRecordsDTO selectRecordByUuidSeq(@Param("recorduuid") String recorduuid,
                                               @Param("archiveseqid") Integer archiveseqid);

    /**
     * 查询该文书的同代码的文书  （活跃的）
     *
     * @param id
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 14:57
     */
    List<FunArchiveRecordsDTO> selectSameRecordById(Integer id);

    /**
     * 根据uuid更新
     *
     * @param record
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 15:04
     */
    void updateRecordByUuid(FunArchiveRecordsDTO record);


    /**
     * 查询含有某个文件代码的正在活跃的seq中的文书
     *
     * @param filecode
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 18:47
     */
    List<FunArchiveRecordsDTO> selectActiveRecordByFilecode(String filecode);


    /**
     * 查询一个type中不带指定嫌疑人的文书
     *
     * @param map {int archivetypeid,String [] notsuspectids}
     * @return |
     * @author Mrlu
     * @createTime 2021/3/8 10:09
     */
    List<FunArchiveRecordsDTO> selectRecordsNotSuspectByType(Map<String, Object> map);

    /**
     * 查询多个seq下不同type的文书  不包含系统文书
     * @param map  ｛seqids,recordtype｝
     * @return |
     * @author Mrlu
     * @createTime 2021/3/10 17:46
     */

    List<FunArchiveRecordsDTO> selectRecordsBySeqType(Map<String, Object> map);

     /**
     * 查询所有应该盖章但是没盖章的文书
     * @author MrLu
     * @createTime  2021/3/23 10:53
     * @return    |
      */
    List<FunArchiveRecordsDTO> selectNoWhRecords();

     /**
     * 根据文件表名表id更新文号
     * @author MrLu
     * @param ｛recordwh，wjbm,wjbid｝
     * @createTime  2021/3/24 9:53
     * @return    |
      */
    void updateRecordWhByWjMessage(Map<String, Object> map);


     /**
     * 根据文件表名表id查询未被送检的文书
     * @author MrLu
     * @param  ｛wjbm,wjbid｝
     * @createTime  2021/3/24 15:35
     * @return    |
      */
    List<FunArchiveRecordsDTO> selectRecordsByWjMessageNotSend(Map<String, Object> map);

     /**
     * 根据文件表名表idseqid查询文书
     * @author MrLu
     * @param
     * @createTime  2021/6/24 17:11
     * @return    |
      */
    FunArchiveRecordsDTO  selectRecordsByWjMessage(@Param("wjbm") String wjbm,@Param("wjbid") Integer wjbid,@Param("archiveseqid") Integer archiveseqid);
     /**
     * 删除文书 isdelete=2 未被送检的
     * @author MrLu
     * @param  ｛wjbm,wjbid｝
     * @createTime  2021/3/25 14:17
     * @return    |
      */
    void  deleteRecordsByWjMessageNotSend(Map<String, Object> map);


     /**
     * 根据uuid查询文书
     * @author MrLu
     * @param  uuid
     * @createTime  2021/3/31 14:22
     * @return    |
      */
    List<FunArchiveRecordsDTO> selectFunArchiveRecordsByUUID(String uuid);

     /**
     * 更新一个seq下的错误顺序 将非系统文书的负数顺序改为1
     * @author MrLu
     * @param archiveseqid
     * @createTime  2021/12/21 10:38
     * @return    |
      */
    void updateWrongOrderBySeq(Integer archiveseqid);

     /**
     * 查询文书名
     * @author MrLu
     * @param ids
     * @createTime  2021/12/21 10:54
     * @return    |
      */
    List<String> selectRecordNameByRecordIds(@Param("ids") int[] ids);


     /**
     * 更新为已经生成图片
     * @author MrLu
     * @param 
     * @createTime  2022/1/11 15:44
     * @return    |  
      */
    void updateRecordsIscoverimg(Integer iscoverimg,Integer id);
}