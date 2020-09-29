package com.module.SystemManagement.Services.Impl;

import com.bean.jzgl.DTO.SysBmbDTO;
import com.mapper.jzgl.SysBmbDTOMapper;
import com.module.SystemManagement.Services.BmbService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2020/9/29 16:22
 * @describe
 */
@Service
public class BmbImpl implements BmbService {
    @Resource
    SysBmbDTOMapper sysBmbDTOMapper;
    @Override
    public List<String> getBmbType() {
        return sysBmbDTOMapper.getBmbType();
    }

    @Override
    public List<SysBmbDTO> getBmbMapByType(String type) {
        return sysBmbDTOMapper.getBmbMapByType(type);
    }
}
