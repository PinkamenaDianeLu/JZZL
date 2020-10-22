package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import com.bean.jzgl.DTO.FunArchiveTypeDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunArchiveType;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.ArrangeArchivesService;
import com.module.SystemManagement.Services.UserService;
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
     * 根据送检id查询最后一次整理记录
     *
     * @param id 送检id
     * @return |
     * @author MrLu
     * @createTime 2020/10/11 16:05
     */
    @RequestMapping(value = "/selectLastSeqBySfc", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "根据送检id查询最后一次整理记录", operType = OperLog.type.SELECT)
    public String selectLastSeqBySfc(String id) {
        JSONObject reValue = new JSONObject();
        try {
            int sfcId = Integer.parseInt(DecodeUrlP(id));
            reValue.put("value", arrangeArchivesService.selectLastSeqBySfc(sfcId));
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
            JSONArray records = new JSONArray();
            for (FunArchiveRecords thisRecord :
                    arrangeArchivesService.selectRecordsByTypeid(Integer.parseInt(id), Integer.parseInt(isDelete))) {
                JSONObject record = new JSONObject();
                record.put("record", thisRecord);
                record.put("files", arrangeArchivesService.selectRecordFilesByRecordId(thisRecord.getId()));
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
                thisRecord.setArchivesfcid(thisSeq.getArchivesfcid());//送检（新建卷）次序id
                arrangeArchivesService.insertFunArchiveRecords(thisRecord);//保存新建
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
                //根据id获取文书
                FunArchiveRecordsDTO thisRecord = arrangeArchivesService.selectFunArchiveRecordsById(thisJsonObj.getInteger("id"));
                thisRecord.setIsdelete(1);//这是被删除的卷
                thisRecord.setRecordname(thisJsonObj.getString("name"));//新的文书名
                thisRecord.setThisorder(thisJsonObj.getInteger("order"));//新的顺序
                thisRecord.setArchivetypeid(typeId);//类型id
                thisRecord.setArchiveseqid(archiveType.getArchiveseqid());//seqId
                thisRecord.setArchivesfcid(archiveType.getArchivesfcid());//送检（新建卷）次序id
                arrangeArchivesService.insertFunArchiveRecords(thisRecord);//保存新建
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
    @OperLog(operModul = operModul, operDesc = "按照文书代码按顺序查询文书列表", operType = OperLog.type.INSERT)
    public String loadFilesByFileCodes(String fileOrder) {
        JSONObject reValue = new JSONObject();
        try {
            if (!StringUtils.isEmpty(fileOrder)) {
                String[] fileOrders = fileOrder.split(",");
                //该文书没有图片了
                reValue.put("value", arrangeArchivesService.selectRecordFilesByFileCodes(fileOrders));
            }

            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 通过文书代码查询文书
     * @param filecode 文书代码
     * @return String |
     * @author MrLu
     * @createTime 2020/10/22 9:29
     */
    @RequestMapping(value = "/loadFilesByFileCode", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "通过文书代码查询文书", operType = OperLog.type.INSERT)
    public String loadFilesByFileCode(String filecode) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isNotEmpty(filecode)){
                reValue.put("value", arrangeArchivesService.selectFilesByFileCode(filecode));
                reValue.put("message", "success");
            }else {
                throw new  Exception("你传n\uD83D\uDC34呢？文书代码呢？");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
