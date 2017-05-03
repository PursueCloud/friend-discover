<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="sccc_cont" style="padding:2% 5%;text-align: center;width:90%;height:90%;">
        <form id="sccfc_form" action="dataShow/showClusterCenterFileCont" method="post" enctype="multipart/form-data">
            <input type="hidden" id="show_type" name="showType" value="0" />
            <table style="margin: auto;">
                <tr>
                    <td>数据位置：</td>
                    <td>
                        <input type="radio" id="data_loc_local" name="dataLoc" value="0" checked/><label for="data_loc_local">本地</label>
                        <input type="radio" id="data_loc_hdfs" name="dataLoc" value="1"/><label for="data_loc_hdfs">HDFS</label>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>聚类中心数据文件：</td>
                    <td class="cluster-data-file-path-td">
                        <!-- 默认数据文件：WEB-INF/classes/centervector.dat -->
                        <input id="cluster_data_file" name="clusterDataFile" class="easyui-filebox" style="width:440px;" data-options="prompt:'请选择文件（默认：WEB-INF/classes/centervector.dat)', buttonText:'浏览', buttonIcon:'icon-search', multiple:false, required:false, "/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
            </table>
        </form>
        <a id="show_as_word" class="easyui-linkbutton" style="width:150px;margin-left:20px;">查看内容</a>
        <%--<a id="show_as_chart" class="easyui-linkbutton" style="width:150px;margin-left:20px;">以图表形式查看</a>--%>
        <br/><br/>
        <hr style="border: 1px dashed black;">
        <div id="cluster_data_cont" style="width:100%;height:85%;">
            <div id="prog_div" style="margin:0px auto 0;text-align:center;width:400px;display:none;"><span class="loading-status">正在加载文件</span>...
                <div id="load_file_prog" class="easyui-progressbar" ></div><br/>
                <img src="assets/images/loading.spanner.gif"/>
            </div>
            <div id="data_type" style="font-weight: bold;font-size: 20px;">内容</div>
            <div id="data_cont" style="width:100%;height:95%;padding-top:2%;">
                <div id="word" style=""></div>
                <div id="chart" style="width: 95%;height:95%;margin:auto;"></div>
            </div>
        </div>
    </div>
    <script>
        $(function(){
            seajs.use([ 'module/dataShow/dataShow.js' ], function(ds) {
                ds.showClusterCenterCensus();
            });
        })
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>