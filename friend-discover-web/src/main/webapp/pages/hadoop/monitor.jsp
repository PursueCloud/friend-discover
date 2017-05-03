<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="m_dialog_win"></div>
    <div id="m_dialog_win2"></div>
    <table id="monitor_grid" data-options="title:'任务实时监控（所有）'"></table>
    <div id="monitor_toolbar">
        <table cellspacing="0" cellpadding="0" style="width:100%">
            <td style="padding-left:2px">
                <select id="job_type" class="easyui-combobox" style="width:120px;" data-options="prompt:'请选择任务类型', required:true, editable:false,">
                    <option value="0">当前</option>
                    <option value="1">历史</option>
                </select>
                <a href="javascript:void(0)" id="show_curr_job_a" class="easyui-linkbutton" iconCls="icon-large-clipart" plain="true" >当前任务监控</a>
                <input type="radio" name="refreshType" value="0" id="auto_refresh" style="vertical-align: middle;"/><label style="vertical-align: middle;" for="auto_refresh">自动刷新</label>
                <input type="radio" name="refreshType" value="1" id="manual_refresh" style="vertical-align: middle;" checked/><label style="vertical-align: middle;" for="manual_refresh">手动刷新</label>
                <a href="javascript:void(0)" id="refresh_a" class="easyui-linkbutton" iconCls="icon-reload" plain="true" >刷新</a>
            </td>
            <td style="text-align:right;padding-right:2px">
                <input id="search_cont" class="easyui-searchbox" style="width:200px;" data-options="prompt:'请输入搜索内容(jobName)'"/>
            </td>
        </table>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(ha) {
                ha.monitor();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>