<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="e_dialog_win"></div>
    <div id="e_dialog_win2"></div>
    <div id="hes_cont" style="padding:30px 2% 0;height:95%;">
        <div>
            <div style="font-weight:bold;font-size:20px;margin-bottom:20px;">选择一个示例来运行：</div>
            <div style="padding:0 30px;text-align:center;">
                <select id="hadoop_examples_select" name="exampleId" class="easyui-combobox" style="width:150px;" data-options="value:'0', url: 'assets/module/hadoop/examples.json',
                    valueField: 'exampleId',textField: 'exampleName',prompt:'请选择一个示例来运行', panelHeight:'auto', required:true, editable:false, ">
                    <%--<option value="0">wordcount</option>--%>
                    <%--<option value="1">pi</option>--%>
                    <%--<option value="2">maxtemperature</option>--%>
                    <%--<option value="3">wordmean</option>--%>
                    <%--<option value="4">wordmedian</option>--%>
                    <%--<option value="5">wordstandarddeviation</option>--%>
                    <%--<option value="6">aggregatewordcount</option>--%>
                    <%--<option value="7">aggregatewordhist</option>--%>
                    <%--<option value="8">grep</option>--%>
                    <%--<option value="9">randomwriter</option>--%>
                    <%--<option value="10">randomtextwriter</option>--%>
                    <%--<option value="11">sort</option>--%>
                    <%--<option value="12">bbp</option>--%>
                    <%--<option value="13">distbbp</option>--%>
                    <%--<option value="14">pentomino</option>--%>
                    <%--<option value="15">secondarysort</option>--%>
                    <%--<option value="16">sudoku</option>--%>
                    <%--<option value="17">join</option>--%>
                    <%--<option value="18">multifilewc</option>--%>
                    <%--<option value="19">dbcount</option>--%>
                    <%--<option value="20">teragen</option>--%>
                    <%--<option value="21">terasort</option>--%>
                    <%--<option value="22">teravalidate</option>--%>
                </select>
                <span style="width:50px;display:inline-block;"></span>
                <input id="he_input_box" name="param1" class="easyui-textbox" style="width:250px;" data-options="prompt:'参数1', buttonText:'浏览', buttonIcon:'icon-search', required:true"/>
                <input id="he_output_box" name="param2" class="easyui-textbox" style="width:250px;" data-options="prompt:'参数2', buttonText:'浏览', buttonIcon:'icon-search', required:true"/>
                <a id="run_mr_example_a" class="easyui-linkbutton" style="width:150px;margin-left:54px;">提交并运行</a>
                <div id="example_desc_wrapper" style="text-align: left;font-size:12px;padding: 20px 0 0 60px;font-weight: bold;">描述：<span id="example_desc_cont" style="font-size:12px;"></span></div>
                <div id="usage_wrapper" style="text-align: left;font-size:12px;padding: 20px 0 0 60px;font-weight: bold;">用法：<span id="usage_cont" style="font-size:12px;"></span></div>
            </div>
        </div>
        <br/><br/>
        <hr>
        <div style="height: 78%;">
            <div id="prog_div" style="margin:auto;text-align:center;width:400px;display:none;">当前进度：<div id="run_mr_example_prog" class="easyui-progressbar" ></div><br/><img /></div>
            <div style="font-weight:bold;font-size:20px;margin-top:20px;margin-bottom:10px;">运行过程及结果
                <span class="left-kuohao">(</span>
                <span>状态：</span><span class="running" style="color:dodgerblue;display: none;">正在运行...</span>
                <span class="success" style="color:green;display:none;">成功</span>
                <span class="failed" style="color:red;display:none;">失败</span>
                <span class="right-kuohao">)</span>：
            </div>
            <div class="run-mr-example-res" style="height: 73%;">
            </div>
        </div>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/hadoop/hadoop.js' ], function(ha) {
                ha.examples();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>