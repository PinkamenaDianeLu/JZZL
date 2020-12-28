package com.module.ArchiveManager.Services;


import com.bean.jzgl.DTO.FunArchiveFilesDTO;
import com.bean.jzgl.DTO.FunArchiveRecordsDTO;

/**
 * 文书联动
 * 增
 * 删
 * 改
 */
public interface RecordComovement {

    /**
     * 子卷增加文书  基础卷也要增加
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 10:10
     */
    void subvolumeAddRecord(FunArchiveRecordsDTO priveReocrd, FunArchiveRecordsDTO newRecord);

    /**
     * 文书增加文件  其它所有文书也要增加
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 10:12
     */
    void addFile(FunArchiveRecordsDTO thisRecord, FunArchiveFilesDTO[] files);

    /**
     * 基础卷删除文书  子卷也联动删除
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 10:13
     */
    void BaseArchiveDeleteRecord(FunArchiveRecordsDTO record);

    void baseArchiveDeleteFile(FunArchiveFilesDTO file);


    /**
     * 移动文书的顺序 移动完后调用！
     * 把recordB移动到recordA 的上面
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 10:13
     */
    void moveRecord(FunArchiveRecordsDTO recordA, FunArchiveRecordsDTO recordB);

    /**
     * 移动文件的顺序 (仅限于同一文书内移动)
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 10:14
     */
    void moveFileOrder(int order, String fileCode);
    
     /**
     * 移动文书的顺序
     * @author MrLu
     * @param 
     * @createTime  2020/12/28 19:42
     * @return    |  
      */
    void moveRecordOrder(int order, String recordUuid);


    /**
     * 文书的重命名()
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 17:36
     */
    void renameRecord(String newName,String recordUuid);

    /**
     * 文件的重命名全跟着变
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2020/12/28 19:10
     */
    void renameFile(String newName, String fileCode);

}
