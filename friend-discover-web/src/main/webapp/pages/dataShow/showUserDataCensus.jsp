<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
<div id="dialog_win"></div>
<div id="sudc_cont" style="padding:1% 5%;width:90%;height:90%;text-align: left;">
    <div style="font-weight: bold;font-size:20px;text-align: center;margin-bottom:50px;">用户统计数据</div>
    <div id="per_attr_user_chart" style="width:500px;height:500px;display:inline-block;"></div><!-- 各种属性值用户总数占比 -->
    <div id="similar_attr_user_chart" style="width:500px;height:500px;display:inline-block;"></div><!-- 属性相似的用户总数占比 -->
    <div id="per_field_user_chart" style="width:500px;height:500px;display:inline-block;"></div><!-- 各领域用户总数占比 -->
</div>
<script>
    $(function(){
        seajs.use([ 'module/dataShow/dataShow.js' ], function(ds) {
            ds.showUserDataCensus();
        });
    })
</script>
</body>

<%@ include file="/pages/include/ft.jsp"%>