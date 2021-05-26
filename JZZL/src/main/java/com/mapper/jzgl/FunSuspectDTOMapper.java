package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunSuspectDTO;

import java.util.List;

public interface FunSuspectDTOMapper {


    int insertSelective(FunSuspectDTO record);

    FunSuspectDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FunSuspectDTO record);

     /**
     * 根据案件信息表id查询嫌疑人
     * @author MrLu
     * @param caseinfoid 案件信息表id
     * @createTime  2020/11/22 18:24
     * @return  List<FunSuspectDTO>  |
      */
   List<FunSuspectDTO> selectSuspectByCaseinfoId(Integer caseinfoid);

    /**
    * 查询一个文书对应的所有嫌疑人
    * @author MrLu
    * @param recordid 文书id
    * @createTime  2020/11/25 18:09
    * @return   List<FunSuspectRecordDTO> |
     */
    List<FunSuspectDTO> selectSuspectByRecordid(Integer recordid);

     /**
     * 按照seqid查询本次送检的嫌疑人
     * @author Mrlu
     * @param
     * @createTime  2021/2/20 16:11
     * @return    |
      */
    List<FunSuspectDTO> selectSuspectByArchiveseqid(Integer archiveseqid);
     /**
     * 按照id查询对应案件下的嫌疑人
     * @author MrLu
     * @param  id  caseinfoid
     * @createTime  2020/12/8 18:47
     * @return  List<FunSuspectDTO>  |
      */
    List<FunSuspectDTO>   selectSuspectById(Integer id);

     /**
     * 通过seqid查询该案件的嫌疑人
     * @author MrLu
     * @param seqid
     * @createTime  2020/12/23 15:00
     * @return    |
      */
    List<FunSuspectDTO> selectSuspectBySeqId(Integer seqid);

     /**
     * 根据id查询嫌疑人信息
     * @author Mrlu
     * @param  array
     * @createTime  2021/3/8 11:09
     * @return    |
      */
    List<FunSuspectDTO>  selectByPrimaryKeys(String [] array);


     /**
     * 根据嫌疑人编号查询嫌疑人
     * @author MrLu
     * @param suspectcode
     * @createTime  2021/3/25 14:28
     * @return    |
      */
    FunSuspectDTO selectSuspectBySuspectcode(String suspectcode);


     /**
     * 查询案件中嫌疑人的最大顺序
     * @author MrLu
     * @param caseinfoid
     * @createTime  2021/3/25 15:42
     * @return    |
      */
    int selectMaxOrderByCaseid(Integer caseinfoid);
}