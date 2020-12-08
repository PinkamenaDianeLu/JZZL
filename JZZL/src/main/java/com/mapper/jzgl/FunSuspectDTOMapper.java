package com.mapper.jzgl;

import com.bean.jzgl.DTO.FunSuspectDTO;
import com.bean.jzgl.DTO.FunSuspectRecordDTO;

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
     * 按照seqId查询对应案件下的嫌疑人
     * @author MrLu
     * @param  id  eqId
     * @createTime  2020/12/8 18:47
     * @return  List<FunSuspectDTO>  |
      */
    List<FunSuspectDTO>   selectSuspectById(Integer id);
}