package com.zhcx.netcar.pojo.app;

import java.io.Serializable;

/**
 * @author buhao
 * @email 1249285896@qq.com
 * @date 2019-05-28 13:57
 * 配置更新
 */
public class ConfigurationUpgrade implements Serializable {

    private int uuid;

    private String content;

    private String version;

    private String downloadUrl;

    private String dateTime;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ConfigurationUpgrade{" +
                "uuid=" + uuid +
                ", content='" + content + '\'' +
                ", version='" + version + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
