package com.zhcx.netcar.netcarservice.utils;

import com.zhcx.netcar.annotation.FieldMeta;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Lee
 * @email 570815140@qq.com
 * @date 2019/7/9 11:11
 **/
@Component
public class ExcelUtil {

    /**
     * 首行样式
     */
    private XSSFCellStyle topCellStyle;
    /**
     * 内容样式
     */
    private XSSFCellStyle cellStyle;
    /**
     * 创建Excel表格
     * @param workbook 工作蒲文件
     * @param list 数据集合
     * @param sheetName 表单名
     * @param clazz 类型
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public void createExcelSheet(XSSFWorkbook workbook, List list, String sheetName, Class<?> clazz) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        //生成一表格
        XSSFSheet sheet = workbook.createSheet(sheetName);
        //生成第一行
        XSSFRow row = sheet.createRow(0);
        //表格样式
        initCellStyle(workbook);
        //设置行高
        row.setHeight((short) (26.25 * 20));

        initTopRow(row, clazz);

        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            Object obj = list.get(i);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(FieldMeta.class)) {
                    FieldMeta filedMeta = field.getAnnotation(FieldMeta.class);
                    int index = filedMeta.index();
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), clazz);
                    Method getMethod = propertyDescriptor.getReadMethod();
                    String cellValue = getMethod.invoke(obj) == null ? "" : getMethod.invoke(obj).toString();
                    XSSFCell xssfCell = row.createCell(index);
                    xssfCell.setCellValue(cellValue);
                    xssfCell.setCellStyle(cellStyle);
                }
            }
            sheet.autoSizeColumn(i);
            // 解决自动设置列宽中文失效的问题
            sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
        }
        sheet.setDefaultRowHeight((short) (16.5 * 20));
    }

    /**
     * excel请求响应
     * @param response
     * @param fileName
     * @param workbook
     */
    public void writeExcelResponse(HttpServletResponse response, String fileName, XSSFWorkbook workbook) {
        try {
            // 告诉浏览器用什么软件可以打开此文件       
            response.setHeader("content-Type","application/vnd.ms-excel");
            // 下载文件的默认名称 
            response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName,"utf-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        try {
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
//            try {
//                workbook.close();
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
        }
    }

    private void initCellStyle(XSSFWorkbook workbook) {
        setTopCellStyle(workbook);
        setCellStyle(workbook);
    }
    /**
     * 配置表格样式--标题行
     * @param workbook
     */
    private void setTopCellStyle(XSSFWorkbook workbook) {
        topCellStyle = workbook.createCellStyle();
        topCellStyle.setAlignment(HorizontalAlignment.CENTER);
        topCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        topCellStyle.setWrapText(false);
        //边框
        topCellStyle.setBorderBottom(BorderStyle.THIN);
        topCellStyle.setBorderLeft(BorderStyle.THIN);
        topCellStyle.setBorderTop(BorderStyle.THIN);
        topCellStyle.setBorderRight(BorderStyle.THIN);
        //填充样式
        topCellStyle.setFillForegroundColor(HSSFColor.LIME.index);
        topCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //设置表头字体
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setFontHeight((short) 500);
        titleFont.setFontHeightInPoints((short) 11);
        topCellStyle.setFont(titleFont);
    }

    /**
     * 配置表格样式--内容
     * @param workbook
     * @return
     */
    private void setCellStyle(XSSFWorkbook workbook) {
        cellStyle = workbook.createCellStyle();
        cellStyle.setWrapText(false);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置字体
        XSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 8);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        cellStyle.setFont(font);
    }


    /**
     * 初始化首行
     */
    private void initTopRow(XSSFRow row, Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(FieldMeta.class)) {
                FieldMeta filedMeta = field.getAnnotation(FieldMeta.class);
                int index = filedMeta.index();
                String name = filedMeta.name();
                XSSFCell xssfCell = row.createCell(index);
                xssfCell.setCellValue(StringUtils.isBlank(name) ? "" : name);
                xssfCell.setCellStyle(topCellStyle);
            }
        }
    }


}
