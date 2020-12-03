package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveSeq;
import com.bean.jzgl.Source.FunArchiveType;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.ArrangeArchivesService;
import com.module.SystemManagement.Services.UserService;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/10/8 15:37
 * @describe 卷整理
 */
@Controller
@RequestMapping("/ArrangeArchives")
public class ArrangeArchivesController extends BaseFactory {
    private final String operModul = "ArrangeArchives";
    private final ArrangeArchivesService arrangeArchivesService;
    private final UserService userServiceByRedis;

    @Autowired
    public ArrangeArchivesController(ArrangeArchivesService arrangeArchivesService, @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.arrangeArchivesService = arrangeArchivesService;
        this.userServiceByRedis = userServiceByRedis;
    }

    /**
     * 根据送检id查询最后一次整理记录和送检卷信息
     *
     * @param id 送检id
     * @return |
     * @author MrLu
     * @createTime 2020/10/11 16:05
     */
    @RequestMapping(value = "/selectLastSeqSfcBySfc", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "根据送检id查询最后一次整理记录和送检卷信息", operType = OperLog.type.SELECT)
    public String selectLastSeqSfcBySfc(String id) {
        JSONObject reValue = new JSONObject();
        try {
            int sfcId = Integer.parseInt(DecodeUrlP(id));
            JSONObject reV=new JSONObject();
            reV.put("sfc", arrangeArchivesService.selectFunArchiveSFCById(sfcId));
            reV.put("seq", arrangeArchivesService.selectLastSeqBySfc(sfcId));
            reValue.put("value",reV);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 通过送检记录id查询文书目录
     *
     * @param id 送检记录id
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/9 9:50
     */
    @RequestMapping(value = "/getArchiveIndex", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "得到文书目录", operType = OperLog.type.SELECT)
    public String getArchiveIndex(String id) {
        JSONObject reValue = new JSONObject();
        try {
            int seqId = Integer.parseInt(id);
            //查询本次送检的所有文书类型
            List<FunArchiveType> archiveTypes = arrangeArchivesService.selectArchiveTypeByJqSeq(seqId);
            reValue.put("value", archiveTypes);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 通过typeId查询该卷下的文书目录
     *
     * @param id       typeId 未加密
     * @param isDelete 是否删除 0已删除 1未删除
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/11 13:58
     */
    @RequestMapping(value = "/getRecordsIndex", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "得到文书目录(具体文书)", operType = OperLog.type.SELECT)
    public String getRecordsIndex(String id, String isDelete) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(id)) {
                throw new Exception("你传nm呢？");
            }
            int isdelete = Integer.parseInt(isDelete);
            JSONArray records = new JSONArray();
            for (FunArchiveRecords thisRecord :
                    arrangeArchivesService.selectRecordsByTypeid(Integer.parseInt(id), isdelete)) {
                JSONObject record = new JSONObject();
                record.put("record", thisRecord);
                record.put("files", arrangeArchivesService.selectRecordFilesByRecordId(thisRecord.getId(), isdelete));
                records.add(record);
            }
            reValue.put("value", records);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 查询文书下的所有文件
     *
     * @param recordid 文书id
     * @return |
     * @author MrLu
     * @createTime 2020/11/7 21:45
     */
    @RequestMapping(value = "/selectFilesByRecordId", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询文书下的所有文件", operType = OperLog.type.SELECT)
    public String selectFilesByRecordId(String recordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(recordid)) {
                throw new Exception("你传nm呢？");
            }
            reValue.put("value", arrangeArchivesService.selectRecordFilesByRecordId(Integer.parseInt(recordid), 0));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 新建整理记录
     *
     * @param seqid 这一次整理记录id
     * @return String （JSONObject）| 带有新建整理记录的id
     * @author MrLu
     * @createTime 2020/10/13 17:03
     */
    @RequestMapping(value = "/createNewSeq", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "保存卷整理顺序", operType = OperLog.type.INSERT)
    public String createNewSeq(Integer seqid) {
        JSONObject reValue = new JSONObject();
        try {
            if (null == seqid) {
                throw new Exception("你传nm呢？seqid：" + seqid);
            }
            FunArchiveSeqDTO thisSeq = arrangeArchivesService.selectFunArchiveSeqById(seqid);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            // 新建整理记录
            thisSeq.setBatchesseq(thisSeq.getBatchesseq() + 1);//原有记录次序+1
            thisSeq.setAuthor(userNow.getXm());//姓名
            thisSeq.setAuthoridcard(userNow.getIdcardnumber());//身份证号
            thisSeq.setAuthorid(userNow.getId());//人员表id
            //插入
            arrangeArchivesService.insertFunArchiveSeq(thisSeq);
            reValue.put("value", thisSeq.getId());
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * @param saveData 一个json对象数组 对象格式为： {id, name, typeid, order}
     * @param typeid   文书类型id（原有的）
     * @param seqid    整理次序id（新建的）
     * @return |
     * @author MrLu
     * @createTime 2020/10/13 16:42
     */
    @RequestMapping(value = "/saveArchiveIndexSortByType", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "保存卷整理顺序", operType = OperLog.type.INSERT)
    public String saveArchiveIndexSortByType(String saveData, String typeid, String seqid) {
        JSONObject reValue = new JSONObject();
        try {
            FunArchiveSeqDTO thisSeq = arrangeArchivesService.selectFunArchiveSeqById(Integer.parseInt(seqid));
            if (null == thisSeq) {
                throw new Exception("该整理次序id无法查询到对应信息！");
            }
            int oriTypeId = Integer.parseInt(typeid);//原类型id
            FunArchiveTypeDTO oriArchiveType = arrangeArchivesService.selectFunArchiveTypeById(oriTypeId);
            if (null == oriArchiveType) {
                throw new Exception("该文书类型id无法查询到对应信息！");
            }
            oriArchiveType.setArchivesfcid(thisSeq.getArchivesfcid());//送检卷id
            oriArchiveType.setArchiveseqid(thisSeq.getId());//设置新的整理次序
            arrangeArchivesService.insertFunArchiveType(oriArchiveType);//新建卷宗类型
            int newArchiveTypeId = oriArchiveType.getId();//新建的卷类型id


            JSONArray pDate = JSON.parseArray(saveData);
            //开始复制文书信息
            for (Object thisP :
                    pDate) {
                JSONObject thisJsonObj = (JSONObject) thisP;
                //根据id获取文书
                FunArchiveRecordsDTO thisRecord = arrangeArchivesService.selectFunArchiveRecordsById(thisJsonObj.getInteger("id"));
                thisRecord.setRecordname(thisJsonObj.getString("name"));//新的文书名
                thisRecord.setThisorder(thisJsonObj.getInteger("order"));//新的顺序
                thisRecord.setArchivetypeid(newArchiveTypeId);//类型id
                thisRecord.setArchiveseqid(thisSeq.getId());//seqId
                thisRecord.setPrevid(thisRecord.getId());//保存原有id
                thisRecord.setIsdelete(0);//未删除
                thisRecord.setArchivesfcid(thisSeq.getArchivesfcid());//送检（新建卷）次序id
                arrangeArchivesService.insertFunArchiveRecords(thisRecord);//保存新建
                //开始复制文书下的文件
                String fileCodes[] = thisJsonObj.getString("filecodes").split(",");//文书下的文件变为数组
                for (String thisFileCode :
                        fileCodes) {
                    if (StringUtils.isEmpty(thisFileCode)) {
                        continue;
                    }
                    FunArchiveFilesDTO oriFile = arrangeArchivesService.selectFilesByFileCode(thisFileCode, thisRecord.getPrevid());
                    oriFile.setArchiverecordid(thisRecord.getId());//文书id
                    oriFile.setArchiveseqid(thisRecord.getArchiveseqid());//整理次序id
                    oriFile.setArchivesfcid(thisRecord.getArchivesfcid());//送检卷id
                    oriFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
                    oriFile.setIsshow(0);
                    oriFile.setIsdelete(0);
                    arrangeArchivesService.insertFunArchiveFilesDTO(oriFile);
                }
            }
            reValue.put("value", newArchiveTypeId);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 保存卷整理顺序中被删除的文书
     *
     * @param saveData  一个json对象数组 对象格式为： {id, name, typeid, order}
     * @param newTypeid 文书类型id（新建的）
     * @return |
     * @author MrLu
     * @createTime 2020/10/14 16:25
     */
    @RequestMapping(value = "/saveRecycleIndexSortByType", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "保存卷整理顺序中被删除的文书", operType = OperLog.type.INSERT)
    public String saveRecycleIndexSortByType(String saveData, String newTypeid) {
        JSONObject reValue = new JSONObject();
        try {
            int typeId = Integer.parseInt(newTypeid);//原类型id
            FunArchiveTypeDTO archiveType = arrangeArchivesService.selectFunArchiveTypeById(typeId);
            if (null == archiveType) {
                throw new Exception("该文书类型id无法查询到对应信息！");
            }
            JSONArray pDate = JSON.parseArray(saveData);
            //开始复制文书信息
            for (Object thisP :
                    pDate) {
                JSONObject thisJsonObj = (JSONObject) thisP;
                int rId = thisJsonObj.getInteger("id");
                //根据id获取文书
                FunArchiveRecordsDTO thisRecord = arrangeArchivesService.selectNextRecord(rId);
                if (1 == thisJsonObj.getInteger("isdelete")) {
                    //未完全删除  更新对应状态 此时更新的是在saveArchiveIndexSortByType中新建完的文书
                    thisRecord.setIsdelete(1);
                    arrangeArchivesService.updateFunArchiveRecordsById(thisRecord);
                } else {
                    //完全删除 新建文书为删除
                    thisRecord = arrangeArchivesService.selectFunArchiveRecordsById(rId);
                    thisRecord.setIsdelete(2);//这是被删除的卷
                    thisRecord.setRecordname(thisJsonObj.getString("name"));//新的文书名
                    thisRecord.setThisorder(thisJsonObj.getInteger("order"));//新的顺序
                    thisRecord.setArchivetypeid(typeId);//类型id
                    thisRecord.setPrevid(thisRecord.getId());
                    thisRecord.setArchiveseqid(archiveType.getArchiveseqid());//seqId
                    thisRecord.setArchivesfcid(archiveType.getArchivesfcid());//送检（新建卷）次序id
                    arrangeArchivesService.insertFunArchiveRecords(thisRecord);//保存新建
                }
                //开始复制文书下的文件
                String fileCodes[] = thisJsonObj.getString("filecodes").split(",");//文书下的文件变为数组
                for (String thisFileCode :
                        fileCodes) {
                    FunArchiveFilesDTO oriFile = arrangeArchivesService.selectFilesByFileCode(thisFileCode, thisRecord.getPrevid());
                    oriFile.setArchiverecordid(thisRecord.getId());//文书id
                    oriFile.setArchiveseqid(thisRecord.getArchiveseqid());//整理次序id
                    oriFile.setArchivesfcid(thisRecord.getArchivesfcid());//送检卷id
                    oriFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
                    oriFile.setIsshow(0);
                    oriFile.setIsdelete(1);
                    arrangeArchivesService.insertFunArchiveFilesDTO(oriFile);
                }

            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 按照文书代码的顺序查询可看的文件
     *
     * @param fileOrder 文件代码数组
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/15 18:17
     */
    @RequestMapping(value = "/loadFilesByFileCodes", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "按照文书代码按顺序查询文书列表", operType = OperLog.type.SELECT)
    public String loadFilesByFileCodes(String fileOrder, String seqId) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileOrder) || StringUtils.isEmpty(seqId)) {
                throw new Exception("给一个? 自己体会");
            }
            String[] fileOrders = fileOrder.split(",");
            //该文书没有图片了
            reValue.put("value", arrangeArchivesService.selectRecordFilesByFileCodes(fileOrders, Integer.parseInt(seqId)));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 通过文件代码查询文件
     *
     * @param filecode 文件代码
     * @return String |
     * @author MrLu
     * @createTime 2020/10/22 9:29
     */
    @RequestMapping(value = "/loadFilesByFileCode", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "通过文件代码查询文件", operType = OperLog.type.SELECT)
    public String loadFilesByFileCode(String filecode, String recordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isNotEmpty(filecode) || StringUtils.isNotEmpty(recordid)) {
                reValue.put("value", arrangeArchivesService.selectFilesByFileCode(filecode, StringUtil.StringToInteger(recordid)));
                reValue.put("message", "success");
            } else {
                throw new Exception("你传n\uD83D\uDC34呢？文书代码呢？");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 根据文书id生成文书并加载全部文件
     *
     * @param recordid 文书id
     * @return |
     * @author MrLu
     * @createTime 2020/10/26 14:58
     */
    @RequestMapping(value = "/createRecordByRecordId", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "根据文书id生成文书并加载全部文件", operType = OperLog.type.SELECT)
    public String createRecordByRecordId(String recordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(recordid)) {
                throw new Exception("你传nm呢？");
            }
            JSONObject record = new JSONObject();
            int recordId = Integer.parseInt(recordid);
            record.put("record", arrangeArchivesService.selectFunArchiveRecordsById(recordId));//加载文书
            record.put("files", arrangeArchivesService.selectRecordFilesByRecordId(recordId, null));
            reValue.put("value", record);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 实时保存移动的顺序
     *
     * @param paramjson {recordid:文书id 必填,typeid:文书类型id 必填,seqId:整理次序id 必填 ,filecode:当移动的是文件时传入的文件代码}
     * @return |
     * @author MrLu
     * @createTime 2020/11/2 18:03
     */
    @RequestMapping(value = "/saveDateOnTime", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "实时保存移动的顺序", operType = OperLog.type.UPDATE)
    public String saveDateOnTime(String paramjson) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject paramObj = (JSONObject) JSONObject.parse(paramjson);
            int recordId = paramObj.getInteger("recordid");//文书id
            int seqId = paramObj.getInteger("seqId");//整理次序id
            int order = 0;//被移动到的顺序
            int typeId = StringUtil.StringToInteger(paramObj.getString("typeid"));//送检类型id

            String fileCode = paramObj.getString("filecode");
            if (StringUtils.isNotEmpty(fileCode)) {

                String prevFileCode = paramObj.getString("prevId");//上一个文件的文书代码
                if (StringUtils.isNotEmpty(prevFileCode)) {
                    FunArchiveFilesDTO prevFile = arrangeArchivesService.selectFilesByFileCode(prevFileCode, recordId);
                    order = prevFile.getThisorder() + 1;
                } else {
                    order = 1;
                }
                //更新文件
                FunArchiveFilesDTO fileDto = new FunArchiveFilesDTO();
                fileDto.setArchivetypeid(typeId);//文书类型id
                fileDto.setArchiverecordid(recordId);//对应的文书id
                fileDto.setThisorder(order);
                //where 条件
                fileDto.setFilecode(fileCode);
                fileDto.setArchiveseqid(seqId);
                arrangeArchivesService.updateFileByFileCode(fileDto);
                //更新后面的顺序
                arrangeArchivesService.updateFileOrder(recordId, order, fileCode);
            } else {
                //更新文书
                Integer prevId = StringUtil.StringToInteger(paramObj.getString("prevId"));//上一个文书的id
                FunArchiveRecordsDTO recordsDTO = new FunArchiveRecordsDTO();
                if (null != prevId) {
                    FunArchiveRecordsDTO prevRecord = arrangeArchivesService.selectFunArchiveRecordsById(prevId);//获取上一个文书以供定位
                    order = prevRecord.getThisorder() + 1;
                } else {
                    order = 1;
                }
                recordsDTO.setThisorder(order);
                recordsDTO.setId(recordId);
                recordsDTO.setArchivetypeid(typeId);
                //更改该文书的信息
                arrangeArchivesService.updateFunArchiveRecordsById(recordsDTO);
                //更新文书下的文件
                FunArchiveFilesDTO thisFileDto = new FunArchiveFilesDTO();
                thisFileDto.setArchivetypeid(typeId);
                thisFileDto.setArchiverecordid(recordId);
                //更新
                arrangeArchivesService.updateFileByRecordId(thisFileDto);
                //更新对应文书类型的顺序
                arrangeArchivesService.updateRecordOrderByTypeId(typeId, recordId, order);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 实时删除
     *
     * @param paramjson
     * @return String  |
     * @author MrLu
     * @createTime 2020/11/5 10:35
     */
    @RequestMapping(value = "/saveDeleteDateOnTime", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "实时保存对文书文件的删除", operType = OperLog.type.UPDATE)
    public String saveDeleteDateOnTime(String paramjson) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject paramObj = (JSONObject) JSONObject.parse(paramjson);
            Integer recordId = StringUtil.StringToInteger(paramObj.getString("recordid"));
            if (null == recordId) {
                throw new Exception("recordId没给传！！！");
            }
            int seqId = paramObj.getInteger("seqId");//整理次序id
            String fileCode = paramObj.getString("filecode");
            //要被操作的文书对象
            FunArchiveRecordsDTO thisRecord = new FunArchiveRecordsDTO();
            thisRecord.setId(recordId);
            //要被操作的文件对象
            FunArchiveFilesDTO thisFile = new FunArchiveFilesDTO();
            if (StringUtils.isNotEmpty(fileCode)) {
                //删除文件

                thisFile.setIsdelete(1);
                //where 条件
                thisFile.setFilecode(fileCode);
                thisFile.setArchiveseqid(seqId);
                arrangeArchivesService.updateFileByFileCode(thisFile);
                //更新对应文书的isdelet为1： 有文件删除
                thisRecord.setIsdelete(1);

            } else {
                //删除文书
                //文书isdelete为2 ：文书全部删除
                thisRecord.setIsdelete(2);
                //文书内的所有文件删除
                thisFile.setArchiverecordid(recordId);
                thisFile.setIsdelete(1);
                arrangeArchivesService.updateFileByRecordId(thisFile);
            }
            //更新文书
            arrangeArchivesService.updateFunArchiveRecordsById(thisRecord);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 实时还原被删除的文书文件
     *
     * @param paramjson
     * @return String |
     * @author MrLu
     * @createTime 2020/11/5 15:15
     */
    @RequestMapping(value = "/saveRestoreDateOnTime", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "实时还原被删除的文书文件", operType = OperLog.type.UPDATE)
    public String saveRestoreDateOnTime(String paramjson) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject paramObj = (JSONObject) JSONObject.parse(paramjson);
            Integer recordId = StringUtil.StringToInteger(paramObj.getString("recordid"));
            if (null == recordId) {
                throw new Exception("recordId没给传！！！");
            }
            int seqId = paramObj.getInteger("seqId");//整理次序id
            String fileCode = paramObj.getString("filecode");
            //要被操作的文书对象
            FunArchiveRecordsDTO thisRecord = new FunArchiveRecordsDTO();
            thisRecord.setId(recordId);
            //还原后的位置
            int maxOrder = 1;
            //要被操作的文件对象
            FunArchiveFilesDTO thisFile = new FunArchiveFilesDTO();
            if (StringUtils.isNotEmpty(fileCode)) {
                //还原文件

                thisFile.setIsdelete(0);
                //where 条件
                thisFile.setFilecode(fileCode);
                thisFile.setArchiveseqid(seqId);

                arrangeArchivesService.updateFileByFileCode(thisFile);
                //更新对应文书的isdelet为1： 有文件删除
                thisRecord.setIsdelete(1);
                //还原至顺序的最后一位
                maxOrder = arrangeArchivesService.selectFileMaxOrder(recordId) + 1;
                thisRecord.setArchiveseqid(maxOrder);
            } else {
                //还原文书
                //文书isdelete为0 ：没有文件被删除
                thisRecord.setIsdelete(0);
                //文书内的所有文件还原
                thisFile.setArchiverecordid(recordId);
                thisFile.setIsdelete(0);
                arrangeArchivesService.updateFileByRecordId(thisFile);
                //还原后在文书中的最后一位
                maxOrder = arrangeArchivesService.selectRecordMaxOrder(recordId) + 1;
                thisRecord.setThisorder(maxOrder);
            }
            //更新文书
            arrangeArchivesService.updateFunArchiveRecordsById(thisRecord);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 实时重命名
     *
     * @param paramjson
     * @return |
     * @author MrLu
     * @createTime 2020/11/5 11:03
     */
    @RequestMapping(value = "/saveReNameDateOnTime", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "实时重命名文书文件", operType = OperLog.type.UPDATE)
    public String saveReNameDateOnTime(String paramjson) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject paramObj = (JSONObject) JSONObject.parse(paramjson);
            int seqId = paramObj.getInteger("seqId");//整理次序id
            String name = paramObj.getString("rename");//更新的名
            String fileCode = paramObj.getString("filecode");

            if (StringUtils.isEmpty(fileCode)) {
                //更新文书
                Integer recordId = StringUtil.StringToInteger(paramObj.getString("recordid"));
                FunArchiveRecordsDTO thisRecord = new FunArchiveRecordsDTO();
                thisRecord.setRecordname(name);
                thisRecord.setId(recordId);
                arrangeArchivesService.updateFunArchiveRecordsById(thisRecord);
            } else {

                FunArchiveFilesDTO thisFile = new FunArchiveFilesDTO();
                thisFile.setFilecode(fileCode);
                thisFile.setArchiveseqid(seqId);
                thisFile.setFilename(name);
                arrangeArchivesService.updateFileByFileCode(thisFile);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 通过sfcid查询案件下的所有嫌疑人
     *
     * @param baserecordid
     * @return String  |
     * @author MrLu
     * @createTime 2020/11/22 18:33
     */
    @RequestMapping(value = "/selectSuspectByBaserecordId", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "通过baserecordid查询该文书对应的所有嫌疑人", operType = OperLog.type.SELECT)
    public String selectSuspectByBaserecordId(String baserecordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(baserecordid)) {
                throw new Exception("没有baserecordid");
            }
            List<FunSuspectRecordDTO> suspects = arrangeArchivesService.selectSuspectByRecordid(Integer.parseInt(baserecordid));
            reValue.put("value", suspects);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 查询案件活跃的基础卷内的所有卷类型
     *
     * @param caseinfoid 案件表id
     * @return |
     * @author MrLu
     * @createTime 2020/11/27 9:32
     */
    @RequestMapping(value = "/selectBaseTypes", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询案件活跃的基础卷内的所有卷类型", operType = OperLog.type.SELECT)
    public String selectBaseTypes(String caseinfoid) {
        JSONObject reValue = new JSONObject();
        try {
            FunArchiveSeqDTO baseSeq = arrangeArchivesService.selectActiveSeqByCaseId(Integer.parseInt(caseinfoid));
            reValue.put("value", arrangeArchivesService.selectArchiveTypeByJqSeq(baseSeq.getId()));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


     /**
     * 更新送检卷是否已为人排序字段
     * @author MrLu
     * @param issuspectorder 是否已被排序 0否 1是
      *  @param sfcid sfc表id
     * @createTime  2020/12/3 10:09
     * @return    |
      */
    @RequestMapping(value = "/updateArchiveSfcIssuspectorder", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "更新送检卷是否已为人排序字段", operType = OperLog.type.SELECT)
    public String updateArchiveSfcIssuspectorder(String issuspectorder, String sfcid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(issuspectorder) && StringUtils.isEmpty(sfcid)) {
                throw new Exception("你传尼玛呢?????????????");
            }
            //更新其状态为已对嫌疑人排过序了
            int sfcId = Integer.parseInt(DecodeUrlP(sfcid));
            FunArchiveSFCDTO updateFunArchiveSFC = new FunArchiveSFCDTO();
            updateFunArchiveSFC.setId(sfcId);
            updateFunArchiveSFC.setIssuspectorder(Integer.parseInt(issuspectorder));
            arrangeArchivesService.updateArchiveSfcById(updateFunArchiveSFC);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 根据嫌疑人顺序生成卷
     *    //查找基础卷id select max(id) from fun_archive_seq where scbj=0 and archivetype=0 and caseinfoid=
     *                 //得到基础卷中对应类型的文书  select * from fun_archive_type where scbj=0 and seqid=
     *                 //返回ajax 开始进度条 回收站*6+普通*6
     *                 //ajax后台  新建卷类型 查找卷类型的所有文书  order by defaultorder   ok
     *                 // 循环文书  查找文书对应在 sys_recordorder 中 是否存在 select * from sys_recordorder where recordcode=? and recordtype=? and archivetype=?
     *                 //不存在 无视该文书  存在 判断该文书是否对人 不对人 -> 正常复制
     *                 //对人的 开始统一插入所有对人的文书
     *                 // 循环人  人 + 类型  查找所有对单个人的文书 foreach(suspectid){select * from fun_suspect_record where suspectid=? and recordtype=? and recordstyle=1}
     *                 // 对人的文书插入结束之后 开始继续插入  此时判断该文书是否对人 只插入不对人的文书
     *                 //完成 返回
     *
     *                 //基础卷挪动时 子卷不会将对文书的文书插入到对人文书的中间
     * @param suspectorder 嫌疑人顺序数组
     * @param seqid        整理次序id
     * @return String |
     * @author MrLu
     * @createTime 2020/11/26 16:38
     */
    @RequestMapping(value = "/createArchiveBySuspectOrder", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "根据嫌疑人顺序生成卷", operType = OperLog.type.SELECT)
    public String createArchiveBySuspectOrder(String suspectorder, String seqid, String recordtypeid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(seqid)) {
                throw new Exception("?");
            }
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            int recordTypeId = Integer.parseInt(recordtypeid);//基础卷的 文书类型id
            int seqId = Integer.parseInt(seqid);
            //查询新的送检卷类型
            FunArchiveSeqDTO thisFunArchiveSeq = arrangeArchivesService.selectFunArchiveSeqById(seqId);
            //得到该卷类型详细信息
            FunArchiveTypeDTO thisArchiveType = arrangeArchivesService.selectFunArchiveTypeById(recordTypeId);//这里的recordTypeId为基础卷的
            //查询卷类型中所有的文书  注意这行要紧贴着查询  否在在插入后id就会发生改变  注意此时的recordTypeId为基础卷的
            List<FunArchiveRecordsDTO> Records = arrangeArchivesService.selectRecordsDtoByTypeid(recordTypeId, null);
            int oriSeqId = thisArchiveType.getArchiveseqid();//原有的（基础卷）整理次序id
            //创建新的卷类型
            thisArchiveType.setArchivesfcid(thisFunArchiveSeq.getArchivesfcid());
            thisArchiveType.setArchiveseqid(thisFunArchiveSeq.getId());
            thisArchiveType.setArchiveseqid(seqId);
            thisArchiveType.setIsazxt(1);//不是案宗来的了
            arrangeArchivesService.insertFunArchiveType(thisArchiveType);
            int newRecordTypeId = thisArchiveType.getId();

            int recordOrder = 1;
            boolean peopleFinish = true;//对人文书是否已经集体完成
            for (FunArchiveRecordsDTO thisRecord : Records) {
                // 循环文书  查找文书对应在 sys_recordorder 中 是否存在
                //查询这个文书的在文书信息表中的信息
                SysRecordorderDTO thisRo =
                        arrangeArchivesService.selectRecordOrderByTypes(thisRecord.getRecordscode(),
                                thisFunArchiveSeq.getArchivetype(),
                                thisArchiveType.getArchivetype());

                //当全部对人文书插入完毕后 在遇到对人的文书就可以直接跳过了
                if (null != thisRo) {
                    boolean toPeople = (1 == thisRo.getRecordstyle());
                    if (toPeople) {
                        if (!peopleFinish){continue;//当peopleFinish为false时  此文书已经录入过了 无需在录入
                             }
                        //该文书对嫌疑人单选
                        //插入按照顺序插入全部对人文书
                        String[] suspectOrder = suspectorder.split(",");
                        for (String thisSuspectId :
                                suspectOrder) {
                            //查找这个嫌疑人在当前卷类型下的所有文书
                            List<FunArchiveRecordsDTO> records =
                                    arrangeArchivesService.selectRecordsBySuspect(StringUtil.StringToInteger(thisSuspectId),
                                            thisArchiveType.getArchivetype(),
                                            oriSeqId, thisFunArchiveSeq.getArchivetype());
                            System.out.println("嫌疑人文书数："+records.size());
                            for (FunArchiveRecordsDTO thisSuspectRecord :
                                    records) {
                                int oriRecordId = thisSuspectRecord.getId();
                                thisSuspectRecord.setPrevid(oriRecordId);//基于什么变得
                                thisSuspectRecord.setArchiveseqid(seqId);//整理次序id
                                thisSuspectRecord.setArchivetypeid(newRecordTypeId);//文书类型id
                                thisSuspectRecord.setArchivesfcid(thisFunArchiveSeq.getArchivesfcid());
                                thisSuspectRecord.setThisorder(recordOrder++);//文书顺序
                                thisSuspectRecord.setAuthorid(userNow.getId());
                                thisSuspectRecord.setAuthor(userNow.getXm());
                                //得到文书代码的顺序
                                copyRecordToNew(oriRecordId, thisSuspectRecord);
                            }

                        }
                        peopleFinish = false;//对人的都插入完了  就再不用插入了

                    } else {
                        //对文书
                        int oriRecordId = thisRecord.getId();
                        thisRecord.setPrevid(oriRecordId);//基于什么变得
                        thisRecord.setArchiveseqid(seqId);//整理次序id
                        thisRecord.setArchivetypeid(newRecordTypeId);//文书类型id
                        thisRecord.setAuthorid(userNow.getId());
                        thisRecord.setAuthor(userNow.getXm());
                        thisRecord.setArchivesfcid(thisFunArchiveSeq.getArchivesfcid());
                        thisRecord.setThisorder(recordOrder++);//文书顺序
                        copyRecordToNew(oriRecordId, thisRecord);
                    }

                } else {
                    //没有的话说明该卷类型不需要此文书  忽略就行了
                    System.out.println("忽略了呢"+thisRecord.getRecordscode());
                }
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    private void copyRecordToNew(int oriId, FunArchiveRecordsDTO newRecord) {
        arrangeArchivesService.insertFunArchiveRecords(newRecord);//新建
        int newRecordId = newRecord.getId();//新建的文书id
        //开始复制文书文件
        //查找该文书原有的文件
        for (FunArchiveFilesDTO thisFile :
                arrangeArchivesService.selectRecordFilesByRecordId(oriId, null)) {
            thisFile.setArchiveseqid(newRecord.getArchiveseqid());
            thisFile.setArchivesfcid(newRecord.getArchivesfcid());
            thisFile.setArchivetypeid(newRecord.getArchivetypeid());
            thisFile.setArchiverecordid(newRecordId);
            thisFile.setAuthor(newRecord.getAuthor());
            thisFile.setAuthorid(newRecord.getAuthorid());
            //复制插入
            arrangeArchivesService.insertFunArchiveFilesDTO(thisFile);
        }

    }
}
