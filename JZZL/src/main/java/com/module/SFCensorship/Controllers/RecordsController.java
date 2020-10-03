package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    public RecordsController(RecordsService recordsService) {
        this.recordsService = recordsService;
    }


    @RequestMapping(value = "/getFunArchiveRecordsById", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "通过id查询文书", operType = OperLog.type.SELECT)
    public String getFunArchiveRecordsById(Integer id) {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", transformBmField(recordsService.getFunArchiveRecordsById(id),FunArchiveRecords.class));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    ;
}
