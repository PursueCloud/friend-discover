<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="/pages/include/hd.jsp"%>

<body>
    <div id="dialog_win"></div>
    <div id="form_cont">
        <form action="/dataKeep/add" method="post" id="edit_custUser_form" class="formTable" style="padding:5% 5% 4%" enctype="multipart/form-data">
            <table cellpadding="0" cellspacing="0" width="100%">
                <tr>
                    <td><input type="hidden" id="table_code" name="tableCode" /></td>
                    <td><input type="hidden" id="user_id" name="userId" /></td>
                </tr>
                <tr>
                    <td>昵称：</td>
                    <td style="text-align:left;">
                        <input id="display_name" name="displayName" class="easyui-textbox" style="width:200px;" data-options="prompt:'请输入昵称', required:true"/>
                    </td>
                    <td>邮箱hash：</td>
                    <td style="text-align:left;">
                        <input id="email_hash" name="emailHash" class="easyui-textbox" style="width:200px;" data-options="prompt:'请输入邮箱', required:true, "/><!-- validType:['email','length[0,20]'] -->
                    </td>
                </tr>
                <tr>
                    <td>头像：</td>
                    <td style="text-align:left;">
                        <input id="icon" name="iconFile" class="easyui-filebox" style="width:200px;" data-options="buttonText:'浏览', prompt:'请选择头像'"/>
                    </td>
                    <td>预览：</td>
                    <td style="text-align:left;width:200px;height:100px;text-align:center;">
                        <img id="icon_preview_img" src="" style="width:80px;height:80px;border-radius:10px;border:1px solid #999;vertical-align: middle"/>
                    </td>
                </tr>
                <tr>
                    <td>个人主页：</td>
                    <td colspan="3" style="text-align:left;">
                        <input id="website_url" name="websiteUrl" class="easyui-textbox" style="width:465px;" data-options="prompt:'请输入个人主页url', validType:'url',"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>个人简介：</td>
                    <td colspan="3" style="text-align:left;">
                        <input id="about_me" name="aboutMe" class="easyui-textbox" style="width:465px;height:100px;" data-options="prompt:'请输入个人简介', multiline:true,"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>常驻地：</td>
                    <td colspan="3" style="text-align:left;">
                        <input id="location" name="location" class="easyui-combotree" style="width:465px;" data-options="prompt:'请选择常驻地'"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>性别：</td>
                    <td style="text-align:left;">
                        <input type="radio" id="man_sex" name="sex" value="0" checked/><label for="man_sex">男</label>&nbsp;&nbsp;
                        <input type="radio" id="woman_sex" name="sex" value="1" /><label for="woman_sex">女</label>
                    </td>
                    <td>年龄：</td>
                    <td style="text-align:left;">
                        <input id="age" name="age" class="easyui-numberspinner" style="width:200px;" data-options="min:1, value:1, prompt:'请输入年龄'"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>赞同数：</td>
                    <td style="text-align:left;">
                        <input id="up_votes" name="upVotes" class="easyui-numberspinner" style="width:200px;" data-options="min:0, value:0, prompt:'请输入赞同数'"/>
                    </td>
                    <td>反对数：</td>
                    <td style="text-align:left;">
                        <input id="down_votes" name="downVotes" class="easyui-numberspinner" style="width:200px;" data-options="min:0, value:0, prompt:'请输入反对数'"/>
                    </td>
                </tr>
                <tr><td><br/></td></tr>
                <tr>
                    <td>声望值：</td>
                    <td style="text-align:left;">
                        <input id="reputation" name="reputation" class="easyui-numberspinner" style="width:200px;" data-options="min:0, value:0, prompt:'请输入声望值'"/>
                    </td>
                    <td>浏览数：</td>
                    <td style="text-align:left;">
                        <input id="views" name="views" class="easyui-numberspinner" style="width:200px;" data-options="min:0, value:0, prompt:'请输入浏览数'"/>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <script type="text/javascript">
        $(function() {
            seajs.use([ 'module/dataKeep/dataKeep.js' ], function(dk) {
                dk.editCustomUser();
            });
        });
    </script>
</body>

<%@ include file="/pages/include/ft.jsp"%>