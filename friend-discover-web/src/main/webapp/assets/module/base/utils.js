/**
 * Js Utils文件
 */
define(function(require, exports, module) {
	
	var Utils = {};
	
	/**
	 * 按照String格式化时间
	 * @param  {String} format 时间模式
	 * @param  {String} str    时间内容
	 * @return {Date}        时间对象
	 */
	Utils.parseDate = function(format, str) {
		var pattern = format.replace("yyyy", "(\\~1{4})")
			.replace("yy", "(\\~1{2})")
			.replace("MM", "(\\~1{2})")
			.replace("M", "(\\~1{1,2})")
			.replace("dd", "(\\~1{2})")
			.replace("d", "(\\~1{1,2})")
			.replace(/~1/g, "d");
		var returnDate;
		if (new RegExp(pattern).test(this)) {
			var yPos = format.indexOf("yyyy");
			var mPos = format.indexOf("MM");
			var dPos = format.indexOf("dd");
			if (mPos == -1)
				mPos = format.indexOf("M");
			if (yPos == -1)
				yPos = format.indexOf("yy");
			if (dPos == -1)
				dPos = format.indexOf("d");
			var pos = new Array(yPos + "y", mPos + "m", dPos + "d").sort();
			var data = {
				y: 0,
				m: 0,
				d: 0
			};
			var m = this.match(pattern);
			for (var i = 1; i < m.length; i++) {
				if (i == 0)
					return;
				var flag = pos[i - 1].split('')[1];
				data[flag] = m[i];
			}
			if (data.y.toString().length == 2) {
				data.y = parseInt("20" + data.y);
			}
			data.m = data.m - 1;
			returnDate = new Date(data.y, data.m, data.d);
		}
		if (returnDate == null || isNaN(returnDate))
			returnDate = new Date();
		return returnDate;
	};
	/**
	 * 格式化时间
	 * @param  {String} format 时间模式
	 * @param  {Date} date   时间对象
	 * @return {String}        时间内容字符串
	 */
	Utils.formatDate = function(format, date) {
		var o = {
			"M+": date.getMonth() + 1, // month
			"d+": date.getDate(), // day
			"h+": date.getHours(), // hour
			"m+": date.getMinutes(), // minute
			"s+": date.getSeconds(), // second
			"q+": Math.floor((date.getMonth() + 3) / 3), // quarter
			"S": date.getMilliseconds() // millisecond
		};
		if (/(y+)/.test(format)) {
			format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		for (var k in o) {
			if (new RegExp("(" + k + ")").test(format)) {
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
			}
		}
		return format;
	};
	
	/**
	 * 验证日期
	 * 判断指定参数是否不是日期格式（不仅仅匹配了日期格式，而且对日期的逻辑做了严格要求，判断了大月31天，小月30天，2月28，闰年情况2月29天
	 */
	Utils.isNotDate = function(date){
	    var reg = /((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))/ig;  
	    if(!reg.test(date)){  
	        return true;  
	    }  
	    return false;  
	};
	
	Utils.trace=function(s) {
		try {
			console.log(s);
		} catch (e) {
//			alert(s);
		}
	};
	
	/**
	 * html代码转码工具
	 */
	Utils.htmlUtil = {
			htmlEncode: function(html) {
				var temElem = document.createElement("div");
				(temElem.textContent != undefined) ? (temElem.textContent = html) : (temElem.innerText = html);
				var output = temElem.innerHTML;
				temElem = null;
				return output;
			},
			
			htmlDecode: function(html) {
				var temElem = document.createElement("div");
				temElem.innerHTML = html;
				var output = temElem.innerText || temElem.textContent;
				temElem = null;
				return output;
			},
			
			htmlEncodeRegx : function(str) {
				 var s = "";
		         if(str.length == 0) return "";
		         s = str.replace(/&/g,"&amp;");
		         s = s.replace(/</g,"&lt;");
		         s = s.replace(/>/g,"&gt;");
		         s = s.replace(/ /g,"&nbsp;");
		         s = s.replace(/\'/g,"&#39;");
		         s = s.replace(/\"/g,"&quot;");
		         return s;
			},
			
			htmlDecodeRegx : function(str, isKeywordsGroup) {
				var s = "";
		        if(str.length == 0) return "";
		        s = str.replace(/&amp;/g,"&");
		        s = s.replace(/&lt;/g,"<");
		        s = s.replace(/&gt;/g,">");
		        s = s.replace(/&nbsp;/g," ");
		        s = s.replace(/&#39;/g,"\'");
		        s = s.replace(/&quot;/g,"\"");
		        return s;
			},
			/**
			 * 处理目标字符串中不符合json格式的特殊字符
			 * @param s
			 * @return
			 */
			solveSpecialCharInJson : function(s) {
				var newstr = "";
				for (var i=0; i<s.length; i++) {
					c = s.charAt(i);
					switch (c) {
						case '\"':
							newstr+="\\\"";
							break;
						case '\\':
							newstr+="\\\\";
							break;
						case '/':
							newstr+="\\/";
							break;
						case '\b':
							newstr+="\\b";
							break;
						case '\f':
							newstr+="\\f";
							break;
						case '\n':
							newstr+="\\n";
							break;
						case '\r':
							newstr+="\\r";
							break;
						case '\t':
							newstr+="\\t";
							break;
						default:
							newstr+=c;
					}
				}
				return newstr;
			}
};
	
	/**
	 * 对表单的所有输入元素内容进行转码
	 * @param $form
	 */
	Utils.encodeFormInput = function($form) {
		if(!$form) {
			return;
		}
		$form.find('input').each(function() {
			if($(this).attr('name') != 'keywordsGroup') {//关键字组里面的特殊字符忽略
				var val = $(this).val();
				val = Utils.htmlUtil.htmlEncode(val);
				$(this).val(val);
			}
		})
	};
	/**
	 * 对表单的所有输入元素内容进行解码
	 * @param $form
	 */
	Utils.decodeFormInput = function($form) {
		if(!$form) {
			return;
		}
		//siblings('span.textbox').children('input.textbox-value').
		$form.find('.easyui-textbox').each(function(index, domElem) {
			if($(domElem).attr('textboxname') != 'keywordsGroup') {//关键字组里面的特殊字符忽略
				var val = $(domElem).textbox('getValue');
				val = Utils.htmlUtil.htmlDecode(val);
				$(domElem).textbox('setValue', val);
			} 
		})
	};
	/**
	 * 是否含有中文（也包含日文和韩文）
	 */
	Utils.isChineseChar = function(str){   
		var reg = /[\u4E00-\u9FA5\uF900-\uFA2D]/;
	   	return reg.test(str);
	};
	/**
	 * 是否含有全角符号的函数
	 */
	Utils.isFullWidthChar = function(str){
		var reg = /[\uFF00-\uFFEF]/;
		return reg.test(str);
	};
	/**
	 * 是否含有特殊字符（包括中文和英文的特殊字符）
	 */
	Utils.containsSpecChar = function(str) {//除去“(”、“)”\“&”、“|”
		var reg = new RegExp("[`~!@#$^*={}':;',\\[\\].<>/?~！@#￥……*——{}【】‘；：”“'。，、？]"); 
		//完整的特殊字符正则：[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？]
		return reg.test(str);
	};
	/**
	 * 是否含有英文字母或数字
	 */
	Utils.containsEnglishAndNo = function(str) {
		var reg = new RegExp("[a-zA-Z0-9]");
		return reg.test(str);
	};
	/**
	 * 是否为纯汉字（关键字组）
	 */
	Utils.isWholeChineseChar = function(str) {
		if(Utils.isChineseChar(str) && !Utils.containsSpecChar(str) 
				&& !Utils.containsEnglishAndNo(str)) {
			return true;
		} else
			return false;
	};
	/**
	 * 是否为纯汉字（关键字组）
	 */
	Utils.isAbsoluteChineseChar = function(str) {
		var reg = /[\u4E00-\u9FA5\uF900-\uFA2D()\\|&]/g;
		var reLen = str.replace(reg, '');
		if(reLen == 0) {
			return true;
		} else {
			return false;
		}
	};
	/**
	 * 计算指定字符串长度（编码：UTF-8，汉字占3个字节，英文或特殊字符占1个)
	 */
	Utils.calcLength4UTF8 = function(str) {
		var len = 0;
		for(var i in str) {
			var currChar = str[i];
			if(Utils.isChineseChar(currChar)) {
				len += 3;
			} else {
				len ++;
			}
		}
		return len;
	};
	
	/**
	 * 获取一个随机颜色，返回其16进制编码
	 */
	Utils.getRandomColor = function(){ 
		return "#"+("00000"+((Math.random()*16777215+0.5)>>0).toString(16)).slice(-6); 
	} ;
	
	/**
	 * 获取指定颜色的反色
	 */
	Utils.getOppositeColor = function(color) {
	    color = color.replace('#','');
	    var c16, c10, max16 = 15, b = [];
	    for(var i=0; i<color.length; i++){   
	        c16 = parseInt(color.charAt(i), 16);//转换为16进制
	        c10 = parseInt(max16-c16, 10);//将反色（最大（15）减去当前）转换为10进制
	        b.push(c10.toString(16)); // to 16进制
	    }
	    return '#'+b.join('');
	};
	
	/**
	 * 是否为深色
	 */
	Utils.isDarkColor = function(color) {
		color = color.replace('#','');
		//转换为10进制
		var c16, c10, b = [];
		for(var i=0; i<color.length; i++){   
	        c16 = parseInt(color.charAt(i), 16);//转换为16进制
	        c10 = parseInt(c16, 10);//将反色（最大（15）减去当前）转换为10进制
	        b.push(c10); // to 16进制
	    }
		//分别获取R、G、B的值
		var R, G, B;
		if(b.length == 3) {//3位
			R = b[0]*b[0];
			G = b[1]*b[1];
			B = b[2]*b[2];
		} else {//6位
			R = b[0]*b[1];
			G = b[2]*b[3];
			B = b[4]*b[5];
		}
		var grayLevel = R * 0.299 + G * 0.587 + B * 0.114;//求灰阶
//		console.log("当前颜色： %c #" + color + ', 灰阶：' + grayLevel, 'color:#' + color);
		if (grayLevel >= 80) {//浅（亮）色
			return false;
		} else {//深（暗）色
			return true;
		}
	};

	/**
	 * 对指定字符串里面的所有需要转义的特殊字符进行转义,返回处理后的字符串
	 * @param target
	 */
	Utils.escapeString = function(str) {
		if(str) {
			if(str.indexOf('*') != -1) {
				str = str.replace(/\*/g, '\\\*');
			}
			if(str.indexOf('|') != -1) {
				str = str.replace(/\|/g, '\\\|');
			}
			if(str.indexOf('+') != -1) {
				str = str.replace(/\+/g, '\\\+');
			}
			if(str.indexOf('(') != -1) {
				str = str.replace(/\(/g, '\\\(');
			}
			if(str.indexOf(')') != -1) {
				str = str.replace(/\)/g, '\\\)');
			}
		}
		return str;
	};

	/**
	 * 生成uuid
	 * @returns {string}
     */
	Utils.generateUUID = function() {
		var d = new Date().getTime();
		var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			var r = (d + Math.random()*16)%16 | 0;
			d = Math.floor(d/16);
			return (c=='x' ? r : (r&0x3|0x8)).toString(16);
		});
		return uuid;
	};

	/**
	 * 将文件input中的图片显示在指定img元素上(图片预览）
	 * @param imgToShowSelector
	 * @param fileInputSelector
     */
	Utils.previewImg  = function(imgToShowSelector, fileInputSelector) {
		var preview = document.querySelector(imgToShowSelector);
		var file  = document.querySelector(fileInputSelector).files[0];
		var reader = new FileReader();
		reader.onloadend = function () {
			preview.src = reader.result;
		}
		if(file) {
			reader.readAsDataURL(file);
		} else {
			preview.src = "";
		}
	};
	Utils.previewImgOverride1  = function(imgToShowSelector, fileInputElem) {
		var preview = document.querySelector(imgToShowSelector);
		var file  = fileInputElem[0].files[0];
		var reader = new FileReader();
		reader.onloadend = function () {
			preview.src = reader.result;
		}
		if(file) {
			reader.readAsDataURL(file);
		} else {
			preview.src = "";
		}
	};
	/**
	 * 判断鼠标是否在指定选择器的元素边界内
	 * @param selector
	 * @returns {boolean}
     */
	Utils.isMouseInElem = function(selector)
	{
		if( $(selector).length ) {
			console.warn('找不到元素：' + selector);
			return false;
		}
		var wx = window.event.clientX;
		var wy = window.event.clientY;
		var d_left = $(selector)[0].offsetLeft;
		var d_top = $(selector)[0].offsetTop;
		var d_width = $(selector)[0].clientWidth;
		var d_height = $(selector)[0].clientHeight;
		if(wx < d_left || wy < d_top || wx > (d_left + d_width) || wy > (d_top + d_height))
			$(selector)[0].style.display = "none";
		return false;
	};

	/**
	 * 上传文件夹事件初始化话
	 * @type {{}}
     */
	Utils.initUploadDir = function($jqObj) {
		console.log('initUploadDir');
		(function(){
			var cache = {};

			this.tmpl = function tmpl(str, data) {
				// Figure out if we're getting a template, or if we need to
				// load the template - and be sure to cache the result.
				var fn = !/\W/.test(str) ?
						cache[str] = cache[str] ||
								tmpl(document.getElementById(str).innerHTML) :

					// Generate a reusable function that will serve as a template
					// generator (and which will be cached).
						new Function("obj",
								"var p=[],print=function(){p.push.apply(p,arguments);};" +

									// Introduce the data as local variables using with(){}
								"with(obj){p.push('" +

									// Convert the template into pure JavaScript
								str
										.replace(/[\r\t\n]/g, " ")
										.split("<%").join("\t")
										.replace(/((^|%>)[^\t]*)'/g, "$1\r")
										.replace(/\t=(.*?)%>/g, "',$1,'")
										.split("\t").join("');")
										.split("%>").join("p.push('")
										.split("\r").join("\\'")
								+ "');}return p.join('');");

				// Provide some basic currying to the user
				return data ? fn( data ) : fn;
			};
		})();

		window.URL = window.URL ? window.URL :
				window.webkitURL ? window.webkitURL : window;

		function Tree(selector) {
			this.$el = $(selector);
			this.fileList = [];
			this.folderCnt = 0;
			var html_ = [];
			var tree_ = {};
			var pathList_ = [];
			var self = this;

			this.render = function(object) {
				if (object) {
					for (var folder in object) {
						if (!object[folder]) { // file's will have a null value
							html_.push('<li><a href="javascript:void(0)" data-type="file">', folder, '</a></li>');
						} else {
							this.folderCnt++;
							html_.push('<li><a href="javascript:void(0)">', folder, '</a>');
							html_.push('<ul>');
							self.render(object[folder]);
							html_.push('</ul>');
						}
					}
				}
			};

			this.buildFromPathList = function(paths) {
				for (var i = 0, path; path = paths[i]; ++i) {
					var pathParts = path.split('/');
					var subObj = tree_;
					for (var j = 0, folderName; folderName = pathParts[j]; ++j) {
						if (!subObj[folderName]) {
							subObj[folderName] = j < pathParts.length - 1 ? {} : null;
						}
						subObj = subObj[folderName];
					}
				}
				return tree_;
			}

			this.init = function(e) {
				// Reset
				html_ = [];
				tree_ = {};
				pathList_ = [];
				self.fileList = e.target.files;

				// TODO: optimize this so we're not going through the file list twice
				// (here and in buildFromPathList).
				for (var i = 0, file; file = self.fileList[i]; ++i) {
					pathList_.push(file.webkitRelativePath);
				}

				self.render(self.buildFromPathList(pathList_));

				self.$el.html(html_.join('')).tree({
					expanded: 'li:first'
				});
				$('#path_cnt').text(self.folderCnt);
				$('#file_cnt').text(self.fileList.length);
				//$('.tree ul').each(function(){
				//	var pTreeUlCnt = $(this).parents('.tree ul').length + 1;
				//	console.log($(this).siblings('a').text() + ' , ' + pTreeUlCnt);
				//	$(this).css('margin-left', pTreeUlCnt*8 + 'px');
				//});
				// Add full file path to each DOM element.
				$('.tree ul').css('margin-left', '8px');
				var fileNodes = self.$el.get(0).querySelectorAll("[data-type='file']");
				for (var i = 0, fileNode; fileNode = fileNodes[i]; ++i) {
					fileNode.dataset['index'] = i;
				}
			}
		};

		var tree = new Tree('#dir-tree');

		$jqObj.change(tree.init);

		// Initial resize to force scrollbar in when file loads//滚动条
		$('#container div:first-of-type').css('height', (document.height - 20) + 'px');
		window.addEventListener('resize', function(e) {
			$('#container div:first-of-type').css('height', (e.target.innerHeight - 20) + 'px');
		});
	}
	module.exports = Utils;
});
	