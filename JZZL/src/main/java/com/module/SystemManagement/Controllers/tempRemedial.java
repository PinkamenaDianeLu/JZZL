package com.module.SystemManagement.Controllers;

import com.ZfbaETL.BaseServer.BaseServer;
import com.ZfbaETL.UpdateOnTime.Server.UpdateOnTimeServer;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.zfba.SysUserRole;
import com.bean.zfba.XtWjflb;
import com.bean.zfba.XtXyrxxb;
import com.factory.BaseFactory;
import com.module.SystemManagement.Services.TempRemedialService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author MrLu
 * @createTime 2021年11月30日14:18:59
 * @describe 临时补救没有整理的案件或是开具后没有抽过来的文书
 */
@Controller
@RequestMapping("/tempRemedial")
public class tempRemedial extends BaseFactory {

    @Autowired
    private BaseServer baseServer;
    @Autowired
    private UpdateOnTimeServer updateOnTimeServer;
    @Autowired
    private TempRemedialService tempRemedialService;


    @RequestMapping(value = "/supplementaryRecords", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String supplementaryRecords(String ajbh) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isBlank(ajbh)) {
                throw new Exception("案件编号为空");
            }
            //案件编号
            List<XtWjflb> newRecords = updateOnTimeServer.selectNewRecordsForAjbh(ajbh);//新建的/有修改的文书

//            tempRemedialService.selectFileBySeq();
            if (null != newRecords && newRecords.size() > 0) {

                supplementaryRecords(newRecords);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 文书同步
     *
     * @param ajid 案件id
     * @return |
     * @author MrLu
     * @createTime 2021/12/21 9:45
     */
    @RequestMapping(value = "/supplementaryRecordsByAjid", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String supplementaryRecordsByAjid(String ajid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isBlank(ajid)) {
                throw new Exception("案件id为空");
            }
            int caseInfoId = Integer.parseInt(DecodeUrlP(ajid));

            FunCaseInfoDTO thisCase = updateOnTimeServer.selectCaseInfoById(caseInfoId);
            if (null == thisCase || null == thisCase.getId()) {
                throw new Exception("案件id找不到对应案件");
            }
            //案件编号
            List<XtWjflb> newRecords = updateOnTimeServer.selectNewRecordsForAjbh(thisCase.getAjbh());//新建的/有修改的文书
            if (null != newRecords && newRecords.size() > 0) {
                supplementaryRecords(newRecords);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 完整性检查
     *
     * @param ajid 案件id
     * @return |
     * @author MrLu
     * @createTime 2021/12/21 9:45
     */
    @RequestMapping(value = "/integralityCheck", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String integralityCheck(String ajid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isBlank(ajid)) {
                throw new Exception("案件id为空");
            }
            int caseInfoId = Integer.parseInt(DecodeUrlP(ajid));

            List<FunArchiveSeqDTO> archiveSeqList = tempRemedialService.selectActiveSeqByCaseInfoId(caseInfoId);
            if (null == archiveSeqList || archiveSeqList.size() <= 0) {
                throw new Exception("案件id找不到对应整理记录");
            }

            StringBuilder returnMessage = new StringBuilder();
            //循环这些记录  然后挨个判断
            for (FunArchiveSeqDTO thisASeq :
                    archiveSeqList) {
                returnMessage.append(tempRemedialService.integralityCheck(thisASeq).getMessage());

            }
            reValue.put("value", returnMessage.toString());
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    //真正的整理程序
    private void supplementaryRecords(List<XtWjflb> newRecords) throws Exception {
        List<FunArchiveSeqDTO> seqs = updateOnTimeServer.selectSeqByJqbh(newRecords.get(0).getJqbh());
        for (XtWjflb thisRecord :
                newRecords) {
            String wjbm = thisRecord.getWjbm();
            Integer wjbid = thisRecord.getWjbid();

            if (thisRecord.getScbj() != 0) {
                //删除文书 (被删除的文书会在回收站)
                updateOnTimeServer.deleteRecordsByWjMessageNotSend(wjbm, wjbid);
            } else {
                //新增


                //1.查询fun_archive_records中文件表名表id有没有  没有新加  有就不用管了
//                List<FunArchiveRecordsDTO> records = updateOnTimeServer.selectRecordsByWjMessageNotSend(wjbm, wjbid);
                if (true) {
                    //没有以其它途径出现过  新增进去

                    //这些是需要添加新文书的seq
                    //查新新的这个文书应该出现的位置
                    for (FunArchiveSeqDTO thisSeq :
                            seqs) {
                        //判断文书是否已经存在
                        FunArchiveRecordsDTO record = tempRemedialService.selectRecordsByWjMessage(wjbm, wjbid, thisSeq.getId());
                        if (null != record) {
                            continue;
                        }
                        //查询该文书应该出现的位置
                        SysRecordorderDTO thisRecordOrder = updateOnTimeServer.selectRecordOrderByTypes(thisRecord.getWjdm(), thisSeq.getArchivetype());

                        if (null == thisRecordOrder) {
                            //在需要的配置中没有这个文书 说明它是不被需要的存在
                            continue;
                        }
                        //查询对应位置的type
                        FunArchiveTypeDTO type = updateOnTimeServer.selectTypeBySeqType(thisSeq.getId(), thisRecordOrder.getRecordtype());
                        //制作一个空的案件对象以供保存文书方法用
                        FunCaseInfoDTO FunCaseInfoDTO = new FunCaseInfoDTO();
                        FunCaseInfoDTO.setId(thisSeq.getCaseinfoid());
                        FunCaseInfoDTO.setSfcnumber(thisSeq.getSfcnumber());
                        //判断文书是否对人
                        if ((thisRecordOrder.getRecordstyle() == 1 || thisRecordOrder.getRecordstyle() == 7 || thisRecordOrder.getRecordstyle() == 9) && StringUtils.isNotEmpty(thisRecord.getDydm())) {
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
                                        Integer order = Optional.of(updateOnTimeServer.selectRsMaxOrderByTypeid(type.getId()) + 1).orElse(3);
                                        baseServer.createRecordsSuspect(type, suspect, FunCaseInfoDTO, order + 1, thisRecord);

                                    } else {
                                        baseServer.createRecordsSuspect(type, suspect, FunCaseInfoDTO, newRecordObj.getThisorder() + 1, thisRecord);

                                    }

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
                                        baseServer.createRecordsNoSuspect(type, order, thisRecord);

                                    }
                                    updateOnTimeServer.updateOrderAdd(type.getArchiveseqid(),
                                            type.getId()
                                            , order);
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
                                continue;
                            }
                            int order = 1;
                            if (newRecordObj.getThisorder() > 1) {
                                order = newRecordObj.getThisorder() + 1;
                            }
                            baseServer.createRecordsNoSuspect(type, order, thisRecord);
                            //后面的所有文书顺序+1
                            updateOnTimeServer.updateOrderAdd(type.getArchiveseqid(),
                                    type.getId()
                                    , order);
                        }

                    }

                }
            }
        }
    }


    /**
     * 重新抽取
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/12/29 16:09
     */
    @RequestMapping(value = "/reorganizeArchive", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String reorganizeArchive(String jqbh) {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", tempRemedialService.extractCase(jqbh));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 删除案件的所有信息
     *
     * @param ajbh 案件编号
     * @return |
     * @author MrLu
     * @createTime 2022/3/16 14:50
     */
    @RequestMapping(value = "/deleteCaseAll", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String deleteCaseAll(String ajbh) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isBlank(ajbh)) {
                reValue.put("message", "无效的案件编号");
            } else {
                if (tempRemedialService.deleteCaseAll(ajbh)) {
                    reValue.put("message", "success");
                } else {
                    //有已发送的文书 不能删
                    reValue.put("message", "cannot");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

}
