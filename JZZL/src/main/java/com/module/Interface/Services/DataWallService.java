package com.module.Interface.Services;

import java.util.Map;

/**
 * @author MrLu
 * @createTime 2021/1/26 16:47
 * @describe
 */
public interface DataWallService {
     /**
     * 查询案件数
     * @author MrLu
     * @param 
     * @createTime  2021/1/26 16:56
     * @return    |  
      */
    Integer selectCaseCount(Map<String,Object> map);


     /**
     * 查询文书整理数
     * @author MrLu
     * @param 
     * @createTime  2021/1/26 17:32
     * @return    |  
      */
    Integer selectArchivesRearranged(Map<String,Object> map);
    
    
     /**
     * 查询归档数
     * @author MrLu
     * @param 
     * @createTime  2021/1/26 17:32
     * @return    |  
      */
    Integer selectArchives(Map<String,Object> map);
    
    
}
