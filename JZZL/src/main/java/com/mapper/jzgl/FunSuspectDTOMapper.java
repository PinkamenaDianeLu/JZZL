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


}