package com.module.ArchiveManager.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.annotations.recordTidy;
import com.enums.EnumSoft;
import com.factory.BaseFactory;
import com.module.ArchiveManager.Services.FileManipulationService;
import com.module.SystemManagement.Services.UserService;
import com.util.FtpUtil;
import com.util.GlobalUtil;
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

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.util.SftpUtil.uploadList;

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
     * @param fileId String
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/23 10:20
     */
    @RequestMapping(value = "/loadArchiveCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
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
     * 根据file查询对应的行政文书封皮
     *
     * @param fileId String
     * @return |
     * @author MrLu
     * @createTime 2021/6/24 9:26
     */
    @RequestMapping(value = "/loadArchiveXzCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "查询行政文书封皮信息", operType = OperLog.type.SELECT)
    public String loadArchiveXzCover(Integer fileId) {
        JSONObject reValue = new JSONObject();
        try {
            if (null == fileId) {
                throw new Exception("你传nm呢？");
            }
            reValue.put("value", fileManipulationService.selectFunArchiveXzCoverDTOByFileId(fileId));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 根据file查询对应的文书封底
     *
     * @param fileId fileId
     * @return String  |
     * @author MrLu
     * @createTime 2020/10/23 10:20
     */
    @RequestMapping(value = "/loadArchiveBackCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "查询文书封底信息", operType = OperLog.type.SELECT)
    public String loadArchiveBackCover(String fileId) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileId)) {
                throw new Exception("你传nm呢？");
            }
            reValue.put("value", fileManipulationService.selectFunArchiveBackCoverDTOByFileId(Integer.parseInt(fileId)));
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
    @recordTidy
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
            //行政封皮
            if (thisRecord.getRecordscode().equals(EnumSoft.fplx.XZCOVER.getValue())) {

                SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
                re.put("sfcname", thisCase.getGajgmc());//送检卷名称
                re.put("ljdw", userNow.getAgencyname());//立卷单位
                re.put("archivename", "治安管理处罚卷");//文书名称
            } else {
                //刑事的查嫌疑人
                //查询这次送检的嫌疑人姓名
                List<FunSuspectDTO> suspects = fileManipulationService.selectSuspectByArchiveseqid(thisRecord.getArchiveseqid());
                String names = suspects.stream().map(FunSuspectDTO::getSuspectname).collect(Collectors.joining(",", "", ""));
                re.put("suspectsxm", names);//嫌疑人姓名
                re.put("badwmc", thisCase.getBadwdwmc());//办案单位名称
                re.put("badwdm", thisCase.getBadwdwdm());//办案单位代码
                re.put("sfcname", thisSfc.getArchivename());//送检卷名称
                re.put("qzname", thisCase.getGajgmc());//全宗名称
                re.put("archivename", thisRecord.getRecordname());//文书名称（名起错了好像  不要介意）
            }

            re.put("recordstyle", thisRecord.getRecordstyle());//文书类型
            re.put("casename", thisCase.getCasename());//案件名称
            SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//日期格式化
            re.put("ajbh", thisCase.getAjbh());//案件编号
            if (null != thisCase.getLarq()) {
                re.put("larq", formatData.format(thisCase.getLarq()));//立案日期
            }
            if (null != thisCase.getJarq()) {
                re.put("jarq", formatData.format(thisCase.getJarq()));//立案日期
            }


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
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "保存刑事卷宗封皮信息", operType = OperLog.type.InsertOrUpdate)
    public String saveCover(@RequestParam("newFile") MultipartFile newFile, String coverid, String fileid, String cover) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(coverid) || StringUtils.isEmpty(cover)) {
                throw new Exception("你传nm呢？");
            }
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));

            FunArchiveCoverDTO coverDTO = JSON.parseObject(cover, FunArchiveCoverDTO.class);
            newFile(thisFile, newFile);
            int coverId = Integer.parseInt(coverid);
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            coverDTO.setAuthor(userNow.getUsername());
            coverDTO.setAuthorid(userNow.getId());
            coverDTO.setAuthorxm(userNow.getXm());
            if (coverId > 0) {
                //更新
                coverDTO.setId(coverId);
                fileManipulationService.updateFunArchiveCoverById(coverDTO);
            } else {
                //新建
                coverDTO.setJqbh(thisFile.getJqbh());
                coverDTO.setAjbh(thisFile.getAjbh());
                coverDTO.setArchivesfcid(thisFile.getArchivesfcid());
                coverDTO.setArchiveseqid(thisFile.getArchiveseqid());
                coverDTO.setArchivetypeid(thisFile.getArchivetypeid());
                coverDTO.setArchivefileid(thisFile.getId());
                coverDTO.setFilecode(thisFile.getFilecode());
                fileManipulationService.insertFunArchiveCover(coverDTO);
            }
            //更新封皮信息
            fileManipulationService.updateFunArchiveFileDTO(thisFile);
            fileManipulationService.updateRecordsIscoverimg(thisFile.getArchiverecordid());
            reValue.put("value", coverDTO.getId());
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    /**
     * 保存行政卷宗封皮
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/6/24 15:49
     */
    @RequestMapping(value = "/saveXzCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "保存行政卷宗封皮信息", operType = OperLog.type.InsertOrUpdate)
    public String saveXzCover(String coverid, String fileid, String cover) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(coverid) || StringUtils.isEmpty(cover)) {
                throw new Exception("你传nm呢？");
            }
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));

            FunArchiveXzcoverDTO coverDTO = JSON.parseObject(cover, FunArchiveXzcoverDTO.class);

            int coverId = Integer.parseInt(coverid);
            if (coverId > 0) {
                //更新
                coverDTO.setId(coverId);
                fileManipulationService.updateFunArchiveCoverById(coverDTO);
            } else {
                //新建
                SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
                coverDTO.setAuthor(userNow.getUsername());
                coverDTO.setAuthorid(userNow.getId());
                coverDTO.setAuthorxm(userNow.getXm());
                coverDTO.setJqbh(thisFile.getJqbh());
                coverDTO.setAjbh(thisFile.getAjbh());
//                coverDTO.setArchivename("治安管理处罚卷");
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

    @RequestMapping(value = "/saveBackcover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "保存卷宗封底信息", operType = OperLog.type.InsertOrUpdate)
    public String saveBackCover(@RequestParam("newFile") MultipartFile newFile, String backcoverid, String fileid, String backcover) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(backcoverid) || StringUtils.isEmpty(backcover)) {
                throw new Exception("你传nm呢？");
            }
            //查询该文件对应的file表
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));
            newFile(thisFile, newFile);
            FunArchiveBackcoverDTO backcoverDTO = JSON.parseObject(backcover, FunArchiveBackcoverDTO.class);
            int backCoverId = Integer.parseInt(backcoverid);
            if (backCoverId > 0) {
                //更新
                backcoverDTO.setId(backCoverId);
                fileManipulationService.updateFunArchiveBackCoverById(backcoverDTO);
            } else {
                SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
                backcoverDTO.setAuthor(userNow.getUsername());
                backcoverDTO.setAuthorid(userNow.getId());
                backcoverDTO.setAuthorxm(userNow.getXm());
                backcoverDTO.setJqbh(thisFile.getJqbh());
                backcoverDTO.setAjbh(thisFile.getAjbh());
                backcoverDTO.setArchivesfcid(thisFile.getArchivesfcid());
                backcoverDTO.setArchiveseqid(thisFile.getArchiveseqid());
                backcoverDTO.setArchivetypeid(thisFile.getArchivetypeid());
                backcoverDTO.setArchivefileid(thisFile.getId());
                backcoverDTO.setFilecode(thisFile.getFilecode());
                fileManipulationService.insertFunArchiveBackCover(backcoverDTO);
            }
            fileManipulationService.updateFunArchiveFileDTO(thisFile);
            fileManipulationService.updateRecordsIscoverimg(thisFile.getArchiverecordid());
            reValue.put("value", backcoverDTO.getId());
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    @RequestMapping(value = "/loadBackCover", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "加载卷宗封底信息", operType = OperLog.type.InsertOrUpdate)
    public String loadBackCover() {
        JSONObject reValue = new JSONObject();
        try {
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
    @recordTidy
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
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "保存文书目录", operType = OperLog.type.InsertOrUpdate)
    public String saveFunArchiveRecordindex(@RequestParam("newFile") MultipartFile newFile, String fileid, String indexinfo, String indexid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(indexid)) {
                throw new Exception("你传nm呢？");
            }
            int oriId = Integer.parseInt(indexid);
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));
            newFile(thisFile, newFile);
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
            fileManipulationService.updateFunArchiveFileDTO(thisFile);
            fileManipulationService.updateRecordsIscoverimg(thisFile.getArchiverecordid());
            reValue.put("value", Recordindex.getId());
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
    @recordTidy
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


    /**
     * 添加上传
     *
     * @param fileName  文件名
     * @param fileOrder 文件顺序
     * @param recordId  文书id
     * @param maxOrder  最大顺序
     * @param newFile   文件
     * @return |
     * @author MrLu
     * @createTime 2020/12/25 9:55
     */
    @RequestMapping(value = "/addUpLoadRecordFile", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "添加上传图片", operType = OperLog.type.INSERT)
    public String addUpLoadRecordFile(String fileName, String fileOrder,
                                      Integer recordId, Integer maxOrder,
                                      @RequestParam("newFile") MultipartFile newFile) {
        JSONObject reValue = new JSONObject();
        try {
            FunArchiveRecordsDTO thisRecord = fileManipulationService.selectFunArchiveRecordsDTOById(recordId);
            //将文件上传记录保存至数据库
            FunArchiveFilesDTO newRecordFile = new FunArchiveFilesDTO();
            newFile(newRecordFile, newFile);//图片上传 不用了
            newRecordFile.setJqbh(thisRecord.getJqbh());
            newRecordFile.setAjbh(thisRecord.getAjbh());
            newRecordFile.setFiletype(0);
            newRecordFile.setArchiverecordid(recordId);//所属文书id
            newRecordFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
            newRecordFile.setFilename(fileName);//文件名称
            newRecordFile.setArchiveseqid(thisRecord.getArchiveseqid());//seq
            newRecordFile.setArchivesfcid(thisRecord.getArchivesfcid());//sfc
            newRecordFile.setThisorder(maxOrder + Integer.parseInt(fileOrder) + 1);//文件的顺序
            newRecordFile.setBjzid(0);
            fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
            //其它同文书也要添加这个图片
            //查询这个文书在其它seq的版本
            List<FunArchiveRecordsDTO> records = fileManipulationService.selectFunArchiveRecordsByUUID(thisRecord.getRecorduuid());
            for (FunArchiveRecordsDTO tR : records) {
                if (tR.getId().equals(recordId)) {
                    //把自己掠过
                    continue;
                }
                newRecordFile.setArchiveseqid(tR.getArchiveseqid());
                newRecordFile.setArchivesfcid(tR.getArchivesfcid());
                newRecordFile.setArchivetypeid(tR.getArchivetypeid());
                newRecordFile.setArchiverecordid(tR.getId());
                fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
            }
            reValue.put("value", newRecordFile);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    @RequestMapping(value = "/addUpLoadRecordFileList", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "添加上传图片", operType = OperLog.type.INSERT)
    public String addUpLoadRecordFileList(
            Integer recordId,
            @RequestParam("newFile") MultipartFile[] newFile) {
        JSONObject reValue = new JSONObject();

        try {
            SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
            JSONObject uploadRe = SftpUtil.uploadList(newFile);
            FunArchiveRecordsDTO thisRecord = fileManipulationService.selectFunArchiveRecordsDTOById(recordId);
            //上传全部成功
            int thisOrder = fileManipulationService.selectFilecCountByRecordid(recordId) + 1;
            if (0 == uploadRe.getInteger("errno")) {

                for (Object thisImgObj :
                        uploadRe.getJSONArray("data")) {
                    JSONObject thisImg = (JSONObject) thisImgObj;
                    FunArchiveFilesDTO newRecordFile = new FunArchiveFilesDTO();
                    newRecordFile.setServerip(thisImg.getString("host"));//服务器ip
                    newRecordFile.setFileurl("/" + thisImg.getString("href"));//图片路径
                    newRecordFile.setAuthor(userNow.getXm());//用户名
                    newRecordFile.setAuthorid(userNow.getId());//用户id
                  //  newRecordFile.setThisorder();
                    newRecordFile.setFilecode("R" + UUID.randomUUID());//文件代码
                    newRecordFile.setIsdowland(1);//是否已经下载
                    newRecordFile.setIsdelete(0);//是否被删除
                    newRecordFile.setIsshow(0);//是否显示
                    newRecordFile.setIsazxt(1);//是否来自案宗 不是
                    newRecordFile.setJqbh(thisRecord.getJqbh());
                    newRecordFile.setAjbh(thisRecord.getAjbh());
                    newRecordFile.setFiletype(0);
                    newRecordFile.setArchiverecordid(recordId);//所属文书id
                    newRecordFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
                    newRecordFile.setFilename(thisImg.getString("name"));//文件名称
                    newRecordFile.setArchiveseqid(thisRecord.getArchiveseqid());//seq
                    newRecordFile.setArchivesfcid(thisRecord.getArchivesfcid());//sfc
                    newRecordFile.setThisorder(thisOrder+thisImg.getInteger("shunxu"));//文件的顺序
                    newRecordFile.setOriginurl(newRecordFile.getFileurl());
                    newRecordFile.setBjzid(0);
                    fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);

                    List<FunArchiveRecordsDTO> records = fileManipulationService.selectFunArchiveRecordsByUUID(thisRecord.getRecorduuid());
                    for (FunArchiveRecordsDTO tR : records) {
                        if (tR.getId().equals(recordId)) {
                            //把自己掠过
                            continue;
                        }
                        newRecordFile.setArchiveseqid(tR.getArchiveseqid());
                        newRecordFile.setArchivesfcid(tR.getArchivesfcid());
                        newRecordFile.setArchivetypeid(tR.getArchivetypeid());
                        newRecordFile.setArchiverecordid(tR.getId());
                        fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
                    }
                }
                reValue.put("value", "");
            } else {
                //上传有失败
                reValue.put("message", "error");
            }
            //将文件上传记录保存至数据库

//            reValue.put("value", newRecordFile);
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
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "上传图片", operType = OperLog.type.INSERT)
    public String upLoadRecordFiles(String fileName, String fileOrder,
                                    String recordid,
                                    @RequestParam("newFile") MultipartFile newFile) {
        JSONObject reValue = new JSONObject();
        try {
            //如果有附件的 触发附件上传
            if (null != newFile) {
                String[] recordidAry = recordid.split(",");
                for (String thisRid :
                        recordidAry) {
                    int recordId = Integer.parseInt(thisRid);
                    FunArchiveRecordsDTO thisRecord = fileManipulationService.selectFunArchiveRecordsDTOById(recordId);
                    FunArchiveFilesDTO newRecordFile = new FunArchiveFilesDTO();
                    //上传文件
                    newFile(newRecordFile, newFile);
                    newRecordFile.setJqbh(thisRecord.getJqbh());
                    newRecordFile.setAjbh(thisRecord.getAjbh());
                    newRecordFile.setThisorder(Integer.parseInt(fileOrder));//文件的顺序
                    newRecordFile.setArchiverecordid(recordId);//所属文书id
                    newRecordFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
                    newRecordFile.setFilename(fileName);//文件名称
                    newRecordFile.setArchiveseqid(thisRecord.getArchiveseqid());//seq
                    newRecordFile.setArchivesfcid(thisRecord.getArchivesfcid());//sfc
                    newRecordFile.setFiletype(0);
                    newRecordFile.setBjzid(0);

                    fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
                }
                reValue.put("message", "success");
            } else {
                throw new Exception("低情商:你传尼玛呢弟弟 高情商:？");
            }

        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }


    @RequestMapping(value = "/upLoadRecordFilesList", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "上传图片", operType = OperLog.type.INSERT)
    public String upLoadRecordFilesList(
            String recordid,
            @RequestParam("newFile") MultipartFile[] newFile) {
        JSONObject reValue = new JSONObject();
        try {
            //如果有附件的 触发附件上传
            if (null != newFile&&newFile.length>0) {
                String[] recordidAry = recordid.split(",");
                //上传
                JSONObject uploadRe = SftpUtil.uploadList(newFile);

                if (0 == uploadRe.getInteger("errno")) {
                    SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户

                    int fileOrder=0;
                    for (String thisRid :
                            recordidAry) {
                        int recordId = Integer.parseInt(thisRid);
                        FunArchiveRecordsDTO thisRecord = fileManipulationService.selectFunArchiveRecordsDTOById(recordId);

                        for (Object thisImgObj :
                                uploadRe.getJSONArray("data")) {
                            JSONObject thisImg = (JSONObject) thisImgObj;
                            FunArchiveFilesDTO newRecordFile = new FunArchiveFilesDTO();
                            //上传文件
                            newRecordFile.setServerip(thisImg.getString("host"));//服务器ip
                            newRecordFile.setFileurl("/" + thisImg.getString("href"));//图片路径
                            newRecordFile.setAuthor(userNow.getXm());//用户名
                            newRecordFile.setAuthorid(userNow.getId());//用户id
                            newRecordFile.setFilecode("R" + UUID.randomUUID());//文件代码
                            newRecordFile.setIsdowland(1);//是否已经下载
                            newRecordFile.setIsdelete(0);//是否被删除
                            newRecordFile.setIsshow(0);//是否显示
                            newRecordFile.setIsazxt(1);//是否来自案宗 不是
                            newRecordFile.setJqbh(thisRecord.getJqbh());
                            newRecordFile.setAjbh(thisRecord.getAjbh());
                            newRecordFile.setThisorder(fileOrder++);//文件的顺序
                            newRecordFile.setArchiverecordid(thisRecord.getId());//所属文书id
                            newRecordFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
                            newRecordFile.setFilename(thisImg.getString("name"));//文件名称
                            newRecordFile.setArchiveseqid(thisRecord.getArchiveseqid());//seq
                            newRecordFile.setArchivesfcid(thisRecord.getArchivesfcid());//sfc
                            newRecordFile.setThisorder(thisImg.getInteger("shunxu"));//文件的顺序
                            newRecordFile.setFiletype(0);
                            newRecordFile.setBjzid(0);
                            newRecordFile.setOriginurl(newRecordFile.getFileurl());
                            fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
                        }

                    }

                    reValue.put("message", "success");
                }else {
                    reValue.put("message", "error");
                }


            } else {
                throw new Exception("低情商:你传尼玛呢弟弟 高情商:？");
            }

        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    @RequestMapping(value = "/selectFileByFileId", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "按照id查询文件", operType = OperLog.type.SELECT)
    public String selectFileByFileId(Integer fileid) {
        JSONObject reValue = new JSONObject();
        try {
            if (null == fileid) {
                throw new Exception("你搞我呢？");
            }
            reValue.put("value", fileManipulationService.selectFunArchiveFilesDTOById(fileid));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    @RequestMapping(value = "/reUpLoadFile", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "重新上传图片", operType = OperLog.type.UPDATE)
    public String reUpLoadFile(Integer fileId,
                               @RequestParam("newFile") MultipartFile newFile) {
        JSONObject reValue = new JSONObject();
        try {
            FunArchiveFilesDTO oriFile = fileManipulationService.selectFunArchiveFilesDTOById(fileId);
            //把原有的干了
            oriFile.setIsshow(1);
            fileManipulationService.updateFunArchiveFileDTO(oriFile);
            String oriFileCode = oriFile.getFilecode();//原有的文件代码
            //传新的  夺舍它
            newFile(oriFile, newFile);//上传个新的 并对原有值做操作  会生成新的文件代码
            fileManipulationService.insertFunArchiveFilesDTO(oriFile);
            //把同一文件并没有送检的也都夺舍了
            for (FunArchiveFilesDTO otherFile :
                    fileManipulationService.selectFilesByCodeNotSend(oriFileCode)) {
                if (fileId.equals(otherFile.getId())) {
                    //把自己略过
                    continue;
                }
                otherFile.setIsshow(1);//原有的标记为不显示
                fileManipulationService.updateFunArchiveFileDTO(otherFile);
                //再更新其它的
                otherFile.setServerip(oriFile.getServerip());//服务器ip
                otherFile.setFilecode(oriFile.getFilecode());//文件代码
                otherFile.setIsdowland(1);//是否已经下载
                otherFile.setIsdelete(0);//是否被删除
                otherFile.setIsshow(0);//是否显示
                otherFile.setIsazxt(1);//是否来自案宗 不是
                otherFile.setFileurl(oriFile.getFileurl());//图片路径
                otherFile.setOriginurl(oriFile.getOriginurl());//服务器上的路径
                fileManipulationService.insertFunArchiveFilesDTO(otherFile);
            }
            //替换所有的该文件
            reValue.put("value", oriFile.getServerip() + oriFile.getFileurl());
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
     * @author MrLu
     * @createTime 2020/11/6 9:26
     */
    @RequestMapping(value = "/selectArchiveRecordPage", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "文书分页查询", operType = OperLog.type.SELECT)
    public Map<String, Object> selectArchiveRecordPage(Integer offset, Integer limit, String params) {
        Map<String, Object> reMap = new HashMap<>();
        try {
            //参数列表
            JSONObject pJsonObj = JSON.parseObject(params);
            pJsonObj.put("pageStart", String.valueOf((offset - 1) * limit));
            pJsonObj.put("pageEnd", String.valueOf((offset) * limit));
            pJsonObj.put("archiveseqid", pJsonObj.getInteger("seqid"));//整理次序id
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
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "按照文书代码移动文件至所属文书", operType = OperLog.type.SELECT)
    public String moveFiles(String fileOrder, String seqId, String recordid, String orirecordid) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileOrder) || StringUtils.isEmpty(seqId) || StringUtils.isEmpty(recordid)) {
                throw new Exception("给一个? 自己体会");
            }
            int recordId = Integer.parseInt(recordid);
            String[] fileOrders = fileOrder.split(",");
            //该文书没有图片了
            int maxOrder = fileManipulationService.selectFileMaxOrder(recordId);
            List<FunArchiveFilesDTO> moveFiles = fileManipulationService.selectRecordFilesByFileCodes(fileOrders, Integer.parseInt(orirecordid));
            for (FunArchiveFilesDTO thisMoveFile :
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

    /**
     * 上传图片
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/25 15:33
     */
    private void newFile(FunArchiveFilesDTO oriFile, MultipartFile newFile) throws Exception {
        //这里不判断文件后缀 因为vsftp中没有给x权限  所以即使有问题的文件也执行不了
        String orgFileName = newFile.getOriginalFilename();
        String orgFileSuffix = orgFileName.substring(orgFileName.lastIndexOf("."));
        //上传的文件名
        String osFileName = UUID.randomUUID() + orgFileSuffix;
        //上传路径 年与日
        Calendar now = Calendar.getInstance();
        //最终上传路径
        //
        //得到上传完成后的文件名
        String version = GlobalUtil.getGlobal("ftpPortForWindow");
        String jdlj = "";
        String serverIp = "";
        if ("city".equals(version)) {
            //地市版为window服务器
            jdlj = FtpUtil.uploadImage(osFileName, newFile.getInputStream());
            serverIp = GlobalUtil.getGlobal("tomcatFinUrl");//服务器ip地址
            oriFile.setOriginurl(GlobalUtil.getGlobal("ftpFileUrlForWindow") + "/" + jdlj);//服务器上的路径
        } else if ("provinceTest".equals(version)) {
            //省厅培训版本
            String uploadFile = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH);
            jdlj = SftpUtil.upLoad(osFileName, uploadFile, "img", newFile.getInputStream());
            serverIp = GlobalUtil.getGlobal("nginxFinUrl");//服务器ip地址
            oriFile.setOriginurl(GlobalUtil.getGlobal("ftpFileUrlForLinux") + "/" + jdlj);//服务器上的路径
        } else {
            //省厅版为linux服务器
            String uploadFile = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH);
            jdlj = SftpUtil.upLoad(osFileName, uploadFile, "img", newFile.getInputStream());
            serverIp = GlobalUtil.getGlobal("nginxFinUrl");//服务器ip地址
            oriFile.setOriginurl(GlobalUtil.getGlobal("ftpFileUrlForLinux") + "/" + jdlj);//服务器上的路径
        }
//        String jdlj = SftpUtil.upLoad(osFileName, uploadFile, "img", newFile.getInputStream());

        SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
        oriFile.setServerip(serverIp);//服务器ip
        oriFile.setAuthor(userNow.getXm());//用户名
        oriFile.setAuthorid(userNow.getId());//用户id
        oriFile.setFilecode("R" + UUID.randomUUID());//文件代码
        oriFile.setIsdowland(1);//是否已经下载
        oriFile.setIsdelete(0);//是否被删除
        oriFile.setIsshow(0);//是否显示
        oriFile.setIsazxt(1);//是否来自案宗 不是
        oriFile.setFileurl("/" + jdlj);//图片路径

    }


    /**
     * 上传多张图片
     *
     * @param oriFile
     * @param newFile
     * @return |
     * @author MrLu
     * @createTime 2020/12/25 15:33
     */
    private void newFileList(FunArchiveFilesDTO oriFile, MultipartFile[] newFile) throws Exception {
        //省厅版为linux服务器


        JSONObject getObj = uploadList(newFile);
    }
    //uploadList


    /**
     * 更改文书内的文件顺序
     *
     * @param paramjson
     * @return |
     * @author MrLu
     * @createTime 2020/12/31 9:52
     */
    @RequestMapping(value = "/changeFileOrderOnTime", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "更改文书内的文件顺序", operType = OperLog.type.SELECT)
    public String changeFileOrderOnTime(String paramjson) {
        JSONObject reValue = new JSONObject();
        try {
            JSONObject paramJsonObj = JSONObject.parseObject(paramjson);
            String fileCode = paramJsonObj.getString("fileCode");
            Integer fileOrder = paramJsonObj.getInteger("fileOrder");
            Integer recordId = paramJsonObj.getInteger("recordId");
            Integer seqId = paramJsonObj.getInteger("seqId");
            if (fileOrder > 0) {
                String prevFileCode = paramJsonObj.getString("prevFileCode");
                FunArchiveFilesDTO prevFile = fileManipulationService.selectFilesByFileCode(recordId, prevFileCode);
                fileOrder = prevFile.getThisorder() + 1;
            }
            //再往后的所有顺序+1
            fileManipulationService.updateOrderByRecordId(recordId, fileOrder, fileCode);
            //自己顺序改为对应数
            FunArchiveFilesDTO record = new FunArchiveFilesDTO();
            record.setFilecode(fileCode);
            record.setThisorder(fileOrder < 0 ? 1 : fileOrder);
            record.setArchiveseqid(seqId);

            fileManipulationService.updateFileByFileCode(record);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    /**
     * 计算卷的页数
     *
     * @param
     * @return |
     * @author Mrlu
     * @createTime 2021/2/23 10:54
     */
    @RequestMapping(value = "/getArchivePageNum", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "查询文书卷内的页数", operType = OperLog.type.SELECT)
    public String getArchivePageNum(String typeid) {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", fileManipulationService.selectFilesCountByTypeid(Integer.parseInt(typeid)));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }

    @RequestMapping(value = "/getRecordPageNum", method = {RequestMethod.GET,
            RequestMethod.POST})
    @ResponseBody
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "查询文书的页数", operType = OperLog.type.SELECT)
    public String getRecordPageNum(String recordid) {
        JSONObject reValue = new JSONObject();
        try {
            reValue.put("value", fileManipulationService.selectFilecCountByRecordid(Integer.parseInt(recordid)));
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
