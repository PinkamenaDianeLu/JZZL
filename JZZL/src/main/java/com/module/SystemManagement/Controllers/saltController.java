package com.module.SystemManagement.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.util.GlobalUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2020/9/27 9:54
 * @describe
 */
@Controller
@RequestMapping("/Salt")

/**
* @Deprecated  早晚会废弃这个的并并改为使用com.component.saltLoader
**/
@Deprecated
public class saltController {

    @RequestMapping(value = "/getSalt", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getSalt() {
        JSONObject reValue = new JSONObject();
                try {
                    reValue.put("value", GlobalUtil.getGlobal("salt"));
                    reValue.put("message", "success");
                } catch (Exception e) {
                    e.printStackTrace();
                    reValue.put("message", "error");
                }
                return reValue.toJSONString();
    }
}
