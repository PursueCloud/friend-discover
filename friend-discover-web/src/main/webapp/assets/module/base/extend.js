/**
 *  拓展js
 */
define(function(require, exports, module) {
	
	Extend = function(){};
	
	Extend.prototype = {
			constructor:Extend,
			init:function(){
				// extend the 'textbox' addClearBtn
				$.extend($.fn.textbox.methods, {
			         addClearBtn: function(jq, iconCls){
			             return jq.each(function(){
			                 var t = $(this);
			                 var opts = t.textbox('options');
			                 opts.icons = opts.icons || [];
			                 opts.icons.unshift({
			                     iconCls: iconCls,
			                     handler: function(e){
			                         $(e.data.target).textbox('clear').textbox('textbox').focus();
			                         $(this).css('visibility','hidden');
			                     }
			                 });
			                 t.textbox();
			                 if (!t.textbox('getText')){
			                     t.textbox('getIcon',0).css('visibility','hidden');
			                 }
			                 t.textbox('textbox').bind('keyup', function(){
			                     var icon = t.textbox('getIcon',0);
			                     if ($(this).val()){
			                         icon.css('visibility','visible');
			                     } else {
			                         icon.css('visibility','hidden');
			                     }
			                 });
			             });
			         }
			     });
				// extend the 'equals' rule
//				$.extend($.fn.validatebox.defaults.rules, {
//				    equals: {
//				        validator: function(value,param){
//				        	console.log(value,$(param[0]).val());
//				            return value == $(param[0]).val();
//				        },
//				        message: '数据值不相等'
//				    }
//				});
				//datagrid里面通过键盘上下键选中行（author：jay.huang
				$.extend($.fn.datagrid.methods, {
				    keySelect : function (jq) {
				        return jq.each(function () {
				            var grid = $(this);
				            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
				                switch (e.keyCode) {
				                case 38: // up
				                    var selected = grid.datagrid('getSelected');
				                    if (selected) {
				                        var index = grid.datagrid('getRowIndex', selected);
				                        grid.datagrid('selectRow', index - 1);
				                    } else {
				                        var rows = grid.datagrid('getRows');
				                        grid.datagrid('selectRow', rows.length - 1);
				                    }
				                    break;
				                case 40: // down
				                    var selected = grid.datagrid('getSelected');
				                    if (selected) {
				                        var index = grid.datagrid('getRowIndex', selected);
				                        grid.datagrid('selectRow', index + 1);
				                    } else {
				                        grid.datagrid('selectRow', 0);
				                    }
				                    break;
				                }
				            });
				        });
				    },
				    keyMove : function (jq) {
				        return jq.each(function () {
				            var grid = $(this);
				            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
				                switch (e.keyCode) {
				                case 38: // up
				                    var selected = grid.datagrid('getSelected');
				                    if (selected) {
				                        var index = grid.datagrid('getRowIndex', selected);
				                        if(index - 1 >= 0) {
				                        	grid.datagrid('selectRow', index - 1);
				                        } else {
				                        	var rows = grid.datagrid('getRows');
//					                        console.log(rows.length)
					                        grid.datagrid('selectRow', rows.length - 1);
				                        }
				                        grid.datagrid('unselectRow', index);
				                    } else {
				                        var rows = grid.datagrid('getRows');
//				                        console.log(rows.length)
				                        grid.datagrid('selectRow', rows.length - 1);
				                    }
				                    break;
				                case 40: // down
				                    var selected = grid.datagrid('getSelected');
				                    if (selected) {
				                        var index = grid.datagrid('getRowIndex', selected);
				                        var rows = grid.datagrid('getRows');
				                        if(index + 1 <= rows.length -1) {
				                        	grid.datagrid('selectRow', index + 1);
				                        } else {
				                        	grid.datagrid('selectRow', 0);
				                        }
				                        grid.datagrid('unselectRow', index);
				                    } else {
//				                        console.log(rows.length)
				                        grid.datagrid('selectRow', 0);
				                    }
				                    break;
				                }
				            });
				        });
				    }
				});
				
//				$.extend($.fn.treegrid.methods, {
//					keyMove : function (jq) {
//				        return jq.each(function () {
//				            var grid = $(this);
//				            grid.treegrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
//				                switch (e.keyCode) {
//				                case 38: // up
//				                    var selected = grid.treegrid('getSelected');
//				                    if (selected) {
//				                        var index = grid.treegrid('getRowIndex', selected);
//				                        if(index - 1 >= 0) {
//				                        	grid.treegrid('select', index - 1);
//				                        } else {
//				                        	var rows = grid.treegrid('getRows');
////					                        console.log(rows.length)
//					                        grid.treegrid('select', rows.length - 1);
//				                        }
//				                        grid.treegrid('unselect', index);
//				                    } else {
//				                        var rows = grid.treegrid('getRows');
////				                        console.log(rows.length)
//				                        grid.treegrid('select', rows.length - 1);
//				                    }
//				                    break;
//				                case 40: // down
//				                    var selected = grid.treegrid('getSelected');
//				                    if (selected) {
//				                        var index = grid.treegrid('getRowIndex', selected);
//				                        var rows = grid.treegrid('getRows');
//				                        if(index + 1 <= rows.length -1) {
//				                        	grid.treegrid('select', index + 1);
//				                        } else {
//				                        	grid.treegrid('select', 0);
//				                        }
//				                        grid.treegrid('unselect', index);
//				                    } else {
////				                        console.log(rows.length)
//				                        grid.treegrid('select', 0);
//				                    }
//				                    break;
//				                }
//				            });
//				        });
//				    }
//				});

				/**
				 * 扩展树表格级联勾选方法：
				 * @param {Object} container
				 * @param {Object} options
				 * @return {TypeName}
				 */
				var childCnt = 0;
				var lastPid = undefined;
				var causeByChildNode = false;
				$.extend($.fn.treegrid.methods,{
					/**
					 * 实现treegrid级联选中(浅度级联，当且仅当当前节点存在子节点时才进行级联选中
					 * target tree对象
					 * param { //参数
					 * 		row: 当前行,
					 * 	  	opType: 操作类型：1，选中；2：取消选中；
					 * }
					 * @return {TypeName}
					 */
					cascadeSelect : function(target,param){
						//级联选中方法调用
						return checkAllOrNotEvent($(target), param.row, param.opType);
						//级联选中方法定义
						function checkAllOrNotEvent($elem, thisRow, opType) {
							if(!thisRow.isParent) {
								//console.log(childCnt);
								//console.log(lastPid);
								if(childCnt == 0 && !lastPid) {
									//console.log('children node');
									var currPid = thisRow.parentId;
									causeByChildNode = true;
									if(isAllCheck($elem, thisRow)) {
										$elem.treegrid('select', currPid);
									} else {
										$elem.treegrid('unselect', currPid);
									}
									return true;
								} else {
									return false;
								}
							} else {
								var nodes = $elem.treegrid('getChildren', thisRow.menuId);
								if(nodes && nodes.length) {
									if(!causeByChildNode) {
										//console.log('parent node');
										//当不选中一个父节点时，全不选其子节点
										lastPid = thisRow.menuId;
										childCnt = nodes.length;
										if(1 == opType) {
											//console.log('select All');
											for(var i in nodes) {
												$elem.treegrid('select', nodes[i].menuId);
												childCnt--;
											}
										} else {
											//console.log('unselect All');
											for(var i in nodes) {
												$elem.treegrid('unselect', nodes[i].menuId);
												childCnt--;
											}
										}
										lastPid = undefined;
										return true;
									}  else {
										causeByChildNode = false;
										return false;
									}
								}
								return true;
							}
						}
						function isAllCheck($elem2, _row) {
							var pid = _row.parentId;
							var children = $elem2.treegrid('getChildren', pid);
							var checkChildCnt = 0;
							if(children && children.length) {
								var checkNodes = $elem2.treegrid('getSelections');
								if(checkNodes && checkNodes.length) {
									for(var i in checkNodes) {
										if(pid == checkNodes[i].parentId) {
											checkChildCnt++;
										}
									}
								}
								if(children.length == checkChildCnt) {
									return true;
								}
							}
							return false;
						}
					}
				});
			}
	}
	
	
	module.exports = new Extend();
});
