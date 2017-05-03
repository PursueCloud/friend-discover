<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="bestDC_cont" style="padding: 40px 35px">
        <form action="dataCalc/findBestDC" method="post" id="bestDC_form">
            <table>
                <tr class="bestDC-input-path-tr">
                    <td>输入路径(HDFS)：</td>
                    <td class="bestDC-input-path-td">
                        <input id="bestDC_input_path" name="inputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/caldistance', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>输入记录数：</td>
                    <td>
                        <input id="input_recd_num" name="inputRecordNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入输入记录数', precision:0, min:0, value:0, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>阈值百分比(%)：</td>
                    <td>
                        <input id="threshold_percent_numbox" name="thresholdPercent" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入阈值百分比', precision:0, min:1, value:2, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.bestDCCalc();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>