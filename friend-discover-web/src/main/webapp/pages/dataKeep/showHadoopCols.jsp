<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body style="text-align: center;">
	<div style="padding: 50px 40px; margin: auto;">
		<label><input type="checkbox" id="all_check"/>全选/全不选</label>
		<label><input type="checkbox" id="default_check"/>默认</label><br/><br/>
		
		<label><input type="checkbox" name="col" value="0" field="constKey" />配置项名</label>
		<label><input type="checkbox" name="col" value="1" field="constValue" />配置项值</label>
		<label><input type="checkbox" name="col" value="2" field="description" />备注说明</label>
		<label><input type="checkbox" name="col" value="3" field="createDate" />创建时间</label>
		<label><input type="checkbox" name="col" value="4" field="modifyDate" />修改时间</label>
	</div>
	<div style="margin: auto;width:150px;">共<span id="total_cols" style="color:orangered;">5</span>列，选中了：<span class="col-cnt" style="color:orangered;"></span>列</div>
	<script type="text/javascript">
		$(function() {
			seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
				dk.showOrdinaryCols();
			});		
		});
	</script>
</body>

<%@ include file="/pages/include/ft.jsp"%>