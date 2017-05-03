<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="fs_dialog_win"></div><!-- 用于dialog -->
    <div id="fs_dialog_win2"></div>
    <div id="init_div" style="width:80%;margin:auto;padding-top:60px;text-align:center;">
        <div id="upload_div" style="margin-bottom:120px;">
            <div style="font-weight:bold;font-size:20px;margin:0 0 80px;text-align:left;border-bottom:1px dashed black;">上传文件（到hdfs）：</div>
            <a id="choose_or_enter_path_upload_a" class="easyui-linkbutton" style="margin-left:20px;width:150px;">上传</a>
        </div>
        <div id="download_div">
            <div style="font-weight:bold;font-size:20px;margin:0 0 80px;text-align:left;border-bottom:1px dashed black;">下载文件（从hdfs）：</div>
            <a id="choose_or_enter_path_download_a" class="easyui-linkbutton" style="margin-left:20px;width:150px;">选择要下载的文件</a>
        </div>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataSolve/dataSolve.js' ], function(ds) {
                ds.fileSolve();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>