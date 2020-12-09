package com.zhcx.netcar.facade.app;


import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.app.NetcarPermitCompanyInfo;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:11
 **/
public interface PermitCompanyService {

    /**
     * 查询企业经营许可信息列表
     * @param corpId
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarPermitCompanyInfo> selectPermitCompanyInfoList(Long corpId, Integer flag, String keyword, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 根据UUID查询企业经营许可信息详情
     * @param corpId
     * @return
     */
    NetcarPermitCompanyInfo selectPermitCompanyInfoDetail(Long corpId);

    /**
     * 企业经营许可新增
     * @param netcarPermitCompanyInfo
     * @return
     */
    int insertPermitCompanyInfo(NetcarPermitCompanyInfo netcarPermitCompanyInfo) throws Exception;

    /**
     * 企业经营许可更新
     * @return
     */
    int updatePermitCompanyInfo(NetcarPermitCompanyInfo netcarPermitCompanyInfo) throws Exception;

    /**
     * 删除企业经营许可
     * @param uuid
     * @return
     */
    int deletePermitCompanyInfo(Integer uuid) throws Exception;

    /**
     * 企业经营许可审核
     * @param uuid
     * @param flag
     * @param reason
     * @return
     */
    int updatePermitCompanyInfoStatus(Long uuid, Integer flag, String reason) throws Exception;

    /**
     * 获取电话作为账号
     * @param uuid
     * @return
     */
    String selectPhoneByUuid(Long uuid) throws Exception;


}
