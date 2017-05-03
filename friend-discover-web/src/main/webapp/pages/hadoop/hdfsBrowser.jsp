<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>
<style type="text/css">
    body, div, ul, li, span, a, input, table, tr, td, tbody, thead, * {
        margin: 0;
        padding: 0;
        border: none;
    }
    body {
        color: #666;
    }
    .ul {
                        outline:none;
                          margin:0px;
                         width: 82px;
                   text-align:center;
        border: 1px solid dodgerblue;
        border-top:none;
                    list-style: none;
                       display: none;
                  position: absolute;
        z-index:999;
                    color:dodgerblue;
        border-shadow:5px;
        background-color: #FFF;
    }
    .ul li {
        padding:10px;
        color: dodgerblue;
    }
    .ul li:hover {
        cursor: pointer;
        background-color: #EEE;
    }
    .font-a {
        font-family: yunfont!important;
        speak: none;
        font-style: normal;
        font-weight: 400;
        font-variant: normal;
        text-transform: none;
        line-height: 1;
        -webkit-font-smoothing: antialiased;
        -moz-osx-font-smoothing: grayscale;
        font-size: 16px;
        text-align: center;
        line-height: 32px;
        color: #5F6F95;
    }
    .sort-by-a:before {
        line-height: 30px;
        content:'\e910';
    }
    .show-as-a:before {
        line-height: 30px;
        content:'\e911';
    }
    .files {
        padding-top:15px;
    }
    .file {
        border-top: 1px solid #DAEBFE;
        width: 100%;
    }
    .file:hover {
        background-color: #DAEBFE;
    }
    .files-title div {
        display:inline-block;
    }
    .file div {
        display:inline-block;
    }
    .file-checkbox {
        width:30px;
        text-align:center;
        padding: 10px 10px 10px 15px;
    }
    .file-checkbox input[type="checkbox"] {
        width: 14px;
        height: 14px;
        background-color: white;
        border: none;
        outline: none;
        margin: 0;
        padding: 0;
    }
    .file-name {
        width: 60%;
        padding: 10px 10px 10px 15px;
        position: relative;
    }
    .file-name:hover, .file-size:hover, .file-create-time:hover {
        cursor: pointer;
    }
    .file-size {
        width: 10%;
        padding: 10px 10px 10px 15px;
    }
    .file-create-time {
        width: 15%;
        padding: 10px 10px 10px 15px;
    }
    .files-title .file-name:hover, .files-title .file-size:hover, .files-title .file-create-time:hover  {
        background-color: #DAEBFE;
    }
    #hdfs_browser {
        /*width: 90%;*/
        height: 95%;
        padding: 20px 20px 0 30px;
    }
    #hdfs_browser .hdfs-browser-cont {
        min-width:745px;
        border: 1px solid black;
        box-shadow: black;
        border-radius: 5px;
        height: 100%;
        overflow: auto;
    }
    #hdfs_operate_btns {
        padding-left:20px;
    }
    .op-in-file {
        display: none;
        position: absolute;
        right: 10px;
        z-index: 999;

    }
    .op-in-file .download-in-file-a:hover, .op-in-file .more-operations-in-file-a:hover {
        color: dodgerblue;
        cursor: pointer;
    }
</style>
<body>
    <div id="hb_dialog_win"></div>
    <div id="hb_dialog_win2"></div>
    <%--<div id="hdfs_browser">--%>
        <%--<div class="hdfs-browser-cont">--%>
            <%--<div id="hdfs_operate_btns"><!-- hdfs操作按钮 -->--%>
                <%--<table style="width:100%;">--%>
                    <%--<tr>--%>
                        <%--<td style="text-align:left;">--%>
                            <%--<a id="upload_file_to_hdfs_a" class="easyui-linkbutton" style="width:84px;color:white;background-color:dodgerblue;" plain="true" title="点击选择文件">上传</a>--%>
                            <%--<ul class="ul upload-ul">--%>
                                <%--<li>上传文件</li>--%>
                                <%--<li>上传文件夹</li>--%>
                            <%--</ul>--%>
                            <%--<a id="mkdir_in_hdfs_a" class="easyui-linkbutton" style="width:80px;margin-left:10px;">新建文件夹</a>--%>
                            <%--<a id="download_file_from_hdfs_a" class="easyui-linkbutton" style="width:60px;margin-left:10px;">下载</a>--%>
                            <%--<a id="remove_from_hdfs_a" class="easyui-linkbutton" style="width:60px;margin-left:10px;">删除</a>--%>
                            <%--<a id="move_operations_a" class="easyui-linkbutton" style="width:84px;margin-left:10px;" iconCls="icon-more" >更多</a><!-- 包括“重命名”，“复制到”，“移动到”等操作-->--%>
                            <%--<ul class="ul more-operations-ul">--%>
                                <%--<li>重命名</li>--%>
                                <%--<li>复制到</li>--%>
                                <%--<li>移动到</li>--%>
                            <%--</ul>--%>
                        <%--</td>--%>
                        <%--<td style="text-align:right;padding: 0 20px 7px 0;">--%>
                            <%--<input id="file_key_word" class="easyui-searchbox" style="width:250px;" data-options="prompt:'搜索您的文件'"/>--%>
                            <%--<a id="sort_by_a" style="width:30px;height:30px;margin-left:10px;" title="按。。。排序"><span class="font-a sort-by-a"></span></a>--%>
                            <%--<a id="show_as_a" style="width:30px;height:30px;margin-left:10px;" title="以。。。格式排序"><span class="font-a show-as-a"></span></a>--%>
                        <%--</td>--%>
                    <%--</tr>--%>
                <%--</table>--%>
            <%--</div>--%>
            <%--<div id="hdfs_cont" style="padding:10px 0 0;width:100%;height:95%">--%>
                <%--<div class="all-files-title" style="padding-left: 24px;">全部文件</div>--%>
                <%--<div class="files" style="width:100%;height:93%">--%>
                    <%--<div class="files-title">--%>
                        <%--<div class="file-checkbox">--%>
                            <%--<input type="checkbox" id="check_all_input"/>--%>
                        <%--</div>--%>
                        <%--<div class="file-name" >文件名</div>--%>
                        <%--<div class="file-size" >大小</div>--%>
                        <%--<div class="file-create-time" >创建日期</div>--%>
                    <%--</div>--%>
                    <%--<div class="files-list">--%>
                        <%--<div class="file">--%>
                            <%--<div class="file-checkbox">--%>
                                <%--<input type="checkbox" class="check_curr"/>--%>
                            <%--</div>--%>
                            <%--<div class="file-name" title="Android">Android--%>
                                <%--<span class="op-in-file">--%>
                                    <%--<a class="download-in-file-a" style="width:30px;height:30px;" title="下载">下载</a>--%>
                                    <%--<a class="more-operations-in-file-a" style="width:30px;height:30px;margin-left:10px;" title="更多">更多</a>--%>
                                <%--</span>--%>
                            <%--</div>--%>
                            <%--<div class="file-size" title="20M">20M</div>--%>
                            <%--<div class="file-create-time" title="2017-4-8 13:55">2017-4-8 13:55</div>--%>
                        <%--</div>--%>
                        <%--<div class="file">--%>
                            <%--<div class="file-checkbox">--%>
                                <%--<input type="checkbox" class="check_curr"/>--%>
                            <%--</div>--%>
                            <%--<div class="file-name" title="经典游戏">经典游戏--%>
                                <%--<span class="op-in-file">--%>
                                    <%--<a class="download-in-file-a" style="width:50px;height:30px;font-size:9px;" title="下载">下载</a>--%>
                                    <%--<a class="more-operations-in-file-a" style="width:50px;height:30px;margin-left:10px;font-size:9px;" title="更多">更多</a>--%>
                                <%--</span>--%>
                            <%--</div>--%>
                            <%--<div class="file-size" title="500G">500G</div>--%>
                            <%--<div class="file-create-time" title="2017-4-8 15:55">2017-4-8 15:55</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                <%--</div>--%>
            <%--</div><!-- hdfs内容（以树形展示所有列举所有文件和文件夹，具体参考百度网盘（pan.baidu.com） -->--%>
        <%--</div>--%>
    <%--</div>--%>
    <div id="hdfs_dialog_win"></div>
    <table id="hdfs_files_grid"></table>
    <div id="table_toolbar">
        <table cellspacing="5" cellpadding="0" width="100%">
            <tr>
                <td style="padding-left:2px"><%--background-color:dodgerblue;color:white;--%>
                    <a id="upload_file_to_hdfs_a" class="easyui-linkbutton" class="icon-upload" style="width:84px;" title="点击选择文件">上传</a>
                    <ul class="ul upload-ul">
                        <li id="upload_file_to_hdfs_li">上传文件</li>
                        <li id="upload_folder_to_hdfs_li">上传文件夹</li>
                    </ul>
                    <a id="mkdir_in_hdfs_a" class="easyui-linkbutton" iconCls="icon-add" style="width:100px;margin-left:10px;">新建文件夹</a>
                    <a id="download_file_from_hdfs_a" class="easyui-linkbutton" class="icon-download" style="width:60px;margin-left:10px;">下载</a>
                    <a id="remove_from_hdfs_a" class="easyui-linkbutton" iconCls="icon-remove" style="width:60px;margin-left:10px;">删除</a>
                    <a id="move_operations_a" class="easyui-linkbutton" style="width:84px;margin-left:10px;" iconCls="icon-more" iconAlign="right">更多</a><!-- 包括“重命名”，“复制到”，“移动到”等操作-->
                    <ul class="ul more-operations-ul">
                        <li id="rename_file_li">重命名</li>
                        <li id="copy_to_li">复制到</li>
                        <li id="move_to_li">移动到</li>
                    </ul>
                    <a id="reload_files_a" class="easyui-linkbutton" iconCls="icon-reload" style="width:60px;margin-left:10px;">刷新</a>
                    <span id="mkdir_op_span" style="display:none;margin-left:50px;">
                        <a id="save_a" class="easyui-linkbutton" iconCls="icon-save">保存</a>
                        <a id="cancel_a" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>
                    </span>
                </td>
                <td style="text-align:right;padding-right:2px">
                    <input id="search_cont" class="easyui-searchbox" style="width:280px;" data-options="prompt:'搜索您的文件'"/>
                </td>
            </tr>
        </table>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(ha) {
                ha.hdfsBrowser();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>