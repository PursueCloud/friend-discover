package com.yo.friendis.common.common.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSON;
import com.yo.friendis.common.common.bean.OperaterException;
import com.yo.friendis.common.common.bean.OperaterResult;
import com.yo.friendis.common.common.util.StringPool;

@Service
public class UploadService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 上传文件并保存至本地
	 * 
	 * @param name
	 *            文件名
	 * @param fileName
	 *            文件上传表单名
	 * @param file
	 *            文件流
	 * @param serverPath
	 *            服务器地址
	 * @param localPath
	 *            本地地址
	 * @return
	 */
	public OperaterResult<Object> uploadLocal(CommonsMultipartFile file,String serverPath, String localPath) {
		// 记录上传过程起始时的时间，用来计算上传时间
		long startTime = System.currentTimeMillis();
		
		String originalFileName = file.getOriginalFilename();
		String date = new SimpleDateFormat(StringPool.PATTERN_DATE2).format(new Date());
		//文件限定路径字符串
		String limitString = "upload" + File.separator + date + File.separator;
        
        //上传路径
        String uploadPath = _getLocalPath(localPath,limitString);

        // 如果目录不存在则创建
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
		
		try {
			Map<String, String> resultMap = new HashMap<String, String>();
			String newFileName = _getNewFileName(originalFileName);
			resultMap.put("name", newFileName);
			
			//请求路径限定字符串
            limitString = "upload/" + date + "/";
			resultMap.put("path", limitString + newFileName);
			file.transferTo(new File(uploadPath,newFileName));

			return new OperaterResult<Object>(true, "文件上传成功", resultMap);
		} catch (IOException e) {
			logger.error(e.getMessage());

			throw new OperaterException(false, "101", "文件上传失败（文件在服务器存储发生错误）");
		} finally {
			// 记录上传该文件后的时间
			long endTime = System.currentTimeMillis();
			logger.info("上传文件名 : {}, 耗时 : {}", originalFileName, endTime - startTime);
		}
	}

	
	/**
	 * 上传文件至文件服务器
	 * 
	 * @param file
	 * @param serverPath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OperaterResult<Object> upload(CommonsMultipartFile file, String serverPath) {
		
		// 记录上传过程起始时的时间，用来计算上传时间
		long startTime = System.currentTimeMillis();

		serverPath = serverPath.substring(0, serverPath.length()-1);
		
		//HttpClient文件上传
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String fileName = file.getOriginalFilename();
		HttpPost httpPost = new HttpPost(serverPath);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		try {
			builder.addBinaryBody("file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);// 执行提交
			HttpEntity responseEntity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 将响应内容转换为字符串
				String respString = EntityUtils.toString(responseEntity);
				if (!StringUtils.isEmpty(respString)) {
					return JSON.parseObject(respString, OperaterResult.class);
				}
			}
			throw new OperaterException(false, "101", "文件上传失败（文件服务器未正确返回数据）");
		} catch (IOException e) {
			logger.error(e.getMessage());

			throw new OperaterException(false, "108", "文件上传失败（上传文件至文件服务器错误）");
		} finally {
			// 记录上传该文件后的时间
			long endTime = System.currentTimeMillis();
			logger.info("上传文件名 : {}, 耗时 : {}", fileName, endTime - startTime);
		}

	}

	/**
	 * 获取本地文件存储地址
	 * 
	 * @param localPath
	 * @param limitString
	 *            限定名
	 * @return
	 */
	private String _getLocalPath(String localPath,String limitString){
		localPath = localPath.substring(0, localPath.length() - 1);
		int end = localPath.lastIndexOf(File.separator);
		end = end >= 0 ? end : localPath.length();
		localPath = localPath.substring(0, end) + File.separator + limitString;
		return localPath;
	}
	
	
	/**
	 * 获取新文件名
	 * 
	 * @return
	 */
	private String _getNewFileName(String originalFileName) {
		int start = originalFileName.lastIndexOf(".");
		String suffix = originalFileName.substring(start);
		String newFileName = UUID.randomUUID().toString().replace("-", "");
		return newFileName + suffix;
	}

}
