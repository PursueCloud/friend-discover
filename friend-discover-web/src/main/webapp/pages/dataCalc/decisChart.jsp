<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="dc_cont" style="padding:40px 0 25px;text-align:center;">
        <a id="calc_decis_data_a" class="easyui-linkbutton" iconCls="icon-sum" style="width:100px;">计算</a>
        <a id="show_decision_chart_a" class="easyui-linkbutton" iconCls="icon-large-chart" style="margin-left:50px;width:110px;">显示决策图</a><br/><br/><br/>
        <img id="ok_img" src="assets/images/ok.png"/><img id="error_img" src="assets/images/error.png"/>
        <div class="progress-div" style="margin:5px auto;text-align:center;width:250px;">当前进度：<div id="prog_bar" class="easyui-progressbar" data-options="value:10"></div></div>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.decisChart();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>