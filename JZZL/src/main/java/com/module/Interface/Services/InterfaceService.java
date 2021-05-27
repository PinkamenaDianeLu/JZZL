package com.module.Interface.Services;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;

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
}
