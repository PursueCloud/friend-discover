<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>
<body>
<table id="roleGrid"></table>
<div id="editWindow"></div>
<div id="roleGridBar">
	<table cellpadding="0" cellspacing="0" style="width:100%">
		<tr>
			<td style="padding-left:2px">
				<a href="#" class="easyui-linkbutton btn-add" iconCls="icon-add" plain="true">新增</a>
				<a href="#" class="easyui-linkbutton btn-edit" iconCls="icon-edit" plain="true">编辑</a>
				<a href="#" class="easyui-linkbutton btn-del" iconCls="icon-remove" plain="true">删除</a>
				<a href="#" class="easyui-linkbutton btn-editAuth" iconCls="icon-edit" plain="true">修改权限</a>
				<a href="#" class="easyui-linkbutton btn-editUserRold" iconCls="icon-edit" plain="true">分配用户</a>
			</td>
			<td style="text-align:right;padding-right:2px">
				<input id="roleSearch" class="js-searchbox" style="width:150px" data-options="prompt:'搜索角色'"/>
			</td>
		</tr>
	</table>
</div>
<script type="text/javascript">
	$(function() {
		seajs.use([ 'module/admin/role.js' ], function(role) {
			role.index();
		});		
	});
</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>