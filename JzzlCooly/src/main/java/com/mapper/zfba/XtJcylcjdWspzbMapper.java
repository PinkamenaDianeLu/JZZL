package com.mapper.zfba;



import com.bean.zfba.Source.WjJzsjb;
import com.bean.zfba.Source.XtJcylcjdWspzb;
import com.bean.zfba.Source.XtWjflb;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface XtJcylcjdWspzbMapper {
	List<XtJcylcjdWspzb> selectByLcjd(Map<String, Object> map);
	
	List<WjJzsjb> selectWjsjByJqbh(Map<String, Object> map);
	
	List<XtWjflb> selectWjidByInfo(Map<String, Object> map);
	
	List<Map<String, Object>> selectJzml(Map<String,Object> map);
	
	List<Map<String, Object>> selectJzwj(Map<String,Object> map);
}