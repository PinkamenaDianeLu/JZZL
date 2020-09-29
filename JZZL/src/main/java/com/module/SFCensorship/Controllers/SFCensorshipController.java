package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.config.aop.OperLog;
import com.factory.SaltFactory;
import com.module.SFCensorship.Services.SFCensorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/9/27 11:04
 * @describe 送检相关
 */
@Controller
@RequestMapping("/SFCensorship")
public class SFCensorshipController extends SaltFactory {
    private final String operModul = "SFCensorship";

    final
    SFCensorshipService sFCensorshipService;

    @Autowired
    public SFCensorshipController(SFCensorshipService sFCensorshipService) {
        this.sFCensorshipService = sFCensorshipService;
    }

    /**
     * 分页查询送检列表
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/9/27 11:11
     */
    @RequestMapping(value = "/selectSFCensorshipPage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "送检记录分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectSFCensorshipPage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            //解密id参数
            pJsonObj.put("peopelcaseid",DecodeUrlP(pJsonObj.getString("id")));//解密的id
            reMap.put("rows", sFCensorshipService.selectArchiveSeqPage(pJsonObj));
            reMap.put("total", sFCensorshipService.selectArchiveSeqPageCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }
}
