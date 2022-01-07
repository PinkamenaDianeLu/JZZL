package com.module.CaseManager.Services.Impl;

import com.bean.jzgl.DTO.FunArchiveSFCDTO;
import com.factory.BaseFactory;
import com.mapper.jzgl.FunArchiveSFCDTOMapper;
import com.mapper.jzgl.FunCasePeoplecaseDTOMapper;
import com.module.CaseManager.Services.CaseSearchService;
import com.util.MapFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:03
 * @describe
 */
@Service
public class CaseSearchImpl extends BaseFactory implements CaseSearchService {


    @Resource
    FunCasePeoplecaseDTOMapper funCasePeoplecaseDTOMapper;
    @Resource
    FunArchiveSFCDTOMapper funArchiveSFCDTOMapper;

    @Override
    public List<Object> selectPeopleCasePage(Map<String, Object> map) throws Exception {
        if (null != map.get("sfcTimebegin") || null != map.get("sfcTimeend")) {
            map.put("isZlTime", true);
        }

        List<Map<String, Object>> cases = funCasePeoplecaseDTOMapper.selectPeopleCaseByUseridPage(map);
      /*  for (Map<String, Object> thisCase :
                cases) {
            Integer caseInfoId = Integer.valueOf(String.valueOf(Optional.ofNullable(thisCase.get("CASEINFOID")).orElse(0)));
            FunArchiveSFCDTO lastSfc = funArchiveSFCDTOMapper.selectLastSfcByCaseId(caseInfoId);
            String sfcstate = "未送检";
            if (null == lastSfc) {
                sfcstate = "未送检";
            } else {
                if (1 == lastSfc.getArchivetype()) {
                    sfcstate = "治安管理处罚卷";
                } else {
                    sfcstate = lastSfc.getArchivename();
                    switch (lastSfc.getApproval()) {
                        case 0:
                            sfcstate += " 未审批";
                            break;
                        case 1:
                            sfcstate += " 已审批";
                            break;
                        default:
                            sfcstate += " 未审批";
                    }
                }
            }
            thisCase.put("sfcstate", sfcstate);
        }*/
        return MapFactory.mapToListBean(cases);
    }


    /*
     *
     *
     * 查询一个案件除基础卷外最新操作的卷的状态
     * @author MrLu
     * @param  caseinfoid  案件id
     * @createTime  2021/7/2 10:06
     * @return    |
     *
     * FunArchiveSFCDTO
     * */
    @Override
    public int selectPeopleCasePageCount(Map<String, Object> map) throws Exception {

        return funCasePeoplecaseDTOMapper.selectPeopleCaseByUseridCount(map);
    }


}
