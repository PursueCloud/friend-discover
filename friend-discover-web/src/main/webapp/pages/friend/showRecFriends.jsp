<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <table id="rec_user_datagrid"></table>
    <div id="rec_user_toolbar">
        <table cellspacing="0" cellpadding="0" width="100%">
            <tr>
                <td style="padding-left:2px" >
                    <a href="javascript:void(0)" id="rec_filter_cols_a" class="easyui-linkbutton" iconCls="icon-filter" plain="true" >筛选列</a>
                </td>
                <td style="text-align:right;padding-right:2px">
                    <select id="rec_user_search_col_select" class="easyui-combobox" style="width:150px;" data-options="prompt:'请选择搜索项(可多选）',multiple:true, editable:false,panelHeight:'auto', ">
                    </select>
                    <input id="rec_user_search_cont" class="easyui-searchbox" style="width:280px;" data-options="prompt:'请输入搜索内容(如有多个，以英文逗号分隔)'"/>
                </td>
            </tr>
        </table>
    </div>

    <script>
        $(function(){
            seajs.use([ 'module/friend/friend.js' ], function(friend) {
                friend.showRecFriends();
            });
        })
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>