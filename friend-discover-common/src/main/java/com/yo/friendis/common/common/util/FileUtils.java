package com.yo.friendis.common.common.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;

/**
 * 
 * @author jay.huang
 *
 */
public class FileUtils {
	/**
	 * 上传文件到指定路径
	 * @param file(MultipartFile类型)
	 * @param path
	 * @param fileName
	 */
	public static void uploadToLoc(MultipartFile file, String path, String fileName) {
		File targetFile = new File(path, fileName);
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
  
        //保存  
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	
	/**
	 * 下载指定路径下的文件
	 * @param path
	 * @param fileName
	 * @param response
	 * @throws IOException 
	 */
	public static void downloadFile(String path, String fileName, HttpServletResponse response) throws IOException {
        // 得到要下载的文件  
        File file = new File(path, fileName);  
        
        // 设置响应头，控制浏览器下载该文件  
        response.setHeader("content-disposition", "attachment;filename="  
                + URLEncoder.encode(fileName, "UTF-8"));  
        // 读取要下载的文件，保存到文件输入流  
        FileInputStream in = new FileInputStream(file);  
        // 创建输出流  
        OutputStream out = response.getOutputStream();  
        // 创建缓冲区  
        byte buffer[] = new byte[1024];  
        int len = 0;  
        // 循环将输入流中的内容读取到缓冲区当中  
        while ((len = in.read(buffer)) > 0) {  
            // 输出缓冲区的内容到浏览器，实现文件下载  
            out.write(buffer, 0, len);  
        }  
        // 关闭文件输入流  
        in.close();  
        // 关闭输出流  
        out.close();  
	}
	
	/**
	 * 根据指定XSSFWorkbook对象和文件名以excel形式进行下载(XLSX
	 * @param wb
	 * @param fileName
	 * @throws IOException 
	 */
	public static void downLoadExcel(XSSFWorkbook wb, String fileName, HttpServletResponse response) throws IOException {
		//创建文件名
		//设置头信息
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		//使用URLEncoder.encode方法防止出现中文文件名乱码
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		//客户端下载
		OutputStream out = response.getOutputStream();
		wb.write(out);
		//关闭流
		wb.close();
		out.close();
	}
	
	/**
	 * 根据指定HSSFWorkbook对象和文件名以excel形式进行下载(XLSX
	 * @param wb
	 * @param fileName
	 * @throws IOException 
	 */
	public static void downLoadExcel(HSSFWorkbook wb, String fileName, HttpServletResponse response) throws IOException {
		//创建文件名
		//设置头信息
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/x-download");
		//使用URLEncoder.encode方法防止出现中文文件名乱码
		response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		//客户端下载
		OutputStream out = response.getOutputStream();
		wb.write(out);
		//关闭流
		wb.close();
		out.close();
	}

	/**
	 * 获取文件大小（M）
	 * @param size
	 * @return
     */
	public static Double getFileSize(long size) {
		return Double.parseDouble(new DecimalFormat("#.00").format(size/1024.0/1024.0));
	}

	/**
	 * 获取文件大小（如果文件大于等于1M，则单位为M，否则为K）
	 * @param size
	 * @return
	 */
	public static String getFormatFileSize(long size) {
		if( size/1024.0/1024.0 >= 1 ) {
			return getFileSize(size) + "M";
		} else {
			double val = size/1024.0;
			String formatVal = new DecimalFormat("#.00").format(val);
			if( val > 1 ) {
				return formatVal + "K";
			} else {
				return "0" + formatVal + "K";
			}
		}
	}
}
