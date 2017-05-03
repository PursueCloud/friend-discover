<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="init_div" style="width:600px;margin:auto;padding-top:100px;text-align:center;">
        <div>
            <select id="init_tables_select" class="easyui-combobox" style="width:200px;" data-options="prompt:'请选择表',editable:false,panelHeight:'80px', ">
                <option value="">请选择表</option>
                <option value="0">用户属性表</option>
                <option value="1">Hadoop集群配置表</option>
            </select>
            <a id="init_tab_a" class="easyui-linkbutton" style="margin-left:20px;width:150px;">创建表并插入初始数据</a>
        </div>
        <div id="prog_div" style="margin:100px auto 0;text-align:center;width:400px;display:none;">当前进度：
            <div id="init_tab_prog" class="easyui-progressbar" ></div>
        </div>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
                dk.initTable();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>