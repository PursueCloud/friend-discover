<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
	<form action="admin/user/save"  method="post" id="userForm">		
		<input type="hidden" name="userId" value="${user.userId}">
		<table class="formTable" style="text-align:left">
			<tr>
				<td>用户账号：<td>  
				<td><input id="weaccount" name="account" type="text" value="${user.account }" class="f1 easyui-textbox " data-options="required:true,disabled:true"/><td>
			</tr>
			<tr>
				<td>用户名称：<td>
				<td><input name="userName" type="text" value="${user.userName }" class="f1 easyui-textbox" data-options="required:true"/><td>
			</tr>
			<tr>
				<td>角色：<td>
				<td><input class="easyui-combogrid" name="roleIds" style="width:450px" id="roleTypeGrid" data-options="required:true" /><td>
			</tr>
		</table>
	</form>
	<script>
		$(function(){
			seajs.use([ 'module/admin/user.js' ], function(user) {
				//alert("我是用户编辑页面");
				user.edit();
			});			
		})
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>