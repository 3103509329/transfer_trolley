package com.zhcx.netcar.netcarservice.service.impl.app;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhcx.netcar.facade.app.ConfigurationUpgradeService;
import com.zhcx.netcar.netcarservice.mapper.base.ConfigurationUpgradeMapper;
import com.zhcx.netcar.pojo.app.ConfigurationUpgrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-28 13:58
 * app安装包更新
 */
@Service("configurationUpgradeService")
public class ConfigurationUpgradeServiceImpl implements ConfigurationUpgradeService {

    @Autowired
    ConfigurationUpgradeMapper configurationUpgradeMapper;


    @Override
    public void insertConfigurationUpgrade(String version, String content,String fileName) throws Exception {
        ConfigurationUpgrade configurationUpgrade = new ConfigurationUpgrade();
        configurationUpgrade.setContent(content);
        configurationUpgrade.setVersion(version);
//        configurationUpgrade.setDownloadUrl("/upload/"+fileName);
        configurationUpgrade.setDownloadUrl(fileName);
        configurationUpgradeMapper.insertConfigurationUpgrade(configurationUpgrade);
    }

    @Override
    public PageInfo<ConfigurationUpgrade> getConfigurationUpgrade(int page, int pageSize, String keyword) throws Exception {
        PageHelper.startPage(page,pageSize);
        if(null != keyword && !"".equals(keyword)){
            keyword = "%" + keyword + "%";
        }
        List<ConfigurationUpgrade> list = configurationUpgradeMapper.getConfigurationUpgrade(keyword);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public void deleteConfigurationUpgrade(int uuid) throws Exception {
        configurationUpgradeMapper.deleteConfigurationUpgrade(uuid);
    }
}
