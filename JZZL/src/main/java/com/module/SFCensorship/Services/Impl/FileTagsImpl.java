package com.module.SFCensorship.Services.Impl;

import com.bean.jzgl.DTO.FunArchiveRecordsDTO;
import com.bean.jzgl.DTO.FunArchiveTagsDTO;
import com.mapper.jzgl.FunArchiveRecordsDTOMapper;
import com.mapper.jzgl.FunArchiveTagsDTOMapper;
import com.module.SFCensorship.Services.FileTagsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2020/12/7 19:11
 * @describe 标签管理相关
 */

@Service
public class FileTagsImpl implements FileTagsService {
    @Resource
    FunArchiveTagsDTOMapper funArchiveTagsDTOMapper;
    @Resource
    FunArchiveRecordsDTOMapper funArchiveRecordsDTOMapper;
    @Override
    public void createNewTags(FunArchiveTagsDTO funArchiveTagsDTO){
        funArchiveTagsDTOMapper.insert(funArchiveTagsDTO);
    }

    @Override
    public List<FunArchiveTagsDTO> selectArchiveTagsById(int archiveseqid, String filecode) {
        Map<String,Object> map=new HashMap<>();
        map.put("archiveseqid",archiveseqid);
        map.put("filecode",filecode);
        return funArchiveTagsDTOMapper.selectArchiveTagsById(map);
    }

    @Override
    public FunArchiveRecordsDTO selectFunArchiveRecordsDTOById (Integer recordId){
        return  funArchiveRecordsDTOMapper.selectByPrimaryKey(recordId);
    }
}
