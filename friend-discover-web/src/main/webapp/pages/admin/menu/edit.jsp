<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>
<body style="padding:10px;">
	<form id="menu_form" action="admin/menu/save" method="post" style="padding: 5% 22%;">
    	<input type="hidden" name="menuId" value="${menu.menuId}">
            <table cellspacing="5">
                <tr>
                    <td style="text-align:right;width:55px;">菜单名 :</td>
                    <td><input name="menuName" class="f1 easyui-textbox" value="${menu.menuName}" data-options="required:true, prompt:'请输入菜单名'"/></td>
                </tr>
                <tr>
                    <td style="text-align:right;width:55px;">父菜单 :</td>
                    <td><input name="pMenuId" type="text" value="${menu==null ? pMenuId:menu.pMenuId}" /></td>
                </tr>
                <tr>
                    <td>请求URL :</td>
                    <td><input name="url" class="f1 easyui-textbox" value="${menu.url}" /></td>
                </tr>
                <tr>
                    <td>菜单类型 :</td>
                    <td>
                    	<select class="easyui-combobox" name="menuType" style="width:135px;" data-options="panelHeight:'80px', editiable:false">
						    <option value="1" <c:if test="${menu.menuType == 1 }">selected</c:if>  >菜单目录</option>
						    <option value="0" <c:if test="${menu.menuType == 0 }">selected</c:if>  >页面</option>
						</select>
					</td>
                </tr>
                <tr>
                    <td>菜单顺序 :</td>
                    <td>
                    	 <input name="menuOrder" value="${menu.menuOrder}" class="easyui-numberspinner" style="width:135px;"  data-options="min:0" />
					</td>
                </tr>
            </table>
        </form>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/admin/menu.js' ], function(menu) {
                menu.edit();
            });
        });
    </script>
</body>
<%@ include file="/pages/include/ft.jsp"%>