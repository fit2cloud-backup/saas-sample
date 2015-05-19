
$(document).ready(function() {
	$(document).ajaxError(function(event, request, settings, thrownError) {
		if (request.readyState == 0 && request.statusText == "error") { 
		    return; //Skip this error  
		}  
		var result;
	    try {
	    	if (request.responseText.indexOf("while(1);") == 0)return;
	        result = JSON.parse(request.responseText);
	    } catch (e) {
	    	if(trimStr(request.responseText)){
	    		alertMsg(false, request.responseText);
	    	}
	        return;
	    }
	    if (result instanceof Array) return;
	    if (typeof(result) == "object" && typeof(result.limit) == "number") return;
	    if (typeof(result) == "object" && typeof(result.success) == "boolean") {
	    	if(typeof(result.msg) == "number"){
	    	}else{
		    	alertMsg(result.success, result.msg);
	    	}
	    }
    });
    
	$(document).ajaxSuccess(function(event, request, settings) {
	    var result;
	    try {
	        result = JSON.parse(request.responseText);
	    } catch (e) {}
	    if (result instanceof Array) return;
	    if (result == null) return;
	    if (typeof(result) == "object" && typeof(result.limit) == "number") return;
	    if (typeof(result) == "object" && typeof(result.success) == "boolean") {
	    	if(typeof(result.msg) == "number"){
	    	}else{
		    	alertMsg(result.success, result.msg);
		        $.messager.progress('close');
	    	}
			if($('.dataGridTable').filter('.currentGrid')){
				$('.dataGridTable').filter('.currentGrid').datagrid('reload');
			} else{
				$('.dataGridTable').datagrid('reload');
			}
	    }
	});  

    $(document).ajaxComplete(function(event, request, settings, thrownError) {
        var sessionStatus = request.getResponseHeader('sessionstatus');
        if (sessionStatus == 'timeout') {
            alertMsg(false, "Session已经失效，将重新加载页面！");
            window.location.reload();
            return;
        }
        $.messager.progress('close');
    });
	
	$(document).ajaxSend(function(event, request, settings) {
		var token = $("meta[name='_csrf_token']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");
        if(token != null && header != null){
            $("meta[name='_csrf_token']").attr("content", token);
            $("meta[name='_csrf_header']").attr("content", header);
            request.setRequestHeader(header, token);
        }
        if(settings.url && settings.url.indexOf("list.json") > 0)return;
        $.messager.progress({text: '处理中'});
	}); 
	
    $(window).resize(function() {
		var datagrids = $('.dataGridTable');
		for(var i in datagrids){
			$('.dataGridTable:eq(i)').datagrid('resize');
		}
    });
    // Requried: Navigation bar drop-down
    $("nav ul li").hover(function() {
        $(this).addClass("active");
        $(this).find("ul").show().animate({
            opacity: 1
        }, 400);
    }, function() {
        $(this).find("ul").hide().animate({
            opacity: 0
        }, 200);
        $(this).removeClass("active");
    });

    // Requried: Addtional styling elements
    $('nav ul li ul li:first-child').prepend('<li class="arrow"></li>');
    $('nav ul li:first-child').addClass('first');
    $('nav ul li:last-child').addClass('last');
    $('nav ul li ul').parent().append('<span class="dropdown"></span>').addClass('drop');
});

function commonFormSave(url, formId, closableDialogId, refreshGridId, callback){
    $('#'+formId).form('submit', {
			url: url,
			onSubmit: function () {
				return formBeforeSubmit(formId);
			},
			success: function (responseText) {
				var result;
				try {
					result = JSON.parse(responseText);
				} catch (e) {
			    	alertMsg(false, responseText);
					return;
				}
				if (typeof(result) == "object" && typeof(result.limit) == "number") return;
				if (typeof(result) == "object" && typeof(result.success) == "boolean") {
			    	alertMsg(result.success, result.msg);
					$.messager.progress('close');
					if($('#'+closableDialogId)){
						$('#'+closableDialogId).dialog('close');
					}
					if($('#'+refreshGridId)){
						$('#'+refreshGridId).datagrid('reload');
					}else{
						$('.dataGridTable').datagrid('reload');
					}
				}
				callback();
			}
		});
}

function formSubmitCallback(responseText) {
    var result;
    try {
        result = JSON.parse(responseText);;
    } catch (e) {
    	$.messager.progress('close');
    	alertMsg(false, responseText);
        return;
    }
    if (typeof(result) == "object" && typeof(result.limit) == "number") return;
    if (typeof(result) == "object" && typeof(result.success) == "boolean") {
    	alertMsg(result.success, result.msg);
        $.messager.progress('close');
        $('#form-window').dialog('close');
        $('.dataGridTable').datagrid('reload');
    }
}

function formBeforeSubmit(formId) {
    var v;
    if(typeof formId == 'string'){
        v=$("#"+formId).form('validate');
    } else {
        v=$("#fm").form('validate');
    }
    var token = $("meta[name='_csrf_token']").attr("content");
    $('input[name=F2C-CSRF-TOKEN]').val(token);
    if(v==true) $.messager.progress({text: '处理中'});  // close the message box
    return v;    
}

function showMsg(title, msg) {
    $.messager.show({
        title: title,
        msg: msg,
        timeout: 0,
        showType: 'fade'
    });
}

function alertMsg(success, msg) {
	if (msg == "")return;
	if (msg.length>500) msg = msg.substr(0, 499) + "...";
	if (success){
		$.messager.alert("提示", msg, "info");
	}else{
		$.messager.alert("错误", msg, "warning");
	}
}

// define a Map object
function Map(){
	this.container = new Object();
}

Map.prototype.put = function(key, value){
	this.container[key] = value;
}

Map.prototype.get = function(key){
	return this.container[key];
}

Map.prototype.keySet = function() {
	var keyset = new Array();
	var count = 0;
	for (var key in this.container) {
		if (key == 'extend') {
			continue;
		}
		keyset[count] = key;
		count++;
	}
	return keyset;
}

Map.prototype.size = function() {
	var count = 0;
	for (var key in this.container) {
		if (key == 'extend'){
			continue;
		}
		count++;
	}
	return count;
}

Map.prototype.remove = function(key) {
	delete this.container[key];
}

Map.prototype.toString = function(){
	var str = "";
	for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
		str = str + keys[i] + "=" + this.container[keys[i]] + ";\n";
	}
	return str;
} 

Map.prototype.containsKey = function(key) {
	return (key in this.container);
}

var serverHeartbeatStatusMap = new Map();
serverHeartbeatStatusMap.put("ONLINE", "正常");
serverHeartbeatStatusMap.put("OFFLINE", "无连接");
serverHeartbeatStatusMap.put("AGENT_INSTALL_FAILED", "Agent安装失败");
serverHeartbeatStatusMap.put("AGENT_INSTALL_EXPIRED", "Agent安装超时");
serverHeartbeatStatusMap.put("AGENT_INSTALL_FINISHED", "连接中");
serverHeartbeatStatusMap.put("AGENT_INSTALL_ONGOING", "Agent安装中");

var vmStatusMap = new Map();
vmStatusMap.put("Running", "运行中");
vmStatusMap.put("Stopping", "停止中");
vmStatusMap.put("Stopped", "已停止");
vmStatusMap.put("Starting", "启动中");
vmStatusMap.put("Deleted", "已关闭");
vmStatusMap.put("Pending", "创建中");
vmStatusMap.put("Failed", "创建失败");
vmStatusMap.put("Unknown", "未知");

var regionMap = new Map();
regionMap.put("cn-beijing", "北京");
regionMap.put("cn-qingdao", "青岛");
regionMap.put("cn-hangzhou", "杭州");
regionMap.put("cn-hongkong", "香港");
regionMap.put("cn-shenzhen", "深圳");
regionMap.put("us-west-1", "美国硅谷");

var vmTypeMap = new Map();
vmTypeMap.put("ecs.t1.xsmall", "1核 512MB");
vmTypeMap.put("ecs.t1.small", "1核 1G");
vmTypeMap.put("ecs.s1.small", "1核 2G");
vmTypeMap.put("ecs.s1.medium", "1核 4G");
vmTypeMap.put("ecs.s1.large", "1核 8G");
vmTypeMap.put("ecs.s2.small", "2核 2G");
vmTypeMap.put("ecs.s2.large", "2核 4G");
vmTypeMap.put("ecs.s2.xlarge", "2核 8G");
vmTypeMap.put("ecs.s2.2xlarge", "2核 16G");
vmTypeMap.put("ecs.s3.medium", "4核 4G");
vmTypeMap.put("ecs.s3.large", "4核 8G");
vmTypeMap.put("ecs.m1.medium", "4核 16G");
vmTypeMap.put("ecs.m2.medium", "4核 32G");
vmTypeMap.put("ecs.c1.small", "8核 8G");
vmTypeMap.put("ecs.c1.large", "8核 16G");
vmTypeMap.put("ecs.m1.xlarge", "8核 32G");
vmTypeMap.put("ecs.m2.xlarge", "8核 64G");
vmTypeMap.put("ecs.c2.medium", "16核 16G");
vmTypeMap.put("ecs.c2.large", "16核 32G");
vmTypeMap.put("ecs.c2.xlarge", "16核 64G");

var vmNetworkTypeMap = new Map();
vmNetworkTypeMap.put("classic", "经典网络");
vmNetworkTypeMap.put("vpc", "专有网络");

Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}