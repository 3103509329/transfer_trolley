package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.basicdata.domain.taxi.CreditEnterpriseEvaluationRealtimeDo;
import com.zhcx.basicdata.facade.taxi.CreditEnterpriseEvaluationRealtimeService;
import com.zhcx.basicdata.param.taxi.CreditEnterpriseEvaluationRealtimeVo;
import com.zhcx.basicdata.response.taxi.CreditEnterpriseEvaluationRealtimeResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: public-transport
 * @description: 企业预警监控 业务接口
 * @author: TD
 * @create: 2020-05-11 16:03
 */

@RestController
@RequestMapping("/taxi/credit/enterpriseEvaluationRealtime")
@Api(value = "企业预警监控", tags = "企业预警监控")
public class CreditEnterpriseEvaluationRealtimeController {

    private Logger log = LoggerFactory.getLogger(CreditEnterpriseEvaluationRealtimeController.class);

    @Autowired
    private CreditEnterpriseEvaluationRealtimeService creditEnterpriseEvaluationRealtimeService;

    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询企业预警监控（带分页）", notes = "CreditEnterpriseEvaluationRealtimeVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryEnterpriseEvaluationRealtimePage(HttpServletRequest request, @ModelAttribute CreditEnterpriseEvaluationRealtimeVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditEnterpriseEvaluationRealtimeResp> pageInfo = null;
        CreditEnterpriseEvaluationRealtimeDo domodel = new CreditEnterpriseEvaluationRealtimeDo();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(authUser.getCorpId());
            }
        }

        try {
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditEnterpriseEvaluationRealtimeService.queryCreditEnterpriseEvaluationRealtimePage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询企业预警监控成功");
            return resp;
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询企业预警监控异常");
            return resp;
        }
    }


    /**
     * 返回所有指标项分数
     * param evaluationYear 考核周期
     * param objectType 考核类型 1企业 2驾驶员
     * param identificationNum 证件号
     */
    @GetMapping("/findItemScoreMap")
    @ApiOperation(value = "回所有指标项分数", notes = "CreditEnterpriseEvaluationRealtimeVo")
    public MessageResp findItemScoreMap(@RequestParam(value = "evaluationYear") String evaluationYear,
                                        @RequestParam(value = "objectType") Short objectType,
                                        @RequestParam(value = "subjectId") Long subjectId) {

        MessageResp resp = new MessageResp();
        try {
            Map<String, Map<String, Integer>> resMap = creditEnterpriseEvaluationRealtimeService.findItemScoreMap(evaluationYear, objectType, subjectId);
            resp.setData(resMap);
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("获取所有指标项分数成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("获取所有指标项分数异常");
            return resp;
        }

        return resp;
    }


    /**
     * 企业预警监控记录导出
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditEnterpriseEvaluationRealtimeVo param) throws Exception {
        CreditEnterpriseEvaluationRealtimeDo domodel = new CreditEnterpriseEvaluationRealtimeDo();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(authUser.getCorpId());
            }
        }

        BeanUtils.copyProperties(param, domodel);
        //获取数据
        List<CreditEnterpriseEvaluationRealtimeResp> list = creditEnterpriseEvaluationRealtimeService.queryCreditEnterpriseEvaluationRealtime(domodel);
        String newTime = DateUtil.getYMDHMSFormat(new Date());
        String[][] content = new String[list.size()][];
        //excel标题
        String[] title = {"企业名称", "企业简称", "组织机构代码", "法定代表人", "考核周期/年", "信誉等级", "综合评分"};
        //excel文件名
        String fileName = "企业预警监控记录" + newTime + ".xls";
        //sheet名
        String sheetName = "企业预警监控记录";
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            CreditEnterpriseEvaluationRealtimeResp obj = list.get(i);
            content[i][0] = obj.getCorpName();
            content[i][1] = obj.getCorpAbbr();
            content[i][2] = obj.getOrganizationName();
            content[i][3] = obj.getEgalRepName();
            content[i][4] = obj.getEvaluationYear();
            content[i][5] = obj.getCreditGrade();
            content[i][6] = obj.getComprehensiveScore().toString();
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtils.getHSSFWorkbook2(sheetName, sheetName, title, content, null);
        //响应到客户端
        try {
            ExcelUtils.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
