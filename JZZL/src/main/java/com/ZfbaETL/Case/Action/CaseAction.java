package com.ZfbaETL.Case.Action;

import com.ZfbaETL.BaseServer.BaseServer;
import com.ZfbaETL.Case.Server.ArchiveService;
import com.ZfbaETL.Case.Server.CaseServer;
import com.bean.jzgl.DTO.*;
import com.bean.zfba.*;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author MrLu
 * @createTime 2021/1/4 17:30
 * @describe 抽案件
 */
@Configuration
@EnableScheduling
public class CaseAction implements CommandLineRunner {
    @Autowired
    private BaseServer baseServer;
    @Autowired
    private CaseServer caseServer;
    @Autowired
    private ArchiveService archiveService;

    @Override
    public void run(String... args) throws Exception {
        ImportCases();
//        UpdateUsers();
    }

    /**
     * 抽取案件 1小时一次
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:51
     */
    public void ImportCases() {
        EtlTablelogDTO lastV = baseServer.selectLastValue("XT_AJXXB", "ID");
        List<XtAjxxb> cases = caseServer.selectNewCase(lastV.getLastpknumvalue());
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());
        int insertCount = 0;
        for (XtAjxxb thisCase :
                cases) {
            try {
                FunCaseInfoDTO newCaseInfo = new FunCaseInfoDTO();
                XtBarb zbr = caseServer.selectZbrByJqbh(thisCase.getJqbh());//主办人
                SysBadwb zbdw = caseServer.selectZbdwByJqbn(thisCase.getJqbh());//主办单位
                if (null == zbr) {
                    baseServer.insertErrorLog(record, "案件没有主办人", thisCase.getId() + "");
                    continue;
                }
                if (null == zbdw) {
                    baseServer.insertErrorLog(record, "案件没有主办单位", thisCase.getId() + "");
                    continue;
                }
                SysUserDTO thisJzlZbr = caseServer.selectJzUserByIdCard(zbr.getBargmsfhm());
                newCaseInfo.setJqbh(thisCase.getJqbh());
                newCaseInfo.setAjbh(thisCase.getAjbh());
                newCaseInfo.setCasename(Optional.ofNullable(thisCase.getAjmc()).orElse("未填写案件名称"));
                newCaseInfo.setSfcnumber(StringUtil.createSfcNumber(zbdw.getBadwdm().substring(0, 6)));
                newCaseInfo.setCasestate(0);//普通案件
                newCaseInfo.setCasetype(Integer.parseInt(thisCase.getAjlx()));//形式行政
                newCaseInfo.setCasestage(thisCase.getZcjddm());//案件阶段
                newCaseInfo.setCaseclass(thisCase.getAjlx());//案件类别
                newCaseInfo.setCaseclasscn(Optional.ofNullable(thisCase.getAjlbzw()).orElse("无"));//案件类别中文
                newCaseInfo.setBarxm(zbr.getBarxm());//主办人
                newCaseInfo.setBarsysuserid(thisJzlZbr.getId());//主办人再卷整理系统中的用户id
                newCaseInfo.setBaridcard(zbr.getBargmsfhm());//办案人
                newCaseInfo.setBadwdwdm(zbdw.getBadwdm());//办案单位
                newCaseInfo.setBadwdwmc(zbdw.getBadwzw());
                newCaseInfo.setGajgmc("公安机关名称？");
                newCaseInfo.setGajgdm("公安机关代码？");
                newCaseInfo.setLarq(thisCase.getLarq());//立案日期
                newCaseInfo.setJarq(thisCase.getJarq());//简要案情

                caseServer.insertCaseinfo(newCaseInfo);


                //新建部分的对应文书
                FunArchiveTypeDTO newType = crateArchive(newCaseInfo);
                //抽取不对人的文书
                createRecordsNoSuspect(newType);
                //抽取对人的文书
                insertSuspectRecord(newCaseInfo, newType);
                //主办人关系
                insertCasePeopleCase(thisJzlZbr, newCaseInfo, Enums.PersonType.PRIMARY);
                //辅办人
                List<XtBarb> fbrList = caseServer.selectFbrByJqbh(thisCase.getJqbh());//查询案件的辅办人
                for (XtBarb thisFbr :
                        fbrList) {
                    SysUserDTO thisJzlFbr = caseServer.selectJzUserByIdCard(thisFbr.getBargmsfhm());
                    if (null == thisJzlFbr) {
                        baseServer.insertErrorLog(record, "该辅办人不在人员列表中！", thisCase.getId() + "");
                        continue;
                    }
                    insertCasePeopleCase(thisJzlFbr, newCaseInfo, Enums.PersonType.SECONDARY);
                }
                //查询可以看该案件的人  （部门领导、法制科）

                for (SysUser thisAzUser :
                        caseServer.selectLeaderByDwdm(zbdw.getBadwdm())) {
                    //查询该用户对应卷整理的用户
                    SysUserDTO thisJzlUser = caseServer.selectJzUserByAzId(thisAzUser.getId());
                    insertCasePeopleCase(thisJzlUser, newCaseInfo, Enums.PersonType.LEADER);
                }
                //更新最后抽取的值
                lastV.setLastpknumvalue(thisCase.getId());
                baseServer.updateLastValue(lastV);
                insertCount++;
            } catch (Exception ignored) {
                ignored.printStackTrace();
                baseServer.insertErrorLog(record, ignored.getMessage(), thisCase.getId() + "");
            }
        }
        baseServer.insertSuccessLog(record, insertCount);

    }

    /**
     * 创建人案关系表
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 9:55
     */
    private void insertCasePeopleCase(SysUserDTO jzlUser, FunCaseInfoDTO newCaseInfo, Enums.PersonType pt) throws Exception {
        FunCasePeoplecaseDTO funCasePeoplecaseDTO = new FunCasePeoplecaseDTO();
        funCasePeoplecaseDTO.setIdcard(jzlUser.getIdcardnumber());//身份证号
        funCasePeoplecaseDTO.setName(jzlUser.getXm());//姓名
        funCasePeoplecaseDTO.setJqbh(newCaseInfo.getJqbh());//警情编号
        funCasePeoplecaseDTO.setAjbh(newCaseInfo.getAjbh());//案件编号
        funCasePeoplecaseDTO.setPersontype(pt.getValue());//人员类型
        funCasePeoplecaseDTO.setSfcnumber(newCaseInfo.getSfcnumber());//送检编号
        funCasePeoplecaseDTO.setCasename(newCaseInfo.getCasename());//案件名称
        funCasePeoplecaseDTO.setCaseinfoid(newCaseInfo.getId());//案件表id
        funCasePeoplecaseDTO.setSysuserid(jzlUser.getId());//用户id
        funCasePeoplecaseDTO.setBarxm(newCaseInfo.getBarxm());//主办人姓名
        funCasePeoplecaseDTO.setBarsysuserid(newCaseInfo.getBarsysuserid());//主办人id
        funCasePeoplecaseDTO.setBaridcard(newCaseInfo.getBaridcard());//主办人身份证号
        funCasePeoplecaseDTO.setBadwdwdm(newCaseInfo.getBadwdwdm());//办案单位代码
        funCasePeoplecaseDTO.setBadwdwmc(newCaseInfo.getBadwdwmc());
        funCasePeoplecaseDTO.setCasetype(newCaseInfo.getCasetype());//案件类型
        caseServer.insertCasePeopleCase(funCasePeoplecaseDTO);

    }

    /**
     * 插入案件的嫌疑人相关
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 14:01
     */
    private void insertSuspectRecord(FunCaseInfoDTO newCaseInfo, FunArchiveTypeDTO newType) throws Exception {
        List<XtXyrxxb> suspects = caseServer.selectXyrByJqbh(newCaseInfo.getJqbh());
        int i = 1;
        for (XtXyrxxb thisXyr :
                suspects) {
            FunSuspectDTO newSuspectDTO = new FunSuspectDTO();
            newSuspectDTO.setSuspectidcard(thisXyr.getZjhm());
            newSuspectDTO.setSuspectname(thisXyr.getXm());
            newSuspectDTO.setJqbh(newCaseInfo.getJqbh());
            newSuspectDTO.setAjbh(newCaseInfo.getAjbh());
            newSuspectDTO.setCaseinfoid(newCaseInfo.getId());
            newSuspectDTO.setSfcnumber(newCaseInfo.getSfcnumber());
            newSuspectDTO.setCasetype(newCaseInfo.getCasetype());
            newSuspectDTO.setSuspectstate(thisXyr.getQzcszt());
            newSuspectDTO.setDefaultorder(i++);
            caseServer.insertNewSuspect(newSuspectDTO);
            //抽取对应文书并建立文书与人的关系
            createRecordsSuspect(newType, newSuspectDTO, newCaseInfo);
        }

    }

    /**
     * 创建 sfc、初始seq   type 、record 、files
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 16:36
     */
    private FunArchiveTypeDTO crateArchive(FunCaseInfoDTO newCaseInfo) {
        //创建初始卷
        //1.sfc
        FunArchiveSFCDTO newSfc = new FunArchiveSFCDTO();
        newSfc.setJqbh(newCaseInfo.getJqbh());
        newSfc.setAjbh(newCaseInfo.getAjbh());
        newSfc.setIssend(Enums.IsSend.NO.getValue());
        newSfc.setAuthor("系统抽取");
        newSfc.setAuthoridcard("0");
        newSfc.setSfcnumber(newCaseInfo.getSfcnumber());
        newSfc.setArchivetype(EnumSoft.archivetype.CSJ.getValue());//原始卷
        newSfc.setArchivename(EnumSoft.archivetype.CSJ.getName());
        newSfc.setCaseinfoid(newCaseInfo.getId());
        newSfc.setBaserecordid(0);
        newSfc.setIssuspectorder(0);//原始卷没选人
        archiveService.createNewSfc(newSfc);
        //2.创建初始的seq
        FunArchiveSeqDTO newSeq = new FunArchiveSeqDTO();
        newSeq.setJqbh(newSfc.getJqbh());
        newSeq.setAjbh(newSfc.getAjbh());
        newSeq.setAuthor(newSfc.getAuthor());
        newSeq.setAuthoridcard(newSfc.getAuthoridcard());
        newSeq.setBatchesseq(-1);//原始卷
        newSeq.setIsfinal(1);//已经完结了
//        newSeq.setRecordsnumber(newSfc.get);
        newSeq.setCaseinfoid(newSfc.getCaseinfoid());
        newSeq.setArchivetype(newSfc.getArchivetype());
        newSeq.setArchivename(newSfc.getArchivename());
        newSeq.setArchivesfcid(newSfc.getId());
        newSeq.setSfcnumber(newSfc.getSfcnumber());
        newSeq.setAuthorid(0);
        newSeq.setPrevid(0);
        newSeq.setBaserecordid(0);
        newSeq.setIsactive(0);//不是正在被使用的
        archiveService.createNewSeq(newSeq);
        //3.新建type

        FunArchiveTypeDTO newType = new FunArchiveTypeDTO();
        newType.setJqbh(newSeq.getJqbh());
        newType.setAjbh(newSeq.getAjbh());
        newType.setRecordtype(EnumSoft.recordtype.CSJ.getValue());
        newType.setRecordtypecn(EnumSoft.recordtype.CSJ.getName());
        newType.setDefaultorder(0);
        newType.setIsazxt(0);
//        newType.setArchivename();
        newType.setArchiveseqid(newSeq.getId());
//        newType.setArchivecode();
        newType.setArchivesfcid(newSeq.getArchivesfcid());
        archiveService.createNewType(newType);
        return newType;

    }

    /**
     * 抽取文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/5 17:12
     */
    private void createRecordsNoSuspect(FunArchiveTypeDTO newType) {
        //查询不对人的

        List<XtWjflb> Records = archiveService.selectRecordNoSuspect(newType.getJqbh());
        int i = 0;
        for (XtWjflb thisR :
                Records) {
            FunArchiveRecordsDTO newRecord = new FunArchiveRecordsDTO();
            newRecord.setJqbh(newType.getJqbh());
            newRecord.setAjbh(newType.getAjbh());
            newRecord.setThisorder(i++);
            newRecord.setRecordname(thisR.getWjzw());
            newRecord.setArchivetypeid(newType.getId());
            newRecord.setArchivecode(thisR.getWjdm());
            newRecord.setRecordstyle(EnumSoft.recordstyle.DEWS.getValue());
            newRecord.setArchiveseqid(newType.getArchiveseqid());
            newRecord.setRecordscode(thisR.getWjdm());
            newRecord.setIsdelete(0);
            newRecord.setIsazxt(0);//"系统抽取"
            newRecord.setArchivesfcid(newType.getArchivesfcid());
            newRecord.setAuthor("系统抽取");
            newRecord.setAuthorid(0);
            newRecord.setPrevid(0);
            newRecord.setJcyrecordcode("");
            newRecord.setRecordwh("文号");
            newRecord.setEffectivetime(thisR.getJlsj());
            newRecord.setBaserecordid(0);
            newRecord.setRecorduuid(UUID.randomUUID().toString());
            archiveService.createNewRecord(newRecord);
        }
    }

    private void createRecordsSuspect(FunArchiveTypeDTO newType, FunSuspectDTO newSuspect, FunCaseInfoDTO newCaseInfo) {
        //查询对人的

        List<XtWjflb> SusRecords = archiveService.selectRecordBySuspect(newType.getJqbh(), newSuspect.getSuspectcode());
        int i = 0;
        for (XtWjflb thisR :
                SusRecords) {
            FunArchiveRecordsDTO newRecord = new FunArchiveRecordsDTO();
            newRecord.setJqbh(newType.getJqbh());
            newRecord.setAjbh(newType.getAjbh());
            newRecord.setThisorder(i++);
            newRecord.setRecordname(thisR.getWjzw());
            newRecord.setArchivetypeid(newType.getId());
            newRecord.setArchivecode(thisR.getWjdm());
            newRecord.setRecordstyle(EnumSoft.recordstyle.DRWS.getValue());
            newRecord.setArchiveseqid(newType.getArchiveseqid());
            newRecord.setRecordscode(thisR.getWjdm());
            newRecord.setIsdelete(0);
            newRecord.setIsazxt(0);//"系统抽取"
            newRecord.setArchivesfcid(newType.getArchivesfcid());
            newRecord.setAuthor("系统抽取");
            newRecord.setAuthorid(0);
            newRecord.setPrevid(0);
            newRecord.setJcyrecordcode("");
            newRecord.setRecordwh("文号");
            newRecord.setEffectivetime(thisR.getJlsj());
            newRecord.setBaserecordid(0);
            newRecord.setRecorduuid(UUID.randomUUID().toString());
            archiveService.createNewRecord(newRecord);
            //插入嫌疑人文书关系表
            FunSuspectRecordDTO newSR = new FunSuspectRecordDTO();
            newSR.setJqbh(newRecord.getJqbh());
            newSR.setCaseinfoid(newCaseInfo.getId());
            newSR.setAjbh(newRecord.getAjbh());
            newSR.setSfcnumber(newCaseInfo.getSfcnumber());
            newSR.setSuspectid(newSuspect.getId());
            newSR.setRecordid(newRecord.getId());
            newSR.setRecordtype(newType.getRecordtype());//文书类型
            archiveService.createNewSR(newSR);

        }

    }
}
























/*兄弟别指望了，
 * 我都准备辞了另外这个项目很多意外×bug×，
 * 你坚持不了多久的，拜拜，祝好兄弟我只能帮你到这里了!*/