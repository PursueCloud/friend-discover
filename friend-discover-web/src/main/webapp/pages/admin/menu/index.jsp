<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>
<body>
<table id="menu_grid"></table>
<div id="win"></div>
<script type="text/javascript">
	$(function(){
		seajs.use(['module/admin/menu.js' ], function(menu) {
			menu.index();
		});
	});
</script>
</body>
<%@ include file="/pages/include/ft.jsp"%>