<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="ecf_cont" style="padding:20px 35px;">
        <form action="dataCalc/execClassify" method="post" id="ecf_form">
            <table>
                <tr class="classify-input-path-tr">
                    <td>输入路径(HDFS)：</td>
                    <td class="classify-input-path-td">
                        <input id="classify_input_path" name="inputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/preparevectors', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr class="classify-output-path-tr">
                    <td>输出路径(HDFS)：</td>
                    <td class="classify-output-path-td">
                        <input id="classify_output_path" name="outputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_center', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>距离阈值：</td>
                    <td>
                        <input id="distance_threshold_num" name="distanceThresholdNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入距离阈值', precision:0, min:1, value:29, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>聚类中心数：</td>
                    <td>
                        <input id="cluster_center_num" name="clusterCenterNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入聚类中心数', precision:0, min:1, value:4, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.execClassify();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>