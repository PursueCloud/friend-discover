<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
	<table id="userGrid"></table>
	<div id="userEditWindow"></div>
	<div id="userGridBar">
		<table cellpadding="0" cellspacing="0" style="width:100%">
			<tr>
				<td style="padding-left:2px">
					<a href="#" class="easyui-linkbutton btn-add" iconCls="icon-add" plain="true">新增</a>
					<a href="#" class="easyui-linkbutton btn-edit" iconCls="icon-edit" plain="true">编辑</a>
					<a href="#" class="easyui-linkbutton btn-del" iconCls="icon-remove" plain="true">删除</a>
					<a href="#" class="easyui-linkbutton btn-editPas" iconCls="icon-edit" plain="true">重置密码</a>
				</td>				
				<td style="text-align:right;padding-right:2px">
					<input class="js-searchbox" style="width:150px" data-options="prompt:'搜索用户'"/>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
	$(function() {
		seajs.use([ 'module/admin/user.js' ], function(user) {
			user.init();
		});		
	});
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>