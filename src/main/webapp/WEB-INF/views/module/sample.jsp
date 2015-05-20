<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="../include/easyui.jsp"%>
</head>

<body>
	<div style="width: 100%; background-color:#2075c4; height:124px;color: #fff;">
	    <br/>
		<p style="text-align:center;margin-top: 0px;margin-bottom: 0px;font-size: x-large;">FIT2CLOUD SAAS化演示应用</p>
		&nbsp;&nbsp;&nbsp;&nbsp;该演示内容如下:
		<ul>
			<li>列出每个租户的虚拟机及状态</li>
			<li>创建租户: 调用Fit2Cloud API启动虚机,部署应用(Jenkins)</li>
			<li>删除租户: 调用Fit2Cloud API释放虚机</li>
		</ul>
	</div>
	
	<table id="servers"></table>
	
	<div id="tb" style="padding:5px;height:26px">
		<a href="javascript:launchServer()" class="easyui-linkbutton" iconCls="icon-add">创建新租户</a>
		<div style="display: inline-block;float:right; margin-right: 5px;">
			<input id="chkAutoRefresh" name="chkAutoRefresh" type="checkbox" onclick='handleAutoRefresh(this);' checked>自动刷新(每15s)</input>
			<a href="javascript:refresh()" class="easyui-linkbutton" iconCls="icon-reload" plain="true" style="float:right">刷新</a>
		</div>
	</div>
	
	<%@include file="../include/footer.jsp"%>
</body>
<script type="text/javascript">

	var autoRefresh = chkAutoRefresh.checked;
	if (autoRefresh) intervalID = window.setInterval(refresh, 1000 * 15);
	$(document).ready(function() {
		$('#servers').datagrid({
			url : "/server/list.json",
			title : "租户列表(每个租户一台虚机)",
			singleSelect : true,
			fitColumns : true,
			method : 'get',
			fit : true,
			rownumbers : true,	
			toolbar : '#tb',	
			columns : [ [ {
				field : 'id',
				title : '序号',
				width:20
			}, {
				field : 'vmId',
				title : '虚机Id',
				width:35
			}, {
				field : 'name',
				title : '虚机名称',
				width:100
			}, {
				field : 'remoteIP',
				title : '公网IP',
				width:45,
                sortable: true
			}, {
				field : 'region',
				title : '区域',
				width:20,
				formatter: function(value,row,rowIndex){
					return regionMap.get(value);
				}
			}, {
				field : 'vmStatus',
				title : '虚机状态',
				width:40,
				formatter: function(value,row,rowIndex){
					if(!value){
						return "";
					}
					var text = vmStatusMap.get(value);
					if (value == "Deleted"){
		                return '<span style="color:red;">'+text+'</span>';
		            }
		            if (value == "Stopping") {
		            	return text+'<img src="../../static/easyui/images/ajax-loading.gif" style="margin-left:5px;"/>';
		            }
		            if(row.terminateFlag == 1) {
		            	return '等待关闭<img src="../../static/easyui/images/ajax-loading.gif" style="margin-left:5px;"/>';
		            }
		            if (value == "Failed"){
		            	var worksUrl =  '/works?serverId=' + row.id;
		            	return '<span style="color:red;"><img class="easyui-tooltip" title="'+row.failedCause+'" src="../../static/easyui/themes/icons/help.png"/><a class="alertLink" style="margin-left:3px;" href="'+worksUrl+'">'+text+'</a></span>';
		            }
		            if (value == "Running"){
		                return '<span style="color:green;">'+text+'</span>';
		            }
		            if (value == "Pending" || value == "Starting"){
		            	return text+'<img src="../../static/easyui/images/ajax-loading.gif" style="margin-left:5px;"/>';
		            }
		            return text;
				}
			},{
				field : 'heartbeatStatus',
				title : '心跳',
				width:55,
				formatter: function(value,row,rowIndex){
					if(!value){
						return "";
					}
					if (row.vmStatus != "Running"){
						return "";
					}
					var text = serverHeartbeatStatusMap.get(value);
					// 安装agent耗时在5分钟之内
					var newInstall = (new Date().getTime() - row.created) <= 5*60*1000;
					if(newInstall){
						if (value == "OFFLINE") {
							return '连接中<img src="../../static/easyui/images/ajax-loading.gif" style="margin-left:5px;"/>';
						}
					}
					if (value == "ONLINE"){
						return '<span style="color:green;">'+text+'</span>';
					}
					if (value == "OFFLINE") {
						return '<span style="color:red;">'+text+'</span>';
					}
					return text;
				}
			}, {
				field : 'price',
				title : '每小时费用(元)',
				width:30
			}, {
				field : 'created',
				title : '创建时间',
				width:50,
				formatter: function(value, row, index) {
					if(value) {
						return new Date(value).Format("yyyy-MM-dd hh:mm");
					}
				}
			}, {
				field : 'runningTime',
				title : '运行时间',
				width:50
			}, {
				field : 'action',
				title : '操作',
				align : 'center',
				formatter:function(value,row,rowIndex){
					return "<a class='button-link' onclick='terminateServer(" + row.id + ")'>删除租户</a>  ";
				},
				width:40
			}] ]
		});
	});
	
	function terminateServer(serverId) {
		$.messager.confirm('确认框', '您确定要移除这个租户吗?',
			function(r) {
				if (r) {
					$.ajax({
						  type: 'POST',
						  url: '/server/'+serverId+'/delete.json',
						  dataType: 'json',
						  beforeSend: function(){
							  $.messager.progress({text: '处理中'});
						  },
						  complete: function(){
						   	  $.messager.progress('close');
						   	  refresh();
						  }
					});
				}
			});
	}
	
	function launchServer() {
		$.messager.confirm('确认框', '您确定要创建新租户吗?',
			function(r) {
				if (r) {
					$.ajax({
						  type: 'POST',
						  url: '/server/add.json',
						  dataType: 'json',
						  beforeSend: function(){
							  $.messager.progress({text: '处理中'});
						  },
						  complete: function(){
						   	  $.messager.progress('close');
						   	  refresh();
						  }
					});
				}
			});
	}
	
	function refresh() {
		$('#servers').datagrid('reload');	
	}
	
	function handleAutoRefresh(element){
		if (element.checked){
			intervalID = window.setInterval(refresh, 1000 * 15);
		}else{
			if (intervalID)window.clearInterval(intervalID);
		}
	}
</script>
</html>