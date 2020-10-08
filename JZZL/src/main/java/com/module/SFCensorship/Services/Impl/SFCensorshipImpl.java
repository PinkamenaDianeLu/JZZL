package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.Converter.FunArchiveSeqMapper;
import com.bean.jzgl.Converter.FunPeopelCaseMapper;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunPeopelCase;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.mapper.jzgl.FunArchiveSeqDTOMapper;
import com.mapper.jzgl.FunArchiveTypeDTOMapper;
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
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;

    @Override
    public List<FunArchiveSeq> selectArchiveSeqPage(Map<String, Object> map) {
        return FunArchiveSeqMapper.INSTANCE.pcDTOToPcs(funArchiveSeqDTOMapper.selectArchiveSeqPage(map));
    }

    ;

    @Override
    public int selectArchiveSeqPageCount(Map<String, Object> map) {
        return funArchiveSeqDTOMapper.selectArchiveSeqPageCount(map);
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
    public FunPeopelCase getFunPeopelCaseById(Integer id) {
        return FunPeopelCaseMapper.INSTANCE.pcDTOToPc(funPeopelCaseDTOMapper.selectByPrimaryKey(id));
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
    public List<FunArchiveTypeDTO> selectArchiveTypeByJqSeq(Map<String, Object> map) {
        return funArchiveTypeDTOMapper.selectArchiveTypeByJqSeq(map);
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
