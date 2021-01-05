package com.ZfbaETL.BaseServer;

import com.bean.jzgl.DTO.EtlLogsDTO;
import com.bean.jzgl.DTO.EtlTablelogDTO;
import com.bean.jzgl.DTO.SysUserDTO;
import com.bean.zfba.SysUser;
import com.mapper.jzgl.EtlLogsDTOMapper;
import com.mapper.jzgl.EtlTablelogDTOMapper;
import com.mapper.jzgl.SysUserDTOMapper;
import com.mapper.zfba.SysUserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author MrLu
 * @createTime 2021/1/4 14:24
 * @describe
 */
@Service
public class BaseServer {

    @Resource
    EtlTablelogDTOMapper etlTablelogDTOMapper;
    @Resource
    EtlLogsDTOMapper etlLogsDTOMapper;

    /**
     * 查询某个值 （哎我不想取名了，自己明白就行了 d=====(￣▽￣*)b）
     *
     * @param tablename  表名
     * @param lastpkname 唯一标识值
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 14:38
     */
    public EtlTablelogDTO selectLastValue(String tablename, String lastpkname) {
        return etlTablelogDTOMapper.selectLastValue(StringUtils.upperCase(tablename), StringUtils.upperCase(lastpkname));
    }

    /**
     * 更新
     *
     * @param
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 15:53
     */
    public void updateLastValue(EtlTablelogDTO etlTablelogDTO) {
        etlTablelogDTOMapper.updateByPrimaryKeySelective(etlTablelogDTO);
    }

    /**
     * 插入成功日志
     *
     * @param record
     * @param ts     成功条数
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 15:43
     */
    public void insertSuccessLog(EtlLogsDTO record, Integer ts) {
        //成功
        record.setIssuccess(0);
        if (ts > 0) {
            record.setMessage("插入成功，更新" + ts + "条数据！");
        } else {
            record.setMessage("没有可更新的值");
        }

        record.setEndtime(new Date());
        etlLogsDTOMapper.insert(record);
    }

    /**
     * 插入失败日志
     *
     * @param record
     * @param error          错误信息  前1998位
     * @param lastpkstrvalue 源数据报错的key
     * @return |
     * @author MrLu
     * @createTime 2021/1/4 15:43
     */
    public void insertErrorLog(EtlLogsDTO record, String error, String lastpkstrvalue) {
        //失败
        record.setIssuccess(1);
        if (error.length() > 1998) {
            error=error.substring(0, 1998);
        }
        record.setMessage(error);
        record.setLastpkstrvalue(lastpkstrvalue);
        etlLogsDTOMapper.insert(record);
    }
}
