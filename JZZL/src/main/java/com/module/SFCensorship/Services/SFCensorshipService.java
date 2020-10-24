package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.*;

import java.util.List;
import java.util.Map;

public interface SFCensorshipService {

    /**
     * 分页查询送检记录表
     * @author MrLu
     * @param map 分页参数
     * @createTime  2020/9/27 15:48
     * @return    |
     */
    List<FunArchiveSFC> selectArchiveSFCPage(Map<String,Object> map);
    int selectArchiveSFCPageCount(Map<String,Object> map);

     /**
     * 新建送检整理次序
     * @author MrLu
     * @param record
     * @createTime  2020/10/8 13:58
     * @return  void  |
      */
    void insertFunArchiveSeq(FunArchiveSeq record);

    void insertFunArchiveSFC(FunArchiveSFC funArchiveSFC);
     /**
     * 通过id查询人案表
     * @author MrLu
     * @param id 表id
     * @createTime  2020/10/4 16:19
     * @return  FunCaseInfo  |
      */
     FunCaseInfo getFunCaseInfoById(Integer id);

    /**
     * 得到某个案件最后的送检次序
     * @author MrLu
     * @param peoplecaseid 案件表id
     * @createTime  2020/10/4 16:31
     * @return   int  |
     */
    int getLastSFCSeq(int peoplecaseid);

    /**
     * 通过警情编号查询警情对应文书
     * @author MrLu
     * @param map （jqbh）
     * @createTime  2020/10/8 10:37
     * @return  List<FunArchiveRecordsDTO>  |
     */
    List<FunArchiveRecordsDTO> selectRecordsByJqbh(Map<String,Object> map);

    /**
     * 查询卷类型表
     * @author MrLu
     * @param jqbh 警情编号
     * @param archiveseqid 送检次序id
     * @createTime  2020/10/8 11:05
     * @return  List<FunArchiveTypeDTO>  |
     */
    List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(String jqbh,int archiveseqid);


     /**
     * 新建卷类型
     * @author MrLu
     * @param funArchiveTypeDTO
     * @createTime  2020/10/8 11:18
     * @return   void |
      */
    void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO);


     /**
     * 新建送检卷
     * @author MrLu
     * @param record
     * @createTime  2020/10/8 11:38
     * @return  void  |
      */
   void insertFunArchiveRecords(FunArchiveRecordsDTO record);


}
