package com.zhcx.authorization.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddressList;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhcx.basicdata.common.ExcelAnnotation;
import org.springframework.core.io.ClassPathResource;

/**
 * @param <T>
 * @ClassName：ExportExcel
 * @Description: 导出excle
 * @author：lyl
 * @date：2015年11月3日 上午9:30:31
 */
public class ExportExcel<T> {
    Class<T> clazz;

    public ExportExcel(Class<T> clazz) {
        this.clazz = clazz;
    }

    Logger log = Logger.getLogger(ExportExcel.class);

    /**
     * 利用spring注解和java反射机制导出数据 并在本地下载
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合，集合中一定要放置符合javabean风格的类的对象。
     * @param pattem  如果有时间数据，设定输出格式。默认为"yyyy-MM-dd";
     */
    public void exportBasicExcel(String xlsname, String[] headers, Collection<T> dataset, String pattem, HttpServletRequest request, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook(); // 创建一个excel文件
        HSSFSheet sheet = wb.createSheet(xlsname); //创建一个excel的sheet
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(25);
        HSSFRow row = sheet.createRow(0); //新建一行
        row.setHeight((short) 330);
        //样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.LIME.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 8);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        //写入头部信息
        int idex = 0;//列号
        //设置表头字体
        HSSFFont font_title = wb.createFont();
        font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font_title.setFontHeight((short) 500);
        font_title.setFontHeightInPoints((short) 11);
        style.setFont(font_title);
        //设置格式
        HSSFCellStyle cellStyleTitle = wb.createCellStyle();
        cellStyleTitle.setFont(font_title);
        cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 产生表格标题行
        Map<Object, Object> headerFiled = new HashMap<Object, Object>();
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);// 设置单元格样式(包含字体)
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);// 把数据放到单元格中
            headerFiled.put(i, text);
        }
        try {
            /**
             * 类反射得到调用方法
             */
            // 得到目标目标类的所有的字段列表
            Field fields[] = clazz.getDeclaredFields();
            // 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
            Map fieldmap = new HashMap();
            // 循环读取所有字段
            for (int i = 0; i < fields.length; i++) {
                Field f = fields[i];
                // 得到单个字段上的Annotation
                ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
                // 如果标识了Annotationd的话
                if (exa != null) {
                    // 构造设置了Annotation的字段的Setter方法
                    String fieldname = f.getName();
                    String getMethodName = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
                    // 构造调用的method，
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    // 将这个method以Annotaion的名字为key来存入。
                    fieldmap.put(exa.exportName(), getMethod);
                }
            }

            Iterator<Map.Entry<Integer, Integer>> entries = fieldmap.entrySet().iterator();

            // 遍历集合数据,产生数据行
            Iterator<T> it = dataset.iterator();
            int index = 0;


            while (it.hasNext()) {
                index++;
                row = sheet.createRow(index);
                T t = (T) it.next();
                // 利用反射，根据javabean属性的先后顺序，动态的调用getXxx()方法得到属性值
                //			Field[] fields = t.getClass().getDeclaredFields();
                // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值 在 实体对象中利用注解的方式
                for (int i = 0; i < fields.length; i++) {
                    // 这里得到此列的对应的标题
                    String titleString = headerFiled.get(i) == null ? "" : headerFiled.get(i).toString();
                    // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的get方法取值
                    if (StringUtils.isNotBlank(titleString) && fieldmap.containsKey(titleString)) {
                        HSSFCell cell = row.createCell(i);
//							cell.setCellStyle(style2);
                        Method getMethod = (Method) fieldmap.get(titleString);
                        Object value = getMethod.invoke(t, new Object[]{});
                        // 判断值的类型后进行强制类型转换
                        String textValue = null;
                        if (value instanceof Boolean) {
                            boolean booleanValue = (Boolean) value;
                            textValue = "true";
                            if (!booleanValue) {
                                textValue = "false";
                            }
//								cell.setCellValue(textValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattem);
                            textValue = sdf.format(date);
//								cell.setCellValue(textValue);
                        } else if (value instanceof byte[]) {
                            // 有图片时,设置行高为60px;
                            row.setHeightInPoints(60);
                            // 设置有图片所在列宽度为80px,注意这里单位的一个换算
                            sheet.setColumnWidth(i, (short) (35.7 * 80));
                            // sheet.autoSizeColumn(i);
                            byte[] bsValue = (byte[]) value;
                            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 55, (short) 6, index, (short) 6, index);
                            anchor.setAnchorType(2);
//								patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                        } else if (value == String.class) {
                            // 其它数据类型都当做字符串简单处理
                            textValue = value.toString();
//								cell.setCellValue(textValue);
                        } else if (value == BigDecimal.class) {
                            textValue = value.toString();
//								cell.setCellValue(textValue);
                        } else if (value instanceof Integer) {
                            textValue = new DecimalFormat("#").format(value);
//								cell.setCellValue(textValue);
                        } else {
                            if (null == value) {
                                textValue = "";
                            } else {
                                textValue = value.toString();
                            }
//								cell.setCellValue(textValue);
                        }
                        // 如果不是图片数据,就利用正则表达式判断textValue是否全部由数字组成
                        if (textValue != null) {
                            Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                            Matcher matcher = p.matcher(textValue);
                            if (matcher.matches()) {
                                // 是数字当作double处理
                                cell.setCellType(Cell.CELL_TYPE_STRING);
//									textValue = new DecimalFormat("#").format(textValue); 
//									cell.setCellValue(new DecimalFormat("#").format(textValue));
//									cell.setCellValue(Double.parseDouble(textValue));
                                cell.setCellValue(textValue);
                            } else {
                                HSSFRichTextString richString = new HSSFRichTextString(textValue);
                                HSSFFont font3 = wb.createFont();
//									font3.setColor(HSSFColor.BLUE.index);
                                richString.applyFont(font3);
                                cell.setCellValue(richString);
                            }
                        }
                    }
                }
            }
            FileOutputStream fout = new FileOutputStream(xlsname);
            wb.write(fout);
            fout.close();
            File file = new File(xlsname);
            ExcelUtils.downloadexcle(xlsname, "", response, request);
            if (file.exists()) {
                file.delete();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
        }
    }


    /**
     * 这是一个通用的方法,利用了JAVA的反射机制,可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL的形式输出到指定IO设备上
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合，集合中一定要放置符合javabean风格的类的对象。此方法支持的javabean属性的数据类型有基本数据类型以String,Date,byte[](图片数据)
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param pattem  如果有时间数据，设定输出格式。默认为"yyyy-MM-dd";
     */
    public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattem) {
        // 声明一个工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成一个样式(用于单元格)
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体(用于单元格)
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释!"));
        // 设置注释作者,当鼠标移动单单元格上是可以在状态栏中看到该内容。
        comment.setAuthor("aaa");
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);// 设置单元格样式(包含字体)
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);// 把数据放到单元格中
        }
        // 遍历集合数据,产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            // 利用反射，根据javabean属性的先后顺序，动态的调用getXxx()方法得到属性值
            Field[] fields = t.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style2);
                Field field = fields[i];
                String fieldName = field.getName();
                String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                Class tCls = t.getClass();
                try {
                    Method getMethod = tCls.getMethod(getMethodName, new Class[]{});
                    Object value = getMethod.invoke(t, new Object[]{});
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof Boolean) {
//						boolean booleanValue = (Boolean) value;
//						textValue = "男";
//						if (!booleanValue) {
//							textValue = "女";
//						}
                        cell.setCellValue(textValue);
                    } else if (value instanceof Date) {
                        Date date = (Date) value;
                        SimpleDateFormat sdf = new SimpleDateFormat(pattem);
                        textValue = sdf.format(date);
                        cell.setCellValue(textValue);
                    } else if (value instanceof byte[]) {
                        // 有图片时,设置行高为60px;
                        row.setHeightInPoints(60);
                        // 设置有图片所在列宽度为80px,注意这里单位的一个换算
                        sheet.setColumnWidth(i, (short) (35.7 * 80));
                        // sheet.autoSizeColumn(i);
                        byte[] bsValue = (byte[]) value;
                        HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 55, (short) 6, index, (short) 6, index);
                        anchor.setAnchorType(2);
                        patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                    } else {
                        // 其它数据类型都当做字符串简单处理
                        textValue = value.toString();
                    }
                    // 如果不是图片数据,就利用正则表达式判断textValue是否全部由数字组成
                    if (textValue != null) {
                        Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                        Matcher matcher = p.matcher(textValue);
                        if (matcher.matches()) {
                            // 是数字当作double处理
                            cell.setCellValue(Double.parseDouble(textValue));
                        } else {
                            HSSFRichTextString richString = new HSSFRichTextString(textValue);
                            HSSFFont font3 = workbook.createFont();
                            font3.setColor(HSSFColor.BLUE.index);
                            richString.applyFont(font3);
                            cell.setCellValue(richString);
                        }
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } finally {
                    // 关闭资源
                }
            }
        }
        try {
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param @param  headers
     * @param @param  xlsName
     * @param @param  sheetName
     * @param @return
     * @param @throws Exception
     * @return HSSFWorkbook
     * @throws
     * @Title: exportExcelMuban
     * @Description: 导出模板公共方法
     */
    public HSSFWorkbook exportExcelMuban(String[] headers, String xlsName, String sheetName) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook(); // 创建一个excel文件
        HSSFSheet sheet = wb.createSheet(sheetName); //创建一个excel的sheet
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        HSSFRow row = sheet.createRow(0); //新建一行
        //样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //写入头部信息
        int idex = 0;//列号
        //设置表头字体
        HSSFFont font_title = wb.createFont();
        font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font_title.setFontHeight((short) 200);
        //设置格式
        HSSFCellStyle cellStyleTitle = wb.createCellStyle();
        cellStyleTitle.setFont(font_title);
        cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
 /*		// 声明一个画图的顶级管理器
         HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
 		// 定义注释的大小和位置,详见文档
 		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		comment.setString(new HSSFRichTextString("可以在POI中添加注释!"));
		// 设置注释作者,当鼠标移动单单元格上是可以在状态栏中看到该内容。
		comment.setAuthor("aaa");
		*/
        row.setHeight((short) 450);
        HSSFCell cell = null;
        for (String str : headers) {
            cell = row.createCell(idex);
            cell.setCellValue(str);
            cell.setCellStyle(cellStyleTitle);
//			sheet.autoSizeColumn(1, true);
            sheet.setColumnWidth(idex, str.length() * 1000);   //设置列宽
            idex++;
        }
        return wb;
    }


    /**
     * @param @param  headers
     * @param @param  xlsName
     * @param @param  sheetName
     * @param @throws Exception
     * @return void
     * @throws
     * @Title: exportCarBasicMuban
     * @Description: 定制车辆档案模板
     */
    public HSSFWorkbook exportBasicMuban(String[] headers, String xlsName, String sheetName) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook(); // 创建一个excel文件
        HSSFSheet sheet = wb.createSheet(sheetName); //创建一个excel的sheet
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        HSSFRow row = sheet.createRow(0); //新建一行
        //样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //设置字体
        HSSFFont font = wb.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        //写入头部信息
        int idex = 0;//列号
        //设置表头字体
        HSSFFont font_title = wb.createFont();
        font_title.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font_title.setFontHeight((short) 200);
        //设置格式
        HSSFCellStyle cellStyleTitle = wb.createCellStyle();
        cellStyleTitle.setFont(font_title);
        cellStyleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyleTitle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        row.setHeight((short) 450);
        HSSFCell cell = null;
        for (String str : headers) {
            cell = row.createCell(idex);
            cell.setCellValue(str);
            cell.setCellStyle(cellStyleTitle);
            sheet.setColumnWidth(idex, str.length() * 3000);   //设置列宽
            idex++;
        }
        try {
            FileOutputStream fout = new FileOutputStream(xlsName);
            wb.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }


    /**
     * 利用spring注解和java反射机制导出数据 并在本地下载
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合，集合中一定要放置符合javabean风格的类的对象。
     * @param pattem  如果有时间数据，设定输出格式。默认为"yyyy-MM-dd";
     */
    public void exportBasicExcelByExcelTemplate(String filePath, String fileName, Collection<T> dataset, String pattem, HttpServletRequest request, HttpServletResponse response) {

        OutputStream os = null;
        InputStream in = null;
        Workbook wb = null;
        try {
//			File file = new File(filePath);
//			InputStream in = new FileInputStream(file);

            ClassPathResource cpr = new ClassPathResource("/exceltemplate/" + fileName + ".xlsx");
            in = cpr.getInputStream();
            // 得到工作表
            try {
                wb = new XSSFWorkbook(in);
            } catch (Exception ex) {
//	        	wb = new HSSFWorkbook(new FileInputStream(file));
                wb = new HSSFWorkbook(in);
            }
            //第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            //读取标题头
            Row row = sheet.getRow(0);
            ExcelReader excelReader = new ExcelReader();
            //String[] headerFiled = excelReader.readExcelTitle(row);
            List<String> headerFiled = excelReader.readExcelTitle2List(row, null);
            /**
             * 类反射得到调用方法
             */
            // 得到目标目标类的所有的字段列表
            Field fields[] = clazz.getDeclaredFields();
            // 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
            List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
            // 循环读取所有字段
            Map<String, Object> fieldmap = null;
//			Map<String,Object> fieldmap =  new HashMap<String, Object>();
            for (int i = 0; i < fields.length; i++) {
                fieldmap = new HashMap<String, Object>();
                Field f = fields[i];
                // 得到单个字段上的Annotation
                ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
                // 如果标识了Annotationd的话
                if (exa != null) {
                    // 构造设置了Annotation的字段的Setter方法
                    String fieldname = f.getName();
                    String getMethodName = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
                    // 构造调用的method，
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    // 将这个method以Annotaion的名字为key来存入。
                    if (headerFiled.contains(exa.exportName())) {
                        fieldmap.put(exa.exportName(), getMethod);
                        fieldList.add(fieldmap);
                    }

                }
            }
            // 遍历集合数据,产生数据行
            Iterator<T> it = dataset.iterator();
            int index = 1;
            while (it.hasNext()) {
                T t = (T) it.next();
                index++;
                row = sheet.createRow(index);
                // 利用反射，根据javabean属性的先后顺序，动态的调用getXxx()方法得到属性值
                //			Field[] fields = t.getClass().getDeclaredFields();
                // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值 在 实体对象中利用注解的方式
                String titleString = "";
                for (int i = 0; i < fieldList.size(); i++) {
                    for (String headerFile : headerFiled) {
                        if (fieldList.get(i).containsKey(headerFile)) {
                            // 这里得到此列的对应的标题
                            // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的get方法取值
                            Cell cell = row.createCell(i);
                            Method getMethod = (Method) fieldList.get(i).get(headerFile);
                            Object value = getMethod.invoke(t, new Object[]{});
                            // 判断值的类型后进行强制类型转换
                            String textValue = null;
                            if (value instanceof Boolean) {
                                boolean booleanValue = (Boolean) value;
                                textValue = "true";
                                if (!booleanValue) {
                                    textValue = "false";
                                }
//											cell.setCellValue(textValue);
                            } else if (value instanceof Date) {
                                Date date = (Date) value;
                                SimpleDateFormat sdf = new SimpleDateFormat(pattem);
                                textValue = sdf.format(date);
//											cell.setCellValue(textValue);
                            } else if (value instanceof byte[]) {
                                // 有图片时,设置行高为60px;
                                row.setHeightInPoints(60);
                                // 设置有图片所在列宽度为80px,注意这里单位的一个换算
                                sheet.setColumnWidth(i, (short) (35.7 * 80));
                                // sheet.autoSizeColumn(i);
                                byte[] bsValue = (byte[]) value;
                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 55, (short) 6, index, (short) 6, index);
                                anchor.setAnchorType(2);
//											patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                            } else if (value == String.class) {
                                // 其它数据类型都当做字符串简单处理
                                textValue = value.toString();
//											cell.setCellValue(textValue);
                            } else if (value == BigDecimal.class) {
                                textValue = value.toString();
//											cell.setCellValue(textValue);
                            } else if (value instanceof Integer) {
                                textValue = new DecimalFormat("#").format(value);
//											cell.setCellValue(textValue);
                            } else {
                                if (null == value) {
                                    textValue = "";
                                } else {
                                    textValue = value.toString();
                                }
//											cell.setCellValue(textValue);
                            }
                            // 如果不是图片数据,就利用正则表达式判断textValue是否全部由数字组成
                            if (textValue != null) {
                                Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                                Matcher matcher = p.matcher(textValue);
                                if (matcher.matches()) {
                                    // 是数字当作double处理
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
//												textValue = new DecimalFormat("#").format(textValue); 
//												cell.setCellValue(new DecimalFormat("#").format(textValue));
//												cell.setCellValue(Double.parseDouble(textValue));
                                    cell.setCellValue(textValue);
                                } else {
                                    cell.setCellValue(textValue);
                                }
                            }
                        }
                    }
                }
            }

            os = response.getOutputStream();
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + "_" + new Date().getTime() + ".xlsx");
            response.setContentType("application/octet-stream");//八进制流 与文件类型无关
            wb.write(os);


//			FileOutputStream fout = new FileOutputStream(file.getName());
//			wb.write(fout);
//			fout.close();
//			FileHandeUtil.downloadexcle(file.getName(),fileName,response, request);
            // aa.xlsx
//			String oldFileNameHz = file.getName();
            //.xlsx
            //================by td
			/*filePath = filePath.replaceAll("\\\\", "/");
			String hz = filePath.substring(filePath.lastIndexOf("."),filePath.length());
			String oldFileName = filePath.substring(filePath.lastIndexOf("/")+1,filePath.lastIndexOf("."));
			String classPath = "";
			if(StringUtils.isNotBlank(fileName)){
				classPath = this.getClass().getClassLoader().getResource("").getPath()+"templatefile/"+fileName+"_"+new Date().getTime()+hz;
			}else{
				classPath = this.getClass().getClassLoader().getResource("").getPath()+"templatefile/"+oldFileName+"_"+new Date().getTime()+hz;;
			}
			FileOutputStream fout = new FileOutputStream(classPath);
			wb.write(fout);
			fout.close();
			File targetFile = new File(classPath);
			ExcelUtils.downloadexcle2(targetFile.getPath(),response, request);
			if(targetFile.exists()){
				targetFile.delete();
			}*/
            //========================
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (null != os) {
                    os.close();
                }
                if(null != in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public void exportExcelByExcelTemplateStyle2(String filePath, String fileName, Collection<T> dataset, String pattem, HttpServletRequest request, HttpServletResponse response) {

        OutputStream os = null;
        InputStream in = null;
        Workbook wb = null;
        try {

            ClassPathResource cpr = new ClassPathResource("/exceltemplate/" + fileName + ".xlsx");
            in = cpr.getInputStream();
            // 得到工作表
            try {
                wb = new XSSFWorkbook(in);
            } catch (Exception ex) {
//	        	wb = new HSSFWorkbook(new FileInputStream(file));
                wb = new HSSFWorkbook(in);
            }
            //第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            //读取标题头
            Row row = sheet.getRow(1);//第二行
            ExcelReader excelReader = new ExcelReader();
            //String[] headerFiled = excelReader.readExcelTitle(row);
            List<String> headerFiled = excelReader.readExcelTitle2List(row, null);
            //设置风格
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            /**
             * 类反射得到调用方法
             */
            // 得到目标目标类的所有的字段列表
            Field fields[] = clazz.getDeclaredFields();
            // 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
            List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
            // 循环读取所有字段
            Map<String, Object> fieldmap = null;
//			Map<String,Object> fieldmap =  new HashMap<String, Object>();
            for (int i = 0; i < fields.length; i++) {
                fieldmap = new HashMap<String, Object>();
                Field f = fields[i];
                // 得到单个字段上的Annotation
                ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
                // 如果标识了Annotationd的话
                if (exa != null) {
                    // 构造设置了Annotation的字段的Setter方法
                    String fieldname = f.getName();
                    String getMethodName = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
                    // 构造调用的method，
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    // 将这个method以Annotaion的名字为key来存入。
                    if (headerFiled.contains(exa.exportName())) {
                        fieldmap.put(exa.exportName(), getMethod);
                        fieldList.add(fieldmap);
                    }

                }
            }
            // 遍历集合数据,产生数据行
            Iterator<T> it = dataset.iterator();
            int index = 1;
            while (it.hasNext()) {
                T t = (T) it.next();
                index++;
                row = sheet.createRow(index);
                // 利用反射，根据javabean属性的先后顺序，动态的调用getXxx()方法得到属性值
                //			Field[] fields = t.getClass().getDeclaredFields();
                // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值 在 实体对象中利用注解的方式
                String titleString = "";
                for (int i = 0; i < fieldList.size(); i++) {
                    for (String headerFile : headerFiled) {
                        if (fieldList.get(i).containsKey(headerFile)) {
                            // 这里得到此列的对应的标题
                            // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的get方法取值
                            Cell cell = row.createCell(i);
                            cell.setCellStyle(cellStyle);
                            Method getMethod = (Method) fieldList.get(i).get(headerFile);
                            Object value = getMethod.invoke(t, new Object[]{});
                            // 判断值的类型后进行强制类型转换
                            String textValue = null;
                            if (value instanceof Boolean) {
                                boolean booleanValue = (Boolean) value;
                                textValue = "true";
                                if (!booleanValue) {
                                    textValue = "false";
                                }
//											cell.setCellValue(textValue);
                            } else if (value instanceof Date) {
                                Date date = (Date) value;
                                SimpleDateFormat sdf = new SimpleDateFormat(pattem);
                                textValue = sdf.format(date);
//											cell.setCellValue(textValue);
                            } else if (value instanceof byte[]) {
                                // 有图片时,设置行高为60px;
                                row.setHeightInPoints(60);
                                // 设置有图片所在列宽度为80px,注意这里单位的一个换算
                                sheet.setColumnWidth(i, (short) (35.7 * 80));
                                // sheet.autoSizeColumn(i);
                                byte[] bsValue = (byte[]) value;
                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 55, (short) 6, index, (short) 6, index);
                                anchor.setAnchorType(2);
//											patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                            } else if (value == String.class) {
                                // 其它数据类型都当做字符串简单处理
                                textValue = value.toString();
//											cell.setCellValue(textValue);
                            } else if (value == BigDecimal.class) {
                                textValue = value.toString();
//											cell.setCellValue(textValue);
                            } else if (value instanceof Integer) {
                                textValue = new DecimalFormat("#").format(value);
//											cell.setCellValue(textValue);
                            } else {
                                if (null == value) {
                                    textValue = "";
                                } else {
                                    textValue = value.toString();
                                }
//											cell.setCellValue(textValue);
                            }
                            // 如果不是图片数据,就利用正则表达式判断textValue是否全部由数字组成
                            if (textValue != null) {
                                Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                                Matcher matcher = p.matcher(textValue);
                                if (matcher.matches()) {
                                    // 是数字当作double处理
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
//												textValue = new DecimalFormat("#").format(textValue);
//												cell.setCellValue(new DecimalFormat("#").format(textValue));
//												cell.setCellValue(Double.parseDouble(textValue));
                                    cell.setCellValue(textValue);
                                } else {
                                    cell.setCellValue(textValue);
                                }
                            }
                        }
                    }
                }
            }

            os = response.getOutputStream();
            response.setHeader("content-disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"), "ISO-8859-1") + "_" + new Date().getTime() + ".xlsx");
            response.setContentType("application/octet-stream");//八进制流 与文件类型无关
            wb.write(os);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (null != os) {
                    os.close();
                }
                if(null != in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 利用spring注解和java反射机制导出数据 并在本地下载
     *
     * @param title   表格标题名
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合，集合中一定要放置符合javabean风格的类的对象。
     * @param pattem  如果有时间数据，设定输出格式。默认为"yyyy-MM-dd";
     *                startRow , startColunm
     */
    public void exportBasicExcelByExcelTemplateForIndex(String filePath, String fileName, Collection<T> dataset, String pattem, Integer startRow, Integer startColunm, HttpServletRequest request, HttpServletResponse response) {
        try {
            File file = new File(filePath);
            InputStream in = new FileInputStream(file);
            // 得到工作表
            Workbook wb = null;
            try {
                wb = new XSSFWorkbook(in);
            } catch (Exception ex) {
                wb = new HSSFWorkbook(new FileInputStream(file));
            }
            //第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            //读取标题头
            Row row = sheet.getRow(0);
            //隐藏列
            ExcelReader excelReader = new ExcelReader();
            //String[] headerFiled = excelReader.readExcelTitle(row);
            List<String> headerFiled = excelReader.readExcelTitle2List(row, null);
            /**
             * 类反射得到调用方法
             */
            // 得到目标目标类的所有的字段列表
            Field fields[] = clazz.getDeclaredFields();
            // 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
            List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
            // 循环读取所有字段
            Map<String, Object> fieldmap = null;
//			Map<String,Object> fieldmap =  new HashMap<String, Object>();
            for (int i = 0; i < fields.length; i++) {
                fieldmap = new HashMap<String, Object>();
                Field f = fields[i];
                // 得到单个字段上的Annotation
                ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
                // 如果标识了Annotationd的话
                if (exa != null) {
                    // 构造设置了Annotation的字段的Setter方法
                    String fieldname = f.getName();
                    String getMethodName = "get" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
                    // 构造调用的method，
                    Method getMethod = clazz.getMethod(getMethodName, new Class[]{});
                    // 将这个method以Annotaion的名字为key来存入。
                    if (headerFiled.contains(exa.exportName())) {
                        fieldmap.put(exa.exportName(), getMethod);
                        fieldList.add(fieldmap);
                    }

                }
            }
            // 遍历集合数据,产生数据行
            Iterator<T> it = dataset.iterator();
            int index = startRow;
            while (it.hasNext()) {
                T t = (T) it.next();
                index++;
                row = sheet.createRow(index);
                // 利用反射，根据javabean属性的先后顺序，动态的调用getXxx()方法得到属性值
                //			Field[] fields = t.getClass().getDeclaredFields();
                // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值 在 实体对象中利用注解的方式
//				int rowCount = 0 ;
                for (int i = 0; i < fieldList.size(); i++) {
                    for (String headerFile : headerFiled) {
                        if (fieldList.get(i).containsKey(headerFile)) {
                            // 这里得到此列的对应的标题
                            // 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的get方法取值
                            Cell cell = row.createCell(i);
//							rowCount++;
                            Method getMethod = (Method) fieldList.get(i).get(headerFile);
                            Object value = getMethod.invoke(t, new Object[]{});
                            // 判断值的类型后进行强制类型转换
                            String textValue = null;
                            if (value instanceof Boolean) {
                                boolean booleanValue = (Boolean) value;
                                textValue = "true";
                                if (!booleanValue) {
                                    textValue = "false";
                                }
//											cell.setCellValue(textValue);
                            } else if (value instanceof Date) {
                                Date date = (Date) value;
                                SimpleDateFormat sdf = new SimpleDateFormat(pattem);
                                textValue = sdf.format(date);
//											cell.setCellValue(textValue);
                            } else if (value instanceof byte[]) {
                                // 有图片时,设置行高为60px;
                                row.setHeightInPoints(60);
                                // 设置有图片所在列宽度为80px,注意这里单位的一个换算
                                sheet.setColumnWidth(i, (short) (35.7 * 80));
                                // sheet.autoSizeColumn(i);
                                byte[] bsValue = (byte[]) value;
                                HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 55, (short) 6, index, (short) 6, index);
                                anchor.setAnchorType(2);
//											patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                            } else if (value == String.class) {
                                // 其它数据类型都当做字符串简单处理
                                textValue = value.toString();
//											cell.setCellValue(textValue);
                            } else if (value == BigDecimal.class) {
                                textValue = value.toString();
//											cell.setCellValue(textValue);
                            } else if (value instanceof Integer) {
                                textValue = new DecimalFormat("#").format(value);
//											cell.setCellValue(textValue);
                            } else {
                                if (null == value) {
                                    textValue = "";
                                } else {
                                    textValue = value.toString();
                                }
//											cell.setCellValue(textValue);
                            }
                            // 如果不是图片数据,就利用正则表达式判断textValue是否全部由数字组成
                            if (textValue != null) {
                                Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
                                Matcher matcher = p.matcher(textValue);
                                if (matcher.matches()) {
                                    // 是数字当作double处理
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
//												textValue = new DecimalFormat("#").format(textValue); 
//												cell.setCellValue(new DecimalFormat("#").format(textValue));
//												cell.setCellValue(Double.parseDouble(textValue));
                                    cell.setCellValue(textValue);
                                } else {
                                    cell.setCellValue(textValue);
                                }
                            }
                        }
                    }
                }
            }
//			FileOutputStream fout = new FileOutputStream(file.getName());
//			wb.write(fout);
//			fout.close();
//			FileHandeUtil.downloadexcle(file.getName(),fileName,response, request);
            // aa.xlsx
//			String oldFileNameHz = file.getName();
            //.xlsx
            filePath = filePath.replaceAll("\\\\", "/");
            String hz = filePath.substring(filePath.lastIndexOf("."), filePath.length());
            String oldFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
            String classPath = "";
            if (StringUtils.isNotBlank(fileName)) {
                classPath = this.getClass().getClassLoader().getResource("/").getPath() + "templatefile/" + fileName + "_" + new Date().getTime() + hz;
            } else {
                classPath = this.getClass().getClassLoader().getResource("/").getPath() + "templatefile/" + oldFileName + "_" + new Date().getTime() + hz;
                ;
            }
            FileOutputStream fout = new FileOutputStream(classPath);
            wb.write(fout);
            fout.close();
            File targetFile = new File(classPath);
            ExcelUtils.downloadexcle2(targetFile.getPath(), response, request);
            if (targetFile.exists()) {
                targetFile.delete();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
        }
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet    要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow   结束行
     * @param firstCol 开始列
     * @param endCol   结束列
     * @return 设置好的sheet
     */
    public static HSSFSheet setHSSFValidation(HSSFSheet sheet, String[] textlist, int firstRow, int endRow, int firstCol, int endCol) {
        // 加载下拉列表内容  
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列  
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象  
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    public static void main(String[] args) {
        int a = 2;
        for (int i = a; i <= a; i--) {
            a = a - 1;
            if (a < 0) {
                break;
            }
            System.out.println(a);
        }
    }


    /**
     * @param @param  sheet
     * @param @param  firstRow
     * @param @param  endRow
     * @param @param  firstCol
     * @param @param  endCol
     * @param @param  expr1
     * @param @param  expr2
     * @param @return
     * @return HSSFSheet
     * @throws
     * @Title: setHSSFNumericValidation
     * @Description: 设置数字格式
     */
    public static HSSFSheet setHSSFNumberValidation(HSSFSheet sheet, int firstRow, int endRow, int firstCol, int endCol, String expr1, String expr2) {
        // 驾照列为数字验证
        DVConstraint constraint = DVConstraint.createNumericConstraint(ValidationType.INTEGER, OperatorType.BETWEEN, expr1, expr2);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(data_validation_list);
        return sheet;
    }

    /**
     * @param @param  sheet
     * @param @param  firstRow
     * @param @param  endRow
     * @param @param  firstCol
     * @param @param  endCol
     * @param @param  expr1
     * @param @param  expr2
     * @param @return
     * @return HSSFDataValidation
     * @throws
     * @Title: setHSSFLengthValidation
     * @Description: 设置长度
     */
    public static HSSFSheet setHSSFLengthValidation(HSSFSheet sheet, int firstRow, int endRow, int firstCol, int endCol, String expr1, String expr2) {
        DVConstraint constraint = DVConstraint.createNumericConstraint(DVConstraint.ValidationType.TEXT_LENGTH,
                DVConstraint.OperatorType.BETWEEN, expr1, expr2);
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endRow);
        // 设定在哪个单元格生效
        HSSFDataValidation lengthValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(lengthValidation);
        return sheet;
    }

    /**
     * @param @param  sheet
     * @param @param  firstRow
     * @param @param  endRow
     * @param @param  firstCol
     * @param @param  endCol
     * @param @param  expr1
     * @param @param  expr2
     * @param @param  dateFormat
     * @param @return
     * @return HSSFSheet
     * @throws
     * @Title: setHSSFDateValidation
     * @Description: 设置excel单元格日期格式
     */
    public static HSSFSheet setHSSFDateValidation(HSSFSheet sheet, int firstRow, int endRow, int firstCol, int endCol, String expr1, String expr2, String dateFormat) {
        //日期列为日期格式验证
        DVConstraint constraint = DVConstraint.createDateConstraint(ValidationType.DATE, expr1, expr2, dateFormat);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        //数据有效
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidation);
        return sheet;
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet         要设置的sheet.
     * @param promptTitle   标题
     * @param promptContent 内容
     * @param firstRow      开始行
     * @param endRow        结束行
     * @param firstCol      开始列
     * @param endCol        结束列
     * @return 设置好的sheet.
     */
    public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle, String promptContent, int firstRow, int endRow, int firstCol, int endCol) {
        // 构造constraint对象  
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("BB1");
        // 四个参数分别是：起始行、终止行、起始列、终止列  
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象  
        HSSFDataValidation data_validation_view = new HSSFDataValidation(regions, constraint);
        data_validation_view.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(data_validation_view);
        return sheet;
    }
}
