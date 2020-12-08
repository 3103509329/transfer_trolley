package com.zhcx.netcar.netcarservice.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * jwt相关配置
 *
 * @author fengshuonan
 * @date 2017-08-23 9:23
 */
@Configuration
@ConfigurationProperties(prefix = "eec")
public class ExportExcelConst {

    public static String exportVehicleExcel;

    public static String exportDiverExcel;

    public void setExportVehicleExcel(String exportVehicleExcel) {
        ExportExcelConst.exportVehicleExcel = exportVehicleExcel;
    }

    public void setExportDiverExcel(String exportDiverExcel) {
        ExportExcelConst.exportDiverExcel = exportDiverExcel;
    }

}
