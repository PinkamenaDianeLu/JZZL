package com.module.ArchiveManager.Services.Impl;

import com.bean.jzgl.DTO.*;
import com.mapper.jzgl.*;
import com.module.ArchiveManager.Services.RecordComovement;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/12/28 10:09
 * @describe
 */
@Service
public class RecordComovementImpl implements RecordComovement {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    SysRecordorderDTOMapper sysRecordorderDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper FunArchiveTypeDTOMapper;

    @Override
    public void subvolumeAddRecord(FunArchiveRecordsDTO priveReocrd, FunArchiveRecordsDTO newRecord) {
        //1.查询这个文书的基础卷。
        FunArchiveRecordsDTO basePrevId = funArchiveRecordsDTOMapper.selectByPrimaryKey(priveReocrd.getBaserecordid());
        int newRecordId = newRecord.getId();
        newRecord.setThisorder(basePrevId.getThisorder() + 1);//顺序+1
        newRecord.setArchiveseqid(basePrevId.getArchiveseqid());//seq
        newRecord.setArchivesfcid(basePrevId.getArchivesfcid());//sfc
        newRecord.setArchivetypeid(basePrevId.getArchivetypeid());//type
        //后面的顺序全部+1
        funArchiveRecordsDTOMapper.updateOrderAdd(basePrevId.getArchiveseqid(), basePrevId.getArchivetypeid(), basePrevId.getThisorder());
        funArchiveRecordsDTOMapper.insertSelective(newRecord);//新建文书到基础卷

        FunArchiveRecordsDTO updateRecord = new FunArchiveRecordsDTO();//新建基础卷后更新元卷的值
        updateRecord.setBaserecordid(newRecord.getId());
        updateRecord.setId(newRecordId);
        funArchiveRecordsDTOMapper.updateByPrimaryKeySelective(updateRecord);//更新

        //复制文书图片
        for (FunArchiveFilesDTO thisFile :
                funArchiveFilesDTOMapper.selectRecordFilesByRecordId(newRecordId, null)) {
            thisFile.setArchiverecordid(newRecord.getId());
            thisFile.setArchivetypeid(updateRecord.getArchivetypeid());
            thisFile.setArchivesfcid(updateRecord.getArchivesfcid());
            thisFile.setArchiveseqid(updateRecord.getArchiveseqid());
            funArchiveFilesDTOMapper.insert(thisFile);
        }

    }

    @Override
    public void addFile(FunArchiveRecordsDTO record, FunArchiveFilesDTO[] files) {
        //查看拥有相同
//List<SysRecordorderDTO> selectRecordByUuid(String recorduuid);
        List<FunArchiveRecordsDTO> sameRecord = funArchiveRecordsDTOMapper.selectRecordByUuid(record.getRecorduuid());
        //查询这些文书
        for (FunArchiveRecordsDTO thisRecord :
                sameRecord) {
            for (FunArchiveFilesDTO thisFile :
                    files) {
                thisFile.setArchiveseqid(thisRecord.getArchiveseqid());//seqid
                thisFile.setArchivesfcid(thisRecord.getArchivesfcid());
                thisFile.setArchivetypeid(thisRecord.getArchivetypeid());
                thisFile.setArchiverecordid(thisRecord.getId());
                funArchiveFilesDTOMapper.insert(thisFile);
            }
        }
    }

    @Override
    public void BaseArchiveDeleteRecord(FunArchiveRecordsDTO record) {
        FunArchiveRecordsDTO r = new FunArchiveRecordsDTO();
        r.setRecorduuid(record.getRecorduuid());
        r.setIsdelete(2);//所有文书删除
        funArchiveRecordsDTOMapper.updateRecordByUuid(r);
    }

    @Override
    public void baseArchiveDeleteFile(FunArchiveFilesDTO file) {
        //将该文件的文书同文书代码的标记为已有删除
        List<FunArchiveRecordsDTO> sameRecord = funArchiveRecordsDTOMapper.selectSameRecordById(file.getArchiverecordid());
        FunArchiveRecordsDTO r = new FunArchiveRecordsDTO();
        r.setRecorduuid(sameRecord.get(0).getRecorduuid());
        r.setIsdelete(1);//删除某个文书
        funArchiveRecordsDTOMapper.updateRecordByUuid(r);
        FunArchiveFilesDTO f = new FunArchiveFilesDTO();
        f.setIsdelete(1);
        f.setFilecode(f.getFilecode());
        funArchiveFilesDTOMapper.updateFileByFilecode(f);
    }

    @Override
    public void moveRecord(FunArchiveRecordsDTO recordA, FunArchiveRecordsDTO recordB) {
        //查询同样带有被移动的文书(B)文书代码的文书
        List<FunArchiveRecordsDTO> sameRecord = funArchiveRecordsDTOMapper.selectRecordByUuid(recordB.getRecorduuid());
        FunArchiveRecordsDTO r = new FunArchiveRecordsDTO();
        for (FunArchiveRecordsDTO thisRecordB :
                sameRecord) {

            //查询该文书
            FunArchiveRecordsDTO thisRecordA = funArchiveRecordsDTOMapper.selectRecordByUuidSeq(recordA.getRecorduuid(), thisRecordB.getArchiveseqid());
            r.setId(thisRecordB.getId());
      /*      if (null == thisRecordA) {
                //A B 同时存在
                int oriOrder = recordA.getThisorder();//B应该达到的顺序
                //A和A之后的顺序全部+1
                funArchiveRecordsDTOMapper.updateOrderAdd(thisRecordB.getArchiveseqid(), thisRecordB.getArchivetypeid(), oriOrder - 1);
                r.setThisorder(oriOrder);
                funArchiveRecordsDTOMapper.updateByPrimaryKeySelective(r);
            }  */
            if (null == thisRecordA) {
                //A 不存在  寻找离A最近的存在的文书
                FunArchiveSeqDTO thisSeq = funArchiveSeqDTOMapper.selectByPrimaryKey(recordB.getArchiveseqid());
                FunArchiveTypeDTO thisType = FunArchiveTypeDTOMapper.selectByPrimaryKey(recordB.getArchivetypeid());
                Map<String, Object> map1 = new HashMap<>();
                map1.put("recordcode", recordA.getRecordscode());
                map1.put("archivetype", thisSeq.getArchivetype());
                map1.put("recordtype", thisType.getRecordtype());
                SysRecordorderDTO recordAType = sysRecordorderDTOMapper.selectRecordOrderByTypes(map1);
                Map<String, Integer> map = new HashMap<>();
                map.put("recordtype", recordB.getArchivetypeid());
                map.put("archivetype", recordAType.getArchivetype());
                map.put("defaultorder", recordAType.getDefaultorder());
                thisRecordA = funArchiveRecordsDTOMapper.selectPriveRecord(map);

            }

            int oriOrder = thisRecordA.getThisorder();//B应该达到的顺序
            //A和A之后的顺序全部+1
            funArchiveRecordsDTOMapper.updateOrderAdd(thisRecordB.getArchiveseqid(), thisRecordB.getArchivetypeid(), oriOrder - 1);
            r.setThisorder(oriOrder);
            funArchiveRecordsDTOMapper.updateByPrimaryKeySelective(r);
        }

    }

    @Override
    public void moveFileOrder(int order, String fileCode) {
        //查询被挪动的文件的所有文书
        List<FunArchiveRecordsDTO> activeRecords = funArchiveRecordsDTOMapper.selectActiveRecordByFilecode(fileCode);
        //更新顺序
        for (FunArchiveRecordsDTO thisRecord :
                activeRecords) {
            Map<String, Object> map = new HashMap<>();
            map.put("archiverecordid", thisRecord.getId());
            map.put("thisorder", order - 1);
            map.put("filecode", fileCode);
            //该位置后面的顺序都+1
            funArchiveFilesDTOMapper.updateOrderByRecordId(map);
            //更新自己的顺序
            map.put("thisorder", order);
            funArchiveFilesDTOMapper.updateFileOrderByRecord(map);
        }

    }

    @Override
    public void renameRecord(String newName, String recordUuid) {

    }

    @Override
    public void renameFile(String newName, String fileCode) {
        List<FunArchiveRecordsDTO> activeRecords = funArchiveRecordsDTOMapper.selectActiveRecordByFilecode(fileCode);
        for (FunArchiveRecordsDTO thisRecord :
                activeRecords) {
            Map<String, Object> map = new HashMap<>();
            map.put("archiverecordid", thisRecord.getId());
            map.put("filename", newName);
            map.put("filecode", fileCode);
            //更新名称
            funArchiveFilesDTOMapper.updateFileOrderByRecord(map);
        }


    }
}
