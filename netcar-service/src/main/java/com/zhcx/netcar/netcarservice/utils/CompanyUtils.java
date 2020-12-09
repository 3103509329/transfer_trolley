package com.zhcx.netcar.netcarservice.utils;

import com.zhcx.netcar.constant.CompanyDataTypeEnum;
import com.zhcx.netcar.netcarservice.mapper.base.*;
import com.zhcx.netcar.pojo.base.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/4/10 0010 16:42
 **/
@Component
public class CompanyUtils<T> {


    @Resource(name = "redisTemplate4Json")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private NetcarBaseInfoCompanyMapper netcarBaseInfoCompanyMapper;

    @Autowired
    private NetcarBaseInfoCompanyServiceMapper netcarBaseInfoCompanyServiceMapper;

    @Autowired
    private NetcarShareCompanyMapper netcarShareCompanyMapper;

    private T tDara;
    /**
     * 基于当前类的父类进行反射赋值（对象集合）
     *
     * @param pendingData
     * @return
     * @throws Exception
     */
    public List<?> addCompanyName(List<?> pendingData) throws Exception {

        Iterator<?> List = pendingData.iterator();
        while (List.hasNext()) {
            reflect((T) List.next());
        }
        return pendingData;
    }

    /**
     * 基于当前类的父类进行反射赋值（单个对象）
     *
     * @param pendingData
     * @return
     * @throws Exception
     */
    public T addCompanyName(T pendingData) throws Exception {
        try {
            reflect(pendingData);
        } catch (Exception e) {
            throw new Exception();
        }
        return pendingData;
    }

    /**
     * 基于指定的企业ID 和 行政区划 获取公司名称，并赋值给CompanyName
     */
    private void reflect(T clazz) {
        tDara = clazz;
        Class dataClass = clazz.getClass();
        Class<?> classList = dataClass.getSuperclass();
        try {
            Method setMethod = classList.getMethod("setCompanyName", String.class);
            Method getCompanyId = dataClass.getMethod("getCompanyId", null);
            Field[] fields = dataClass.getDeclaredFields();
            Field field = null;
            for (Field f : fields){
                f.setAccessible(true);
                if (f.getName().equals("address")){
                    field = f;
                    break;
                }
            }
            Integer addressData = null;
            if (null == field) {
                addressData = getAddress();
            } else {
                Method getAddress = dataClass.getMethod("getAddress", null);
                addressData = (Integer) getAddress.invoke(clazz);
            }
            String companyId = (String) getCompanyId.invoke(clazz);
            String companyName = redisTemplate.opsForValue().get(CompanyDataTypeEnum.NETCAR_COMPANY.getDesc() + companyId);
            if (StringUtils.isBlank(companyName)) {
                if (clazz instanceof NetcarShareCompany || clazz instanceof NetcarShareOrder || clazz instanceof NetcarSharePay ||  clazz instanceof NetcarShareRoute){
                    companyName = netcarShareCompanyMapper.selectCompanyNameByCompanyIdAndAddress(companyId, addressData);
                }else {
                    companyName = netcarBaseInfoCompanyServiceMapper.selectCompanyNameByCompanyIdAndAddress(companyId, addressData);
                }
                if (StringUtils.isNotBlank(companyName)) {
                    setMethod.invoke(clazz, companyName);
                    insertRedis(companyName);
                }
            } else {
                setMethod.invoke(clazz, companyName);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 基于不同的对象类型获取该数据对应关系的行政区划（主要针对于除企业，驾驶员，车辆基础信息以外的其他数据）
     *
     * @param obj
     * @return
     */
    @Autowired
    private NetcarBaseInfoVehicleMapper netcarBaseInfoVehicleMapper;

    @Autowired
    private NetcarBaseInfoDriverMapper netcarBaseInfoDriverMapper;

//    @Autowired
//    private NetcarOrderMatchMapper netcarOrderMatchMapper;

    private Integer getAddress() {
        Integer address = null;
        Object obj = tDara;
        //企业支付信息
        if (obj instanceof NetcarBaseInfoCompanyPay) {
            NetcarBaseInfoCompanyPay netcarBaseInfoCompanyPay = (NetcarBaseInfoCompanyPay) obj;
            address = netcarBaseInfoCompanyServiceMapper.selectAddressByCompanyId(netcarBaseInfoCompanyPay.getCompanyId());
        }
        //企业营运规模信息
        if (obj instanceof NetcarBaseInfoCompanyStat) {
            NetcarBaseInfoCompanyStat netcarBaseInfoCompanyStat = (NetcarBaseInfoCompanyStat) obj;
            address = netcarBaseInfoCompanyServiceMapper.selectAddressByCompanyId(netcarBaseInfoCompanyStat.getCompanyId());
        }
        //车辆保险信息
        if (obj instanceof NetcarBaseInfoVehicleInsurance) {
            NetcarBaseInfoVehicleInsurance netcarBaseInfoVehicleInsurance = (NetcarBaseInfoVehicleInsurance) obj;
            address = netcarBaseInfoVehicleMapper.selectAddressByCompanyIdAndVehicleNo(netcarBaseInfoVehicleInsurance.getCompanyId(), netcarBaseInfoVehicleInsurance.getVehicleNo());
        }
//        //车辆经营上线查询
//        if (obj instanceof NetcarOperateLogin ) {
//            NetcarOperateLogin netcarOperateLogin = (NetcarOperateLogin) obj;
//            address = netcarBaseInfoDriverMapper.selectAddressByCompanyIdAndLicenseId(netcarOperateLogin.getCompanyId(), netcarOperateLogin.getLicenseId());
//        }
//        //车辆经营下线查询
//        if (obj instanceof  NetcarOperateLogout) {
//            NetcarOperateLogout netcarOperateLogout = (NetcarOperateLogout) obj;
//            address = netcarBaseInfoDriverMapper.selectAddressByCompanyIdAndLicenseId(netcarOperateLogout.getCompanyId(), netcarOperateLogout.getLicenseId());
//        }
//        //经营出发查询
//        if (obj instanceof NetcarOperateDepart) {
//            NetcarOperateDepart netcarOperateDepart = (NetcarOperateDepart) obj;
//            address = netcarOrderMatchMapper.selectAddressByOrderId(netcarOperateDepart.getCompanyId(), netcarOperateDepart.getOrderId());
//        }
//        //经营到达查询
//        if (obj instanceof NetcarOperateArrive) {
//            NetcarOperateArrive netcarOperateArrive = (NetcarOperateArrive) obj;
//            address = netcarOrderMatchMapper.selectAddressByOrderId(netcarOperateArrive.getCompanyId(), netcarOperateArrive.getOrderId());
//        }
//        //乘客评价
//        if (obj instanceof NetcarRatedPassenger) {
//            NetcarRatedPassenger netcarRatedPassenger = (NetcarRatedPassenger) obj;
//            address = netcarOrderMatchMapper.selectAddressByOrderId(netcarRatedPassenger.getCompanyId(), netcarRatedPassenger.getOrderId());
//        }
//        //乘客投诉
//        if (obj instanceof NetcarRatedPassengerComplaint) {
//            NetcarRatedPassengerComplaint netcarRatedPassengerComplaint = (NetcarRatedPassengerComplaint) obj;
//            address = netcarOrderMatchMapper.selectAddressByOrderId(netcarRatedPassengerComplaint.getCompanyId(), netcarRatedPassengerComplaint.getOrderId());
//        }
        //驾驶员处罚
//        if (obj instanceof NetcarRatedDriver) {
//            NetcarRatedDriver netcarRatedDriver = (NetcarRatedDriver) obj;
//            address = netcarOrderMatchMapper.selectAddressByLicenseId(netcarRatedDriver.getCompanyId(), netcarRatedDriver.getLicenseId());
//        }
//        //驾驶员信誉
//        if (obj instanceof NetcarRatedDriverPunish) {
//            NetcarRatedDriverPunish netcarRatedDriverPunish = (NetcarRatedDriverPunish) obj;
//            address = netcarOrderMatchMapper.selectAddressByLicenseId(netcarRatedDriverPunish.getCompanyId(), netcarRatedDriverPunish.getLicenseId());
//
//        }
        return address;
    }


    public void insertRedis(Object object) throws Exception {

        try {
            String companyName = "";
            Map<String, Object> map = new HashMap<>();
            if (object instanceof NetcarBaseInfoCompanyService) {
                //基于企业服务机构进行匹配
                companyName = ((NetcarBaseInfoCompanyService) object).getServiceName();
            } else if (object instanceof NetcarBaseInfoCompany) {
                //基于企业数据进行匹配
                companyName = ((NetcarBaseInfoCompany) object).getCompanyName();
            } else if (object instanceof NetcarShareCompany){
                companyName = ((NetcarShareCompany) object).getCompanyName();
            }
            redisTemplate.opsForValue().set(CompanyDataTypeEnum.NETCAR_COMPANY.getDesc() + map.get("companyId")+"_"+map.get("address"), companyName, 3, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new Exception();
        }
    }


}
