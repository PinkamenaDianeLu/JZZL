package com.module.SystemManagement.Services;

import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import com.bean.jzgl.DTO.FunArchiveSeqDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TempRemedialService {



    /**
     * 查询一个seq下的所有没被删除的文件
     * @author MrLu
     * @param archiveseqid
     * @createTime  2021/12/21 9:54
     * @return    |
     */
    List<FunArchiveFilesDTO> selectFileBySeq(Integer archiveseqid);

    /**
     * 查询一个案件下所有活跃的且未被送检的文书整理次序
     *
     * @param caseinfoid 案件id
     * @return |
     * @author MrLu
     * @createTime 2020/12/15 15:09
     */
    List<FunArchiveSeqDTO> selectActiveSeqByCaseInfoId(int caseinfoid);

    /**
     * 更新一个seq下的错误顺序 将非系统文书的负数顺序改为1
     * @author MrLu
     * @param archiveseqid
     * @createTime  2021/12/21 10:38
     * @return    |
     */
    void updateWrongOrderBySeq(Integer archiveseqid);


    /**
     * 查询文书名
     * @author MrLu
     * @param ids
     * @createTime  2021/12/21 10:54
     * @return    |
     */
    List<String> selectRecordNameByRecordIds( int[] ids);
    
    
     /**
     * 抽取案件
     * @author MrLu
     * @param 
     * @createTime  2021/12/29 16:21
     * @return    |  
      */
    Integer extractCase(String jqbh)throws Exception;
}
