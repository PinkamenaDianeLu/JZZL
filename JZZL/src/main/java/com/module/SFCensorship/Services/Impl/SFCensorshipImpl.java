package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.mapper.jzgl.FunArchiveSeqDTOMapper;
import com.module.SFCensorship.Services.SFCensorshipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/27 11:06
 * @describe 送检相关
 */
@Service
public class SFCensorshipImpl implements SFCensorshipService {
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
  public   List<FunArchiveSeqDTO> selectArchiveSeqPage(Map<String,Object> map){
        return  funArchiveSeqDTOMapper.selectArchiveSeqPage(map);
    };
    public  int selectArchiveSeqPageCount(Map<String,Object> map){
        return  funArchiveSeqDTOMapper.selectArchiveSeqPageCount(map);
    };

}
