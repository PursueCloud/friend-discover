<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="cjm_dialog_win"></div>
    <div id="cjm_dialog_win2"></div>
    <div id="monitor_one_cont" style="padding:20px 35px;">
        <form id="monitor_one_form">
            <div id="no_job_running_tips" style="color:red;text-align: center;padding-top: 150px;">当前无任务运行</div>
            <table id="job_detail_tab" style="display:none;margin:auto;">
                <tr>
                    <td>任务总数(个）：</td>
                    <td>
                        <input id="job_nums" name="jobNums" class="easyui-textbox" style="width:200px;" data-options="value:'#', required:true, editable:false"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>当前任务（序号）：</td>
                    <td>
                        <input id="curr_job" name="currJob" class="easyui-textbox" style="width:200px;" data-options="value:'#', required:true, editable:false"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>JobId：</td>
                    <td>
                        <input id="job_id" name="jobId" class="easyui-textbox" style="width:200px;" data-options="value:'#', required:true, editable:false"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>JobName：</td>
                    <td>
                        <input id="job_name" name="jobName" class="easyui-textbox" style="width:200px;" data-options="value:'#', required:true, editable:false"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>Map进度：</td>
                    <td>
                        <input id="map_progress" name="mapProgress" class="easyui-textbox" style="width:200px;" data-options="value:'0.0%', required:true, editable:false"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>Reduce进度：</td>
                    <td>
                        <input id="red_progress" name="redProgress" class="easyui-textbox" style="width:200px;" data-options="value:'0.0%', required:true, editable:false"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>任务执行状态：</td>
                    <td>
                        <input id="job_state" name="runState" class="easyui-textbox" style="width:200px;" data-options="value:'0.0%', required:true, editable:false"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(ha) {
                ha.monitorOne();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>