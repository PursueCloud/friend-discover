/**
 * Admin页面控制，后台管理的入口页面，提供父Frame的脚本
 */
define(function(require, exports, module) {
	var base = require("module/base/main.js");
	var extend = require("module/base/extend.js");
	var menuUrl = "admin/getMenu";

	var curTop=35;
	var h=70;
	var curPos=0;
	var $navSlider=$("#nav_slider");

	var AdminMain = function() {
		this.$tabs;
	};

	AdminMain.prototype = {
		constructor : AdminMain,
		init:function(){
			var self=this;

			self.$tabs = $(".js-tabs");
			render(self);
			this.initTabMenu();
		},
		/**
		 * 初始化切页的右键菜单
		 *
		 */
		initTabMenu:function(){
			var self=this;

			//双击关闭TAB选项卡
			$("ul.tabs").on("dblclick","li",function(e){
				var tab = self.$tabs.tabs('getSelected');
				var index = self.$tabs.tabs('getTabIndex',tab);
				self.$tabs.tabs('close',index);
			});
			//右键事件
			self.$tabs.tabs({
				//onAdd: function(title, index) {
				//	console.log('A new tab is added just now!');
				//	$('div.tabs-panels iframe').contents().find('body *').bind('click', function(e) {
				//		console.log('click new tab div');
				//		$('#tab_menu').menu('hide');
				//	});
				//},
				onContextMenu:function(e, title, index){
					e.preventDefault();
					self.currTabIndex = index;
					$('#tab_menu').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
				}
			});
			//刷新当前选项卡
			$("#refresh_this").on('click',function(){
				//var tab = self.$tabs.tabs('getSelected');
				var index = self.currTabIndex;
				var tabs =  self.$tabs.tabs('tabs');
				var tab = tabs[index];
				self.$tabs.tabs('update',{
					tab:tab,
					options:{
						content:$(tab.panel('options').content)
					}
				});
			});
			//关闭鼠标所在选项卡
			$("#close_this").on("click",function(){
				//var tab = self.$tabs.tabs('getSelected');
				//var index = self.$tabs.tabs('getTabIndex',tab);
				var index = self.currTabIndex;
				self.$tabs.tabs('close',index);
			});
			//关闭全部选项卡
			$("#close_all").on("click",function(){
				var tablist = self.$tabs.tabs('tabs');
				for(var i=tablist.length-1;i>=0;i--){
					self.$tabs.tabs('close',i);
				}
			});
			//关闭其他选项卡（先关闭右侧，再关闭左侧）
			$("#close_others").on("click",function(){
				var tablist = self.$tabs.tabs('tabs');
				//var tab = self.$tabs.tabs('getSelected');
				//var index = self.$tabs.tabs('getTabIndex',tab);
				var index = self.currTabIndex;
				for(var i=tablist.length-1;i>index;i--){
					self.$tabs.tabs('close',i);
				}
				var num = index-1;
				for(var i=num;i>=0;i--){
					self.$tabs.tabs('close',0);
				}
			});
			//关闭鼠标所在选项卡右侧选项卡
			$("#close_right").on("click",function(){
				var tablist = self.$tabs.tabs('tabs');
				//var tab = self.$tabs.tabs('getSelected');
				//var index = self.$tabs.tabs('getTabIndex',tab);
				var index = self.currTabIndex;
				for(var i=tablist.length-1;i>index;i--){
					self.$tabs.tabs('close',i);
				}
			});
			//关闭鼠标所在选项卡左侧选项卡
			$("#close_left").on("click",function(){
				//var tab = self.$tabs.tabs('getSelected');
				//var index = self.$tabs.tabs('getTabIndex',tab);
				var index = self.currTabIndex;
				var num = index-1;
				for(var i=0;i<=num;i++){
					self.$tabs.tabs('close',0);
				}
			});
		},
	};
	module.exports = new AdminMain();
	
	
	function render(target){
		loadMenus(target);
		$("#nav_menu").on("mouseenter mouseleave",".b-menu-item ",function(e){
			var self=$(this);
			var i=self.index();
			var length=self.siblings(".b-menu-item").length;
			var toTop=curTop+i*h;
			
			if(e.type === "mouseenter"){
				var t=self.offset();
				var allH=t.top+self.find(".sub-menu").height();
				var windowH=$(window).height();
				$navSlider.animate({top:toTop},80,function(){
					if(allH>windowH){
						self.find(".sub-menu").css("bottom",h*(length-i));
					}
					self.siblings(".b-menu-item.menu-hover").removeClass("menu-hover");
					self.addClass("menu-hover");				
				}).clearQueue();
			}else{
				self.removeClass("menu-hover");
				$navSlider.animate({top:curTop+curPos*h},80,function(){
					self.removeClass("menu-hover");
					self.find(".sub-menu").css("bottom","auto");
				}).clearQueue();
			}	
			
		});

		$("#nav_menu").on("click",".sub-menu",function(){
			var self=$(this);
			self.parent(".b-menu-item").removeClass("menu-hover");
			curPos=self.parent(".b-menu-item").index();
		});
	}
	
	/**
	 * 加载菜单
	 */
	function loadMenus(target){
		var tplLi="<li class='b-menu-item'>" +
					"<a href='javascript:void(0);'>" +
						"<img src='assets/images/admin/menu_1.png'>" +
						"<span></span>" +
					"</a>" +
				   "</li>";
		var tplSub="<div class='sub-menu'><ul></ul></div>";
		var tplSubLi="<li><a href='javascript:void(0);'><span></span></a></li>";
		$.getJSON(menuUrl,function(resp){
			for(var i=0;i<resp.length;i++){
				var m=resp[i];
				var $li=$(tplLi);
				$li.find("a").data("node",m);
				$li.find("span").html(m.text);
				$li.find("img").attr("src","assets/images/admin/menu_"+(i%5+1)+".png");
				if(m.children != null && m.children.length > 0){
					var $sub=$(tplSub);
					$li.append($sub);
					for(var k=0;k<m.children.length;k++){
						var sm=m.children[k];
						var $subLi=$(tplSubLi);
						$subLi.find("a").data("node",sm);
						$subLi.find("span").html(sm.text);
						$sub.find("ul").append($subLi);
					}
				}
				$("#nav_menu").append($li);
			}
		});
		$("#nav_menu").on("click",".sub-menu a",function(e){
			var self=$(this);
			var node=self.data("node");
			base.openAjaxTab(target.$tabs, node.id, node.text, node.url);
		});
	}
});