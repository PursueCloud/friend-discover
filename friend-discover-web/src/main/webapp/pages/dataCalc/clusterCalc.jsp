<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="cc_cont" style="width:700px;margin:auto;padding-top:100px;text-align:center;">
        <a id="exec_cluster_a" class="easyui-linkbutton" iconCls="icon-sum" style="width:150px;">执行聚类</a>
        <a id="show_decision_chart_a" class="easyui-linkbutton" iconCls="icon-large-chart" style="margin-left:20px;width:150px;">决策图</a>
        <a id="find_cluster_center_a" class="easyui-linkbutton" iconCls="icon-tip" style="margin-left:20px;width:150px;">寻找聚类中心</a>
        <a id="classify_a" class="easyui-linkbutton" iconCls="icon-large-shapes" style="margin-left:20px;width:150px;">分类</a>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.clusterCalc();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>