package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface FunArchiveFilesDTOMapper {

    int insert(FunArchiveFilesDTO record);

    int insertSelective(FunArchiveFilesDTO record);

    FunArchiveFilesDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunArchiveFilesDTO record);

     /**
     * 将一个文书内顺序大于该值的文件顺序+1
     * @author MrLu

     * @createTime  2020/11/2 16:47
     * @return   void |
      */
    void updateOrderByRecordId(Map<String,Object> map);

     /**
     * 根据文书id更新文件
     * @author MrLu
     * @param record
     * @createTime  2020/11/2 16:48
     * @return  void  |
      */
    void updateFileByRecordId(FunArchiveFilesDTO record);
     /**
     * 根据文件代码更新文件
     * @author MrLu
     * @param
     * @createTime  2020/11/2 18:20
     * @return    |
      */
    void updateFileBySeqIdFileCode(FunArchiveFilesDTO record);
     /**
     * 通过文书id查找其文书图片（最新版本的）
     * @author MrLu
     * @param archiverecordid 文书id
     * @createTime  2020/10/15 17:58
     * @return  FunArchiveFilesDTO  |
      */
     List<FunArchiveFilesDTO>  selectRecordFilesByRecordId(@Param("archiverecordid") int archiverecordid,@Param("isdelete") Integer isdelete);


      /**
      * 根据filecode数组按顺序查询文件
      * @author MrLu
      * @param map
      * @createTime  2020/11/5 10:16
      * @return  List<FunArchiveFilesDTO>  |
       */
    List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(Map<String,Object> map);
     /**
     * 根据文件代码查询该文件的历史版本
     * @author MrLu
     * @param
     * @createTime  2020/10/15 18:13
     * @return    |
      */
    List<FunArchiveFilesDTO> selectFilesHistory(String filecode);

     /**
     * 根据文书代码找到正在显示的文书
     * @author MrLu
     * @param map {filecode:文件代码,archiverecordid:文书id}
     * @createTime  2020/10/22 9:32
     * @return  FunArchiveFilesDTO  |
      */
    FunArchiveFilesDTO selectFilesByFileCode(Map<String,Object> map);

     /**
     * 查询一个文书中的最大顺序
     * @author MrLu
     * @param archiverecordid 文书id
     * @createTime  2020/11/5 14:59
     * @return   int |
      */
    int selectFileMaxOrder(int archiverecordid);
     /**
     * 根据文件代码更新文件
     * @author MrLu
     * @param record
     * @createTime  2020/12/28 15:11
     * @return    |
      */
   void updateFileByFilecode(FunArchiveFilesDTO record);

    /**
    * 查询一个seq下是否有重复的filecode
    * @author MrLu
    * @param  filecode
     * @param  archiveseqid
    * @createTime  2020/12/28 18:19
    * @return    |
     */
   Integer selectRepeatedlyFileCodeBySeqid(@Param("filecode")String filecode,@Param("archiveseqid")int archiveseqid);

    /**
    * 根据文书和文件代码更新文件的顺序或名称
    * @author MrLu
    * @param map {[filename|thisorder],filecode,archiverecordid}
    * @createTime  2020/12/28 19:03
    * @return    |
     */
   void updateFileOrderByRecord(Map<String,Object> map);

}