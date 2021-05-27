package com.module.Interface.Services.Impl;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.mapper.jzgl.FunArchiveSFCDTOMapper;
import com.module.Interface.Services.InterfaceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MrLu
 * @createTime 2021/5/27 10:50
 * @describe 接口用
 */
@Service
public class InterfaceImpl implements InterfaceService {

    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;
    @Override
    public FunArchiveSFCDTO selectArchiveSfcBySeqid(Integer seqid) {
        return funArchiveSFCDTOMapper.selectArchiveSfcBySeqid(seqid);
    }

    @Override
    public void updateArchiveSfcById(FunArchiveSFCDTO funArchiveSFCDTO) {
        //用个新对象 防止传入的参数会更改其他数据
        FunArchiveSFCDTO sfc=new FunArchiveSFCDTO();
        sfc.setId(funArchiveSFCDTO.getId());
        sfc.setIssend(1);
        funArchiveSFCDTOMapper.updateByPrimaryKeySelective(funArchiveSFCDTO);
    }
}
