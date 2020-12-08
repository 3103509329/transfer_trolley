package com.zhcx.netcar.pojo.mapAndVideo;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @email 570815140@qq.com
 * @date 2019/6/1 0001 11:25
 **/
public class VideoRsult implements Serializable {

    private static final long serialVersionUID = 4105589091184334792L;
    public Integer cmsserver;
    public List<FeilsEntity> files;
    public String result;

    @Override
    public String toString() {
        return "VideoRsult{" +
                "cmsserver=" + cmsserver +
                ", files=" + files +
                ", result='" + result + '\'' +
                '}';
    }

    public Integer getCmsserver() {
        return cmsserver;
    }

    public void setCmsserver(Integer cmsserver) {
        this.cmsserver = cmsserver;
    }

    public List<FeilsEntity> getFiles() {
        return files;
    }

    public void setFiles(List<FeilsEntity> files) {
        this.files = files;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
