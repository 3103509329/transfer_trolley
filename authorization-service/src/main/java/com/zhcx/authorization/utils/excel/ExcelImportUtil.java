package com.zhcx.authorization.utils.excel;

import com.zhcx.authorization.utils.MessageResp;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: authorization-service
 * @ClassName:ExcelImpoarUtil
 * @description: Excel导入工具类
 * @author: ZhangKai
 * @create: 2018-12-27 20:06
 **/
public class ExcelImportUtil {

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static MessageResp validateImportExcel(MultipartFile file) {
        MessageResp resp = new MessageResp();
        if (file == null) {
            resp.setResultDesc("请选择文件。。。。。");
            resp.setResult("false");
            resp.setStatusCode("1004");
            return resp;
        }
        if (StringUtils.isBlank(file.getName())) {
            resp.setResultDesc("批量导入时文件不能为空");
            resp.setStatusCode("1004");
            resp.setResult("false");
            return resp;
        }

        CommonsMultipartFile cf = (CommonsMultipartFile) file;
        DiskFileItem fi = (DiskFileItem) cf.getFileItem();
        File userFile = fi.getStoreLocation();
//        if(!FileHandeUtil.validFileSize(userFile)){
//            resp.setResultDesc("文件内容格式不正确,请核查");
//            resp.setStatusCode("1004");
//            resp.setResult("false");
//            return resp;
//        }
        // 判断文件
        if (!(file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().trim().endsWith(".xlsx"))) {
            resp.setResultDesc("文件格式不对，系统只能够上传.xls或.xlsx格式的文件.\r\n");
            resp.setStatusCode("1004");
            resp.setResult("false");
            return resp;
        }
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (Exception e) {
            resp.setResultDesc("文件无法读取，请检查文件内容完整性！\r\n");
            resp.setStatusCode("1004");
            resp.setResult("false");
            return resp;
        }
        return resp;
    }

    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String value = null;
        if (null == cell) {
            return value;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是date类型则 ，获取该cell的date值
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    value = format.format(date);
                } else {// 纯数字
                    BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                    value = big.toString();
                    // 解决1234.0 去掉后面的.0
                    if (null != value && !"".equals(value.trim())) {
                        String[] item = value.split("[.]");
                        if (1 < item.length && "0".equals(item[1])) {
                            value = item[0];
                        }
                    }
                }
                break;
            // 字符串类型
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue().toString();
                break;
            // 公式类型
            case Cell.CELL_TYPE_FORMULA:
                // 读公式计算值
                value = String.valueOf(cell.getNumericCellValue());
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
                    value = cell.getStringCellValue().toString();
                }
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                value = " " + cell.getBooleanCellValue();
                break;
            // 空值
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            // 故障
            case Cell.CELL_TYPE_ERROR:
                value = "";
                break;
            default:
                value = cell.getStringCellValue().toString();
        }
        if ("null".endsWith(value.trim())) {
            value = "";
        }
        return value;
    }
}
