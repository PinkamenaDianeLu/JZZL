package com.module.SystemManagement.Services.Impl;

import com.bean.jzgl.DTO.SysLogsDTO;
import com.bean.jzgl.DTO.SysLogsLoginDTO;
import com.mapper.jzgl.SysLogsLoginDTOMapper;
import com.mapper.jzgl.SysLogsMapper;
import com.module.SystemManagement.Services.LogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MrLu
 * @createTime 2020/5/19
 * @describe
 */
@Service
public class LogImpl implements LogService {
    @Resource
    SysLogsMapper sysLogsMapper;
    @Resource
    SysLogsLoginDTOMapper sysLogsLoginDTOMapper;
    @Override
    public void insertLog(SysLogsDTO record) {
        sysLogsMapper.insertSelective(record);
    }

    @Override
    public void insertLogLogin(SysLogsLoginDTO sysLogsLoginDTO) {
        sysLogsLoginDTOMapper.insert(sysLogsLoginDTO);
    }

    @Override
    public SysLogsLoginDTO selectPrevLogHistory(Integer sysuserid) {
        return sysLogsLoginDTOMapper.selectPrevLogHistory(sysuserid);
    }

    @Override
    public void updateHistoryLog(Integer sysuserid) {
        sysLogsLoginDTOMapper.updateHistoryLog(sysuserid);
    }

}
