package com.mapper.zfba;


import com.bean.zfba.Source.XtJcyXMLJdpzb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface XtJcyXMLJdpzbMapper {               

	
	
    List<XtJcyXMLJdpzb> selectByFjdid(Map<String,Object> pzmap);
    
    List<Map<String, Object>> selectObject(Map<String,Object> map);
}