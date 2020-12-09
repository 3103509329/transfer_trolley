package com.zhcx.authorization.utils.excel;

import com.zhcx.authorization.utils.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ExcelUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);


    /**
     * 下载excel到客户端
     *
     * @param xlsname
     * @param response
     * @param request
     * @throws Exception
     */
    public void downloadExcleTemplate(String xlsname, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setCharacterEncoding("UTF-8");
        ClassPathResource cpr = new ClassPathResource("/exceltemplate/" + xlsname + ".xlsx");
        BufferedInputStream bis = null;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            bis = new BufferedInputStream(cpr.getInputStream());
            if (null == bis) {
                logger.error("模板文件不存在：" + cpr.getPath() + ",文件名：" + xlsname);
                throw new Exception("模板不存在");
            }
            os = response.getOutputStream();
            response.setHeader("content-disposition", "attachment;filename=" + new String(xlsname.getBytes("GBK"), "ISO-8859-1") + ".xlsx");
            response.setContentType("application/octet-stream");//八进制流 与文件类型无关
            byte temp[] = new byte[1024];
//	            fis = new FileInputStream(file);
            int n = 0;
            while ((n = bis.read(temp)) != -1) {
                os.write(temp, 0, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (os != null)
                os.close();
            if (fis != null)
                fis.close();
        }
    }

    /**
     * 下载excel到客户端
     *
     * @param xlsname
     * @param response
     * @param request
     * @throws Exception
     */
    public static void downloadexcle(String xlsname, String fName, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (CommonUtils.isMessyCode(xlsname)) {
            xlsname = new String(xlsname.getBytes("iso-8859-1"), "utf-8");
        }
        String filepath = new String(xlsname);
        if (filepath != null) {
            OutputStream os = null;
            FileInputStream fis = null;
            try {
                String file = filepath;
                if (!(new File(file)).exists()) {
                    return;
                }
                String filename = file.substring(file.lastIndexOf("/") + 1);
                String hz = file.substring(file.lastIndexOf("."), file.length());
                if (StringUtils.isNotBlank(fName) && CommonUtils.isMessyCode(fName)) {
                    filename = new String(fName.getBytes("iso-8859-1"), "utf-8") + hz;
                }
                os = response.getOutputStream();
                response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO-8859-1"));
                response.setContentType("application/octet-stream");//八进制流 与文件类型无关
                byte temp[] = new byte[1024];
                fis = new FileInputStream(file);
                int n = 0;
                while ((n = fis.read(temp)) != -1) {
                    os.write(temp, 0, n);
                }
                if (new File(file).exists()) {
                    new File(file).delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null)
                    os.close();
                if (fis != null)
                    fis.close();
            }
        }
    }

    /**
     * 下载excel到客户端
     *
     * @param response
     * @param request
     * @throws Exception
     */
    public static void downloadexcle2(String filePath, HttpServletResponse response, HttpServletRequest request) throws Exception {
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        if (CommonUtils.isMessyCode(filePath)) {
            filePath = new String(filePath.getBytes("iso-8859-1"), "utf-8");
        }
        String filepath = new String(filePath);
        if (filepath != null) {
            OutputStream os = null;
            FileInputStream fis = null;
            try {
                String file = filepath.replaceAll("\\\\", "/");
                if (!(new File(file)).exists()) {
                    return;
                }
                String filename = file.substring(file.lastIndexOf("/") + 1, file.length());
                os = response.getOutputStream();
                response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO-8859-1"));
                response.setContentType("application/octet-stream");//八进制流 与文件类型无关
                byte temp[] = new byte[1024];
                fis = new FileInputStream(file);
                int n = 0;
                while ((n = fis.read(temp)) != -1) {
                    os.write(temp, 0, n);
                }
                if (new File(file).exists()) {
                    new File(file).delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null)
                    os.close();
                if (fis != null)
                    fis.close();
            }
        }
    }


    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 5000);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }


    /**
     * 导出Excel
     *
     * @param spreadhead 大标题
     * @param sheetName  sheet名称
     * @param title      小标题
     * @param values     内容
     * @param wb         HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook2(String spreadhead, String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();//标题部分
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCellStyle style2 = wb.createCellStyle();//正文部分
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //设置标题字体
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        style.setFont(font);

        //设置正文字体
        HSSFFont font2 = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);//设置字体大小
        style2.setFont(font2);


        //声明列对象
        HSSFCell cell = null;

        //创建大标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(spreadhead);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 5000);
        }
        //合并单元格 参数 开始行 结束行 开始列 结束列
        CellRangeAddress region1 = new CellRangeAddress(0, 0, 0, title.length - 1);
        sheet.addMergedRegion(region1);

        row = sheet.createRow(1);
        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 5000);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 2);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                cell = row.createCell(j);
                cell.setCellValue(values[i][j]);
                cell.setCellStyle(style2);
            }
        }
        return wb;
    }

    //发送响应流方法
    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
