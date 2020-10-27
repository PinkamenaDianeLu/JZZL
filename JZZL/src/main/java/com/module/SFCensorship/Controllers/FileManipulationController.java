package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunCaseInfoDTO;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.FileManipulationService;
import com.module.SystemManagement.Services.UserService;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/10/23 10:00
 * @describe 文书中的文件相关处理
 */
@Controller
@RequestMapping("/FileManipulation")
public class FileManipulationController extends BaseFactory {
    private final String operModul = "FileManipulation";
    private final FileManipulationService fileManipulationService;
    private final UserService userServiceByRedis;

    @Autowired
    public FileManipulationController(FileManipulationService fileManipulationService, @Qualifier("UserServiceByRedis") UserService userServiceByRedis) {
        this.fileManipulationService = fileManipulationService;
        this.userServiceByRedis = userServiceByRedis;
    }

    /**
     * 根据file查询对应的文书封皮
     *
     * @param fileId fileId
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/23 10:20
     */
    @RequestMapping(value = "/loadArchiveCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查询文书封皮信息", operType = OperLog.type.SELECT)
    public String loadArchiveCover(String fileId) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileId)) {
                throw new Exception("你传nm呢？");
            }
            reValue.put("value", fileManipulationService.selectFunArchiveCoverDTOByFileId(Integer.parseInt(fileId)));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 根据案件id查询案件信息
     * 查询三个表 案件、送检卷、文书
     *
     * @param sfcId    送检卷id
     * @param recordId 对应的文书id
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/23 10:33
     */
    @RequestMapping(value = "/initializationCoverMessage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "初始化案件封皮信息", operType = OperLog.type.SELECT)
    public String initializationCoverMessage(String sfcId, String recordId) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(sfcId) || StringUtils.isEmpty(recordId)) {
                throw new Exception("你传nm呢？");
            }
            JSONObject re = new JSONObject();
            FunArchiveRecordsDTO thisRecord = fileManipulationService.selectFunArchiveRecordsDTOById(Integer.parseInt(recordId));
            FunArchiveSFCDTO thisSfc = fileManipulationService.selectFunArchiveSFCDTOById(Integer.parseInt(sfcId));
            FunCaseInfoDTO thisCase = fileManipulationService.selectFunCaseInfoDTOById(thisSfc.getPeoplecaseid());
            re.put("archivename", thisRecord.getRecordname());//文书名称
            re.put("recordstyle", thisRecord.getRecordstyle());//文书类型
            re.put("sfcname", thisSfc.getArchivename());//送检卷名称
            re.put("casename", thisCase.getCasename());//案件名称
            re.put("qzname", thisCase.getGajgmc());//全宗名称
            re.put("ajbh", thisCase.getAjbh());//案件编号
            re.put("larq", thisCase.getLarq());//案件编号
            re.put("jarq", thisCase.getJarq());//案件编号
            re.put("badwmc", thisCase.getBadwdwmc());//案件编号
            re.put("badwdm", thisCase.getBadwdwdm());//案件编号
            reValue.put("value", re);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

     /**
     * 按照文件创建回收站文书
     * @author MrLu
     * @param filecodes [] filecode数组，要保证他们都是一个文书下的，否则只加载第一个文书
     * @createTime  2020/10/26 14:31
     * @return  String  |
      */
    @RequestMapping(value = "/createRecycleRecordByFiles", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "按照文件创建回收站文书", operType = OperLog.type.SELECT)
    public String createRecycleRecordByFiles(String filecodes,String recordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(filecodes)||StringUtils.isEmpty(recordid)) {
                throw new Exception("你传nm呢？");
            }
            JSONObject record = new JSONObject();
            String[] fileCodes = filecodes.split(",");
            List<FunArchiveFilesDTO> files = fileManipulationService.selectRecordFilesByFileCodes(fileCodes, StringUtil.StringToInteger(recordid));
            record.put("files", files);//加载被删除的文书
            record.put("record", fileManipulationService.selectFunArchiveRecordsDTOById(files.get(0).getArchiverecordid()));//加载文书
            reValue.put("value", record);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
