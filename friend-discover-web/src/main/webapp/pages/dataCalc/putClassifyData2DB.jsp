<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="pcf_cont" style="padding:20px 35px;">
        <form action="dataCalc/putClassifyData2DB" method="post" id="pcf_form">
            <table>
                <tr><td><br/></td></tr>
                <tr class="classify-data-input-path-tr">
                    <td>输入路径(HDFS)：</td>
                    <td class="classify-data-input-path-td">
                        <input id="classify_data_input_path" name="inputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_center', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.putClassifyData2DB();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>