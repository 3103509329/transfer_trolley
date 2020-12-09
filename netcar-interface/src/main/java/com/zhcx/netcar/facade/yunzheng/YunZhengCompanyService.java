package com.zhcx.netcar.facade.yunzheng;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
import com.zhcx.netcar.pojo.yuzheng.YzCompany;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/12 13:47
 **/
public interface YunZhengCompanyService {
    /**
     * 根据社会统一信用代码 更新企业许可信息
     * @param yzCompany
     * @return
     */
    void addOrUpdateCompanyInfo(YzCompany yzCompany) throws Exception;

    /**
     * 企业新增（运政）
     * @param company
     * @return
     */
    YunZhengCompany addCompany(YunZhengCompany company);

    /**
     * 企业查询（运政）
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param uuid
     * @return
     */
    PageInfo<YunZhengCompany> selecCompany(String keyword, Integer pageNo, Integer pageSize, String orderBy, Long uuid);

    /**
     * 企业修改（运政）
     * @param company
     * @return
     */
    YunZhengCompany updateCompany(YunZhengCompany company);

    /**
     * 企业删除（运政）
     * @param company
     * @return
     */
    int deleteCompany(YunZhengCompany company);

    int selectByBusiregnumber(YunZhengCompany company);
    /**
     * 登陆时，查询运政企业数据
     * @param corpId
     * @return
     */
    YunZhengCompany selectByCompanyId(Long corpId);

    /**
     * 根据公司ID查询
     * @param companyId
     * @return
     */
    Long selectCountByCompanyId(String companyId);

    /**
     * 查询公司信用代码
     * @return
     */
    List<String> getCompanyBusiRegNumberList();

    /**
     * 获取所有的公司信息
     * @return
     */
    List<YunZhengCompany> selectAll();
}
