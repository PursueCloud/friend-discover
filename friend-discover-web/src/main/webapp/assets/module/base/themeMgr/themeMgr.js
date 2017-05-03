define(function(require, exports, module) {
	
	var ThemeMgr = function() {
		
	}
	
	ThemeMgr.prototype = {
		constructor: ThemeMgr,
		/**
		 * 初始化echarts主题选择元素控件及其事件
		 * @param selector 待初始化元素的选择器字符串
		 */
		initThemeSelect : function(myChart, selector, callback) {
			var self = this;
			
//			var enVersion = location.hash.indexOf('-en') != -1;
//			var hash = location.hash.replace('-en','');
//			hash = hash.replace('#','') || (needMap() ? 'default' : 'macarons');
//			hash += enVersion ? '-en' : '';

			var curTheme;
			function requireCallback (ec, defaultTheme) {
			    curTheme = themeSelector ? defaultTheme : {};
			    echarts = ec;
			    refresh();
			    window.onresize = myChart.resize;
			}
//			function needMap() {
//		        var href = location.href;
//		        return href.indexOf('map') != -1
//		               || href.indexOf('mix3') != -1
//		               || href.indexOf('mix5') != -1
//		               || href.indexOf('dataRange') != -1;
//		    }
//			var themeSelector = $('#theme-select');
			var themeSelector = $(selector);
			if (themeSelector) {
			    themeSelector.html(
		    		'<option name="default" selected >default</option>'
			        + '<option name="macarons">macarons</option>'
			        + '<option name="infographic">infographic</option>'
			        + '<option name="shine">shine</option>'
			        + '<option name="dark">dark</option>'
			        + '<option name="blue">blue</option>'
			        + '<option name="green">green</option>'
			        + '<option name="red">red</option>'
			        + '<option name="gray">gray</option>'
			        + '<option name="helianthus">helianthus</option>'
			        + '<option name="roma">roma</option>'
			        + '<option name="mint">mint</option>'
			        + '<option name="macarons2">macarons2</option>'
			        + '<option name="sakura">sakura</option>'
			    );
			    $(themeSelector).on('change', function(){
			        selectChange($(this).val());
			    });
			    function selectChange(value){
			        var theme = value;
			        myChart.showLoading();
//			        $(themeSelector).val(theme);
			        if (theme != 'default') {
//			            window.location.hash = value + (enVersion ? '-en' : '');
		                setTimeout(function() {
			            	refreshTheme(value)
			            }, 500);
			        }
			        else {
			            window.location.hash = enVersion ? '-en' : '';
			            curTheme = {};
			            setTimeout(function() {
			            	refreshTheme(value)
			            }, 500);
			        }
			    }
			    function refreshTheme(theme){
			        myChart.hideLoading();
//			        myChart.dispose();
			        //获取主题对象并调用回调函数设置主题
			        var themeObj = self.getThemeObjByName(theme);
			        callback(themeObj);
			    }
//			    if ($(themeSelector).val(hash.replace('-en', '')).val() != hash.replace('-en', '')) {
//			        $(themeSelector).val('macarons');
//			        hash = 'macarons' + enVersion ? '-en' : '';
//			        window.location.hash = hash;
//			    }
			}
		},
		
		/**
		 * 根据主题名获取主题对象
		 * @param themeName
		 * @returns
		 */
		getThemeObjByName : function(themeName) {
			var themeObj = undefined;
			if('red' == themeName) {
				themeObj = require('module/base/themeMgr/theme/red.js');
			} else if('blue' == themeName) {
				themeObj = require('module/base/themeMgr/theme/blue.js');
			} else if('dark' == themeName) {
				themeObj = require('module/base/themeMgr/theme/dark.js');
			} else if('default' == themeName) {
				themeObj = require('module/base/themeMgr/theme/default.js');
			} else if('gray' == themeName) {
				themeObj = require('module/base/themeMgr/theme/gray.js');
			} else if('green' == themeName) {
				themeObj = require('module/base/themeMgr/theme/green.js');
			} else if('helianthus' == themeName) {
				themeObj = require('module/base/themeMgr/theme/helianthus.js');
			} else if('infographic' == themeName) {
				themeObj = require('module/base/themeMgr/theme/infographic.js');
			} else if('macarons' == themeName) {
				themeObj = require('module/base/themeMgr/theme/macarons.js');
			} else if('macarons2' == themeName) {
				themeObj = require('module/base/themeMgr/theme/macarons2.js');
			} else if('mint' == themeName) {
				themeObj = require('module/base/themeMgr/theme/mint.js');
			} else if('roma' == themeName) {
				themeObj = require('module/base/themeMgr/theme/roma.js');
			} else if('sakura' == themeName) {
				themeObj = require('module/base/themeMgr/theme/sakura.js');
			} else if('shine' == themeName) {
				themeObj = require('module/base/themeMgr/theme/shine.js');
			}
				
			return themeObj;
		},
	}
	
	module.exports = new ThemeMgr();
});
