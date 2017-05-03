define(function(require, exports, module) {
    var base = require("module/base/main.js");
    var extend = require("module/base/extend.js");
    var dataKeep = require("module/dataKeep/dataKeep.js");

    var pageRootUrl = 'pages/friend/';//页面url根目录
    var requestRootUrl = 'friend/';//请求url跟目录
    //page urls
    var showRecFriendsPageUrl = pageRootUrl + 'showRecFriends.jsp';
    var showColsPageUrl = pageRootUrl + 'showUserCols.jsp';
    //request urls
    var getDataByConditionsRequestUrl = 'dataKeep/getDataByConditions';//根据搜索项和对应的搜索内容获取数据请求url
    var getTableFieldsDataRequestUrl = 'dataKeep/getTableFieldsData';
    var getRecUserDataRequestUrl = requestRootUrl + 'getRecUserData';

    var defaultUserCols = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14];//默认列
    //json对象: 用户数据datagrid columns
    var userColsJson = {
        '0' : {colId:'0', field:'displayName', title:'昵称', width:150, align:'center',sortable:true,
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
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
            }
        },
        '2' : {colId:'2', field:'icon', title:'头像', width:100, align:'center',
            formatter: function(val, row, index){
                if( !val ) {
                    return '无';
                }
                return '<img src="' + val + '" style="margin:auto;width:80px;height:80px;border-radius:10px;border:1px solid #999;vertical-align: middle"/>';
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
            }
        },
        '4' : {colId:'4', field: 'sex',title: '性别',width: 80, align:'center',
            formatter: function(val, row, index){
                switch(val) {
                    case 0: return '男';
                    case 1: return '女';
                    default: return '未知';
                }
            }
        },
        '5' : {colId:'5', field:'age', title:'年龄', width:80, align:'center',sortable:true},
        '6' : {colId:'6', field:'location', title:'常驻地', width:80, align:'center'},
        '7' : {colId: '7', field: 'websiteUrl', title: '个人主页', width: 150, align: 'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            }
        },
        '8' : {colId:'8', field:'upVotes', title:'赞同数', width:80, align:'center',sortable:true},
        '9' : {colId:'9', field:'downVotes', title:'反对数', width:80, align:'center',sortable:true},
        '10' : {colId:'10', field:'reputation', title:'声望值', width:80, align:'center',sortable:true},
        '11' : {colId:'11', field: 'views',title: '浏览数',width: 80, align:'center',sortable:true},
        '12' : {colId:'12', field: 'createDate',title: '创建时间',width: 130, align:'center',sortable:true},
        '13' : {colId:'13', field:'modifyDate', title:'修改时间', width:130, align:'center',sortable:true},
        '14' : {colId:'14', field:'lastAccessDate', title:'最后一次访问时间', width:130, align:'center',sortable:true},
    };
    //用户数据datagrid columns
    var userGridColums = [[
        {field: 'displayName', title: '昵称', width: '150', align:'center', sortable:true,
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
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
            }
        },
        {field: 'icon', title: '头像', width: '100', align:'center',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                return '<img src="' + val + '" style="margin:auto;width:80px;height:80px;border-radius:10px;border:1px solid #999;vertical-align: middle"/>';
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
            }
        },
        {field: 'sex', title: '性别', width: '80', align:'center',
            formatter: function(val, row, index) {
                switch(val) {
                    case 0: return '男';
                    case 1: return '女';
                    default: return '未知';
                }
            }
        },
        {field: 'age', title: '年龄', width: '80',  align:'center',sortable: true},
        {field: 'location', title: '常驻地', width: '80',  align:'center'},
        {field: 'websiteUrl', title: '个人主页',  align:'center',width: '150',
            formatter: function(val, row, index) {
                if( !val ) {
                    return '无';
                }
                var $divElem = $('<div>' + val + '</div>')
                var text = $divElem.text();
                $divElem = undefined;
                return '<div title="' + text + '">' + val + '</div>';
            }
        },
        {field: 'upVotes', title: '赞同数', width: '80',  align:'center',sortable: true},
        {field: 'downVotes', title: '反对数', width: '80',  align:'center',sortable: true},
        {field: 'reputation', title: '声望值', width: '80',  align:'center',sortable: true},
        {field: 'views', title: '浏览数', width: '80',  align:'center',sortable: true},
        {field: 'createDate', title: '创建时间', width: '130',  align:'center',sortable: true},
        {field: 'modifyDate', title: '修改时间', width: '130',  align:'center',sortable: true},
        {field: 'lastAccessDate', title: '最后一次访问时间', width: '130',  align:'center',sortable: true},
    ]];

    var self;

    //定义属性
    var Friend = function() {
        this.$dialog;
        this.$usergrid;
        this.$recUsergrid;
        this.$userToolbar;
        this.$recUserToolbar;
        this.footButtons = [{
            text: '保存',
            iconCls: 'icon-save',
            handler: function() {

            },
        },{
            text: '退出',
            iconCls: 'icon-cancel',
            handler: function() {
                self.$dialog.dialog('close');
            },
        }];
    };
    //定义函数
    Friend.prototype = {
        constructor: Friend,

        /**
         * 检查self和dialog变量，并初始化
         */
        initSelfAndDialog: function() {
            if( !self ) {
                self = this;
            }
            if( !self.$dialog || !self.$dialog.length ) {
                self.$dialog = $('#fd_dialog_win');
            }
            if( !self.$dialog2 || !self.$dialog2.length ) {
                self.$dialog2 = $('#fd_dialog_win2');
            }
        },

        init: function() {
            this.initSelfAndDialog();

            self.initUsergrid();
            self.initIndexPageEvent();
        },
        initUsergrid: function() {
            this.initSelfAndDialog();

            this.initSelfAndDialog();
            extend.init();

            self.$usergrid = $('#user_datagrid');
            self.$userToolbar = $('#user_toolbar');
            self.gridCode = 0;
            self.$usergrid.datagrid({
                url : getDataByConditionsRequestUrl,
                queryParams: self.getQueryParams(true),
                columns: userGridColums,
                toolbar : self.$userToolbar,
                striped: true,
                rownumbers : true,
                fit : true,
                pagination : true,
                pageList : [10, 20, 50, 100],
                pageNumber:1,
                pageSize:20,
                ctrlSelect:true,
                singleSelect: true,//单选
                onLoadSuccess: function() {

                },
            });
        },
        initIndexPageEvent: function() {
            this.initSelfAndDialog();

            $('#friend_rec_a').unbind('click');
            $('#friend_rec_a').bind('click', function() {
                var selected = self.$usergrid.datagrid('getSelected');
                if( !selected ) {
                    $.messager.alert('温馨提示', '请先选择一个用户！', 'warning');
                    return false;
                }
                self.$dialog.dialog({
                    title: '推荐的朋友',
                    href: showRecFriendsPageUrl,
                    width: '550px',
                    height: '400px',
                    modal: true,
                    queryParams: {},
                    buttons: [self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    }
                });
            });

            self.$userToolbar.find('#user_search_col_select').combobox({
                onChange: function(newVal, oldVal) {
                    self.gridCode = 0;
                    self.$usergrid.datagrid('load', self.getQueryParams());
                }
            });
            self.$userToolbar.find('a#more_sc_a').unbind('click');
            self.$userToolbar.find('a#more_sc_a').bind('click', function() {
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
            dataKeep.sendAjaxReq({//设置默认搜索项为用户表的搜索项
                url: getTableFieldsDataRequestUrl,
                params: {tableCode: '0'},
                successCallback: function(resp) {
                    $('#user_search_col_select').combobox({
                        valueField: 'fieldName',
                        textField: 'fieldText',
                        data: resp.data,
                    })
                },
            });
            self.$userToolbar.find('a#filter_cols_a').unbind('click');
            self.$userToolbar.find('a#filter_cols_a').bind('click', function() {
                self.gridCode = 0;
                self.$dialog.dialog({
                    title: '筛选列',
                    href: showColsPageUrl,
                    width: '400px',
                    height: '350px',
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-save',
                        handler: function() {
                            self.gridCode = 0;
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
            self.$userToolbar.find('#user_search_cont').searchbox({
                searcher: function() {
                    //alert('搜索...');
                    self.gridCode = 0;
                    self.$usergrid.datagrid('load', self.getQueryParams());
                }
            });
            self.$userToolbar.find('#user_search_cont').textbox('addClearBtn', 'icon-clear');
            self.$userToolbar.find('a.icon-clear').bind('click', function() {
                self.$userToolbar.find('#user_search_cont').searchbox('setValue', '');
                self.gridCode = 0;
                self.$usergrid.datagrid('load', self.getQueryParams());
            });
            self.$userToolbar.find('#user_search_cont').searchbox('textbox').bind('input propertychange', function() {
                self.$userToolbar.find('#user_search_cont').searchbox('setValue', $(this).val());
                self.gridCode = 0;
                self.$usergrid.datagrid('load', self.getQueryParams());
            });

            self.$userToolbar.find('#search_a').unbind('click');
            self.$userToolbar.find('#search_a').bind('click', function() {
                self.gridCode = 0;
                self.$usergrid.datagrid('load', self.getQueryParams());
                return false;
            });
        },

        getQueryParams: function(first) {
            this.initSelfAndDialog();

            var $currGrid = self.gridCode==0 ? self.$usergrid : self.$recUsergrid;
            var searchConditionFields;
            var searchConditionConts = [];
            var searchCont;
            if( self.gridCode == 0 ) {
                searchConditionFields = self.$userToolbar.find('#user_search_col_select').combobox('getValues');
                searchCont = self.$userToolbar.find('#user_search_cont').searchbox('getValue');
            } else {
                searchConditionFields = self.$recUserToolbar.find('#rec_user_search_col_select').combobox('getValues');
                searchCont = self.$recUserToolbar.find('#rec_user_search_cont').searchbox('getValue');
            }
            if( searchCont ) {
                var searchContArr = searchCont.split(',');
                for(var i in searchContArr) {
                    searchConditionConts[i] = searchContArr[i];
                }
            }
            if( searchConditionFields && searchConditionFields.length ) {
                for(var i in searchConditionFields) {
                    if( searchConditionConts[i] === undefined )  {
                        searchConditionConts[i] = '';
                    };
                }
            }
            var queryParams = {
                'tableCode': '0',//用户数据
                'searchConditionFields[]': searchConditionFields,
                'searchConditionConts[]': searchConditionConts,
            }
            if( self.gridCode == 0 ) {//当且仅当搜索的是用户数据时才添加日期搜索条件
                queryParams.beginCreateDateStr = $('#begin_create_date').datebox('getValue');
                queryParams.endCreateDateStr = $('#end_create_date').datebox('getValue');
                queryParams.beginModifyDateStr = $('#begin_modify_date').datebox('getValue');
                queryParams.endModifyDateStr = $('#end_modify_date').datebox('getValue');
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
                var options = $currGrid.datagrid('getPager').data("pagination").options;
                var pageNum = options.pageNumber;
                var pageSize = options.pageSize;
                var sort = $currGrid.datagrid('options').sortName;
                queryParams.sort = sort;
                var order = $currGrid.datagrid('options').sortOrder;
                queryParams.order = order;
                queryParams.page = pageNum;
                queryParams.rows = pageSize;
            }
            return queryParams;
        },
        /**
         * 回显原来选定的列
         */
        showOrdinaryCols : function() {
            this.initSelfAndDialog();

            var $currGrid = self.gridCode==1 ? self.$recUsergrid : self.$usergrid;

            var defaultCols = defaultUserCols;
            var colsJson = userColsJson;

            //获取原来选定的列
            var oldColumnsArray = $currGrid.datagrid('options').columns[0];
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

            var $currGrid = self.gridCode==1 ? self.$recUsergrid : self.$usergrid;
            var defaultCols = defaultUserCols;
            var colsJson = userColsJson;
            //获取原来选定的列
            var oldColumnsArray = $currGrid.datagrid('options').columns[0];
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

            var $currGrid = self.gridCode==1 ? self.$recUsergrid : self.$usergrid;
            var colsJson = userColsJson;
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
            for(var i in colsArray) {
                dataArray.push(colsJson[colsArray[i]]);
            }
            dataColumnFilter.push(dataArray);
            //重新设置columns属性
            $currGrid.datagrid({
                columns: dataColumnFilter
            });
            //关闭弹出框
            if( self.gridCode == 0 ) {
                self.$dialog.dialog('close');
            } else {
                self.$dialog2.dialog('close');
            }
        },

        showRecFriends: function() {
            this.initSelfAndDialog();

            self.initRecUsergrid();
            self.initShowRecFriendsPageEvent();
        },
        initRecUsergrid: function() {
            this.initSelfAndDialog();

            var selected = self.$usergrid.datagrid('getSelected');

            self.$recUsergrid = $('#rec_user_datagrid');
            self.$recUserToolbar = $('#rec_user_toolbar');
            self.$recUsergrid.datagrid({
                url : getRecUserDataRequestUrl,
                queryParams: {orginalUserId: selected.userId},
                columns: userGridColums,
                toolbar : self.$recUserToolbar,
                striped: true,
                rownumbers : true,
                fit : true,
                pagination : true,
                pageList : [10, 20, 50, 100],
                pageNumber:1,
                pageSize:20,
                ctrlSelect:true,
                singleSelect:false,
                onLoadSuccess: function(data) {
                    var searchKeyword = self.$recUserToolbar.find('#rec_user_search_cont').searchbox('getValue');
                    if( !searchKeyword ) {
                        if( data && data.rows && data.rows.length ) {
                            var rows = data.rows;
                            for(var i in rows) {
                                if( rows[i].userId == selected.userId ) {

                                }
                            }
                        } else {
                            $.messager.alert('温馨提示', '很抱歉，系统还不够智能，无法找到当前用户的推荐朋友!', 'info');
                        }
                    }
                },
                onBeforeLoad: function(param) {
                    if( param ) {
                        param.orginalUserId = selected.userId;
                    }
                },
            });
        },
        initShowRecFriendsPageEvent: function() {
            this.initSelfAndDialog();

            dataKeep.sendAjaxReq({//设置默认搜索项为用户表的搜索项
                url: getTableFieldsDataRequestUrl,
                params: {tableCode: '0'},
                successCallback: function(resp) {
                    $('#rec_user_search_col_select').combobox({
                        valueField: 'fieldName',
                        textField: 'fieldText',
                        data: resp.data,
                    })
                },
            });
            self.$recUserToolbar.find('#rec_user_search_col_select').combobox({
                onChange: function(newVal, oldVal) {
                    self.gridCode = 1;
                    self.$recUsergrid.datagrid('load', self.getQueryParams());
                }
            });
            self.$recUserToolbar.find('a#rec_filter_cols_a').unbind('click');
            self.$recUserToolbar.find('a#rec_filter_cols_a').bind('click', function() {
                self.gridCode = 1;
                self.$dialog2.dialog({
                    title: '筛选列',
                    href: showColsPageUrl,
                    width: '400px',
                    height: '350px',
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text: '保存',
                        iconCls: 'icon-save',
                        handler: function() {
                            self.gridCode = 1;
                            self.filterCols();
                        },
                    },{
                        text: '退出',
                        iconCls: 'icon-cancel',
                        handler: function() {
                            self.$dialog2.dialog('close');
                        },
                    }],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog2.dialog('center');
                    },
                });
                return false;
            });
            self.$recUserToolbar.find('#rec_user_search_cont').searchbox({
                searcher: function() {
                    //alert('搜索...');
                    self.gridCode = 1;
                    self.$recUsergrid.datagrid('load', self.getQueryParams());
                }
            });
            self.$recUserToolbar.find('#rec_user_search_cont').textbox('addClearBtn', 'icon-clear');
            self.$recUserToolbar.find('a.icon-clear').bind('click', function() {
                self.$recUserToolbar.find('#rec_user_search_cont').searchbox('setValue', '');
                self.gridCode = 1;
                self.$recUsergrid.datagrid('load', self.getQueryParams());
            });
            self.$recUserToolbar.find('#rec_user_search_cont').searchbox('textbox').bind('input propertychange', function() {
                self.$recUserToolbar.find('#rec_user_search_cont').searchbox('setValue', $(this).val());
                self.gridCode = 1;
                self.$recUsergrid.datagrid('load', self.getQueryParams());
            });

        },
    };

    module.exports = new Friend();
});