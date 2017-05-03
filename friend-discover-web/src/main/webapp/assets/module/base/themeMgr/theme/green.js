define(function() {

var theme = {
    // Ĭ��ɫ��
    color: [
        '#408829','#68a54a','#a9cba2','#86b379',
        '#397b29','#8abb6f','#759c6a','#bfd3b7'
    ],

    // ͼ�����
    title: {
        textStyle: {
            fontWeight: 'normal',
            color: '#408829'
        }
    },
    
    // ֵ��
    dataRange: {
        color:['#1f610a','#97b58d']
    },

    // ������
    toolbox: {
        color : ['#408829','#408829','#408829','#408829']
    },

    // ��ʾ��
    tooltip: {
        backgroundColor: 'rgba(0,0,0,0.5)',
        axisPointer : {            // ������ָʾ���������ᴥ����Ч
            type : 'line',         // Ĭ��Ϊֱ�ߣ���ѡΪ��'line' | 'shadow'
            lineStyle : {          // ֱ��ָʾ����ʽ����
                color: '#408829',
                type: 'dashed'
            },
            crossStyle: {
                color: '#408829'
            },
            shadowStyle : {                     // ��Ӱָʾ����ʽ����
                color: 'rgba(200,200,200,0.3)'
            }
        }
    },

    // �������ſ�����
    dataZoom: {
        dataBackgroundColor: '#eee',            // ���ݱ�����ɫ
        fillerColor: 'rgba(64,136,41,0.2)',   // �����ɫ
        handleColor: '#408829'     // �ֱ���ɫ
    },
    
    // ����
    grid: {
        borderWidth: 0
    },

    // ��Ŀ��
    categoryAxis: {
        axisLine: {            // ��������
            lineStyle: {       // ����lineStyle����������ʽ
                color: '#408829'
            }
        },
        splitLine: {           // �ָ���
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: ['#eee']
            }
        }
    },

    // ��ֵ��������Ĭ�ϲ���
    valueAxis: {
        axisLine: {            // ��������
            lineStyle: {       // ����lineStyle����������ʽ
                color: '#408829'
            }
        },
        splitArea : {
            show : true,
            areaStyle : {
                color: ['rgba(250,250,250,0.1)','rgba(200,200,200,0.1)']
            }
        },
        splitLine: {           // �ָ���
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: ['#eee']
            }
        }
    },

    timeline : {
        lineStyle : {
            color : '#408829'
        },
        controlStyle : {
            normal : { color : '#408829'},
            emphasis : { color : '#408829'}
        }
    },

    // K��ͼĬ�ϲ���
    k: {
        itemStyle: {
            normal: {
                color: '#68a54a',          // ���������ɫ
                color0: '#a9cba2',      // ���������ɫ
                lineStyle: {
                    width: 1,
                    color: '#408829',   // ���߱߿���ɫ
                    color0: '#86b379'   // ���߱߿���ɫ
                }
            }
        }
    },
    
    map: {
        itemStyle: {
            normal: {
                areaStyle: {
                    color: '#ddd'
                },
                label: {
                    textStyle: {
                        color: '#c12e34'
                    }
                }
            },
            emphasis: {                 // Ҳ��ѡ����ʽ
                areaStyle: {
                    color: '#99d2dd'
                },
                label: {
                    textStyle: {
                        color: '#c12e34'
                    }
                }
            }
        }
    },
    
    force : {
        itemStyle: {
            normal: {
                linkStyle : {
                    color : '#408829'
                }
            }
        }
    },
    
    chord : {
        padding : 4,
        itemStyle : {
            normal : {
                borderWidth: 1,
                borderColor: 'rgba(128, 128, 128, 0.5)',
                chordStyle : {
                    lineStyle : {
                        color : 'rgba(128, 128, 128, 0.5)'
                    }
                }
            },
            emphasis : {
                borderWidth: 1,
                borderColor: 'rgba(128, 128, 128, 0.5)',
                chordStyle : {
                    lineStyle : {
                        color : 'rgba(128, 128, 128, 0.5)'
                    }
                }
            }
        }
    },
    
    gauge : {
        axisLine: {            // ��������
            show: true,        // Ĭ����ʾ������show������ʾ���
            lineStyle: {       // ����lineStyle����������ʽ
                color: [[0.2, '#86b379'],[0.8, '#68a54a'],[1, '#408829']], 
                width: 8
            }
        },
        axisTick: {            // ������С���
            splitNumber: 10,   // ÿ��splitϸ�ֶ��ٶ�
            length :12,        // ����length�����߳�
            lineStyle: {       // ����lineStyle����������ʽ
                color: 'auto'
            }
        },
        axisLabel: {           // �������ı���ǩ�����axis.axisLabel
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: 'auto'
            }
        },
        splitLine: {           // �ָ���
            length : 18,         // ����length�����߳�
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: 'auto'
            }
        },
        pointer : {
            length : '90%',
            color : 'auto'
        },
        title : {
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: '#333'
            }
        },
        detail : {
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: 'auto'
            }
        }
    },
    
    textStyle: {
        fontFamily: '΢���ź�, Arial, Verdana, sans-serif'
    }
};

    return theme;
});