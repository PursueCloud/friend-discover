/**
 * 数据维护模块js
 */
define(function(require, exports, module) {
    var base = require("module/base/main.js");
    var extend = require("module/base/extend.js");
    var utils = require("module/base/utils.js");

    var pageRootUrl = 'pages/dataKeep/';
    var requestRootUrl = 'dataKeep/';
    //page urls
    var importDataPageUrl = pageRootUrl + 'import.jsp';//导入数据页面url
    var editCustomUserPageUrl = pageRootUrl + 'editCustomUser.jsp';//编辑用户属性页面url
    var editHaConstPageUrl = pageRootUrl + 'editHaConst.jsp';//编辑hadoop配置页面url
    var showUserColsPageUrl = pageRootUrl + 'showUserCols.jsp';//显示当前用户属性页面选中的列页面url
    var showHadoopColsPageUrl = pageRootUrl + 'showHadoopCols.jsp';//显示当前hadoop配置页面选中的列页面url
    //request urls
    var existsTableRequestUrl = requestRootUrl + 'existsTable';//查询表是否已存在请求url
    var createTableRequestUrl = requestRootUrl + 'createTable';//创建表请求url
    var getTableFieldsDataRequestUrl = requestRootUrl + 'getTableFieldsData';//根据tableCode获取表的搜索项数据请求url
    var getDataByConditionsRequestUrl = requestRootUrl + 'getDataByConditions';//根据搜索项和对应的搜索内容获取数据请求url
    var addDataRequestUrl = requestRootUrl + 'add';//新增数据请求url
    var editDataRequestUrl = requestRootUrl + 'update';//修改数据请求url
    var delDataRequestUrl = requestRootUrl + 'delete';//删除数据请求url
    var exportDataRequestUrl = requestRootUrl + 'export';//导出数据请求url

    var self;

    function checkIsChooseImgFile(fileInputSelector, paramJson) {//检查当前选中的文件是否为图片文件（png/jpg/jpeg）
        var fileInputDom = !fileInputSelector ? $('td[field="icon"] input[type="file"]')[0] : $(fileInputSelector)[0];
        var iconFile = fileInputDom.files[0];//获取上传图片文件
        if( iconFile ) {
            var fileSuffix = iconFile.name.substring(iconFile.name.lastIndexOf('.')+1);//获取选中文件后缀
            if( fileSuffix!='png' && fileSuffix!='jpg' && fileSuffix!='jpeg') {
                $.messager.alert('温馨提示', '只能选择png/jpg/jpeg格式文件作为头像！', 'warning');
                fileInputDom.files[0] = undefined;//清空选中的文件
                if( paramJson && paramJson.fail ) {
                    console.log('check img failed');
                    paramJson.fail();
                }
                return false;
            }
            if( paramJson && paramJson.success ) {
                console.log('check img success');
                paramJson.success();
            }
        }
        return true;
    }
    var defaultUserCols = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14];//默认列
    var defaultHadoopCols = [0, 1, 2, 3, 4];//默认hadoop配置列
    //json对象: datagrid columns
    var userColsJson = {
        '-1' : {colId:'-1', field:'userId', checkbox:true},
        '0' : {colId:'0', field:'displayName', title:'昵称', width:150, align:'center',sortable:true,
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }
        },
        '1' : {colId:'1', field: 'aboutMe',title: '个人简介',width: 200, align:'center',
            formatter: function(val, row, rowIndex) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                return '<div title="' + val + '">' + text + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:false,
                }
            }
        },
        '2' : {colId:'2', field:'icon', title:'头像', width:100, align:'center',
            formatter: function(val, row, index){
                if( !val ) {
                    return '无';
                }
                if(val.indexOf('fakepath') != -1) {
                    return '';
                }
                return '<img id="formatter_icon_img" src="' + val + '" style="margin:auto;width:80px;height:80px;border-radius:10px;border:1px solid #999;vertical-align: middle"/>';
            },
            editor : {
                type:'filebox',
                options: {
                    required:false,
                    onChange: function() {
                        checkIsChooseImgFile();
                    }
                },
            }
        },
        '3' : {colId:'3', field: 'emailHash',title: '邮箱hash',width: 100, align:'center',
            formatter: function(val, row, index){
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }
        },
        '4' : {colId:'4', field: 'sex',title: '性别',width: 80, align:'center',
            formatter: function(val, row, index){
                switch(val) {
                    case 0: return '男';
                    case 1: return '女';
                    default: return '未知';
                }
            },
            editor : {
                type:'combobox',
                options: {
                    required:false,
                    valueField: 'value',
                    textField: 'text',
                    value: [{value:'0', text:'男'}, {'value':'1', text:'女'}]
                }
            }
        },
        '5' : {colId:'5', field:'age', title:'年龄', width:80, align:'center',sortable:true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        '6' : {colId:'6', field:'location', title:'常驻地', width:80, align:'center',
            editor : {
                type:'textbox',
                options: {
                    required:false,
                }
            }
        },
        '7' : {colId: '7', field: 'websiteUrl', title: '个人主页', width: 150, align: 'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    validType: 'url',
                    required:false,
                }
            }
        },
        '8' : {colId:'8', field:'upVotes', title:'赞同数', width:80, align:'center',sortable:true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        '9' : {colId:'9', field:'downVotes', title:'反对数', width:80, align:'center',sortable:true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        '10' : {colId:'10', field:'reputation', title:'声望值', width:80, align:'center',sortable:true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        '11' : {colId:'11', field: 'views',title: '浏览数',width: 80, align:'center',sortable:true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        '12' : {colId:'12', field: 'createDate',title: '创建时间',width: 130, align:'center',sortable:true},
        '13' : {colId:'13', field:'modifyDate', title:'修改时间', width:130, align:'center',sortable:true},
        '14' : {colId:'14', field:'lastAccessDate', title:'最后一次访问时间', width:130, align:'center',sortable:true},
    };
    var hadoopColsJson = {
        '-1' : {colId:'-1', field:'constId', checkbox:true},
        '0' : {colId:'0', field: 'constKey', title: '配置项名', width: '200', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbpx',
                options: {
                    required:true,
                }
            }

        },
        '1' : {colId:'1', field: 'constValue', title: '配置项值', width: '150', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }

        },
        '2' : {colId:'2', field: 'description', title: '备注说明', width: '200', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:false,
                }
            }

        },
        '3' : {colId:'3', field: 'createDate',title: '创建时间',width: 130, align:'center',sortable:true},
        '4' : {colId:'4', field:'modifyDate', title:'修改时间', width:130, align:'center',sortable:true},
    };
    //默认datagrid columns
    var userGridColums = [[
        {field:'userId', checkbox:true},
        {field: 'displayName', title: '昵称', width: '150', align:'center', sortable:true,
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }
        },
        {field: 'aboutMe', title: '个人简介', width: '200', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                return '<div title="' + val + '">' + text + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:false,
                }
            }
        },
        {field: 'icon', title: '头像', width: '100', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                if(val.indexOf('fakepath') != -1) {
                    return '';
                }
                return '<img id="formatter_icon_img" src="' + val + '" style="margin:auto;width:80px;height:80px;border-radius:10px;border:1px solid #999;vertical-align: middle"/>';
            },
            editor : {
                type:'filebox',
                options: {
                    required:false,
                    onChange: function() {
                        checkIsChooseImgFile();
                    }
                },
            }
        },
        {field: 'emailHash', title: '邮箱hash', width: '100', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }
        },
        {field: 'sex', title: '性别', width: '80', align:'center',
            formatter: function(val, row, index) {
                switch(val) {
                    case 0: return '男';
                    case 1: return '女';
                    default: return '未知';
                }
            },
            editor : {
                type:'combobox',
                options: {
                    valueField: 'value',
                    textField: 'text',
                    value: [{value:'0', text:'男'}, {value:'1', text:'女'}],
                    required:false,
                }
            }
        },
        {field: 'age', title: '年龄', width: '80',  align:'center',sortable: true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        {field: 'location', title: '常驻地', width: '80',  align:'center',
            editor : {
                type:'textbox',
                options: {
                    required:false,
                }
            }
        },
        {field: 'websiteUrl', title: '个人主页',  align:'center',width: '150',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    validType: 'url',
                    required:false,
                }
            }

        },
        {field: 'upVotes', title: '赞同数', width: '80',  align:'center',sortable: true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        {field: 'downVotes', title: '反对数', width: '80',  align:'center',sortable: true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        {field: 'reputation', title: '声望值', width: '80',  align:'center',sortable: true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        {field: 'views', title: '浏览数', width: '80',  align:'center',sortable: true,
            editor : {
                type:'numberspinner',
                options: {
                    required:false,
                }
            }
        },
        {field: 'createDate', title: '创建时间', width: '130',  align:'center',sortable: true},
        {field: 'modifyDate', title: '修改时间', width: '130',  align:'center',sortable: true},
        {field: 'lastAccessDate', title: '最后一次访问时间', width: '130',  align:'center',sortable: true},
    ]];
    var hadoopGridColums = [[
        {field:'constId', checkbox:true},
        {field: 'constKey', title: '配置项名', width: '200', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }

        },
        {field: 'constValue', title: '配置项值', width: '150', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:true,
                }
            }

        },
        {field: 'description', title: '备注说明', width: '200', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            },
            editor : {
                type:'textbox',
                options: {
                    required:false,
                }
            }

        },
        {field: 'createDate', title: '创建时间', width: '130',  align:'center',sortable: true},
        {field: 'modifyDate', title: '修改时间', width: '130',  align:'center',sortable: true},
    ]];

    var DataKeep = function() {
        this.$tablegrid;
        this.$dialog;
        this.$form;
        this.$toolbar;
        this.$searchbox;
        this.footButtons = [{
            text: '保存',
            iconCls: 'icon-save',
            handler: function() {
                var tableCode = $('#keepData_table_select').combobox('getValue');
                self.$form.find('input#table_code').val(tableCode)
                self.$form.form('submit', {
                    onSubmit: function() {
                        if( self.$form.form('validate') ) {
                            return checkIsChooseImgFile(self.$form.find('input[name="iconFile"]'));
                        } else {
                            $.messager.alert('温馨提示', '必输项不能为空！', 'warning');
                            return false;
                        }
                    },
                    success: function(resp) {
                        resp = JSON.parse(resp);
                        var opName = self.opType=='add' ? '新增' : '修改';
                        if( resp.meta.success ) {
                            $.messager.alert('温馨提示', opName + '成功', 'info', function() {
                                self.$dialog.dialog('close');
                                self.$tablegrid.datagrid('reload');
                            });
                        } else {
                            if( resp.meta.message ) {
                                $.messager.alert('温馨提示', resp.meta.message, 'info');
                            } else {
                                $.messager.alert('温馨提示', opName + '失败', 'info');
                            }
                        }
                    }
                })
            },
        },{
            text: '退出',
            iconCls: 'icon-cancel',
            handler: function() {
                self.$dialog.dialog('close');
            },
        }];
    };

    DataKeep.prototype = {
        constructor: DataKeep,

        initTable: function() {
            this.initSelfAndDialog();
            self.initInitTablePageEvent();
        },
        initInitTablePageEvent: function() {
            this.initSelfAndDialog();

            $('a#init_tab_a').unbind('click');
            $('a#init_tab_a').bind('click', function() {
                //alert('创建表。。。');
                var params = {
                    'tableCode': $('#init_tables_select').combobox('getValue')
                }
                if( !params.tableCode ) {
                    $.messager.alert('温馨提示', '请先选择一张表', 'warning');
                    return false;
                }
                var tableName = params.tableCode==0 ? '“用户属性表”' : '“Hadoop集群配置表”';
                //检测表是否已存在并根据用户选择创建表（如果选中的表不存在，则直接创建）
                self.sendAjaxReq({
                    url: existsTableRequestUrl,
                    params: params,
                    successCallback: function() {
                        $.messager.confirm('温馨提示', '系统检测到该表已存在，继续创建将删除该表并重新创建，是否继续？', function(r) {
                            if(r) {
                                var progInterval = self.startProgress({
                                    'wrapperSelector': 'div#prog_div',
                                    'progressbarSelector': 'div#init_tab_prog',
                                });
                                initTab(function() {
                                    self.finishProgress({
                                        'progInterval': progInterval,
                                        'progressbarSelector': 'div#init_tab_prog',
                                    });
                                });
                            }
                        });
                    },
                    failedCallback: function() {
                        var progInterval = self.startProgress({
                            'wrapperSelector': 'div#prog_div',
                            'progressbarSelector': 'div#init_tab_prog',
                        });
                        initTab(function() {
                            self.finishProgress({
                                'progInterval': progInterval,
                                'progressbarSelector': 'div#init_tab_prog',
                            });
                        });
                    }
                });

                function initTab(responseCallback) {
                    self.sendAjaxReq({
                        url: createTableRequestUrl,
                        params: params,
                        successMsg: tableName + '表初始化成功',
                        errorMsg: '很抱歉，系统出错，无法初始化' + tableName + '表！',
                        responseCallback: responseCallback,
                    })
                }
                return false;
            });

        },
        /**
         *
         * @param json
         * {
         *  wrapperSelector: progressbar父容器选择器
         *  progressbarSelector: progressbar选择器
         * }
         */
        startProgress: function(json) {
            $(json.wrapperSelector).show();
            var $progBar = $(json.progressbarSelector).progressbar({value:0});
            var progInterval = setInterval(function() {
                var nextVal = $progBar.progressbar('getValue') + 20;
                if( nextVal >= 90 ) {
                    nextVal = 90;
                }
                $progBar.progressbar('setValue', nextVal);
            }, 500);
            return progInterval;
        },
        /**
         *
         * @param json
         * {
         *  progInterval: progressbar自动增长interval
         *  progressbarSelector: progressbar选择器
         * }
         */
        finishProgress: function(json) {
            if( json.progInterval ) {
                clearInterval(json.progInterval);
            }
            $(json.progressbarSelector).progressbar('setValue', 100);
        },
        /**
         * ajax请求方法（默认返回值类型为OperaterResult
         * @param json json类型参数，其中的key值如下
         * {
         *      type: 请求类型
         *      url: 请求url
         *      params: 参数
         *      dataType: 返回的数据类型（如果有）
         *      successMsg: resp.meta.success为true时提示的消息
         *      errorMsg: resp.meta.success为false时提示的消息
         *      responseCallback: 获取到返回值时执行的回调函数
         *      successCallback: resp.meta.success为true时执行的回调函数, 回调函数有参数resp
         *      failedCallback: resp.meta.success为false时执行的回调函数, 回调函数有参数resp
         *      callback: 当返回值不为OperaterResult类型时执行的回调函数, 回调函数有参数resp
         * }
         */
        sendAjaxReq: function(json) {
            if( !json || !json.url ) {
                console.log('参数不能为空');
                return;
            }
            $.ajax({
                type: json.type ? json.type: 'get',//默认get方式发送请求
                url: json.url,
                data: json.params,
                dataType: json.dataType ? json.dataType : 'json',
                success: function(resp) {
                    if( json.responseCallback ) {
                        json.responseCallback(resp);
                    }
                    if( resp ) {
                        if( resp.meta!==undefined && resp.data!==undefined ) {//返回值属于OperaterResult类型
                            if( resp.meta.success ) {
                                if( json.successCallback ) {//resp.meta.success为true时执行该回调函数
                                    json.successCallback(resp);
                                } else {
                                    if( resp.meta.message ) {
                                        $.messager.alert('温馨提示', resp.meta.message, 'info');
                                    } else {
                                        $.messager.alert('温馨提示', json.successMsg ? json.successMsg : '请求成功', 'info');
                                    }
                                }
                            } else {
                                if( json.failedCallback ) {//resp.meta.success为false时执行该回调函数
                                    if( !resp.meta.message ) {
                                        json.failedCallback(resp);
                                    } else {
                                        $.messager.alert('温馨提示', resp.meta.message, 'error');//出现异常
                                    }
                                } else {
                                    if( resp.meta.message ) {
                                        $.messager.alert('温馨提示', resp.meta.message, 'warning');
                                    } else {
                                        $.messager.alert('温馨提示', json.errorMsg ? json.errorMsg : '请求失败', 'warning');
                                    }
                                }
                            }
                        } else {//返回值不属于OperaterResult类型
                            json.callback(resp);
                        }
                    }
                }
            });
        },

        /**
         * 数据维护
         */
        keepData: function() {
            this.initSelfAndDialog();
            extend.init();

            self.$tablegrid = $('#table_datagrid');
            self.$toolbar = $('#table_toolbar');
            self.$tablegrid.datagrid({
                url : getDataByConditionsRequestUrl,
                queryParams: self.getQueryParams(true),
                columns: userGridColums,
                toolbar : self.$toolbar,
                striped: true,
                rownumbers : true,
                fit : true,
                pagination : true,
                pageList : [10, 20, 50, 100],
                pageNumber:1,
                pageSize:20,
                ctrlSelect:true,
                singleSelect:false,
                onLoadSuccess: function() {

                },
                onDblClickCell: function(index, field, value) {
                    if( self.openRowEditPattern ) {
                        if( index != self.lastEditIndex ) {
                            $(this).datagrid('cancelEdit', self.lastEditIndex);
                            self.lastEditIndex = index;
                            $(this).datagrid('beginEdit', index);
                        } else {
                            var selected = self.$tablegrid.datagrid('getSelected');
                            var currIndex = self.$tablegrid.datagrid('getRowIndex', selected);
                            self.lastEditIndex = currIndex;
                            self.$tablegrid.datagrid('beginEdit', currIndex);
                        }
                    } else {
                        self.openEditDialog(true);
                    }
                },
                onDblClickRow: function(index, row) {
                    if( self.openRowEditPattern ) {
                        if( index != self.lastEditIndex ) {
                            $(this).datagrid('cancelEdit', self.lastEditIndex);
                            self.lastEditIndex = index;
                            $(this).datagrid('beginEdit', index);
                        } else {
                            var selected = self.$tablegrid.datagrid('getSelected');
                            var currIndex = self.$tablegrid.datagrid('getRowIndex', selected);
                            self.lastEditIndex = currIndex;
                            self.$tablegrid.datagrid('beginEdit', currIndex);
                        }
                    } else {
                        self.openEditDialog(true);
                    }
                },
                onEndEdit: function(index,row,changes) {
                    var tableCode = $('#keepData_table_select').combobox('getValue');
                    var dataName = tableCode=='0' ? '用户属性数据' : 'hadoop配置数据';
                    var form = new FormData();
                    var iconFile = $('td[field="icon"] input[type="file"]')[0].files[0];//获取上传图片文件
                    if( iconFile ) {
                        if(!checkIsChooseImgFile()) {
                            return;
                        }
                        form.append('iconFile', iconFile);
                    }
                    form.append('tableCode', tableCode);
                    if( row ) {
                        for(var i in row) {
                            if(i!='createDate' && i!='modifyDate' && i!='lastAccessDate') {
                                form.append(i, row[i]);
                            }
                        }
                    }
                    var reqUrl = addDataRequestUrl;
                    var opName = '新增';
                    if( (form.get('userId')||form.get('constId')) && self.opType=='edit' ) {//编辑
                        opName = '修改';
                        reqUrl = editDataRequestUrl;
                    }
                    $.ajax({
                        type: 'post',
                        url: reqUrl,
                        data: form,
                        contentType: false,
                        processData: false,
                        dataType: 'json',
                        success: function(resp) {
                            if( resp.meta.success ) {
                                console.log(opName + dataName + '成功');
                                self.$tablegrid.datagrid('acceptChanges', self.lastEditIndex);
                                self.$tablegrid.datagrid('reload');
                            } else {
                                self.$tablegrid.datagrid('cancelEdit', self.lastEditIndex);//回滚
                                self.$tablegrid.datagrid('rejectChanges', self.lastEditIndex);
                            }
                            self.lastEditIndex = undefined;
                        }
                    });
                },
            });

            self.initDataKeepPageEvent();
        },
        initDataKeepPageEvent: function() {
            this.initSelfAndDialog();

            self.sendAjaxReq({//设置默认搜索项为用户表的搜索项
                url: getTableFieldsDataRequestUrl,
                params: {tableCode: '0'},
                successCallback: function(resp) {
                    self.$toolbar.find('#keepData_search_col_select').combobox({
                        valueField: 'fieldName',
                        textField: 'fieldText',
                        data: resp.data,
                        onChange: function(newVal, oldVal) {
                            console.log(newVal);
                            self.$tablegrid.datagrid('load', self.getQueryParams());
                        }
                    })
                },
            });
            //self.$toolbar.find('#keepData_search_col_select').combobox({//不需要重复定义
            //    onChange: function(newVal, oldVal) {
            //        console.log(newVal);
            //        self.$tablegrid.datagrid('load', self.getQueryParams());
            //    }
            //});
            self.$toolbar.find('a#more_sc_a').unbind('click');
            self.$toolbar.find('a#more_sc_a').bind('click', function() {
                var isVisible = $('tr.more-sc-tr').is(':visible');
                if( isVisible ) {
                    $(this).linkbutton({
                        iconCls: 'icon-arrow-bottom'
                    });
                    $('tr.more-sc-tr').hide();
                    $(this).attr('title', '更多搜索条件');
                } else {
                    $(this).linkbutton({
                        iconCls: 'icon-arrow-top'
                    });
                    $('tr.more-sc-tr').show();
                    $(this).attr('title', '收起搜索条件');
                }
                return false;
            });
            self.$toolbar.find('#keepData_table_select').combobox({
                onChange: function() {
                    //alert('切换表');
                    var params = {
                        'tableCode': $('#keepData_table_select').combobox('getValue')
                    }
                    if( params.tableCode ) {
                        self.sendAjaxReq({
                            url: getTableFieldsDataRequestUrl,
                            params: params,
                            successCallback: function(resp) {
                                $('#keepData_search_col_select').combobox({
                                    valueField: 'fieldName',
                                    textField: 'fieldText',
                                    data: resp.data,
                                })
                            },
                        });
                    } else {
                        self.$tablegrid('loadData', {rows:[], total:0});
                    }
                    var gridColumns = userGridColums;
                    $('#more_sc_a').show();
                    if( params.tableCode == '1' ) {
                        gridColumns = hadoopGridColums;
                        $('tr.more-sc-tr').hide();
                        $('#more_sc_a').hide();
                    }
                    self.$tablegrid.datagrid({
                        columns: gridColumns,
                        pagination : true,
                        pageList : [10, 20, 50, 100],
                        pageNumber:1,
                        pageSize:20,
                        onBeforeLoad: function(param) {
                            //bugFix: 修复切换表时数据错乱且无法对可排序的列进行排序的bug（原因：可排序字段与表属性不对应，导致后台查询数据出错
                            //if( !self.didSomethingBeforeLoad ) {
                            //    if( param ) {
                            //        param.sort = '';
                            //        param.order = '';
                            //    }
                            //    self.didSomethingBeforeLoad = true;
                            //} else {
                            //    self.didSomethingBeforeLoad = false;
                            //    //do nothing
                            //}
                        },
                        queryParams: self.getQueryParams(),
                    })
                    //self.$tablegrid.datagrid('load', self.getQueryParams());
                }
            });
            self.$toolbar.find('a#add_a').unbind('click');
            self.$toolbar.find('a#add_a').bind('click', function() {
                if( self.openRowEditPattern ) {//行编辑模式
                    self.$tablegrid.datagrid('insertRow',{
                        index: 0,	// 新增到第一行
                        row: {}
                    });
                    self.lastEditIndex = 0;
                    self.$tablegrid.datagrid('beginEdit', 0);
                } else {//非行编辑模式
                    self.openEditDialog();
                }
                return false;
            });
            self.$toolbar.find('a#edit_a').unbind('click');
            self.$toolbar.find('a#edit_a').bind('click', function() {
                if( self.openRowEditPattern ) {//行编辑模式
                    var selected = self.$tablegrid.datagrid('getSelected');
                    if( selected ) {
                        var currIndex = self.$tablegrid.datagrid('getRowIndex', selected);
                        self.lastEditIndex = currIndex;
                        self.$tablegrid.datagrid('beginEdit', currIndex);
                    } else {
                        $.messager.alert('温馨提示', '请先选择行！', 'warning');
                    }
                } else {//非行编辑模式
                    self.openEditDialog(true);
                }
                return false;
            });
            self.$toolbar.find('a#del_a').unbind('click');
            self.$toolbar.find('a#del_a').bind('click', function() {
                var selections = self.$tablegrid.datagrid('getSelections');
                if( !selections || !selections.length ) {
                    $.messager.alert('温馨提示', '请先选择要删除的行（可多选）!', 'warning');
                    return false;
                }
                $.messager.confirm('温馨提示', '删除后不可撤销，确定删除选中行吗？', function(r) {
                    if(r) {
                        var tableCode = $('#keepData_table_select').combobox('getValue');
                        var ids = [];
                        for(var i in selections) {
                            if( tableCode == 0 ) {
                                ids.push(selections[i].userId);
                            } else if( tableCode == 1 ) {
                                ids.push(selections[i].constId);
                            }
                        }
                        var param = {
                            'tableCode': tableCode,
                        };
                        if( tableCode == 0 ) {
                            param['custUserIds[]'] = ids;
                        } else if( tableCode == 1 ) {
                            param['haConstIds[]'] = ids;
                        }
                        $.post(delDataRequestUrl, param, function(resp) {
                            resp = JSON.parse(resp);
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '删除成功!', 'info', function() {
                                    self.$tablegrid.datagrid('reload');
                                });
                            } else {
                                $.messager.alert('温馨提示', '删除失败!', 'info');
                            }
                        });
                    }
                });
                //alert('删除');
                return false;
            });
            self.$toolbar.find('#row_edit_pattern_check').unbind('click');
            self.$toolbar.find('#row_edit_pattern_check').bind('click', function(e) {
                //e.stopPropagation();
                if( $(this).is(':checked')) {
                    self.$toolbar.find('a#save_a').linkbutton({disabled: false});
                    self.$toolbar.find('a#undo_a').linkbutton({disabled: false});
                    self.$toolbar.find('span#edit_pattern_state').text('关闭');
                    self.openRowEditPattern = true;
                } else {
                    self.openRowEditPattern = false;
                    self.$toolbar.find('a#save_a').linkbutton({disabled: true});
                    self.$toolbar.find('a#undo_a').linkbutton({disabled: true});
                    self.$toolbar.find('span#edit_pattern_state').text('开启');
                }
            });
            self.$toolbar.find('a#save_a').unbind('click');
            self.$toolbar.find('a#save_a').bind('click', function() {
                //alert('保存');
                if( self.openRowEditPattern && (self.lastEditIndex || self.lastEditIndex==0) ) {
                    var tableCode = $('#keepData_table_select').combobox('getValue');
                    if( tableCode == '0' ) {
                        var displayNameEd = self.$tablegrid.datagrid('getEditor', {index:self.lastEditIndex,field:'displayName'});
                        var emailHashEd = self.$tablegrid.datagrid('getEditor', {index:self.lastEditIndex,field:'emailHash'});
                        var inputDisplayName = $(displayNameEd.target).textbox('getValue');
                        var inputEmailHash = $(emailHashEd.target).textbox('getValue');
                        if( !inputDisplayName || (inputDisplayName && !inputDisplayName.replace(/\s/g, '')) ) {
                            $.messager.alert('温馨提示', '昵称不能为空!', 'warning');
                        } else if( !inputEmailHash || (inputEmailHash && !inputEmailHash.replace(/\s/g, '')) ) {
                            $.messager.alert('温馨提示', '邮箱hash不能为空!', 'warning');
                        } else {
                            self.$tablegrid.datagrid('endEdit', self.lastEditIndex);
                        }
                    } else {
                        var constKeyEd = self.$tablegrid.datagrid('getEditor', {index:self.lastEditIndex,field:'constKey'});
                        var constValueEd = self.$tablegrid.datagrid('getEditor', {index:self.lastEditIndex,field:'constValue'});
                        var inputConstKey = $(constKeyEd.target).textbox('getValue');
                        var inputConstValue = $(constValueEd.target).textbox('getValue');
                        if( !inputConstKey || (inputConstKey && !inputConstKey.replace(/\s/g, '')) ) {
                            $.messager.alert('温馨提示', '配置项名不能为空!', 'warning');
                        } else if( !inputConstValue || (inputConstValue && !inputConstValue.replace(/\s/g, '')) ) {
                            $.messager.alert('温馨提示', '配置项值不能为空!', 'warning');
                        } else {
                            self.$tablegrid.datagrid('endEdit', self.lastEditIndex);
                        }
                    }
                } else {
                    console.log('当前处于非行编辑模式或当前无新增或编辑行');
                }
                return false;
            });
            self.$toolbar.find('a#undo_a').unbind('click');
            self.$toolbar.find('a#undo_a').bind('click', function() {
                //alert('撤销');
                if( self.openRowEditPattern && (self.lastEditIndex || self.lastEditIndex==0) ) {
                    self.$tablegrid.datagrid('cancelEdit', self.lastEditIndex);
                    self.$tablegrid.datagrid('rejectChanges', self.lastEditIndex);
                } else {
                    console.log('当前处于非行编辑模式或当前无新增或编辑行');
                }
                return false;
            });
            self.$toolbar.find('a#imp_a').unbind('click');
            self.$toolbar.find('a#imp_a').bind('click', function() {
                self.$dialog.dialog({
                    title: '导入数据',
                    href: importDataPageUrl,
                    width: '400px',
                    height: '300px',
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-save',
                        handler: function() {
                            var tableCode = $('#keepData_table_select').combobox('getValue');
                            self.$form.find('input#table_code').val(tableCode)
                            self.$form.form('submit', {
                                onSubmit: function() {
                                    if( self.$form.form('validate') ) {
                                        return true;
                                    } else {
                                        $.messager.alert('温馨提示', '请先选择文件！', 'warning');
                                        return false;
                                    }
                                },
                                success: function(resp) {
                                    resp = JSON.parse(resp);
                                    var tableName = tableCode=='0' ? '用户属性' : 'hadoop配置';
                                    if( resp.meta.success ) {
                                        $.messager.alert('温馨提示', '导入' + tableName + '数据成功', 'info', function() {
                                            self.$dialog.dialog('close');
                                            self.$tablegrid.datagrid('reload');
                                        });
                                    } else {
                                        if( resp.meta.message ) {
                                            $.messager.alert('温馨提示', resp.meta.message, 'info');
                                        } else {
                                            $.messager.alert('温馨提示', '导入' + tableName + '数据失败', 'info');
                                        }
                                    }
                                }
                            })
                        },
                    }, self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    },
                });
                return false;
            });
            self.$toolbar.find('a#exp_a').unbind('click');
            self.$toolbar.find('a#exp_a').bind('click', function() {
                //alert('导出');
                var rows=self.$tablegrid.datagrid('getSelections');
                var queryParams = self.getQueryParams();
                var cols = self.getCurrentCols();
                var $colsInput = ('<input type="text" name="cols[]" value="' + cols + '"/>');
                var $form = $('<form></form>')
                $form.attr('action', exportDataRequestUrl);
                $form.attr('method', 'post');
                $form.append($colsInput);
                if( queryParams ) {//将json类型参数中的所有参数添加到form表单
                    for(var i in queryParams) {
                        var $paramInput = $('<input type="text" name="' + i + '" value="' + queryParams[i] + '"/>');
                        $form.append($paramInput);
                    }
                }
                var tableCode = $('#keepData_table_select').combobox('getValue');
                var dataName = tableCode=='0' ? '用户属性数据' : 'hadoop配置数据';
                if(rows && rows.length>0){
                    var ids=new Array();
                    for(var i=0;i<rows.length;i++){
                        if( queryParams.tableCode == '0' ) {
                            ids.push(rows[i].userId);
                        } else {
                            ids.push(rows[i].constId);
                        }
                    }
                    $.messager.confirm('请确认','导出所选' + dataName + '吗？',function(r){
                        if (r){
                            var $idsInput = ('<input type="text" name="ids[]" value="' + ids + '"/>');
                            $form.append($idsInput);
                            $(document.body).append($form);
                            $form.submit();
                            $form.remove();
                            self.exportData();
                        }
                    });
                } else {
                    $.messager.confirm('请确认','您未选择行，是否要导出当前条件下所有' + dataName + '？',function(r){
                        if (r){
                            //查询条件
                            $(document.body).append($form);
                            $form.submit();
                            $form.remove();
                            self.exportData();
                        }
                    });
                }
                return false;
            });
            self.$toolbar.find('#keepData_search_col_select').combobox({
                onChange: function(newVal) {
                    //console.log(newVal);
                    //alert('切换搜索条件');
                }
            });
            self.$toolbar.find('a#filter_cols_a').unbind('click');
            self.$toolbar.find('a#filter_cols_a').bind('click', function() {
                var tableCode = $('#keepData_table_select').combobox('getValue');
                var tableName = tableCode=='0' ? '用户属性数据' : 'hadoop配置数据';
                var showColsPageUrl = tableCode=='0' ? showUserColsPageUrl : showHadoopColsPageUrl;
                var height = tableCode=='0' ? '350px' : '300px';
                self.$dialog.dialog({
                    title: '筛选' + tableName + '列',
                    href: showColsPageUrl,
                    width: '400px',
                    height: height,
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-save',
                        handler: function() {
                            self.filterCols();
                        },
                    }, self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    },
                });
                return false;
            });
            self.$toolbar.find('#search_cont').searchbox({
                searcher: function() {
                    //alert('搜索...');
                    self.$tablegrid.datagrid('load', self.getQueryParams());
                }
            });
            self.$toolbar.find('#search_cont').textbox('addClearBtn', 'icon-clear');
            self.$toolbar.find('a.icon-clear').bind('click', function() {
                self.$toolbar.find('#search_cont').searchbox('setValue', '');
                self.$tablegrid.datagrid('load', self.getQueryParams());
            });
            self.$toolbar.find('#search_cont').searchbox('textbox').bind('input propertychange', function() {
                self.$toolbar.find('#search_cont').searchbox('setValue', $(this).val());
                self.$tablegrid.datagrid('load', self.getQueryParams());
            });

            self.$toolbar.find('#search_a').unbind('click');
            self.$toolbar.find('#search_a').bind('click', function() {
                self.$tablegrid.datagrid('load', self.getQueryParams());
                return false;
            });
        },

        /**
         * 打开编辑数据窗口（包括新增/编辑用户数据和新增/编辑hadoop配置
         * @param isEdit
         * @returns {boolean}
         */
        openEditDialog: function(isEdit) {
            this.initSelfAndDialog();

            if( isEdit ) {
                self.opType = 'edit';//编辑模式
                var selected = self.$tablegrid.datagrid('getSelected');
                if( !selected ) {
                    $.messager.alert('温馨提示', '请先选择行', 'warning');
                    return false;
                }
            } else {
                self.opType = 'add';//新增模式
            }
            self.currSelected = selected;
            var tableCode = $('#keepData_table_select').combobox('getValue');
            var title = (isEdit ? '编辑' : '新增') + '用户属性';
            var pageUrl = editCustomUserPageUrl;
            var width = '600px';
            var height = '565px';
            if( tableCode == 1 ) {
                title = (isEdit ? '编辑' : '新增') + 'Hadoop配置';
                pageUrl = editHaConstPageUrl;
                width = '400px';
                height = '350px';
            }
            self.$dialog.dialog({
                title: title,
                href: pageUrl,
                width: width,
                height: height,
                modal: true,
                queryParams: {},
                buttons: self.footButtons,
                maximizable: true,
                resizable: true,
                onLoad: function() {
                    self.$dialog.dialog('center');
                },
            });
        },
        /**
         * 检查self和dialog变量，并初始化
         */
        initSelfAndDialog: function() {
            if( !self ) {
                self = this;
            }
            if( !self.$dialog || !self.$dialog.length ) {
                self.$dialog = $('#dialog_win');
            }
        },

        search: function() {
            this.initSelfAndDialog();
        },

        getQueryParams: function(first) {
            this.initSelfAndDialog();

            var tableCode = $('#keepData_table_select').combobox('getValue');
            var searchConditionFields = $('#keepData_search_col_select').combobox('getValues');
            var searchConditionConts = [];
            var searchCont = self.$toolbar.find('#search_cont').searchbox('getValue');
            if( searchCont ) {
                var searchContArr = searchCont.split(',');
                for(var i in searchContArr) {
                    searchConditionConts[i] = searchContArr[i];
                }
            }
            for(var i in searchConditionFields) {
                if( searchConditionConts[i] === undefined )  {
                    searchConditionConts[i] = '';
                };
            }

            var queryParams = {
                'tableCode': tableCode,
                'searchConditionFields[]': searchConditionFields,
                'searchConditionConts[]': searchConditionConts,
                'beginCreateDateStr': $('#begin_create_date').datebox('getValue'),
                'endCreateDateStr': $('#end_create_date').datebox('getValue'),
                'beginModifyDateStr': $('#begin_modify_date').datebox('getValue'),
                'endModifyDateStr': $('#end_modify_date').datebox('getValue'),
            }

            if( tableCode == '0' ) {
                var sex = $('input[name="sex"]:checked').val();
                if( sex ) {
                    queryParams['sex'] = sex;
                }
                var userParam = {
                    'minAge': $('#min_age').numberspinner('getValue'),
                    'maxAge': $('#max_age').numberspinner('getValue'),
                    'minUpVotes': $('#min_upVotes').numberspinner('getValue'),
                    'maxUpVotes': $('#max_upVotes').numberspinner('getValue'),
                    'minDownVotes': $('#min_downVotes').numberspinner('getValue'),
                    'maxDownVotes': $('#max_downVotes').numberspinner('getValue'),
                    'minReputation': $('#min_reputation').numberspinner('getValue'),
                    'maxReputation': $('#max_reputation').numberspinner('getValue'),
                    'minViews': $('#min_views').numberspinner('getValue'),
                    'maxViews': $('#max_views').numberspinner('getValue'),
                    'beginLastAccessDateStr': $('#begin_lastAccess_date').datebox('getValue'),
                    'endLastAccessDateStr': $('#end_lastAccess_date').datebox('getValue'),
                }
                for(var i in userParam) {
                    if( userParam[i] ) {
                        queryParams[i] = userParam[i];
                    }
                }
            }
            if( !first ) {
                var options = self.$tablegrid.datagrid('getPager').data("pagination").options;
                var pageNum = options.pageNumber;
                var pageSize = options.pageSize;
                var sort = self.$tablegrid.datagrid('options').sortName;
                queryParams.sort = sort;
                var order = self.$tablegrid.datagrid('options').sortOrder;
                queryParams.order = order;
                queryParams.page = pageNum;
                queryParams.rows = pageSize;
            }
            return queryParams;
        },

        editCustomUser: function() {
            this.initSelfAndDialog();

            self.$form = $('#edit_custUser_form');
            if( self.opType == 'edit' ) {
                self.recoverCustomUserData();
                self.$form.attr('action', editDataRequestUrl);
            } else {
                self.$form.attr('action', addDataRequestUrl);
            }
            self.initEditCustomUserPageEvent();
        },
        initEditCustomUserPageEvent: function() {
            this.initSelfAndDialog();

            self.$form.find('#icon').filebox({
                onChange: function(newVal) {
                    //self.$form.find('#icon_preview_img').attr('src', newVal);
                    checkIsChooseImgFile(self.$form.find('input[name="iconFile"]'), {
                        success: function() {
                            utils.previewImg('#icon_preview_img', 'input[name="iconFile"]');
                        },
                        fail: function() {
                            $('#icon_preview_img').attr('src', '');
                        }
                    });
                }
            });
        },
        recoverCustomUserData: function() {
            this.initSelfAndDialog();

            if( self.currSelected ) {
                self.$form.find('#user_id').val(self.currSelected.userId);
                self.$form.find('#display_name').textbox('setValue', self.currSelected.displayName);
                self.$form.find('#email_hash').textbox('setValue', self.currSelected.emailHash);
                //self.$form.find('#icon').filebox('setValue', self.currSelected.icon);
                self.$form.find('#icon_preview_img').attr('src', self.currSelected.icon)
                self.$form.find('#about_me').textbox('setValue', self.currSelected.aboutMe);
                self.$form.find('#website_url').textbox('setValue', self.currSelected.websiteUrl);
                self.$form.find('#location').textbox('setValue', self.currSelected.location);
                self.$form.find('input[name="sex"]').each(function(index, domElem) {
                    if( $(this).val() == self.currSelected.sex ) {
                        $(this).prop('checked', true);
                    }
                });
                self.$form.find('#age').numberspinner('setValue', self.currSelected.age);
                self.$form.find('#up_votes').numberspinner('setValue', self.currSelected.upVotes);
                self.$form.find('#down_votes').numberspinner('setValue', self.currSelected.downVotes);
                self.$form.find('#reputation').numberspinner('setValue', self.currSelected.reputation);
                self.$form.find('#views').numberspinner('setValue', self.currSelected.views);
            }

        },

        editHaConst: function() {
            this.initSelfAndDialog();

            self.$form = $('#edit_haConst_form');
            if( self.opType == 'edit' ) {
                self.recoverHaConstData();
                self.$form.attr('action', editDataRequestUrl);
            } else {
                self.$form.attr('action', addDataRequestUrl);
            }
        },
        recoverHaConstData: function() {
            this.initSelfAndDialog();

            if( self.currSelected ) {
                self.$form.find('#const_id').val(self.currSelected.constId);
                self.$form.find('#const_key').textbox({required:true});
                self.$form.find('#const_key').textbox('setValue', self.currSelected.constKey);
                self.$form.find('#const_value').textbox({required:true});
                self.$form.find('#const_value').textbox('setValue', self.currSelected.constValue);
                self.$form.find('#description').textbox({});
                self.$form.find('#description').textbox('setValue', self.currSelected.description);
            }
        },


        importData: function() {
            this.initSelfAndDialog();

            self.$form = $('#import_data_form');
        },

        exportData: function() {
            this.initSelfAndDialog();

            var tableCode = $('#keepData_table_select').combobox('getValue');
            var dataName = tableCode=='0' ? '用户属性数据' : 'hadoop配置数据';
            $.messager.show({
                title:'温馨提示',
                msg:'正在导出' + dataName + '，请稍候。。。',
                timeout: 2000,
                showType:'slide'
            });
        },
        /**
         * 回显原来选定的列
         */
        showOrdinaryCols : function() {
            this.initSelfAndDialog();

            var tableCode = $('#keepData_table_select').combobox('getValue');
            var defaultCols = tableCode=='0' ? defaultUserCols : defaultHadoopCols;
            var colsJson = tableCode=='0' ? userColsJson : hadoopColsJson;

            //获取原来选定的列
            var oldColumnsArray = self.$tablegrid.datagrid('options').columns[0];
            if( oldColumnsArray!=null && oldColumnsArray.length ) {//删除checkbox列
                if( oldColumnsArray[0].checkbox ) {
                    oldColumnsArray.splice(0, 1);
                }
            }
//			alert(self.getCurrentCols());

            //回显
            $('span.col-cnt').text(oldColumnsArray.length);
            $('input[name="col"]').each(function() {
                var a = 1;
                for(var i in oldColumnsArray) {
                    var field = oldColumnsArray[i].field;
                    var elemField = $(this).attr('field');
                    if(elemField == field) {
                        $(this).prop('checked', 'checked');
                    }
                }
            });
            //当所有多选按钮被选中时“全选”按钮被选中，否则只要有一个多选按钮未被选中，取消选中“全选”按钮
            var allChecked = true;
            $('input[name="col"]').unbind('click');
            $('input[name="col"]').bind('click', function() {
                //更新当前所选中列的数量
                var colCnt = $('input[name="col"]:checked').length;
                $('span.col-cnt').text(colCnt);
                $('input[name="col"]').each(function(index, domElem) {
                    if(!$(domElem).is(':checked')) {
                        allChecked = false;
                    }
                });
                if(allChecked) {
                    $('input#all_check').prop('checked', 'checked');
                } else {
                    $('input#all_check').prop('checked', '');
                    allChecked = true;
                }
                checkIsDefaultChecked();
            });
            //绑定“全选”按钮点击事件(这里必须使用prop方法设置checked属性值，因为attr方法不起作用)
            $('input#all_check').unbind('click');
            $('input#all_check').bind('click', function() {
                $('input#default_check').prop('checked', '');
                if($(this).attr('checked')) {
                    $('input[name="col"]').prop('checked', 'checked');
                } else {
                    $('input[name="col"]').prop('checked', '');
                }
                //更新当前所选中列的数量
                var colCnt = $('input[name="col"]:checked').length;
                $('span.col-cnt').text(colCnt);
                checkIsDefaultChecked();
            });
            //绑定“默认列”按钮点击事件
            $('input#default_check').unbind('click');
            $('input#default_check').bind('click', function() {
                if($(this).attr('checked')) {
                    $('input#all_check').prop('checked', '');
                    $('input[name="col"]').prop('checked', '');
                    for(var i in defaultCols) {
                        $('input[name="col"]').each(function() {
                            if($(this).val() == defaultCols[i]) {
                                $(this).prop('checked', 'checked');
                            }
                        });
                    }
                } else {
                    for(var i in defaultCols) {
                        $('input[name="col"]').each(function() {
                            if($(this).val() == defaultCols[i]) {
                                $(this).prop('checked', '');
                            }
                        });
                    }
                }
                //更新当前所选中列的数量
                var colCnt = $('input[name="col"]:checked').length;
                $('span.col-cnt').text(colCnt);
                checkIsAllChecked();
            });
            checkIsAllChecked();
            checkIsDefaultChecked();
            function checkIsAllChecked() {
                //当所有列都被选中时，选中“全选/全不选”按钮
                if( $('span#total_cols').text() == $('span.col-cnt').text() ) {
                    $('input#all_check').prop('checked', 'checked');
                } else {
                    $('input#all_check').prop('checked', '');
                }
            }
            function checkIsDefaultChecked() {
                //当默认列都被选中时，选中“默认”按钮
                var currCols = [];
                $('input[name="col"]:checked').each(function() {
                    currCols.push($(this).val());
                });
                var isDefaultChecked = true;
                for(var i in defaultCols) {
                    if( defaultCols[i] != currCols[i] ) {
                        isDefaultChecked = false;
                    }
                }
                if( isDefaultChecked ) {
                    $('input#default_check').prop('checked', 'checked');
                } else {
                    $('input#default_check').prop('checked', '');
                }
            }
        },

        /**
         * 获取当前所选的列
         * @returns {Array}
         */
        getCurrentCols : function() {
            this.initSelfAndDialog();

            var tableCode = $('#keepData_table_select').combobox('getValue');
            var defaultCols = tableCode=='0' ? defaultUserCols : defaultHadoopCols;
            var colsJson = tableCode=='0' ? userColsJson : hadoopColsJson;
            //获取原来选定的列
            var oldColumnsArray = self.$tablegrid.datagrid('options').columns[0];
            if( oldColumnsArray!=null && oldColumnsArray.length ) {//删除checkbox列
                if( oldColumnsArray[0].checkbox ) {
                    oldColumnsArray.splice(0, 1);
                }
            }
            var cols = new Array();

            for(var i in oldColumnsArray) {
                for(var n=0; n <= defaultCols.length-1; n++) {
                    if(!colsJson[n]) {//跳过空列和checkbox列
                        continue;
                    }
                    if(colsJson[n].field == oldColumnsArray[i].field) {
                        cols.push(colsJson[n].colId);
                    }
                }
            }

            return cols;
        },
        /**
         * 筛选列
         */
        filterCols : function() {
            this.initSelfAndDialog();

            var tableCode = $('#keepData_table_select').combobox('getValue');
            var colsJson = tableCode=='0' ? userColsJson : hadoopColsJson;
            var colsArray = new Array();
            //获取选中的列checkbox的值
            $('input[name="col"]').each(function() {
                if($(this).is(':checked')) {
                    colsArray.push($(this).val());
                }
            });
            if(colsArray.length <= 0) {
                $.messager.alert('温馨提示','请至少选择一列!', 'warning');
                return;
            }
            var dataColumnFilter = [];
            var dataArray = [];
            //构造dataColumn对象
            dataArray.push(colsJson['-1']);//默认添加checkbox按钮
            for(var i in colsArray) {
                dataArray.push(colsJson[colsArray[i]]);
            }
            dataColumnFilter.push(dataArray);
            //重新设置columns属性
            self.$tablegrid.datagrid({
                columns: dataColumnFilter
            });
            //关闭弹出框
            self.$dialog.dialog('close');
        },
    };

    module.exports = new DataKeep();
});