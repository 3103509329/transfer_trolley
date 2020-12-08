package com.zhcx.netcar.netcarservice.utils.excel;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


/**
 * @ClassName：FileHandeUtil
 * @Description: 文件操作类
 * @author：lyl
 * @date：2016年6月23日 上午10:07:25 
 * @version
 */
public class FileHandeUtil {
	
	
	/**
	 * 下载excel到客户端
	 * @param xlsname
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	public void  downloadExcleTemplate(String xlsname, HttpServletResponse response, HttpServletRequest request) throws Exception{
		response.setCharacterEncoding("UTF-8");    
	    String filepath = this.getClass().getClassLoader().getResource("").getPath()+"exceltemplate/"+xlsname+".xlsx";
	    if (filepath != null) {
	        OutputStream os = null;    
	        FileInputStream fis = null;    
	       try {    
	           String file = filepath;    
	           if (!(new File(file)).exists()) {    
	               return;    
	            }    
	            String filename = file.substring(file.lastIndexOf("/")+1);
	            os = response.getOutputStream();    
	            response.setHeader("content-disposition", "attachment;filename=" + new String(filename.getBytes("GBK"), "ISO-8859-1"));      
	            response.setContentType("application/octet-stream");//八进制流 与文件类型无关  
	            byte temp[] = new byte[1024];    
	            fis = new FileInputStream(file);    
	            int n = 0;    
	            while ((n = fis.read(temp)) != -1) {    
	                os.write(temp, 0, n);    
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
	 * @param xlsname
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	public static void downloadexcle(String xlsname, String fName, HttpServletResponse response, HttpServletRequest request) throws Exception{
		response.setCharacterEncoding("UTF-8");    
	    request.setCharacterEncoding("UTF-8");
//	    if(CommonUtils.isMessyCode(xlsname)){
//	    	xlsname = new String(xlsname.getBytes("iso-8859-1"),"utf-8");
//	    }
	    String filepath = new String(xlsname);
	    if (filepath != null) {
	        OutputStream os = null;    
	        FileInputStream fis = null;    
	       try {    
	            String file = filepath;  
	            if (!(new File(file)).exists()) {    
	               return;    
	            }    
	            String filename = file.substring(file.lastIndexOf("/")+1);
	            String hz = file.substring(file.lastIndexOf("."),file.length());
//	            if(StringUtils.isNotBlank(fName) && CommonUtils.isMessyCode(fName)){
//	            	filename = new String(fName.getBytes("iso-8859-1"),"utf-8")+hz;
//	        	}
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
	 * @param xlsname
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	public static void downloadexcle2(String filePath, HttpServletResponse response, HttpServletRequest request) throws Exception{
		response.setCharacterEncoding("UTF-8");    
	    request.setCharacterEncoding("UTF-8");
//	    if(CommonUtils.isMessyCode(filePath)){
//	    	filePath = new String(filePath.getBytes("iso-8859-1"),"utf-8");
//	    }
	    String filepath = new String(filePath);
	    if (filepath != null) {
	        OutputStream os = null;    
	        FileInputStream fis = null;    
	       try {    
	            String file = filepath.replaceAll("\\\\", "/");  
	            if (!(new File(file)).exists()) {    
	               return;    
	            }    
	            String filename = file.substring(file.lastIndexOf("/")+1,file.length());
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
	 * @Title: getZipFileNames 
	 * @Description: 获取zip中的文件名
	 * @param @param path
	 * @param @return
	 * @param @throws Exception
	 * @return List<String>
	 * @author lyl
	 * @throws
	 */
	private static final String CHINESE_CHARSET = "GBK";
	
	 
     
     /**
       * @Title: validateImageNameError  验证图片名称是否符合格式
       * @Description: TODO 验证图片名称是否符合格式
       * @param @return
       * @return boolean
       * @throws
       * @author lyl
       * @date 2016年12月22日 下午10:04:34
      */
     private static boolean validateImageNameError(String fname,String express[]){
    	 boolean flag = false;
         Pattern pattern = null;
         Matcher matcher = null;
         if(null != express && express.length > 0){
        	 for (String expr : express) {
        		 pattern = Pattern.compile(expr);
         		 matcher = pattern.matcher(fname);
         		 if (matcher.matches()){
         			flag = true;
         			break;
         		 }
			 }
         }
    	 return flag;
     }
     public static File createDirectory(String directory, String subDirectory){
	        String[] dir;
	        File file = new File(directory);
	        File temfile = null;
	        try{
	            if ((subDirectory.length() == 0) && (!file.exists())){
	                file.mkdirs();
	            }else if (subDirectory.length() != 0){
		            dir = subDirectory.replace("//", "/").split("/");
		            String temstr = directory;
		            for(int i = 0; i < dir.length; i++){
		               temstr += "/" + dir[i]; 
		               temfile = new File(temstr);
		               if(!temfile.exists()){
		                boolean bl = temfile.mkdir();
		               }
		               else{
		               }
		            }
	            }
	        }catch (Exception e){
	        }
	        return temfile;
	 }
	
	
	public static void readZipFile(String file) throws Exception { 
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file)); 
		ZipInputStream zin = new ZipInputStream(in); 
		ZipEntry ze;
		while ((ze = (ZipEntry) zin.getNextEntry()) != null) { 
		if (ze.isDirectory()) {
		}else { 
			long size = ze.getSize(); 
			if (size > 0) { 
				BufferedReader br = new BufferedReader( 
				new InputStreamReader(zf.getInputStream(ze))); 
				String line; 
			while ((line = br.readLine()) != null) { 
			} 
				br.close(); 
			} 
		} 
	  } 
	  zin.closeEntry(); 
	}
	
	
	/**
	 * 下载文件
	 * @param response
	 * @param filePath  文件路径
	 * @param file   文件
	 * @throws IOException
	 */
	public static void downLoadFile(HttpServletResponse response, String filePath, File file)  throws IOException {
	    String fileName = file.getName();
	    //下载文件
	    exportFile(response, filePath, fileName);
	    //删除单个文件
	    deleteFile(filePath, fileName);
	}
	
	/**
     * 下载文件
     * @param response
     * @param csvFilePath
     *              文件路径
     * @param fileName
     *              文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName) throws IOException {
        response.setContentType("application/csv;charset=GBK");
        response.setHeader("Content-Disposition",
            "attachment;  filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
        //URLEncoder.encode(fileName, "GBK")
        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("GBK");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
 
    /**
     * 删除该目录filePath下的所有文件
     * @param filePath
     *            文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
        }
    }
 
    /**
     * 删除单个文件
     * @param filePath
     *         文件目录路径
     * @param fileName
     *         文件名称
     */
    public static void deleteFile(String filePath, String fileName) {
        File file = new File(filePath);
        if (file.exists()) {
        	file.delete();
            return;
        }
    }
    
    
   /** 
    * 复制单个文件 
    * @param oldPath String 原文件路径 如：c:/fqf.txt 
    * @param newPath String 复制后路径 如：f:/fqf.txt 
    * @return boolean 
    */ 
    public static void copyFile(String oldPath, String newPath) { 
	    try { 
	    	int bytesum = 0; 
	    	int byteread = 0; 
	    	File oldfile = new File(oldPath); 
	    	if (oldfile.exists()) { //文件存在时 
	    		InputStream inStream = new FileInputStream(oldPath); //读入原文件 
	    		FileOutputStream fs = new FileOutputStream(newPath); 
	    		byte[] buffer = new byte[1444]; 
	    		int length; 
	    		while ( (byteread = inStream.read(buffer)) != -1) { 
	    			bytesum += byteread; //字节数 文件大小 
	    			fs.write(buffer, 0, byteread); 
	    		} 
	    		inStream.close(); 
	    	} 
	    } 
	    catch (Exception e) { 
	    	e.printStackTrace(); 
	    } 
    } 

    /** 
    * 复制整个文件夹内容 
    * @param oldPath String 原文件路径 如：c:/fqf 
    * @param newPath String 复制后路径 如：f:/fqf/ff 
    * @return boolean 
    */ 
	public void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	
	public static void test(){
		boolean flag = true;
		int i = 10;
		label:
		while(flag){
			i--;
			if(i % 2 == 0){
				continue label;
			}
			if(i <= 0){
				break;
			}
		}
	}
	
//	public static void main(String[] args) {
//		String fileName = "file";
//		String suffix = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length()).toLowerCase();
//		String name = fileName.substring(0,fileName.lastIndexOf("."));
//	}
}
