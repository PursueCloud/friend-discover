define(function(require, exports, module) {
    var base = require("module/base/main.js");
    var extend = require("module/base/extend.js");
    var index = require("module/admin/index.js");
    var utils = require("module/base/utils.js");
    //var dataSolve = require("module/dataSolve/dataSolve.js");

    var pageRootUrl = 'pages/hadoop/';
    var requestRootUrl = 'hadoop/';
    //page urls
    var currJobMonitorPageUrl = pageRootUrl + 'currJobMonitor.jsp';
    var chooseFileOrEnterPathUrl = pageRootUrl + 'chooseFileOrEnterPath.jsp';
    var browserHdfsFilesOrDirsPageUrl = pageRootUrl + 'browserHdfsFilesOrDirs.jsp';
    var chooseOrEnterCopyMovePathPageUrl = pageRootUrl + 'chooseOrEnterCopyMovePath.jsp';
    //request urls
    var getHdfsDirsRequestUrl = requestRootUrl + 'getHdfsDirs';
    var getHdfsFilesRequestUrl = requestRootUrl + 'getHdfsFiles';
    var uploadFile2HDFSRequestUrl = requestRootUrl + 'uploadFile2HDFS';
    var downloadFileFromHDFSRequestUrl = requestRootUrl + 'downloadFileFromHDFS';
    var deleteHdfsFilesRequestUrl = requestRootUrl + 'deleteHdfsFiles';
    var getOneMonitorDataRequestUrl = requestRootUrl + 'getOneMonitorData';
    var getAllMonitorDataRequestUrl = requestRootUrl + 'getAllMonitorData';
    var renameHdfsFileRequestUrl = requestRootUrl + 'rename';
    var mkdirHdfsFileRequestUrl = requestRootUrl + 'mkdir';
    var copyToRequestUrl = requestRootUrl + 'copyTo';
    var moveToRequestUrl = requestRootUrl + 'moveTo';
    var execMapReduceExampleRequestUrl = requestRootUrl + 'execMapReduceExample';
    var self;
    var monitorAllInterval;//监控定时器(全部）
    var monitorOneInterval;//监控定时器（当前）

    //定义属性
    var Hadoop = function() {
        this.$monitorgrid;
        this.$dialog;
        this.$toolbar;
        this.$searchbox;
        this.footButtons = [{
            text: '保存',
            iconCls: 'icon-save',
            handler: function() {
                self.$form.form('submit', {
                    onSubmit: function() {
                        if( self.$form.form('validate') ) {
                            return true;
                        } else {
                            if( self.fileSolveType == '0' ) {
                                if( !$('#choose_input_filebox').textbox('getValue') ) {
                                    $.messager.alert('温馨提示', '请选择要上传的文件', 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '请输入或选择hdfs上传路径', 'warning');
                                }
                            } else if( self.fileSolveType == '1' ){
                                $.messager.alert('温馨提示', '请选择要下载的文件', 'warning');
                            } else {
                                //console.log($('#deduplicate_input_box').length);
                                if( $('#deduplicate_input_box').length && !$('#deduplicate_input_box').textbox('getValue') ) {
                                    $.messager.alert('温馨提示', '请选择输入路径', 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '请选择输出路径', 'warning');
                                }
                            }
                            return false;
                        }
                    },
                    success: function(resp) {
                        resp = JSON.parse(resp);
                        if( self.fileSolveType == '0' ) {
                            var uploadFileName = '';

                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '上传文件' + uploadFileName + '到hdfs成功!', 'info');
                                $('#fs_dialog_win').dialog('close');
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', resp.meta.message, 'error');
                                } else {
                                    $.messager.alert('温馨提示', '上传文件' + uploadFileName + '到hdfs失败!', 'error');
                                }
                            }
                        } else if( self.fileSolveType == '1' ){
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '下载文件：' + self.$form.find('#downloadHdfsFilePath').textbox('getValue') + '成功!', 'info');
                                $('#fs_dialog_win').dialog('close');
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', resp.meta.message, 'error');
                                } else {
                                    $.messager.alert('温馨提示', '下载文件：' + self.$form.find('#downloadHdfsFilePath').textbox('getValue') + '失败!', 'error');
                                }
                            }
                        } else {
                        }
                    }
                })
            },
        }];
    };
    //定义函数
    Hadoop.prototype = {
        constructor: Hadoop,

        /**
         * 检查self和dialog变量，并初始化
         */
        initSelfAndDialog: function(dialogSelector1, dialogSelector2) {
            if( !self ) {
                self = this;
            }
            if( dialogSelector1 ) {
                if( !self.$dialog || !self.$dialog.length ) {
                    self.$dialog = $(dialogSelector1);
                }
            } else {
                var $dialogDiv = $('#hadoop_dialog_win').length ? $('#hadoop_dialog_win') :
                    $('<div id="hadoop_dialog_win"></div>');//bugfix:第一次才能成功打开窗口,原因：dialog的id重复出现，导致jq查找不到元素
                self.$dialog = $dialogDiv;
            }
            if( dialogSelector2 ) {
                if( !self.$dialog2 || !self.$dialog2.length ) {
                    self.$dialog2 = $(dialogSelector2);
                }
            } else {
                var $dialogDiv2= $('#hadoop_dialog_win2').length ? $('#hadoop_dialog_win2') :
                    $('<div id="hadoop_dialog_win2"></div>');//bugfix:第一次才能成功打开窗口,原因：dialog的id重复出现，导致jq查找不到元素
                self.$dialog2 = $dialogDiv2;
            }
        },

        /**
         * HDFS浏览器
         */
        hdfsBrowser: function() {
            this.initSelfAndDialog('#hb_dialog_win', '#hb_dialog_win2');

            self.$hdfsFilesgrid = $('#hdfs_files_grid');
            self.$hdfsFilesgrid.treegrid({
                url: getHdfsFilesRequestUrl,
                rownumbers: true,
                fit: true,
                toolbar: $('#table_toolbar'),
                idField: 'fileId',
                treeField: 'fileName',
                animate: true,
                cascadeCheck: true,
                columns:[[
                    {field:'fileId', checkbox:true},
                    {title:'文件',field:'fileName',width:500, sortable:true,
                        formatter: function(val, row, index) {
                            if( !val ) {
                                return '无';
                            }
                            var $divElem = $('<div>' + val + '</div>')
                            var text = $divElem.text();
                            $divElem = undefined;
                            var keyword = $('#search_cont').searchbox('getValue');
                            if( keyword ) {
                                val = row.rootPath + val;
                            }
                            return '<div title="' + text + '">' + val + '</div>';
                        },
                        editor: {
                            type: 'textbox',
                        }
                    },
                    {title:'大小',field:'fileSize',width:120, sortable:true,},
                    {title:'创建（修改）日期',field:'modifyTime',width:150, sortable:true,}
                ]],
                onSelect: function(row) {
                    //console.log('check node');
                    var children = row.children;
                    if( children && children.length ) {
                        for(var i in children) {
                            $(this).treegrid('select', children[i].fileId);
                        }
                    }
                    //function isAllChecked(row) {
                    //    var parent = self.$hdfsFilesgrid.treegrid('find', row.parentId);
                    //    var pChildren = parent.children;
                    //    var allChildCheck = true;
                    //    if( pChildren && pChildren.length ) {
                    //        for(var i in pChildren) {
                    //            if( !isChecked(pChildren[i]) ) {
                    //                allChildCheck = false;
                    //                break;
                    //            }
                    //        }
                    //    }
                    //    return allChildCheck;
                    //}
                    //function isChecked(row) {
                    //    if( !row ) {
                    //        return false;
                    //    }
                    //    var selectRows = self.$hdfsFilesgrid.treegrid('getSelections');
                    //    if( selectRows && selectRows.length ) {
                    //        for(var i in selectRows) {
                    //            if(selectRows[i] == row) {
                    //                return true;
                    //            }
                    //        }
                    //    }
                    //    return false;
                    //}
                },
                onUnselect: function(row) {
                    //console.log('uncheck node');
                    var children = row.children;
                    if( children && children.length ) {
                        for(var i in children) {
                            $(this).treegrid('unselect', children[i].fileId);
                        }
                    }
                },
                onBeforeLoad: function(row,param){
                    if (!row) {    // load top level rows
                        param.id = 0;    // set id=0, indicate to load new page rows
                        param.isRoot=true;
                    }
                },
                onBeforeExpand: function(row) {
                    if (row) {    // load top level rows
                        var options = self.$hdfsFilesgrid.treegrid('options');
                        var param = options.queryParams;
                        param.id = row.fileId;
                        param.expandNodeFilePath = row.rootPath + row.fileName;
                    }
                },
                onDblClickRow: function(row) {
                    //console.log(self.getFullPathInHDFS(row));
                },
                onAfterEdit: function(row, changes) {
                    if( self.isSave ) {//重命名或新建文件夹
                        if( self.mkdir ) {
                            if( !changes.fileName || !changes.fileName.replace(/\s/g, '') ) {
                                changes.fileName = '新建文件夹';
                            }
                        } else {
                            if( !changes.fileName || !changes.fileName.replace(/\s/g, '') ) {
                                //console.log('无改变');
                                return;
                            }
                        }
                        var path = self.getCurrSelectedFilePathInHDFS($(this));
                        //console.log('path:');
                        //console.log(path);
                        var newName = changes.fileName;
                        var url = '';
                        var params = {};
                        var opName = '';
                        if( self.mkdir ) {//新建文件夹
                            //console.log('新建文件夹');
                            url = mkdirHdfsFileRequestUrl;
                            params.dirName = newName;
                            params.rootPath = path;
                            opName = '新建文件夹';
                        } else {//重命名
                            //console.log('重命名');
                            url = renameHdfsFileRequestUrl;
                            params.newName = newName;
                            params.orginalFilePath = self.$hdfsBrowser_orginalFilePath;
                            opName = '重命名';
                        }
                        //console.log(params);
                        $.ajax({
                            type: 'get',
                            url: url,
                            data: params,
                            dataType: 'json',
                            success: function(resp) {
                                if( resp.meta.success ) {
                                    row.hasSave = true;
                                    self.$hdfsFilesgrid.treegrid('acceptChanges');
                                    self.$hdfsFilesgrid.treegrid('reload');
                                } else {
                                    self.$hdfsFilesgrid.treegrid('rejectChanges');
                                    if( self.mkdir ) {
                                        self.$hdfsFilesgrid.treegrid('remove', self.lastRowId);
                                    }
                                    row.hasSave = false;
                                    if( resp.meta.message ) {
                                        $.messager.alert('温馨提示', resp.meta.message, 'warning');
                                    } else {
                                        $.messager.alert('温馨提示', opName + '失败！', 'warning');
                                    }
                                }
                            }
                        });
                    }
                },
                onClickCell: function(field, row) {
                    if( self.hdfsBrowser_lastField!=field || self.hdsfBrowser_lastRow!=row.fileId ) {
                        self.$hdfsFilesgrid.treegrid('cancelEdit', self.hdsfBrowser_lastRow);
                    }
                },
                onDblClickCell: function(field, row) {
                    if( row.fileId=='1' && row.fileName=='全部文件' ) {
                        $.messager.alert('温馨提示', '根目录无法重命名！', 'warning');
                        return;
                    }
                    self.mkdir = false;
                    self.hdfsBrowser_lastField = field;
                    self.hdsfBrowser_lastRow = row.fileId;
                    self.$hdfsBrowser_orginalFilePath = row.rootPath + row.fileName;
                    self.$hdfsFilesgrid.treegrid('beginEdit', self.hdsfBrowser_lastRow);
                    $('span#mkdir_op_span').show();
                },
                ctrlSelect: true,
                singleSelect: false,
            });
            self.initHdfsBrowserPageEvent();
        },
        initHdfsBrowserPageEvent: function() {
            this.initSelfAndDialog('#hb_dialog_win', '#hb_dialog_win2');

            $('#search_cont').searchbox({
                searcher: function(val) {
                    self.$hdfsFilesgrid.treegrid('load', {keyword: val});
                }
            })
            $('a#upload_file_to_hdfs_a').unbind('click');
            $('a#upload_file_to_hdfs_a').bind('click', function(e) {
                console.log('上传文件');
                self.openChooseFileOrEnterPathDialog(self.$dialog, null, '250px', '0');
            });
            $('li#upload_file_to_hdfs_li').unbind('click');
            $('li#upload_file_to_hdfs_li').bind('click', function(e) {
                console.log('上传文件');
                self.isUploadDir = false;
                self.openChooseFileOrEnterPathDialog(self.$dialog, null, '250px', '0');
            });
            $('li#upload_folder_to_hdfs_li').unbind('click');
            $('li#upload_folder_to_hdfs_li').bind('click', function(e) {
                console.log('上传文件夹');
                self.isUploadDir = true;
                self.openChooseFileOrEnterPathDialog(self.$dialog, null, '350px', '0');
            });
            $('a#mkdir_in_hdfs_a').unbind('click');
            $('a#mkdir_in_hdfs_a').bind('click', function(e) {
                //console.log('新建文件夹');
                self.createNewDirInHdfs(self.$hdfsFilesgrid);
            });
            $('a#save_a').unbind('click');
            $('a#save_a').bind('click', function(e) {
                console.log('保存');
                self.isSave = true;
                self.$hdfsFilesgrid.treegrid('endEdit', self.hdsfBrowser_lastRow);
                $('span#mkdir_op_span').hide();
            });
            $('a#cancel_a').unbind('click');
            $('a#cancel_a').bind('click', function(e) {
                console.log('取消');
                self.isSave = false;
                if( self.mkdir ) {
                    self.$hdfsFilesgrid.treegrid('remove', self.hdsfBrowser_lastRow);
                }
                self.$hdfsFilesgrid.treegrid('cancelEdit', self.hdsfBrowser_lastRow);
                $('span#mkdir_op_span').hide();
            });
            $('a#download_file_from_hdfs_a').unbind('click');
            $('a#download_file_from_hdfs_a').bind('click', function(e) {
                console.log('下载');
                var selectedRows = self.$hdfsFilesgrid.treegrid('getSelections');
                if( !selectedRows || !selectedRows.length ) {
                    $.messager.alert('温馨提示', '请选择要下载的文件或文件夹（可多选）!', 'warning');
                    return false;
                }
                for(var i in selectedRows) {
                    if( selectedRows[i].fileId == '1' ) {
                        $.messager.alert('温馨提示', '请不要选择“全部文件”!', 'warning');
                        return false;
                    }
                }
                $.messager.confirm('请确认', '确定下载选中的' + selectedRows.length + '个文件吗？', function(r) {
                    if (r) {
                        var formQueue = [];
                        for(var i in selectedRows) {//如果选中了多个文件，则发送多次下载请求
                            (function(index) {
                                var downloadHdfsFilePath = selectedRows[index].rootPath + selectedRows[index].fileName;
                                console.log(downloadHdfsFilePath);
                                var $downloadForm = $('<form></form>');
                                $downloadForm.attr('action', downloadFileFromHDFSRequestUrl);
                                $downloadForm.attr('method', 'post');
                                var $paramInput = $('<input name="downloadHdfsFilePath" value="' + downloadHdfsFilePath  +'"/>')
                                $downloadForm.append($paramInput);
                                $(document.body).append($downloadForm);
                                $downloadForm.submit();
                                $downloadForm.remove();
                            })(i);
                            //return;//仅下载第一个文件。
                        }
                        //console.log('formQueue size:' + formQueue.length);
                    }
                });
            });
            $('a#remove_from_hdfs_a').unbind('click');
            $('a#remove_from_hdfs_a').bind('click', function(e) {
                console.log('删除');
                var selectedRows = self.$hdfsFilesgrid.treegrid('getSelections');
                if( !selectedRows || !selectedRows.length ) {
                    $.messager.alert('温馨提示', '请选择要删除的文件或文件夹（可多选）!', 'warning');
                    return false;
                }
                $.messager.confirm('请确认', '删除后将无法恢复，确定要删除选中的' + selectedRows.length + '个文件吗？', function(r) {
                    if( r ) {
                        var deleteFilePaths = [];
                        for(var i in selectedRows) {
                            deleteFilePaths.push(selectedRows[i].rootPath + selectedRows[i].fileName);
                        }
                        $.ajax({
                            type: 'get',
                            url: deleteHdfsFilesRequestUrl,
                            data:{'deleteFilePaths[]': deleteFilePaths},
                            dataType: 'json',
                            success: function(resp) {
                                if( resp.meta.success ) {
                                    $.messager.alert('温馨提示', '删除成功！', 'info');
                                    self.$hdfsFilesgrid.treegrid('reload');
                                } else {
                                    if( resp.meta.message ) {
                                        $.messager.alert('温馨提示', resp.meta.message, 'info');
                                    } else {
                                        $.messager.alert('温馨提示', '删除失败', 'info');
                                    }
                                }
                            }
                        });
                    }
                });
            });
            $('a#reload_files_a').unbind('click');
            $('a#reload_files_a').bind('click', function(e) {
                self.$hdfsFilesgrid.treegrid('reload');
            });
            $('li#rename_file_li').unbind('click');
            $('li#rename_file_li').bind('click', function(e) {
                console.log('重命名');
                var selected = self.$hdfsFilesgrid.treegrid('getSelected');
                if( !selected ) {
                    $.messager.alert('温馨提示', '请先选择一个文件或文件夹!', 'warning');
                    return false;
                }
                if( selected.fileId=='1' && selected.fileName=='全部文件' ) {
                    $.messager.alert('温馨提示', '根目录无法重命名！', 'warning');
                    return;
                }
                self.mkdir = false;
                self.hdfsBrowser_lastField = "fileName";
                self.hdsfBrowser_lastRow = selected.fileId;
                self.$hdfsBrowser_orginalFilePath = selected.rootPath + selected.fileName;
                self.$hdfsFilesgrid.treegrid('beginEdit', self.hdsfBrowser_lastRow);
                $('span#mkdir_op_span').show();
            });
            $('li#copy_to_li').unbind('click');
            $('li#copy_to_li').bind('click', function(e) {
                console.log('复制到');
                var selectedRows = self.$hdfsFilesgrid.treegrid('getSelections');
                if( !selectedRows || !selectedRows.length ) {
                    $.messager.alert('温馨提示', '请选择要复制的文件或文件夹（可多选）!', 'warning');
                    return false;
                }
                self.cmOpType = '0';
                self.$dialog.dialog({
                    title: '请选择目标目录（hdfs）',
                    href: chooseOrEnterCopyMovePathPageUrl,
                    width: '410px',
                    height: '200px',
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text:'保存',
                        iconCls: 'icon-save',
                        handler: function() {
                            var srcPaths = self.getSelectedFilePaths();
                            self.$form.find('input[name="srcPaths[]"]').val(srcPaths);
                            console.log('复制-保存')
                            self.$form.form('submit', {
                                onSubmit: function() {
                                    if( self.$form.form('validate') ) {
                                        return true;
                                    } else {
                                        $.messager.alert('温馨提示', '请选择目标路径', 'warning');
                                        return false;
                                    }
                                },
                                success: function(resp) {
                                    resp = JSON.parse(resp);
                                    var cmOpTypeName = self.cmOpType=='0' ? '复制' : '移动';
                                    if( resp.meta.success ) {
                                        $.messager.alert('温馨提示', cmOpTypeName + '成功!', 'info');
                                        self.$dialog.dialog('close');
                                        self.$hdfsFilesgrid.treegrid('reload');
                                    } else {
                                        if( resp.meta.message ) {
                                            $.messager.alert('温馨提示', resp.meta.message, 'error');
                                        } else {
                                            $.messager.alert('温馨提示', cmOpTypeName + '失败!', 'error');
                                        }
                                    }
                                }
                            })
                        }
                    }],
                    maximizable: true,
                    resizable: true,
                    onLoad: function () {
                        self.$dialog.dialog('center');
                    }
                });
            });
            $('li#move_to_li').unbind('click');
            $('li#move_to_li').bind('click', function(e) {
                console.log('移动到');
                var selectedRows = self.$hdfsFilesgrid.treegrid('getSelections');
                if( !selectedRows || !selectedRows.length ) {
                    $.messager.alert('温馨提示', '请选择要移动的文件或文件夹（可多选）!', 'warning');
                    return false;
                }
                self.cmOpType = '1';
                self.$dialog.dialog({
                    title: '请选择目标目录（hdfs）',
                    href: chooseOrEnterCopyMovePathPageUrl,
                    width: '410px',
                    height: '200px',
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text:'保存',
                        iconCls: 'icon-save',
                        handler: function() {
                            console.log('移动-保存')
                            var srcPaths = self.getSelectedFilePaths();
                            self.$form.find('input[name="srcPaths[]"]').val(srcPaths);
                            self.$form.form('submit', {
                                onSubmit: function() {
                                    if( self.$form.form('validate') ) {
                                        return true;
                                    } else {
                                        $.messager.alert('温馨提示', '请选择目标路径', 'warning');
                                        return false;
                                    }
                                },
                                success: function(resp) {
                                    resp = JSON.parse(resp);
                                    var cmOpTypeName = self.cmOpType=='0' ? '复制' : '移动';
                                    if( resp.meta.success ) {
                                        $.messager.alert('温馨提示', cmOpTypeName + '成功!', 'info');
                                        self.$dialog.dialog('close');
                                        self.$hdfsFilesgrid.treegrid('reload');
                                    } else {
                                        if( resp.meta.message ) {
                                            $.messager.alert('温馨提示', resp.meta.message, 'error');
                                        } else {
                                            $.messager.alert('温馨提示', cmOpTypeName + '失败!', 'error');
                                        }
                                    }
                                }
                            })
                        }
                    }],
                    maximizable: true,
                    resizable: true,
                    onLoad: function () {
                        self.$dialog.dialog('center');
                    }
                });
            });
            self.initDropbox('#upload_file_to_hdfs_a', '.upload-ul');
            self.initDropbox('#move_operations_a', '.more-operations-ul');
            self.initFileRowEvent();
        },

        /**
         * 获取选中的文件（多个）hdfs路径集合（返回数组）
         * @param $grid
         * @returns {Array}
         */
        getSelectedFilePaths: function($grid) {
            var $currGrid = $grid ? $grid : self.$hdfsFilesgrid;
            var selectedRows = $currGrid.treegrid('getSelections');
            var selectedFilePaths = [];
            if( selectedRows && selectedRows.length ) {
                //过滤
                var removeRows = [];
                for(var i in selectedRows) {
                    for(var j in selectedRows) {
                        if( selectedRows[j].parentId == selectedRows[i].fileId ) {
                            removeRows.push(selectedRows[j]);
                        }
                    }
                }
                var avaibleRows = [];
                for(var i in selectedRows) {
                    var avaible = true;
                    for(var j in removeRows) {
                        if( removeRows[j].fileId == selectedRows[i].fileId ) {
                            avaible = false;
                            break;
                        }
                    }
                    if( avaible ) {
                        avaibleRows.push(selectedRows[i]);
                    }
                }
                selectedRows = avaibleRows;
                for(var i in selectedRows) {
                    selectedFilePaths.push(selectedRows[i].rootPath + selectedRows[i].fileName);
                }
            }
            return selectedFilePaths;
        },
        /**
         * 打开选择文件或输入文件路径窗口
         * @param $dialog
         * @param title
         * @param height
         * @param fileSolveType
         * @param callback
         */
        openChooseFileOrEnterPathDialog: function($dialog, title, height, fileSolveType, callback) {
            this.initSelfAndDialog();

            self.fileSolveType = fileSolveType;
            $dialog.dialog({
                title: title ? title : '选择待上传的文件',
                href: chooseFileOrEnterPathUrl,
                width: '410px',
                height: height ? height : '200px',
                modal: true,
                queryParams: {},
                buttons: self.footButtons,
                maximizable: true,
                resizable: true,
                onLoad: function() {
                    $dialog.dialog('center');
                    if( fileSolveType == '1' ) {
                        $('span.file-type').text('下载');
                        $('span.to-loc').text('本地');
                    } else if( fileSolveType == '0' ) {
                        $('span.file-type').text('上传');
                        $('span.to-loc').text('hdfs');
                    } else {

                    }
                    if( fileSolveType == '1' ) {//如果是下载文件，则重新绑定filebox的按钮事件，改为弹出框选择要下载的hdfs的文件
                        //修改文件选择输入框name属性为downloadHdfsFilePath，输入框类型为text
                        $('td.file-input-box-td').find('span.textbox').remove();
                        //$('td.file-input-box-td').find('span.textbox').find('input[type="file"]').attr('name', 'downloadHdfsFilePath');
                        //$('td.file-input-box-td').find('span.textbox').find('input[type="file"]').attr('type', 'hidden');
                        $('#choose_input_filebox').filebox({//将filebox初始化为textbox
                            //prompt: '请选择或输入hdfs文件路径',
                            required: false,
                        });
                        //$('#upload_hdfs_path').textbox({required: false});
                        //$('#upload_hdfs_path').filebox({
                        //    prompt: '请选择文件保存路径',
                        //    name: 'downloadLocalDirFile',
                        //    required: false,
                        //});
                        $('td.upload-path-td').find('span.textbox').find('input[type="hidden"]').attr('name', 'downloadHdfsFilePath');

                        //重新绑定“浏览”按钮点击事件
                        $('td.upload-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.upload-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('从hdfs中选择要下载的文件');//弹出一个类似百度网盘的弹出框，供用户选择hdfs中要下载的文件
                            self.chooseFilesInHdfs('#upload_hdfs_path', '选择要下载的文件或文件夹（HDFS）');
                        });
                    } else {
                        if( self.isUploadDir ) {
                            $('td.file-input-box-td').find('span.textbox').find('input[type="file"]').attr('webkitdirectory', 'webkitdirectory');
                            $('td.file-input-box-td').find('span.textbox').find('input[type="file"]').attr('directory', 'directory');
                        } else {
                            $('td.file-input-box-td').find('span.textbox').find('input[type="file"]').removeAttr('webkitdirectory');
                        }
                    }
                    if( callback ) {
                        callback();
                    }
                }
            });
        },
        chooseFileOrEnterPath: function() {
            this.initSelfAndDialog('#cfep_dialog_win', '#cfep_dialog_win2');

            self.$form = $('#hdfs_file_form');
            if( self.fileSolveType == '0' ) {
                self.$form.attr('action', uploadFile2HDFSRequestUrl);//上传
                self.$form.attr('enctype', 'multipart/form-data');
                $('tr.upload-path-tr').find('span.textbox').find('a.textbox-button').unbind('click');
                $('tr.upload-path-tr').find('span.textbox').find('a.textbox-button').bind('click', function() {
                    self.chooseDirsInHdfs('#upload_hdfs_path');
                });
                $('tr.upload-path-br-tr').show();
                $('tr.upload-path-tr').show();
                if(self.isUploadDir) {
                    $('tr#selected_folder_tr').show();
                    utils.initUploadDir($('td.file-input-box-td').find('span.textbox').find('input[type="file"]'));
                }
            } else {
                self.$form.removeAttr('enctype');
                self.$form.attr('action', downloadFileFromHDFSRequestUrl);//下载
                $('tr.file-choose-tr').hide();
                $('tr.upload-path-br-tr').hide();
                $('tr.upload-path-tr').show();
            }
        },

        chooseOrEnterCopyMovePath: function() {
            this.initSelfAndDialog('#cecp_dialog_win', '#cecp_dialog_win2');

            self.$form = $('#hdfs_file_form');
            if( self.cmOpType == '0' ) {
                self.$form.attr('action', copyToRequestUrl);//复制
                $('span.cm-op-type').text('复制');
                $('tr.dst-path-tr').find('span.textbox').find('a.textbox-button').unbind('click');
                $('tr.dst-path-tr').find('span.textbox').find('a.textbox-button').bind('click', function() {
                    self.chooseDirsInHdfs('#dst_path');
                });
            } else {
                $('span.cm-op-type').text('移动');
                self.$form.attr('action', moveToRequestUrl);//移动
                $('tr.dst-path-tr').find('span.textbox').find('a.textbox-button').unbind('click');
                $('tr.dst-path-tr').find('span.textbox').find('a.textbox-button').bind('click', function() {
                    self.chooseDirsInHdfs('#dst_path');
                });
            }
        },

        /**
         * 选择hdfs文件夹
         * @param title
         */
        chooseDirsInHdfs: function(recoverSelectPathSelector, title) {
            this.initSelfAndDialog();

            //console.log(title);
            self.isShowFile = false;
            self.$dialog2.dialog({
                title: title ? title : '选择hdfs路径(文件夹）',
                href: browserHdfsFilesOrDirsPageUrl,
                width: '500px',
                height: '400px',
                modal: true,
                queryParams: {},
                buttons: [{
                    text: '新建文件夹',
                    iconCls: 'icon-add',
                    id: 'hdfs_mkdir_a',
                    handler: function() {
                        //console.log('新建文件夹');
                        self.createNewDirInHdfs();
                    },
                },{
                    text: '刷新',
                    iconCls: 'icon-reload',
                    id: 'refresh_hdfs_a',
                    handler: function() {
                        console.log('刷新hdfs');
                        self.refreshHDFSFilesOrDirsGrid();
                    },
                },{
                    text: '确定',
                    iconCls: 'icon-save',
                    handler: function() {
                        //console.log('确定选中文件夹');
                        var fileOrDirPath = self.getCurrSelectedFilePathInHDFS();
                        //console.log(fileOrDirPath);
                        $(recoverSelectPathSelector).textbox('setValue', fileOrDirPath);
                        self.$dialog2.dialog('close');
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
                    self.$dialog2.siblings('.dialog-button').find('#hdfs_mkdir_a').css('float', 'left');
                }
            });
        },
        /**
         * 选择hdfs文件（或文件夹）
         * @param title
         */
        chooseFilesInHdfs: function(recoverSelectPathSelector, title) {
            this.initSelfAndDialog();

            //console.log(title);
            self.isShowFile = true;
            self.$dialog2.dialog({
                title: title ? title : '选择hdfs路径(文件或文件夹)',
                href: browserHdfsFilesOrDirsPageUrl,
                width: '500px',
                height: '400px',
                modal: true,
                queryParams: {},
                buttons: [{
                    text: '新建文件夹',
                    iconCls: 'icon-add',
                    id: 'hdfs_mkdir_a',
                    handler: function() {
                        //console.log('新建文件夹');
                        self.createNewDirInHdfs();
                    },
                },{
                    text: '刷新',
                    iconCls: 'icon-reload',
                    id: 'refresh_hdfs_a',
                    handler: function() {
                        console.log('刷新hdfs');
                        self.refreshHDFSFilesOrDirsGrid();
                    },
                },{
                    text: '确定',
                    iconCls: 'icon-save',
                    handler: function() {
                        //console.log('确定选中文件');
                        var fileOrDirPath = self.getCurrSelectedFilePathInHDFS();
                        //console.log(fileOrDirPath);
                        $(recoverSelectPathSelector).textbox('setValue', fileOrDirPath);
                        self.$dialog2.dialog('close');
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
                    self.$dialog2.siblings('.dialog-button').find('#hdfs_mkdir_a').css('float', 'left');
                }
            });
        },

        /**
         * 初始化文件行事件
         */
        initFileRowEvent: function() {
            this.initSelfAndDialog();

            $('div.file').unbind('click');
            $('div.file').bind('click', function() {
                var isChecked = $(this).find('input[type="checkbox"]').is(':checked');
                if( isChecked ) {
                    $(this).find('input[type="checkbox"]').prop('checked', false);
                } else {
                    $(this).find('input[type="checkbox"]').prop('checked', true);
                }
                if( isAllFilesChecked() ) {
                    $('input#check_all_input').prop('checked', true);
                } else {
                    $('input#check_all_input').prop('checked', false);
                }
            });
            $('div.file').hover(function() {
                $(this).find('.op-in-file').show();
            }, function() {
                $(this).find('.op-in-file').hide();
            });
            $('input.check_curr').unbind('click');
            $('input.check_curr').bind('click', function(e) {
                e.stopPropagation();//阻止冒泡
                if( isAllFilesChecked() ) {
                    $('input#check_all_input').prop('checked', true);
                } else {
                    $('input#check_all_input').prop('checked', false);
                }
            });
            $('input#check_all_input').unbind('click');
            $('input#check_all_input').bind('click', function(e) {
                e.stopPropagation();//阻止冒泡
                if( $('input#check_all_input').is(':checked') ) {
                    $('input.check_curr').prop('checked', true);
                } else {
                    $('input.check_curr').prop('checked', false);
                }
            });
            function isAllFilesChecked() {
                var isAllCheck = true;
                $('input.check_curr').each(function(index, domElem) {
                    if( !$(domElem).is(':checked') ) {
                        isAllCheck = false;
                    }
                });
                return isAllCheck;
            }
            $('a.download-in-file-a').unbind('click');
            $('a.download-in-file-a').bind('click', function(e) {
                //e.preventDefault();
                e.stopPropagation();
            });
            $('a.more-operations-in-file-a').unbind('click');
            $('a.more-operations-in-file-a').bind('click', function(e) {
                //e.preventDefault();
                e.stopPropagation();
            });
        },
        initDropbox: function(btnSelector, dropboxUlSelector) {
            var isHoverOutBtn = false;
            var isHoverOutUl = false;
            $(btnSelector).hover(function() {
                isHoverOutBtn = false;
                var uploadBtnLeft = ($(this).offset().left - 1) + 'px';
                var uploadBtnTop = ($(this).offset().top + 23) + 'px';
                $(dropboxUlSelector).attr('style', 'width:82px;left:' + uploadBtnLeft + ';top:' + uploadBtnTop);
                $(this).css('border-radius','5px 5px 0 0');
                $(dropboxUlSelector).show();
            }, function() {
                isHoverOutBtn = true;
                var uploadBtnLeft = ($(this).offset().left - 1) + 'px';
                var uploadBtnTop = ($(this).offset().top + 23) + 'px';
                $(dropboxUlSelector).attr('style', 'width:82px;left:' + uploadBtnLeft + ';top:' + uploadBtnTop);
                if( isHoverOutBtn && isHoverOutUl ) {
                    $(btnSelector).css('border-radius','5px');
                    $(dropboxUlSelector).hide();
                }
            });
            $(dropboxUlSelector).hover(function() {
                isHoverOutUl = false;
                $(btnSelector).css('border-radius','5px 5px 0 0');
                $(dropboxUlSelector).show();
            }, function() {
                isHoverOutUl = true;
                if( isHoverOutBtn && isHoverOutUl ) {
                    $(btnSelector).css('border-radius','5px');
                    $(dropboxUlSelector).hide();
                }
            });
        },
        /**
         * MapReduce示例
         */
        examples: function() {
            this.initSelfAndDialog('#e_dialog_win', '#e_dialog_win2');

            self.initExamplesPageEvent();
        },
        initExamplesPageEvent: function() {
            this.initSelfAndDialog('#e_dialog_win', '#e_dialog_win2');

            $('#hadoop_examples_select').combobox({
                onLoadSuccess: function() {
                    var data = $(this).combobox('getData');
                    if( data && data.length ) {
                        var firstExampleDesc = data[0].exampleDesc;
                        var firstExampleUsage = data[0].usage;
                        $('span#example_desc_cont').html(firstExampleDesc);
                        $('span#usage_cont').html(firstExampleUsage);
                    }
                },
                onSelect: function(record) {
                    $('span#example_desc_cont').html(record.exampleDesc ? record.exampleDesc : '');
                    $('span#usage_cont').html(record.usage ? record.usage : '');
                }
            });
            $('#he_input_box').next().find('a.textbox-button').unbind('click');
            $('#he_input_box').next().find('a.textbox-button').bind('click', function() {
                //alert('选择输入路径');
                self.chooseFilesInHdfs('#he_input_box', '请选择输入文件或目录（hdfs）');
            });
            $('#he_output_box').next().find('a.textbox-button').unbind('click');
            $('#he_output_box').next().find('a.textbox-button').bind('click', function() {
                //alert('选择输出路径');
                self.chooseDirsInHdfs('#he_output_box', '请选择输出目录（hdfs）');
            })
            $('#run_mr_example_a').unbind('click');
            $('#run_mr_example_a').bind('click', function() {
                //alert('执行mapreduce示例');
                self.execMRExample();
            });
        },

        execMRExample: function() {
            var param = {
                exampleId: $('#hadoop_examples_select').combobox('getValue'),
                param1: $('#he_input_box').textbox('getValue'),
                param2: $('#he_output_box').textbox('getValue'),
            }
            var exampleName = $('#hadoop_examples_select').combobox('getText');
            $('.run-mr-example-res').html('');
            $('span.left-kuohao').show();
            $('span.right-kuohao').show();
            $('span.running').show();
            $('span.success').hide();
            $('span.failed').hide();
            var runMRExampleProgInterval = self.startProgress({
                wrapperSelector: '#prog_div',
                progressbarSelector: '#run_mr_example_prog',
            });
            $('#run_mr_example_a').unbind('click');
            $('#run_mr_example_a').bind('click', function() {
                //alert('执行mapreduce示例');
                $.messager.alert('温馨提示', 'MapReduce示例：' + exampleName + '正在运行，请稍候再试！', 'warning');
            });
            $.ajax({
                type: "post",
                url: execMapReduceExampleRequestUrl,
                data: param,
                dataType: 'json',
                success: function(resp) {
                    $('#run_mr_example_a').unbind('click');
                    $('#run_mr_example_a').bind('click', function() {
                        //alert('执行mapreduce示例');
                        self.execMRExample();
                    });
                    self.finishProgress({
                        progInterval: runMRExampleProgInterval,
                        progressbarSelector: '#run_mr_example_prog',
                    })
                    if( resp.data ) {//根据制表符格式化mr示例运行结果
                        resp.data = resp.data.replace(/(\\t){5}/g, '<br/>&#009;&#009;&#009;&#009;');
                        resp.data = resp.data.replace(/(\\t){4}/g, '<br/>&#009;&#009;&#009;');
                        resp.data = resp.data.replace(/(\\t){3}/g, '<br/>&#009;&#009;');
                        resp.data = resp.data.replace(/(\\t){2}/g, '<br/>&#009;&#009;');
                        resp.data = resp.data.replace(/\\t{1}/g, '<br/>&#009;');
                        resp.data = resp.data.replace(/\\n/g, '<br/>');
                        resp.data = '<pre style="overflow: auto;padding:10px 20px 20px;height:90%;">' + resp.data + '</pre>';
                    }
                    $('.run-mr-example-res').html(resp.data);;
                    if( resp.meta.success ) {
                        $('span.running').hide();
                        $('span.success').show();
                        $('span.failed').hide();
                        $.messager.alert('温馨提示', '运行MapReduce示例：' + exampleName + '成功!', 'info');
                    } else {
                        $('span.running').hide();
                        $('span.success').hide();
                        $('span.failed').show();
                        if( resp.meta.message ) {
                            $.messager.alert('温馨提示', '运行MapReduce示例：' + exampleName + '失败,原因：' + resp.meta.message, 'warning');
                        } else {
                            $.messager.alert('温馨提示', '运行MapReduce示例：' + exampleName + '失败,原因：请看运行结果！', 'warning');
                        }
                    }
                }
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
            $(json.wrapperSelector).find('img').attr('src', 'assets/images/loading.spanner.gif');
            var $progBar = $(json.progressbarSelector).progressbar({value:0});
            var progInterval = setInterval(function() {
                var nextVal = $progBar.progressbar('getValue') + 20;
                if( nextVal > 90 ) {
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
            $(json.progressbarSelector).parent().find('img').attr('src', 'assets/images/ok.png');
        },
        /**
         * MapReduce任务实时监控(所有）
         */
        monitor: function() {
            this.initSelfAndDialog('#m_dialog_win', '#m_dialog_win2');

            var tableColumns = [[
                {field: 'jobId', title: 'JobId', width:200, sortable:true,
                    formatter: function(val, row, index) {
                        var $divElem = $('<div>' + val + '</div>')
                        var text = $divElem.text();
                        $divElem = undefined;
                        return '<div title="' + text + '">' + val + '</div>';
                    }
                },
                {field: 'jobName', title: 'jobName', width:200, sortable:true,
                    formatter: function(val, row, index) {
                        var $divElem = $('<div>' + val + '</div>')
                        var text = $divElem.text();
                        $divElem = undefined;
                        return '<div title="' + text + '">' + val + '</div>';
                    }
                },
                {field: 'mapProgress', title: 'Map进度', width:100, sortable:true},
                {field: 'redProgress', title: 'Reduce进度', width:100, sortable:true},
                {field: 'runState', title: '任务状态', width:100, sortable:true,
                    formatter: function(val, row, index) {
                        if( !val ) {
                            return '';
                        }
                        var color = 'black';
                        if( 'SUCCEEDED' == val ) {
                            color = 'green';
                        } else if( 'SUCCEEDED' == val){
                            color = 'red';
                        } else if( '' == val ) {
                            color = 'orange';
                        } else if( val.indexOf('RUNNING') != -1) {
                            color = 'dodgerblue';
                        } else if( val.indexOf('PREP') != -1) {
                            color = '#EEE';
                        }
                        return '<div style="color:' + color + ';" title="' + val + '">' + val + '</div>';
                    }
                },
                {field: 'startTimeTs', title: '任务开始时间', width:150, sortable:true},
                {field: 'finishTimeTs', title: '任务完成时间', width:150, sortable:true},
                {field: 'spendTime', title: '耗时（时:分:秒.毫秒）', width:150, sortable:true},
            ]];
            self.$monitorgrid = $('#monitor_grid');
            self.$toolbar = $('#monitor_toolbar');
            self.$monitorgrid.datagrid({
                url : getAllMonitorDataRequestUrl,
                //data: {rows:[], total:0},
                columns: tableColumns,
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
                onDblClickCell: function() {

                },
                onDblClickRow: function() {

                },
            });
            self.initMonitorPageEvent();
        },
        initMonitorPageEvent: function() {
            this.initSelfAndDialog('#m_dialog_win', '#m_dialog_win2');

            self.$toolbar.find('a#exp_a').unbind('click');
            self.$toolbar.find('a#exp_a').bind('click', function() {
                alert('导出');
            });
            self.$toolbar.find('a#show_curr_job_a').unbind('click');
            self.$toolbar.find('a#show_curr_job_a').bind('click', function() {
                self.$dialog.dialog({
                    title: '当前任务实时监控概况',
                    href: currJobMonitorPageUrl,
                    width: '420px',
                    height: '455px',
                    modal: true,
                    queryParams: {},
                    buttons: [{
                        text: '退出',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            clearInterval(monitorOneInterval);//关闭窗口时清楚监听器monitorOneInterval
                            self.$dialog.dialog('close');
                        },
                    }],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    },
                    onClose: function() {
                        clearInterval(monitorOneInterval);//关闭窗口时清楚监听器monitorOneInterval
                    }
                });
            });
            self.$toolbar.find('input[name="refreshType"]').unbind('click');
            self.$toolbar.find('input[name="refreshType"]').bind('click', function() {
                if( $(this).val() == '0' ) {
                    $('#refresh_a').hide();
                    monitorInterval = self.startMonitorInteval();
                    console.log('自动刷新')
                } else {
                    console.log('手动刷新')
                    clearInterval(monitorInterval);
                    $('#refresh_a').show();
                }
            });
            self.$toolbar.find('a#refresh_a').unbind('click');
            self.$toolbar.find('a#refresh_a').bind('click', function() {
                self.$monitorgrid.datagrid('reload');//手动刷新
            });

            self.$toolbar.find('#search_cont').searchbox({
                searcher: function(val) {
                    self.$monitorgrid.datagrid('load', {keyword: val});//按jobName搜索
                }
            });
        },
        startMonitorInteval: function() {
            return setInterval(function() {
                    self.$monitorgrid.datagrid('reload');//重新获取监控数据
            }, 500);
        },
        /**
         * MapReduce任务实时监控(单个）
         */
        monitorOne: function() {
            this.initSelfAndDialog('#cjm_dialog_win', '#cjm_dialog_win2');

            //var $dialog = $('#cjm_dialog_win').parent();
            //if( $dialog.length ) {
            //    var options = $dialog.dialog({
            //        onClose: function() {
            //            console.log('停止任务监控轮询!');
            //            clearInterval(monitorOneInterval);
            //        }
            //    });
            //    console.log(options);
            //}
            self.$form = $('#monitor_one_form');
            monitorOneInterval = setInterval(function() {
                $.ajax({
                    type: 'get',
                    url: getOneMonitorDataRequestUrl,
                    dataType: 'json',
                    success: function(resp) {
                        //console.log(resp);
                        var monitorData = resp.data;
                        if( monitorData ) {
                            if( monitorData.rows ) {
                                $('#no_job_running_tips').hide();
                                $('#job_detail_tab').show();
                                self.$form.form('load', monitorData);
                                self.$form.form('load', monitorData.rows);
                            } else {
                                $('#no_job_running_tips').show();
                                $('#job_detail_tab').hide();
                            }
                            if( monitorData.finished== 'true' ) {
                                clearInterval(monitorOneInterval);
                                $.messager.alert('温馨提示', 'MapReduce任务完成!', 'info');
                                $('#dialog_win').dialog('close');
                                //$('#dialog_win2').dialog('close');
                            } else if( monitorData.finished == 'false'){

                            } else {
                                clearInterval(monitorOneInterval);
                                $.messager.alert('温馨提示', 'MapReduce任务出错!', 'info');
                                $('#dialog_win2').dialog('close');
                            }
                        }
                    }
                });
            },500);
            self.initMonitorOnePageEvent();
        },
        initMonitorOnePageEvent: function() {
            this.initSelfAndDialog('#cjm_dialog_win', '#cjm_dialog_win2');

        },

        /**
         * 刷新选择文件夹或文件treegrid数据
         */
        refreshHDFSFilesOrDirsGrid: function() {
            this.initSelfAndDialog();

            //self.$hdfsDirgrid = self.$hdfsDirgrid ? self.$hdfsDirgrid : $('#hdfs_dirs_grid');
            self.$hdfsDirgrid.treegrid('reload');
        },

        browserHDFSFilesOrDirs: function() {
            this.initSelfAndDialog('#bhf_dialog_win', '#bhf_dialog_win2');

            var treeDataUrl = '';
            var columnTitle = '';
            //console.log(self.isShowFile);
            if( self.isShowFile ) {
                columnTitle = '文件名';
                treeDataUrl = getHdfsFilesRequestUrl;
            } else {
                columnTitle = '文件夹名';
                treeDataUrl = getHdfsDirsRequestUrl;
            }
            //console.log(treeDataUrl);
            self.$hdfsDirgrid = $('#hdfs_dirs_grid');
            self.$hdfsDirgrid.treegrid({
                url: treeDataUrl,
                rownumbers: true,
                fit: true,
                idField: 'fileId',
                treeField: 'fileName',
                animate: true,
                columns:[[
                    {title:columnTitle,field:'fileName',width:454, halign:'center',
                        formatter: function(val, row, index) {
                            if( !val ) {
                                return '无';
                            }
                            var $divElem = $('<div>' + val + '</div>')
                            var text = $divElem.text();
                            $divElem = undefined;
                            if( row.fileSize != '0.0M' ) {
                                return '<div title="' + text + '（修改时间：' + row.modifyTime + '，大小：' + row.fileSize + '）">' + val + '</div>';
                            } else {
                                if( row.isEmptyFolder ) {
                                    return '<div title="' + text + '（空文件夹）">' + val + '</div>';
                                }
                                return '<div title="' + text + '">' + val + '</div>';
                            }
                        },
                        editor: {
                            type:'textbox',
                        },
                    },
                ]],
                ctrlSelect: true,
                singleSelect: true,
                onAfterEdit: function(row, changes) {
                    if( self.isSave ) {//重命名或新建文件夹
                        if( self.mkdir ) {
                            if( !changes.fileName || !changes.fileName.replace(/\s/g, '') ) {
                                changes.fileName = '新建文件夹';
                            }
                        } else {
                            if( !changes.fileName || !changes.fileName.replace(/\s/g, '') ) {
                                //console.log('无改变');
                                return;
                            }
                        }
                        var path = self.getCurrSelectedFilePathInHDFS();
                        //console.log('path:');
                        //console.log(path);
                        var newName = changes.fileName;
                        var url = '';
                        var params = {};
                        var opName = '';
                        if( self.mkdir ) {//新建文件夹
                            //console.log('新建文件夹');
                            url = mkdirHdfsFileRequestUrl;
                            params.dirName = newName;
                            params.rootPath = path;
                            opName = '新建文件夹';
                        } else {//重命名
                            //console.log('重命名');
                            url = renameHdfsFileRequestUrl;
                            params.newName = newName;
                            params.orginalFilePath = self.orginalFilePath;
                            opName = '重命名';
                        }
                        //console.log(params);
                        $.ajax({
                            type: 'get',
                            url: url,
                            data: params,
                            dataType: 'json',
                            success: function(resp) {
                                if( resp.meta.success ) {
                                    row.hasSave = true;
                                    self.$hdfsDirgrid.treegrid('acceptChanges');
                                    self.$hdfsDirgrid.treegrid('reload');
                                } else {
                                    self.$hdfsDirgrid.treegrid('rejectChanges');
                                    if( self.mkdir ) {
                                        self.$hdfsDirgrid.treegrid('remove', self.lastRowId);
                                    }
                                    row.hasSave = false;
                                    if( resp.meta.message ) {
                                        $.messager.alert('温馨提示', resp.meta.message, 'warning');
                                    } else {
                                        $.messager.alert('温馨提示', opName + '失败！', 'warning');
                                    }
                                }
                            }
                        });
                    }
                },
                onClickCell: function(field, row) {
                    if( self.lastRowId != row.fileId) {
                        $('#new_dir_tools').hide();
                        $(this).treegrid('endEdit', self.lastRowId);
                    }
                },
                onDblClickCell: function(field, row) {
                    if( row.fileId=='1' && row.fileName=='全部文件' ) {
                        $.messager.alert('温馨提示', '根目录无法重命名！', 'warning');
                        return;
                    }
                    self.mkdir = false;
                    self.lastRowId = row.fileId;
                    $('#new_dir_tools').show();
                    self.orginalFilePath = row.rootPath + row.fileName;
                    $(this).treegrid('beginEdit', row.fileId);
                },
                onBeforeLoad: function(row,param){
                    if (!row) {    // load top level rows
                        param.id = 0;    // set id=0, indicate to load new page rows
                        param.isRoot=true;
                    }
                },
                onBeforeExpand: function(row) {
                    if (row) {    // load top level rows
                        var options = self.$hdfsDirgrid.treegrid('options');
                        var param = options.queryParams;
                        param.id = row.fileId;
                        param.expandNodeFilePath = row.rootPath + row.fileName;
                    }
                },
                onDblClickRow: function(row) {
                    //console.log(self.getFullPathInHDFS(row));
                },
            });

            $('#new_dir_tools').find('#save_new_dir').unbind('click');
            $('#new_dir_tools').find('#save_new_dir').bind('click', function() {
                self.isSave = true;
                $('#new_dir_tools').hide();
                self.$hdfsDirgrid.treegrid('endEdit', self.lastRowId);

            });
            $('#new_dir_tools').find('#cancel_new_dir').unbind('click');
            $('#new_dir_tools').find('#cancel_new_dir').bind('click', function() {
                self.isSave = false;
                $('#new_dir_tools').hide();
                if( self.mkdir ) {
                    self.$hdfsDirgrid.treegrid('remove', self.lastRowId);
                }
                self.$hdfsDirgrid.treegrid('cancelEdit', self.lastRowId);
            });
        },

        /**
         * 在hdfs中新建文件夹
         */
        createNewDirInHdfs: function($grid) {
            this.initSelfAndDialog();

            console.log('hdfs: 新建文件夹')
            var newDirFileId = self.getLastIndex($grid);
            var $currGrid = !$grid ? self.$hdfsDirgrid : $grid;
            if( newDirFileId != '-1' ) {
                var selected = $currGrid.treegrid('getSelected');
                var parentId = '1';
                if( selected ) {
                    if( !selected.isFolder ) {
                        $.messager.alert('温馨提示', '文件不是路径，请选择正确的路径来新增文件夹！', 'warning');
                        return;
                    }
                    parentId = selected.fileId;
                }
                var newDirNode = {
                    fileId: newDirFileId,
                    fileName: '新建文件夹',
                    parentId: parentId,
                    iconCls: 'icon-tree-folder',
                };
                $currGrid.treegrid('append', {
                    parent: parentId,
                    data: [newDirNode]
                });
                $currGrid.treegrid('expand', parentId);//展开节点
                $currGrid.treegrid('endEdit', self.lastRowId);
                self.lastRowId = newDirNode.fileId;
                if( $grid ) {
                    self.hdsfBrowser_lastRow = newDirNode.fileId;
                }
                self.mkdir = true;
                $('#new_dir_tools').show();
                $('span#mkdir_op_span').show();
                $currGrid.treegrid('beginEdit', newDirNode.fileId);
            }
        },
        getLastIndex: function($grid) {
            this.initSelfAndDialog();
            var lastIndex = -1;

            var $currGrid = !$grid ? self.$hdfsDirgrid : $grid;
            var root = $currGrid.treegrid('find', 1);
            if( root && root.lastIndex ) {
                lastIndex = root.lastIndex++;
            }
            return lastIndex;
        },
        /**
         * 获取当前选中的文件（文件夹）hdfs中的全路径
         * @returns {*}
         */
        getCurrSelectedFilePathInHDFS: function($grid) {
            this.initSelfAndDialog();

            var $currGrid = !$grid ? self.$hdfsDirgrid : $grid;
            var selected = $currGrid.treegrid('getSelected');
            return self.getFullPathInHDFS(selected, $grid);
        },
        /**
         * 递归获取选中文件（文件夹）hdfs中的全路径
         * @param row
         * @returns {*}
         */
        getFullPathInHDFS: function(row, $grid) {
            //this.initSelfAndDialog();
            if( row ) {
                if( row.parentId == '1' ) {
                    return '/' + row.fileName;
                }
                if( row.fileId != '1' ) {
                    var $currGrid = !$grid ? self.$hdfsDirgrid : $grid;
                    //console.log(self.$hdfsDirgrid.treegrid('find', row.parentId));
                    var pRow = $currGrid.treegrid('find', row.parentId);
                    var fullPath = self.getFullPathInHDFS(pRow, $grid) + '/' + row.fileName;
                    return fullPath;
                } else {
                    return '/';
                }
            } else {
                return '';
            }
        },

        /**
         * 打开任务监控菜单栏并显示当前任务监控信息
         */
        openCurrJobMonitor : function() {
            var $tab = $('#centerTab', parent.document);
            //base.openAjaxTab($tab, '38', '任务监控', 'hadoop/monitor', function() {
            //    console.log('打开当前任务监控窗口');
            //    $('a#show_curr_job_a').click();
            //});
        },
    };

    module.exports = new Hadoop();
});