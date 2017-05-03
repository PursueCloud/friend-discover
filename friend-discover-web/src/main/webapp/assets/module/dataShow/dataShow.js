define(function(require, exports, module) {
    var base = require("module/base/main.js");
    var extend = require("module/base/extend.js");
    var hadoop = require("module/hadoop/hadoop.js");
    require("echarts");

    var pageRootUrl = 'pages/dataShow/';//页面url根目录
    var requestRootUrl = 'dataShow/';//请求url跟目录
    //page urls
    //request urls
    var showClusterCenterFileContRequestUrl = requestRootUrl + 'showClusterCenterFileCont';
    var getUserAgeCensusDataRequestUrl = requestRootUrl + 'getUserAgeCensusData';

    var self;

    //定义属性
    var DataShow = function() {
        this.$dialog;
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
    DataShow.prototype = {
        constructor: DataShow,

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

        showClusterCenterCensus: function() {
            this.initSelfAndDialog();
            self.$form = $('#sccfc_form');
            self.initShowClusterCenterCensusPageEvent();
        },
        initShowClusterCenterCensusPageEvent: function() {
            this.initSelfAndDialog();

            $('input[name="dataLoc"]').unbind('click');
            $('input[name="dataLoc"]').bind('click', function() {
                if( $(this).val() == '0' ) {//数据位置为本地
                    $('input#cluster_data_file').filebox({
                        required:false
                    });//初始化为filebox
                    //$('td.cluster-data-file-path-td').find('span.textbox').find('input[type="hidden"]').attr('name', 'clusterDataFile');
                } else {//数据位置为hdfs
                    $('input#cluster_data_file').textbox({
                        prompt:'请选择或输入文件路径(HDFS)',
                        required:true
                    });//初始化为textbox
                    $('td.cluster-data-file-path-td').find('span.textbox').find('input[type="file"]').attr('name', 'inputPath');
                    $('td.cluster-data-file-path-td').find('span.textbox').find('input[type="file"]').attr('type', 'hidden');
                    //重新绑定“浏览”按钮点击事件
                    $('td.cluster-data-file-path-td').find('span.textbox').find('a.textbox-button').unbind('click');
                    $('td.cluster-data-file-path-td').find('span.textbox').find('a.textbox-button').bind('click', function() {
                        //alert('选择聚类中心数据文件');
                        hadoop.chooseFilesInHdfs('#cluster_data_file', '请选择输入路径（hdfs文件或文件夹）');
                    });
                }
            });
            $('#show_as_word').unbind('click');
            $('#show_as_word').bind('click', function() {
                self.$form.find('#show_type').val(0);
                submitDataShowForm();

            });
            $('#show_as_chart').unbind('click');
            $('#show_as_chart').bind('click', function() {
                self.$form.find('#show_type').val(1);
                submitDataShowForm();
            });

            function submitDataShowForm() {
                var dataLoc = self.$form.find('input[name="dataLoc"]:checked').val();
                var progInterval;
                self.$form.form('submit', {
                    onSubmit: function() {
                        if( self.$form.form('validate') ) {
                            $('#data_type').html('');
                            $('#prog_div').find('img').attr('src', 'assets/images/loading.spanner.gif');
                            $('#prog_div').find('span.loading-status').text('正在加载文件');
                            progInterval = self.startProgress({
                                wrapperSelector: '#prog_div',
                                progressbarSelector: '#load_file_prog',
                            });
                            return true;
                        } else {
                            if( dataLoc =='1' ) {
                                $.messager.alert('温馨提示', '请先选择或输入HDFS文件路径!', 'warning');
                            } else {
                                $.messager.alert('温馨提示', '请先选择文件!', 'warning');
                            }
                            return false;
                        }
                    },
                    success: function(resp) {
                        resp = JSON.parse(resp);
                        self.finishProgress({
                            progInterval: progInterval,
                            progressbarSelector: '#load_file_prog',
                        });
                        if( resp.meta.success ) {
                            $('#prog_div').find('span.loading-status').text('加载成功');
                            $('#prog_div').find('img').attr('src', 'assets/images/ok.png');
                            $('#data_type').html(resp.data.clusterCenterFileCont);
                        } else {
                            $('#prog_div').find('span.loading-status').text('加载失败');
                            $('#prog_div').find('img').attr('src', 'assets/images/error.png');
                            if( resp.meta.message ) {
                                if( dataLoc =='0' ) {
                                    $.messager.alert('温馨提示', '加载文件失败：原因：' + resp.meta.message, 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '显示失败：原因：' + resp.meta.message, 'warning');
                                }
                            } else {
                                if( dataLoc =='0' ) {
                                    $.messager.alert('温馨提示', '加载文件失败!', 'warning');
                                } else {
                                    $.messager.alert('温馨提示', '显示失败!', 'warning');
                                }
                            }
                        }
                    }
                })
            }
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
        showUserDataCensus: function() {
            this.initSelfAndDialog();
            self.initShowUserDataCensusPageEvent();
        },
        initShowUserDataCensusPageEvent: function() {
            this.initSelfAndDialog();
            var perAttrUserChart = echarts.init($('#per_attr_user_chart')[0]);
            perAttrUserChart.showLoading();
            $.get(getUserAgeCensusDataRequestUrl, null, function(resp) {
                resp = JSON.parse(resp);
                perAttrUserChart.hideLoading();
                perAttrUserChart.setOption({
                    title : {
                        text: '用户年龄分布统计图',//各种属性值用户总数占比\n(可以有年龄、常驻地、职业等，\n以下为年龄）
                        //subtext: '纯属虚构',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    toolbox: {
                        show : true,
                        feature : {
                            mark : {show: true},
                            dataView : {show: true, readOnly: false},
                            magicType : {
                                show: true,
                                type: ['pie'],
                                option: {
                                }
                            },
                            restore : {show: true},
                            saveAsImage : {show: true}
                        },
                        bottom:'0'
                    },
                    calculable : true,
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: resp.data.name
                    },
                    series : [
                        {
                            name: '数量(占比)：',
                            type: 'pie',
                            radius : '45%',
                            center: ['50%', '60%'],
                            data: resp.data.data,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                },
                                normal:{
                                    label:{
                                        show: true,
                                        formatter: '{b}\n{c} ({d}%)'
                                    },
                                    labelLine :{show:true}
                                }
                            }
                        }
                    ]
                });
            });

            //var similarAttrUserChart = echarts.init($('#similar_attr_user_chart')[0]);
            //similarAttrUserChart.showLoading();
            //$.get(getSimilarAttrUserDataCensusDataRequestUrl, null, function(resp) {
            //    resp = JSON.parse(resp);
            //    similarAttrUserChart.hideLoading();
            //    similarAttrUserChart.setOption({
            //        title : {
            //            text: '属性相似的用户总数占比',
            //            //subtext: '纯属虚构',
            //            x:'center'
            //        },
            //        tooltip : {
            //            trigger: 'item',
            //            formatter: "{a} <br/>{b} : {c} ({d}%)"
            //        },
            //        legend: {
            //            orient: 'vertical',
            //            left: 'left',
            //            data: ['年龄','常驻地','感兴趣领域']
            //        },
            //        series : [
            //            {
            //                name: '访问来源',
            //                type: 'pie',
            //                radius : '55%',
            //                center: ['50%', '60%'],
            //                data:[
            //                    {value:335, name:'年龄'},
            //                    {value:310, name:'常驻地'},
            //                    {value:234, name:'感兴趣领域'},
            //                ],
            //                itemStyle: {
            //                    emphasis: {
            //                        shadowBlur: 10,
            //                        shadowOffsetX: 0,
            //                        shadowColor: 'rgba(0, 0, 0, 0.5)'
            //                    }
            //                }
            //            }
            //        ]
            //    });
            //});
            //
            //var perFieldUserChart = echarts.init($('#per_field_user_chart')[0]);
            //perFieldUserChart.showLoading();
            //$.get(getPerFieldUserChartDataCensusDataRequestUrl, null, function(resp) {
            //    resp = JSON.parse(resp);
            //    perFieldUserChart.hideLoading();
            //    perFieldUserChart.setOption({
            //        title : {
            //            text: '各领域用户总数占比',
            //            //subtext: '纯属虚构',
            //            x:'center'
            //        },
            //        tooltip : {
            //            trigger: 'item',
            //            formatter: "{a} <br/>{b} : {c} ({d}%)"
            //        },
            //        legend: {
            //            orient: 'vertical',
            //            left: 'left',
            //            data: ['教育','IT','金融', '电力', '通讯']
            //        },
            //        series : [
            //            {
            //                name: '访问来源',
            //                type: 'pie',
            //                radius : '55%',
            //                center: ['50%', '60%'],
            //                data:[
            //                    {value:335, name:'教育'},
            //                    {value:310, name:'IT'},
            //                    {value:234, name:'金融'},
            //                    {value:1548, name:'电力'},
            //                    {value:250, name:'通讯'},
            //                ],
            //                itemStyle: {
            //                    emphasis: {
            //                        shadowBlur: 10,
            //                        shadowOffsetX: 0,
            //                        shadowColor: 'rgba(0, 0, 0, 0.5)'
            //                    }
            //                }
            //            }
            //        ]
            //    });
            //});
        }
    };

    module.exports = new DataShow();
});