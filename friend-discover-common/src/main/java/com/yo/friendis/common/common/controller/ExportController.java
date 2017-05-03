package com.yo.friendis.common.common.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yo.friendis.common.common.service.ExportService;

@Controller
@RequestMapping("export")
public class ExportController extends BaseController {
	@Autowired
	private ExportService exportService;

	@RequestMapping("excel")
	public @ResponseBody void excel() {
		String sql = (String) request.getAttribute("sql");// 在服务器端跳转获取,为了安全问题，只能使用服务端跳转。
		logger.debug("导出文件的sql源：{}", sql);
		HSSFWorkbook wb = exportService.createExcelFromSql(sql);
		try {
			response.setContentType("application/x-download");
			response.setHeader("Content-Disposition", "attachment; filename=export" + DateFormatUtils.format(new Date(), "yyyyMMdd") + ".xls");
			wb.write(response.getOutputStream());
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			try {
				wb.close();
				response.getOutputStream().close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}

}
