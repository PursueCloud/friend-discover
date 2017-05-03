<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="ds_dialog_win"></div>
    <div id="ds_dialog_win2"></div>
    <div id="init_div" style="width:600px;margin:auto;padding-top:100px;text-align:center;">
        <a id="deduplicate_a" class="easyui-linkbutton" iconCls="icon-deduplicate" style="margin-left:20px;width:100px;">去重</a>
        <a id="filter_exp_a" class="easyui-linkbutton" iconCls="icon-print" style="width:100px;">过滤导出</a><!--导出到本地或hdfs-->
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataSolve/dataSolve.js' ], function(ds) {
                ds.dataSolve();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>