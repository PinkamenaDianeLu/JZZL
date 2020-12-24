package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.DTO.*;
import com.mapper.jzgl.*;
import com.module.SFCensorship.Services.FileManipulationService;
import com.util.MapFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    FunArchiveRecordindexDTOMapper funArchiveRecordindexDTOMapper;
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
    @Override
    public List<FunArchiveFilesDTO> selectRecordFilesByFileCodes(String[] filesCode,int archiverecordid) {
        Map<String,Object> map=new HashMap<>();
        map.put("filecode",filesCode);
        map.put("archiverecordid",archiverecordid);
        return funArchiveFilesDTOMapper.selectRecordFilesByFileCodes(map);
    }
    @Override
    public void insertFunArchiveRecordindexDTO(FunArchiveRecordindexDTO record){
        funArchiveRecordindexDTOMapper.insertSelective(record);
    };

    @Override
    public void updateFunArchiveRecordindexDTO(FunArchiveRecordindexDTO record){
        funArchiveRecordindexDTOMapper.updateByPrimaryKeySelective(record);
    };
    @Override
    public FunArchiveFilesDTO selectFunArchiveFilesDTOById(Integer id){
        return funArchiveFilesDTOMapper.selectByPrimaryKey(id);
    }

    @Override
    public FunArchiveRecordindexDTO selectRecordIndexByTypeId(Integer archiveseqid, Integer archivetypeid) {
        return funArchiveRecordindexDTOMapper.selectRecordIndexByTypeId(archiveseqid,archivetypeid);
    }
    @Override
    public void  updateFunArchiveCoverById(FunArchiveCoverDTO record){
        funArchiveCoverDTOMapper.updateByPrimaryKeySelective(record);
    };
    @Override
    public void  insertFunArchiveCover(FunArchiveCoverDTO record){
        funArchiveCoverDTOMapper.insertSelective(record);
    }

    @Override
    public List<Object> selectArchiveRecordPage(Map<String, Object> map) throws Exception {
        return MapFactory.mapToListBean(funArchiveRecordsDTOMapper.selectArchiveRecordPage(map));
    }

    @Override
    public int selectArchiveRecordPageCount(Map<String, Object> map) {
        return funArchiveRecordsDTOMapper.selectArchiveRecordPageCount(map);
    }

    @Override
    public int selectFileMaxOrder(int archiverecordid) {
        return funArchiveFilesDTOMapper.selectFileMaxOrder(archiverecordid);
    }

    @Override
    public void updateFileByFileCode(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.updateFileBySeqIdFileCode(record);
    }

    @Override
    public void insertFunArchiveFilesDTO(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.insert(record);
    }

}
