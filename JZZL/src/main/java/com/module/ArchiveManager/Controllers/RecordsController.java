package com.module.ArchiveManager.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveType;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.annotations.recordTidy;
import com.factory.BaseFactory;
import com.module.ArchiveManager.Services.RecordsService;
import com.module.SystemManagement.Services.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:44
 * @describe
 */
@Controller
@RequestMapping("/Records")
public class RecordsController extends BaseFactory {
    private final String operModul = "Records";
    private final
    RecordsService recordsService;
    private final UserService userServiceByRedis;

    @Autowired
    public RecordsController(RecordsService recordsService, @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.recordsService = recordsService;
        this.userServiceByRedis = userServiceByRedis;
    }


    @RequestMapping(value = "/getFunArchiveRecordsById", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody    @recordTidy
    @OperLog(operModul = operModul, operDesc = "通过id查询文书", operType = OperLog.type.SELECT)
    public String getFunArchiveRecordsById(Integer id) {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", recordsService.getFunCaseInfoById(id));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 分页查询文书
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/4 14:10
     */
    @RequestMapping(value = "/selectRecordsByJqbhPage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody    @recordTidy
    @OperLog(operModul = operModul, operDesc = "文书分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectRecordsByJqbhPage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            String pid = pJsonObj.getString("pid");
            if (StringUtils.isEmpty(pid)) {
                throw new Exception("缺少关键参数：pid(peoplecaseid)");
            }
            int caseInfoId = Integer.parseInt(DecodeUrlP(pid));
//            pJsonObj.put("jqbh", recordsService.getFunCaseInfoById(caseInfoId).getJqbh());
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            //查找该案件的基础卷的第0次（案宗抽取次）的整理次序id
            FunArchiveSeqDTO thisSeq = recordsService.selectBaseArchive(caseInfoId);
            pJsonObj.put("archiveseqid", thisSeq.getId());//这里查询的是未被送检的卷 所有传0
            reMap.put("rows", recordsService.selectRecordsByJqbhPage(pJsonObj));
            reMap.put("total", recordsService.selectRecordsByJqbhCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }


    @RequestMapping(value = "/getRecordCords", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody    @recordTidy
    @OperLog(operModul = operModul, operDesc = "获取对应卷的文书代码", operType = OperLog.type.SELECT)
    public String getRecordCords(Integer offset, Integer limit, String params) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject pJsonObj = JSON.parseObject(params);
            int seqId = pJsonObj.getInteger("seqid");
            FunArchiveSeqDTO thisSeq = recordsService.selectFunArchiveSeqById(seqId);
            //通过seqid查询该卷的所属类型
            pJsonObj.put("archivetype", thisSeq.getArchivetype());//文书代码
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            reValue.put("value", recordsService.selectRecordCodesByAtype(pJsonObj));
            reValue.put("count", ((recordsService.selectRecordCodesByAtypeCount(pJsonObj) + limit - 1) / limit));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 判断文书查询该案件的嫌疑人
     *
     * @param recordtypeid 文书id
     * @param seqId        seqId
     * @return |
     * @author MrLu
     * @createTime 2020/12/23 14:01
     */
    @RequestMapping(value = "/selectRecordTypeSuspectByRid", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody    @recordTidy
    @OperLog(operModul = operModul, operDesc = "判断文书查询该案件的嫌疑人", operType = OperLog.type.SELECT)
    public String selectRecordTypeSuspectByRid(Integer recordtypeid, Integer seqId) {
        JSONObject reValue = new JSONObject();
        try {
            SysRecordorderDTO thisRType = recordsService.selectSysRecordorderDTOById(recordtypeid);
            FunArchiveSeqDTO thisSeq = recordsService.selectFunArchiveSeqById(seqId);
            //判断是否选人
            if (1 == thisRType.getRecordstyle()) {
                //嫌疑人单选
                if (thisSeq.getBaserecordid() == 0) {
                    // 基础卷显示全部的人
                    reValue.put("value", recordsService.selectSuspectBySeqId(seqId));
                } else {
                    //非基础卷 只展示相关文书的人
                    //查询这个文书的相关人即可
                    reValue.put("value", recordsService.selectSuspectByRecordid(thisSeq.getBaserecordid()));
                }

            }
            //不是用来选人的就不带列表过去
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    @RequestMapping(value = "/createNewRecord", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody    @recordTidy
    @OperLog(operModul = operModul, operDesc = "新建文书", operType = OperLog.type.INSERT)
    public String createNewRecord(String record) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject newRecordJsonObj = JSON.parseObject(record);
            int seqId = newRecordJsonObj.getInteger("seqId");//seqid
            FunArchiveSeqDTO thisSeq = recordsService.selectFunArchiveSeqById(seqId);
            //查看这个文书代码应有的信息
            SysRecordorderDTO thisRecordOrder = recordsService.selectSysRecordorderDTOById(newRecordJsonObj.getInteger("sysRecordId"));
            Integer suspectId = newRecordJsonObj.getInteger("suspectId");//被选中的嫌疑人id
            SysUser userNow = userServiceByRedis.getUserNow(null);//当前用户

            //该文书不对人  查找该文书的应在位置
            //查找该文书应有顺序的上个文书的顺序  肯定会找到一个  因为最上面肯定有一个封皮和目录
            FunArchiveRecordsDTO newRecordObj = recordsService.selectPriveRecord(thisRecordOrder.getRecordtype(),
                    thisRecordOrder.getArchivetype(),
                    thisRecordOrder.getDefaultorder(),
                    seqId,
                    suspectId);
            newRecordObj.setRecordname(newRecordJsonObj.getString("recordName"));
            newRecordObj.setRecordwh(newRecordJsonObj.getString("recordWh"));//recordWh
            newRecordObj.setPrevid(0);
            newRecordObj.setAuthor(userNow.getUsername());
            newRecordObj.setAuthorid(userNow.getId());
            newRecordObj.setRecorduuid(UUID.randomUUID().toString());
            newRecordObj.setRecordstyle(thisRecordOrder.getRecordstyle());
            newRecordObj.setRecordscode(thisRecordOrder.getRecordcode());
            if (newRecordObj.getThisorder() < 1) {
                newRecordObj.setThisorder(1);
            } else {
                newRecordObj.setThisorder(newRecordObj.getThisorder() + 1);
            }

            newRecordObj.setJcyrecordcode("");//没有这个代码啊
            newRecordObj.setBaserecordid(0);
            newRecordObj.setEffectivetime(new Date());
            newRecordObj.setIsdelete(0);
            newRecordObj.setIsazxt(1);
            recordsService.insertFunArchiveRecords(newRecordObj);
            //对人文书  - 插入 fun_suspect_record 表
            //人是肯定有的   区别在于是否有相同的文书
            //有相同的文书  查询出来复制+1
            //没有相同的文书 找人

            //插入
            if (null != suspectId && suspectId > 0) {
                //插入嫌疑人文书顺序表
                FunSuspectRecordDTO suspectRecord = new FunSuspectRecordDTO();
                suspectRecord.setJqbh(newRecordObj.getJqbh());
                suspectRecord.setCaseinfoid(thisSeq.getCaseinfoid());
                suspectRecord.setAjbh(newRecordObj.getAjbh());
                suspectRecord.setSfcnumber(thisSeq.getSfcnumber());
                suspectRecord.setSuspectid(suspectId);
                suspectRecord.setRecordid(newRecordObj.getId());
                suspectRecord.setRecordtype(thisRecordOrder.getRecordtype());
                recordsService.insertSuspectRecord(suspectRecord);
            }
//查询该文书信息
            //查询该文书是否存在与这本卷  如果存在放在他后买你
            //后面的所有文书顺序+1
            recordsService.updateOrderAdd(seqId,
                    newRecordObj.getArchivetypeid()
                    , newRecordObj.getThisorder());
            reValue.put("value", newRecordObj.getId());//返回新创建的文书id
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 为树形查询基础卷的数据
     *
     * @param seqId seqid
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 20:07
     */
    @RequestMapping(value = "/selectBaseTypeForTree", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody    @recordTidy
    @OperLog(operModul = operModul, operDesc = "新建文书", operType = OperLog.type.SELECT)
    public String selectBaseTypeForTree(Integer seqId, Integer nodeId, String parentId) {
        JSONObject reValue = new JSONObject();
        try {
            //获取基础卷seqid

            FunArchiveSeqDTO baseSeq = recordsService.selectBaseArchiveBySeqId(seqId);
            JSONArray data = new JSONArray();
            if (null != baseSeq) {

                if (StringUtils.isEmpty(parentId)) {
                    List<FunArchiveType> archiveTypes = recordsService.selectArchiveTypeByJqSeq(seqId);
                    for (FunArchiveType thisType :
                            archiveTypes) {
                        JSONObject thisTypeJsonObj = new JSONObject();
                        thisTypeJsonObj.put("id", thisType.getId());
                        thisTypeJsonObj.put("title", thisType.getRecordtypecn());
                        thisTypeJsonObj.put("parentId", "0");
                        thisTypeJsonObj.put("checkArr", "0");
                        thisTypeJsonObj.put("last", false);
                        thisTypeJsonObj.put("level", "1");
                        thisTypeJsonObj.put("children", "[]");
                        //"children":[]
                        data.add(thisTypeJsonObj);
                    }

                } else if ("0".equals(parentId)) {
                    //当前情况说明为加载二级节点
                    //查询这个type下的所有文书(抛出系统文书 zl系列的)
                    for (FunArchiveRecords thisRecord :
                            recordsService.selectRecordsByTypeid(nodeId, 0,0)) {
                        JSONObject thisRecordJsonObj = new JSONObject();
                        thisRecordJsonObj.put("id", thisRecord.getId());
                        thisRecordJsonObj.put("title", thisRecord.getRecordname());
                        thisRecordJsonObj.put("parentId", thisRecord.getArchivetypeid());
                        thisRecordJsonObj.put("checkArr", "0");
                        thisRecordJsonObj.put("last", true);
                        thisRecordJsonObj.put("level", "2");
                        thisRecordJsonObj.put("children", "[]");
                        data.add(thisRecordJsonObj);
                    }
                } else {
                    //说明此处时加载基础卷下的文书图片
                    //郝哥说不要第三级了 嘤嘤嘤
                    for (FunArchiveFilesDTO thisFile :
                            recordsService.selectRecordFilesByRecordId(nodeId, 0)) {
                        JSONObject thisRecordJsonObj = new JSONObject();
                        thisRecordJsonObj.put("id", thisFile.getId());
                        thisRecordJsonObj.put("title", thisFile.getFilename());
                        thisRecordJsonObj.put("parentId", thisFile.getArchiverecordid());
                        thisRecordJsonObj.put("checkArr", "0");
                        thisRecordJsonObj.put("last", true);
                        thisRecordJsonObj.put("level", "3");
                        data.add(thisRecordJsonObj);
                    }

                }

                reValue.put("data", data);
                reValue.put("code", "0");
            } else {
                throw new Exception("莫得基础卷啊");
            }

        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("code", "1");
        }
        return reValue.toJSONString();
    }
}
