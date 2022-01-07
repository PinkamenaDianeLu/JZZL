package com.ZfbaETL.BaseServer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysRecordMessage;
import com.bean.thkjdmtjz.JzFjcb;
import com.bean.zfba.Wh;
import com.bean.zfba.WjBjz;
import com.bean.zfba.XtWjflb;
import com.enums.EnumSoft;
import com.mapper.jzgl.*;
import com.mapper.thkjdmtjz.JzFjcbMapper;
import com.mapper.zfba.WjBjzMapper;
import com.util.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author MrLu
 * @createTime 2021/1/4 14:24
 * @describe
 */
@Service
public class BaseServer {

    @Resource
    EtlTablelogDTOMapper etlTablelogDTOMapper;
    @Resource
    EtlLogsDTOMapper etlLogsDTOMapper;
    @Resource
    SysRecordMessageMapper sysRecordMessageMapper;
    @Resource
    WjBjzMapper wjBjzMapper;
    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    JzFjcbMapper jzFjcbMapper;

    /**
     * 查询某个值 （哎我不想取名了，自己明白就行了 d=====(￣▽￣*)b）
     *
     * @param tablename  表名
     * @param lastpkname 唯一标识值
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 14:38
     */
    public EtlTablelogDTO selectLastValue(String tablename, String lastpkname) {
        return etlTablelogDTOMapper.selectLastValue(StringUtils.upperCase(tablename), StringUtils.upperCase(lastpkname));
    }

    /**
     * 更新
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 15:53
     */
    public void updateLastValue(EtlTablelogDTO etlTablelogDTO) {
        etlTablelogDTOMapper.updateByPrimaryKeySelective(etlTablelogDTO);
    }

    /**
     * 插入成功日志
     *
     * @param record
     * @param ts     成功条数
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 15:43
     */
    public void insertSuccessLog(EtlLogsDTO record, Integer ts) {
        //成功
        record.setIssuccess(0);
        if (ts > 0) {
            record.setMessage("插入成功，更新" + ts + "条数据！");
        } else {
            record.setMessage("没有可更新的值");
        }

        record.setEndtime(new Date());
        etlLogsDTOMapper.insert(record);
    }

    /**
     * 插入失败日志
     *
     * @param record
     * @param error          错误信息  前1998位
     * @param lastpkstrvalue 源数据报错的key
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 15:43
     */
    public void insertErrorLog(EtlLogsDTO record, String error, String lastpkstrvalue) {
        //失败
        record.setIssuccess(1);
        if (error.length() > 1998) {
            error = error.substring(0, 1998);
        }
        record.setMessage(error);
        record.setLastpkstrvalue(lastpkstrvalue);
        etlLogsDTOMapper.insert(record);
    }

    /**
     * 查询该文书的检察院代码与字
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/3/19 14:28
     */
    public SysRecordMessage selectMessageByCode(String recordcode,Integer archivetype) {
        return sysRecordMessageMapper.selectMessageByCode(recordcode,archivetype);
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
     * 查询该文书的详细信息并将其转换成JSONObject
     *
     * @param wjbm
     * @param wjbid
     * @return JSONObject  |
     * @author MrLu
     * @createTime 2021/3/19 15:14
     */
    public JSONObject selectObjectByBmBid(String wjbm, Integer wjbid) {
        Map<String, Object> pamMap = new HashMap<>();
        pamMap.put("wjbm", wjbm);
        pamMap.put("id", wjbid);
        Map<String, Object> rm = wjBjzMapper.selectObjectByBmBid(pamMap);
        if (null != rm) {
            return JSONObject.parseObject(JSON.toJSONString(rm));
        } else {
            return null;
        }

    }

    /**
     * 为文书添加文号、检察院文书代码
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/3/25 14:59
     */
    public void recordSetWhJcycode(FunArchiveRecordsDTO newRecord, String wjdm, String wjbm, Integer wjbid) {
        //查询该文书信息
        SysRecordMessage recordMessage = selectMessageByCode(wjdm,null);
        if (null == recordMessage) {
            //该文书没有查询到信息
            newRecord.setJcyrecordcode("");
            newRecord.setRecordwh("");
        } else {

            newRecord.setJcyrecordcode(recordMessage.getJcycode());
            //判断该文书不为附件  因为附件没有字，也判断不了特殊情况 所以只处理文书
            if (!wjbm.equals("XT_FJZB")) {
                //为文书添加文号
                if (StringUtils.isNotEmpty(recordMessage.getZ())) { //判断该文书是否有文号
                    //没有文号的文书   大多为附件 清单 通知书之类的  一般文书这里都是有文号的
                    //查询文号
                    Wh w = selectWhByBmBid(wjbm, wjbid);
                    if (null != w && StringUtils.isNotEmpty(w.getH())) {//号如果为空说明该文书还没有盖章，所以就不带有文号
                        String z = w.getG() + "公（" + recordMessage.getZ() + "）字〔" + w.getN() + "〕" + w.getH() + "号";
                        newRecord.setRecordwh(z);
                    }
                }
                //有些文书需要手动判断
                //目前已知的XS_BGJYQXTZS XS_XWTZS XS_WCNRFDDLRDCTZS 只能手动判断  判断条件在SysRecordMessage表中的different字段
                if (StringUtils.isNotEmpty(recordMessage.getDifference())) {
                    JSONObject thisWs = selectObjectByBmBid(wjbm, wjbid);//查到文书的详细信息
                    if (null != thisWs) {
                        if (wjbm.equals("XS_BGJYQXTZS")) {
                            //变更羁押期限通知书
                            if (null == thisWs.getString("QZCSLX")) {
                                newRecord.setJcyrecordcode("11033");
                                newRecord.setRecordscode("AS026");
                            } else if (thisWs.getString("QZCSLX").indexOf("拘留") > 0) {
                                newRecord.setJcyrecordcode("11033");
                                newRecord.setRecordscode("AS026");
                            } else if (thisWs.getString("QZCSLX").indexOf("逮捕") > 0) {
                                newRecord.setJcyrecordcode("");//2021年3月22日 10:57:51  该代码还没发明
                                newRecord.setRecordscode("AS027");
                            }
                            //判断不了拘留还是逮捕还是什么玩应就没办法
                        } else if (wjbm.equals("XS_XWTZS")) {
                            //询问通知书
                            if (thisWs.getInteger("ZRBHRXZ") == 0) {
                                //证人询问通知书
                                newRecord.setJcyrecordcode("10025");
                                newRecord.setRecordscode("AS060");
                            } else if (thisWs.getInteger("ZRBHRXZ") == 1) {
                                //被害人
                                newRecord.setJcyrecordcode("10031");
                                newRecord.setRecordscode("AS058");
                            }

                        } else if (wjbm.equals("XS_WCNRFDDLRDCTZS")) {
                            //未成年人法定代理人到场通知书
                            if (null == thisWs.getString("ZRBHRXZ")) {
                                //为null则为嫌疑人
                                newRecord.setJcyrecordcode("10032");
                                newRecord.setRecordscode("AS059");
                            } else if (thisWs.getString("ZRBHRXZ").equals("证人")) {
                                //未成年证人法定代理人到场通知书
                                newRecord.setJcyrecordcode("10026");
                                newRecord.setRecordscode("AS068");
                            } else if (thisWs.getString("ZRBHRXZ").equals("被害人")) {
                                //未成年被害人法定代理人到场通知书
                                newRecord.setJcyrecordcode("10018");
                                newRecord.setRecordscode("AS069");
                            }
                        }
                    }
                }

            }

        }
    }


    /**
     * 新建文书  不对人的
     *
     * @param newType type
     * @param order   文书顺序
     * @param thisR   案宗的文件分类表
     * @return |
     * @author MrLu
     * @createTime 2021/3/25 15:04
     */
    public void createRecordsNoSuspect(FunArchiveTypeDTO newType, Integer order, XtWjflb thisR) throws Exception {
        FunArchiveRecordsDTO newRecord = new FunArchiveRecordsDTO();
        newRecord.setJqbh(newType.getJqbh());
        newRecord.setAjbh(newType.getAjbh());
        newRecord.setThisorder(order<0?1:order);
        newRecord.setRecordname(thisR.getWjzw());
        newRecord.setArchivetypeid(newType.getId());
        newRecord.setRecordstyle(EnumSoft.recordstyle.DEWS.getValue());
        newRecord.setArchiveseqid(newType.getArchiveseqid());
        newRecord.setRecordscode(thisR.getWjdm());//文件代码
        newRecord.setIsdelete(0);//是否已删除
        newRecord.setIsazxt(0);//"系统抽取"
        newRecord.setArchivesfcid(newType.getArchivesfcid());

        if(StringUtils.isBlank(thisR.getJlrxm())){
            newRecord.setAuthor("系统抽取");
        }else{
            newRecord.setAuthor(thisR.getJlrxm());
        }

        if(StringUtils.isBlank(thisR.getJlrgmsfhm())){
            newRecord.setAuthoridcard("系统抽取");
        }else{
            newRecord.setAuthoridcard(thisR.getJlrgmsfhm());
        }
//        newRecord.setAuthor(thisR.getJlrxm());
//        newRecord.setAuthoridcard(thisR.getJlrgmsfhm());
        newRecord.setAuthorid(0);//作者id
        newRecord.setPrevid(0);
        newRecord.setJcyrecordcode("");//检察院文书代码
        //查询文号！、检察院代码
        recordSetWhJcycode(newRecord, thisR.getWjdm(), thisR.getWjbm().toUpperCase(), thisR.getWjbid());
        //查询对应的文书
        newRecord.setEffectivetime(thisR.getJlsj());
        newRecord.setBaserecordid(0);
        newRecord.setArchivecode(thisR.getWjdm());
        newRecord.setRecorduuid(UUID.randomUUID().toString());
        newRecord.setWjbm(thisR.getWjbm());//文件表名
        newRecord.setWjbid(thisR.getWjbid());//文件表id
        createNewRecord(newRecord);
        if (!"thkjdmtjz".equals(thisR.getWjbm())) {
            createFiles(newRecord);
        } else {
            createFiles_dmtjz(newRecord);
        }
        //查询是文件是否是受案登记表
        if ("XT005".equals(thisR.getWjdm())) {
            // caseServer.selectSahzBool(thisR.getJqbh(),thisR.getId())
            //查询对应的受案回执
            //判断是否是受案回执
            newRecord.setWjbm("XT_SAHZ");
            newRecord.setRecordscode("XT013");//文件代码  该文件代码为卷整理自创
            newRecord.setRecordname("受案回执");
            createNewRecord(newRecord);//再复制创建一个受案回执
            if (!"thkjdmtjz".equals(thisR.getWjbm())) {
                createFiles(newRecord);
            } else {
                createFiles_dmtjz(newRecord);
            }


        }
    }

    /**
     * 新建文书 对人的
     *
     * @param newType     type
     * @param newSuspect  嫌疑人
     * @param newCaseInfo 案件表
     * @param order       文书顺序
     * @param thisR       案宗的文件分类表
     * @return |
     * @author MrLu
     * @createTime 2021/3/25 16:02
     */
    public void createRecordsSuspect(FunArchiveTypeDTO newType,
                                     FunSuspectDTO newSuspect,
                                     FunCaseInfoDTO newCaseInfo, Integer order, XtWjflb thisR) throws Exception {
        //查询对人的

        FunArchiveRecordsDTO newRecord = new FunArchiveRecordsDTO();
        newRecord.setJqbh(newType.getJqbh());
        newRecord.setAjbh(newType.getAjbh());
        newRecord.setThisorder(order<0?1:order);
        newRecord.setRecordname(thisR.getWjzw());
        newRecord.setArchivetypeid(newType.getId());
        newRecord.setArchivecode(thisR.getWjdm());
        newRecord.setRecordstyle(EnumSoft.recordstyle.DRWS.getValue());
        newRecord.setArchiveseqid(newType.getArchiveseqid());
        newRecord.setRecordscode(thisR.getWjdm());
        newRecord.setIsdelete(0);
        newRecord.setIsazxt(0);//"系统抽取"
        newRecord.setArchivesfcid(newType.getArchivesfcid());

        if(StringUtils.isBlank(thisR.getJlrxm())){
            newRecord.setAuthor("系统抽取");
        }else{
            newRecord.setAuthor(thisR.getJlrxm());
        }

        if(StringUtils.isBlank(thisR.getJlrgmsfhm())){
            newRecord.setAuthoridcard("系统抽取");
        }else{
            newRecord.setAuthoridcard(thisR.getJlrgmsfhm());
        }

        newRecord.setAuthorid(0);
        newRecord.setPrevid(0);
        //查询文号！、检察院代码
        recordSetWhJcycode(newRecord, thisR.getWjdm(), thisR.getWjbm().toUpperCase(), thisR.getWjbid());
        newRecord.setEffectivetime(thisR.getJlsj());
        newRecord.setBaserecordid(0);
        newRecord.setRecorduuid(UUID.randomUUID().toString());
        newRecord.setWjbm(thisR.getWjbm());//文件表名
        newRecord.setWjbid(thisR.getWjbid());//文件表id
        createNewRecord(newRecord);
        //插入嫌疑人文书关系表
        FunSuspectRecordDTO newSR = new FunSuspectRecordDTO();
        newSR.setJqbh(newRecord.getJqbh());
        newSR.setCaseinfoid(newCaseInfo.getId());
        newSR.setAjbh(newRecord.getAjbh());
        newSR.setSfcnumber(newCaseInfo.getSfcnumber());
        newSR.setSuspectid(newSuspect.getId());
        newSR.setRecordid(newRecord.getId());
        newSR.setRecordtype(newType.getRecordtype());//文书类型
        newSR.setArchiveseqid(newRecord.getArchiveseqid());
        createNewSR(newSR);
        if (!"thkjdmtjz".equals(thisR.getWjbm())) {
            createFiles(newRecord);
        } else {
            createFiles_dmtjz(newRecord);
        }


    }

    /**
     * 抽取文书的文件
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/6 10:16
     */
    public void createFiles(FunArchiveRecordsDTO newRecord) throws Exception {
        String wjbm = newRecord.getWjbm();
        if ("XT013".equals(newRecord.getRecordscode())) {
            //xt013是受案回执 但是在案宗中表现为sadjb
            wjbm = "XT_SADJB";
        }
        List<WjBjz> wjdzs = selectWjdzByBmBid_Bjz(newRecord.getWjbid(), wjbm);
        int i = 0;
        for (WjBjz thisWjdz :
                wjdzs) {
            FunArchiveFilesDTO newFile = new FunArchiveFilesDTO();
            newFile.setJqbh(newRecord.getJqbh());
            newFile.setAjbh(newRecord.getAjbh());
            if (null != thisWjdz.getXh()) {
                i = thisWjdz.getXh() + i;
                newFile.setThisorder(i);
            } else {
                newFile.setThisorder(i++);
            }

            //是受案回执就不要第一页  第一页是受案登记表
            if ("XT013".equals(newRecord.getRecordscode()) && thisWjdz.getWjdz().endsWith("0.jpg")) {
                continue;
            } else if ("XT005".equals(newRecord.getRecordscode()) && thisWjdz.getWjdz().endsWith("1.jpg")) {
                //是受案登记表就不要第二页  第二页是受案回执
                continue;
            }

            newFile.setBjzid(thisWjdz.getId());
            if("XT_FJZB".equals(thisWjdz.getWjbm())){
                newFile.setFiletype(4);//附件
            }else {
                newFile.setFiletype(0);//标准文书
            }
            newFile.setArchiverecordid(newRecord.getId());
            newFile.setArchivetypeid(newRecord.getArchivetypeid());
            newFile.setFileurl(thisWjdz.getWjdz());
            newFile.setOriginurl(thisWjdz.getWjdz());
            newFile.setIsdowland(0);
            newFile.setFilename(thisWjdz.getWjzw());
            newFile.setArchiveseqid(newRecord.getArchiveseqid());
            newFile.setArchivesfcid(newRecord.getArchivesfcid());
            newFile.setIsazxt(0);
            newFile.setAuthor("系统抽取");
            newFile.setAuthorid(0);
            newFile.setIsshow(0);
            newFile.setFilecode(UUID.randomUUID().toString());
            newFile.setIsdelete(0);

            String httpHead="http://35.0.11.40";
            String version = GlobalUtil.getGlobal("version");//查询版本
             if ("provinceTest".equals(version)) {
                //省厅测试版本
                httpHead="http://35.0.40.36:8081";
            }

            if ("1".equals(thisWjdz.getWsgl())) {
                newFile.setServerip(httpHead+"/WordToImage/");
            } else {
                //if ("3".equals(thisWjdz.getWsgl()))
                //设置用于判断是否是附件的关键词
                String[] wsfj_directory_yuejuan = {"wsfjNew", "wsfj", "wpfjNew", "wpfj"};
                for (String thisWDY :
                        wsfj_directory_yuejuan) {
                    //判断是否有这些关键词
                    if (thisWjdz.getWjdz().indexOf(thisWDY) > -1) {
                        //yes

                        if ("provinceTest".equals(version)) {
                            //省厅测试版本
                            newFile.setServerip(httpHead+":8080/fjupload");
                        }else{
                            newFile.setServerip(httpHead+":8080/fjupload");
                        }
                        break;
                    } else {
                        //no   //这个是上传的word转换的
                        newFile.setServerip(httpHead+"/UploadFileToImage");
                    }
                }
            }
            createFils(newFile);
        }
    }

    /**
     * 多媒体卷宗对应的文件创建
     *
     * @param newRecord FunArchiveRecordsDTO
     * @return |
     * @author MrLu
     * @createTime 2021/6/18 9:47
     */
    public void createFiles_dmtjz(FunArchiveRecordsDTO newRecord) throws Exception {
        String wjbm = newRecord.getWjbm();
        if ("XT013".equals(newRecord.getRecordscode())) {
            //xt013是受案回执 但是在案宗中表现为sadjb
            wjbm = "XT_SADJB";
        }
        List<JzFjcb> fjcmList = jzFjcbMapper.selectFjcbByzbid(newRecord.getWjbid());
        int i = 0;
        for (JzFjcb thisFjcm: fjcmList) {
            FunArchiveFilesDTO newFile = new FunArchiveFilesDTO();
            newFile.setJqbh(newRecord.getJqbh());
            newFile.setAjbh(newRecord.getAjbh());
            newFile.setThisorder(i++);
            //是受案回执就不要第一页  第一页是受案登记表
            if ("XT013".equals(newRecord.getRecordscode()) && thisFjcm.getWjdz().endsWith("0.jpg")) {
                continue;
            } else if ("XT005".equals(newRecord.getRecordscode()) && thisFjcm.getWjdz().endsWith("1.jpg")) {
                //是受案登记表就不要第二页  第二页是受案回执
                continue;
            }

            newFile.setBjzid(thisFjcm.getId());
            newFile.setFiletype(4);//来的都算附件
            newFile.setArchiverecordid(newRecord.getId());
            newFile.setArchivetypeid(newRecord.getArchivetypeid());
            newFile.setFileurl(thisFjcm.getWjdz());
            newFile.setOriginurl(thisFjcm.getWjdz());
            newFile.setIsdowland(0);
            newFile.setFilename(thisFjcm.getWjzw());
            newFile.setArchiveseqid(newRecord.getArchiveseqid());
            newFile.setArchivesfcid(newRecord.getArchivesfcid());
            newFile.setIsazxt(0);
            newFile.setAuthor("系统抽取");
            newFile.setAuthorid(0);
            newFile.setIsshow(0);
            newFile.setFilecode(UUID.randomUUID().toString());
            newFile.setIsdelete(0);
            newFile.setServerip("http://35.108.7.18:8099/dqZfqlcFile/");
   /*         if ("1".equals(thisWjdz.getWsgl())) {
                newFile.setServerip("http://35.0.11.40/WordToImage/");
            } else {
                //if ("3".equals(thisWjdz.getWsgl()))

                //设置用于判断是否是附件的关键词
                String[] wsfj_directory_yuejuan = {"wsfjNew", "wsfj", "wpfjNew", "wpfj"};
                for (String thisWDY :
                        wsfj_directory_yuejuan) {
                    //判断是否有这些关键词
                    if (thisFjcm.getWjdz().contains(thisWDY)) {
                        //yes
                        newFile.setServerip("http://35.0.11.40:8080/fjupload");
                        break;
                    } else {
                        //no   //这个是上传的word转换的
                        newFile.setServerip("http://35.0.11.40/UploadFileToImage");
                    }
                }
            }*/
            createFils(newFile);
        }
    }

    /**
     * 根据文件表名表id查询文件的图片地址
     *
     * @param wjbid
     * @param wjbm
     * @return |
     * @author MrLu
     * @createTime 2021/3/25 14:48
     */
    public List<WjBjz> selectWjdzByBmBid_Bjz(Integer wjbid, String wjbm) {
        return wjBjzMapper.selectWjdzByBmBid(wjbid, wjbm);
    }

    //这个方法不会有人需要注释吧  不会吧不会吧
    public void createFils(FunArchiveFilesDTO record) {
        funArchiveFilesDTOMapper.insert(record);
    }

    public void createNewRecord(FunArchiveRecordsDTO record) {
        funArchiveRecordsDTOMapper.insertSelective(record);
    }

    public void createNewSR(FunSuspectRecordDTO record) {
        funSuspectRecordDTOMapper.insert(record);
    }

}
