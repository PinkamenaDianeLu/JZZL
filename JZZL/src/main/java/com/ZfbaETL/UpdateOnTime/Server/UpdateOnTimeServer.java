package com.ZfbaETL.UpdateOnTime.Server;/**
 * @author Mrlu
 * @createTime 2021/3/24
 * @describe
 */

import com.bean.jzgl.DTO.*;
import com.bean.thkjdmtjz.JzFjzb;
import com.bean.zfba.*;
import com.mapper.jzgl.*;
import com.mapper.thkjdmtjz.JzFjzbMapper;
import com.mapper.zfba.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2021/3/24 9:03
 * @describe
 */
@Service
public class UpdateOnTimeServer {
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    XtDzqzbMapper xtDzqzbMapper;
    @Resource
    WjBjzMapper wjBjzMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    XtWjflbMapper xtWjflbMapper;
    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    SysRecordorderDTOMapper sysRecordorderDTOMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    XtXyrxxbMapper xtXyrxxbMapper;
    @Resource
    JzFjzbMapper jzFjzbMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    XtAjxxbMapper xtAjxxbMapper;

     /**
     * 查询应该盖章却没有盖章的文书
     * @author MrLu
     * @createTime  2021/3/24 9:07
     * @return  List<FunArchiveRecordsDTO>  |
      */
    public List<FunArchiveRecordsDTO> selectNoWhRecords() {
        return funArchiveRecordsDTOMapper.selectNoWhRecords();
    }

     /**
     * 查询某个文书是否盖章
     * @author MrLu
     * @param
     * @createTime  2021/3/24 9:39
     * @return    |
      */
    public  boolean selectSealByRecord(String wjbm,Integer wjbid){
        Map<String,Object> map=new HashMap<>();
        map.put("wjbm",wjbm);
        map.put("wjbid",wjbid);
        return xtDzqzbMapper.selectSealByRecord(map)>0;
    }
    /**
     * 查询文号
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/3/19 15:09
     */
    public Wh selectWhByBmBid(String wjbm, Integer wjbid) {
        Map<String, Object> map = new HashMap<>();
        map.put("wjbm", wjbm);
        map.put("id", wjbid);
        return wjBjzMapper.selectWhByBmBid(map);
    }

    /**
     * 根据文件表名表id更新文号
     * @author MrLu
     * @param
     * @createTime  2021/3/24 9:53
     * @return    |
     */
    public void updateRecordWhByWjMessage(String recordwh,String wjbm,Integer wjbid){
        Map<String, Object> map=new HashMap<>();
        map.put("recordwh",recordwh);
        map.put("wjbm",wjbm);
        map.put("wjbid",wjbid);
        funArchiveRecordsDTOMapper.updateRecordWhByWjMessage(map);
    };


    /**
     * 查询某单位一段时间之后新添加或有更新的文件
     * @author MrLu
     * @param ｛groupcode，lastpkdatevalue｝
     * @createTime  2021/3/24 10:46
     * @return    |
     */
    public List<WjBjz> selectNewFilesAfterDate(String recordwh, Date lastpkdatevalue){
        Map<String, Object> map=new HashMap<>();
        map.put("groupcode",recordwh);
        map.put("lastpkdatevalue",lastpkdatevalue);
     return    wjBjzMapper.selectNewFilesAfterDate(map);
    }
    /**
     * 删除文件By表卷准表id
     * @author MrLu
     * @param bjzid
     * @createTime  2021/3/24 14:30
     * @return    |
     */
    public void  deleteFilesByBjzid(Integer bjzid){
        funArchiveFilesDTOMapper.deleteFilesByBjzid(bjzid);
    }

     /**
     * 更新文件路径
     * @author MrLu
     * @param
     * @createTime  2021/3/24 15:20
     * @return    |
      */
    public void  updateFilesByBjzid(FunArchiveFilesDTO record){
        funArchiveFilesDTOMapper.updateFilesByBjzid(record);
    }

    public List<FunArchiveRecordsDTO> selectRecordsByWjMessageNotSend(String wjbm,Integer wjbid){
        Map<String, Object> map=new HashMap<>();
        map.put("wjbm",wjbm);
        map.put("wjbid",wjbid);
     return    funArchiveRecordsDTOMapper.selectRecordsByWjMessageNotSend(map);
    }
    public Integer selectFilecCountByRecordid(Integer recordid) {
        return funArchiveFilesDTOMapper.selectFilecCountByRecordid(recordid);
    }
    public void createFils(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.insert(record);
    }
    /**
     * 查询某单位某时间后新增的文书
     * @author MrLu
     * @param
     * @createTime  2021/3/25 10:14
     * @return    |
     */
    public List<XtWjflb> selectNewRecordsAfterDate(String jlrgajgjgdm, Date lastpkdatevalue){
        Map<String, Object> map=new HashMap<>();
        map.put("jlrgajgjgdm",jlrgajgjgdm);
        map.put("lastpkdatevalue",lastpkdatevalue);
        return xtWjflbMapper.selectNewRecordsAfterDate(map);
    }

    /**
     * 查询某些警情编号的文书
     * @author MrLu
     * @param
     * @createTime  2021/3/25 10:22
     * @return    |
     */
    public List<XtWjflb> selectNewRecordsForAjbh(String jqbhAry){
        return xtWjflbMapper.selectNewRecordsForAjbh(jqbhAry);
    }
    ;
    /**
     * 按照警情编号查询没有发送的seq
     * @author MrLu
     * @param jqbh 警情编号
     * @createTime  2021/3/25 11:37
     * @return    |
     */
    public List<FunArchiveSeqDTO> selectSeqByJqbh(String jqbh){
        return  funArchiveSeqDTOMapper.selectSeqByJqbh(jqbh);
    }
    /**
     * 按照送检卷类型和卷类型查询默认顺序
     *
     * @param recordcode 文书代码
     * @param archivetype     送检卷卷类型
     * @return List<SysRecordorderDTO>    |
     * @author MrLu
     * @createTime 2020/11/26 15:23
     */
    public SysRecordorderDTO selectRecordOrderByTypes(String recordcode,Integer archivetype){
        Map<String, Object> map=new HashMap<>();
        map.put("recordcode",recordcode);
        map.put("archivetype",archivetype);
     return    sysRecordorderDTOMapper.selectRecordOrderByTypes(map);
    }


    public FunArchiveRecordsDTO selectPriveRecord(int recordtype, int archivetype, int defaultorder, int archiveseqid,Integer suspectid) {
        Map<String, Integer> map = new HashMap<>();
        map.put("recordtype", recordtype);
        map.put("archivetype", archivetype);
        map.put("defaultorder", defaultorder);//suspectid
        map.put("archiveseqid", archiveseqid);
        if (null!=suspectid){
            map.put("suspectid", suspectid);
        }
        return funArchiveRecordsDTOMapper.selectPriveRecord(map);
    }


    /**
     * 删除文书 isdelete=2 未被送检的
     * @author MrLu
     * @param  wjbm
     * @param wjbid
     * @createTime  2021/3/25 14:17
     * @return    |
     */
    public  void  deleteRecordsByWjMessageNotSend(String wjbm,Integer wjbid){
        Map<String, Object> map=new HashMap<>();
        map.put("wjbm",wjbm);
        map.put("wjbid",wjbid);
        funArchiveRecordsDTOMapper.deleteRecordsByWjMessageNotSend(map);
    }

    /**
     * 根据嫌疑人编号查询嫌疑人
     * @author MrLu
     * @param suspectcode
     * @createTime  2021/3/25 14:28
     * @return    |
     */
    public FunSuspectDTO selectSuspectBySuspectcode(String suspectcode){
      return   funSuspectDTOMapper.selectSuspectBySuspectcode(suspectcode);
    }

    /**
     * 通过嫌疑人编号会查询嫌疑人信息
     * @author MrLu
     * @param
     * @createTime  2021/3/25 15:33
     * @return    |
     */
    public XtXyrxxb selectXyrByXyrbh(String xyrbh){
        return xtXyrxxbMapper.selectXyrByXyrbh(xyrbh);
    }

    /**
     * 查询seq下某个type
     * @author MrLu
     * @param archiveseqid
     * @param recordtype
     * @createTime  2021/3/25 14:55
     * @return    |
     */
    public FunArchiveTypeDTO  selectTypeBySeqType(Integer archiveseqid,Integer recordtype){
       return funArchiveTypeDTOMapper.selectTypeBySeqType(archiveseqid,recordtype);
    }
    public void updateOrderAdd(int archiveseqid, int archivetypeid, int thisorder) {
        funArchiveRecordsDTOMapper.updateOrderAdd(archiveseqid, archivetypeid, thisorder);
    }
    public void insertNewSuspect(FunSuspectDTO record) {
        funSuspectDTOMapper.insertSelective(record);
    }
    /**
     * 查询案件中嫌疑人的最大顺序
     * @author MrLu
     * @param caseinfoid
     * @createTime  2021/3/25 15:42
     * @return    |
     */
    public int selectMaxOrderByCaseid(Integer caseinfoid){
        return funSuspectDTOMapper.selectMaxOrderByCaseid(caseinfoid);
    }

    /**
     * 查询一个type中对嫌疑人文书的最大顺序（不针对某个嫌疑人）
     * @author MrLu
     * @param typeid
     * @createTime  2021/3/25 16:19
     * @return    |
     */
    public Integer selectRsMaxOrderByTypeid(int typeid){
        return funArchiveRecordsDTOMapper.selectRsMaxOrderByTypeid(typeid);
    }
    /**
     * 查询一个type中文书的最大顺序
     * @author MrLu
     * @param typeid
     * @createTime  2021/3/25 16:19
     * @return    |
     */
    public Integer selectMaxOrderByTypeid(int typeid){
        return funArchiveRecordsDTOMapper.selectMaxOrderByTypeid(typeid);
    }

    /**
     * 根据标准卷id查看该文书是否已经存在
     * @author MrLu
     * @param  bjzid
     * @createTime  2021/3/26 11:40
     * @return  int  |
     */
    public boolean selectFileCountByBjzid(Integer bjzid){
        return  funArchiveFilesDTOMapper.selectFileCountByBjzid(bjzid)>0;
    }

    /**
     * 查看更新的附件
     * @author MrLu
     * @param
     * @createTime  2021/6/17 14:47
     * @return    |
     */
    public List<JzFjzb> selectFjzbGtFxsj(Date gxsj){
        return  jzFjzbMapper.selectFjzbGtFxsj(gxsj);
    };

    public FunArchiveSeqDTO selectActiveSeqByJqbh(String jqbh){
        return funArchiveSeqDTOMapper.selectActiveSeqByJqbh(jqbh);
    };

    /**
     * 查询一个文书类型中的最大顺序数
     *
     * @param id 文书id
     * @return int   |
     * @author MrLu
     * @createTime 2020/11/5 15:02
     */
    public int selectRecordMaxOrder(int id){
        return funArchiveRecordsDTOMapper.selectRecordMaxOrder(id);
    };
    /**
     * 查询更新的案件
     * @param gxsj Date
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    public List<XtAjxxb> selectUpdateCase(Date gxsj){
        return  xtAjxxbMapper.selectUpdateCase(gxsj);
    }


    /**
     * 根据案件id查询案件信息
     * @param id Date
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:41
     */
    public FunCaseInfoDTO selectCaseInfoById(Integer id) {
        return funCaseInfoDTOMapper.selectByPrimaryKey(id);
    }
}
