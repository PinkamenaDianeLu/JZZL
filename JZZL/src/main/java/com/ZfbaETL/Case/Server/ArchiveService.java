package com.ZfbaETL.Case.Server;

import com.bean.jzgl.DTO.*;
import com.bean.zfba.XtWjflb;
import com.mapper.jzgl.*;
import com.mapper.zfba.*;
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
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    XtAjxxbMapper xtAjxxbMapper;
    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    XtWjflbMapper xtWjflbMapper;
    @Resource
    XtXyrxxbMapper xtXyrxxbMapper;
    @Resource
    XtBarbMapper xtBarbMapper;
    @Resource
    SysUserMapper zfbaSysUserMapper;
    @Resource
    SysUserDTOMapper jzglSysUserMapper;
    @Resource
    SysBadwbMapper sysBadwbMapper;

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
    };

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

    //funArchiveTypeDTOMapper
}
