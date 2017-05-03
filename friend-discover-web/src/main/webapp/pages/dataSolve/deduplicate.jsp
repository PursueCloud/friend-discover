<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dd_dialog_win"></div>
    <div id="dd_dialog_win2"></div>
    <div id="coep_cont" style="padding:20px 30px;">
        <form action="dataSolve/deduplicate" method="get" id="deduplicate_form">
            <table>
                <tr><td><br/></td></tr>
                <tr>
                    <td>输入路径(hdfs)：</td>
                    <td>
                        <input id="deduplicate_input_box" name="input" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_source/source_users.xml', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', required:true"/>
                    </td>
                </tr>
                <tr><td><br/><br/></td></tr>
                <tr>
                    <td>输出路径(hdfs)：</td>
                    <td>
                        <input id="output_box" name="output" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/deduplicate', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataSolve/dataSolve.js' ], function(ds) {
                ds.deduplicate();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>