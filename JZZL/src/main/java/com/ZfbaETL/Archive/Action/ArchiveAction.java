package com.ZfbaETL.Archive.Action;

import com.ZfbaETL.Archive.Service.AutoArchiveService;
import com.ZfbaETL.BaseServer.BaseServer;
import com.bean.jzgl.DTO.*;
import com.enums.EnumSoft;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;

/**
 * @author MrLu
 * @createTime 2021/1/5 16:21
 * @describe 文书的抽取与更新
 */
@Configuration
@EnableScheduling
public class ArchiveAction implements CommandLineRunner {
    @Autowired
    private BaseServer baseServer;
    @Autowired
    private AutoArchiveService autoArchiveService;

    @Override
    public void run(String... args) throws Exception {
//        autoManager();
    }

    /**
     * 自动整理
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/6 13:58
     */
    public void autoManager() {
        EtlTablelogDTO lastV = baseServer.selectLastValue("FUN_ARCHIVE_SFC", "ID");
        //查询所有的未被整理的原始卷

        List<FunArchiveSFCDTO> newSfcList = autoArchiveService.selectNewOriginArchive(lastV.getLastpknumvalue());
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());

        int insertCount = 0;
        if (0 < newSfcList.size()) {
            for (FunArchiveSFCDTO thisSfc :
                    newSfcList) {
                try {
                    //新建一个基础卷
                    Integer oriSfcid = thisSfc.getId();

                    thisSfc.setAuthor("系统整理");
                    thisSfc.setAuthoridcard("0");
                    thisSfc.setArchivetype(EnumSoft.archivetype.JCJ.getValue());//基础卷
                    thisSfc.setArchivename(EnumSoft.archivetype.JCJ.getName());//基础卷
                    thisSfc.setIssuspectorder(1);//基础卷是某人给人排好序的
                    autoArchiveService.createNewSfc(thisSfc);//新建这个基础sfc
                    //新建一个基础卷的seq  这时候案卷应该只有一个原始卷
                    FunArchiveSeqDTO oriSeq = autoArchiveService.selectLastSeqBySfc(oriSfcid);
                    Integer OriSeqId = oriSeq.getId();//原始卷的id
                    oriSeq.setAuthor("系统整理");
                    oriSeq.setBatchesseq(0);//基础卷
                    oriSeq.setIsfinal(0);//已经完结了
                    oriSeq.setArchivetype(thisSfc.getArchivetype());//注意这里是基础卷了！
                    oriSeq.setArchivename(thisSfc.getArchivename());
                    oriSeq.setArchivesfcid(thisSfc.getId());
                    oriSeq.setIsactive(0);//正在被使用的
                    autoArchiveService.createNewSeq(oriSeq);//新建的基础卷
//                    Integer BaseSeqId = oriSeq.getId();//基础卷的id

                    createArchiveBySuspect(OriSeqId, oriSeq);
                    record.setLastpkstrvalue(thisSfc.getId() + "");
                    insertCount++;
                    lastV.setLastpknumvalue(thisSfc.getId());
                    baseServer.updateLastValue(lastV);
                } catch (Exception e) {
                    e.printStackTrace();
                    //错误日志
                    baseServer.insertErrorLog(record, e.getMessage(), thisSfc.getId() + "");
                }
            }
            baseServer.insertSuccessLog(record, insertCount);

        } else {
            if (StringUtils.isEmpty(lastV.getLastpkstrvalue())){
                record.setLastpkstrvalue(lastV.getLastpknumvalue().toString());
            }else {
                record.setLastpkstrvalue(lastV.getLastpkstrvalue());
            }

            //啥也没更新
            baseServer.insertSuccessLog(record, 0);
        }


    }


    private void createArchiveBySuspect(int oriSeqId, FunArchiveSeqDTO newSeq) throws Exception {
        //得到基础卷的默认顺序
        List<SysRecordorderDTO> baseOrder = autoArchiveService.selectSysRecordOrderByArchiveType(newSeq.getArchivetype());
        //查询基础卷需要的卷
        List<SysRecordtypeorderDTO> needRecordType = autoArchiveService.selectRecordtypeorderByArchivetype(0);
        FunArchiveTypeDTO funArchiveTypeDTO = new FunArchiveTypeDTO();
        funArchiveTypeDTO.setArchiveseqid(newSeq.getId());//seqid
        funArchiveTypeDTO.setIsazxt(1);//不是案宗来的了
        funArchiveTypeDTO.setJqbh(newSeq.getJqbh());//警情编号
        funArchiveTypeDTO.setArchivesfcid(newSeq.getArchivesfcid());//sfcid
        funArchiveTypeDTO.setAjbh(newSeq.getAjbh());//案件编号
        funArchiveTypeDTO.setArchivename("?");//不知道干啥的两个字段
        funArchiveTypeDTO.setArchivecode("?");
        //循环插入对应的类型
        Map<Integer, Integer> recordTypeIdMap = new HashMap<>();
        for (SysRecordtypeorderDTO thisRecordType : needRecordType) {
            funArchiveTypeDTO.setRecordtype(thisRecordType.getRecordcode());//文书类型代码
            funArchiveTypeDTO.setRecordtypecn(thisRecordType.getRecordname());//文书类型名
            funArchiveTypeDTO.setDefaultorder(thisRecordType.getDefaultorder());//默认顺序
            autoArchiveService.createNewType(funArchiveTypeDTO);//插入
            //新建封皮和封底
            FunArchiveRecordsDTO cover = new FunArchiveRecordsDTO();
            cover.setJqbh(funArchiveTypeDTO.getJqbh());
            cover.setAjbh(funArchiveTypeDTO.getAjbh());
            cover.setBaserecordid(0);//封皮目录封底等不会基于什么生成 而是新建一个
            cover.setThisorder(EnumSoft.fplx.COVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.COVER.getName());
            cover.setArchivetypeid(funArchiveTypeDTO.getId());
            cover.setArchivecode(funArchiveTypeDTO.getArchivecode());
            cover.setArchivesfcid(newSeq.getArchivesfcid());
            cover.setRecordstyle(0);
            cover.setIsdelete(0);
            cover.setPrevid(0);
            cover.setAuthor(newSeq.getAuthor());//整理人姓名
            cover.setAuthorid(newSeq.getAuthorid());//整理人id
            cover.setIsazxt(1);//封皮、目录、封底 都不是安综原有的东西
            cover.setArchiveseqid(newSeq.getId());
            cover.setRecordscode(EnumSoft.fplx.COVER.getValue());//文件代码
            autoArchiveService.insertZlRecords(cover, funArchiveTypeDTO);
            //文书目录
            cover.setThisorder(EnumSoft.fplx.INDEX.getOrder());
            cover.setRecordname(EnumSoft.fplx.INDEX.getName());
            cover.setRecordscode(EnumSoft.fplx.INDEX.getValue());//文件代码
            autoArchiveService.insertZlRecords(cover, funArchiveTypeDTO);
            //封底
            cover.setThisorder(EnumSoft.fplx.BACKCOVER.getOrder());
            cover.setRecordname(EnumSoft.fplx.BACKCOVER.getName());
            cover.setRecordscode(EnumSoft.fplx.BACKCOVER.getValue());//文件代码
            autoArchiveService.insertZlRecords(cover, funArchiveTypeDTO);

            //将类型对应的type表的id记录下来
            recordTypeIdMap.put(thisRecordType.getRecordcode(), funArchiveTypeDTO.getId());

        }
        int i = 0;//页数
        Set<Integer> recordType = new HashSet<>();//文书类型 默认没有
        //循环所有的顺序
        for (SysRecordorderDTO thisOrder :
                baseOrder) {
            //判断文书是否对嫌疑人或是对多人
            if (1 == thisOrder.getRecordstyle() || 7 == thisOrder.getRecordstyle()) {
                //如果对嫌疑人 按照人查询文书
                //该卷类型中所有的对嫌疑人文书都已经创建过了  跳过
                if (recordType.contains(thisOrder.getRecordtype())) {
                    continue;
                }

                List<FunSuspectDTO> Suspects = autoArchiveService.selectSuspectByCaseinfoId(newSeq.getCaseinfoid());

                //循环这个嫌疑人的顺序
                for (FunSuspectDTO thisSuspect : Suspects) {
                    //该嫌疑人在此卷下的所有文书
                    List<FunArchiveRecordsDTO> suspectRecord = autoArchiveService
                            .selectRecordOrderForSuspect(thisSuspect.getId(),
                                    thisOrder.getArchivetype(),
                                    thisOrder.getRecordtype(),
                                    oriSeqId);
                    //把嫌疑人的文书按顺序插入
                    int j = 1;//相对顺序
                    for (FunArchiveRecordsDTO thisSuspectRecord :
                            suspectRecord) {
                        i = i + j++;
                        thisSuspectRecord.setArchivetypeid(recordTypeIdMap.get(thisOrder.getRecordtype()));//typeid
                        thisSuspectRecord.setArchiveseqid(newSeq.getId());//seqid
                        thisSuspectRecord.setThisorder(i);//顺序
//                        thisSuspectRecord.setArchivesfcid(newSeq.getArchivesfcid());//sfcid
                        FunSuspectRecordDTO sr = autoArchiveService.selectSuspectRecordByRid(thisSuspectRecord.getId());//此时使用原有的id查询

                        //查询该文书取第几页
                        SysRecordorderDTO ro=  autoArchiveService.selectRecordOrderByTypes(thisSuspectRecord.getRecordscode(),0);  //文书取第几页
                        copyRecordsToNew(thisSuspectRecord, ro.getDemand());//（copy）
                        if (null != sr) {
                            //这里应该保证sr不能为空  因为这个文书如果对人但是在嫌疑人文书关联表中没有数据那么就是数据出现错误了  是个问题了
                            //对人文书复制关联表
                            sr.setRecordid(thisSuspectRecord.getId());//注意此时是新的id了
                            sr.setArchiveseqid(newSeq.getId());//seqid也是新的
                            autoArchiveService.createNewSR(sr);
                        }
                    }
                }
                recordType.add(thisOrder.getRecordtype());//记录当前操作的文书类型
            } else {
                //文书不是对嫌疑人的
                //查看这个顺序的应有的文书

                List<FunArchiveRecordsDTO> thisRecord = autoArchiveService.selectRecordBySeqRcode(oriSeqId, thisOrder.getRecordcode());
                //卷首、卷尾、卷目录创建

                //同一个文书代码可能对应多个文书（大多数情况就一个）
                int j = 1;//相对顺序
                for (FunArchiveRecordsDTO thisReocrd : thisRecord) {
                    i += j++;
                    thisReocrd.setArchivetypeid(recordTypeIdMap.get(thisOrder.getRecordtype()));//typeid
                    thisReocrd.setArchiveseqid(newSeq.getId());//seqid
                    thisReocrd.setThisorder(i);//顺序
                    copyRecordsToNew(thisReocrd, thisOrder.getDemand());//插入（copy）
                }
            }

        }
    }


    /**
     * @param newRecord 新建的文书
     * @param demand    区第几页 0为全都要
     * @return |
     * @author Mrlu
     * @createTime 2021/1/29 10:57
     */
    private void copyRecordsToNew(FunArchiveRecordsDTO newRecord, Integer demand) {
        int oriId = newRecord.getId();//此时还是原有的id
        newRecord.setPrevid(oriId);//上一个 源于谁的id
        autoArchiveService.createNewRecord(newRecord);//新建 （此时该实体类的id已经变成新的了）
        copyFilesToNew(oriId, newRecord, demand);
    }

    private void copyFilesToNew(int oriId, FunArchiveRecordsDTO newRecord, Integer demand) {

        if (newRecord.getRecordscode().equals("AS021")){
            System.out.println(demand);
        }
        int newRecordId = newRecord.getId();//新建的文书id
        //开始复制文书文件
        //查找该文书原有的文件
        int index =1;
        for (FunArchiveFilesDTO thisFile :
                autoArchiveService.selectRecordFilesByRecordId(oriId, null)) {
            //只取第几页
            if ( 0 != demand&&index != demand ) {
                index++;
                continue;
            }
            //判断该文件的uuid是否有重复  如果有的话重新生成uuid  同一个seq下不允许有重复的fileCode ！
            int Repeated = autoArchiveService.selectRepeatedlyFileCodeBySeqid(thisFile.getFilecode(), newRecord.getArchiveseqid());
            if (Repeated > 0) {
                thisFile.setFilecode(UUID.randomUUID().toString());
            }

            thisFile.setArchiveseqid(newRecord.getArchiveseqid());
            thisFile.setArchivesfcid(newRecord.getArchivesfcid());
            thisFile.setArchivetypeid(newRecord.getArchivetypeid());
            thisFile.setArchiverecordid(newRecordId);
            thisFile.setAuthor(newRecord.getAuthor());
            thisFile.setAuthorid(newRecord.getAuthorid());
            //复制插入
            autoArchiveService.createFiles(thisFile);
            if ( 0 != demand){
                //当该文书抽取指定页数 此页整理完毕后后面的就不要了
                break;
            }
        }

    }
}
