define(function(require, exports, module) {
	
	module.exports.init=function(){
		
	}
	/**
	 * 异步打开Tab
	 */
	module.exports.openAjaxTab = function openAjaxTab($tab, id, title, url, onLoadCallback) {
		var tabId='tab_'+id;
		if ($tab.tabs('exists', title)){
			$tab.tabs('select', title);
		} else {
//			var content = '<iframe class="easyui-layout" fit="true" scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
			var content = '<div class="new-tab" style="overflow: hidden;height:100%;"><iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe></div>';
			$tab.tabs('add',{
				id:tabId,
				title:title,
				content:content,
				closable:true,
				fit:true,
				onLoadSuccess: function() {
					if( onLoadCallback ) {
						onLoadCallback();
					}
				}
			});
		}
	};

	/**
	 * 提交表单
	 */
	module.exports.formSubmit = function(id) {
		$("#" + id).submit();
	};

	/**
	 * 刷新当前tab
	 */
	module.exports.refreshCurrentTab = function(url, tabFrame) {
		var tab = $(tabFrame).tabs('getSelected');
		$(tabFrame).tabs('update', {
			tab: tab,
			options: {
				href: url
			}
		});
	};

	/**
	 * 异步打开窗口
	 */
	module.exports.openModalWindow = function($frame, url, title, options) {
		var options = $.fn.$.extend({}, {
			href: url,
			title: title
		}, options);
		return this.openModalWindow($frame, options);
	};

	module.exports.openModalWindow = function($frame, url, options) {
		var options = $.fn.$.extend({}, {
			href: url
		}, options);
		return this.openModalWindow($frame, options);
	};

	module.exports.openModalWindow = function($frame, options) {
		$frame.window(options).window("open");
		//重绘UI样式
		$.parser.parse($frame);
		return $frame;
	};

	/**
	 * 中间弹出信息框，并且拦截操作
	 * @param  {String} title   弹窗标题
	 * @param  {String} msg     提示内容
	 * @param  {Number} timeout 自动消失时间，0为不消失
	 * @return {[type]}         [description]
	 */
	module.exports.openMessageAlertInCenter = function(title, msg, timeout) {
		var message = '<div style="height:100%;width:100%;vertical-align:middle;display:inline-table;"><span style="vertical-align:middle;display:table-cell;">' + msg + '</span></div>';
		$.messager.show({
			title: '提示',
			msg: message,
			timeout: timeout,
			showType: 'fade',
			style: {
				right: window.innerWidth / 2 - 125,
				top: window.innerHeight / 2 - 50
			}
		});
	};
	
	module.exports.defaultHandleError = function(data){
		var self = this;
		if(!data.result){
			self.openMessageAlertInCenter("错误",data.errMsg,1200);
			console.error(data.errCode);
		}
	}
	
	module.exports.fixDatagridPageLoader = function($dg,pager){
		 var $pg = $dg.datagrid("getPager");
		 $pg.pagination({
		 	total: pager.totalCount
		 });
	};

});