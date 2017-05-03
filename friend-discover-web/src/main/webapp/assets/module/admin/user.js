/**
 * 用户管理界面控制
 */
define(function(require, exports, module) {

	var base = require("module/base/main.js"); // 基本页面工具调用
	var extend = require("module/base/extend.js")
//	var admin = require("module/admin/js/admin.js");//父框架调用
	var utils = require("module/base/utils.js"); // 工具
	//连接配置
	var userUrl = "admin/user/getUsers";
	var editUrl = "admin/user/edit";
	var saveUrl = "admin/user/save";
	var delUrl = "admin/user/del";
	var userRoleUrl = "admin/user/getUserRole";
	var width='600px';
	var height='300px';

	


	var UserMain =function(){
		this.$grid;
		this.$dialog;
		this.$form;
		this.$toolbar;
		this.$searchbox;
		this.$roleTypeGrid;
	}

	UserMain.prototype = {
		constructor: UserMain,
		/**
		 * 初始化函数
		 */
		init: function() {
			extend.init();
			var self = this;
			self.$grid=$('#userGrid');
			self.$dialog=$('#userEditWindow');
			self.$toolbar=$("#userGridBar");
			self.$form=$("#userForm");
			self.$searchbox=self.$toolbar.find(".js-searchbox");
			//表头
			var dgColumns = [[
			      {field: "account",title: "账户名",width: 150},
			      {field: 'userName',title: '昵称',width: 120},
			      {field: 'status',title: '状态',width: 50,
			    	  formatter: function(val, row, index){
			    		 if(val == '1') return "有效";
			    		 else if(val == '2') return "无效";
			    	  }	
			      },
				  {field: 'createTime', title: '创建时间', width:130},
				  {field: 'modifyTime', title: '修改时间', width:130},
			]];
			
			self.$grid.datagrid({
				url : userUrl,
				columns : dgColumns,
				toolbar : self.$toolbar,
				rownumbers : true,
				fit : true,
//				fitColumns:true,
				pagination : true,
				pageList : [ 10,20,50,100 ],
				pageNumber:1,
				pageSize:20,
				ctrlSelect:true,
                singleSelect:false
			});
			self.initEleEvent();
			
			self.$searchbox.searchbox({
			    searcher:function(value,name){
			    	//模糊搜索keyword
			    	//返回结果（分页）刷新列表
			    	//分页是个问题
			    	console.log(self.$grid);
			    	var options = $('#userGrid').datagrid('getPager').data("pagination").options;  
			    	var page = options.pageNumber;  
			    	var pageSize = options.pageSize;  
			    	$('#userGrid').datagrid('load',{
						"keyword":value,
						"page":page,
						"rows":pageSize
					});
			    }
			});
			self.$searchbox.textbox('addClearBtn', 'icon-clear');//添加
		},
		
		initEleEvent:function(){
			var self = this;
			//窗口底部按钮
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
			
			
			self.$toolbar.on("click",".btn-add",function(){
				self.$dialog.dialog({
					title:'新建用户',
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
					}
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
					title:'编辑用户',
					width:width,
					height:height,
					modal:true,
					href:editUrl,
					queryParams:{userId: selected.userId},
					buttons:footButtons,
					minimizable:true,
					maximizable:true,
					resizable:true,
					onLoad: function() {
						self.$dialog.dialog('center');
					}
				}); 
				return false;
			})
			.on("click",".btn-del",function(){
				var rows=self.$grid.datagrid('getSelections');
				if(rows){
					var ids=new Array();
					for(var i=0;i<rows.length;i++){
						ids.push(rows[i].userId);
					}
					$.messager.confirm('请确认','删除所选用户吗？',function(r){
					    if (r){
					    	$.post(delUrl,{"ids[]":ids},function(resp){
					    		resp=$.parseJSON(resp);
			                	if(resp.meta.success){
					    			self.$grid.datagrid('reload');
					    		}else {
					    			$.messager.alert('提示', "删除失败", 'info')
					    		}
							});
					    }
					});
				}
				return false;
			}).on("click",".btn-editPas",function(){
				var editPasFootBtn = [{
					text:'保存',
					iconCls:'icon-ok',
					handler:function(){
						self.$userPasForm.submit();
					}
				},{
					text:'取消',
					handler:function(){self.$dialog.dialog("close");}
				}];
				
				var selected=self.$grid.datagrid('getSelected');
				if(!selected){
					$.messager.alert('提示','请选择行','warning');
					return false;
				}
				self.$dialog.dialog({
					title:'重置密码',
					width: '400px',
					height: '300px',
					modal:true,
					href:editUrl,
					queryParams:{userId:selected.userId,editPasFlag:"yes"},
					buttons: editPasFootBtn,
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
		
		edit:function(){
			var self = this;
			self.$form=$('#userForm');
			var userId = document.getElementsByName("userId")[0].getAttribute("value");
			self.$roleTypeGrid = $('#roleTypeGrid');
			var dgColumns = [[
			    {field:"checkbox",checkbox:true},
			    {field: 'roleId',title: '角色ID：',width: 100}, 
				{field: 'roleName',title: '角色名称：',width: 100}, 
				{field: "description",title: "描述：",width: 100},
			]];
			self.$form.form({
                success:function(resp){
                	resp=$.parseJSON(resp);
                	if(resp.meta.success){
	                    $.messager.alert('提示', "保存成功", 'info',function(){
	                    	self.$dialog.dialog("close");
	                    	self.$grid.datagrid('reload');
	                    });
                	}else{
                		 $.messager.alert('错误', "保存失败", 'error');
                	}
                }			
            });
			self.$roleTypeGrid.combogrid({       
			    idField:'roleId',    
			    textField:'roleName',    
			    url:userRoleUrl,    
			    columns: dgColumns,
			    fitColumns:true,
			    checkbox : true,
			    singleSelect:false,
			    multiple: true,
			    editable:false,
				resizeHandle:'both',
				queryParams:{"userId":userId},
				onLoadSuccess:function(data){
					self.$roleTypeGrid.combogrid('setValues',data.userRoleIds)
				}
//			    mode:'remote'
			}); 
			var $account = $('#weaccount');
			if($account.val() != ""){
				$account.textbox({"disabled":true});
			}else{
				$account.textbox({"disabled":false});
			}

//远程验证用户账号唯一性
			
/*			var $account = $('input[name="account"]');
			$account.blur(function(){
				var account = $account.val();
				if(account != ""){
					$.ajax({
						type:'post',
						data:{"account" : account},
						url:"admin/user/cheakAccount",
						success:function(result){
							result = $.parseJSON(result);
							if(!result){
								 $.messager.alert('提示', "账号已被注册，请重新输入", 'warning');
								 $account.val("");
							}
						}
					})
				}				
			})*/
		},

		checkTwoPwdSame: function($userPasForm) {
			var self = this;

			if( $userPasForm && $userPasForm.length ) {
				var $firstPas = $userPasForm.find('#firstPas');
				var $newPas = $userPasForm.find('#newPas');
				var firstPwdCont = $firstPas.textbox("getValue");
				var newPwdCont = $newPas.textbox("getValue");
				if( firstPwdCont && newPwdCont && firstPwdCont==newPwdCont ){
					var psd = $.sha256($firstPas.textbox("getValue"));
					$firstPas.textbox("setValue",psd);
					$newPas.textbox("setValue",psd);
					return true;
				} else {
					$.messager.alert('提示','两次密码输入不一致,请重新输入','warning',function(){
						//$firstPas.textbox("setValue","");;
						//$newPas.textbox("setValue","");
						$firstPas.focus();
					});
					return false;
				}
			}
			console.log('$userPasForm表单参数为空')
		},
		editPas : function(){
			var self = this;

			self.$userPasForm = $('#userPasForm');
			self.$userPasForm.form({
				onSubmit:function(){
					if( self.checkTwoPwdSame(self.$userPasForm) ) {
						return true;
					} else {
						return false;
					}
				},
                success:function(resp){
                	resp=$.parseJSON(resp);
                	if(resp.meta.success){
	                    $.messager.alert('提示', "保存成功", 'info',function(){
	                    	self.$dialog.dialog("close");
	                    	self.$grid.datagrid('reload');
	                    })
                	}else{
                		 $.messager.alert('错误', "保存失败", 'error');
                	}
                }			
            });
		}
		
		
		
	}

	module.exports = new UserMain();
})