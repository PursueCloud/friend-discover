/**
 * 角色界面控制
 */


//所有变化过的权限id都登记在这个数组中
var authChangeIndexs = {
		menuAuthGrid:[],
};

//模块化区
define(function(require,exports,module){
	var base = require("module/base/main.js"); // 基本页面工具调用
	var extend = require("module/base/extend.js");
	var utils = require("module/base/utils.js"); // 工具
	var roleUrl = "admin/role/getRoles";
	var editUrl = "admin/role/edit";
	var delUrl = "admin/role/del";
	
//editUserRole
	var getUsersRoleUrl = "admin/role/searchUsers";
	var updateUserRoleUrl = "admin/role/updateUsers";
//editAuth
	var menuUrl = "admin/auth/loadRoleAuth";
	var orgUrl = "admin/org/loadOrgAuth";
	var getAuthUrl = "admin/role/getResources";
	var authEdit = "admin/role/updateRolePermission";
	var curEditRoleId;
	var width='400px';
	var height='300px';
	var editingId;
	var RoleMain =function(){
		this.$grid;
		this.$dialog;
		this.$form;
		this.$toolbar;
		this.$searchBox;
		this.$authTab;
		this.$menuAuthGrid;
		this.$userRoleGrid;
		this.$userRoleSearch;
		this.$userRoleGridBar;
	}
		
	RoleMain.prototype = {
		constructor:RoleMain,
		/**
		 * 初始化函数
		 */
		index : function(){
			var self = this;
			extend.init();
			self.$grid=$('#roleGrid');
			self.$dialog=$('#editWindow');
			self.$toolbar=$("#roleGridBar");
			self.$form=$("#authRoleForm");
			self.$searchBox = $("#roleSearch");
//角色列表头
			var dgColumns = [[
			      {field: 'roleName',title: '角色名称',width: 100}, 
			      {field: "description",title: "描述",width: 170},
			      {field: 'createTime',title: '创建时间',width: 135},
				  {field: 'modifyTime',title: '修改时间',width: 135},
			 ]];
			
//搜索框
			self.$searchBox.searchbox({
			    searcher:function(value,name){
			    	self.$grid.datagrid("load",{"keyword":value})
			    }
			});
			self.$searchBox.textbox('addClearBtn', 'icon-clear');//添加关闭小标签
			
//表格初始化
			self.$grid.datagrid({
				url : roleUrl,
				columns : dgColumns,
				toolbar : self.$toolbar,
				rownumbers : true,
			    fit : true,
//				fitColumns:true,
				pagination : true,
				pageSize:20,
				pageList : [ 10, 20, 50 ,100 ],
				ctrlSelect:true,
                singleSelect:false
			});
			self.initEleEvent();
		},//end init

//事件初始化
//窗口底部按钮
		initEleEvent:function(){
			var self = this;

			var footButtons=[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						self.$form.submit();
					}
				},{
					text:'取消',
					handler:function(){self.$dialog.dialog("close");}
				}];
			var authFootButtons=[{
				text:'确定修改',
				iconCls:'icon-ok',
				handler:function(){
					var roleId = document.getElementsByName("role")[0].value;
					var rows =[];
					for(var i in authChangeIndexs.menuAuthGrid){
						rows.push($("#menuAuthGrid").treegrid('find',authChangeIndexs.menuAuthGrid[i]));
						rows[rows.length-1].name="Menu";
					}
					//记得清洗全局变量数组
					//每次提交都清洗监听更新的变量
					authChangeIndexs.menuAuthGrid.length = 0;
					for (var i = 0; i < rows.length; i++) {
						if (rows[i].isNew === undefined  || !rows[i].isNew) {
							rows.splice(i,1);
							i--;
						}
						rows[i].roleId = roleId;
					}	
					//rows最后是已勾选的行
					$.ajax({
						"url":"admin/role/updateRolePermission",
						"type":"post",
						"dataType":"json",
						"contentType":"application/json",
						"data":JSON.stringify(rows)
					})
					$.messager.alert('提示','修改成功','info');
					
					self.$dialog.dialog("close");
				}
			},{
				text:'取消',
				handler:function(){self.$dialog.dialog("close");}
			}];
			
			var userRolrEditFootBtn=[{
				text:'确定分配',
				iconCls:'icon-ok',
				handler:function(){
					var roleId = $("input[name='role']").attr("value");
					var checkedRows = $("#userRoleGrid").datagrid("getSelections");
					var usersStaus = $("#thisRoleUserStatus").val();

					var currPageRows = $("#userRoleGrid").datagrid('getRows');
					var addUserIds = [];
					var delUserIds = [];
					for(var i in checkedRows) {//将所有选中的行添加到待新增权限id数组中
						addUserIds.push(checkedRows[i].userId);
					}
					for(var i in currPageRows) {
						var isChecked = false;
						for(var j in checkedRows) {
							if( currPageRows[i] == checkedRows[j] ) {
								isChecked = true;
								break;
							}
						}
						if( !isChecked ) {//如果当前行未被选中，则添加到待移除权限id数组中
							delUserIds.push(currPageRows[i].userId);
						}
					}
					var data = {
						roleId: roleId,
						'addUserIds[]': addUserIds,
						'delUserIds[]': delUserIds,
					}
					$.ajax({
						"url":updateUserRoleUrl,
						"type":"post",
						"dataType":"json",
						//"contentType":"application/json",
						"data": data,
						"success":function(resp){
							if (resp.meta.success) {
								$.messager.alert('提示', "保存成功", 'info', function() {
									self.$dialog.dialog("close");
									self.$grid.datagrid('reload');
								});
							} else {
								$.messager.alert('错误', "保存失败", 'error');
							}
						}
					});
				}
			},{
				text:'取消',
				handler:function(){self.$dialog.dialog("close");}
			}];
		
			
			self.$toolbar.on("click",".btn-add",function(){
				self.$dialog.dialog({
					title:'新建角色',
					width:width,
					height:height,
					modal:true,
					href:editUrl,
					queryParams:{},
					buttons:footButtons,
					minimizable:true,
					maximizable:true,
					resizable:true,
					onLoad: function() {
						self.$dialog.dialog('center');
					},
				}); 
				return false;
			})
			.on("click",".btn-edit",function(){
				var selected=self.$grid.datagrid('getSelected');
				if(!selected){
					$.messager.alert('提示','请选择行','warning');
					return false;
				}
				self.$dialog.dialog({
					title:'编辑角色',
					width:width,
					height:height,
					href:editUrl,
					modal:true,
					queryParams:{roleId:selected.roleId},
					buttons:footButtons,
					minimizable:true,
					maximizable:true,
					resizable:true,
					onLoad: function() {
						self.$dialog.dialog('center');
					},
				}); 
				return false;
			})
			.on("click",".btn-del",function(){
				var rows=self.$grid.datagrid('getSelections');
				if(rows){
					var ids=new Array();
					for(var i=0;i<rows.length;i++){
						ids.push(rows[i].roleId);
					}
					$.messager.confirm('请确认','删除所选角色吗？',function(r){
					    if (r){
					    	$.ajax({
					    		url:delUrl,
					    		data:{"ids[]":ids},
					    		type:'post',
					    		dataType:'json',
					    		success:function(resp){
				                	if(resp.meta.success){
						    			self.$grid.datagrid('reload');
						    		}else {
						    			$.messager.alert('提示', "删除失败", 'info')
						    		}
					    		}
							});
					    }
					});
				}
				return false;
			})
			.on("click",".btn-editAuth",function(){
				var selected=self.$grid.datagrid('getSelected');
				if(!selected){
					$.messager.alert('提示','请选择行','warning');
					return false;
				}
				self.$dialog.dialog({
					title:'修改权限',
					width:  '500px',
					height:  '400px',
					modal:true,
					href:editUrl,
					queryParams:{roleId:selected.roleId,name:"editAuth",id:''},
					buttons:authFootButtons,
					minimizable:true,
					maximizable:true,
					resizable:true,
					onLoad: function() {
						self.$dialog.dialog('center');
					},
				}); 
				return false;
			})
			.on("click",".btn-editUserRold",function(){
				var selected=self.$grid.datagrid('getSelected');
				if(!selected){
					$.messager.alert('提示','请选择行','warning');
					return false;
				}
				self.$dialog.dialog({
					title:'分配用户',
					width:  '500px',
					height:  '400px',
					modal:true,
					href:editUrl,
					queryParams: {roleId:selected.roleId,name:"editUserRole",id:''},
					buttons: userRolrEditFootBtn,
					minimizable:true,
					maximizable:true,
					resizable:true,
					onLoad: function() {
						self.$dialog.dialog('center');
					},
				}); 
				return false;
			});
		},//end initEleEvent

//edit页面初始化
		editRole : function() {
			var self = this;
			self.$form=$("#authRoleForm");
			self.$form.form({
				success : function(resp) {
					resp = $.parseJSON(resp);
					if (resp.meta.success) {
						$.messager.alert('提示', "保存成功", 'info', function() {
							self.$dialog.dialog("close");
							self.$grid.datagrid('reload');
						});
					} else {
						$.messager.alert('错误', "保存失败", 'error');
					}
				}
			});
		},
		
		editAuth:function(){
			var self = this;
			var roleId = $("input[name='role']").attr("value");
			var menuColumns = [
			                   	[
			                   	 {
			                   		 title:'菜单目录',
			                   		 field:'menuName',
			                   		 width:'354px'
			                   	 },{
			                   		 title:'可见',
			                   		 field:'view',
			                   		 width:'50px',
			                   		 height:'25px',
			                   		 formatter: function(value,row,index){	
			                   			var actions = row.actions;
			                   			var rowId = row.menuId;
			                   			var authCheckBox = '<input type="checkbox" onClick="authCheckBoxClick(this)" data-field="view" data-gridId="menuAuthGrid" data-rowId="'+rowId+'" ';
			                   			if (actions != 'undefined') {
			                   				for(var i in actions){
			                   					if(actions[i] == 'view'){
			                   						return authCheckBox + 'checked="checked" />';
			                   					}			
			                   				}			                   				
			                   			}
		                   				return authCheckBox +'/>';          						                   			
			                   		 }
			                   	 },{
			                   		 title:'编辑',
			                   		 field:'edit',
			                   		 width:'50px',
			                   		 height:'25px',
			                   		 formatter: function(value,row,index){
			                   			var actions = row.actions;
			                   			var rowId = row.menuId;
			                   			var authCheckBox = '<input type="checkbox" onClick="authCheckBoxClick(this)" data-field="edit" data-gridId="menuAuthGrid" data-rowId="'+rowId+'" ';
			                   			if (actions != 'undefined') {
			                   				for(var i in actions){
			                   					if(actions[i] == 'edit'){
			                   						return authCheckBox + 'checked="checked" />'
			                   					}			
			                   				}			                   				
			                   			}
		                   				return authCheckBox +'/>';         						                   			
			                   		 }
			                   	 }
			                   	]
			                  ];
			
			self.$authTab=$("#authTab");
			self.$menuAuthGrid=$("#menuAuthGrid");
			var $menuAuthEditToolBar=$("#menuAuthEditToolBar");

			self.initTreeGrid(self.$menuAuthGrid,menuUrl,menuColumns,roleId,"menuId","menuName",'Menu', $menuAuthEditToolBar);
		},
		
		initTreeGrid:function($grid,url,columns,roleId,idField,treeField,authType,toolbar){
			var self = this;
			curEditRoleId = roleId;
			$grid.treegrid({
			    url:url,
			    rownumbers: true,
			    fit: true,
			    pagination:true,
			    toolbar:toolbar,
			    pageList: [10,20, 50, 100],
			    idField : idField,
			    treeField : treeField,
			    columns: columns,
			    animate:true,
			    queryParams:{
			    	"roleId":roleId,
			    	"authType":authType
			    },
			    onBeforeLoad: function(row,param){
                    if (!row) {    // load top level rows
                    	param.id = 0;    // set id=0, indicate to load new page rows
                        param.isRoot=true;
                    }
                },
                ctrlSelect:true,
                singleSelect:false
			});
		},
		
		editUserRole : function(){
			var self = this;
			self.$userRoleGridBar = $("#userRoleGridBar");
			self.$userRoleSearch = $("#userRoleSearch");
			self.$userRoleGrid = $("#userRoleGrid");
			var roleId = $("input[name='role']").attr("value");
			
			var userRoleColumn = [[
			    {field: "isCheck",checkbox:true},
			    {field: 'userName',title: '用户名称',width: 100}, 
			    {field: 'status',title: '状态',width: 100,
			    	formatter: function(val, row, index){
						if( row && row.assigned ) {
							self.$userRoleGrid.datagrid('checkRow', index);
						}
			    		if(val == '1') return "有效";
			    		else if(val == '2') return "无效";
				    }
			    } 
			]];

			$("#thisRoleUserStatus").combobox({
				onChange: function(newVal, oldVal) {
					var keyword = self.$userRoleSearch.searchbox('getValue');
					self.$userRoleGrid.datagrid("load",{"roleId":roleId,"keyword":keyword,"filter":newVal});
				}
			})
			//搜索框
			self.$userRoleSearch.searchbox({
				searcher:function(value,name){
					var usersStaus = $("#thisRoleUserStatus").val();
					self.$userRoleGrid.datagrid("load",{"roleId":roleId,"keyword":value,"filter":usersStaus});
				}
			});
			self.$userRoleSearch.textbox('addClearBtn', 'icon-clear');//添加关闭小标签
			
			

			self.$userRoleGrid.datagrid({
				url : getUsersRoleUrl,
				idField: "userId",
				columns : userRoleColumn ,
				toolbar : self.$userRoleGridBar,
				rownumbers : true,
			    fit : true,
//				fitColumns:true,
				pagination : true,
				pageSize:20,
				pageList : [ 10, 20, 50 ,100 ],
				queryParams:{
			    	"roleId":roleId,
			    },
                onLoadSuccess : function(resp){
                	if (resp.users) {
                		for (var i =0;i < resp.users.length;i++) {
                			var index  = self.$userRoleGrid.datagrid('getRowIndex',resp.users[i].userId);
                			if(index != -1){
                    			self.$userRoleGrid.datagrid('checkRow',index);
                			}
                		}              		
                	}
                },
                rowStyler: function(index,row){
                	return 'background-color:#FAFAFA;color:#000;';
            	},
				singleSelect:false,				
			});
			
			$("#thisRoleUserStatus").change(function(){
				var usersStaus = $(this).val();
				switch(usersStaus){
					case '全部用户'  : 
						self.$userRoleGrid.datagrid("unselectAll");
						self.$userRoleGrid.datagrid("load",{"fiterFlag":"全部用户","roleId":roleId});
						break;
					case '当前用户' : 
						self.$userRoleGrid.datagrid("load",{"fiterFlag":"当前用户","roleId":roleId});
						break;
					case '可分配用户' : 
						self.$userRoleGrid.datagrid("unselectAll");
						self.$userRoleGrid.datagrid("load",{"fiterFlag":"可分配用户","roleId":roleId});
						break;
				}
				
			})
		}
	}


	module.exports = new RoleMain();
});


function authCheckBoxClick(target){
	var rowId = target.getAttribute("data-rowId");
	var gridId = target.getAttribute("data-gridId");
	var field = target.getAttribute("data-field");
	var row = $('#'+gridId).treegrid('find',rowId);
	if(row.children !== undefined){
//		console.log(row);
	}
	if(row.actions === undefined){
		row.actions = [];
	}
	var length = row.actions.length;
	for(var i in row.actions){
		if (row.actions[i] == field) {
			row.actions.splice(i,1);
		}
	}
	if (length == row.actions.length) {
		row.actions.push(field);
	}
	if (row.isNew === undefined) {
		row.isNew = true;
		authChangeIndexs[gridId].push(rowId);
	}
	$('#'+gridId).treegrid('update',{
		id: rowId,
		row: row
	})
	
}	