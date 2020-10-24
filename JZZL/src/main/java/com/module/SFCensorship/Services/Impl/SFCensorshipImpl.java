package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.*;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.FunArchiveSFC;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunCaseInfo;
import com.bean.jzgl.Source.FunPeopelCase;
import com.mapper.jzgl.*;
import com.module.SFCensorship.Services.SFCensorshipService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;

    @Override
    public List<FunArchiveSFC> selectArchiveSFCPage(Map<String, Object> map) {
        return FunArchiveSFCMapper.INSTANCE.pcDTOToPcs(funArchiveSFCDTOMapper.selectArchiveSFCPage(map));
    }

    @Override
    public int selectArchiveSFCPageCount(Map<String, Object> map) {
        return funArchiveSFCDTOMapper.selectArchiveSFCPageCount(map);
    }

    /**
     * 新建送检记录表
     *
     * @param record
     * @author MrLu
     * @createTime 2020/10/4 15:18
     */
    @Override
    public void insertFunArchiveSeq(FunArchiveSeq record) {
        FunArchiveSeqDTO FunArchiveSeqDTO=FunArchiveSeqMapper.INSTANCE.pcToPcDTO(record);
        funArchiveSeqDTOMapper.insertSelective(FunArchiveSeqDTO);
        record.setId(FunArchiveSeqDTO.getId());
    }

    @Override
    public void insertFunArchiveSFC(FunArchiveSFC funArchiveSFC) {
        FunArchiveSFCDTO funArchiveSFCDTO= FunArchiveSFCMapper.INSTANCE.pcToPcDTO(funArchiveSFC);
        funArchiveSFCDTOMapper.insertSelective(funArchiveSFCDTO);
        funArchiveSFC.setId(funArchiveSFCDTO.getId());
    }

    @Override
    public FunCaseInfo getFunCaseInfoById(Integer id) {
        return FunCaseInfoMapper.INSTANCE.pcDTOToPc(funCaseInfoDTOMapper.selectByPrimaryKey(id));
    }

    @Override
    public int getLastSFCSeq(int peoplecaseid) {
        return funArchiveSeqDTOMapper.getLastSFCSeq(peoplecaseid);
    }

    @Override
    public List<FunArchiveRecordsDTO> selectRecordsByJqbh(Map<String,Object> map) {
        return funArchiveRecordsDTOMapper.selectRecordsByJqbh(map);
    }

    @Override
    public List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(String jqbh,int archiveseqid) {
        Map<String, Object> pMap = new HashMap<>();//查询参数
        pMap.put("jqbh", jqbh);
        pMap.put("archiveseqid", 0);
        return funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap);
    }

    @Override
    public void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO) {
        funArchiveTypeDTOMapper.insertSelective(funArchiveTypeDTO);
    }

    @Override
    public void insertFunArchiveRecords(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }


}
