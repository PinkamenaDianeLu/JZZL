package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.DTO.FunArchiveCoverDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.mapper.jzgl.*;
import com.module.SFCensorship.Services.FileManipulationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MrLu
 * @createTime 2020/10/23 10:04
 * @describe
 */
@Service
public class FileManipulationImpl implements FileManipulationService {
    @Resource
    private FunArchiveCoverDTOMapper funArchiveCoverDTOMapper;
    @Resource
    private FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Override
    public FunArchiveCoverDTO selectFunArchiveCoverDTOByFileId (Integer fileid){
        return  funArchiveCoverDTOMapper.selectFunArchiveCoverDTOByFileId(fileid);
    }
    @Override
    public FunCaseInfoDTO selectFunCaseInfoDTOById (Integer caseId){
        return  funCaseInfoDTOMapper.selectByPrimaryKey(caseId);
    }
    @Override
    public FunArchiveSFCDTO selectFunArchiveSFCDTOById (Integer sfcId){
        return  funArchiveSFCDTOMapper.selectByPrimaryKey(sfcId);
    }
    @Override
    public FunArchiveRecordsDTO selectFunArchiveRecordsDTOById (Integer recordId){
        return  funArchiveRecordsDTOMapper.selectByPrimaryKey(recordId);
    }
}