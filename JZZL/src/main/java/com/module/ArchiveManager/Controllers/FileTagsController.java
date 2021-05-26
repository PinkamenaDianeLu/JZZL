package com.module.ArchiveManager.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveTagsDTO;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.annotations.recordTidy;
import com.factory.BaseFactory;
import com.module.ArchiveManager.Services.FileTagsService;
import com.module.SystemManagement.Services.UserService;
import com.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author MrLu
 * @createTime 2020/12/7 19:10
 * @describe 标签管理相关
 */

@Controller
@RequestMapping("/FileTags")
public class FileTagsController extends BaseFactory {
    private final String operModul = "FileTags";
    private final UserService userServiceByRedis;
    private final
    FileTagsService fileTagsService;

    @Autowired
    public FileTagsController(FileTagsService fileTagsService, @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.fileTagsService = fileTagsService;
        this.userServiceByRedis = userServiceByRedis;
    }


    /**
     * 创建新的标签
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/7 19:17
     */
    @RequestMapping(value = "/createNewTags", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "创建新的标签", operType = OperLog.type.INSERT)
    public String createNewTags(String tag) {
        JSONObject reValue = new JSONObject();
        try {
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            FunArchiveTagsDTO tagsDTO = JSON.parseObject(tag, FunArchiveTagsDTO.class);

            FunArchiveRecordsDTO thisRecord = fileTagsService.selectFunArchiveRecordsDTOById(tagsDTO.getRecordid());
            if (null == thisRecord) {
                throw new Exception("文书id查不到对应文书！");
            }
            tagsDTO.setArchiveseqid(thisRecord.getArchiveseqid());
            tagsDTO.setArchivesfcid(thisRecord.getArchivesfcid());
            tagsDTO.setArchivetypeid(thisRecord.getArchivetypeid());
            tagsDTO.setArchivefileid(0);
            tagsDTO.setAuthor(userNow.getUsername());
            tagsDTO.setAuthorid(userNow.getId());
            tagsDTO.setAuthorxm(userNow.getXm());
            fileTagsService.createNewTags(tagsDTO);//创建
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 删除标签
     *
     * @param id 标签id
     * @return |
     * @author Mrlu
     * @createTime 2021/3/2 9:49
     */
    @RequestMapping(value = "/delTag", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "删除标签", operType = OperLog.type.UPDATE)
    public String delTag(Integer id) {
        JSONObject reValue = new JSONObject();
        try {
            fileTagsService.delTagById(id);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 查询文件的标签
     *
     * @param archiveseqid
     * @param filecode
     * @return |
     * @author MrLu
     * @createTime 2020/12/8 9:27
     */
    @RequestMapping(value = "/selectArchiveTags", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "查询文件的标签", operType = OperLog.type.SELECT)
    public String selectArchiveTags(String archiveseqid, String filecode) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtil.isEmptyAll(archiveseqid, filecode)) {
                throw new Exception("传nm呢？？？  值呢？？！");
            }
            reValue.put("value", fileTagsService.selectArchiveTagsById(Integer.parseInt(archiveseqid), filecode));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
    //

    @RequestMapping(value = "/changeTagColor", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "更改标签的亚麻色", operType = OperLog.type.UPDATE)
    public String changeTagColor(Integer id, String color) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtil.isEmptyAll(id+"", color)) {
                throw new Exception("传nm呢？？？  值呢？？！");
            }
            fileTagsService.changeTagColor(id, color);//更新颜色
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
