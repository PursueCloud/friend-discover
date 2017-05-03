<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="fd_dialog_win"></div>
    <div id="fd_dialog_win2"></div>
    <table id="user_datagrid"></table>
    <div id="user_toolbar">
        <table cellspacing="5" cellpadding="0" width="100%">
            <tr>
                <td style="padding-left:2px">
                    创建日期：<input id="begin_create_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'开始'"/> ~
                    <input id="end_create_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'结束'"/> &nbsp;&nbsp;&nbsp;&nbsp;|
                    修改日期：<input id="begin_modify_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'开始'"/> ~
                    <input id="end_modify_date" class="easyui-datebox" style="width:95px;" data-options="prompt:'结束'"/>
                    <a href="javascript:void(0)" id="more_sc_a" class="easyui-linkbutton" style="outline:none;" iconCls="icon-arrow-bottom" plain="true" title="更多搜索条件"></a>
                </td>
                <td style="text-align:right;padding-right:2px">
                    <a href="javascript:void(0)" id="search_a" class="easyui-linkbutton" style="background-color:dodgerblue;color:white;outline:none;" iconCls="icon-search" plain="true">搜索</a>
                    <select id="user_search_col_select" class="easyui-combobox" style="width:150px;" data-options="prompt:'请选择搜索项(可多选）',multiple:true, editable:false,panelHeight:'auto', ">
                    </select>
                    <input id="user_search_cont" class="easyui-searchbox" style="width:280px;" data-options="prompt:'请输入搜索内容(如有多个，以英文逗号分隔)'"/>
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
                    <a href="javascript:void(0)" id="filter_cols_a" class="easyui-linkbutton" iconCls="icon-filter" plain="true" >筛选列</a> |
                    <a href="javascript:void(0)" id="friend_rec_a" class="easyui-linkbutton" iconCls="icon-man" plain="true" >朋友推荐</a> |
                    <a href="javascript:void(0)" id="show_user_relationship_a" class="easyui-linkbutton" iconCls="icon-large-chart" plain="true" >用户关系图</a>
                </td>
            </tr>
        </table>
    </div>

    <script>
        $(function(){
            seajs.use([ 'module/friend/friend.js' ], function(frd) {
                frd.init();
            });
        })
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>