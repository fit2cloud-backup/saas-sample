<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="author" content="The FIT2CLOUD Team (support@fit2cloud.com)" />
<title>FIT2CLOUD SAAS化应用演示</title>
<style type="text/css">
	body {
	  visibility: hidden;
	}
</style>
<link rel="stylesheet" type="text/css" href="/static/easyui/css/style.css" />
<link rel="stylesheet" type="text/css" href="/static/easyui/themes/default/easyui.css">
<script type="text/javascript" src="/static/easyui/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="/static/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/static/easyui/utils.js"></script>
<script>
	jQuery(document).ready(function(){
		setTimeout('jQuery("body").css("visibility","visible");	$("body").css("display", "none").fadeIn(1000,"swing"); ', 500);
	});
</script>
