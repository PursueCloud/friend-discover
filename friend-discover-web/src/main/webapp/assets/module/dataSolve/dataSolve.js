define(function(require, exports, module) {
    var base = require("module/base/main.js");
    var extend = require("module/base/extend.js");
    var hadoop = require("module/hadoop/hadoop.js");

    var pageRootUrl = 'pages/dataSolve/';
    var requestRootUrl = 'dataSolve/';
    //page urls
    var chooseFileOrEnterPathUrl = pageRootUrl + 'chooseFileOrEnterPath.jsp';
    var deduplicatePageUrl = pageRootUrl + 'deduplicate.jsp';
    var filterExpPageUrl = pageRootUrl + 'filterExp.jsp';
    var browserHdfsFilesOrDirsPageUrl = 'pages/hadoop/browserHdfsFilesOrDirs.jsp';
    var showOneMonitorDataPageUrl = 'pages/hadoop/currJobMonitor.jsp';
    //request urls
    var uploadFile2HDFSRequestUrl = 'hadoop/uploadFile2HDFS';
    var downloadFileFromHDFSRequestUrl = 'hadoop/downloadFileFromHDFS';

    var self;

    //定义属性
    var DataSolve = function() {
        this.$dialog;
        this.$dialog2;
        this.$form;
        this.footButtons = [{
                text: '保存',
                iconCls: 'icon-save',
                handler: function() {
                    self.$form.form('submit', {
                        onSubmit: function() {
                            if( self.$form.form('validate') ) {
                                return true;
                            } else {
                                //console.log($('#deduplicate_input_box').length);
                                if( $('#deduplicate_input_box').length && !$('#deduplicate_input_box').textbox('getValue') ) {
                                    $.messager.alert('温馨提示', '请选择输入路径', 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '请选择输出路径', 'warning');
                                }
                                return false;
                            }
                        },
                        success: function(resp) {
                            resp = JSON.parse(resp);
                            if( resp.meta.success ) {
                                if( resp.data &&resp.data.monitor ) {
                                    //self.$dialog2.dialog({
                                    //    title: '当前MR任务监控',
                                    //    href: showOneMonitorDataPageUrl,
                                    //    width: '500px',
                                    //    height: '500px',
                                    //    button: [{
                                    //        text: '退出',
                                    //        iconCls: 'icon-cancel',
                                    //        handler: function() {
                                    //            self.$dialog2.dialog('close');
                                    //        },
                                    //    }],
                                    //    modal: true,
                                    //    queryParams: {},
                                    //    maximizable: true,
                                    //    resizable: true,
                                    //    onClose: function() {
                                    //        console.log('停止任务监控轮询!');
                                    //        //clearInterval(monitorOneInterval);
                                    //    }
                                    //});
                                    hadoop.openCurrJobMonitor();
                                    //alert('打开任务监控！')
                                } else {
                                    $.messager.alert('温馨提示', self.taskName + '任务执行成功！', 'info');
                                }
                            } else {
                                $.messager.alert('温馨提示', self.taskName + '任务执行失败！', 'info');
                            }
                        }
                    })
                },
            }];
    };
    //定义函数
    DataSolve.prototype = {
        constructor: DataSolve,

        /**
         * 检查self和dialog变量，并初始化
         */
        initSelfAndDialog: function(dialogSelector1, dialogSelector2) {
            if( !self ) {
                self = this;
            }
            if( !self.$dialog || !self.$dialog.length ) {
                self.$dialog = $(dialogSelector1);
            }
            if( !self.$dialog2 || !self.$dialog2.length ) {
                self.$dialog2 = $(dialogSelector2);
            }
        },

        fileSolve: function() {
            this.initSelfAndDialog('#fs_dialog_win', '#fs_dialog_win2');
            self.initFileSolvePageEvent();
        },
        /**
         * 初始化选择（待上传或待下载）文件按钮事件
         */
        initFileSolvePageEvent: function() {
            this.initSelfAndDialog('#fs_dialog_win', '#fs_dialog_win2');

            $('a#choose_or_enter_path_upload_a').unbind('click');
            $('a#choose_or_enter_path_upload_a').bind('click', function() {
                console.log('选择上传文件');
                //self.fileSolveType = '0';//文件处理类型为0，即上传
                hadoop.openChooseFileOrEnterPathDialog(self.$dialog, null, '250px', '0');
            });
            $('a#choose_or_enter_path_download_a').unbind('click');
            $('a#choose_or_enter_path_download_a').bind('click', function() {
                console.log('选择下载文件');
                //self.fileSolveType = '1';//文件处理类型为1，即下载
                hadoop.openChooseFileOrEnterPathDialog(self.$dialog, '选择待下载的文件', null, '1');
            });

        },

        dataSolve: function() {
            this.initSelfAndDialog('#ds_dialog_win', '#ds_dialog_win2');
            self.initDataSolvePageEvent();
        },
        initDataSolvePageEvent: function() {
            this.initSelfAndDialog('#ds_dialog_win', '#ds_dialog_win2');

            $('a#deduplicate_a').unbind('click');
            $('a#deduplicate_a').bind('click', function() {
                self.$dialog.dialog({
                    title: '去重',
                    href: deduplicatePageUrl,
                    width: '400px',
                    height: '300px',
                    modal: true,
                    queryParams: {},
                    buttons: self.footButtons,
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('#deduplicate_input_box').siblings('span.textbox').find('a.textbox-button').unbind('click');
                        $('#deduplicate_input_box').siblings('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径');
                            hadoop.chooseFilesInHdfs('#deduplicate_input_box', '选择输入路径（hdfs, 文件或文件夹）');
                        });
                        $('#output_box').siblings('span.textbox').find('a.textbox-button').unbind('click');
                        $('#output_box').siblings('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输出路径');
                            hadoop.chooseDirsInHdfs('#output_box', '选择输出路径（hdfs，文件夹）');
                        });
                    }
                });
            });
            $('a#filter_exp_a').unbind('click');
            $('a#filter_exp_a').bind('click', function() {
                console.log('过滤导出');
                self.$dialog.dialog({
                    title: '过滤导出',
                    href: filterExpPageUrl,
                    width: '400px',
                    height: '300px',
                    modal: true,
                    queryParams: {},
                    buttons: self.footButtons,
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('tr.exp-path-tr').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('tr.exp-path-tr').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            console.log('选择输出路径');
                            //self.chooseDirsInHdfs('#output_box', '请选择导出路径（hdfs）');
                        });
                    }
                });
            });
        },

        deduplicate: function() {
            this.initSelfAndDialog('#dd_dialog_win', '#dd_dialog_win2');

            self.taskName = '去重';
            self.$form = $('#deduplicate_form');
        },

        filterExp: function() {
            this.initSelfAndDialog('#fe_dialog_win', '#fe_dialog_win2');

            self.taskName = '过滤导出';
            self.$form = $('#filterExp_form');
            self.initFilterExpPageEvent();
        },
        initFilterExpPageEvent: function() {
            this.initSelfAndDialog('#fe_dialog_win', '#fe_dialog_win2');

            $('input[name="expLoc"]').unbind('click');
            $('input[name="expLoc"]').bind('click', function() {
                if( $(this).val() == '0' ) {
                    $('#output_box').textbox({required: false});
                    $('tr.exp-path-tr').hide();
                    $('tr.exp-path-tr-br').hide();
                } else {
                    $('#output_box').textbox({required: true});
                    $('tr.exp-path-tr').show();
                    $('tr.exp-path-tr-br').show();
                }
                $('tr.exp-path-tr').find('span.textbox').find('a.textbox-button').unbind('click');
                $('tr.exp-path-tr').find('span.textbox').find('a.textbox-button').bind('click', function() {
                    //console.log('选择输出路径');
                    hadoop.chooseDirsInHdfs('#output_box', '请选择导出路径（hdfs）');
                });
            });
        },
    };

    module.exports = new DataSolve();
});