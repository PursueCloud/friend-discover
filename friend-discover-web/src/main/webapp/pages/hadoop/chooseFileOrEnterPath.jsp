<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="cfep_dialog_win"></div>
    <div id="cfep_dialog_win2"></div>
    <div id="coep_cont" style="padding:40px 50px;">
        <form id="hdfs_file_form" action="hadoop/uploadFile2HDFSRequestUrl" method="post" enctype="multipart/form-data"><!-- 默认action为上传文件到hdfsurl-->
            <table>
                <tr class="file-choose-tr">
                    <td>待<span class="file-type">上传</span>文件：</td>
                    <td class="file-input-box-td">
                        <input id="choose_input_filebox" name="uploadFile" class="easyui-filebox" style="width:200px;" data-options="prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr class="upload-path-br-tr"><td><br/></td></tr>
                <tr class="upload-path-tr" style="display: none;">
                    <td><span class="file-type">上传</span>到(<span class="to-loc">hdfs</span>)：</td>
                    <td class="upload-path-td">
                        <input id="upload_hdfs_path" name="uploadHdfsPath" class="easyui-textbox" style="width:200px;" data-options="prompt:'请选择或输入上传后路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(hd) {
                hd.chooseFileOrEnterPath();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>