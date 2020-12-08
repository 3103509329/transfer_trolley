package com.zhcx.netcar.netcarservice.service.impl.yunzheng;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.yunzheng.YunZhengCompanyService;
import com.zhcx.netcar.netcarservice.constant.YzStatusEnum;
import com.zhcx.netcar.netcarservice.mapper.yunzheng.YunZhengCompanyMapper;
import com.zhcx.netcar.pojo.yuzheng.YunZhengCompany;
import com.zhcx.netcar.pojo.yuzheng.YzCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/5/12 13:50
 **/
@Service("yunZhengCompanyService")
public class YunZhengCompanyServiceImpl implements YunZhengCompanyService {

    @Autowired
    private YunZhengCompanyMapper yunZhengCompanyMapper;

//    @Autowired
//    private AuthUserService authUserService;

    @Override
    @Transactional(rollbackFor = Exception.class, transactionManager = "carHailingDbTransactionManager")
    public void addOrUpdateCompanyInfo(YzCompany yzCompany) throws Exception {
        YunZhengCompany yunZhengCompany = new YunZhengCompany();
        yunZhengCompany.setBusiregnumber(yzCompany.getBusiRegNumber());
        yunZhengCompany.setTime(new Date(yzCompany.getTime()).toString());
        yunZhengCompany.setFlage(yzCompany.getFlage());
        if(yunZhengCompany.getFlage() == YzStatusEnum.DELETE.getCode()){
            yunZhengCompanyMapper.updateByBusiregnumber(yunZhengCompany);
            return;
        }
        yunZhengCompany.setPhone(yzCompany.getPhone());
        yunZhengCompany.setRegisteredCapital(yzCompany.getRegisteredCapital());
        yunZhengCompany.setClitname(yzCompany.getClitname());
        yunZhengCompany.setBnscope(yzCompany.getBnscope());
        yunZhengCompany.setAddress(yzCompany.getAddress());
        yunZhengCompany.setEstablishmentTime(yzCompany.getEstablishmentTime());
        yunZhengCompany.setStartDate(yzCompany.getStartDate());
        yunZhengCompany.setEndDate(yzCompany.getEndDate());
        yunZhengCompany.setPersonLiable(yzCompany.getPersonLiable());
        yunZhengCompany.setBusinessLicense(yzCompany.getBusinessLicense());

        //将数据写入运政表（企业数据唯一）
        if(yunZhengCompany.getFlage() == YzStatusEnum.ADD.getCode()){
            yunZhengCompanyMapper.insert(yunZhengCompany);
            /**
             * 自动添加企业用户，功能屏蔽
             */
//            authUserService.insertAuthUser(yunZhengCompany);
        }else{
            yunZhengCompanyMapper.updateByBusiregnumber(yunZhengCompany);
        }

    }

    /**
     * 新增
     *
     * @param company
     * @return
     */
    @Override
    public YunZhengCompany addCompany(YunZhengCompany company) {
        company.setTime(new Date().toString());
        yunZhengCompanyMapper.insert(company);
        return company;
    }

    /**
     * 查询
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @param uuid
     * @return
     */
    @Override
    public PageInfo<YunZhengCompany> selecCompany(String keyword, Integer pageNo, Integer pageSize, String orderBy, Long uuid) {


        PageHelper.startPage(pageNo, pageSize);
//        PageHelperUtil.orderBy(orderBy);

        List<YunZhengCompany> companyList = yunZhengCompanyMapper.selectByCompanyName(keyword, uuid);
        return new PageInfo<>(companyList);
    }

    /**
     * 修改
     *
     * @param company
     * @return
     */
    @Override
    public YunZhengCompany updateCompany(YunZhengCompany company) {
        yunZhengCompanyMapper.updateByPrimaryKeySelective(company);
        return company;
    }

    /**
     * 删除
     *
     * @param company
     * @return
     */
    @Override
    public int deleteCompany(YunZhengCompany company) {

        return yunZhengCompanyMapper.deleteByPrimaryKey(company.getUuid());
    }

    /**
     * 唯一性验证
     * @param company
     * @return
     */
    @Override
    public int selectByBusiregnumber(YunZhengCompany company) {

        return yunZhengCompanyMapper.selectByBusiregnumber(company.getBusiregnumber());
    }

    /**
     * 登陆时，查询运政企业数据
     * @param corpId
     * @return
     */
    @Override
    public YunZhengCompany selectByCompanyId(Long corpId) {
        return yunZhengCompanyMapper.selectByCorpId(corpId);
    }

    @Override
    public Long selectCountByCompanyId(String companyId) {
        return  yunZhengCompanyMapper.selectCountByCompanyId(companyId);
    }

    /**
     * 查询所有的企业标识
     * @return
     */
    @Override
    public List<String> getCompanyBusiRegNumberList(){
        List<String> list = yunZhengCompanyMapper.getCompanyBusiRegNumberList();
        return list;
    }

    @Override
    public List<YunZhengCompany> selectAll() {

        return yunZhengCompanyMapper.selectAll();
    }


}
