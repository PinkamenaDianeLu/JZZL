package com.module.SFCensorship.Services;

import com.bean.jzgl.DTO.FunArchiveCoverDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunCaseInfoDTO;

/**
 * @author MrLu
 * @createTime 2020/10/23 10:01
 * @describe
 */
public interface FileManipulationService {

     /**
     * 通过fileid查询文书封皮
     * @author MrLu
     * @param fileid
     * @createTime  2020/10/23 10:17
     * @return  FunArchiveCoverDTO  |
      */
    FunArchiveCoverDTO selectFunArchiveCoverDTOByFileId (Integer fileid);

     /**
     * 根绝案件id查找案件
     * @author MrLu
     * @param caseId 案件id
     * @createTime  2020/10/23 10:39
     * @return  FunCaseInfoDTO  |
      */
    FunCaseInfoDTO selectFunCaseInfoDTOById (Integer caseId);

     /**
     * 根据送检卷id查询送检卷
     * @author MrLu
     * @param
     * @createTime  2020/10/23 10:56
     * @return    |
      */
    FunArchiveSFCDTO selectFunArchiveSFCDTOById (Integer sfcId);
     /**
     * 根据文书id查找文书
     * @author MrLu
     * @param
     * @createTime  2020/10/23 10:57
     * @return    |
      */
    FunArchiveRecordsDTO selectFunArchiveRecordsDTOById (Integer recordId);
}
