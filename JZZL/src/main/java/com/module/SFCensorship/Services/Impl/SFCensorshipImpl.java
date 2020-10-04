package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveSeqMapper;
import com.bean.jzgl.Converter.FunPeopelCaseMapper;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunPeopelCase;
import com.mapper.jzgl.FunArchiveSeqDTOMapper;
import com.mapper.jzgl.FunPeopelCaseDTOMapper;
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
    @Resource
    FunPeopelCaseDTOMapper funPeopelCaseDTOMapper;
    @Override
  public   List<FunArchiveSeq> selectArchiveSeqPage(Map<String,Object> map){
        return  FunArchiveSeqMapper.INSTANCE.pcDTOToPcs(funArchiveSeqDTOMapper.selectArchiveSeqPage(map));
    };
    @Override
    public  int selectArchiveSeqPageCount(Map<String,Object> map){
        return  funArchiveSeqDTOMapper.selectArchiveSeqPageCount(map);
    }

     /**
     * 新建送检记录表
     * @author MrLu
     * @param record
     * @createTime  2020/10/4 15:18
      */
    @Override
    public void insertSelective(FunArchiveSeq record) {
        funArchiveSeqDTOMapper.insertSelective(FunArchiveSeqMapper.INSTANCE.pcToPcDTO(record));
    }

    @Override
    public FunPeopelCase getFunPeopelCaseById(Integer id) {
        return FunPeopelCaseMapper.INSTANCE.pcDTOToPc(funPeopelCaseDTOMapper.selectByPrimaryKey(id));
    }

    @Override
    public int getLastSFCSeq(int peoplecaseid) {
        return funArchiveSeqDTOMapper.getLastSFCSeq(peoplecaseid);
    }




}
