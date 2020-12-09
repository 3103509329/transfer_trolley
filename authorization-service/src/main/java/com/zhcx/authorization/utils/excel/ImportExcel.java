package com.zhcx.authorization.utils.excel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.zhcx.basicdata.common.ExcelAnnotation;
/**
 * @ClassName：ImportExcel
 * @Description: 导入excle
 * @author：luoyalan
 * @date：2015年11月3日 下午1:16:26 
 * @version 
 * @param <T>
 */
public class ImportExcel<T> {
	Class<T> clazz;

	public ImportExcel(Class<T> clazz) {
		this.clazz = clazz;
	}
	Logger log = Logger.getLogger(ImportExcel.class);
    /**
     * @Title: importExcel 
     * @Description: 利用反射机制导入数据
     * @param @param file
     * @param @param pattern
     * @param @return
     * @author luoyalan
     * @return Collection<T>
     * @throws
     */
	public Collection<T> importExcel(MultipartFile file, String... pattern) {
		Collection<T> dist = new ArrayList();
		try {
			/**
			 * 类反射得到调用方法
			 */
			Map fieldmap = getFiledList();
			/**
			 * excel的解析开始
			 */
			Workbook book = null;
	        try {
	            book = new XSSFWorkbook(file.getInputStream());
	        } catch (Exception ex) {
	            book = new HSSFWorkbook(file.getInputStream());
	        }
			// // 得到第一页
			Sheet sheet = book.getSheetAt(0);
			// // 得到第一面的所有行
			Iterator<Row> row = sheet.rowIterator();
			/**
			 * 标题解析
			 */
			// 得到第一行，也就是标题行
			Row title = row.next();
			// 得到第一行的所有列
			Iterator<Cell> cellTitle = title.cellIterator();
			// 将标题的文字内容放入到一个map中。
			Map titlemap = new HashMap();
			// 从标题第一列开始
			int i = 0;
			// 循环标题所有的列
			while (cellTitle.hasNext()) {
				Cell cell = cellTitle.next();
				String value = cell.getStringCellValue();
				titlemap.put(i, value);
				i = i + 1;
			}
			/**
			 * 解析内容行
			 */
			// 用来格式化日期的DateFormat
			SimpleDateFormat sf;
			if (pattern.length < 1) {
				sf = new SimpleDateFormat("yyyy-MM-dd");
			} else
				sf = new SimpleDateFormat(pattern[0]);
			int readflag = 0 ;
			while (row.hasNext()) {
//			int rowsize = sheet.getLastRowNum();
//			for(int p = 0 ; p < rowsize ; p ++){
				// 标题下的第一行
				Row rown = row.next();
				readflag ++ ;
				if(readflag == 1){
					continue;
				}
				if(isBlankRow(rown)) 
					continue;
				// 行的所有列
				Iterator<Cell> cellbody = rown.cellIterator();
				// 得到传入类的实例
				T tObject = clazz.newInstance();
				int k = 0;
				// 遍历一行的列
//				while (cellbody.hasNext()) {
				for(int b = 0; b < rown.getLastCellNum(); b++){
//					Cell cell = cellbody.next();
					Cell cell = rown.getCell(b);
					// 这里得到此列的对应的标题
//					String titleString = (String) titlemap.get(k);
					String titleString = (String) titlemap.get(b);
					// 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值
					if (fieldmap.containsKey(titleString)) {
						Method setMethod = (Method) fieldmap.get(titleString);
						// 得到setter方法的参数
						Class<?>[] ts = setMethod.getParameterTypes();
						// 只要一个参数
						Object xclass = ts[0];
						// 判断参数类型
//						setMethod.invoke(tObject, getValue(cell));
						if (xclass == String.class) {
							setMethod.invoke(tObject,getValue(cell));
						}else if (xclass == Date.class) {
							try {
								setMethod.invoke(tObject, StringUtils.isBlank(getValue(cell))? null : sf.parse(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中Date类型异常");
							}
						} else if(xclass == BigDecimal.class){
							try {
								setMethod.invoke(tObject, (StringUtils.isBlank(getValue(cell)) || null == getValue(cell)) ? null : new BigDecimal(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中BigDecimal类型异常");
							}
						}else if(xclass == Integer.class || xclass == int.class){
							try {
								setMethod.invoke(tObject, StringUtils.isBlank(getValue(cell)) ? null : Integer.parseInt(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中Integer类型异常");
							}
						}else if(xclass == Long.class || xclass == long.class){
							try {
								setMethod.invoke(tObject,  StringUtils.isBlank(getValue(cell)) ? null : Long.parseLong(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中Long类型异常");
							}
						}else if (xclass == Boolean.class) {
							String boolname = "是";
							if (cell.getStringCellValue().equals("否")) {
								boolname = "否";
							}
							setMethod.invoke(tObject, boolname);
						}
					}
					// 下一列
					k = k + 1;
				}
				dist.add(tObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dist;
	}
	
	

	public static boolean isBlankRow(Row row){
		if(row == null) return true;
		boolean result = true;
		for(int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++){
			Cell cell = row.getCell(i,Row.RETURN_BLANK_AS_NULL);
			String value = "";
			if(cell != null){
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					value = String.valueOf((int) cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					value = String.valueOf(cell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					value = String.valueOf(cell.getCellFormula());
					break;
				default:
					break;
				}
				
				if(!value.trim().equals("")){
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * @Title: getFiledList 
	 * @Description: 获取类中zhu
	 * @param @return
	 * @return List<Map<String,Object>>
	 * @author lyl
	 * @throws
	 */
	public Map<String,Object> getFiledList(){
		/**
		 * 类反射得到调用方法
		 */
		// 得到目标目标类的所有的字段列表
		Field filed[] = clazz.getDeclaredFields();
		// 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
		List<Map<String,Object>> filedList = new ArrayList<Map<String,Object>>();
		// 循环读取所有字段
		Map fieldmap = new HashMap();
		try {
			for (int i = 0; i < filed.length; i++) {
				Field f = filed[i];
				// 得到单个字段上的Annotation
				ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
				// 如果标识了Annotationd的话
				if (exa != null) {
					// 构造设置了Annotation的字段的Setter方法
					String fieldname = f.getName();
					String setMethodName = "set" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
					// 构造调用的method，
					Method setMethod = clazz.getMethod(setMethodName, new Class[] { f.getType() });
					// 将这个method以Annotaion的名字为key来存入。
					fieldmap.put(exa.exportName(), setMethod);
//					filedList.add(fieldmap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fieldmap;
	}
	
	/*public Collection<T> importExcelTest(File file, String... pattern) {
		Collection<T> dist = new ArrayList();
		try {
			*//**
			 * 类反射得到调用方法
			 *//*
			// 得到目标目标类的所有的字段列表
			Field filed[] = clazz.getDeclaredFields();
			// 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
			List<Map<String,Object>> filedList = new ArrayList<Map<String,Object>>();
			// 循环读取所有字段
			Map fieldmap = new HashMap();
			for (int i = 0; i < filed.length; i++) {
				Field f = filed[i];
				// 得到单个字段上的Annotation
				ExcelAnnotation exa = f.getAnnotation(ExcelAnnotation.class);
				// 如果标识了Annotationd的话
				if (exa != null) {
					// 构造设置了Annotation的字段的Setter方法
					String fieldname = f.getName();
					String setMethodName = "set" + fieldname.substring(0, 1).toUpperCase() + fieldname.substring(1);
					// 构造调用的method，
					Method setMethod = clazz.getMethod(setMethodName, new Class[] { f.getType() });
					// 将这个method以Annotaion的名字为key来存入。
					fieldmap.put(exa.exportName(), setMethod);
					filedList.add(fieldmap);
				}
			}
			*//**
			 * excel的解析开始
			 *//*
			// 将传入的File构造为FileInputStream;
			FileInputStream in = new FileInputStream(file);
			// // 得到工作表
			Workbook book = null;
	        try {
	            book = new XSSFWorkbook(file);
	        } catch (Exception ex) {
	            book = new HSSFWorkbook(new FileInputStream(file));
	        }
			//得到第一页
			Sheet sheet = book.getSheetAt(0);
			// // 得到第一面的所有行
			Iterator<Row> row = sheet.rowIterator();
			*//**
			 * 标题解析
			 *//*
			// 得到第一行，也就是标题行
			Row title = row.next();
			// 得到第一行的所有列
			Iterator<Cell> cellTitle = title.cellIterator();
			// 将标题的文字内容放入到一个map中。
			Map titlemap = new HashMap();
			// 从标题第一列开始
			int i = 0;
			// 循环标题所有的列
			while (cellTitle.hasNext()) {
				Cell cell = cellTitle.next();
				String value = cell.getStringCellValue();
				titlemap.put(i, value);
				i = i + 1;
			}
			*//**
			 * 解析内容行
			 *//*
			// 用来格式化日期的DateFormat
			SimpleDateFormat sf;
			if (pattern.length < 1) {
				sf = new SimpleDateFormat("yyyy-MM-dd");
			} else
				sf = new SimpleDateFormat(pattern[0]);
//			int rows = sheet.getLastRowNum();
			int readflag = 0 ;
			while (row.hasNext()) {
				// 标题下的第一行
				Row rown = row.next();
				readflag ++ ;
				if(readflag == 1){
					continue;
				}
				// 行的所有列
//				Iterator<Cell> cellbody = rown.cellIterator();
				// 得到传入类的实例
				T tObject = clazz.newInstance();
				int k = 0;
				// 遍历一行的列
//				while (cellbody.hasNext()) {
				int cellsize = rown.getLastCellNum();
				for(int b = 0; b < cellsize ; b ++){
					Cell cell = rown.getCell(b) ;
					// 这里得到此列的对应的标题
					String titleString = (String) titlemap.get(k);
					// 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值
					if (fieldmap.containsKey(titleString)) {
						Method setMethod = (Method) fieldmap.get(titleString);
						// 得到setter方法的参数
						Class<?>[] ts = setMethod.getParameterTypes();
						// 只要一个参数
						Object xclass = ts[0];
						// 判断参数类型
						if (xclass == String.class) {
							setMethod.invoke(tObject,getValue(cell));
						}else if (xclass == Date.class) {
							try {
								setMethod.invoke(tObject, StringUtils.isBlank(getValue(cell))? null : sf.parse(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中Date类型异常");
							}
						} else if(xclass == BigDecimal.class){
							try {
								setMethod.invoke(tObject, (StringUtils.isBlank(getValue(cell)) || null == getValue(cell)) ? null : new BigDecimal(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中BigDecimal类型异常");
							}
						}else if(xclass == Integer.class || xclass == int.class){
							try {
								setMethod.invoke(tObject, StringUtils.isBlank(getValue(cell)) ? null : Integer.parseInt(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中Integer类型异常");
							}
						}else if(xclass == Long.class || xclass == long.class){
							try {
								setMethod.invoke(tObject,  StringUtils.isBlank(getValue(cell)) ? null : Long.parseLong(getValue(cell)));
							} catch (Exception e) {
								log.error("解析excel中Long类型异常");
							}
						}else if (xclass == Boolean.class) {
							String boolname = "是";
							if (cell.getStringCellValue().equals("否")) {
								boolname = "否";
							}
							setMethod.invoke(tObject, boolname);
						}
					}
					// 下一列
					k = k + 1;
				}
				dist.add(tObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return dist;
	}
	*/
//	
//	public Collection<T> importOtherExcel(MultipartFile file, String... pattern) {
//		Collection<T> dist = new ArrayList();
//		try {
//			// 得到目标目标类的所有的字段列表
//			Field filed[] = clazz.getDeclaredFields();
//			// 将所有标有Annotation的字段，也就是允许导入数据的字段,放入到一个map中
//			Map fieldmap = new HashMap();
//			// 循环读取所有字段
//			for (int i = 0; i < filed.length; i++) {
//				Field f = filed[i];
//			}
//			/**
//			 * excel的解析开始
//			 */
//			// 将传入的File构造为FileInputStream;
//			InputStream  in = file.getInputStream();
//			//得到工作表
//			HSSFWorkbook book = new HSSFWorkbook(in);
//			//得到第一页
//			HSSFSheet sheet = book.getSheetAt(0);
//			//得到第一面的所有行
//			Iterator<Row> row = sheet.rowIterator();
//			/**
//			 * 标题解析
//			 */
//			// 得到第一行，也就是标题行
//			Row title = row.next();
//			// 得到第一行的所有列
//			Iterator<Cell> cellTitle = title.cellIterator();
//			// 将标题的文字内容放入到一个map中。
//			Map titlemap = new HashMap();
//			// 从标题第一列开始
//			int i = 0;
//			// 循环标题所有的列
//			while (cellTitle.hasNext()) {
//				Cell cell = cellTitle.next();
//				String value = cell.getStringCellValue();
//				titlemap.put(i, value);
//				i = i + 1;
//			}
//			/**
//			 * 解析内容行
//			 */
//			// 用来格式化日期的DateFormat
//			SimpleDateFormat sf;
//			if (pattern.length < 1) {
//				sf = new SimpleDateFormat("yyyy-MM-dd");
//			} else
//				sf = new SimpleDateFormat(pattern[0]);
//			while (row.hasNext()) {
//				// 标题下的第一行
//				Row rown = row.next();
//				// 行的所有列
//				Iterator<Cell> cellbody = rown.cellIterator();
//				// 得到传入类的实例
//				T tObject = clazz.newInstance();
//				int k = 0;
//				// 遍历一行的列
//				while (cellbody.hasNext()) {
//					Cell cell = cellbody.next();
//					// 这里得到此列的对应的标题
//					String titleString = (String) titlemap.get(k);
//					// 如果这一列的标题和类中的某一列的Annotation相同，那么则调用此类的的set方法，进行设值
//					if (fieldmap.containsKey(titleString)) {
//						Method setMethod = (Method) fieldmap.get(titleString);
//						// 得到setter方法的参数
//						Class<?>[] ts = setMethod.getParameterTypes();
//						// 只要一个参数
//						Object xclass = ts[0];
//						// 判断参数类型
//						System.out.println(xclass);
//						if (xclass instanceof String) {
//							setMethod.invoke(tObject, cell.getStringCellValue());
//						} else if (xclass instanceof Date) {
//							setMethod.invoke(tObject, sf.parse(cell.getStringCellValue()));
//						} else if (xclass instanceof Boolean) {
//							String boolname = "是";
//							if (cell.getStringCellValue().equals("否")) {
//								boolname = "否";
//							}
//							setMethod.invoke(tObject, boolname);
//						}
//					}
//					// 下一列
//					k = k + 1;
//				}
//				dist.add(tObject);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//		return dist;
//	}
//	
//	
	 /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(InputStream is) {
    	String[] title = null;
    	try {
    		Workbook book = null;
    		try {
	            book = new XSSFWorkbook(is);
	        } catch (Exception ex) {
	            book = new HSSFWorkbook(is);
	        }
            Sheet sheet = book.getSheetAt(0);
            Row row = sheet.getRow(0);
            // 标题总列数
            int colNum = row.getPhysicalNumberOfCells();
            title = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                title[i] = getCellFormatValue(row.getCell((short) i));
            }
            //根据类型判断是否是正常模板
        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }
	
    public String getValue(Cell cell) {  
        String value = "";  
        if(null==cell){  
            return value;  
        } 
        try {
        	switch (cell.getCellType()) {  
            //数值型  
            case Cell.CELL_TYPE_NUMERIC:  
                if (HSSFDateUtil.isCellDateFormatted(cell)) {  
                    //如果是date类型则 ，获取该cell的date值  
                    Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());  
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
                    value = format.format(date);;  
                }else {// 纯数字  
                    BigDecimal big=new BigDecimal(cell.getNumericCellValue());  
                    value = big.toString();  
                    //解决1234.0  去掉后面的.0  
//                    if(null!=value&&!"".equals(value.trim())){  
//                         String[] item = value.split("[.]");  
//                         if(1<item.length&&"0".equals(item[1])){  
//                             value=item[0];  
//                         }  
//                    }  
                    if(value.contains(".")){
                    	  Double valueDouble = new Double(value);
                          DecimalFormat  df  = new DecimalFormat("######0.00"); 
                          value = df.format(valueDouble); 
                    }
                }  
                break;  
                //字符串类型   
            case Cell.CELL_TYPE_STRING:  
                value = cell.getStringCellValue().toString();  
                break;  
            // 公式类型  
            case Cell.CELL_TYPE_FORMULA:  
                //读公式计算值  
                value = String.valueOf(cell.getNumericCellValue());  
                if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串  
                    value = cell.getStringCellValue().toString();  
                }  
                break;  
            // 布尔类型  
            case Cell.CELL_TYPE_BOOLEAN:  
                value = " "+ cell.getBooleanCellValue();  
                break;  
            // 空值  
            case Cell.CELL_TYPE_BLANK:   
                value = "";  
//                LogUtil.getLogger().error("excel出现空值");  
                break;  
            // 故障  
            case Cell.CELL_TYPE_ERROR:   
                value = "";  
//                LogUtil.getLogger().error("excel出现故障");  
                break;  
            default:  
                value = cell.getStringCellValue().toString();  
	        }  
	        if("null".endsWith(value.trim())){  
	            value="";  
	        }  
		} catch (Exception e) {
			value="";  
		}
        return value;  
    }  

    
    
	/**
	 * @Title: getCellValue 
	 * @Description: 获得单元信息
	 * @param @param c
	 * @param @return
	 * @return String
	 * @throws
	 */
	public String getCellValue(Cell c) {
		String o = null;
		if(c!=null){
			c.setCellType(HSSFCell.CELL_TYPE_STRING);
			switch (c.getCellType()) {
				case HSSFCell.CELL_TYPE_BLANK:
					o = "";
					break;
				case HSSFCell.CELL_TYPE_BOOLEAN:
					o = String.valueOf(c.getBooleanCellValue());
					break;
				case HSSFCell.CELL_TYPE_FORMULA:
					o = String.valueOf(c.getCellFormula());
					break;
				case HSSFCell.CELL_TYPE_NUMERIC:
					o = String.valueOf(c.getNumericCellValue());
					break;
				case HSSFCell.CELL_TYPE_STRING:
					o = c.getStringCellValue();
					break;
				default:
					o = null;
					break;
			}
			if(o != null){
				return o;
			}
		}
		return "";
	}

	
	/**
     * 获取单元格数据内容为字符串类型的数据
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     * @param cell  Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }
	
    
}