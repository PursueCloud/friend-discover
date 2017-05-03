<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
	<form action="admin/role/save"  method="post" id="authRoleForm" enctype="multipart/form-data">		
		<input type="hidden" name="roleId" value="${role.roleId}">
		<table class="formTable" cellspacing="5">
			<tr>
				<td>角色名称 :<td>
				<td style="text-align:left;"><input name="roleName" type="text" value="${role.roleName }" class="f1 easyui-textbox"/><td>
			</tr>
			<tr>
				<td>角色描述 :<td>
				<td><input name="description" type="text" value="${role.description }" class="f1 easyui-textbox" data-options="multiline:true, height:'80px',width:'250px'"/><td>
			</tr>
		</table>
	</form>
	<script>
		seajs.use([ 'module/admin/role.js' ], function(role) {
			//alert("我是角色编辑页面");
			role.editRole();
		});
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>