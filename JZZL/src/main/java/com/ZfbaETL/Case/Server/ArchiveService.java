package com.ZfbaETL.Case.Server;

import com.bean.jzgl.DTO.*;
import com.bean.zfba.WjBjz;
import com.bean.zfba.WjWjdz;
import com.bean.zfba.XtWjflb;
import com.mapper.jzgl.*;
import com.mapper.zfba.WjBjzMapper;
import com.mapper.zfba.WjWjdzMapper;
import com.mapper.zfba.XtWjflbMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/5 16:21
 * @describe
 */
@Service
public class ArchiveService {
    @Resource
    XtWjflbMapper xtWjflbMapper;
    @Resource
    WjWjdzMapper wjWjdzMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    WjBjzMapper wjBjzMapper;

    /**
     * 查询嫌疑人的文书
     * @author MrLu
     * @param
     * @createTime  2021/1/5 16:18
     * @return    |
     */
    public List<XtWjflb> selectRecordBySuspect(String jqbh, String dydm){
        return  xtWjflbMapper.selectRecordBySuspect(jqbh,dydm);
    };
    /**
     * 查询警情编号下所有不对人的文书
     * @author MrLu
     * @param
     * @createTime  2021/1/5 17:16
     * @return    |
     */
    public List<XtWjflb>   selectRecordNoSuspect(String jqbh){
        return xtWjflbMapper.selectRecordNoSuspect(jqbh);
    }

    public  void  createNewSfc(FunArchiveSFCDTO record){
        funArchiveSFCDTOMapper.insertSelective(record);
    }
    public  void createNewSeq(FunArchiveSeqDTO record){
        funArchiveSeqDTOMapper.insertSelective(record);
    }
    public  void createNewType(FunArchiveTypeDTO record){
        funArchiveTypeDTOMapper.insertSelective(record);
    }
    public  void createNewRecord(FunArchiveRecordsDTO record){
        funArchiveRecordsDTOMapper.insertSelective(record);
    }
    public  void createNewSR(FunSuspectRecordDTO record){
        funSuspectRecordDTOMapper.insert(record);
    }
    public  void createFils(FunArchiveFilesDTO record){
        funArchiveFilesDTOMapper.insert(record);
    }


    /**
     * 根据文件表名表id查询文件的图片地址
     * @author MrLu
     * @param
     * @createTime  2021/1/6 10:54
     * @return    |
     */
   public List<WjWjdz> selectWjdzByBmBid (Integer wjbid, String wjbm){
        return  wjWjdzMapper.selectWjdzByBmBid(wjbid,wjbm);
    }
    //wjBjzMapper
    public List<WjBjz> selectWjdzByBmBid_Bjz (Integer wjbid, String wjbm){
        return  wjBjzMapper.selectWjdzByBmBid(wjbid,wjbm);
    }
}
