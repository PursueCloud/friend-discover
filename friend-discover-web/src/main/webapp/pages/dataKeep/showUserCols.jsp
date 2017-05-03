<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body style="text-align: center;">
	<div style="padding: 50px 40px; margin: auto;">
		<label><input type="checkbox" id="all_check"/>全选/全不选</label>
		<label><input type="checkbox" id="default_check"/>默认</label><br/><br/>
		
		<label><input type="checkbox" name="col" value="0" field="displayName" />昵称</label>
		<label><input type="checkbox" name="col" value="1" field="aboutMe" />个人简介</label>
		<label><input type="checkbox" name="col" value="2" field="icon" />头像</label>
		<label><input type="checkbox" name="col" value="3" field="emailHash" />邮箱</label>
		<label><input type="checkbox" name="col" value="4" field="sex" />性别</label><br/>
		<label><input type="checkbox" name="col" value="5" field="age" />年龄</label>
		<label><input type="checkbox" name="col" value="6" field="location" />长驻地</label>
		<label><input type="checkbox" name="col" value="7" field="websiteUrl" />个人主页</label>
		<label><input type="checkbox" name="col" value="8" field="upVotes" />赞同数</label><br/>
		<label><input type="checkbox" name="col" value="9" field="downVotes" />反对数</label>
		<label><input type="checkbox" name="col" value="10" field="reputation" />声望值</label>
		<label><input type="checkbox" name="col" value="11" field="views" />浏览数</label>
		<label><input type="checkbox" name="col" value="12" field="createDate" />创建时间</label><br/>
		<label><input type="checkbox" name="col" value="13" field="modifyDate" />修改时间</label>
		<label><input type="checkbox" name="col" value="14" field="lastAccessDate" />最后一次访问时间</label><br/>
	</div>
	<div style="margin: auto;width:150px;">共<span id="total_cols" style="color:orangered;">15</span>列，选中了：<span class="col-cnt" style="color:orangered;"></span>列</div>
	<script type="text/javascript">
		$(function() {
			seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
				dk.showOrdinaryCols();
			});		
		});
	</script>
</body>

<%@ include file="/pages/include/ft.jsp"%>