package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.jzgl.Source.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author MrLu
 * @createTime 2020/9/29 15:18
 * @describe
 */
@Mapper
public interface SysUserMapper {
    SysUserMapper INSTANCE = Mappers.getMapper(SysUserMapper.class);

    SysUserDTO pcToPcDTO(SysUser fpc);

    SysUser pcDTOToPc(SysUserDTO fpc);


}
