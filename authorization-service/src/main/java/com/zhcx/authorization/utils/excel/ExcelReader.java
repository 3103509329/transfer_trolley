package com.zhcx.authorization.utils.excel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.zhcx.authorization.utils.MessageResp;

/**
 * 
 * @ClassName：ExcelReader
 * @Description: TODO(操作Excel表格的功能类)
 * @author：Goo
 * @date：2015-11-11 上午9:49:11
 * @version
 */
@SuppressWarnings({ "unused", "deprecation" })
public class ExcelReader {

	private POIFSFileSystem fs;
	private Workbook wb;
	private Sheet sheet;
	private Row row;
	
	Logger log = Logger.getLogger(ExcelReader.class);
	
	/**
	 * @Title: validateImportExcel 
	 * @Description: 验证导入excle内容
	 * @param @param file
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static MessageResp validateImportExcel(MultipartFile file){
		MessageResp resp = new MessageResp();
		if(file == null ){
			resp.setResultDesc("请选择文件。。。。。");
			resp.setResult("false");
			resp.setStatusCode("-50");
			return resp;
		}
		if(StringUtils.isBlank(file.getName())){
			resp.setResultDesc("批量导入时文件不能为空");
			resp.setStatusCode("-50");
			resp.setResult("false");
			return resp;
		}
		
		/*CommonsMultipartFile cf = (CommonsMultipartFile) file;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		File userFile = fi.getStoreLocation();
		if(!FileHandeUtil.validFileSize(userFile)){
			resp.setResultDesc("文件内容格式不正确,请核查");
			resp.setStatusCode("1004");
			resp.setResult("false");
			return resp;
		}*/
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
	 * 
	  * @Title: validateExcelHeader
	  * @Description: 验证标题头的有效性
	  * @param @return
	  * @return MessageResp
	  * @throws
	  * @author lyl
	  * @date 2016年7月4日 上午9:22:39
	 */
	public static MessageResp validateExcelHeader(String xlsname,MultipartFile ufile){
		MessageResp messageResp = new MessageResp();
		messageResp.setResult("true");
		//模板文件
		String msg = "";
		String filepath = ExcelReader.class.getClassLoader().getResource("").getPath()+"exceltemplate/"+xlsname+".xlsx";
		File templateF = new File(filepath);
		if (!templateF.exists()) { 
        	messageResp.setResult("false");
        	messageResp.setResultDesc("模板文件不存在");
    	    return messageResp;
        }   
        ExcelReader excelReader = new ExcelReader();
        FileInputStream in = null;
		try {
			in = new FileInputStream(filepath);
	        String[] templateHeader = excelReader.readExcelTitle(in);
//	        //上传上来的文件的标题头
	        String[] uploadHeader = excelReader.readExcelTitle(ufile.getInputStream());
			//模板标题头
//	        String[] templateHeader = excelReader.readExcelTitle(ufile.getInputStream());
//	        String[] templateHeader = excelReader.readExcelTitle4Fac(templateF);
	        //	        用户传入的excel
	      
//	        CommonsMultipartFile cf = (CommonsMultipartFile) ufile;
//			DiskFileItem fi = (DiskFileItem) cf.getFileItem();
//			File userFile = fi.getStoreLocation();
			
//			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(ufile.getBytes());
//	        String[] uploadHeader = excelReader.readExcelTitle4Byte(byteInputStream);
	        
	        
//	        String[] uploadHeader = excelReader.readExcelTitle4SXSSF(ufile.getInputStream());
	        
	        msg += "[Excel中不存在";
	        String head = "";
	        int count = 0 ; 
	        
		    for (int i = 0; i < uploadHeader.length; i++) {
			  head = uploadHeader[i];
			  for (int j = 0; j < templateHeader.length; j++) {
				 if(head.equals(templateHeader[j])){
					 break;
				 }else{
					 if(j == templateHeader.length-1){
						msg +="“"+head+"”列   ";
						count ++ ;
					 } 
				 }
			   }
		    }
	        msg +="]";
	        if(count > 0){
	        	messageResp.setResult("false");
	        	messageResp.setResultDesc(msg);
	        }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return messageResp;
	}
	
	
	
	
	 /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public String[] readExcelTitle(Row row) {
    	String[] title = null;
         // 标题总列数
         int colNum = row.getPhysicalNumberOfCells();
         title = new String[colNum];
         for (int i = 0; i < colNum; i++) {
             title[i] = getCellFormatValue(row.getCell((short) i)).trim();
         }
        return title;
    }
    
	 /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public List<String> readExcelTitle2List(Row row,Integer index) {
//    	String[] title = null;
    	 List<String> title = new ArrayList<String>();
         // 标题总列数
         int colNum = row.getPhysicalNumberOfCells();
//         title = new String[colNum];
         if(null == index){
        	 index = 0;
         }
         for (int i = index; i < colNum; i++) {
            title.add(getCellFormatValue(row.getCell((short) i)).trim());
         }
        return title;
    }
    
    
	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @param InputStream
	 * @return String 表头内容的数组
	 * @throws IOException 
	 */
	public String[] readExcelTitle(InputStream is) throws IOException {
		Long start = System.currentTimeMillis();
		try {
			wb = new XSSFWorkbook(is);
        } catch (Exception ex) {
			wb = new HSSFWorkbook(is);
        }
		Long end =  System.currentTimeMillis();
		log.debug(" readExcelTitle ....耗时 ： " + (end-start)+"mm");
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}
	
	
	
	
	public String[] readExcelTitle4Fac(File file) throws IOException {
		Long start = System.currentTimeMillis();
		try {
//			wb = new XSSFWorkbook(is);
			wb = WorkbookFactory.create(file);
	    } catch (Exception ex) {
			wb = new HSSFWorkbook(new FileInputStream(file));
	    	log.debug(ex.getMessage(),ex);
	    }
		Long end =  System.currentTimeMillis();
		log.debug("readExcelTitle ....耗时 ： " + (end-start)+"mm");
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}
	
	
	public String[] readExcelTitle4Byte(ByteArrayInputStream  byteInputStream) throws IOException {
		Long start = System.currentTimeMillis();
		try {
			wb = new XSSFWorkbook(byteInputStream);
	    } catch (Exception ex) {
			wb = new HSSFWorkbook(byteInputStream);
	    	log.debug(ex.getMessage(),ex);
	    }
		Long end =  System.currentTimeMillis();
		log.debug("readExcelTitle ....耗时 ： " + (end-start)+"mm");
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}
	
	
	public String[] readExcelTitle4SXSSF(InputStream is) throws IOException {
		Long start = System.currentTimeMillis();
		try {
			SXSSFWorkbook  wb = new SXSSFWorkbook();
//			wb.setCompressTempFiles(true); // temp files will be gzipped
//			wb.cloneSheet(1);
	    } catch (Exception ex) {
			wb = new HSSFWorkbook(is);
	    	log.debug(ex.getMessage(),ex);
	    }
		Long end =  System.currentTimeMillis();
		log.debug("readExcelTitle ....耗时 ： " + (end-start)+"mm");
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}
	
	
	public String[] readExcelTitle4Fac(File file,Workbook wb) throws IOException {
		Long start = System.currentTimeMillis();
		/*try {
//			wb = new XSSFWorkbook(is);
			wb = WorkbookFactory.create(file);
	    } catch (Exception ex) {
			wb = new HSSFWorkbook(new FileInputStream(file));
	    	log.debug(ex.getMessage(),ex);
	    }*/
		this.wb =  wb;
		Long end =  System.currentTimeMillis();
		log.debug("readExcelTitle ....耗时 ： " + (end-start)+"mm");
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}
	
	
	public Workbook getWorkbook(File file) throws IOException {
		Long start = System.currentTimeMillis();
		try {
//			wb = new XSSFWorkbook(is);
			wb = WorkbookFactory.create(file);
	    } catch (Exception ex) {
			wb = new HSSFWorkbook(new FileInputStream(file));
	    	log.debug(ex.getMessage(),ex);
	    }
		return wb;
	}
	
	
	 /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
   /* public String[] readExcelTitle(InputStream is) {
    	String[] title = null;
    	try {
    		Workbook book = null;
    		log.error("poi ....");
    		Long start = System.currentTimeMillis();
    		try {
//	            book = new XSSFWorkbook(is);
    			book = new SXSSFWorkbook(10);
    			book.cloneSheet(1);
	        } catch (Exception ex) {
	        	log.error("HSSFWorkbook");
	            book = new HSSFWorkbook(is);
	        }
    		Long end = System.currentTimeMillis();
    		log.error("poi ....耗时："+(end-start)+"mm");
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
    }*/
	

	/**
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return List<Map<String, String>> Map的key是表头值是具体内容
	 * @throws IOException 
	 */
	public List<Map<String, String>> readExcelContentRetrunList(InputStream is) throws IOException {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		try {
			wb = new XSSFWorkbook(is);
        } catch (IOException ex) {
			wb = new HSSFWorkbook(is);
        }
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			Map<String, String> map = new HashMap<String, String>();
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";

				map.put(title[j], getCellFormatValue(row.getCell((short) j)).trim().replaceAll("\t\r", ""));
				// str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
				j++;
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return List<Map<String, String>> Map的key是列Id(0代表第一列)，值是具体内容
	 * @throws IOException 
	 */
	public List<Map<Integer, String>> readExcelContentByList(InputStream is) throws IOException {
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		try {
			wb = new XSSFWorkbook(is);
        } catch (IOException ex) {
			wb = new HSSFWorkbook(is);
        }
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(1);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			Map<Integer, String> map = new HashMap<Integer, String>();
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				// 之前的无法导入，修改一种数据格式读取
				// map.put(j, getCellFormatValue(row.getCell((short) j)).trim().replaceAll("\t\r", ""));

				map.put(j, getValue((Cell) row.getCell((short) j)).trim().replaceAll("\t\r", ""));
				// str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
				j++;
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public Map<Integer, String> readExcelContent(InputStream is) {
		Map<Integer, String> content = new HashMap<Integer, String>();
		String str = "";
		try {
			// fs = new POIFSFileSystem(is);
			// wb = new HSSFWorkbook(fs);
			wb = new HSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i <= rowNum; i++) {
			row = sheet.getRow(i);
			int j = 0;
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
				j++;
			}
			content.put(i, str);
			str = "";
		}
		return content;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		if (cell == null) {
			return "";
		}
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
		return strCell;
	}

	/**
	 * 获取单元格数据内容为日期类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
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
	 * 
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

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
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

	/**
	 * 
	 * @Title: getValue
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param cell
	 * @param @return
	 * @return String
	 * @throws
	 */
	// 解决excel类型问题，获得数值
	public String getValue(Cell cell) {
		String value = "";
		if (null == cell) {
			return value;
		}
		switch (cell.getCellType()) {
		// 数值型
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 如果是date类型则 ，获取该cell的date值
				Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				value = format.format(date);
				;
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

	public static void main(String[] args) {
		try {
			// 对读取Excel表格标题测试
			InputStream is = new FileInputStream("c:\\student.xls");
			ExcelReader excelReader = new ExcelReader();
			String[] title = new String[]{};
			try {
				title = excelReader.readExcelTitle(is);
			} catch (Exception e) {
			}
			System.out.println("获得Excel表格的标题:");
			for (String s : title) {
				System.out.print(s + " ");
			}

			// 对读取Excel表格内容测试
			InputStream is2 = new FileInputStream("c:\\student.xls");
			Map<Integer, String> map = excelReader.readExcelContent(is2);
			System.out.println("获得Excel表格的内容:");
			for (int i = 1; i <= map.size(); i++) {
				System.out.println(map.get(i));
			}

		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
	}
	
	public void validateExcelHeader(String xlsname){
		
	}
}