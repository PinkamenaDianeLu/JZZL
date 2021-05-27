package com.module.Interface.Controllers;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.Source.SysUser;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.module.Interface.Services.InterfaceService;
import com.module.SystemManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2021/5/26 16:38
 * @describe
 */
@Controller
@RequestMapping("/SendArchive")
public class SendArchiveController extends BaseFactory {
    private final
    UserService userService;
    private final
    InterfaceService interfaceService;
    public SendArchiveController(@Qualifier("UserService") UserService userService, InterfaceService interfaceService) {
        this.userService = userService;
        this.interfaceService = interfaceService;
    }

    /**
     * 发送完成后接收信息
     *
     * @param message {key:"",username:"",seqid:"",messagecode:"0:打包完成;1:打包失败",message:"可选项"}
     * @return |code：
     * 503 接口错误（走到catch里了）;
     * 200 (成功) ；
     * 400 （请求参数错误或参数无法解析）
     * 403 （方法已被废除）
     * 404（key值错误）；
     * @author MrLu
     * @createTime 2021/5/26 17:00
     */
    @RequestMapping(value = "/selectLastSeqSfcBySfc", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    public String sendOver(String message) {
        try {
            //参数转化
            JSONObject JsonMessage = JSONObject.parseObject(message);
            String key = JsonMessage.getString("key");//验证key
            Integer seqid = JsonMessage.getInteger("seqid");//seqid
            //验证是否是我认识的接口
            if (Enums.passwordSwitch.sendToAz.getValue().equals(key)) {
                //判断用户是否存在
                String username = JsonMessage.getString("username");
                //判断案件信息是否存在
                SysUser sysUserNow = userService.loginVerification(username);

                FunArchiveSFCDTO sfc=interfaceService.selectArchiveSfcBySeqid(seqid);
                if (null!=sysUserNow||null!=sfc){
                    //websocket通知用户打包完成
                    websocketSendMessage(sfc.getArchivename()+"，已打包完成", sysUserNow.getIdcardnumber(), Enums.messagetype.typeOne);
                    //更改记录为已打包
                    sfc.setIssend(1);//标记为已打包
                    interfaceService.updateArchiveSfcById(sfc);
                    return "200";
                }else {
                    return "400";
                }

            } else {
                return "404";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "400";
        } catch (Exception e) {
            e.printStackTrace();
            return "503";
        }
    }

}
