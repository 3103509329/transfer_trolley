package com.zhcx.netcar.netcarservice.service.impl.yunzheng;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.yunzheng.YunZhengDriverService;
import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunZhengDriverMapper;
import com.zhcx.netcar.pojo.yuzheng.YunZhengDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/12 13:57
 **/
@Service("yunZhengDriverService")
public class YunZhengDriverServiceImpl implements YunZhengDriverService {
    @Autowired
    private YunZhengDriverMapper yunZhengDriverMapper;

    @Override
    public int updateByPrimaryKeySelective(YunZhengDriver yunZhengDriver) {
        return yunZhengDriverMapper.updateByPrimaryKeySelective(yunZhengDriver);
    }

    @Override
    public int insert(YunZhengDriver yunZhengDriver) {
        return yunZhengDriverMapper.insert(yunZhengDriver);
    }

    /**
     * 驾驶员新增（运政）
     * @param yunZhengDriver
     * @return
     */
    @Override
    public YunZhengDriver addDriver(YunZhengDriver yunZhengDriver) {

        yunZhengDriver.setTime(new Date().toString());
        yunZhengDriverMapper.insert(yunZhengDriver);
        return yunZhengDriver;
    }

    /**
     * 驾驶员查询（运政）
     * @param pageSize
     * @param orderBy
     * @return
     */
    @Override
    public PageInfo<YunZhengDriver> selectDriver(String keyword, String busiRegNumber, Integer pageNo, Integer pageSize, String orderBy) {

        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);
        List<YunZhengDriver> driverList = yunZhengDriverMapper.selectByNameOrLicenseId(keyword, busiRegNumber);
        return new PageInfo<>(driverList);
    }

    /**
     * 驾驶员修改（运政）
     * @param yunZhengDriver
     * @return
     */
    @Override
    public YunZhengDriver updateDriver(YunZhengDriver yunZhengDriver) {
        yunZhengDriverMapper.updateByPrimaryKeySelective(yunZhengDriver);
        return yunZhengDriver;
    }

    /**
     * 驾驶员删除（运政）
     * @param driverIdCard
     * @param time
     * @return
     */
    @Override
    public Integer deleteDriver(String driverIdCard, String time) {
        return yunZhengDriverMapper.deleteByDriverIdCardAndTime(driverIdCard, time);
    }

    /**
     * 驾驶员信息唯一性验证
     * @param licenseId
     * @return
     */
    @Override
    public int selectByLicenseId(String licenseId) {
        return yunZhengDriverMapper.selectBylicenseId(licenseId);
    }

    @Override
    public Long selectCountByCompanyId(String companyId) {
        return yunZhengDriverMapper.selectCountByCompanyId(companyId);
    }
}
