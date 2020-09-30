package com.module.SFCensorship.Services;

import com.bean.jzgl.Source.FunArchiveRecords;

/**
 * @author MrLu
 * @createTime 2020/9/30 10:47
 * @describe
 */
public interface RecordsService {

    FunArchiveRecords getFunArchiveRecordsById(Integer id);
}
