package com.zhcx.netcar.facade.app;


import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.pojo.app.ConfigurationUpgrade;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-28 13:56
 * app安装包更新
 */
public interface ConfigurationUpgradeService {

    void insertConfigurationUpgrade(String version, String content, String fileName)throws Exception;

    PageInfo<ConfigurationUpgrade> getConfigurationUpgrade(int page, int pageSize, String keyword)throws Exception;

    void deleteConfigurationUpgrade(int uuid) throws Exception;
}
