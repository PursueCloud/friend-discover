<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
	<form action="admin/user/editPas"  method="post" id="userPasForm">		
		<input type="hidden" name="id" value="${user.userId}">
		<table class=formTable>
			<tr>
				<td>输入新密码：</td>
				<td><input id="firstPas" type="password" class="easyui-textbox" data-options="required:true"></td>
			</tr>
			<tr>
				<td>重复新密码：</td>
				<td><input id="newPas" name="newPas" type="password" class="easyui-textbox" data-options="required:true"></td>
			</tr>
		</table>
	</form>
	<script>
		$(function(){
			seajs.use([ 'module/admin/user.js' ], function(user) {
				//alert("我是用户编辑页面");
				user.editPas();
			});
		})
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>