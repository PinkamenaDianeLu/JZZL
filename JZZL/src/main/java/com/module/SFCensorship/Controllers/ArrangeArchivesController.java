package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
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
            reValue.put("value", arrangeArchivesService.selectRecordsByTypeid(Integer.parseInt(id), Integer.parseInt(isDelete)));
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
     * @return |
     * @author MrLu
     * @createTime 2020/10/13 16:42
     */
    @RequestMapping(value = "/saveArchiveIndexSortByType", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "保存卷整理顺序", operType = OperLog.type.INSERT)
    public String saveArchiveIndexSortByType(String saveData, String seqid) {
        JSONObject reValue = new JSONObject();
        try {
            FunArchiveSeqDTO thisSeq = arrangeArchivesService.selectFunArchiveSeqById(Integer.parseInt(seqid));
            if (null == thisSeq) {
                throw new Exception("该整理次序id无法查询到对应信息！");
            }
            JSONArray pDate = JSON.parseArray(saveData);
            int thisBatches = thisSeq.getBatchesseq() ;
            //开始复制文书信息
            //1.通过文书id得到对应文书
            int i=0;
            for (Object thisP :
                    pDate) {
                JSONObject thisJsonObj = (JSONObject) thisP;
                //根据id获取文书
                FunArchiveRecordsDTO thisRecord = arrangeArchivesService.selectFunArchiveRecordsById(thisJsonObj.getInteger("id"));
                thisRecord.setRecordname(thisJsonObj.getString("name"));//新的文书名
                thisRecord.setThisorder(thisJsonObj.getInteger("order"));//新的顺序
                thisRecord.setArchivetypeid(thisJsonObj.getInteger("typeid"));//说不定会更新的文书类型
                thisRecord.setArchiveseqid(thisSeq.getId());//seqId
                thisRecord.setArchivesfcid(thisSeq.getArchivesfcid());//送检（新建卷）次序id
                arrangeArchivesService.insertFunArchiveRecords(thisRecord);//保存新建
                System.out.println(i++);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
