<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>
<body>
	<div style="text-align:center;margin-top:20px">
		<form id="import_data_form" action="dataKeep/import" method="post" enctype="multipart/form-data">
			<span style="float:left;text-indent:2em;margin-bottom:10px">请选择文件(支持xml、xls、xlsx格式):</span>
			<input type="hidden" id="table_code" name="tableCode" />
			<input name="file" class="easyui-filebox" style="width:80%;clear:both" data-options="required:true,prompt:'请选择上传的文件',buttonText:' 浏 览 '">
		</form>
		<div style="text-indent:2em;float:left;margin-top:135px">导入模板：
			1、<a href="${pageContext.request.contextPath}/upload/custom-user/template/用户属性导入模板.xml" class="template">用户属性导入模板.xml</a>
			2、<a href="${pageContext.request.contextPath}/upload/custom-user/template/用户属性导入模板.xlsx" class="template">用户属性导入模板.xlsx</a>
		</div>
	</div>
	<script>
		$(function(){
			seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
				dk.importData();
			});			
		})
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>