package com.yo.friendis.common.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.yo.friendis.common.common.service.UploadService;
import com.yo.friendis.common.common.util.WebUtil;

@Controller
@RequestMapping("file")
public class FileController extends BaseController {

	@Autowired
	UploadService uploadService;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Object upload(@RequestParam(value = "file",required = false) CommonsMultipartFile file) {
		if(file == null){
			return "";
		}
//		String storePath = WebUtil.getFileServerPath(request);
//		return uploadService.upload(file, storePath);
		return uploadService.uploadLocal(file, WebUtil.getServerFullPath(request), WebUtil.getLocalFullPath(request));
	}

}
