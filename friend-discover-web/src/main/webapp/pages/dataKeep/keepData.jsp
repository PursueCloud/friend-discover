<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <table id="table_datagrid"></table>
    <div id="dialog_win"></div>
    <div id="table_toolbar">
        <table cellspacing="5" cellpadding="0" width="100%">
            <tr>
                <td style="padding-left:2px">
                    表：<select id="keepData_table_select" class="easyui-combobox" style="width:200px;" data-options="prompt:'请选择表',editable:false,panelHeight:'80px', ">
                        <option value="0">用户属性表</option>
                        <option value="1">Hadoop集群配置表</option>
                    </select> &nbsp;&nbsp;&nbsp;&nbsp;|
                    创建日期：<input id="begin_create_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'开始'"/> ~
                    <input id="end_create_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'结束'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    修改日期：<input id="begin_modify_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'开始'"/> ~
                    <input id="end_modify_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'结束'"/>
                    <a href="javascript:void(0)" id="more_sc_a" class="easyui-linkbutton" style="outline:none;" iconCls="icon-arrow-bottom" plain="true" title="更多搜索条件"></a>
                </td>
                <td style="text-align:right;padding-right:2px">
                    <a href="javascript:void(0)" id="search_a" class="easyui-linkbutton" style="background-color:dodgerblue;color:white;outline:none;" iconCls="icon-search" plain="true">搜索</a>
                    <select id="keepData_search_col_select" class="easyui-combobox" style="width:150px;" data-options="prompt:'请选择搜索项(可多选）',multiple:true, editable:false,panelHeight:'auto', ">
                    </select>
                    <input id="search_cont" class="easyui-searchbox" style="width:280px;" data-options="prompt:'请输入搜索内容(如有多个，以英文逗号分隔)'"/>
                </td>
            </tr>
            <tr class="more-sc-tr" style="display:none;">
                <td style="padding-left:2px" colspan="2">
                    性别：<input type="radio" name="sex" value="" id="random_radio" checked/><label for="random_radio">不限</label>
                    <input type="radio" name="sex" value="0" id="man_radio" /><label for="man_radio">男</label>
                    <input type="radio" name="sex" value="1" id="woman_radio" /><label for="woman_radio">女</label> &nbsp;&nbsp;&nbsp;&nbsp;|
                    年龄：<input id="min_age" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最小'"/> ~
                    <input id="max_age" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最大'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    赞同数：<input id="min_upVotes" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最小'"/> ~
                    <input id="max_upVotes" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最大'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    反对数：<input id="min_downVotes" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最小'"/> ~
                    <input id="max_downVotes" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最大'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    声望值：<input id="min_reputation" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最小'"/> ~
                    <input id="max_reputation" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最大'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    浏览数：<input id="min_views" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最小'"/> ~
                    <input id="max_views" class="easyui-numberspinner" style="width:55px;" data-options="min:1, prompt:'最大'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    最后一次访问日期：<input id="begin_lastAccess_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'开始'"/> ~
                    <input id="end_lastAccess_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'结束'"/>
                </td>
            </tr>
            <tr>
                <td style="padding-left:2px" colspan="2">
                    <a href="javascript:void(0)" id="add_a" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a> |
                    <a href="javascript:void(0)" id="edit_a" class="easyui-linkbutton" iconCls="icon-edit" plain="true" >编辑</a> |
                    <a href="javascript:void(0)" id="del_a" class="easyui-linkbutton" iconCls="icon-remove" plain="true" >删除</a> |
                    <a href="javascript:void(0)" id="imp_a" class="easyui-linkbutton" iconCls="icon-import" plain="true" >导入</a> |
                    <a href="javascript:void(0)" id="exp_a" class="easyui-linkbutton" iconCls="icon-print" plain="true" >导出</a> |
                    <a href="javascript:void(0)" id="filter_cols_a" class="easyui-linkbutton" iconCls="icon-filter" plain="true" >筛选列</a> |
                    <a href="javascript:void(0)" id="save_a" class="easyui-linkbutton" iconCls="icon-save" plain="true" data-options="disabled:true">保存</a> |
                    <a href="javascript:void(0)" id="undo_a" class="easyui-linkbutton" iconCls="icon-undo" plain="true" data-options="disabled:true">撤销</a> |
                    <input type="checkbox" id="row_edit_pattern_check" style="vertical-align: middle;margin-top:5px;"/><label for="row_edit_pattern_check" style="vertical-align: middle;"><span id="edit_pattern_state" style="display:none;">开启</span>行编辑模式</label>
                </td>
            </tr>
        </table>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
                dk.keepData();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>