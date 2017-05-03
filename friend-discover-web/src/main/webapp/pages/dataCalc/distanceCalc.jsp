<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="dist_cont" style="padding:20px 35px;">
        <form action="dataCalc/calcDistance" method="post" id="dist_form">
            <table>
                <tr><td><br/></td></tr>
                <tr>
                    <td>输入路径(HDFS)：</td>
                    <td>
                        <input id="discCalc_input_box" name="inputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/preparevectors', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', required:true"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>输出路径(HDFS)：</td>
                    <td>
                        <input id="discCalc_output_box" name="outputPath" class="easyui-textbox" style="width:200px;" data-options="value:'/user/root/_filter/caldistance', prompt:'请选择或输入路径', buttonText:'浏览', buttonIcon:'icon-search', required:true"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataCalc/dataCalc.js' ], function(dc) {
                dc.distanceCalc();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>