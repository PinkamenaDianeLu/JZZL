package com.ZfbaETL.Case.Server;

import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysRecordMessage;
import com.bean.zfba.WjWjdz;
import com.bean.zfba.XtWjflb;
import com.mapper.jzgl.*;
import com.mapper.zfba.WjBjzMapper;
import com.mapper.zfba.WjWjdzMapper;
import com.mapper.zfba.XtWjflbMapper;
import org.apache.ibatis.annotations.Param;
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
    @Resource
    SysRecordMessageMapper sysRecordMessageMapper;


    /**
     * 查询嫌疑人的文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 16:18
     */
    public List<XtWjflb> selectRecordBySuspect(String jqbh, String dydm) {
        return xtWjflbMapper.selectRecordBySuspect(jqbh, dydm);
    }

    ;

    /**
     * 查询警情编号下所有不对人的文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 17:16
     */
    public List<XtWjflb> selectRecordNoSuspect(String jqbh) {
        return xtWjflbMapper.selectRecordNoSuspect(jqbh);
    }

    public void createNewSfc(FunArchiveSFCDTO record) {
        funArchiveSFCDTOMapper.insertSelective(record);
    }

    public void createNewSeq(FunArchiveSeqDTO record) {
        funArchiveSeqDTOMapper.insertSelective(record);
    }

    public void createNewType(FunArchiveTypeDTO record) {
        funArchiveTypeDTOMapper.insertSelective(record);
    }

    public void createNewRecord(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }

    public void createNewSR(FunSuspectRecordDTO record) {
        funSuspectRecordDTOMapper.insert(record);
    }



    /**
     * 根据文件表名表id查询文件的图片地址
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/6 10:54
     */
    public List<WjWjdz> selectWjdzByBmBid(Integer wjbid, String wjbm) {
        return wjWjdzMapper.selectWjdzByBmBid(wjbid, wjbm);
    }

    //wjBjzMapper


    /**
     * 查询该文书的检察院代码与字
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/3/19 14:28
     */
    public SysRecordMessage selectMessageByCode(String recordcode,Integer recordtype) {
        return sysRecordMessageMapper.selectMessageByCode(recordcode,recordtype);
    }


    /**
     * 根据文件表名表idseqid查询文书
     * @author MrLu
     * @param
     * @createTime  2021/6/24 17:11
     * @return    |
     */
    public FunArchiveRecordsDTO  selectRecordsByWjMessage(@Param("wjbm") String wjbm, @Param("wjbid") Integer wjbid, @Param("archiveseqid") Integer archiveseqid){
        return funArchiveRecordsDTOMapper.selectRecordsByWjMessage(wjbm,wjbid,archiveseqid);
    };


}
