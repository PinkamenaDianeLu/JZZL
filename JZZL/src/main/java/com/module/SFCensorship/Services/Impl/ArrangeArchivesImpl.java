package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveRecordsMapper;
import com.bean.jzgl.Converter.FunArchiveSeqMapper;
import com.bean.jzgl.Converter.FunArchiveTypeMapper;
import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunArchiveType;
import com.mapper.jzgl.FunArchiveFilesDTOMapper;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.mapper.jzgl.FunArchiveSeqDTOMapper;
import com.mapper.jzgl.FunArchiveTypeDTOMapper;
import com.module.SFCensorship.Services.ArrangeArchivesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/10/8 15:40
 * @describe 卷整理
 */
@Service
public class ArrangeArchivesImpl implements ArrangeArchivesService {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;

    @Override
    public List<FunArchiveType> selectArchiveTypeByJqSeq(int seqId) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archiveseqid", seqId);
        return FunArchiveTypeMapper.INSTANCE.pcDTOToPcs(funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(pMap));
    }

    @Override
    public List<FunArchiveRecords> selectRecordsByTypeid(int archivetypeid, int isDelete) {
        Map<String, Object> pMap = new HashMap<>();
        pMap.put("archivetypeid", archivetypeid);
        pMap.put("isdelete", isDelete);

        return FunArchiveRecordsMapper.INSTANCE.pcDTOToPcs(funArchiveRecordsDTOMapper.selectRecordsByTypeid(pMap));
    }

    @Override
    public FunArchiveSeq selectLastSeqBySfc(int sfcId) {
        return FunArchiveSeqMapper.INSTANCE.pcDTOToPc(funArchiveSeqDTOMapper.selectLastSeqBySfc(sfcId));
    }

    @Override
    public FunArchiveSeqDTO selectFunArchiveSeqById(Integer id) {
        return funArchiveSeqDTOMapper.selectByPrimaryKey(id);
    }

    ;

    @Override
    public FunArchiveRecordsDTO selectFunArchiveRecordsById(Integer id) {
        return funArchiveRecordsDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public void insertFunArchiveSeq(FunArchiveSeqDTO record) {
        funArchiveSeqDTOMapper.insertSelective(record);
    }

    @Override
    public void insertFunArchiveRecords(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }
    @Override
    public  void insertFunArchiveType(FunArchiveTypeDTO funArchiveTypeDTO){
        funArchiveTypeDTOMapper.insertSelective(funArchiveTypeDTO);
    }
    @Override
    public FunArchiveTypeDTO selectFunArchiveTypeById(Integer id){
        return  funArchiveTypeDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByRecordId(int archiverecordid) {
        return funArchiveFilesDTOMapper.selectRecordFilesByRecordId(archiverecordid);
    }

    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String[] filesCode) {
        return funArchiveFilesDTOMapper.selectRecordFilesByFileCodes(filesCode);
    }

    @Override
    public List<FunArchiveFilesDTO> selectFilesHistory(String filecode) {
        return funArchiveFilesDTOMapper.selectFilesHistory(filecode);
    }
}
