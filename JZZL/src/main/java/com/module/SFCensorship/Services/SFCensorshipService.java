package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;

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
    List<FunArchiveSeqDTO> selectArchiveSeqPage(Map<String,Object> map);
    int selectArchiveSeqPageCount(Map<String,Object> map);

}
