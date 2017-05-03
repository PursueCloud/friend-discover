define(function() {
var theme = {
    backgroundColor: '#F2F2E6',
    // Ĭ��ɫ��
    color: [
        '#44B7D3','#E42B6D','#F4E24E','#FE9616','#8AED35',
        '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0',
        '#E95569','#ff6347','#7b68ee','#00fa9a','#ffd700',
        '#6699FF','#ff6666','#3cb371','#b8860b','#30e0e0'
    ],

    // ͼ�����
    title: {
        backgroundColor: '#F2F2E6',
        itemGap: 10,               // ������������������λpx��Ĭ��Ϊ10��
        textStyle: {
            color: '#8A826D'          // ������������ɫ
        },
        subtextStyle: {
            color: '#E877A3'          // ������������ɫ
        }
    },

    // ֵ��
    dataRange: {
        x:'right',
        y:'center',
        itemWidth: 5,
        itemHeight:25,
        color:['#E42B6D','#F9AD96'],
        text:['��','��'],         // �ı���Ĭ��Ϊ��ֵ�ı�
        textStyle: {
            color: '#8A826D'          // ֵ��������ɫ
        }
    },

    toolbox: {
        color : ['#E95569','#E95569','#E95569','#E95569'],
        effectiveColor : '#ff4500',
        itemGap: 8
    },

    // ��ʾ��
    tooltip: {
        backgroundColor: 'rgba(138,130,109,0.7)',     // ��ʾ������ɫ��Ĭ��Ϊ͸����Ϊ0.7�ĺ�ɫ
        axisPointer : {            // ������ָʾ���������ᴥ����Ч
            type : 'line',         // Ĭ��Ϊֱ�ߣ���ѡΪ��'line' | 'shadow'
            lineStyle : {          // ֱ��ָʾ����ʽ����
                color: '#6B6455',
                type: 'dashed'
            },
            crossStyle: {          //ʮ��׼��ָʾ��
                color: '#A6A299'
            },
            shadowStyle : {                     // ��Ӱָʾ����ʽ����
                color: 'rgba(200,200,200,0.3)'
            }
        }
    },

    // �������ſ�����
    dataZoom: {
        dataBackgroundColor: 'rgba(130,197,209,0.6)',            // ���ݱ�����ɫ
        fillerColor: 'rgba(233,84,105,0.1)',   // �����ɫ
        handleColor: 'rgba(107,99,84,0.8)'     // �ֱ���ɫ
    },

    // ����
    grid: {
        borderWidth:0
    },

    // ��Ŀ��
    categoryAxis: {
        axisLine: {            // ��������
            lineStyle: {       // ����lineStyle����������ʽ
                color: '#6B6455'
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
        splitArea : {
            show: false
        },
        splitLine: {           // �ָ���
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: ['#FFF'],
                type: 'dashed'
            }
        }
    },

    polar : {
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
        lineStyle : {
            color : '#6B6455'
        },
        controlStyle : {
            normal : { color : '#6B6455'},
            emphasis : { color : '#6B6455'}
        },
        symbol : 'emptyCircle',
        symbolSize : 3
    },

    // ����ͼĬ�ϲ���
    bar: {
        itemStyle: {
            normal: {
                barBorderRadius: 0
            },
            emphasis: {
                barBorderRadius: 0
            }
        }
    },

    // ����ͼĬ�ϲ���
    line: {
        smooth : true,
        symbol: 'emptyCircle',  // �յ�ͼ������
        symbolSize: 3           // �յ�ͼ�δ�С
    },


    // K��ͼĬ�ϲ���
    k: {
        itemStyle: {
            normal: {
                color: '#E42B6D',       // ���������ɫ
                color0: '#44B7D3',      // ���������ɫ
                lineStyle: {
                    width: 1,
                    color: '#E42B6D',   // ���߱߿���ɫ
                    color0: '#44B7D3'   // ���߱߿���ɫ
                }
            }
        }
    },

    // ɢ��ͼĬ�ϲ���
    scatter: {
        itemStyle: {
            normal: {
                borderWidth:1,
                borderColor:'rgba(200,200,200,0.5)'
            },
            emphasis: {
                borderWidth:0
            }
        },
        symbol: 'circle',    // ͼ������
        symbolSize: 4        // ͼ�δ�С������뾶����������ͼ��Ϊ������������ܿ��ΪsymbolSize * 2
    },

    // �״�ͼĬ�ϲ���
    radar : {
        symbol: 'emptyCircle',    // ͼ������
        symbolSize:3
        //symbol: null,         // �յ�ͼ������
        //symbolRotate : null,  // ͼ����ת����
    },

    map: {
        itemStyle: {
            normal: {
                areaStyle: {
                    color: '#ddd'
                },
                label: {
                    textStyle: {
                        color: '#E42B6D'
                    }
                }
            },
            emphasis: {                 // Ҳ��ѡ����ʽ
                areaStyle: {
                    color: '#fe994e'
                },
                label: {
                    textStyle: {
                        color: 'rgb(100,0,0)'
                    }
                }
            }
        }
    },

    force : {
        itemStyle: {
            normal: {
                nodeStyle : {
                    borderColor : 'rgba(0,0,0,0)'
                },
                linkStyle : {
                    color : '#6B6455'
                }
            }
        }
    },

    chord : {
        itemStyle : {
            normal : {
                chordStyle : {
                    lineStyle : {
                        width : 0,
                        color : 'rgba(128, 128, 128, 0.5)'
                    }
                }
            },
            emphasis : {
                chordStyle : {
                    lineStyle : {
                        width : 1,
                        color : 'rgba(128, 128, 128, 0.5)'
                    }
                }
            }
        }
    },

    gauge : {                  // �Ǳ���
        center:['50%','80%'],
        radius:'100%',
        startAngle: 180,
        endAngle : 0,
        axisLine: {            // ��������
            show: true,        // Ĭ����ʾ������show������ʾ���
            lineStyle: {       // ����lineStyle����������ʽ
                color: [[0.2, '#44B7D3'],[0.8, '#6B6455'],[1, '#E42B6D']],
                width: '40%'
            }
        },
        axisTick: {            // ������С���
            splitNumber: 2,   // ÿ��splitϸ�ֶ��ٶ�
            length: 5,        // ����length�����߳�
            lineStyle: {       // ����lineStyle����������ʽ
                color: '#fff'
            }
        },
        axisLabel: {           // �������ı���ǩ�����axis.axisLabel
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: '#fff',
                fontWeight:'bolder'
            }
        },
        splitLine: {           // �ָ���
            length: '5%',         // ����length�����߳�
            lineStyle: {       // ����lineStyle�����lineStyle������������ʽ
                color: '#fff'
            }
        },
        pointer : {
            width : '40%',
            length: '80%',
            color: '#fff'
        },
        title : {
          offsetCenter: [0, -20],       // x, y����λpx
          textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
            color: 'auto',
            fontSize: 20
          }
        },
        detail : {
            offsetCenter: [0, 0],       // x, y����λpx
            textStyle: {       // ��������Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                color: 'auto',
                fontSize: 40
            }
        }
    },

    textStyle: {
        fontFamily: '΢���ź�, Arial, Verdana, sans-serif'
    }
};

    return theme;
});