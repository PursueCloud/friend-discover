<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="dc_chart_cont" style="padding:10px 0 0;">
        <%--<div id="dc_chart" style="width:405px;height:405px;margin:auto;"></div>--%>
        <img id="dc_chart_img" />
    </div>
    <div class="progress-div" style="margin:150px auto;text-align:center;width:250px;">
        当前进度：<div id="prog_bar" class="easyui-progressbar" data-options="value:10"></div>正在生成决策图。。。
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.showDecisChart();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>