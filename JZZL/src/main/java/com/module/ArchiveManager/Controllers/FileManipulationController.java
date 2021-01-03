package com.module.ArchiveManager.Controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bean.jzgl.DTO.*;
import com.bean.jzgl.Source.SysUser;
import com.config.annotations.OperLog;
import com.config.annotations.recordTidy;
import com.factory.BaseFactory;
import com.module.ArchiveManager.Services.FileManipulationService;
import com.module.SystemManagement.Services.UserService;
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
    @recordTidy
    @OperLog(operModul = operModul, operDesc = "保存卷宗封皮信息", operType = OperLog.type.InsertOrUpdate)
    public String saveCover(String coverid, String fileid, String cover) {
        JSONObject reValue = new JSONObject();
        try {
            if (StringUtils.isEmpty(fileid) || StringUtils.isEmpty(coverid) || StringUtils.isEmpty(cover)) {
                throw new Exception("你传nm呢？");
            }
            FunArchiveFilesDTO thisFile = fileManipulationService.selectFunArchiveFilesDTOById(Integer.parseInt(fileid));

            FunArchiveCoverDTO coverDTO = JSON.parseObject(cover, FunArchiveCoverDTO.class);

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
            newFile(newRecordFile, newFile);
            newRecordFile.setJqbh(thisRecord.getJqbh());
            newRecordFile.setAjbh(thisRecord.getAjbh());
            newRecordFile.setFiletype(0);
            newRecordFile.setArchiverecordid(recordId);//所属文书id
            newRecordFile.setArchivetypeid(thisRecord.getArchivetypeid());//文书类型id
            newRecordFile.setFilename(fileName);//文件名称
            newRecordFile.setArchiveseqid(thisRecord.getArchiveseqid());//seq
            newRecordFile.setArchivesfcid(thisRecord.getArchivesfcid());//sfc
            newRecordFile.setThisorder(maxOrder + Integer.parseInt(fileOrder) + 1);//文件的顺序
            fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
            reValue.put("value", newRecordFile);
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
                int recordId = Integer.parseInt(recordid);
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

                fileManipulationService.insertFunArchiveFilesDTO(newRecordFile);
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
            oriFile.setIsshow(oriFile.getIsshow() + 1);
            fileManipulationService.updateFunArchiveFileDTO(oriFile);
            //传新的  夺舍它
            newFile(oriFile, newFile);//上传个新的 并对原有值做操作
            fileManipulationService.insertFunArchiveFilesDTO(oriFile);
            reValue.put("value", oriFile.getServerip()+oriFile.getFileurl());
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
        String uploadFile = now.get(Calendar.YEAR) + "/" + (now.get(Calendar.MONTH) + 1) + now.get(Calendar.DAY_OF_MONTH);
        //得到上传完成后的文件名
        String jdlj = SftpUtil.upLoad(osFileName, uploadFile, "img", newFile.getInputStream());
        SysUser userNow = userServiceByRedis.getUserNow(null);//获取当前用户
        String serverIp = GlobalUtil.getGlobal("nginxFinUrl");//服务器ip地址
        oriFile.setServerip(serverIp);//服务器ip
        oriFile.setAuthor(userNow.getXm());//用户名
        oriFile.setAuthorid(userNow.getId());//用户id
        oriFile.setFilecode("R" + UUID.randomUUID());//文件代码
        oriFile.setIsdowland(1);//是否已经下载
        oriFile.setIsdelete(0);//是否被删除
        oriFile.setIsshow(0);//是否显示
        oriFile.setIsazxt(1);//是否来自案宗 不是
        oriFile.setFileurl("/" + uploadFile + "/" + osFileName);//图片路径
        oriFile.setOriginurl(jdlj);//服务器上的路径
    }


     /**
     * 更改文书内的文件顺序
     * @author MrLu
     * @param paramjson
     * @createTime  2020/12/31 9:52
     * @return    |
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
            if (fileOrder>0){
                String prevFileCode = paramJsonObj.getString("prevFileCode");
                FunArchiveFilesDTO prevFile=  fileManipulationService.selectFilesByFileCode(recordId,prevFileCode);
                fileOrder=prevFile.getThisorder()+1;
            }
            //再往后的所有顺序+1
            fileManipulationService.updateOrderByRecordId(recordId,fileOrder,fileCode);
            //自己顺序改为对应数
            FunArchiveFilesDTO record =new FunArchiveFilesDTO();
            record.setFilecode(fileCode);
            record.setThisorder(fileOrder);
            record.setArchiveseqid(seqId);

            fileManipulationService.updateFileByFileCode(record);
            reValue.put("message", "success");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();
    }
}
