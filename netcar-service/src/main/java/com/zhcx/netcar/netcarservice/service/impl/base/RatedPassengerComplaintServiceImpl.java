package com.zhcx.netcar.netcarservice.service.impl.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarRatedPassengerComplaint;
import com.zhcx.netcar.facade.base.CompanyServiceService;
import com.zhcx.netcar.facade.base.OrderService;
import com.zhcx.netcar.facade.base.RatedPassengerComplaintService;
import com.zhcx.netcar.netcarservice.mapper.base.NetcarRatedPassengerComplaintMapper;
import com.zhcx.netcar.netcarservice.utils.PageHelperUtil;
import com.zhcx.netcar.params.PassengerComplainParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/27 21:26
 **/
@Service("ratedPassengerComplaintService")
public class RatedPassengerComplaintServiceImpl implements RatedPassengerComplaintService {

    @Autowired
    private NetcarRatedPassengerComplaintMapper netcarRatedPassengerComplaintMapper;

    @Autowired
    private CompanyServiceService companyServiceService;

    @Autowired
    private OrderService orderService;

    @Override
    public PageInfo<NetcarRatedPassengerComplaint> queryPassengerComplaintList(PassengerComplainParam param) throws Exception {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarRatedPassengerComplaint> list = netcarRatedPassengerComplaintMapper.selectListByKeyword(param);

        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }


    /**
     * 查询乘客投诉信息（基于车牌号）
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo<NetcarRatedPassengerComplaint> queryPassengerComplaintvehicleNo(PassengerComplainParam param) {
        PageHelper.startPage(param.getPageNo(), param.getPageSize());
        PageHelperUtil.orderBy(param.getOrderBy());
        List<NetcarRatedPassengerComplaint> list = netcarRatedPassengerComplaintMapper.queryPassengerComplaintvehicleNo(param);
        //添加企业名称
        list.forEach(item -> {
            Integer address = orderService.selectAddressByCompanyIdAndOrderId(item.getCompanyId(), item.getOrderId());
            String companyName = companyServiceService.selectCompanyNameByCompanyIdAndAddress(item.getCompanyId(), address);
            item.setCompanyName(companyName);
        });
        return new PageInfo<>(list);
    }
}
