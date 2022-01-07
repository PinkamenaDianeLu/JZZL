package com.module.Interface.Services;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.bean.jzgl.DTO.FunCasePeoplecaseDTO;

import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/5/27 10:50
 * @describe 接口用
 */
public interface InterfaceService {

    /**
     * 通过seqid查询对应的SFC
     * @author MrLu
     * @param seqid
     * @createTime  2021/5/27 10:49
     * @return    |
     */
    FunArchiveSFCDTO selectArchiveSfcBySeqid(Integer seqid);

     /**
     * 更新sfc表
     * @author MrLu
     * @param
     * @createTime  2021/5/27 14:58
     * @return    |
      */
    void updateArchiveSfcById(FunArchiveSFCDTO funArchiveSFCDTO);
    /**
     * 根据案件表id查询所有的关系
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/8 16:21
     */
    List<FunCasePeoplecaseDTO> selectRelationByCaseid(Integer caseinfoid);
}
