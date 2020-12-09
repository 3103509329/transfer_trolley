package com.zhcx.netcar.netcarservice.utils.excel;

import com.zhcx.netcar.netcarservice.common.ExcelAnnotation;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class ExportByStep {
	
	private static final Logger logger = LoggerFactory.getLogger(ExportByStep.class);
	
	private static short fontColor = Font.COLOR_NORMAL;
	private static short  fontHeight = 10;
	private static String fontName="Arial";
	
	/**
	 * @Title: initWorkbook
	 * @Description: step 1
	 * @param @return
	 * @return Workbook
	 * @throws @author
	 *             lyl
	 * @date 2017年12月20日 下午5:21:48
	 */
	public static Workbook initWorkbook() {
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		return workbook;
	}

	/**
	 * 根据文件路径初始化模板
	 * 
	 * @Title:initWorkBook
	 * @Description:
	 * @param:@return
	 * @return:Workbook
	 * @author zk
	 */
	public static Workbook initWorkBook(String filePath) {
		
		Workbook wb = null;
		SXSSFWorkbook swb = null;
		File file = null;
		InputStream in = null;
		try {
			file = new File(filePath);
			in = new FileInputStream(file);
			
			wb = WorkbookFactory.create(in);
			
//			wb = new XSSFWorkbook(in);
		} catch (Exception ex) {
			try {
				wb = new HSSFWorkbook(new FileInputStream(file));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		} finally {
			try {
				if (null != in) {
					in.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
//		swb = new SXSSFWorkbook((XSSFWorkbook) wb);
		return wb;
	}

	public static Sheet initSheet(Workbook workbook, String sheetName) {
		return workbook.createSheet(null == sheetName ? "" : sheetName);
	}
	
	/**
	 * 从模板中获取sheet
	 * 
	 * @Title:initTemplateSheet
	 * @Description:
	 * @param:@param workbook
	 * @param:@param sheetName
	 * @param:@return
	 * @return:Sheet
	 * @author zk
	 */
	public static Sheet initTemplateSheet(Workbook workbook, String sheetName) {
		Sheet sheet = workbook.getSheetAt(0);
		return sheet;
	}

	/**
	 * 从模板中获取第一行标题
	 * @Title:getInitRowFromTemplate
	 * @Description:
	 * @param:@param sheet
	 * @param:@return
	 * @return:Row
	 * @thros
	 */
	public static Row getInitRowFromTemplate(Sheet sheet) {
		return sheet.getRow(0);
	}
	
	/**
	 * @Title: writeFristOne
	 * @Description: step 2
	 * @param @return
	 * @return Workbook
	 * @throws @author
	 *             lyl
	 * @date 2017年12月20日 下午5:21:48
	 */
	public static void writeFristOne(List<ExcelItem> excelItemsList, Sheet sheet) {
		Row row = sheet.createRow(0);
		for (int i = 0; i < excelItemsList.size(); i++) {
			Cell celi = row.createCell(i);
			celi.setCellValue(excelItemsList.get(i).getName());
		}
	}
	
	
	/**
	 * 
	 * @Description: 带样式的单表头
	 * @Title: writeHeader 
	 * @param @param excelItemsList
	 * @param @param sheet
	 * @param @param workbook
	 * @return void
	 * @throws
	 */
	public static void writeHeader(List<ExcelItem> excelItemsList, Sheet sheet, Workbook workbook) {
		Row row = sheet.createRow(0);
		
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//设置字体
		Font font = workbook.createFont();
		font.setColor(fontColor);//字体颜色
		font.setFontHeightInPoints(fontHeight);//字体大小
		font.setFontName(fontName);
		style.setFont(font);
		
		for (int i = 0; i < excelItemsList.size(); i++) {
			Cell celi = row.createCell(i);
			celi.setCellValue(excelItemsList.get(i).getName());
			celi.setCellStyle(style);
			
			sheet.setColumnWidth(i, 5000);
       	    sheet.setDefaultColumnStyle(i, style);
		}
	}
	
	/**
	 * @param workbook 
	 * @Description: 输出excel表头（双表头，复合表头）
	 * @Title: writeHeader 
	 * @param @param excelItemsList
	 * @param @param sheet
	 * @param @param style
	 * @return void
	 * @throws
	 */
	public static void writeDoubleHeader(List<ExcelItem> excelItemsList, Sheet sheet, Workbook workbook) {
		Row row = sheet.createRow(0);
		Row childRow = sheet.createRow(1);
		
		Cell firstCell=null;
		Cell firstCell2=null;
		
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		//设置字体
		Font font = workbook.createFont();
		font.setColor(fontColor);//字体颜色
		font.setFontHeightInPoints(fontHeight);//字体大小
		font.setFontName(fontName);
		style.setFont(font);
		
		for (int i = 0; i < excelItemsList.size(); i++) {
			String name = excelItemsList.get(i).getName();
			String childName = excelItemsList.get(i).getChildName();
			
			Cell cell = row.createCell(i);
			cell.setCellValue(name);
			cell.setCellStyle(style);
			
			Cell cell2 = childRow.createCell(i);
			if(StringUtil.isNullOrEmpty(childName) || childName.equals(name)) {
				cell2.setCellValue(name);
				cell2.setCellStyle(style);
				//对列合并操作
				CellRangeAddress region = new CellRangeAddress(0, 1, i, i);
				sheet.addMergedRegion(region );
			}else {
				cell2.setCellValue(childName);
				cell2.setCellStyle(style);
			}
			
			
			//行合并
			if(firstCell==null) {
				firstCell = cell;
				firstCell2= cell2;
			}else {
				if(!firstCell.getStringCellValue().equals(name)) {
					if(firstCell.getColumnIndex()!=cell.getColumnIndex()-1) {
						CellRangeAddress region = new CellRangeAddress(0, 0, firstCell.getColumnIndex(), cell.getColumnIndex()-1);
						sheet.addMergedRegion(region );
					}
					firstCell = cell;
				}
				
				if(!firstCell2.getStringCellValue().equals(childName)) {
					if(firstCell2.getColumnIndex()!=cell2.getColumnIndex()-1) {
						CellRangeAddress region = new CellRangeAddress(0, 0, firstCell2.getColumnIndex(), cell2.getColumnIndex()-1);
						sheet.addMergedRegion(region);
					}
					firstCell2 = cell2;
				}
				
				if( i == (excelItemsList.size()-1)) {
					if(firstCell.getStringCellValue().equals(name) && firstCell.getColumnIndex()!=cell.getColumnIndex()) {
						CellRangeAddress region = new CellRangeAddress(0, 0, firstCell.getColumnIndex(), cell.getColumnIndex());
						sheet.addMergedRegion(region );
					}
				}
			}
			
			sheet.setColumnWidth(i, 5000);
	   	    sheet.setDefaultColumnStyle(i, style);
		}
		
	}

	/**
	 * @Title: writePoi
	 * @Description: stet 3
	 * @param  excelItemsList
	 * @param  sheet
	 * @param  workbook
	 * @param  data
	 * @param  param
	 * @return void
	 * @author
	 *             lyl
	 * @date 2017年12月20日 下午5:22:13
	 */
	public static <E> void writePoi(List<ExcelItem> excelItemsList, Sheet sheet, Workbook workbook, List<E> data,
                                    int pageNo, int pageSize, int headSize, String sdfPattem) {
		try {
			Class<?> clazz = data.get(0).getClass();
			int startRow = (pageNo - 1) * pageSize + headSize;
			for (int rowNo = startRow; rowNo < data.size() + startRow; rowNo++) {// 数据行从sheet第二行开始
				Row dataRow = getRow(sheet, rowNo);
				Object rowDataValue = data.get(rowNo - startRow);
				for (int i = 0; i < excelItemsList.size(); i++) {
					ExcelItem excelItem = excelItemsList.get(i);
					String excelItemCode = excelItem.getCode();
					Method getMethod = getClazzMethod(clazz, excelItemCode);
					if(getMethod==null) {
						continue ;
					}
					getMethod.setAccessible(true);
					Cell cell = dataRow.createCell(i);
					setCellStyleAndValue(getMethod, rowDataValue, cell, workbook,sdfPattem);
				}

			}
			data.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private static Method getClazzMethod(Class<?> clazz,String fieldName) {
		Method getMethod = null;
		try {
			getMethod = clazz.getDeclaredMethod(
					"get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
					new Class[] {});
		} catch (NoSuchMethodException e) {
			Class<?> supperClazz = clazz.getSuperclass();
			if(supperClazz !=null) {
				return getClazzMethod(supperClazz, fieldName);
			}else {
				e.printStackTrace();
			}
			
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return getMethod;
	}
	public static <E> void writePoiRow(List<ExcelItem> excelItemsList, Sheet sheet, Workbook workbook, E data, int rowIndex, String sdfPattem) {
		try {
			Class<?> clazz = data.getClass();
			Row dataRow = getRow(sheet, rowIndex);
			Object rowDataValue = data;
			for (int i = 0; i < excelItemsList.size(); i++) {
				ExcelItem excelItem = excelItemsList.get(i);
				String excelItemCode = excelItem.getCode();
				
				Method getMethod = clazz.getDeclaredMethod(
						"get" + excelItemCode.substring(0, 1).toUpperCase() + excelItemCode.substring(1),
						new Class[] {});
				getMethod.setAccessible(true);
				Cell cell = dataRow.createCell(i);
				setCellStyleAndValue(getMethod, rowDataValue, cell, workbook,sdfPattem);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 数据写入模板
	 * 
	 * @Title:writePoi
	 * @Description:
	 * @param:@param sheet
	 * @param:@param workbook
	 * @param:@param data
	 * @param:@param pageNo
	 * @param:@param pageSize
	 * @param:@param clazz
	 * @param:@param pattem
	 * @return:void
	 * @author zk
	 */
	public static <T> void writePoi(Sheet sheet, Workbook workbook, List<T> data, int pageNo, int pageSize,
                                    String pattem) {
		Map<String, Object> fieldMap = null;
		int startRow = (pageNo - 1) * pageSize + 1;
		Class<?> clazz = data.get(0).getClass();
		try {
			// 1.读取标题头
			Row row = sheet.getRow(0);
			ExcelReader excelReader = new ExcelReader();
			List<String> titleField = excelReader.readExcelTitle2List(row, null);

			// 2.得到目标类所有需要导出的数据字段--利用反射
			Field[] fields = clazz.getDeclaredFields();
			List<Map<String, Object>> fieldList = new ArrayList<Map<String, Object>>();
			int fieldSize = fields.length;
			for (int i = 0; i < fieldSize; i++) {
				fieldMap = new HashMap<String, Object>();
				Field field = fields[i];
				ExcelAnnotation eat = field.getAnnotation(ExcelAnnotation.class);
				if (null != eat) {
					String fieldName = field.getName();
					String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
					Method getMethod = clazz.getMethod(getMethodName, new Class[] {});
					if (titleField.contains(eat.exportName())) {
						fieldMap.put(eat.exportName(), getMethod);
						fieldList.add(fieldMap);
					}
				}
			}

			// 3.遍历数据集
			ListIterator<T> it = (ListIterator<T>) data.listIterator();
			int index = startRow;
			int fieldListSize = fieldList.size();
//			Row row = null;
			while (it.hasNext()) {
				T t = it.next();
				index++;
				row = sheet.createRow(index);
				String titleString = "";
				for (int i = 0; i < fieldListSize; i++) {
//					for (String title : titleField) {
					for(int j = 0;j<titleField.size();j++) {
						if (fieldList.get(i).containsKey(titleField.get(j))) {
							Cell cell = row.createCell(j);
							Method getMethod = (Method) fieldList.get(i).get(titleField.get(j));
							Object value = getMethod.invoke(t, new Object[] {});

							String textValue = null;
							if (value instanceof Boolean) {
								boolean booleanValue = (Boolean) value;
								textValue = "true";
								if (!booleanValue) {
									textValue = "false";
								}
							} else if (value instanceof Date) {
								Date date = (Date) value;
								SimpleDateFormat sdf = new SimpleDateFormat(pattem);
								textValue = sdf.format(date);
							} else if (value instanceof byte[]) {
								// 有图片时,设置行高为60px;
								row.setHeightInPoints(60);
								// 设置有图片所在列宽度为80px,注意这里单位的一个换算
								sheet.setColumnWidth(i, (short) (35.7 * 80));
								byte[] bsValue = (byte[]) value;
								HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 55, (short) 6, index,
										(short) 6, index);
								anchor.setAnchorType(2);
							} else if (value instanceof String) {
								// 其它数据类型都当做字符串简单处理
								textValue = value.toString();
							} else if (value instanceof BigDecimal) {
								textValue = value.toString();
							} else if (value instanceof Integer) {
								textValue = new DecimalFormat("#").format(value);
							} else {
								if (null == value) {
									textValue = "";
								} else {
									textValue = value.toString();
								}
							}

							if (textValue != null) {
								Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");
								Matcher matcher = p.matcher(textValue);
								if (matcher.matches()) {
									// 是数字当作double处理
									cell.setCellType(Cell.CELL_TYPE_STRING);
									cell.setCellValue(textValue);
								} else {
									cell.setCellValue(textValue);
								}
							}
						}
					}
				}

			}
			data.clear();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * @Title: download
	 * @Description: step 4
	 * @param @param
	 *            workbook
	 * @param @param
	 *            response
	 * @return void
	 * @throws @author
	 *             lyl
	 * @date 2017年12月20日 下午5:22:25
	 */
	public static void download(Workbook workbook, String fileName, HttpServletResponse response) {
		response.setCharacterEncoding("UTF-8");
		try {
			response.setHeader("content-disposition",
					"attachment;filename=" +URLEncoder.encode(fileName+ ".xls","UTF-8"));
			response.setContentType("application/octet-stream");// 八进制流 与文件类型无关
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			log.error("download failed!!!", e);
		}
	}
	/**
	 * 模板文件下载
	 * 
	 * @Title:download
	 * @Description:
	 * @param:@param workbook
	 * @param:@param filePath
	 * @param:@param fileName
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 * @author zk
	 */
	public static void download(Workbook workbook, String filePath, String fileName, HttpServletRequest request,
                                HttpServletResponse response) {
		filePath = filePath.replaceAll("\\\\", "/");
		String suffix = filePath.substring(filePath.lastIndexOf("."), filePath.length());
		String oldFileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
		
		String classPath = new Object() {
			public String getPath() {
				return this.getClass().getClassLoader().getResource("/").getPath();
			}
		}.getPath();
		
		FileOutputStream fos = null;
		
		if (StringUtils.isNotBlank(fileName)) {
//			classPath = ExportByStep.class.getClass().getClassLoader().getResource("/").getPath() + "templatefile/"
//					+ fileName + "_" + new Date().getTime() + suffix;
			classPath = classPath+ "templatefile/"+ fileName + "_" + new Date().getTime() + suffix;
		} else {
//			classPath = ExportByStep.class.getClass().getClassLoader().getResource("/").getPath() + "templatefile/"
//					+ oldFileName + "_" + new Date().getTime() + suffix;
			classPath = classPath+ "templatefile/"+ oldFileName + "_" + new Date().getTime() + suffix;
		}

		try {
			fos = new FileOutputStream(classPath);
			workbook.write(fos);
			File targetFile = new File(classPath);
			FileHandeUtil.downloadexcle2(targetFile.getPath(), response, request);
			if (targetFile.exists()) {
				targetFile.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fos) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static String getClassPath(String fileName) {
		String path = new Object() {
			public String getPath() {
				return this.getClass().getClassLoader().getResource("/").getPath();
			}
		}.getPath();
		return path;
	}

	/**
	 * sheet的row使用treeMap存储的，是非线程安全的，所以在创建row时需要进行同步操作。
	 * 
	 * @param sheet
	 * @param rownum
	 * @return
	 */
	private static synchronized Row getRow(Sheet sheet, int rownum) {
		return sheet.createRow(rownum);
	}

	public static void setCellStyleAndValue(Method getMethod, Object rowDataValue, Cell cell, Workbook workbook, String sdfPattem)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		XSSFCellStyle cellStyle = (XSSFCellStyle) workbook.createCellStyle();
		//设置字体
//		Font font = workbook.createFont();
//		font.setColor(fontColor);//字体颜色
//		font.setFontHeightInPoints(fontHeight );//字体大小
//		font.setFontName(fontName  );
//		cellStyle.setFont(font);
		
		
		Class<?> methodReturnType = getMethod.getReturnType();
		if (methodReturnType.isPrimitive()) {
			if ("int".equals(methodReturnType.toString())) {
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
				int cellValue = getMethod.invoke(rowDataValue, new Object[] {}) == null ? 0
						: (int) getMethod.invoke(rowDataValue, new Object[] {});// get方法不需要传参数
				cell.setCellValue(cellValue);
			} else if ("double".equals(methodReturnType.toString())) {
				cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
				double cellValue = getMethod.invoke(rowDataValue, new Object[] {}) == null ? 0.0
						: (double) getMethod.invoke(rowDataValue, new Object[] {});// get方法不需要传参数
				cell.setCellValue(cellValue);
			}
		} else {
			Object value = getMethod.invoke(rowDataValue, new Object[] {});// get方法不需要传参数
			if (value == null){  
				cell.setCellValue("");  
            } else if (value instanceof String) {  
            	cell.setCellValue((String) value);  
            } else if (value instanceof Integer) {  
            	cell.setCellValue((Integer) value);  
            } else if (value instanceof Long) {  
            	cell.setCellValue((Long) value);  
            } else if (value instanceof Double) {  
            	cell.setCellValue((Double) value);  
            } else if (value instanceof Float) {  
            	cell.setCellValue((Float) value);  
            } else if (value instanceof Date) {
            	Date date= (Date) value;
            	if(date==null || date.getTime()==32503651199000L || date.getTime()==-2209017600000L) {
                	cell.setCellValue("--");  
            	}else {
            		DataFormat format = workbook.createDataFormat();
                	cellStyle.setDataFormat(format.getFormat(sdfPattem));
                	cell.setCellValue((Date) value);  	
            	}
            	
            }  else if (value instanceof BigDecimal) {  
                double doubleVal = ((BigDecimal) value).doubleValue();  
                cell.setCellValue(doubleVal);
            }else {
            	cell.setCellValue(value.toString());  
            }
		}
		cell.setCellStyle(cellStyle);
	}
}
