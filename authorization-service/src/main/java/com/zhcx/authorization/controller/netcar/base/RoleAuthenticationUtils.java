package com.zhcx.authorization.controller.netcar.base;

import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.netcarbasic.facade.netcar.CompanyService;
import com.zhcx.netcarbasic.facade.netcar.CompanyServiceService;
import com.zhcx.netcarbasic.pojo.netcar.NetcarBaseInfoCompany;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/4/28 0028 16:54
 **/
@Component
public class RoleAuthenticationUtils {

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    public String getCompanyId(HttpServletRequest request, String companyId) throws Exception{

        AuthUserResp user = sessionHandler.getUser(request);
        //网约车企业管理员
        if (null != user && StringUtils.equals(user.getUserType(),"05")) {
            if (user.getCorpId() != null && user.getCorpId() != 0L) {
                String id = companyServiceService.selectCompanyIdByYZuuid(user.getCorpId());
                if(StringUtils.isBlank(id)){
                    throw new Exception("公司不存在或部级平台无数据");
                }
                if (StringUtils.isNotBlank(companyId) && !StringUtils.equals(id, companyId)) {
                    throw new Exception("查询参数有误或无权限");
                }
                companyId = id;
            }
        }
        return companyId;
    }

    /**
     * 添加过滤条件：服务机构信息存在的公司
     */
    public String filterCompanyIdCondition(){
        List<String> companyIdList = companyServiceService.selectAllCompanyIdList();
        return StringUtils.join(companyIdList, ",");
    }


}
