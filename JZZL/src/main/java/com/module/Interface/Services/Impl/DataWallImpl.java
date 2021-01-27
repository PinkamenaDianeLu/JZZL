package com.module.Interface.Services.Impl;

import com.mapper.jzgl.FunArchiveSFCDTOMapper;
import com.mapper.jzgl.FunCaseInfoDTOMapper;
import com.module.Interface.Services.DataWallService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2021/1/26 16:48
 * @describe
 */
@Service
public class DataWallImpl implements DataWallService {
    @Resource
    FunCaseInfoDTOMapper funCaseInfoDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Override
    public Integer selectCaseCount(Map<String, Object> map) {
        return funCaseInfoDTOMapper.selectCaseCount(map);
    }

    @Override
    public Integer selectArchivesRearranged(Map<String, Object> map) {
        return funArchiveSFCDTOMapper.selectArchivesRearranged(map);
    }

    @Override
    public Integer selectArchives(Map<String, Object> map) {
        return null;
    }
}
