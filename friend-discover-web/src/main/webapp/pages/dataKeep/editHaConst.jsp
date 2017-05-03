<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="form_cont">
        <form action="/dataKeep/add" method="post" id="edit_haConst_form" class="formTable">
            <table>
                <tr>
                    <td><input type="hidden" id="table_code" name="tableCode" /></td>
                    <td><input type="hidden" id="const_id" name="constId" /></td>
                </tr>
                <tr>
                    <td>配置项名：</td>
                    <td><input id="const_key" name="constKey" class="easyui-textbox" style="width:270px;" data-options="prompt:'请输入配置项名', required:true"/></td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>配置项值：</td>
                    <td><input id="const_value" name="constValue" class="easyui-textbox" style="width:270px;" data-options="prompt:'请输入配置项值', required:true"/></td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>备注说明：</td>
                    <td><input id="description" name="description" class="easyui-textbox" style="width:270px;height:100px;" data-options="prompt:'请输入备注说明', multiline:true"/></td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
                dk.editHaConst();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>