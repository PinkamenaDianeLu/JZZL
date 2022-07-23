package com.module.SystemManagement.Services.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.IntegralityCheckResult;
import com.bean.jzgl.Source.SysRecordMessage;
import com.bean.jzgl.Source.SysRoleUser;
import com.bean.zfba.*;
import com.enums.EnumSoft;
import com.enums.Enums;
import com.mapper.jzgl.*;
import com.mapper.zfba.*;
import com.module.SystemManagement.Services.LogService;
import com.module.SystemManagement.Services.TempRemedialService;
import com.module.SystemManagement.Services.UserService;
import com.util.GlobalUtil;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MrLu
 * @createTime 2021/12/21 9:54
 * @describe
 */
@Service
public class TempRemedialImpl implements TempRemedialService {

    @Resource
    FunArchiveFilesDTOMapper funArchiveFilesDTOMapper;

    @Resource
    FunArchiveSeqDTOMapper funArchiveSeqDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    XtAjxxbMapper xtAjxxbMapper;
    @Resource
    XtBarbMapper xtBarbMapper;
    @Resource
    SysBadwbMapper sysBadwbMapper;
    @Resource
    SysUserDTOMapper jzglSysUserMapper;
    @Resource
    SysGroupMapper sysGroupMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Resource
    FunArchiveTypeDTOMapper funArchiveTypeDTOMapper;
    @Resource
    XtWjflbMapper xtWjflbMapper;
    @Resource
    SysRecordMessageMapper sysRecordMessageMapper;
    @Resource
    WjBjzMapper wjBjzMapper;
    @Resource
    XtXyrxxbMapper xtXyrxxbMapper;
    @Resource
    FunSuspectDTOMapper funSuspectDTOMapper;
    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;
    @Resource
    FunSuspectRecordDTOMapper funSuspectRecordDTOMapper;
    @Resource
    SysUserMapper zfbaSysUserMapper;

    @Resource
    DeleteCaseMapper DeleteCaseMapper;
    @Qualifier("UserService")
    @Autowired
    private UserService userService;

    @Override
    public List<FunArchiveFilesDTO> selectFileBySeq(Integer archiveseqid) {
        return funArchiveFilesDTOMapper.selectFileBySeq(archiveseqid);
    }

    @Override
    public List<FunArchiveSeqDTO> selectActiveSeqByCaseInfoId(int caseinfoid) {
        return funArchiveSeqDTOMapper.selectActiveSeqByCaseInfoId(caseinfoid);
    }

    @Override
    public void updateWrongOrderBySeq(Integer archiveseqid) {
        funArchiveRecordsDTOMapper.updateWrongOrderBySeq(archiveseqid);
    }

    @Override
    public List<String> selectRecordNameByRecordIds(int[] ids) {
        return funArchiveRecordsDTOMapper.selectRecordNameByRecordIds(ids);
    }

    @Override
    public Integer extractCase(String jqbh) throws Exception {
        XtAjxxb thisCase = xtAjxxbMapper.selectCaseByJqbh(jqbh);
        int isHaved = funCaseInfoDTOMapper.selectCaseCountByJqbh(thisCase.getJqbh());
        //判断案件抽没抽过
        if (!(isHaved > 0)) {
            //没抽过 抽一下
            FunCaseInfoDTO newCaseInfo = new FunCaseInfoDTO();
            XtBarb zbr = xtBarbMapper.selectZbrByJqbh(thisCase.getJqbh());//主办人
            SysBadwb zbdw = sysBadwbMapper.selectZbdwByJqbn(thisCase.getJqbh());//主办单位

            if (null == zbr) {
                //大概率主办人是后出现的
                zbr = new XtBarb();
                zbr.setBargmsfhm("admin");
                zbr.setBarxm("admin");
//                    continue;
            }

            if (null == zbdw) {
                //此情况从未发生过
                throw new Exception("案件无主办单位");
            }
            SysUserDTO thisJzlZbr = jzglSysUserMapper.selectJzUserByIdCard(zbr.getBargmsfhm());
            if (null == thisJzlZbr) {
                //把这个案件的主办人添加到数据库中即可
                //"系统中未查询到主办人对应的用户",
                throw new Exception("系统中未查询到主办人对应的用户");
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
            newCaseInfo.setBarxm(Optional.ofNullable(zbr.getBarxm()).orElse("系统抽取"));//主办人
            newCaseInfo.setBarsysuserid(thisJzlZbr.getId());//主办人再卷整理系统中的用户id
            newCaseInfo.setBaridcard(Optional.ofNullable(zbr.getBargmsfhm()).orElse("系统抽取"));//办案人

            newCaseInfo.setBadwdwdm(zbdw.getBadwdm());//办案单位
            newCaseInfo.setBadwdwmc(zbdw.getBadwzw());
            String gajgmc = Optional.ofNullable(sysGroupMapper.selectGroupByDwdm(zbdw.getBadwdm()).getDwjc()).orElse(zbdw.getBadwzw());
            newCaseInfo.setGajgmc(gajgmc);//公安机关名称

            newCaseInfo.setGajgdm(zbdw.getBadwdm());
            newCaseInfo.setLarq(thisCase.getLarq());//立案日期
            newCaseInfo.setJarq(thisCase.getJarq());//简要案情
            newCaseInfo.setIssorted(0);//未整理
            funCaseInfoDTOMapper.insertSelective(newCaseInfo);

            //新建部分的对应文书
            FunArchiveTypeDTO newType = crateArchive(newCaseInfo);
            //抽取不对人的文书
            createRecordsNoSuspect(newType);
            //抽取对人的文书
            insertSuspectRecord(newCaseInfo, newType);
            //主办人关系
            insertCasePeopleCase(thisJzlZbr, newCaseInfo, Enums.PersonType.PRIMARY);
            //辅办人
            List<XtBarb> fbrList = xtBarbMapper.selectFbrByJqbh(thisCase.getJqbh());//查询案件的辅办人
            for (XtBarb thisFbr :
                    fbrList) {
                SysUserDTO thisJzlFbr = jzglSysUserMapper.selectJzUserByIdCard(thisFbr.getBargmsfhm());
                if (null == thisJzlFbr) {
                    // "该辅办人不在人员列表中！"
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
                List<SysUser> lu = zfbaSysUserMapper.selectLeaderByDwdm(thisGroupCOde);
                if (null != lu && lu.size() > 0) {
                    leaderUsers.addAll(lu);
                }
            }
            System.out.println(leaderUsers);

            for (SysUser thisAzUser :
                    leaderUsers) {
//                    String badwdm=zbdw.getBadwdm().substring(01);
                //查询该用户对应卷整理的用户
                SysUserDTO thisJzlUser = jzglSysUserMapper.selectJzUserByAzId(thisAzUser.getId());
                if (null == thisJzlUser || null == thisJzlUser.getId()) {
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
            return newCaseInfo.getId();
        } else {
            return 0;
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
        funArchiveSFCDTOMapper.insertSelective(newSfc);
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
        funArchiveSeqDTOMapper.insertSelective(newSeq);
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
        funArchiveTypeDTOMapper.insertSelective(newType);
        return newType;

    }


    private void createRecordsNoSuspect(FunArchiveTypeDTO newType) throws Exception {
        //查询不对人的
        List<XtWjflb> Records = xtWjflbMapper.selectRecordNoSuspect(newType.getJqbh());
        int i = 0;
        for (XtWjflb thisR :
                Records) {
            try {

                createRecordsNoSuspect(newType, i++, thisR);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.printf("错误：" + newType.getId());
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
        newRecord.setThisorder(order < 0 ? 1 : order);
        newRecord.setRecordname(thisR.getWjzw());
        newRecord.setArchivetypeid(newType.getId());
        newRecord.setRecordstyle(EnumSoft.recordstyle.DEWS.getValue());
        newRecord.setArchiveseqid(newType.getArchiveseqid());
        newRecord.setRecordscode(thisR.getWjdm());//文件代码
        newRecord.setIsdelete(0);//是否已删除
        newRecord.setIsazxt(0);//"系统抽取"
        newRecord.setArchivesfcid(newType.getArchivesfcid());

        if (StringUtils.isBlank(thisR.getJlrxm())) {
            newRecord.setAuthor("系统抽取");
        } else {
            newRecord.setAuthor(thisR.getJlrxm());
        }

        if (StringUtils.isBlank(thisR.getJlrgmsfhm())) {
            newRecord.setAuthoridcard("系统抽取");
        } else {
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
        newRecord.setIscoverimg(0);
        funArchiveRecordsDTOMapper.insertSelective(newRecord);
        createFiles(newRecord);
        //查询是文件是否是受案登记表
        if ("XT005".equals(thisR.getWjdm())) {
            // caseServer.selectSahzBool(thisR.getJqbh(),thisR.getId())
            //查询对应的受案回执
            //判断是否是受案回执
            newRecord.setWjbm("XT_SAHZ");
            newRecord.setRecordscode("XT013");//文件代码  该文件代码为卷整理自创
            newRecord.setRecordname("受案回执");

            funArchiveRecordsDTOMapper.insertSelective(newRecord);//再复制创建一个受案回执
            createFiles(newRecord);


        }
    }

    /**
     * 查询该文书的检察院代码与字
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/3/19 14:28
     */
    public SysRecordMessage selectMessageByCode(String recordcode, Integer archivetype) {
        return sysRecordMessageMapper.selectMessageByCode(recordcode, archivetype);
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

    public void recordSetWhJcycode(FunArchiveRecordsDTO newRecord, String wjdm, String wjbm, Integer wjbid) {
        //查询该文书信息
        SysRecordMessage recordMessage = selectMessageByCode(wjdm, null);
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
        List<WjBjz> wjdzs = wjBjzMapper.selectWjdzByBmBid(newRecord.getWjbid(), wjbm);
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
            if ("XT_FJZB".equals(thisWjdz.getWjbm())) {
                newFile.setFiletype(4);//附件
            } else {
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

            String httpHead = "http://35.0.11.40";
            String version = GlobalUtil.getGlobal("version");//查询版本
            if ("provinceTest".equals(version)) {
                //省厅测试版本
                httpHead = "http://35.0.40.36:8081";
            }

            if ("1".equals(thisWjdz.getWsgl())) {
                newFile.setServerip(httpHead + "/WordToImage/");
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
                            newFile.setServerip(httpHead + ":8080/fjupload");
                        } else {
                            newFile.setServerip(httpHead + ":8080/fjupload");
                        }
                        break;
                    } else {
                        //no   //这个是上传的word转换的
                        newFile.setServerip(httpHead + "/UploadFileToImage");
                    }
                }
            }
            funArchiveFilesDTOMapper.insert(newFile);
        }
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
        List<XtXyrxxb> suspects = xtXyrxxbMapper.selectXyrByJqbh(newCaseInfo.getJqbh());
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
            funSuspectDTOMapper.insertSelective(newSuspectDTO);
            //抽取对应文书并建立文书与人的关系
            createRecordsSuspect(newType, newSuspectDTO, newCaseInfo);
        }

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
        funCasePeoplecaseDTOMapper.insertSelective(funCasePeoplecaseDTO);

    }


    private void createRecordsSuspect(FunArchiveTypeDTO newType, FunSuspectDTO newSuspect, FunCaseInfoDTO newCaseInfo) throws Exception {
        //查询对人的

        List<XtWjflb> SusRecords = xtWjflbMapper.selectRecordBySuspect(newType.getJqbh(), newSuspect.getSuspectcode());
        int i = 0;
        for (XtWjflb thisR :
                SusRecords) {
            try {

                //查看这个文书有没有  如果有就不插文书只插关系表
                FunArchiveRecordsDTO newRecord = funArchiveRecordsDTOMapper.selectRecordsByWjMessage(thisR.getWjbm(), thisR.getWjbid(), newType.getArchiveseqid());
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
                    recordSetWhJcycode(newRecord, thisR.getWjdm(), thisR.getWjbm().toUpperCase(), thisR.getWjbid());
                    newRecord.setEffectivetime(thisR.getJlsj());
                    newRecord.setBaserecordid(0);
                    newRecord.setRecorduuid(UUID.randomUUID().toString());
                    newRecord.setWjbm(thisR.getWjbm());//文件表名
                    newRecord.setWjbid(thisR.getWjbid());//文件表id
                    funArchiveRecordsDTOMapper.insertSelective(newRecord);
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
                funSuspectRecordDTOMapper.insert(newSR);
                createFiles(newRecord);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据文件表名表idseqid查询文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/6/24 17:11
     */
    public FunArchiveRecordsDTO selectRecordsByWjMessage(String wjbm, Integer wjbid, Integer archiveseqid) {
        return funArchiveRecordsDTOMapper.selectRecordsByWjMessage(wjbm, wjbid, archiveseqid);
    }

    @Override
    public boolean deleteCaseAll(String ajbh) {

        //已发送的数量
        Integer sendedCount= funArchiveSFCDTOMapper.selectCountSended(ajbh);
        if (sendedCount>0){
            return  false;
        }else {
            DeleteCaseMapper.delFuncaseinfo(ajbh);
            DeleteCaseMapper.delFunarchivefiles(ajbh);
            DeleteCaseMapper.delFunarchiverecords( ajbh);
            DeleteCaseMapper.delFunarchivesfc( ajbh);
            DeleteCaseMapper.delFunarchiveseq( ajbh);
            DeleteCaseMapper.delFunarchivetype( ajbh);
            DeleteCaseMapper.delFunarchivefiles(ajbh);
            DeleteCaseMapper.delFuncasepeoplecase(ajbh);
            DeleteCaseMapper.delFunsuspectrecord(ajbh);
            DeleteCaseMapper.delFunsuspect(ajbh);
            return  true;
        }


    }

    @Override
    public IntegralityCheckResult integralityCheck(FunArchiveSeqDTO thisASeq) {

        IntegralityCheckResult reObj=new IntegralityCheckResult();

        StringBuilder returnMessage = new StringBuilder();
        Set<Integer> recordIdSet = new HashSet<>();//存储文书
        returnMessage.append("<h3>《" + thisASeq.getArchivename() + "》 检查结果：</h3>");
        //查询这个seq下所有没被删除的文书
        List<FunArchiveFilesDTO> fileList = this.selectFileBySeq(thisASeq.getId());
        //检测文件完整性
        Long fileSize=Long.valueOf(0);
        for (FunArchiveFilesDTO thisFile :
                fileList) {
            try {
                //完整路径
                //用于计算所有文件的大小 当文书小于200页将不会计算大小
                boolean isActive=fileList.size()>200;

                //系统文书没路径
                if (StringUtils.isNotBlank(thisFile.getFilecode()) && thisFile.getFilecode().startsWith("FZL")) {
                    continue;
                }

                //路径校验
                if (StringUtils.isBlank(thisFile.getFileurl()) || thisFile.getFileurl().endsWith("/") || "NULL".equals(thisFile.getFileurl().toUpperCase())|| "/NULL".equals(thisFile.getFileurl().toUpperCase())) {
                    //存储还有损坏图片的文书
                    recordIdSet.add(thisFile.getArchiverecordid());
                    continue;
                }

                //图片物理文件校验
                String integralityUrl = thisFile.getServerip() + thisFile.getFileurl();
                URL url = new URL(integralityUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60 * 1000);
                //不存在，可能是大小写问题
                if (404 == conn.getResponseCode() || 403 == conn.getResponseCode()) {
                    //截取后缀
                    int suffixIndex = thisFile.getFileurl().lastIndexOf(".");
                    String fileName = thisFile.getFileurl().substring(0, suffixIndex);
                    String suffix = thisFile.getFileurl().substring(suffixIndex);
                    //先小写尾缀
                    integralityUrl = thisFile.getServerip() + fileName + suffix.toLowerCase();
                    url = new URL(integralityUrl);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(60 * 1000);


                    //图片不存在或者路径不对
                    if (404 == conn.getResponseCode() || 403 == conn.getResponseCode()) {
                        //再大写尾缀
                        integralityUrl = thisFile.getServerip() + fileName + suffix.toUpperCase();
                        url = new URL(integralityUrl);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(60 * 1000);
                        if (404 == conn.getResponseCode() || 403 == conn.getResponseCode()) {
                            //大小写都不存在
                            recordIdSet.add(thisFile.getArchiverecordid());
                            isActive=false;
                        }
                    }
                }

                if (isActive){

                    System.out.println(conn.getContentLengthLong());
                    fileSize+= conn.getContentLengthLong();
                }



            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //检查文书顺序是否都为正数，负数就改为1
        this.updateWrongOrderBySeq(thisASeq.getId());
        String fileSizeMessage="您的数据包较小，能够正常发送";
        if (fileSize>( (long) (20 * 1024 *1024 ))*((long) (1024 *1024 ))){
            fileSizeMessage="你的数据包大小约为："+(fileSize/1024/1024/1024)+1+"M;打包所需视角可能较长";
        }
        if (recordIdSet.size() > 0) {
            int[] recordIdAry = recordIdSet.stream().mapToInt(Integer::intValue).toArray();
            List<String> recordNameList = this.selectRecordNameByRecordIds(recordIdAry);
            String reName = recordNameList.stream().map(String::valueOf).collect(Collectors.joining("》,《", "《", "》"));//将用户的权限转换成一个以，分割的字符串
            returnMessage.append("<p>以下文书无法通过完整性校验：</p>");
            returnMessage.append("<p>" + reName + "</p>");
            returnMessage.append("<p>" + fileSizeMessage + "</p>");
            returnMessage.append("<p style='color:red'>无法通过完整性校验的文件，请在卷整理内删除不规范的图片，重新上传</p>");


            //
            reObj.setOk(false);

        } else {
            returnMessage.append("<br/>");
            returnMessage.append("<p>图片完整性无异常</p>");
            returnMessage.append("<p>" + fileSizeMessage+ "</p>");
            reObj.setOk(true);
        }
        reObj.setMessage(returnMessage);

        return  reObj;
    }

    ;

}
