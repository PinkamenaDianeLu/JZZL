package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.FunArchiveType;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.ArrangeArchivesService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ArrangeArchivesController(ArrangeArchivesService arrangeArchivesService) {
        this.arrangeArchivesService = arrangeArchivesService;
    }

    /**
     * 通过送检记录id查询文书目录
     *
     * @param id 送检记录id(已加密)
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
            //解密id
            int seqId = Integer.parseInt(DecodeUrlP(id));
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

    @RequestMapping(value = "/getRecordsIndex", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "得到文书目录(具体文书)", operType = OperLog.type.SELECT)
    public String getRecordsIndex(String id) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(id)) {
                throw new Exception("你传nm呢？");
            }
            reValue.put("value", arrangeArchivesService.selectRecordsByTypeid(Integer.parseInt(id)));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
