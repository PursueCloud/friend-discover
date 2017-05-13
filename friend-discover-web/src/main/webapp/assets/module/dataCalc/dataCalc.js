define(function(require, exports, module) {
    var base = require("module/base/main.js");
    var extend = require("module/base/extend.js");
    require("echarts");
    var utils = require("module/base/utils.js");
    var hadoop = require("module/hadoop/hadoop.js");

    var pageRootUrl = 'pages/dataCalc/';
    var requestRootUrl = 'dataCalc/';
    //page urls
    var distanceCalcPageUrl = pageRootUrl + 'distanceCalc.jsp';
    var bestDCCalcPageUrl = pageRootUrl + 'bestDCCalc.jsp';
    var execClusterPageUrl = pageRootUrl + 'execCluster.jsp';
    var decisChartPageUrl = pageRootUrl + 'decisChart.jsp';
    var showDecisChartPageUrl = pageRootUrl + 'showDecisChart.jsp';
    var findClusterCenterPageUrl = pageRootUrl + 'findClusterCenter.jsp';
    var classifyPageUrl = pageRootUrl + 'classify.jsp';
    var execClassifyPageUrl = pageRootUrl + 'execClassify.jsp';
    var showOneMonitorDataPageUrl = 'pages/hadoop/currJobMonitor.jsp';
    var putClassifyData2DBPageUrl = pageRootUrl + 'putClassifyData2DB.jsp';
    //request urls
    var calcDistanceRequestUrl = requestRootUrl + 'calcDistance';//距离计算请求url
    var findBestDCRequestUrl = requestRootUrl + 'findBestDC';//寻找最佳DC请求url
    var putClassifyData2DBRequetsUrl = requestRootUrl + 'putClassifyData2DB';
    var getDecisChartDataRequestUrl = requestRootUrl + 'getDecisChartData';
    var drawDecisionChartRequestUrl = requestRootUrl + 'drawDecisionChart';
    var self;

    /**
     * 表单验证回调行数（主要用于footButton保存按钮handler
     * param paramJson {
     *      onSubmitErrorTipsCallback :如果为空，则只需默认提示
     *      successCallback: 表单提交后返回成功后执行的回调函数，参数为返回的数据（resp）
       }
     */
    function formValidateFunc(paramJson) {
        self.$form.form('submit', {
            onSubmit: function() {
                if( self.$form.form('validate') ) {
                    return true;
                } else {
                    if( paramJson && paramJson.onSubmitErrorTipsCallback ) {
                        var onSubmitErrorTipsCallback = paramJson.onSubmitErrorTipsCallback;
                        onSubmitErrorTipsCallback();
                    } else {
                        $.messager.alert('温馨提示', '您还有必须输入的项未输入，请确认！', 'warning');
                    }
                    return false;
                }
            },
            success: function(resp) {
                resp = JSON.parse(resp);
                if( paramJson && paramJson.successCallback ) {
                    var successCallback = paramJson.successCallback;
                    successCallback(resp);
                }
            }
        })
    }
    //定义属性
    var DataCalc = function() {
        this.$dialog;
        this.$form;
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
    DataCalc.prototype = {
        constructor: DataCalc,

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

        preCalc: function() {
            this.initSelfAndDialog();
            self.initPreCalcPageEvent();
        },
        initPreCalcPageEvent: function() {
            this.initSelfAndDialog();

            $('a#distance_calc_a').unbind('click');
            $('a#distance_calc_a').bind('click', function() {
                //console.log('距离计算');
                self.footButtons[0].handler = function() {
                    formValidateFunc({
                        onSubmitErrorTipsCallback: function() {
                            if( $('#discCalc_input_box').length && !$('#discCalc_input_box').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输入路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else {
                                $.messager.alert('温馨提示', '输出路径(HDFS)不能为空，请选择或输入！', 'warning');
                            }
                        },
                        successCallback: function(resp) {//resp已经为json格式
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '距离计算-MR任务启动成功！', 'info', function() {
                                    if( resp.data && resp.data.monitor ) {//打开mr任务监听窗口
                                        //self.$dialog.dialog({
                                        //    title: '当前MR任务监控',
                                        //    href: showOneMonitorDataPageUrl,
                                        //    width: '500px',
                                        //    height: '500px',
                                        //    button: [{
                                        //        text: '退出',
                                        //        iconCls: 'icon-cancel',
                                        //        handler: function() {
                                        //            self.$dialog.dialog('close');
                                        //        },
                                        //    }],
                                        //    modal: true,
                                        //    queryParams: {},
                                        //    maximizable: true,
                                        //    resizable: true,
                                        //});
                                        hadoop.openCurrJobMonitor();
                                        //alert('打开任务监控！')
                                    }
                                });
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', '距离计算-MR任务启动失败，原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '距离计算-MR任务启动失败！', 'warning');
                                }
                            }
                        }
                    });
                };
                self.$dialog.dialog({
                    title: '距离计算',
                    href: distanceCalcPageUrl,
                    width: '400px',
                    height: '250px',
                    modal: true,
                    queryParams: {},
                    buttons: self.footButtons,
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('#discCalc_input_box').siblings('span.textbox').find('a.textbox-button').unbind('click');
                        $('#discCalc_input_box').siblings('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径');
                            hadoop.chooseFilesInHdfs('#discCalc_input_box', '请选择输入路径（hdfs文件或文件夹）');
                        });
                        $('#discCalc_output_box').siblings('span.textbox').find('a.textbox-button').unbind('click');
                        $('#discCalc_output_box').siblings('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输出路径');
                            hadoop.chooseDirsInHdfs('#discCalc_output_box', '请选则输出路径（hdfs文件夹）');
                        });
                    }
                });
            });
            $('a#bestDC_calc_a').unbind('click');
            $('a#bestDC_calc_a').bind('click', function() {
                //console.log('最佳DC计算');
                self.footButtons[0].handler = function() {
                    formValidateFunc({
                        onSubmitErrorTipsCallback: function() {
                            if( $('#discCalc_input_box').length && !$('#discCalc_input_box').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输入路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else if( $('#input_recd_num').length && !$('#input_recd_num').numberspinner('getValue') ){
                                $.messager.alert('温馨提示', '输入记录数不能为空，请输入！', 'warning');
                            } else {
                                $.messager.alert('温馨提示', '阈值百分比不能为空，请输入！', 'warning');
                            }
                        },
                        successCallback: function(resp) {//resp已经为json格式
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '最佳DC计算成功，最佳DC的值为：' + resp.data, 'info');
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', '最佳DC计算失败，原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '最佳DC计算失败！', 'warning');
                                }
                            }
                        }
                    });
                };
                self.$dialog.dialog({
                    title: '最佳DC计算',
                    href: bestDCCalcPageUrl,
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
                        $('td.bestDC-input-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.bestDC-input-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径');
                            hadoop.chooseFilesInHdfs('#bestDC_input_path', '请选择输入路径（hdfs文件或文件夹）');
                        });
                    }
                });
            });
        },

        distanceCalc: function() {
            this.initSelfAndDialog();

            self.$form = $('#dist_form');
        },

        bestDCCalc: function() {
            this.initSelfAndDialog();

            self.$form = $('#bestDC_form');
        },

        clusterCalc: function() {
            this.initSelfAndDialog();
            self.initClusterCalcPageEvent();
        },
        initClusterCalcPageEvent: function() {
            this.initSelfAndDialog();

            $('a#exec_cluster_a').unbind('click');
            $('a#exec_cluster_a').bind('click', function() {
                //console.log('执行聚类');
                self.footButtons[0].handler = function() {
                    formValidateFunc({
                        onSubmitErrorTipsCallback: function() {
                            if( $('#cluster_input_path').length && !$('#cluster_input_path').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输入路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else if( $('#distance_threshold_num').length && !$('#distance_threshold_num').numberspinner('getValue') ) {
                                $.messager.alert('温馨提示', '距离阈值不能为空，请输入！', 'warning');
                            } else if( $('#consistency_calc_algorithm').length && !$('#consistency_calc_algorithm').combobox('getValue') ) {
                                $.messager.alert('温馨提示', '请选择一种密度计算算法！', 'warning');
                            } else if( $('#part_consistency_mr_reducer_num').length && !$('#part_consistency_mr_reducer_num').numberspinner('getValue') ) {
                                $.messager.alert('温馨提示', '局部密度MR Reducer个数不能为空，请输入！', 'warning');
                            } else if( $('#min_distance_mr_reducer_num').length && !$('#min_distance_mr_reducer_num').numberspinner('getValue') ) {
                                $.messager.alert('温馨提示', '最小距离MR Reducer个数不能为空，请输入！', 'warning');
                            } else {
                                $.messager.alert('温馨提示', '排序MR Reducer个数不能为空，请输入！', 'warning');
                            }
                        },
                        successCallback: function(resp) {//resp已经为json格式
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '聚类计算-MR任务启动成功！', 'info', function() {
                                    if( resp.data && resp.data.monitor ) {//打开mr任务监听窗口
                                        //self.$dialog.dialog({
                                        //    title: '当前MR任务监控',
                                        //    href: showOneMonitorDataPageUrl,
                                        //    width: '500px',
                                        //    height: '500px',
                                        //    button: [{
                                        //        text: '退出',
                                        //        iconCls: 'icon-cancel',
                                        //        handler: function() {
                                        //            self.$dialog.dialog('close');
                                        //        },
                                        //    }],
                                        //    modal: true,
                                        //    queryParams: {},
                                        //    maximizable: true,
                                        //    resizable: true,
                                        //});
                                        hadoop.openCurrJobMonitor();
                                        //alert('打开任务监控！')
                                    }
                                });
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', '聚类计算-MR任务启动失败，原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '聚类计算-MR任务启动失败！', 'warning');
                                }
                            }
                        }
                    });
                };
                self.$dialog.dialog({
                    title: '执行聚类',
                    href: execClusterPageUrl,
                    width: '450px',
                    height: '410px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: [{
                        text: '重置',
                        iconCls: 'icon-reload',
                        handler: function() {

                        },
                    }, self.footButtons[0], self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('td.cluster-input-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.cluster-input-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径');
                            hadoop.chooseFilesInHdfs('#cluster_input_path', '请选择输入路径（hdfs文件或文件夹）');
                        });
                    }
                });
            });
            $('#show_decision_chart_a').unbind('click');
            $('#show_decision_chart_a').bind('click', function() {
                console.log('显示决策图');
                self.$dialog.dialog({
                    title: '显示决策图',
                    href: showDecisChartPageUrl,
                    width: '500px',
                    height: '560px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: [self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    },
                });
            });
            $('a#find_cluster_center_a').unbind('click');
            $('a#find_cluster_center_a').bind('click', function() {
                //console.log('寻找聚类中心');
                self.footButtons[0].handler = function() {
                    formValidateFunc({
                        onSubmitErrorTipsCallback: function() {
                            if( $('#part_consistency_threshold_num').length && !$('#part_consistency_threshold_num').numberspinner('getValue') ) {
                                $.messager.alert('温馨提示', '局部密度阈值不能为空，请输入！', 'warning');
                            } else if( $('#min_distance_threshold_num').length && !$('#min_distance_threshold_num').numberspinner('getValue') ) {
                                $.messager.alert('温馨提示', '最小距离阈值不能为空，请输入！', 'warning');
                            } else if( $('#cluster_center_input_path').length && !$('#cluster_center_input_path').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输入路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else if( $('#cluster_center_output_path').length && !$('#cluster_center_output_path').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输出路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else {
                                $.messager.alert('温馨提示', '输出路径(本地)不能为空，请选择！', 'warning');
                            }
                        },
                        successCallback: function(resp) {//resp已经为json格式
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '寻找聚类中心-MR任务启动成功！', 'info', function() {
                                    if( resp.data && resp.data.monitor ) {//打开mr任务监听窗口
                                        //self.$dialog.dialog({
                                        //    title: '当前MR任务监控',
                                        //    href: showOneMonitorDataPageUrl,
                                        //    width: '500px',
                                        //    height: '500px',
                                        //    button: [{
                                        //        text: '退出',
                                        //        iconCls: 'icon-cancel',
                                        //        handler: function() {
                                        //            self.$dialog.dialog('close');
                                        //        },
                                        //    }],
                                        //    modal: true,
                                        //    queryParams: {},
                                        //    maximizable: true,
                                        //    resizable: true,
                                        //});
                                        hadoop.openCurrJobMonitor();
                                        //alert('打开任务监控！')
                                    }
                                });
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', '寻找聚类中心-MR任务启动失败，原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '寻找聚类中心-MR任务启动失败！', 'warning');
                                }
                            }
                        }
                    });
                };
                self.$dialog.dialog({
                    title: '寻找聚类中心',
                    href: findClusterCenterPageUrl,
                    width: '400px',
                    height: '350px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: self.footButtons,
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('td.cluster-cener-input-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.cluster-cener-input-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径（HDFS）');
                            hadoop.chooseFilesInHdfs('#cluster_center_input_path', '请选择输入路径（hdfs文件或文件夹）');
                        });
                        $('td.cluster-cener-output-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.cluster-cener-output-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输出路径（HDFS）');
                            hadoop.chooseDirsInHdfs('#cluster_center_output_path', '请选择输出路径（hdfs文件夹）');
                        });
                        $('td.local-data-path-td').find('span.textbox').find('input[type="file"]').attr("webkitdirectory", "webkitdirectory");//选择文件夹
                    }
                });
            });
            $('a#classify_a').unbind('click');
            $('a#classify_a').bind('click', function() {
                //console.log('分类');
                self.$dialog.dialog({
                    title: '分类',
                    href: classifyPageUrl,
                    width: '400px',
                    height: '200px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: [self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    },
                });
            });
        },

        execCluster: function() {
            this.initSelfAndDialog();

            self.$form = $('#ect_form');
        },

        decisChart: function() {
            this.initSelfAndDialog();
            self.initDecisChartPageEvent();
        },
        initDecisChartPageEvent: function() {
            this.initSelfAndDialog();

            $('#calc_decis_data_a').unbind('click');
            $('#calc_decis_data_a').bind('click', function() {
                $('div.progress-div').show();

                var progInterval = setInterval(function() {
                    var currVal = $('#prog_bar').progressbar('getValue');
                    if( currVal < 100 ) {
                        $('#prog_bar').progressbar('setValue', currVal+20)
                    } else {
                        $('#prog_bar').progressbar('setValue', 100)
                        clearInterval(progInterval);
                        $('img#ok_img').show();
                        $('img#error_img').hide();
                    }

                }, 500);
            });
            $('#show_decision_chart_a').unbind('click');
            $('#show_decision_chart_a').bind('click', function() {
                console.log('显示决策图');
                self.$dialog.dialog({
                    title: '显示决策图',
                    href: showDecisChartPageUrl,
                    width: '500px',
                    height: '500px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: [{
                        text: '返回',
                        iconCls: 'icon-back',
                        handler: function() {
                            self.$dialog.dialog({
                                title: '决策图',
                                href: decisChartPageUrl,
                                width: '400px',
                                height: '300px',
                                modal: true,
                                //openAnimation: 'fade',
                                //closeAnimation: 'fade',
                                queryParams: {},
                                buttons: self.footButtons,
                                maximizable: true,
                                resizable: true,
                                onLoad: function() {
                                    self.$dialog.dialog('center');
                                },
                            });
                        },
                    }, self.footButtons[0], self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                    },
                });
            });
        },

        showDecisChart: function() {
            this.initSelfAndDialog();

            var proInterval = self.startProgress({
                wrapperSelector: '.progress-div',
                progressbarSelector: '#prog_bar'
            });
            $.ajax({
                type: 'get',
                url: drawDecisionChartRequestUrl,
                dataType: 'json',
                success: function(resp) {
                    self.finishProgress({
                        progInterval: proInterval,
                        progressbarSelector: '#prog_bar',
                    });
                    $('div.progress-div').hide();
                    if( resp.meta.success ) {
                        $('#dc_chart_img').attr('src', resp.data.path);
                    } else {
                        if( resp.meta.message ) {
                            $.messager.alert('温馨提示', '决策图显示失败：原因：' + resp.meta.message, 'warning');
                        } else {
                            $.messager.alert('温馨提示', '决策图显示失败！', 'warning');
                        }
                    }
                }
            })

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
        findClusterCenter: function() {
            this.initSelfAndDialog();

            self.$form = $('#fcc_form');
        },

        classify: function() {
            this.initSelfAndDialog();
            self.initClassifyPageEvent();
        },
        initClassifyPageEvent: function() {
            this.initSelfAndDialog();

            $('#exec_classify_a').unbind('click');
            $('#exec_classify_a').bind('click', function() {
                self.footButtons[0].handler = function() {
                    formValidateFunc({
                        onSubmitErrorTipsCallback: function() {
                            if( $('#classify_input_path').length && !$('#classify_input_path').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输入路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else if( $('#classify_output_path').length && !$('#classify_output_path').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输出路径(HDFS)不能为空，请选择或输入！', 'warning');
                            } else if( $('#distance_threshold_num').length && !$('#distance_threshold_num').numberspinner('getValue') ) {
                                $.messager.alert('温馨提示', '距离阈值不能为空，请输入！', 'warning');
                            } else {
                                $.messager.alert('温馨提示', '聚类中心数不能为空，请输入！', 'warning');
                            }
                        },
                        successCallback: function(resp) {//resp已经为json格式
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '执行分类-MR任务启动成功！', 'info', function() {
                                    if( resp.data && resp.data.monitor ) {//打开mr任务监听窗口
                                        //self.$dialog.dialog({
                                        //    title: '当前MR任务监控',
                                        //    href: showOneMonitorDataPageUrl,
                                        //    width: '500px',
                                        //    height: '500px',
                                        //    button: [{
                                        //        text: '退出',
                                        //        iconCls: 'icon-cancel',
                                        //        handler: function() {
                                        //            self.$dialog.dialog('close');
                                        //        },
                                        //    }],
                                        //    modal: true,
                                        //    queryParams: {},
                                        //    maximizable: true,
                                        //    resizable: true,
                                        //});
                                        hadoop.openCurrJobMonitor();
                                    }
                                });
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', '执行分类-MR任务启动失败，原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '执行分类-MR任务启动失败！', 'warning');
                                }
                            }
                        }
                    });
                };
                self.$dialog.dialog({
                    title: '执行分类',
                    href: execClassifyPageUrl,
                    width: '400px',
                    height: '300px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: [{
                        text: '返回',
                        iconCls: 'icon-back',
                        handler: function() {
                            self.$dialog.dialog({
                                title: '分类',
                                href: classifyPageUrl,
                                width: '400px',
                                height: '200px',
                                modal: true,
                                //openAnimation: 'fade',
                                //closeAnimation: 'fade',
                                queryParams: {},
                                buttons: self.footButtons,
                                maximizable: true,
                                resizable: true,
                                onLoad: function() {
                                    self.$dialog.dialog('center');
                                }
                            });
                        },
                    }, self.footButtons[0], self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('td.classify-input-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.classify-input-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径');
                            hadoop.chooseFilesInHdfs('#classify_input_path', '请选择输入路径（hdfs文件或文件夹）');
                        });
                        $('td.classify-output-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.classify-output-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输出路径');
                            hadoop.chooseDirsInHdfs('#classify_output_path', '请选择输出路径（hdfs文件夹）');
                        });
                    }
                });
            });
            $('#exp_classify_data_a').unbind('click');
            $('#exp_classify_data_a').bind('click', function() {
                alert('下载分类数据。。。');
            });
            $('#put_classify_data_into_db_a').unbind('click');
            $('#put_classify_data_into_db_a').bind('click', function() {
                //alert('分类数据入库。。。');
                self.footButtons[0].handler = function() {
                    formValidateFunc({
                        onSubmitErrorTipsCallback: function() {
                            if( $('#classify_data_input_path').length && !$('#classify_data_input_path').textbox('getValue') ) {
                                $.messager.alert('温馨提示', '输入路径(HDFS)不能为空，请选择或输入！', 'warning');
                            }
                        },
                        successCallback: function(resp) {//resp已经为json格式
                            if( resp.meta.success ) {
                                $.messager.alert('温馨提示', '分类数据入库成功!', 'info');
                            } else {
                                if( resp.meta.message ) {
                                    $.messager.alert('温馨提示', '分类数据入库失败，原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '分类数据入库失败！', 'warning');
                                }
                            }
                        }
                    });
                };
                self.$dialog.dialog({
                    title: '分类数据入库',
                    href: putClassifyData2DBPageUrl,
                    width: '400px',
                    height: '200px',
                    modal: true,
                    //openAnimation: 'fade',
                    //closeAnimation: 'fade',
                    queryParams: {},
                    buttons: [{
                        text: '返回',
                        iconCls: 'icon-back',
                        handler: function() {
                            self.$dialog.dialog({
                                title: '分类',
                                href: classifyPageUrl,
                                width: '400px',
                                height: '200px',
                                modal: true,
                                //openAnimation: 'fade',
                                //closeAnimation: 'fade',
                                queryParams: {},
                                buttons: self.footButtons,
                                maximizable: true,
                                resizable: true,
                                onLoad: function() {
                                    self.$dialog.dialog('center');
                                }
                            });
                        },
                    }, self.footButtons[0], self.footButtons[1]],
                    maximizable: true,
                    resizable: true,
                    onLoad: function() {
                        self.$dialog.dialog('center');
                        //重新绑定“浏览”按钮点击事件
                        $('td.classify-data-input-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                        $('td.classify-data-input-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                            //alert('选择输入路径');
                            hadoop.chooseFilesInHdfs('#classify_data_input_path', '请选择输入路径（hdfs文件或文件夹）');
                        });
                    }
                });
            });
        },

        execClassify: function() {
            this.initSelfAndDialog();

            self.$form = $('#ecf_form');
        },

        putClassifyData2DB: function() {
            this.initSelfAndDialog();

            self.$form = $('#pcf_form');
        },
    };

    module.exports = new DataCalc();
});