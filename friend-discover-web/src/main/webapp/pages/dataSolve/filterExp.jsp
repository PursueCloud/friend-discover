<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="fe_dialog_win"></div><!-- 用于dialog -->
    <div id="fe_dialog_win2"></div>
    <div id="fe_cont" style="padding:40px 40px;">
        <form action="dataSolve/filterExp" method="get" id="filterExp_form">
            <table>
                <tr>
                    <td>输出位置：</td>
                    <td>
                        <input type="radio" id="exp_loc_local" name="expLoc" value="0" checked/><label for="exp_loc_local">本地</label>
                        <input type="radio" id="exp_loc_hdfs" name="expLoc" value="1"/><label for="exp_loc_hdfs">HDFS</label>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr class="exp-path-tr" style="display: none;">
                    <td>输出路径：</td>
                    <td class="exp-path-td"><%--multiple:true,--%>
                        <input id="output_box" name="expPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/preparevectors', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search'"/>
                    </td>
                </tr>
                <tr class="exp-path-tr-br" style="display: none;"><td><br/></td></tr>
                <tr>
                    <td>生成文件个数：</td>
                    <td>
                        <input id="generate_file_numbox" name="generateFileNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入生成文件个数', precision:0, min:1, value:1, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>

    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataSolve/dataSolve.js' ], function(ds) {
                ds.filterExp();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>