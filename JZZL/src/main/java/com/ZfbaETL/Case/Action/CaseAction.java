package com.ZfbaETL.Case.Action;

import com.ZfbaETL.BaseServer.BaseServer;
import com.ZfbaETL.Case.Server.ArchiveService;
import com.ZfbaETL.Case.Server.CaseServer;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysRoleUser;
import com.bean.zfba.*;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.module.SystemManagement.Services.UserService;
import com.util.GlobalUtil;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

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
    @Qualifier("UserService")
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
//      ImportCases();
    }

    /**
     * 抽取案件 1小时一次
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 17:51
     */
    @Scheduled(fixedRate=3000000)//每50分钟
    public void ImportCases() {
        if ("1".equals(GlobalUtil.getGlobal("startEtl"))){
            return;
        }
        String groupcode = GlobalUtil.getGlobal("groupcode");//查询的单位代码
        String version = GlobalUtil.getGlobal("version");//查询版本
        EtlTablelogDTO lastV = baseServer.selectLastValue("XT_AJXXB", "ID");
        boolean isXz = true;//是否抽取行政案件
        if ("city".equals(version)) {
            //地市版  这是省厅版本的！
            return;
        } else if ("province".equals(version)) {
            //省厅版本
            isXz = false;
        } else if ("provinceTest".equals(version)) {
            //省厅测试版本
            isXz = false;
        } else {
            System.err.println("啥版本啊？！ global.properties -> version ");
            return;
        }
        List<XtAjxxb> cases =  caseServer.selectNewCase(lastV.getLastpknumvalue(), groupcode, isXz);
//        List<XtAjxxb> cases = caseServer.selectNeededAj();

//       if ("province".equals(version)) {
//            //省厅版本  表中可以放一些非得抽的案件
//            List<XtAjxxb> neededAj = caseServer.selectNeededAj();
//            cases.addAll(neededAj);
//        }
        //selectNeededAj
        EtlLogsDTO record = new EtlLogsDTO();
        record.setSystemname(lastV.getSystemname());
        record.setTablename(lastV.getTablename());
        record.setStarttime(new Date());
        record.setLastpkname(lastV.getLastpkname());
        record.setLastpkstrvalue("无数据更新");
        int insertCount = 0;
        for (XtAjxxb thisCase :
                cases) {
            try {
                int isHaved = caseServer.selectCaseCountByJqbh(thisCase.getJqbh());
                if (isHaved > 0) {
                    continue;
                }
                FunCaseInfoDTO newCaseInfo = new FunCaseInfoDTO();
                XtBarb zbr = caseServer.selectZbrByJqbh(thisCase.getJqbh());//主办人
                SysBadwb zbdw = caseServer.selectZbdwByJqbn(thisCase.getJqbh());//主办单位

                if (null == zbr) {
                    //大概率主办人是后出现的
                    baseServer.insertErrorLog(record, "案件没有主办人，主办人暂为admin", thisCase.getId() + "");
                    zbr =new XtBarb();
                    zbr.setBargmsfhm("admin");
                    zbr.setBarxm("admin");
//                    continue;
                }

                if (null == zbdw) {
                    //此情况从未发生过
                    baseServer.insertErrorLog(record, "案件没有主办单位", thisCase.getId() + "");
                    continue;
                }
                SysUserDTO thisJzlZbr = caseServer.selectJzUserByIdCard(zbr.getBargmsfhm());
                if (null == thisJzlZbr) {
                    //把这个案件的主办人添加到数据库中即可
                    baseServer.insertErrorLog(record, "系统中未查询到主办人对应的用户", thisCase.getId() + "");
                    continue;
                }



                newCaseInfo.setJqbh(thisCase.getJqbh());
                newCaseInfo.setAjbh(thisCase.getAjbh());
                newCaseInfo.setCasename(Optional.ofNullable(thisCase.getAjmc()).orElse("未填写案件名称"));
                newCaseInfo.setSfcnumber(StringUtil.createSfcNumber(zbdw.getBadwdm().substring(0, 6)));
                newCaseInfo.setCasestate(0);//普通案件
                newCaseInfo.setCasetype(Integer.parseInt(thisCase.getAjlx()));//形式行政
                newCaseInfo.setCasestage(thisCase.getZcjddm());//案件阶段
                newCaseInfo.setCaseclass(thisCase.getAjlx());//案件类别

                if ("1".equals(newCaseInfo.getCaseclass())) {
                    //刑事案件
                    newCaseInfo.setLarq(thisCase.getLarq());
                } else {
                    //行政案件
                    newCaseInfo.setLarq(thisCase.getSarq());
                }
                newCaseInfo.setCaseclasscn(Optional.ofNullable(thisCase.getAjlbzw()).orElse("无"));//案件类别中文
                newCaseInfo.setBarxm( Optional.ofNullable(zbr.getBarxm()).orElse("系统抽取"));//主办人
                newCaseInfo.setBarsysuserid(thisJzlZbr.getId());//主办人再卷整理系统中的用户id
                newCaseInfo.setBaridcard(Optional.ofNullable(zbr.getBargmsfhm()).orElse("系统抽取"));//办案人



                newCaseInfo.setBadwdwdm(zbdw.getBadwdm());//办案单位
                newCaseInfo.setBadwdwmc(zbdw.getBadwzw());
                String gajgmc = Optional.ofNullable(caseServer.selectGroupByDwdm(zbdw.getBadwdm()).getDwjc()).orElse(zbdw.getBadwzw());
                newCaseInfo.setGajgmc(gajgmc);//公安机关名称

                newCaseInfo.setGajgdm(zbdw.getBadwdm());
                newCaseInfo.setLarq(thisCase.getLarq());//立案日期
                newCaseInfo.setJarq(thisCase.getJarq());//简要案情
                newCaseInfo.setIssorted(0);//未整理
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

                //开始截0  然后每两个0查一级
                String group = zbdw.getBadwdm();
                Set<SysUser> leaderUsers = new HashSet<>();
                for (int i = 12; i > 0; i = i - 2) {
                    String subGroup = group.substring(0, i);
                    String thisGroupCOde = String.format("%-12s", subGroup).replace(" ", "0");
                    List<SysUser> lu = caseServer.selectLeaderByDwdm(thisGroupCOde);
                    if (null != lu && lu.size() > 0) {
                        leaderUsers.addAll(lu);
                    }
                }
                System.out.println(leaderUsers);

                for (SysUser thisAzUser :
                        leaderUsers) {
//                    String badwdm=zbdw.getBadwdm().substring(01);
                    //查询该用户对应卷整理的用户
                    SysUserDTO thisJzlUser = caseServer.selectJzUserByAzId(thisAzUser.getId());
                    if (null==thisJzlUser||null==thisJzlUser.getId()){
                        continue;
                    }
                    //查看该用户的权限
                    List<SysRoleUser> userRoles = userService.selectRoleByUserid(thisJzlUser.getId());

                    String roles = userRoles.stream().map(SysRoleUser::getRolecode).map(String::valueOf).collect(Collectors.joining(",", ",", ","));//将用户的权限转换成一个以，分割的字符串

                    int roleCount = 0;
                    if (roles.contains(",81,") || roles.contains(",82,")) {
                        roleCount = 1;
                    }
                    if (roles.contains(",41,") || roles.contains(",25,")) {
                        roleCount = roleCount + 2;
                    }
                    if (1 == roleCount) {
                        //部门领导
                        insertCasePeopleCase(thisJzlUser, newCaseInfo, Enums.PersonType.LEADER);
                    } else if (2 == roleCount) {
                        //法制科
                        insertCasePeopleCase(thisJzlUser, newCaseInfo, Enums.PersonType.LEGAL);
                    } else {
                        //即是领导 又是法制科
                        insertCasePeopleCase(thisJzlUser, newCaseInfo, Enums.PersonType.LEADERLEGAL);
                    }

                }


                //更新最后抽取的值
                lastV.setLastpknumvalue(thisCase.getId());
                baseServer.updateLastValue(lastV);
                insertCount++;
                record.setLastpkstrvalue(thisCase.getId() + "");
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
            newSuspectDTO.setSuspectcode(thisXyr.getXyrbh());
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
    private FunArchiveTypeDTO crateArchive(FunCaseInfoDTO newCaseInfo) throws Exception {
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
        newSeq.setIsactive(1);//不是正在被使用的
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
    private void createRecordsNoSuspect(FunArchiveTypeDTO newType) throws Exception {
        //查询不对人的
        List<XtWjflb> Records = archiveService.selectRecordNoSuspect(newType.getJqbh());
        int i = 0;
        for (XtWjflb thisR :
                Records) {
            try {

                baseServer.createRecordsNoSuspect(newType, i++, thisR);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("错误：" + newType.getId());
            }
        }
    }


    private void createRecordsSuspect(FunArchiveTypeDTO newType, FunSuspectDTO newSuspect, FunCaseInfoDTO newCaseInfo) throws Exception {
        //查询对人的

        List<XtWjflb> SusRecords = archiveService.selectRecordBySuspect(newType.getJqbh(), newSuspect.getSuspectcode());
        int i = 0;
        for (XtWjflb thisR :
                SusRecords) {
            try {

                //查看这个文书有没有  如果有就不插文书只插关系表
                FunArchiveRecordsDTO newRecord = archiveService.selectRecordsByWjMessage(thisR.getWjbm(), thisR.getWjbid(), newType.getArchiveseqid());
                if (null == newRecord) {
                    newRecord = new FunArchiveRecordsDTO();
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
                    newRecord.setAuthor(thisR.getJlrxm());
                    newRecord.setAuthoridcard(thisR.getJlrgmsfhm());
                    newRecord.setAuthorid(0);
                    newRecord.setPrevid(0);
                    //查询文号！、检察院代码
                    baseServer.recordSetWhJcycode(newRecord, thisR.getWjdm(), thisR.getWjbm().toUpperCase(), thisR.getWjbid());
                    newRecord.setEffectivetime(thisR.getJlsj());
                    newRecord.setBaserecordid(0);
                    newRecord.setRecorduuid(UUID.randomUUID().toString());
                    newRecord.setWjbm(thisR.getWjbm());//文件表名
                    newRecord.setWjbid(thisR.getWjbid());//文件表id
                    archiveService.createNewRecord(newRecord);
                }

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
                archiveService.createNewSR(newSR);
                baseServer.createFiles(newRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
