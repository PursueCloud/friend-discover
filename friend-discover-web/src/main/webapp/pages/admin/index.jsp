<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title>朋友发现管理平台</title><!--朋友发现系统  -->
        <%@include file="admin-refer.jspf" %>
    </head>
<body>
    <div class="header">
    <div class="logo">
        <h3 style="font-size: 18px; color: #fff;font-weight: bold;"><span style="color: #52abcd;">朋友发现管理平台</span></h3>
    </div>
    <div class="tools">
        <div class="login-info">
            <a>
                <i class="icon-person" style="position:relative;top: 2px;"></i>
                <span ><shiro:principal property="userName"/></span>
            </a>
        </div>
        <a class="btn-logout" href="admin/logout" title="退出登录">退出登录</a>
    </div>
</div>
<div class="main-box">
    <div class="lr">
        <div class="wrapper">
            <div id="nav_slider">
                <div class="shadow-box"></div>
            </div>
            <ul id="nav_menu" >
            </ul>
        </div>
    </div>
    <div class="content">
        <div class="easyui-tabs js-tabs" id="centerTab" fit="true" border="false" tabHeight='29'>
            <!--    welcom home -->
        </div>
    </div>
</div>
    <!-- tab右键菜单 -->
    <div id="tab_menu" class="easyui-menu" style="display:none;" >
        <div id="refresh_this">重新加载</div>
        <div class="menu-sep"></div>
        <div id="close_this">关闭标签页</div>
        <div id="close_all">关闭全部标签页</div>
        <div id="close_others">关闭其他标签页</div>
        <div class="menu-sep"></div>
        <div id="close_right">关闭右侧标签页</div>
        <div id="close_left">关闭左侧标签页</div>
    </div>
<script type="text/javascript">
    $(function(){
        seajs.use([ 'module/admin/index.js' ], function(module) {
            module.init();
        }); 
    });

</script>
</body>
</html>