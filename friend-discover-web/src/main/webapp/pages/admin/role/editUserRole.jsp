<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body class="easyui-layout">
	<input type="hidden" name="role" value="${role.roleId}">
	<table id="userRoleGrid"></table>
	<div id="userRoleGridBar">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
					<label>筛选：</label>
					<select class="easyui-combobox" style="padding-right:6px" id="thisRoleUserStatus" data-options="panelHeight:'80px', editable:false">
						<option value="all">全部用户</option>
						<option value="assigned">已分配用户</option>
						<option value="unassigned">可分配用户</option>
					</select>
					<input id="userRoleSearch" class="js-searchbox" style="width:150px;" data-options="prompt:'搜索用户名'"/>
				</td>
			</tr>
		</table>
	</div>
	<script>
		$(function() {
			seajs.use([ 'module/admin/role.js' ], function(role) {
				role.editUserRole();
			});	
		});
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>