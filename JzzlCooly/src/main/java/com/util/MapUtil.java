/**
 * MapUtil.java
 * common.util
 *
 * Function�� TODO 
 *
 *   ver     date      		author
 * ��������������������������������������������������������������������
 *   		 2019��11��6�� 		����
 *
 * Copyright (c) 2019, TNT All Rights Reserved.
*/

package com.util;

import java.util.Map;
import java.util.Set;

/**
 * 用于读取配置文件
 * @author pl
 * @createTime 2020/8/28
 * @describe 补全map<list>
 */
public class MapUtil {
	
	public static Map<String, Object> nullToEmpty(Map<String, Object> map) {
		Set<String> set = map.keySet();
		if (set!=null&&!set.isEmpty()) {
			for (String key : set) {
				if (map.get(key)==null) {
					map.put(key, "");
				}
			}
		}
		return map;
		
	}

}

