<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
	<div style="padding: 10px 60px 20px 60px">
		<form id="chpwd" method="post" action="admin/account/changePwd" enctype="application/x-www-form-urlencoded; charset=UTF-8">
			<table cellpadding="5">
				<tr>
					<td>原密码:</td>
					<td><input class="easyui-validatebox" type="password"
						name="oldPwd" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>新密码:</td>
					<td><input id="pwd" class="easyui-validatebox" type="password"
						name="newPwd" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>确认密码:</td>
					<td><input class="easyui-validatebox" type="password"
						name="confirPwd" required="required" validType="equals['#pwd']" /></td>
				</tr>
				<tr>
					<td></td>
					<td align="right"><a href="javascript:void(0)"
						class="easyui-linkbutton js-btn-chpwd">修改密码</a></td>
				</tr>
			</table>
		</form>
	</div>
	<script type="text/javascript">
		$(function() {
			seajs.use([ 'module/admin/config.js' ], function(config) {
				config.init();
			});
		});
	</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>