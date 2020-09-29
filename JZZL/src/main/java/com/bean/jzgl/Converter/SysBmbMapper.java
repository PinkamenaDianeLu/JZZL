package com.bean.jzgl.Converter;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SysBmbMapper {
    SysBmbMapper INSTANCE = Mappers.getMapper(SysBmbMapper.class);

}
