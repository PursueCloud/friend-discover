<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="bhf_dialog_win"></div>
    <div id="bhf_dialog_win2"></div>
    <table id="hdfs_dirs_grid"></table>
    <div id="new_dir_tools" style="display:none;position:absolute;left:120px;bottom:18px;z-index: 99999;">
        <a id="save_new_dir" class="easyui-linkbutton" style="width:26px;height:16px;padding:0;border:none;" iconCls="icon-save" plain="true" title="保存"></a>
        <a id="cancel_new_dir" class="easyui-linkbutton" style="width:26px;height:16px;padding:0;border:none;" iconCls="icon-cancel" plain="true" title="取消"></a>
    </div>

    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(hd) {
                hd.browserHDFSFilesOrDirs();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>