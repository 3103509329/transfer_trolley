package com.zhcx.netcar.netcarservice.mapper.base;

import com.zhcx.netcar.pojo.app.ConfigurationUpgrade;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-28 13:57
 * app安装包更新
 */
@Mapper
public interface ConfigurationUpgradeMapper {

    @Insert(" INSERT INTO netcar_app_update(content,version,download_url,date_time) " +
            " VALUES(#{content},#{version},#{downloadUrl},now()) ")
    void insertConfigurationUpgrade(ConfigurationUpgrade configurationUpgrade);

    @Select(" <script>" +
            " SELECT uuid,content,version,download_url as downloadUrl,date_time as dateTime " +
            " FROM netcar_app_update WHERE 1 = 1 " +
            " <if test=\"keyword != null and keyword != '' \"> " +
            " (content LIKE #{keyword} OR version LIKE  #{keyword}) " +
            " </if> " +
            " ORDER BY date_time DESC" +
            " </script>  ")
    List<ConfigurationUpgrade> getConfigurationUpgrade(@Param("keyword") String keyword);

    @Delete(" DELETE FROM netcar_app_update WHERE uuid = #{uuid} ")
    void deleteConfigurationUpgrade(@Param("uuid") Integer uuid);
}
