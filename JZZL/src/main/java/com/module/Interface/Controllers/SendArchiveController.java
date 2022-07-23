package com.module.Interface.Controllers;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;
import com.bean.jzgl.Source.SysUser;
import com.enums.Enums;
import com.factory.BaseFactory;
import com.module.Interface.Services.InterfaceService;
import com.module.SFCensorship.Services.SFCensorshipService;
import com.module.SystemManagement.Services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

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
    private final
    SFCensorshipService sFCensorshipService;

    public SendArchiveController(@Qualifier("UserService") UserService userService, InterfaceService interfaceService, SFCensorshipService sFCensorshipService) {
        this.userService = userService;
        this.interfaceService = interfaceService;
        this.sFCensorshipService = sFCensorshipService;
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
    @CrossOrigin
    public String sendOver(String message) {
        try {
            String realMessage = new String(Base64.getDecoder().decode(message.replaceAll(" ", "")), StandardCharsets.UTF_8);
//            String realMessage = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
            //参数转化
            JSONObject JsonMessage = JSONObject.parseObject(realMessage);
            String key = JsonMessage.getString("key");//验证key
            Integer seqid = JsonMessage.getInteger("seqid");//seqid
            //验证是否是我认识的接口
            if (Enums.passwordSwitch.sendToAz.getValue().equals(key)) {
                //判断用户是否存在
                String username = JsonMessage.getString("username");
                //判断案件信息是否存在
                SysUser sysUserNow = userService.loginVerification(username);

                FunArchiveSFCDTO sfc = interfaceService.selectArchiveSfcBySeqid(seqid);
                if (null != sysUserNow || null != sfc) {
                    Integer messageCode = JsonMessage.getInteger("messagecode");
                    //websocket通知用户打包完成
                    if (0 == messageCode) {
                        websocketSendMessage(sfc.getArchivename() + "，已打包完成", sysUserNow.getIdcardnumber(), Enums.messagetype.typeOne);
                        //更改记录为已打包
                        sfc.setIssend(1);//标记为已打包
                        interfaceService.updateArchiveSfcById(sfc);
                    } else if (1 == messageCode) {
                        sFCensorshipService.updateSendTypeById(0, sfc.getId());//更改为未发送
                        websocketSendMessage(sfc.getArchivename() + "，打包失败", sysUserNow.getIdcardnumber(), Enums.messagetype.typeNine);
                    } else if (-2 == messageCode) {
                        sFCensorshipService.updateSendTypeById(-2, sfc.getId());//更改为未发送
                        websocketSendMessage(sfc.getArchivename() + "，打包失败，数据包过大", sysUserNow.getIdcardnumber(), Enums.messagetype.typeTen);
                    }


                    return "200";
                } else {
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

    /**
     * 打包发送进度
     *
     * @param message {key:"",seqid:"",messagecode:"-1:打包失败:0:政法委;1:到政法委",message:"说话！"}
     * @return |code：
     * 503 接口错误（走到catch里了）;
     * 200 (成功) ；
     * 400 （请求参数错误或参数无法解析）
     * 403 （方法已被废除）
     * 404（key值错误）；
     * @author MrLu
     * @createTime 2021/5/26 17:00
     */
    @RequestMapping(value = "/getSendNode", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @CrossOrigin
    public String getSendNode(String message) {
        try {

            String realMessage = new String(Base64.getDecoder().decode(message.replaceAll(" ", "+")));

//            String realMessage = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
            //参数转化
            JSONObject JsonMessage = JSONObject.parseObject(realMessage);
            String key = JsonMessage.getString("key");//验证key
            Integer seqid = JsonMessage.getInteger("seqid");//seqid
            //验证是否是我认识的接口
            if (Enums.passwordSwitch.sendToAz.getValue().equals(key)) {

                //判断案件信息是否存在
                FunArchiveSFCDTO sfc = interfaceService.selectArchiveSfcBySeqid(seqid);

                if (null != sfc) {
//                    null != sysUserNow ||
                    List<FunCasePeoplecaseDTO> peopleCase = interfaceService.selectRelationByCaseid(sfc.getCaseinfoid());
                    for (FunCasePeoplecaseDTO thhisGx :
                            peopleCase) {
                        SysUser sysUserNow = userService.loginVerification(thhisGx.getIdcard());
                        if (null != sysUserNow) {
                            //websocket通知用户打包信息
                            Integer messagecode = JsonMessage.getInteger("messagecode");
                            if (messagecode < 0) {
                                if (messagecode == -2) {
                                    sFCensorshipService.updateSendTypeById(-2, sfc.getId());//更改为未发送
                                    websocketSendMessage(sfc.getArchivename() + "，打包失败:数据包过大",
                                            sysUserNow.getIdcardnumber(), Enums.messagetype.typeTen);
                                } else if (messagecode == -1) {
                                    sFCensorshipService.updateSendTypeById(0, sfc.getId());//更改为未发送
                                    websocketSendMessage(sfc.getArchivename() + "，打包失败:" + JsonMessage.getString("message"),
                                            sysUserNow.getIdcardnumber(), Enums.messagetype.typeNine);
                                } else {
                                    sFCensorshipService.updateSendTypeById(-99, sfc.getId());//更改为未发送
                                    websocketSendMessage(sfc.getArchivename() + "，打包失败:" + JsonMessage.getString("message"),
                                            sysUserNow.getIdcardnumber(), Enums.messagetype.typeNine);
                                }
                            } else if (messagecode == 0) {
                                sFCensorshipService.updateSendTypeById(Enums.messagetype.typeSeven.getValue(), sfc.getId());//更改为未发送
                                websocketSendMessage(sfc.getArchivename() + "，包流转进度已更新:检察院已解析完",
                                        sysUserNow.getIdcardnumber(), Enums.messagetype.typeSeven);
                            } else if (messagecode == 1) {
                                sFCensorshipService.updateSendTypeById(Enums.messagetype.typeEight.getValue(), sfc.getId());//更改为未发送
                                websocketSendMessage(sfc.getArchivename() + "，包流转进度已更新:接收单位已解析完",
                                        sysUserNow.getIdcardnumber(), Enums.messagetype.typeEight);
                            }
                        }
                    }

                    return "200";
                } else {
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
//    @RequestMapping(value = "/cheneckJpg", method = {RequestMethod.GET,
//            RequestMethod.POST})
//    @ResponseBody
//    @CrossOrigin
    public String cheneckJpg(String message) {
        String jy = null;
        try {
            String realMessage = new String(Base64.getDecoder().decode(message.replaceAll(" ", "")), StandardCharsets.UTF_8);
            File saveDir = new File(realMessage);
            if (!saveDir.exists()) {
                jy="false";
            }else {
                    BufferedImage image = ImageIO.read(saveDir);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(image,"png",stream);
                Base64.Encoder encoder = Base64.getEncoder();
                jy=  encoder.encodeToString(stream.toByteArray());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "400";
        } catch (Exception e) {
            e.printStackTrace();
            return "503";
        }
        return  jy;
    }

}
