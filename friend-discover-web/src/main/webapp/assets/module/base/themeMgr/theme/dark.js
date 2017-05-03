define(function() {

var theme = {
    // ȫͼĬ�ϱ���
    backgroundColor: '#1b1b1b',

    // Ĭ��ɫ��
    color: [
        '#FE8463','#9BCA63','#FAD860','#60C0DD','#0084C6',
        '#D7504B','#C6E579','#26C0C0','#F0805A','#F4E001',
        '#B5C334'
    ],

    // ͼ�����
    title: {
        textStyle: {
            fontWeight: 'normal',
            color: '#fff'          // ������������ɫ
        }
    },

    // ͼ��
    legend: {
        textStyle: {
            color: '#ccc'          // ͼ��������ɫ
        }
    },

    // ֵ��
    dataRange: {
        itemWidth: 15,
        color: ['#FFF808','#21BCF9'],
        textStyle: {
            color: '#ccc'          // ֵ��������ɫ
        }
    },

    toolbox: {
        color : ['#fff', '#fff', '#fff', '#fff'],
        effectiveColor : '#FE8463',
        disableColor: '#666'
    },

    // ��ʾ��
    tooltip: {
        backgroundColor: 'rgba(250,250,250,0.8)',     // ��ʾ������ɫ��Ĭ��Ϊ͸����Ϊ0.7�ĺ�ɫ
        axisPointer : {            // ������ָʾ���������ᴥ����Ч
            type : 'line',         // Ĭ��Ϊֱ�ߣ���ѡΪ��'line' | 'shadow'
            lineStyle : {          // ֱ��ָʾ����ʽ����
                color: '#aaa'
            },
            crossStyle: {
                color: '#aaa'
            },
            shadowStyle : {                     // ��Ӱָʾ����ʽ����
                color: 'rgba(200,200,200,0.2)'
            }
        },
        textStyle: {
            color: '#333'
        }
    },

    // �������ſ�����
    dataZoom: {
        dataBackgroundColor: '#555',            // ���ݱ�����ɫ
        fillerColor: 'rgba(200,200,200,0.2)',   // �����ɫ
        handleColor: '#eee'     // �ֱ���ɫ
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
        },
        axisLabel: {           // �������ı���ǩ�����axis.axisLabel
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: '#ccc'
            }
        },
        splitLine: {           // �ָ���
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
        axisLabel: {           // �������ı���ǩ�����axis.axisLabel
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: '#ccc'
            }
        },
        splitLine: {           // �ָ���
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: ['#aaa'],
                type: 'dashed'
            }
        },
        splitArea: {           // �ָ�����
            show: false
        }
    },

    polar : {
        name : {
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: '#ccc'
            }
        },
        axisLine: {            // ��������
            lineStyle: {       // ����lineStyle����������ʽ
                color: '#ddd'
            }
        },
        splitArea : {
            show : true,
            areaStyle : {
                color: ['rgba(250,250,250,0.2)','rgba(200,200,200,0.2)']
            }
        },
        splitLine : {
            lineStyle : {
                color : '#ddd'
            }
        }
    },

    timeline : {
        label: {
            textStyle:{
                color: '#ccc'
            }
        },
        lineStyle : {
            color : '#aaa'
        },
        controlStyle : {
            normal : { color : '#fff'},
            emphasis : { color : '#FE8463'}
        },
        symbolSize : 3
    },

    // ����ͼĬ�ϲ���
    line: {
        smooth : true
    },

    // K��ͼĬ�ϲ���
    k: {
        itemStyle: {
            normal: {
                color: '#FE8463',       // ���������ɫ
                color0: '#9BCA63',      // ���������ɫ
                lineStyle: {
                    width: 1,
                    color: '#FE8463',   // ���߱߿���ɫ
                    color0: '#9BCA63'   // ���߱߿���ɫ
                }
            }
        }
    },

    // �״�ͼĬ�ϲ���
    radar : {
        symbol: 'emptyCircle',    // ͼ������
        symbolSize:3
        //symbol: null,         // �յ�ͼ������
        //symbolRotate : null,  // ͼ����ת����
    },

    pie: {
        itemStyle: {
            normal: {
                borderWidth: 1,
                borderColor : 'rgba(255, 255, 255, 0.5)'
            },
            emphasis: {
                borderWidth: 1,
                borderColor : 'rgba(255, 255, 255, 1)'
            }
        }
    },

    map: {
        itemStyle: {
            normal: {
                borderColor:'rgba(255, 255, 255, 0.5)',
                areaStyle: {
                    color: '#ddd'
                },
                label: {
                    textStyle: {
                        // color: '#ccc'
                    }
                }
            },
            emphasis: {                 // Ҳ��ѡ����ʽ
                areaStyle: {
                    color: '#FE8463'
                },
                label: {
                    textStyle: {
                        // color: 'ccc'
                    }
                }
            }
        }
    },

    force : {
        itemStyle: {
            normal: {
                linkStyle : {
                    color : '#fff'
                }
            }
        }
    },

    chord : {
        itemStyle : {
            normal : {
                borderWidth: 1,
                borderColor: 'rgba(228, 228, 228, 0.2)',
                chordStyle : {
                    lineStyle : {
                        color : 'rgba(228, 228, 228, 0.2)'
                    }
                }
            },
            emphasis : {
                borderWidth: 1,
                borderColor: 'rgba(228, 228, 228, 0.9)',
                chordStyle : {
                    lineStyle : {
                        color : 'rgba(228, 228, 228, 0.9)'
                    }
                }
            }
        }
    },

    gauge : {
        axisLine: {            // ��������
            show: true,        // Ĭ����ʾ������show������ʾ���
            lineStyle: {       // ����lineStyle����������ʽ
                color: [[0.2, '#9BCA63'],[0.8, '#60C0DD'],[1, '#D7504B']],
                width: 3,
                shadowColor : '#fff', //Ĭ��͸��
                shadowBlur: 10
            }
        },
        axisTick: {            // ������С���
            length :15,        // ����length�����߳�
            lineStyle: {       // ����lineStyle����������ʽ
                color: 'auto',
                shadowColor : '#fff', //Ĭ��͸��
                shadowBlur: 10
            }
        },
        axisLabel: {            // ������С���
            textStyle: {       // ����lineStyle����������ʽ
                fontWeight: 'bolder',
                color: '#fff',
                shadowColor : '#fff', //Ĭ��͸��
                shadowBlur: 10
            }
        },
        splitLine: {           // �ָ���
            length :25,         // ����length�����߳�
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                width:3,
                color: '#fff',
                shadowColor : '#fff', //Ĭ��͸��
                shadowBlur: 10
            }
        },
        pointer: {           // �ָ���
            shadowColor : '#fff', //Ĭ��͸��
            shadowBlur: 5
        },
        title : {
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                fontWeight: 'bolder',
                fontSize: 20,
                fontStyle: 'italic',
                color: '#fff',
                shadowColor : '#fff', //Ĭ��͸��
                shadowBlur: 10
            }
        },
        detail : {
            shadowColor : '#fff', //Ĭ��͸��
            shadowBlur: 5,
            offsetCenter: [0, '50%'],       // x, y����λpx
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                fontWeight: 'bolder',
                color: '#fff'
            }
        }
    },

    funnel : {
        itemStyle: {
            normal: {
                borderColor : 'rgba(255, 255, 255, 0.5)',
                borderWidth: 1
            },
            emphasis: {
                borderColor : 'rgba(255, 255, 255, 1)',
                borderWidth: 1
            }
        }
    },

    textStyle: {
        fontFamily: '΢���ź�, Arial, Verdana, sans-serif'
    }
};

    return theme;
});