define(function() {

var theme = {
    // Ĭ��ɫ��
    color: [
        '#c12e34','#e6b600','#0098d9','#2b821d',
        '#005eaa','#339ca8','#cda819','#32a487'
    ],

    // ͼ�����
    title: {
        textStyle: {
            fontWeight: 'normal'
        }
    },
    
    // ֵ��
    dataRange: {
        itemWidth: 15,             // ֵ��ͼ�ο�ȣ����Խ���ˮƽ���ֿ��Ϊ��ֵ * 10
        color:['#1790cf','#a2d4e6']
    },

    // ������
    toolbox: {
        color : ['#06467c','#00613c','#872d2f','#c47630']
    },

    // ��ʾ��
    tooltip: {
        backgroundColor: 'rgba(0,0,0,0.6)'
    },

    // �������ſ�����
    dataZoom: {
        dataBackgroundColor: '#dedede',            // ���ݱ�����ɫ
        fillerColor: 'rgba(154,217,247,0.2)',   // �����ɫ
        handleColor: '#005eaa'     // �ֱ���ɫ
    },
    
    // ����
    grid: {
        borderWidth: 0
    },
    
    // ��Ŀ��
    categoryAxis: {
        axisLine: {            // ��������
            show: false
        },
        axisTick: {            // ������С���
            show: false
        }
    },

    // ��ֵ��������Ĭ�ϲ���
    valueAxis: {
        axisLine: {            // ��������
            show: false
        },
        axisTick: {            // ������С���
            show: false
        },
        splitArea: {           // �ָ�����
            show: true,       // Ĭ�ϲ���ʾ������show������ʾ���
            areaStyle: {       // ����areaStyle�����areaStyle������������ʽ
                color: ['rgba(250,250,250,0.2)','rgba(200,200,200,0.2)']
            }
        }
    },
    
    timeline : {
        lineStyle : {
            color : '#005eaa'
        },
        controlStyle : {
            normal : { color : '#005eaa'},
            emphasis : { color : '#005eaa'}
        }
    },

    // K��ͼĬ�ϲ���
    k: {
        itemStyle: {
            normal: {
                color: '#c12e34',          // ���������ɫ
                color0: '#2b821d',      // ���������ɫ
                lineStyle: {
                    width: 1,
                    color: '#c12e34',   // ���߱߿���ɫ
                    color0: '#2b821d'   // ���߱߿���ɫ
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
                    color: '#e6b600'
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
                    color : '#005eaa'
                }
            }
        }
    },
    
    chord : {
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
                color: [[0.2, '#2b821d'],[0.8, '#005eaa'],[1, '#c12e34']], 
                width: 5
            }
        },
        axisTick: {            // ������С���
            splitNumber: 10,   // ÿ��splitϸ�ֶ��ٶ�
            length :8,        // ����length�����߳�
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
            length : 12,         // ����length�����߳�
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: 'auto'
            }
        },
        pointer : {
            length : '90%',
            width : 3,
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