package com.mapper.jzgl;

import com.bean.jzgl.DTO.SysBmbDTO;

import java.util.List;

public interface SysBmbDTOMapper {


    int insertSelective(SysBmbDTO record);

    SysBmbDTO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysBmbDTO record);
     /**
     * 得到所有的编码类型
     * @author MrLu
     * @createTime  2020/9/29 16:14
     * @return  List<String>  |
      */
    List<String> getBmbType();

     /**
     * 得通过编码类型得到对应的编码
     * @author MrLu
     * @param type 编码类型
     * @createTime  2020/9/29 16:17
     * @return  List<SysBmbDTO>   |
      */
    List<SysBmbDTO> getBmbMapByType(String type);
}