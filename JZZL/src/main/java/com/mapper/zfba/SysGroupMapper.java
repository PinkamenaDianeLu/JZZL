package com.mapper.zfba;

import com.bean.zfba.SysGroup;

import java.util.Map;

public interface SysGroupMapper {


	SysGroup selectByPrimaryKey(Map<String, String> map);
	 /**
	 * 通过单位代码查询单位信息
	 * @author MrLu
	 * @param dwmcdm
	 * @createTime  2021/3/13 15:45
	 * @return    |
	  */
	SysGroup selectGroupByDwdm(String dwmcdm);

}