<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>登录</title>
        <meta charset="utf-8">
        <meta http-equiv="pragma" content="no-cache" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="content-type" content="text/html;charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
        <base href='<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/"%>'>
        <link rel="shortcut icon" href="<%=request.getContextPath()%>/assets/images/favicon.ico">
        <script src="assets/library/jquery/jquery-3.2.0.min.js"></script>
        <link rel="stylesheet" type="text/css" href="assets/css/admin/base.css" />
        <link rel="stylesheet" type="text/css" href="assets/css/admin/login.css" />
        <style type="text/css">
            *{font-family: "Microsoft YaHei",Arial,Helvetica,sans-serif; }
            .serial_title{
                display: inline-flex;
                padding: 60px 0;
                vertical-align:middle;
            }
            .serial_title img{
                vertical-align: middle;
                display: inline-block;
                margin-right: 20px;
                width: 32px;
                height: 32px;
            }
            .serial_title h2{
                vertical-align: middle;
                display: inline-block;
                color: #2e4058;
                font-size: 24px;
                font-weight: bold;
                line-height: 32px;
            }
            .serial_input label{
                font-size:18px;
            }
            .serial_input input{
                width: 368px;
                height: 26px;
                padding: 5px;
                font-size: 16px;
            }
            .serial_btn{
                margin: 32px 0;
            }
            .serial_btn button{
                background: #3ad9f9;
                border: none;
                color: #fff;
                font-size: 18px;
                width: 86px;
                height: 36px;
                
            }
            .serial_help{
                position: relative;
			    bottom: -40px;
			    right: -178px;
            }
            .serial_help a{
                font-size: 12px;
                color: rgb(57,124,220);
                text-decoration:underline;
            }
            .login-btn {
                display: inline-block;
                width: 100%;
                height: 40px;
                line-height: 40px;
                text-align: center;
                background-color: #39d9f8;
                color: #fff;
                font-size: 18px;
                font-weight: bold;
            }
            .login-btn:hover {
                background-color: dodgerblue;
            }
        </style>
    </head>
<body class="login-page-bg">
    <script type="text/javascript">
        if (top.location != location){
            $("#index-main").hide();
            top.location.href = "./";  
        }
    </script>
    <shiro:authenticated>
        <%
            response.sendRedirect(request.getContextPath());
        %>
    </shiro:authenticated>
<div class="header">
    <img src="assets/images/logo.png" width="48px" height="48px">
    <div class="title">
        <h3>朋友发现管理平台</h3><!-- 朋友发现系统 -->
        <h4>Friendis</h4>
    </div> 
</div>
<div class="box-main">
    <form class="box-login" action="admin/login" method="post">
        <c:if test="${shiroLoginFailure != null }">
            <div style="position:relative;top:-28px; color: #EC1A37;font-size: 20px;">${"org.apache.shiro.authc.UnknownAccountException" == shiroLoginFailure ? "用户不存在!" :"用户密码错误!"}</div>
        </c:if>
        <div style="background-color: #3c4c5f;">
            <div class="text-box  username">
                <i class="icon"></i>
                <input type="text" name="username" placeholder="登录账户名" autofocus="true" >
                <div class="underline"></div>
            </div>
            <div class="text-box  password">
                <i class="icon"></i>
                <input type="password" name="password" placeholder="账户密码">
                <div class="underline"></div>
            </div>
            <div class="text-box remember-me">
                <input type="hidden" name="rememberMe" >
                <a href="javascript:void(0);">
                    <span class="icon"></span>
                    <span style="display: inline-block;height: 32px;line-height: 32px;font-size: 12px;color: black;">记住我</span>
                </a>
            </div>
        </div>
        <div>
            <a id="login" class="login-btn" href="javascript:void(0);" >登录</a>
        </div>
    </form>
</div>

<div class="footer">
    <p style="color: #9ca4af;">
        Jay.huang ©2016
    </p>
</div>
<script type="text/javascript" src="assets/library/jquery/jquery.sha256.min.js"></script>
<script type="text/javascript">
    $(function(){
        var $btnRem=$(".remember-me");
        $btnRem.on("click","a",function(){
            var $rememberMe=$btnRem.find("input[type=hidden]");
            var rememberMe=$rememberMe.val();
            if(typeof rememberMe == "undefined" || rememberMe == "" ||  rememberMe == "false"){
                $rememberMe.val("true");
                $btnRem.find(".icon").addClass("checked");
            }else if(rememberMe == "true"){
                $rememberMe.val("false");
                $btnRem.find(".icon").removeClass("checked");
            }

        });

        $("#login").click(function(){
            var $pwd=$("input[name=password]");
            $pwd.val($.sha256($pwd.val()));     
            $("form").submit();
            return false;
        });
        $("input[name=password]").keydown(function(e){
            if(e.keyCode==13){
                $("#login").click();
            }
        });
        
    });

</script>
    
</body>
</html>