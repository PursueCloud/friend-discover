<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="ect_cont" style="padding:20px 30px;">
        <form action="dataCalc/execCluster" method="post" id="ect_form">
            <table>
                <tr class="cluster-input-path-tr">
                    <td>输入路径(HDFS)：</td>
                    <td class="cluster-input-path-td">
                        <input id="cluster_input_path" name="inputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/caldistance', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', multiple:true, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>距离阈值：</td>
                    <td>
                        <input id="distance_threshold_num" name="distanceThresholdNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入距离阈值', precision:0, min:1, value:100, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>密度计算算法：</td>
                    <td>
                        <select id="consistency_calc_algorithm" name="consistencyCalcAlgorithm" class="easyui-combobox" style="width:200px;" data-options="prompt:'请选择一种密度计算算法', required:true, panelHeight:'auto', editable:false,">
                            <option value="0">Cut-off算法</option>
                            <option value="1">Gaussian算法</option>
                        </select>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>局部密度MR Reducer个数：</td>
                    <td>
                        <input id="part_consistency_mr_reducer_num" name="partConsistencyMRReducerNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入局部密度MR Reducer个数', precision:0, min:1, value:1, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>最小距离MR Reducer个数：</td>
                    <td>
                        <input id="min_distance_mr_reducer_num" name="minDistanceMRReducerNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入最小距离MR Reducer个数', precision:0, min:1, value:1, required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>
                        排序MR Reducer个数：</td>
                    <td>
                        <input id="sort_mr_reducer_num" name="sortMRReducerNum" class="easyui-numberspinner" style="width:200px;" data-options="prompt:'请输入排序MR Reducer个数', precision:0, min:1, value:1, required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.execCluster();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>