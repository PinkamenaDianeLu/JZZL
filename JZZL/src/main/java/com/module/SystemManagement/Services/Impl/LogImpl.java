package com.module.SystemManagement.Services.Impl;

import com.bean.jzgl.SysLogs;
import com.mapper.jzgl.SysLogsMapper;
import com.module.SystemManagement.Services.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Mrlu
 * @createTime 2020/5/19
 * @describe
 */
@Service
public class LogImpl implements LogService {
    @Resource
    SysLogsMapper sysLogsMapper;
    @Override
    public void insertLog(SysLogs record) {
        sysLogsMapper.insertSelective(record);
    }
}
