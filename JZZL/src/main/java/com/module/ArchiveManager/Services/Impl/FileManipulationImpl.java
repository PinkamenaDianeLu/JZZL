package com.module.ArchiveManager.Services.Impl;

import com.bean.jzgl.DTO.*;
import com.mapper.jzgl.*;
import com.module.ArchiveManager.Services.FileManipulationService;
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
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    FunArchiveBackcoverDTOMapper FunArchiveBackcoverDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;

    @Override
    public FunArchiveCoverDTO selectFunArchiveCoverDTOByFileId (Integer fileid){
        return  funArchiveCoverDTOMapper.selectFunArchiveCoverDTOByFileId(fileid);
    }

    @Override
    public FunArchiveBackcoverDTO selectFunArchiveBackCoverDTOByFileId(Integer fileid) {
        return FunArchiveBackcoverDTOMapper.selectFunArchiveBackCoverDTOByFileId(fileid);
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
    public List<FunArchiveRecordsDTO> selectFunArchiveRecordsByUUID(String recordUuid) {
        return funArchiveRecordsDTOMapper.selectFunArchiveRecordsByUUID(recordUuid);
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
    }

    @Override
    public void updateFunArchiveBackCoverById(FunArchiveBackcoverDTO FunArchiveBackcoverDTO) {
        FunArchiveBackcoverDTOMapper.updateByPrimaryKeySelective(FunArchiveBackcoverDTO);
    }

    ;
    @Override
    public void  insertFunArchiveCover(FunArchiveCoverDTO record){
        funArchiveCoverDTOMapper.insertSelective(record);
    }

    @Override
    public void insertFunArchiveBackCover(FunArchiveBackcoverDTO FunArchiveBackcoverDTO) {
        FunArchiveBackcoverDTOMapper.insertSelective(FunArchiveBackcoverDTO);
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

    @Override
    public void updateFunArchiveFileDTO(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void updateOrderByRecordId(int archiverecordid,int thisorder,String filecode) {
        Map<String,Object> map=new HashMap<>();
        map.put("archiverecordid",archiverecordid);
        map.put("thisorder",thisorder);
        map.put("filecode",filecode);
        funArchiveFilesDTOMapper.updateOrderByRecordId(map);
    }

    @Override
    public FunArchiveFilesDTO selectFilesByFileCode(int archiverecordid,String filecode) {
        Map<String,Object> map=new HashMap<>();
        map.put("archiverecordid",archiverecordid);
        map.put("filecode",filecode);
        return funArchiveFilesDTOMapper.selectFilesByFileCode(map);
    }

    @Override
    public List<FunSuspectDTO> selectSuspectByArchiveseqid(Integer archiveseqid) {
        return funSuspectDTOMapper.selectSuspectByArchiveseqid(archiveseqid);
    }

    @Override
    public Integer selectFilesCountByTypeid(Integer archivetypeid) {
        return funArchiveFilesDTOMapper.selectFilesCountByTypeid(archivetypeid);
    }

    @Override
    public Integer selectFilecCountByRecordid(Integer recordid) {
        return funArchiveFilesDTOMapper.selectFilecCountByRecordid(recordid);
    }

    @Override
    public FunArchiveSeqDTO selectBaseArchiveBySeqId(int seqid) {
        return funArchiveSeqDTOMapper.selectBaseArchiveBySeqId(seqid);
    }

    @Override
    public FunArchiveTypeDTO selectSameTypeWithSeq(Integer archiveseqid, Integer id) {
        return funArchiveTypeDTOMapper.selectSameTypeWithSeq(archiveseqid,id);
    }

    @Override
    public List<FunArchiveFilesDTO> selectFilesByCodeNotSend(String filecode) {
       return   funArchiveFilesDTOMapper.selectFilesByCodeNotSend(filecode);
    }

}
