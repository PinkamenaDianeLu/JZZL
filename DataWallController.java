package com.module.Interface.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.module.Interface.Services.DataWallService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2021/1/26 15:03
 * @describe 数据图墙的接口
 * 不会吧不会吧 不会有人连这代码都需要看注释吧！
 */
@Controller
@RequestMapping("/DataWallInterface")
public class DataWallController {
    @Autowired
    DataWallService dataWallService;

    /**
     * 行政案件数
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 15:38
     */
    @RequestMapping(value = "/xzCasesCount", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String xzCasesCount(String JsonParam) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject jP = (JSONObject) JSONObject.parse(JsonParam);
            String code = jP.getString("key");
            if (!"FC46940548FAFD696CC6418FF87F4AA5".equals(code)) {
                reValue.put("code", "404");
                return reValue.toJSONString();
            }
            Map<String, Object> pM = ParamParse(jP);//查询参数
            pM.put("casetype", 2);//行政
            reValue.put("value", dataWallService.selectCaseCount(pM));
            reValue.put("code", "200");
        } catch (NumberFormatException e) {
            reValue.put("code", "400");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("code", "503");
        }
        return reValue.toJSONString();
    }

    /**
     * 刑事案件数
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 15:38
     */
    @RequestMapping(value = "/xsCasesCount", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String xsCasesCount(String JsonParam) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject jP = (JSONObject) JSONObject.parse(JsonParam);
            String code = jP.getString("key");
            if (!"FC46940548FAFD696CC6418FF87F4AA5".equals(code)) {
                reValue.put("code", "404");
                return reValue.toJSONString();
            }
            Map<String, Object> pM = ParamParse(jP);//查询参数
            pM.put("casetype",1);//刑事
            reValue.put("value", dataWallService.selectCaseCount(pM));
            reValue.put("message", "success");
        } catch (NumberFormatException e) {
            reValue.put("code", "400");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "503");
        }
        return reValue.toJSONString();
    }

    /**
     * 行政案卷整理数
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 15:40
     */
    @RequestMapping(value = "/xzArchivesRearranged", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String xzArchivesRearranged(String JsonParam) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject jP = (JSONObject) JSONObject.parse(JsonParam);
            String code = jP.getString("key");
            if (!"FC46940548FAFD696CC6418FF87F4AA5".equals(code)) {
                reValue.put("code", "404");
                return reValue.toJSONString();
            }
            Map<String, Object> pM = ParamParse(jP);//查询参数
            pM.put("casetype", 2);//行政
            reValue.put("value", dataWallService.selectArchivesRearranged(pM));
            reValue.put("message", "success");
        } catch (NumberFormatException e) {
            reValue.put("code", "400");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "503");
        }
        return reValue.toJSONString();
    }

    /**
     * 刑事案卷整理数
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 15:41
     */
    @RequestMapping(value = "/xsArchivesRearranged", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String xsArchivesRearranged(String JsonParam) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject jP = (JSONObject) JSONObject.parse(JsonParam);
            String code = jP.getString("key");
            if (!"FC46940548FAFD696CC6418FF87F4AA5".equals(code)) {
                reValue.put("code", "404");
                return reValue.toJSONString();
            }
            Map<String, Object> pM = ParamParse(jP);//查询参数
            pM.put("casetype",1);//刑事
            reValue.put("value", dataWallService.selectArchivesRearranged(pM));
            reValue.put("message", "success");
        } catch (NumberFormatException e) {
            reValue.put("code", "400");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "503");
        }
        return reValue.toJSONString();
    }

    /**
     * 刑事案件归档数
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 15:42
     */
    @RequestMapping(value = "/xsArchives", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String xsArchives(String JsonParam) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject jP = (JSONObject) JSONObject.parse(JsonParam);
            String code = jP.getString("key");
            if (!"FC46940548FAFD696CC6418FF87F4AA5".equals(code)) {
                reValue.put("code", "404");
                return reValue.toJSONString();
            }
            Map<String, Object> pM = ParamParse(jP);//查询参数
            pM.put("casetype", 1);//刑事
            reValue.put("value", dataWallService.selectArchives(pM));
            reValue.put("message", "success");
        } catch (NumberFormatException e) {
            reValue.put("code", "400");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "503");
        }
        return reValue.toJSONString();
    }

    /**
     * 行政案件归档数
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 15:56
     */
    @RequestMapping(value = "/xzArchives", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String xzArchives(String JsonParam) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject jP = (JSONObject) JSONObject.parse(JsonParam);
            String code = jP.getString("key");
            if (!"FC46940548FAFD696CC6418FF87F4AA5".equals(code)) {
                reValue.put("code", "404");
                return reValue.toJSONString();
            }
            Map<String, Object> pM = ParamParse(jP);//查询参数
            pM.put("casetype", 2);//行政
            reValue.put("value", dataWallService.selectArchives(pM));
            reValue.put("message", "success");
        } catch (NumberFormatException e) {
            reValue.put("code", "400");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "503");
        }
        return reValue.toJSONString();
    }

    /**
     * 将参数转换
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/26 16:14
     */
    private Map<String, Object> ParamParse(JSONObject jP) throws Exception {
        Map<String, Object> rMap = new HashedMap();
        String agencyCode = jP.getString("agencyCode");//单位代码
        String startTime = jP.getString("startTime");//开始时间
        String endTime = jP.getString("endTime");//结束时间
        if (null != agencyCode) {
            //单位代码
            rMap.put("agencyCode", agencyCode);
        }
        if (null != startTime) {
            //开始时间
            long lt = new Long(startTime);
            Date date = new Date(lt);
            rMap.put("startTime", date);
        }
        if (null != endTime) {
            //结束时间
            long lt = new Long(endTime);
            Date date = new Date(lt);
            rMap.put("endTime", date);
        }
        return rMap;
    }

}
