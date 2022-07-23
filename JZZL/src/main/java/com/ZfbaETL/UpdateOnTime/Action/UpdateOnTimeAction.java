package com.ZfbaETL.UpdateOnTime.Action;/**
 * @author Mrlu
 * @createTime 2021/3/24
 * @describe
 */

import com.ZfbaETL.BaseServer.BaseServer;
import com.ZfbaETL.UpdateOnTime.Server.UpdateOnTimeServer;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysRecordMessage;
import com.bean.thkjdmtjz.JzFjzb;
import com.bean.zfba.Converter.XtWjflbMapper;
import com.bean.zfba.*;
import com.util.GlobalUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author MrLu
 * @createTime 2021/3/24 9:02
 * @describe 实时更新
 */
@Configuration
@EnableScheduling
public class UpdateOnTimeAction implements CommandLineRunner {
    @Autowired
    private BaseServer baseServer;
    @Autowired
    private UpdateOnTimeServer updateOnTimeServer;

    @Override
    public void run(String... args) throws Exception {
//        try {
//            updateOnTime();
//        }catch (Exception e){
//            System.out.println("实时更新出现错误");
//            e.printStackTrace();
//        }

//        dmtjzUpload();
    }

  //  @Scheduled(fixedRate=1000000)//每50分钟
    public void updateOnTime() throws Exception {
        if ("1".equals(GlobalUtil.getGlobal("startEtl"))){
            return;
        }
        String groupcode = GlobalUtil.getGlobal("groupcode");//查询的单位代码
        try {
          //  updateSealRecord();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            updateRecords(groupcode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //thkjdmtjz
        try {
           // updateFiles(groupcode);
        } catch (Exception e) {
            e.printStackTrace();
        }

      /*  try {
            dmtjzUpload();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    //1.实时更新盖章问题
    public void updateSealRecord() {
        //查找应盖章却还没盖章的文书
        List<FunArchiveRecordsDTO> records = updateOnTimeServer.selectNoWhRecords();
        EtlLogsDTO etlLog = new EtlLogsDTO();
        etlLog.setSystemname("JZZL");
        etlLog.setTablename("FUN_ARCHIVE_RECORDS");
        etlLog.setStarttime(new Date());
        etlLog.setLastpkname("RECORDWH");
        for (FunArchiveRecordsDTO thisRecord :
                records) {
            String wjbm = thisRecord.getWjbm();
            Integer wjbid = thisRecord.getWjbid();
            //这些文书为什么没盖章呢？
            //查查有没有新的盖章记录
            if (updateOnTimeServer.selectSealByRecord(wjbm, wjbid)) {
                //有  补充盖章的信息
                //查询文号
                Wh w = updateOnTimeServer.selectWhByBmBid(wjbm, wjbid);
                SysRecordMessage recordMessage = baseServer.selectMessageByCode(thisRecord.getRecordscode(),null);
                if (null != w && StringUtils.isNotEmpty(w.getH())) {//号如果为空说明该文书还没有盖章，所以就不带有文号
                    String z = w.getG() + "公（" + recordMessage.getZ() + "）字〔" + w.getN() + "〕" + w.getH() + "号";
                    //更新！
                    updateOnTimeServer.updateRecordWhByWjMessage(z, wjbm, wjbid);
                    etlLog.setLastpkstrvalue(z + "");//写日志
                }
            }
        }

    }

    //2.新建/删除文书
    public void updateRecords(String groupcode) throws Exception {
        EtlTablelogDTO lastV = baseServer.selectLastValue("XT_WJFLB", "GXSJ");
        List<XtWjflb> newRecords = updateOnTimeServer.selectNewRecordsAfterDate(groupcode, lastV.getLastpkdatevalue());//新建的/有修改的文书
        EtlLogsDTO etlLog = new EtlLogsDTO();
        etlLog.setSystemname(lastV.getSystemname());
        etlLog.setTablename(lastV.getTablename());
        etlLog.setStarttime(new Date());
        etlLog.setLastpkname(lastV.getLastpkname());
        int updateRows = 0;
        for (XtWjflb thisRecord :
                newRecords) {
            String wjbm = thisRecord.getWjbm();
            Integer wjbid = thisRecord.getWjbid();
            Date updateTime= Optional.ofNullable(thisRecord.getGxsj()).orElse(new Date());

            if (thisRecord.getScbj() != 0) {
                //删除文书 (被删除的文书会在回收站)
                updateOnTimeServer.deleteRecordsByWjMessageNotSend(wjbm, wjbid);
                updateRows++;
                etlLog.setLastpkstrvalue(wjbm + "," + wjbid);//写日志
            } else {
                //新增
                //1.查询fun_archive_records中文件表名表id有没有  没有新加  有就不用管了
                List<FunArchiveRecordsDTO> records = updateOnTimeServer.selectRecordsByWjMessageNotSend(wjbm, wjbid);
                if (records.size() == 0) {
                    //没有以其它途径出现过  新增进去
                    List<FunArchiveSeqDTO> seqs = updateOnTimeServer.selectSeqByJqbh(thisRecord.getJqbh());
                    //这些是需要添加新文书的seq
                    //查新新的这个文书应该出现的位置
                    for (FunArchiveSeqDTO thisSeq :
                            seqs) {
                        //查询该文书应该出现的位置
                        SysRecordorderDTO thisRecordOrder = updateOnTimeServer.selectRecordOrderByTypes(thisRecord.getWjdm(), thisSeq.getArchivetype());
                        if (null == thisRecordOrder) {
                            //在需要的配置中没有这个文书 说明它是不被需要的存在
                            baseServer.insertErrorLog(etlLog, "该文书代码不存在与SYS_RECORDORDER中", wjbm + "," + wjbid);
                            continue;
                        }
                        //查询对应位置的type
                        FunArchiveTypeDTO type = updateOnTimeServer.selectTypeBySeqType(thisSeq.getId(), thisRecordOrder.getRecordtype());
                        //制作一个空的案件对象以供保存文书方法用
                        FunCaseInfoDTO FunCaseInfoDTO = new FunCaseInfoDTO();
                        FunCaseInfoDTO.setId(thisSeq.getCaseinfoid());
                        FunCaseInfoDTO.setSfcnumber(thisSeq.getSfcnumber());
                        //判断文书是否对人
                        if ((thisRecordOrder.getRecordstyle() == 1||thisRecordOrder.getRecordstyle() == 7||thisRecordOrder.getRecordstyle() == 9) && StringUtils.isNotEmpty(thisRecord.getDydm())) {
                            //该文书对人
                            //查看嫌疑人是否存在
                            String[] suspectAry = thisRecord.getDydm().split(",");//查看该文书是否有对应的嫌疑人
                            for (String xyrbh :
                                    suspectAry) {
                                FunSuspectDTO suspect = updateOnTimeServer.selectSuspectBySuspectcode(xyrbh);
                                if (null != suspect) {
                                    //这个嫌疑人是已存在的  添加关系和文书
                                    //文书
                                    FunArchiveRecordsDTO newRecordObj = updateOnTimeServer.selectPriveRecord(thisRecordOrder.getRecordtype(),
                                            thisRecordOrder.getArchivetype(),
                                            thisRecordOrder.getDefaultorder(),
                                            thisSeq.getId(),
                                            suspect.getId());
                                    if (null == newRecordObj) {
                                        //这种现象有记录出现在选人文书中
                                        //没有找到这个文书应该出现的文书的位置  那么就查找该嫌疑人的最后一个位置


//                                        baseServer.insertErrorLog(etlLog, "该文书无法找到对应的上一个文书（选人）", wjbm + "," + wjbid);
//                                        continue;
                                        Integer  order = Optional.of(updateOnTimeServer.selectRsMaxOrderByTypeid(type.getId()) + 1).orElse(3);
                                        baseServer.createRecordsSuspect(type, suspect, FunCaseInfoDTO, order + 1, thisRecord);

                                    }else {
                                        baseServer.createRecordsSuspect(type, suspect, FunCaseInfoDTO, newRecordObj.getThisorder() + 1, thisRecord);

                                    }
                                    updateRows++;
                                    lastV.setLastpkdatevalue(updateTime);
                                    baseServer.updateLastValue(lastV);

                                } else {
                                    //这个嫌疑人不存在  添加嫌疑人、关系、文书
                                    //查找嫌疑人
                                    XtXyrxxb xyr = updateOnTimeServer.selectXyrByXyrbh(xyrbh);
                                    Integer order = 1;
                                    //新建嫌疑人   理论上这步只会执行一次  因为在这里新建嫌疑人后  再次查询对应嫌疑人编号就应该有这个人了
                                    if (null != xyr) {
                                        order = Optional.of(updateOnTimeServer.selectRsMaxOrderByTypeid(type.getId()) + 1).orElse(3);
                                        ;//准备出该插入那里的顺序
                                        FunSuspectDTO newSuspectDTO = new FunSuspectDTO();
                                        newSuspectDTO.setSuspectidcard(xyr.getZjhm());
                                        newSuspectDTO.setSuspectname(xyr.getXm());
                                        newSuspectDTO.setJqbh(thisSeq.getJqbh());
                                        newSuspectDTO.setAjbh(thisSeq.getAjbh());
                                        newSuspectDTO.setCaseinfoid(thisSeq.getCaseinfoid());
                                        newSuspectDTO.setSfcnumber(thisSeq.getSfcnumber());
                                        newSuspectDTO.setCasetype(1);//我也不知道我为什么非得要案件类型  但是一直到最后也没用上 那么就写死算了
                                        newSuspectDTO.setSuspectstate(xyr.getQzcszt());
                                        newSuspectDTO.setDefaultorder(updateOnTimeServer.selectMaxOrderByCaseid(thisSeq.getCaseinfoid()) + 1);
                                        newSuspectDTO.setSuspectcode(xyrbh);
                                        updateOnTimeServer.insertNewSuspect(newSuspectDTO);
                                        baseServer.createRecordsSuspect(type, newSuspectDTO, FunCaseInfoDTO, order, thisRecord);
                                    } else {
                                        //特殊情况 发现找不到对应的嫌疑人了
                                        updateRows++;
                                        baseServer.createRecordsNoSuspect(type, order, thisRecord);

                                    }
                                    updateOnTimeServer.updateOrderAdd(type.getArchiveseqid(),
                                            type.getId()
                                            , order);
                                    lastV.setLastpkdatevalue(updateTime);
                                    baseServer.updateLastValue(lastV);

                                }
                            }
                        } else {
                            //该文书不对人  查找该文书的应在位置
                            //查找该文书应有顺序的上个文书的顺序  肯定会找到一个  因为最上面肯定有一个封皮和目录
                            FunArchiveRecordsDTO newRecordObj = updateOnTimeServer.selectPriveRecord(thisRecordOrder.getRecordtype(),
                                    thisRecordOrder.getArchivetype(),
                                    thisRecordOrder.getDefaultorder(),
                                    thisSeq.getId(),
                                    null);
                            if (null == newRecordObj) {
                                //这种现象有记录出现在选人文书中
//                                throw new Exception("该文书代码无法匹配上一个文书");
                                baseServer.insertErrorLog(etlLog, "该文书无法找到对应的上一个文书", wjbm + "," + wjbid);

                                continue;
                            }
                            int order = 1;
                            if (newRecordObj.getThisorder() > 1) {
                                order = newRecordObj.getThisorder() + 1;
                            }
                            updateRows++;
                            baseServer.createRecordsNoSuspect(type, order, thisRecord);
                            //后面的所有文书顺序+1
                            updateOnTimeServer.updateOrderAdd(type.getArchiveseqid(),
                                    type.getId()
                                    , order);
                            lastV.setLastpkdatevalue(updateTime);
                            baseServer.updateLastValue(lastV);
                        }

                    }

                }
            }
            lastV.setLastpkdatevalue(updateTime);
            baseServer.updateLastValue(lastV);
        }
        baseServer.insertSuccessLog(etlLog, updateRows);


    }

    //3.已有文书新增/删除 文件
    public void updateFiles(String groupcode) throws Exception {
        //查询最后一次更新时间 lastV.getLastpkdatevalue()
        EtlTablelogDTO lastV = baseServer.selectLastValue("WJ_BJZ", "GXSJ,JLSJ");
        //查找某地市下某时间后新增或有更新的文件
        List<WjBjz> newFiles = updateOnTimeServer.selectNewFilesAfterDate(groupcode, lastV.getLastpkdatevalue());
        EtlLogsDTO etlLog = new EtlLogsDTO();
        etlLog.setSystemname(lastV.getSystemname());
        etlLog.setTablename("FUN_ARCHIVE_FILES");
        etlLog.setStarttime(new Date());
        etlLog.setLastpkname(lastV.getLastpkname());
        etlLog.setLastpkstrvalue("无数据更新");
        int updateRows = 0;
        for (WjBjz thisFile :
                newFiles) {
            Date updateTime= Optional.ofNullable(thisFile.getGxsj()).orElse(new Date());

            //先判别删除
            if (thisFile.getScbj() == 1) {
                updateOnTimeServer.deleteFilesByBjzid(thisFile.getId());//鲨了
                updateRows++;
                lastV.setLastpkdatevalue(updateTime);
                baseServer.updateLastValue(lastV);
            } else {
                //案宗图片更新时会更新图片地址
                //判断gxsj字段是否为空
                FunArchiveFilesDTO file = new FunArchiveFilesDTO();
                if (null == thisFile.getGxsj()) {
                    //为空则说明是新上传的 thisWjdz.getWjdz()
                    file.setFileurl(thisFile.getWjdz());
                    file.setBjzid(thisFile.getId());
                    updateOnTimeServer.updateFilesByBjzid(file);
                    updateRows++;
                } else {
                    //不为空则是更新的
                    if (updateOnTimeServer.selectFileCountByBjzid(thisFile.getId())) {
                        baseServer.insertErrorLog(etlLog, "该文件已抽取过", thisFile.getId() + "");
                        continue;
                    }
                    //查询该案件对应的文书
                    List<FunArchiveRecordsDTO> records = updateOnTimeServer.selectRecordsByWjMessageNotSend(thisFile.getWjbm(), thisFile.getWjbid());
                    if (records.size() == 0) {
                        baseServer.insertErrorLog(etlLog, "该文件没有找到对应的文书", thisFile.getId() + "");
                        continue;
                    }
                    //给这些文书添加新的页
                    file.setIsdowland(0);
                    file.setIsazxt(0);
                    file.setAuthor("系统抽取");
                    file.setAuthorid(0);
                    file.setIsshow(0);
                    file.setFileurl(thisFile.getWjdz());
                    file.setOriginurl(thisFile.getWjdz());
                    file.setFilename(thisFile.getWjzw());
                    file.setIsdelete(0);
                    file.setFiletype(0);//标准文书
                    file.setBjzid(thisFile.getId());
                    String httpHead="http://35.0.11.40";
                    String version = GlobalUtil.getGlobal("version");//查询版本
                    if ("provinceTest".equals(version)) {
                        //省厅测试版本
                        httpHead="http://35.0.40.36:8081";
                    }

                    if ("1".equals(thisFile.getWsgl())) {
                        file.setServerip(httpHead+"/WordToImage/");
                    } else {
                        //if ("3".equals(thisWjdz.getWsgl()))
                        //设置用于判断是否是附件的关键词
                        String[] wsfj_directory_yuejuan = {"wsfjNew", "wsfj", "wpfjNew", "wpfj"};
                        for (String thisWDY :
                                wsfj_directory_yuejuan) {
                            //判断是否有这些关键词
                            if (thisFile.getWjdz().indexOf(thisWDY) > -1) {
                                //yes

                                if ("provinceTest".equals(version)) {
                                    //省厅测试版本
                                    file.setServerip(httpHead+":8080/fjupload");
                                }else{
                                    file.setServerip(httpHead+":8080/fjupload");
                                }
                                break;
                            } else {
                                //no   //这个是上传的word转换的
                                file.setServerip(httpHead+"/UploadFileToImage");
                            }
                        }
                    }
                    for (FunArchiveRecordsDTO record :
                            records) {
                        file.setJqbh(record.getJqbh());
                        file.setAjbh(record.getAjbh());
                        //查看这东西的最后一页  然后将其放放到最后
                        file.setThisorder(updateOnTimeServer.selectFilecCountByRecordid(record.getId()) + 1);
                        file.setArchiverecordid(record.getId());
                        file.setArchivetypeid(record.getArchivetypeid());
                        file.setArchiveseqid(record.getArchiveseqid());
                        file.setArchivesfcid(record.getArchivesfcid());
                        file.setFilecode(UUID.randomUUID().toString());
                        updateOnTimeServer.createFils(file);
                        etlLog.setLastpkstrvalue(file.getId() + "");
                        updateRows++;
                    }
                    lastV.setLastpkdatevalue(updateTime);
                    baseServer.updateLastValue(lastV);
                }
            }
        }
        baseServer.insertSuccessLog(etlLog, updateRows);
    }

    //更新案件信息
    public  void updateCaseInformation() throws Exception{
        //查看案件信息表更新的数据
        EtlTablelogDTO lastV = baseServer.selectLastValue("XT_AJXXB", "GXSJ");
        //
        List<XtAjxxb> updateAjxxb=  updateOnTimeServer.selectUpdateCase(lastV.getLastpkdatevalue());
    }

    //更新多媒体卷宗上传的附件
    public void dmtjzUpload() {
        String groupcode = GlobalUtil.getGlobal("groupcode");//查询的单位代码
        String version = GlobalUtil.getGlobal("version");//查询版本
        //只有大庆版有这个
        if ("2306".equals(groupcode) && version.equals("city")) {
            try {
                EtlTablelogDTO lastV = baseServer.selectLastValue("JZ_FJZB", "GXSJ");
                List<JzFjzb> updatedFjzb = updateOnTimeServer.selectFjzbGtFxsj(lastV.getLastpkdatevalue());
                EtlLogsDTO etlLog = new EtlLogsDTO();
                etlLog.setSystemname(lastV.getSystemname());
                etlLog.setTablename(lastV.getTablename());
                etlLog.setStarttime(new Date());
                etlLog.setLastpkname(lastV.getLastpkname());
                int updateRows = 0;
                for (JzFjzb thisFjzb :
                        updatedFjzb) {
                    String wjbm = "thkjdmtjz";
                    Integer wjbid = thisFjzb.getId();
                    String jqbh = thisFjzb.getJqbh();//警情编号
                    XtWjflb mapWjflb = XtWjflbMapper.INSTANCE.fjzbToXtWjflb(thisFjzb);
                    mapWjflb.setWjbm(wjbm);

                    if (thisFjzb.getScbj() > 0) {
                        //删除
                        updateOnTimeServer.deleteRecordsByWjMessageNotSend(wjbm, wjbid);
                        updateRows++;
                        etlLog.setLastpkstrvalue(wjbm + "," + wjbid);//写日志
                    } else {
                        //新增
                        //1.查询fun_archive_records中文件表名表id有没有  没有新加  有就不用管了
                        List<FunArchiveRecordsDTO> records = updateOnTimeServer.selectRecordsByWjMessageNotSend(wjbm, wjbid);
                        if (records.size() == 0) {
                            //没有以其它途径出现过  新增进去
                            List<FunArchiveSeqDTO> seqs = updateOnTimeServer.selectSeqByJqbh(thisFjzb.getJqbh());
                            //这些是需要添加新文书的seq
                            //查新新的这个文书应该出现的位置
                            for (FunArchiveSeqDTO thisSeq :
                                    seqs) {
                                //查询该文书应该出现的位置
                                SysRecordorderDTO thisRecordOrder = updateOnTimeServer.selectRecordOrderByTypes(thisFjzb.getWjdm(), thisSeq.getArchivetype());
                                FunArchiveTypeDTO type = new FunArchiveTypeDTO();
                                if (null == thisRecordOrder) {
                                    //即使是他们瞎编的文书也要录入进去
                                    thisRecordOrder = new SysRecordorderDTO();
                                    //当文书代码是瞎编的时  无论如何都不可以对人否则在下次整理时将失踪 只能插在证据材料卷末尾
                                 /*   if (StringUtils.isNotEmpty(thisFjzb.getXyrbh())){
                                        thisRecordOrder.setRecordstyle(1);//对嫌疑人单选
                                    }else {
                                        thisRecordOrder.setRecordstyle(2);//对案文书
                                    }*/

                                    thisRecordOrder.setRecordstyle(2);//对案文书
                                    thisRecordOrder.setRecordtype(2);//一律证据材料卷
                                    thisRecordOrder.setArchivetype(thisSeq.getArchivetype());
                                    thisRecordOrder.setDefaultorder(99999);//顺序放最后
                                    //查询最大顺序
                                    //查询对应位置的type
                                    type = updateOnTimeServer.selectTypeBySeqType(thisSeq.getId(), thisRecordOrder.getRecordtype());
                                    int maxOrder = updateOnTimeServer.selectMaxOrderByTypeid(type.getId());
                                    thisRecordOrder.setDefaultorder(maxOrder + 1);//顺序放最后
                                    //  FunArchiveSeqDTO     updateOnTimeServer.selectActiveSeqByJqbh(jqbh);

                                } else {
                                    //查询对应位置的type
                                    type = updateOnTimeServer.selectTypeBySeqType(thisSeq.getId(), thisRecordOrder.getRecordtype());
                                }
                                //查询对应位置的type
                                //  FunArchiveTypeDTO type = updateOnTimeServer.selectTypeBySeqType(thisSeq.getId(), thisRecordOrder.getRecordtype());
                                //制作一个空的案件对象以供保存文书方法用
                                FunCaseInfoDTO FunCaseInfoDTO = new FunCaseInfoDTO();
                                FunCaseInfoDTO.setId(thisSeq.getCaseinfoid());
                                FunCaseInfoDTO.setSfcnumber(thisSeq.getSfcnumber());
                                //判断文书是否对人
                                if (thisRecordOrder.getRecordstyle() == 1 && StringUtils.isNotEmpty(thisFjzb.getXyrbh())) {
                                    //该文书对人
                                    //查看嫌疑人是否存在
                                    String[] suspectAry = thisFjzb.getXyrbh().split(",");//查看该文书是否有对应的嫌疑人
                                    for (String xyrbh :
                                            suspectAry) {
                                        FunSuspectDTO suspect = updateOnTimeServer.selectSuspectBySuspectcode(xyrbh);
                                        if (null != suspect) {
                                            //这个嫌疑人是已存在的  添加关系和文书
                                            //文书
                                            FunArchiveRecordsDTO newRecordObj = updateOnTimeServer.selectPriveRecord(thisRecordOrder.getRecordtype(),
                                                    thisRecordOrder.getArchivetype(),
                                                    thisRecordOrder.getDefaultorder(),
                                                    thisSeq.getId(),
                                                    suspect.getId());
                                            if (null == newRecordObj) {
                                                //这种现象有记录出现在选人文书中
                                                baseServer.insertErrorLog(etlLog, "该文书无法找到对应的上一个文书（选人）", wjbm + "," + wjbid);
                                                continue;

                                            }
                                            updateRows++;
                                            baseServer.createRecordsSuspect(type, suspect, FunCaseInfoDTO, newRecordObj.getThisorder() + 1, mapWjflb);
                                            lastV.setLastpkdatevalue(new Date());
                                            baseServer.updateLastValue(lastV);

                                        } else {
                                            //这个嫌疑人不存在  添加嫌疑人、关系、文书
                                            //查找嫌疑人
                                            XtXyrxxb xyr = updateOnTimeServer.selectXyrByXyrbh(xyrbh);
                                            Integer order = 1;
                                            //新建嫌疑人   理论上这步只会执行一次  因为在这里新建嫌疑人后  再次查询对应嫌疑人编号就应该有这个人了
                                            if (null != xyr) {
                                                order = Optional.of(updateOnTimeServer.selectRsMaxOrderByTypeid(type.getId()) + 1).orElse(3);
                                                ;//准备出该插入那里的顺序
                                                FunSuspectDTO newSuspectDTO = new FunSuspectDTO();
                                                newSuspectDTO.setSuspectidcard(xyr.getZjhm());
                                                newSuspectDTO.setSuspectname(xyr.getXm());
                                                newSuspectDTO.setJqbh(thisSeq.getJqbh());
                                                newSuspectDTO.setAjbh(thisSeq.getAjbh());
                                                newSuspectDTO.setCaseinfoid(thisSeq.getCaseinfoid());
                                                newSuspectDTO.setSfcnumber(thisSeq.getSfcnumber());
                                                newSuspectDTO.setCasetype(1);//我也不知道我为什么非得要案件类型  但是一直到最后也没用上 那么就写死算了
                                                newSuspectDTO.setSuspectstate(xyr.getQzcszt());
                                                newSuspectDTO.setDefaultorder(updateOnTimeServer.selectMaxOrderByCaseid(thisSeq.getCaseinfoid()) + 1);
                                                newSuspectDTO.setSuspectcode(xyrbh);
                                                updateOnTimeServer.insertNewSuspect(newSuspectDTO);
                                                baseServer.createRecordsSuspect(type, newSuspectDTO, FunCaseInfoDTO, order, mapWjflb);
                                                etlLog.setLastpkstrvalue(wjbm + "," + wjbid);//写日志
                                            } else {
                                                //特殊情况 发现找不到对应的嫌疑人了
                                                updateRows++;
                                                baseServer.createRecordsNoSuspect(type, order, mapWjflb);

                                            }
                                            updateOnTimeServer.updateOrderAdd(type.getArchiveseqid(),
                                                    type.getId()
                                                    , order);
                                            lastV.setLastpkdatevalue(new Date());
                                            baseServer.updateLastValue(lastV);

                                        }
                                    }
                                } else {
                                    //该文书不对人  查找该文书的应在位置
                                    //查找该文书应有顺序的上个文书的顺序  肯定会找到一个  因为最上面肯定有一个封皮和目录
                                    FunArchiveRecordsDTO newRecordObj = updateOnTimeServer.selectPriveRecord(thisRecordOrder.getRecordtype(),
                                            thisRecordOrder.getArchivetype(),
                                            thisRecordOrder.getDefaultorder(),
                                            thisSeq.getId(),
                                            null);
                                    if (null == newRecordObj) {
                                        //这种现象有记录出现在选人文书中
//                                throw new Exception("该文书代码无法匹配上一个文书");
                                        baseServer.insertErrorLog(etlLog, "该文书无法找到对应的上一个文书", wjbm + "," + wjbid);

                                        continue;
                                    }
                                    int order = 1;
                                    if (newRecordObj.getThisorder() > 1) {
                                        order = newRecordObj.getThisorder() + 1;
                                    }
                                    updateRows++;
                                    baseServer.createRecordsNoSuspect(type, order, mapWjflb);
                                    //后面的所有文书顺序+1
//                                    updateOnTimeServer.updateOrderAdd(type.getArchiveseqid(),
//                                            type.getId()
//                                            , order);
                                    lastV.setLastpkdatevalue(new Date());
                                    baseServer.updateLastValue(lastV);
                                    etlLog.setLastpkstrvalue(wjbm + "," + wjbid);//写日志
                                }

                            }

                        }
                    }

                }
                if (0 == updateRows) {
                    etlLog.setLastpkstrvalue("没有需要更新的数据");//写日志
                }
                //插入成功日志
                baseServer.insertSuccessLog(etlLog, updateRows);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
