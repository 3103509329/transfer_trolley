package com.zhcx.netcar.facade.yunzheng;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.yuzheng.YunZhengDriver;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/12 13:56
 **/
public interface YunZhengDriverService {
    int updateByPrimaryKeySelective(YunZhengDriver yunZhengDriver);

    int insert(YunZhengDriver yunZhengDriver);

    /**
     * 新增
     * @param yunZhengDriver
     * @return
     */
    YunZhengDriver addDriver(YunZhengDriver yunZhengDriver);

    /**
     * 查询
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<YunZhengDriver> selectDriver(String keyword, String busiRegNumber, Integer pageNo, Integer pageSize, String orderBy);

    /**
     * 修改
     * @param yunZhengDriver
     * @return
     */
    YunZhengDriver updateDriver(YunZhengDriver yunZhengDriver);

    /**
     * 删除
     * @param driverIdCard
     * @param time
     * @return
     */
    Integer deleteDriver(String driverIdCard, String time);


    int selectByLicenseId(String licenseId);

    Long selectCountByCompanyId(String companyId);
}
