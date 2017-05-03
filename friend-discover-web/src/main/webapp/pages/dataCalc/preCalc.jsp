<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="init_div" style="width:600px;margin:auto;padding-top:100px;text-align:center;">
        <a id="distance_calc_a" class="easyui-linkbutton" iconCls="icon-deduplicate" style="margin-left:20px;width:100px;">距离计算</a>
        <a id="bestDC_calc_a" class="easyui-linkbutton" iconCls="icon-print" style="width:100px;">最佳DC计算</a>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.preCalc();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>