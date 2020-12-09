package com.zhcx.authorization.controller.file;

import com.zhcx.authorization.socketIO.MessgeBean;
import com.zhcx.authorization.socketIO.SocketIOResponse;
import com.zhcx.authorization.utils.CommonUtils;
import com.zhcx.authorization.utils.FastDFSUtil;
import com.zhcx.authorization.utils.MessageResp;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/taxi/file")
public class TaxiFileController extends SocketIOResponse{
	private static final Logger logger = LoggerFactory.getLogger(TaxiFileController.class);
	//private static final String STORE_FILE_DIR="D:\\";//文件保存的路径

	@Autowired
    private FastDFSUtil fastDFSUtil;


	@RequestMapping("/receive")
	public String receive(HttpServletRequest request,HttpServletResponse response) throws Exception {
		logger.debug("--------终端上传视频文件----start");
		String fileUrl = "";
		// 判断enctype属性是否为multipart/form-data
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart)
			throw new IllegalArgumentException(
					"上传内容不是有效的multipart/form-data类型.");

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request
		List<?> items = upload.parseRequest(request);
		logger.debug("--------上传的视频文件流----"+items.size());
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				// 如果是普通表单字段
				String name = item.getFieldName();
				String value = item.getString();
				// ...
			} else {
				// 如果是文件字段
				String fieldName = item.getFieldName();
				String fileName = item.getName();
				String contentType = item.getContentType();
				boolean isInMemory = item.isInMemory();
				long sizeInBytes = item.getSize();
				/*
				String filePath=STORE_FILE_DIR+fileName;
				//写入文件到当前服务器磁盘
				File uploadedFile = new File(filePath);
				// File uploadedFile = new File("D:\haha.txt");
				item.write(uploadedFile);*/
			}
			logger.debug("--------上传的视频文件名----"+item.getFieldName());
			String  file_id = fastDFSUtil.uploadVideoFile(item.getInputStream(), item.getFieldName());
			fileUrl = file_id;
			/*
			 *	向客户端推送下载地址
			 */
			Set<String> clients = getClient(VEDIOEVENT,item.getFieldName());
			MessgeBean bean = new MessgeBean();
			bean.setContent(file_id);
			bean.setType(VEDIOEVENT);
			pushData(bean.getType(), bean, clients, "/vedio");
		}
		logger.debug("--------上传视频结束，文件地址----"+fileUrl);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println("上传成功，文件地址："+fileUrl);
		return null;
	}


	@PostMapping("/upload")
	@ApiOperation(value = "上传视频", notes = "上传视频")
	public MessageResp upload2(@RequestParam MultipartFile file, String oldUrl) {
		MessageResp result = new MessageResp();
		String fileId = null;
		int delRes = 999;
		InputStream sourceFileStream = null;
		try {
			if (null != oldUrl && !"".equals(oldUrl)) {
				delRes = fastDFSUtil.deleteFile(oldUrl);
				if (delRes != 0) {
					logger.error("原视频删除失败");
				}
			}
			sourceFileStream = file.getInputStream();
			fileId = fastDFSUtil.uploadVideoFile(sourceFileStream, file.getOriginalFilename());
			if (null == fileId || "".equals(fileId)) {
				return CommonUtils.returnErrorInfo("上传失败");
			}
			result.setData(fileId);
			result.setResult(Boolean.TRUE.toString());
			result.setResultDesc("上传成功");
		} catch (Exception e) {
			logger.error("上传失败,{}", e);
			e.printStackTrace();
			return CommonUtils.returnErrorInfo("上传失败");
		} finally {
			try {
				sourceFileStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


}
