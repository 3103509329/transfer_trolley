package com.zhcx.netcar.facade.base;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.base.NetcarRatedPassengerComplaint;
import com.zhcx.netcar.params.PassengerComplainParam;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2018/11/27 21:26
 **/
public interface RatedPassengerComplaintService {
    /**
     * 查询乘客投诉信息
     *
     * @param param
     * @return
     * @throws Exception
     */
    PageInfo<NetcarRatedPassengerComplaint> queryPassengerComplaintList(PassengerComplainParam param) throws Exception;

    /**
     * 查询乘客投诉信息（基于车牌号）
     *
     * @param param
     * @return
     */
    PageInfo<NetcarRatedPassengerComplaint> queryPassengerComplaintvehicleNo(PassengerComplainParam param);
}
