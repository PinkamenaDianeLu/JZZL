package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.config.aop.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:44
 * @describe
 */
@Controller
@RequestMapping("/Records")
public class RecordsController extends BaseFactory {
    private final String operModul = "Records";
    final
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

            List<FunArchiveRecords> a=new ArrayList<>();
            a.add(recordsService.getFunArchiveRecordsById(id));
            setBmbName(FunArchiveRecords.class, a);
//            reValue.put("value", recordsService.getFunArchiveRecordsById(id));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    ;
}
