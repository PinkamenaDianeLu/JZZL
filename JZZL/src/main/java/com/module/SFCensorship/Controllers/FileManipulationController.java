package com.module.SFCensorship.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.factory.BaseFactory;
import com.module.SFCensorship.Services.FileManipulationService;
import com.module.SystemManagement.Services.UserService;
import com.util.SftpUtil;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
            FunCaseInfoDTO thisCase = fileManipulationService.selectFunCaseInfoDTOById(thisSfc.getCaseinfoid());
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
     * 保存卷宗封皮
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/10/30 15:36
     */
    @RequestMapping(value = "/saveCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "保存卷宗封皮信息", operType = OperLog.type.InsertOrUpdate)
    public String saveCover(String coverid, String fileid,String cover) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) ||StringUtils.isEmpty(coverid) || StringUtils.isEmpty(cover)) {
                throw new Exception("你传nm呢？");
            }
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));

            FunArchiveCoverDTO coverDTO = JSON.parseObject(cover, FunArchiveCoverDTO.class);

            int coverId=Integer.parseInt(coverid);
            if (coverId>0){
                //更新
                coverDTO.setId(coverId);
                fileManipulationService.updateFunArchiveCoverById(coverDTO);
            }else {
                //新建
                SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
                coverDTO.setAuthor(userNow.getUsername());
                coverDTO.setAuthorid(userNow.getId());
                coverDTO.setAuthorxm(userNow.getXm());
                coverDTO.setJqbh(thisFile.getJqbh());
                coverDTO.setAjbh(thisFile.getAjbh());
                coverDTO.setArchivesfcid(thisFile.getArchivesfcid());
                coverDTO.setArchiveseqid(thisFile.getArchiveseqid());
                coverDTO.setArchivetypeid(thisFile.getArchivetypeid());
                coverDTO.setArchivefileid(thisFile.getId());
                coverDTO.setFilecode(thisFile.getFilecode());
                fileManipulationService.insertFunArchiveCover(coverDTO);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 按照文件创建回收站文书
     *
     * @param filecodes [] filecode数组，要保证他们都是一个文书下的，否则只加载第一个文书
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/26 14:31
     */
    @RequestMapping(value = "/createRecycleRecordByFiles", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "按照文件创建回收站文书", operType = OperLog.type.SELECT)
    public String createRecycleRecordByFiles(String filecodes, String recordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(filecodes) || StringUtils.isEmpty(recordid)) {
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

    /**
     * 保存文书目录
     *
     * @param indexinfo 文书目录信息 一个json数组
     * @param fileid    对应的fileid
     * @param indexid   在recordindex表中的id 新建时传个<=0的数即可 不可为空
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/29 15:23
     */
    @RequestMapping(value = "/saveFunArchiveRecordindex", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "保存文书目录", operType = OperLog.type.InsertOrUpdate)
    public String saveFunArchiveRecordindex(String fileid, String indexinfo, String indexid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(indexid)) {
                throw new Exception("你传nm呢？");
            }
            int oriId = Integer.parseInt(indexid);
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));
            FunArchiveRecordindexDTO Recordindex = new FunArchiveRecordindexDTO();
//文书目录信息
            Recordindex.setIndexinfo(indexinfo);
            if (0 < oriId) {
                //更新目录
                Recordindex.setId(oriId);
                fileManipulationService.updateFunArchiveRecordindexDTO(Recordindex);
            } else {
                //新建目录
                SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
                Recordindex.setAuthor(userNow.getUsername());
                Recordindex.setAuthorid(userNow.getId());
                Recordindex.setAuthorxm(userNow.getXm());
                Recordindex.setJqbh(thisFile.getJqbh());
                Recordindex.setAjbh(thisFile.getAjbh());
                Recordindex.setArchivesfcid(thisFile.getArchivesfcid());
                Recordindex.setArchiveseqid(thisFile.getArchiveseqid());
                Recordindex.setArchivetypeid(thisFile.getArchivetypeid());
                Recordindex.setArchivefileid(thisFile.getId());
                Recordindex.setFilecode(thisFile.getFilecode());
                fileManipulationService.insertFunArchiveRecordindexDTO(Recordindex);
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 查找文书类型下的文书目录信息
     *
     * @param archiveseqid  整理次序id
     * @param archivetypeid 卷类型id
     * @return |
     * @author MrLu
     * @createTime 2020/10/29 15:46
     */
    @RequestMapping(value = "/selectFunArchiveRecordindexByType", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "查找文书类型下的文书目录信息", operType = OperLog.type.SELECT)
    public String selectFunArchiveRecordindexByType(String archiveseqid, String archivetypeid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(archiveseqid) || StringUtils.isEmpty(archivetypeid)) {
                throw new Exception("你传nm呢？");
            }
            reValue.put("value", fileManipulationService.selectRecordIndexByTypeId(Integer.parseInt(archiveseqid), Integer.parseInt(archivetypeid)));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();

    }

    @RequestMapping(value = "/upLoadRecordFiles", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "上传图片", operType = OperLog.type.INSERT)
    public String upLoadRecordFiles(String reportTitle, String reportContent, @RequestParam("newFile") MultipartFile newFile) {
        JSONObject reValue = new JSONObject();
        try {
            //如果有附件的 触发附件上传
            if (null != newFile) {
                //这里不判断文件后缀 因为vsftp中没有给x权限  所以即使有问题的文件也执行不了
                String orgFileName = newFile.getOriginalFilename();
                String orgFileSuffix = orgFileName.substring(orgFileName.lastIndexOf("."));
                //上传的文件名
                String fileName = UUID.randomUUID() + orgFileSuffix;
                //上传路径 年与日
                Calendar now = Calendar.getInstance();
                String uploadFile = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH);
                String jdlj = SftpUtil.upLoad(fileName, uploadFile, "others", newFile.getInputStream());
//                newReport.setAttachment(newFile.getOriginalFilename());//上传文件原名
//                newReport.setAttachmenturl(jdlj);//文件在服务器上的位置
            }
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


     /**
     * 分页查询文书
     * @author MrLu
     * @createTime  2020/11/6 9:26
      */
    @RequestMapping(value = "/selectArchiveRecordPage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "文书分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectArchiveRecordPage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            pJsonObj.put("archiveseqid",pJsonObj.getInteger("seqid"));//整理次序id
            reMap.put("rows", fileManipulationService.selectArchiveRecordPage(pJsonObj));
            reMap.put("total", fileManipulationService.selectArchiveRecordPageCount(pJsonObj));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reMap;
    }

    /**
     * 按照文书代码移动文件至所属文书
     *
     * @param fileOrder 文件代码数组
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/15 18:17
     */
    @RequestMapping(value = "/moveFiles", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @OperLog(operModul = operModul, operDesc = "按照文书代码移动文件至所属文书", operType = OperLog.type.SELECT)
    public String moveFiles(String fileOrder, String seqId,String recordid,String orirecordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileOrder) || StringUtils.isEmpty(seqId)|| StringUtils.isEmpty(recordid)) {
                throw new Exception("给一个? 自己体会");
            }
            int recordId=Integer.parseInt(recordid);
            String[] fileOrders = fileOrder.split(",");
            //该文书没有图片了
          int maxOrder=  fileManipulationService.selectFileMaxOrder(recordId);
            List<FunArchiveFilesDTO> moveFiles=  fileManipulationService.selectRecordFilesByFileCodes(fileOrders, Integer.parseInt(orirecordid));
            for (FunArchiveFilesDTO thisMoveFile:
                    moveFiles) {
                thisMoveFile.setArchiverecordid(recordId);
                thisMoveFile.setThisorder(maxOrder++);
                //再加一个更新
                fileManipulationService.updateFileByFileCode(thisMoveFile);
            }
            //获取对应文书的最大顺序
            reValue.put("value", moveFiles);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

}
