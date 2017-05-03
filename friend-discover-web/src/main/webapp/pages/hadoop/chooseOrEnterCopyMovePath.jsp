<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="cecp_dialog_win"></div>
    <div id="cecp_dialog_win2"></div>
    <div id="cecp_cont" style="padding:40px 50px;">
        <form id="hdfs_file_form" action="hadoop/copyTo" method="post" ><!-- 默认action为复制surl-->
            <table>
                <tr><td><input id="src_path" type="hidden" name="srcPaths[]" /></td></tr>
                <tr class="dst-path-tr" >
                    <td><span class="cm-op-type">复制</span>到(hdfs)：</td>
                    <td class="dst-path-td">
                        <input id="dst_path" name="dstPath" class="easyui-textbox" style="width:200px;" data-options="prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(hd) {
                hd.chooseOrEnterCopyMovePath();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>