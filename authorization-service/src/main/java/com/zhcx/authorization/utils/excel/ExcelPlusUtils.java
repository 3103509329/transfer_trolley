package com.zhcx.authorization.utils.excel;

import com.zhcx.authorization.utils.CommonUtils;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ExcelPlusUtils {

    private static final Logger logger = LoggerFactory.getLogger(ExcelPlusUtils.class);


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
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(SheetPojo sheetDta, HSSFWorkbook wb) {

        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        //声明列对象
        Integer rowControl = 0;//行控制
        HSSFCell cell = null;
        HSSFSheet sheet = wb.createSheet(sheetDta.getSheetName());
        HSSFRow row = sheet.createRow(0);

        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //大标题
        if (sheetDta.getType() == 1) {
            CellRangeAddress cra = new CellRangeAddress(0, 0, 0, sheetDta.getTitle().length - 1);
            sheet.addMergedRegion(cra);
            cell = row.createCell(0);
            cell.setCellValue(sheetDta.getHeadline());
            cell.setCellStyle(style);
            sheet.setColumnWidth(0, 5000);
            rowControl = 1;
        }

        //创建小标题
        row = sheet.createRow(rowControl);
        for (int i = 0; i < sheetDta.getTitle().length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(sheetDta.getTitle()[i]);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 5000);
        }

        //创建内容
        System.out.print("sheetDta.getValues().length:" + sheetDta.getValues().length);
        for (int i = 0; i < sheetDta.getValues().length; i++) {
            if (sheetDta.getValues()[i] != null) {
                row = sheet.createRow(i + 1 + rowControl);
                for (int j = 0; j < sheetDta.getValues()[i].length; j++) {
                    //将内容按顺序赋给对应的列对象
                    row.createCell(j).setCellValue(sheetDta.getValues()[i][j]);
                }
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
