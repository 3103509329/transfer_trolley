package com.zhcx.netcar.facade.app;

import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.app.NetcarPermitDriverInfo;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/2/22 17:55
 **/
public interface PermitDriverService {
    /**
     * 驾驶员许可信息列表查询
     * @param corpId
     * @param keyword
     * @param flag
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    PageInfo<NetcarPermitDriverInfo> selectPermitDriverInfoList(Long corpId, String keyword, Integer flag, Integer pageNo, Integer pageSize, String orderBy) throws Exception;

    /**
     * 驾驶员许可信息新增
     * @param netcarPermitDriverInfo
     * @return
     */
    int insertPermitDriverInfo(NetcarPermitDriverInfo netcarPermitDriverInfo) throws Exception;

    /**
     * 驾驶员许可信息更新
     * @param netcarPermitDriverInfo
     * @return
     */
    int updatePermitDriverInfo(NetcarPermitDriverInfo netcarPermitDriverInfo) throws Exception;

    /**
     * 驾驶员许可审核
     * @param uuid
     * @param flag
     * @param reason
     * @return
     */
    int updatePermitDriverInfoStatus(Long uuid, Integer flag, String reason);
}
