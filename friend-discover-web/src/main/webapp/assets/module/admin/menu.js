/**
 * Tag页面控制，标签管理的入口页面
 */
define(function(require, exports, module) {
	var base = require("module/base/main.js");
	var extend = require("module/base/extend.js");
	var width='400px';
	var height='300px';
	var menuUrl = "admin/menu/searchMenus";
	var menuTreeUrl = "admin/getMenu";
	var editUrl = "admin/menu/edit";
	var delUrl = "admin/menu/del";
	
	var MenuMain = function() {
		this.$grid;
		this.$form;
		this.$dialog;
	};

	MenuMain.prototype = {
		constructor : MenuMain,
		/**
		 * 初始化函数
		 */
		index : function() {
			extend.init();
			var self = this;
			self.$grid=$('#menu_grid');
			self.$dialog=$('#win');
			//关闭事件
			var close=function(){
				self.$dialog.dialog("close");
			};
			//窗口底部按钮
			var footButtons=[{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						self.$form.submit();
					}
				},{
					text:'取消',
					handler:function(){close();}
				}];
			var toolbars=[{
				iconCls: 'icon-add',
				text:'新增',
				handler: function(){
					var selected=self.$grid.treegrid('getSelected');
					var pMenuId=0;
					if(selected){
						pMenuId=selected.menuId;
					}
					self.$dialog.dialog({
						title:'新建菜单',
						width:width,
						height:height,
						modal: true,
						href:editUrl,
						queryParams:{pMenuId: pMenuId},
						buttons:footButtons,
						minimizable:true,
						maximizable:true,
						resizable:true,
						onLoad: function() {
							self.$dialog.dialog('center');
						},
					}); 
				}
			},'-',{
				iconCls: 'icon-edit',
				text:'编辑',
				handler: function(){
					var selected=self.$grid.treegrid('getSelected');
					if(!selected){
						$.messager.alert('提示','请选择行','warning');
						return;
					}
					self.$dialog.dialog({
						title:'编辑菜单',
						width:width,
						height:height,
						modal: true,
						href:editUrl,
						queryParams:{menuId:selected.menuId},
						buttons:footButtons,
						minimizable:true,
						maximizable:true,
						resizable:true,
						onLoad: function() {
							self.$dialog.dialog('center');
						},
					}); 
				}
			},'-',{
				iconCls: 'icon-remove',
				text:'删除',
				handler: function(){
					var rows=self.$grid.treegrid('getSelections');
					if(rows){
						var ids=new Array;
						for(var i=0;i<rows.length;i++){
							ids.push(rows[i].menuId);
						}
						$.messager.confirm('请确认','确认删除所选菜单吗？',function(r){
						    if (r){
						    	$.post(delUrl,{ids:ids},function(resp){
						    		if(resp.meta.success){
						    			self.$grid.treegrid('reload');
						    		}
								},'json');
						    }
						});
						
					}
				}
			},'-',{
				iconCls: 'icon-reload',
				text:' 清除菜单缓存',
				handler: function(){
					self.$grid.treegrid('loading');
					$.get('admin/menu/clearMenuCache', null, function(resp) {
						resp = JSON.parse(resp);
						if( resp.meta.success ) {
							$.messager.alert('温馨提示', '菜单缓存清除完毕，请刷新页面！', 'info');
						}
						self.$grid.treegrid('loaded');
					});
				}
			}];
			self.$grid.treegrid({
			    url:menuUrl,
			    rownumbers: true,
			    fit: true,
			    pagination:true,
			    pageList: [1,10,20],
			    idField:'menuId',
			    treeField:'menuName',
			    toolbar: toolbars,
				animate: true,
			    columns:[[
			        {title:'菜单',field:'menuName',width:180},
			        {title:'URL',field:'url',width:180},
					{field: 'description', title: '备注', width:200},
					{field: 'createTime', title: '创建时间', width:130},
					{field: 'modifyTime', title: '修改时间', width:130},
			    ]],
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
		
		edit:function(){
			var self = this;
			var $combotree=$('input[name=pMenuId]');
			self.$form=$('#menu_form');
			$combotree.combotree({
				icons: [{
					iconCls:'icon-clear',
					handler: function(e){
						$combotree.combotree('clear');
					}
				}],
			    url: menuTreeUrl
			});
			self.$form.form({
				onSubmit: function() {
					if( self.$form.form("validate") ) {
						var menuOrder = self.$form.find('input[name="menuOrder"]').val();
						if( !menuOrder || menuOrder.replace(/\s/g, '')=='' ) {
							self.$form.find('input[name="menuOrder"]').textbox('setValue', 0)
							return true;
						} else if( isNaN( menuOrder.replace(/\s/g, ''))){
							$.messager.alert('温馨提示', '请输入正确的菜单顺序！', 'warning');
							return false;
						}
					} else {
						$.messager.alert('温馨提示', '您还有必输项未输入，请输入完成后再保存！', 'warning');
						return false;
					}
				},
                success:function(resp){
                	resp=$.parseJSON(resp);
                	if(resp.meta.success){
	                    $.messager.alert('提示', "保存成功", 'info',function(){
	                    	self.$dialog.dialog("close");
	//                    	self.$grid.treegrid('reload');
	                    });
                	}else{
                		 $.messager.alert('错误', "保存失败", 'error');
                	}
                }
            });
		}
	}

	module.exports = new MenuMain();
})