<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="fcc_cont" style="padding:20px 35px;">
        <form action="dataCalc/findClusterCenter" method="post" id="fcc_form">
            <table>
                <tr>
                    <td>局部密度阈值：</td>
                    <td>
                        <input id="part_consistency_threshold_num" name="partConsistencyThresholdNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入局部密度阈值', precision:0, min:1, value:50, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>最小距离阈值：</td>
                    <td>
                        <input id="min_distance_threshold_num" name="minDistanceThresholdNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入最小距离阈值', precision:0, min:1, value:50, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr class="cluster-cener-input-path-tr">
                    <td>输入路径(HDFS)：</td>
                    <td class="cluster-cener-input-path-td">
                        <input id="cluster_center_input_path" name="inputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/sort/part-r-00000', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr class="cluster-cener-input-path-tr">
                    <td>输出路径(HDFS)：</td>
                    <td class="cluster-cener-output-path-td">
                        <input id="cluster_center_output_path" name="outputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_center/iter_0/clustered/part-m-00000', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr class="local-data-path-tr">
                    <td>输出路径(本地)：</td>
                    <td class="local-data-path-td">
                        <input id="local_data_path" name="localFilePath" class="easyui-textbox" style="width:200px;" data-options="value:'WEB-INF/classes/centervector.dat', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.findClusterCenter();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>