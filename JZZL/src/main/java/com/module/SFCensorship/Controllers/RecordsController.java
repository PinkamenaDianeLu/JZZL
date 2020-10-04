package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.Source.FunArchiveRecords;
import com.bean.jzgl.Source.FunPeopelCase;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.RecordsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

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
            reValue.put("value", transformBmField(recordsService.getFunPeopleCaseById(id), FunPeopelCase.class));
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
    @ResponseBody
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
            int peocaseid = Integer.parseInt(DecodeUrlP(pid));
            pJsonObj.put("jqbh", recordsService.getFunPeopleCaseById(peocaseid).getJqbh());
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            reMap.put("rows", transformBmField(recordsService.selectRecordsByJqbhPage(pJsonObj), FunArchiveRecords.class));
            reMap.put("total", recordsService.selectRecordsByJqbhCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

}
