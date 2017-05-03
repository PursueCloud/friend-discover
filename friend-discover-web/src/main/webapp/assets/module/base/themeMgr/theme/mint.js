define({
    // ȫͼĬ�ϱ���
    // backgroundColor: 'rgba(0,0,0,0)',
    
    // Ĭ��ɫ��
    color: ['#8aedd5','#93bc9e','#cef1db','#7fe579','#a6d7c2',
            '#bef0bb','#99e2vb','#94f8a8','#7de5b8','#4dfb70'],

    
    
    // ֵ��
    dataRange: {
        color:['#93bc92','#bef0bb']
    },

    // K��ͼĬ�ϲ���
    k: {
        // barWidth : null          // Ĭ������Ӧ
        // barMaxWidth : null       // Ĭ������Ӧ 
        itemStyle: {
            normal: {
                color: '#8aedd5',          // ���������ɫ
                color0: '#7fe579',      // ���������ɫ
                lineStyle: {
                    width: 1,
                    color: '#8aedd5',   // ���߱߿���ɫ
                    color0: '#7fe579'   // ���߱߿���ɫ
                }
            },
            emphasis: {
                // color: ����,
                // color0: ����
            }
        }
    },
    
    // ��ͼĬ�ϲ���
    pie: {
        itemStyle: {
            normal: {
                // color: ����,
                borderColor: '#fff',
                borderWidth: 1,
                label: {
                    show: true,
                    position: 'outer',
                  textStyle: {color: '#1b1b1b'},
                  lineStyle: {color: '#1b1b1b'}
                    // textStyle: null      // Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                },
                labelLine: {
                    show: true,
                    length: 20,
                    lineStyle: {
                        // color: ����,
                        width: 1,
                        type: 'solid'
                    }
                }
            }
        }
    },
    
    map: {
        mapType: 'china',   // ��ʡ��mapType��ʱ��������
        mapLocation: {
            x : 'center',
            y : 'center'
            // width    // ����Ӧ
            // height   // ����Ӧ
        },
        showLegendSymbol : true,       // ��ʾͼ����ɫ��ʶ��ϵ�б�ʶ��СԲ�㣩������legendʱ��Ч
        itemStyle: {
            normal: {
                // color: ����,
                borderColor: '#fff',
                borderWidth: 1,
                areaStyle: {
                    color: '#ccc'//rgba(135,206,250,0.8)
                },
                label: {
                    show: false,
                    textStyle: {
                        color: 'rgba(139,69,19,1)'
                    }
                }
            },
            emphasis: {                 // Ҳ��ѡ����ʽ
                // color: ����,
                borderColor: 'rgba(0,0,0,0)',
                borderWidth: 1,
                areaStyle: {
                    color: '#f3f39d'
                },
                label: {
                    show: false,
                    textStyle: {
                        color: 'rgba(139,69,19,1)'
                    }
                }
            }
        }
    },
    
    force : {
        itemStyle: {
            normal: {
                // color: ����,
                label: {
                    show: false
                    // textStyle: null      // Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                },
                nodeStyle : {
                    brushType : 'both',
                    strokeColor : '#49b485'
                },
                linkStyle : {
                    strokeColor : '#49b485'
                }
            },
            emphasis: {
                // color: ����,
                label: {
                    show: false
                    // textStyle: null      // Ĭ��ʹ��ȫ���ı���ʽ�����TEXTSTYLE
                },
                nodeStyle : {},
                linkStyle : {}
            }
        }
    },

    gauge : {
        axisLine: {            // ��������
            show: true,        // Ĭ����ʾ������show������ʾ���
            lineStyle: {       // ����lineStyle����������ʽ
                color: [[0.2, '#93bc9e'],[0.8, '#8aedd5'],[1, '#a6d7c2']], 
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
    }
});
                