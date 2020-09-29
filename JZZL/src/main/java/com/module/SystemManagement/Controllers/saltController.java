package com.module.SystemManagement.Controllers;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2020/9/27 9:54
 * @describe 将salt加载到前台
 */
@Controller
@RequestMapping("/Salt")
public class saltController {
    @Autowired
    RedisTemplate<String, Object> redisCCTemplate;

     /**
     * 查询当前salt
     * @author MrLu
     * @createTime  2020/9/29 16:54
     * @return  String  |   salt
      */
    @RequestMapping(value = "/getSalt", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getSalt() {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value",  redisCCTemplate.opsForValue().get("salt"));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
