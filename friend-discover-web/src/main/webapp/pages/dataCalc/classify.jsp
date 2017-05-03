<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="c_cont" style="text-align:center;padding:40px 0 25px;">
        <a id="exec_classify_a" class="easyui-linkbutton" iconCls="icon-sum" style="width:100px;">执行分类</a>
        <%--<a id="exp_classify_data_a" class="easyui-linkbutton" iconCls="icon-print" style="margin-left:20px;width:110px;">下载分类数据</a>--%>
        <a id="put_classify_data_into_db_a" class="easyui-linkbutton" iconCls="icon-import" style="margin-left:20px;width:110px;">分类数据入库</a><br/><br/><br/>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.classify();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>