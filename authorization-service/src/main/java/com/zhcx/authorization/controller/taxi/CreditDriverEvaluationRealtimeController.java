package com.zhcx.authorization.controller.taxi;

import com.github.pagehelper.PageInfo;
import com.zhcx.auth.pojo.AuthUserResp;
import com.zhcx.authorization.config.SessionConfig;
import com.zhcx.authorization.utils.Constants;
import com.zhcx.authorization.utils.DateUtil;
import com.zhcx.authorization.utils.MessageResp;
import com.zhcx.authorization.utils.PageBeanUtil;
import com.zhcx.authorization.utils.excel.ExcelUtils;
import com.zhcx.basicdata.domain.taxi.CreditDriverEvaluationRealtimeDo;
import com.zhcx.basicdata.facade.taxi.CreditDriverEvaluationRealtimeService;
import com.zhcx.basicdata.param.taxi.CreditDriverEvaluationRealtimeVo;
import com.zhcx.basicdata.response.taxi.CreditDriverEvaluationRealtimeResp;
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

/**
 * @program: public-transport
 * @description: 驾驶员预警监控 业务接口
 * @author: TD
 * @create: 2020-05-11 15:58
 */

@RestController
@RequestMapping("/taxi/credit/driverEvaluationRealtime")
@Api(value = "驾驶员预警监控管理", tags = "驾驶员预警监控 业务接口")
public class CreditDriverEvaluationRealtimeController {

    private Logger log = LoggerFactory.getLogger(CreditDriverEvaluationRealtimeController.class);

    @Autowired
    private CreditDriverEvaluationRealtimeService creditDriverEvaluationRealtimeService;


    @Autowired
    private SessionConfig.SessionHandler sessionHandler;

    @ApiOperation(value = "查询驾驶员预警监控（带分页）", notes = "参数CreditDriverEvaluationRealtimeVo")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public MessageResp queryDriverEvaluationRealtimePage(HttpServletRequest request, @ModelAttribute CreditDriverEvaluationRealtimeVo param) {
        MessageResp resp = new MessageResp();
        PageInfo<CreditDriverEvaluationRealtimeResp> pageInfo = null;
        CreditDriverEvaluationRealtimeDo domodel = new CreditDriverEvaluationRealtimeDo();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(authUser.getCorpId());
            }
        }

        try {
            BeanUtils.copyProperties(param, domodel);
            pageInfo = creditDriverEvaluationRealtimeService.queryCreditDriverEvaluationRealtimePage(domodel);
            resp.setPageBean(PageBeanUtil.createPageBean(pageInfo));
            resp.setData(pageInfo.getList());
            resp.setStatusCode("00");
            resp.setResult(Boolean.TRUE.toString());
            resp.setResultDesc("查询驾驶员预警监控成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            resp.setResult(Boolean.FALSE.toString());
            resp.setStatusCode("-50");
            resp.setResultDesc("查询驾驶员预警监控异常");
        }
        return resp;
    }

    /**
     * 驾驶员预警监控记录导出
     */
    @RequestMapping("/export")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response, @ModelAttribute CreditDriverEvaluationRealtimeVo param) throws Exception {
        CreditDriverEvaluationRealtimeDo domodel = new CreditDriverEvaluationRealtimeDo();

        AuthUserResp authUser = sessionHandler.getUser(request);
        if (!Constants.AUTH_USER_TYPE_SYS.equals(authUser.getUserType()) && !Constants.AUTH_USER_TYPE_ORG.equals(authUser.getUserType())) {
            if (authUser.getCorpId() != null && authUser.getCorpId() != 0L) {
                param.setCorpId(authUser.getCorpId());
            }
        }

        BeanUtils.copyProperties(param, domodel);
        //获取数据
        List<CreditDriverEvaluationRealtimeResp> list = creditDriverEvaluationRealtimeService.queryCreditDriverEvaluationRealtime(domodel);
        String newTime = DateUtil.getYMDHMSFormat(new Date());
        String[][] content = new String[list.size()][];
        //excel标题
        String[] title = {"姓名", "从业资格证号", "所属企业", "考核周期/年", "信誉等级", "综合评分"};
        //excel文件名
        String fileName = "驾驶员预警监控记录" + newTime + ".xls";
        //sheet名
        String sheetName = "驾驶员预警监控记录";
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            CreditDriverEvaluationRealtimeResp obj = list.get(i);
            content[i][0] = obj.getDriverName();
            content[i][1] = obj.getCertificateNumber();
            content[i][2] = obj.getCorpName();
            content[i][3] = obj.getEvaluationYear();
            content[i][4] = obj.getCreditGrade();
            content[i][5] = obj.getComprehensiveScore().toString();
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
