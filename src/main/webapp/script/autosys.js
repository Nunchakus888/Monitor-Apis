
var tempName = "";
var triggerSum = 0;
var infoTop = 5;
var timer;
var timerDown;
var pageIndex = 1;

$(".back").click(function(){
 	clearInterval(timer);
	clearInterval(timerDown);
	window.location.href = "http://" + ip + "/index.html#/main/monitor/monitor-apiWatch";
});

$(".autoRefresh").click(function(){
	clearInterval(timer);
	clearInterval(timerDown);
	$(".loadingLogo").show();
	setTimeout(function(){
    	successTimer();
    	countTimer();
        restartTimer();
	},1000)
});

$(".detailed-info").mouseover(function(){
	$(this).show();
});

$(".detailed-info").mouseout(function(){
	$(this).hide();
});

$("body").on("mouseover",".treeview ul li .node-icon",function(){
	$(".detailed-info").html($(this).parent().find("pre").html()+'<i class="san-tip"></i>');
	var top = $(this).offset().top - $(".detailed-info").height()/2 + $(this).height()/2;
	var right = $("body").width() - $(this).offset().left - $(".san-tip").width();
	var staff = $(this).offset().top - $("body").scrollTop();
	if(top>0 && staff<800){
		$(".detailed-info .san-tip").css({"top":$(".detailed-info").height()/2 + $(".san-tip").height()/2});
		$(".detailed-info").css({"top":top,"right":right});
	}else if(top<0 && staff<800){
		$(".detailed-info .san-tip").css({"top":$(".detailed-info").height()/2 + $(".san-tip").height()/2 - (infoTop-top)});
		$(".detailed-info").css({"top":infoTop,"right":right});
	}else if(top>0 && staff>800){
		if($(this).parent().text().indexOf("JOB") != -1){
			$(".detailed-info .san-tip").css({"top":$(".detailed-info").height()/2 + $(".san-tip").height()/2});
			$(".detailed-info").css({"top":top,"right":right});
		}else{
			$(".detailed-info .san-tip").css({"top":$(".detailed-info").height()/2 + $(".san-tip").height()/2 + (top-700) - $("body").scrollTop()});
			$(".detailed-info").css({"top":700 + $("body").scrollTop(),"right":right});
		}
	}
	$(".detailed-info").show();
});

$("body").on("mouseout",".treeview ul li .node-icon",function(){
	$(".detailed-info").hide();
});

$(".pageup-btn").click(function(){
	pageIndex--;
	pageIndex==1 ? $(".pageup-btn").attr("disabled","disabled") : null;
	isRestartTimer(pageIndex);
	getTree(pageIndex);
});

$(".pagedown-btn").click(function(){
	$(".pageup-btn").removeAttr("disabled");
	pageIndex++;
	isRestartTimer(pageIndex);
	getTree(pageIndex);
});

function statusCode(code){
//	code = Number(code);
	switch(code){
		case 0 : return "未知";
		case 1 : return "异常";
		case 2 : return "执行中";
		case 3 : return "成功";
		default: return "错误";
	}
}

function jobFilter(node){
	var tempJobText1 = "";
	var tempJobText2 = "";
	for(var j in jobConfig){
		var tempJob = jobConfig[j];
		if(j == jobConfig.length-1){
			tempJobText1 += '<span class="infoKey">'+ autosysConfig[tempJob] +':</span><span class="infoValueJob">"'+ node[tempJob] +'"</span>';
		}else{
			tempJobText1 += '<span class="infoKey">'+ autosysConfig[tempJob] +':</span><span class="infoValueJob">"'+ node[tempJob] +'",</span>';
		}
	}
	for(var i in node){
		if(i == "nodes"){
			node.nodes.forEach(triggerFilter);
		}else{
			if( !jobConfig.includes(i) ){
				tempJobText2 += '<p><span class="infoKey">'+ autosysConfig[i] +':</span><span class="infoValueJob">"'+ node[i] +'"</span></p>';
			}
		}
	}
	node.text = '<p>'+ tempJobText1 +'<pre class="temp-pre">'+ tempJobText2 +'</pre></p>';
}

function triggerFilter(node){
	triggerSum++;
	var tempTriggerText1 = "";
	var tempTriggerText2 = "";
	for(var j in triggerConfig){
		var tempTrigger = triggerConfig[j];
		if(j == jobConfig.length-1){
			if(tempTrigger == "status"){
				tempTriggerText1 += '<span class="infoKey">'+ autosysConfig[tempTrigger] +':</span><span class="infoValue">"'+ statusCode(node[tempTrigger]) +'"</span>';
			}else{
				tempTriggerText1 += '<span class="infoKey">'+ autosysConfig[tempTrigger] +':</span><span class="infoValue">"'+ node[tempTrigger] +'"</span>';
			}
		}else{
			if(tempTrigger == "status"){
				tempTriggerText1 += '<span class="infoKey">'+ autosysConfig[tempTrigger] +':</span><span class="infoValue">"'+ statusCode(node[tempTrigger]) +'",</span>';
			}else{
				tempTriggerText1 += '<span class="infoKey">'+ autosysConfig[tempTrigger] +':</span><span class="infoValue">"'+ node[tempTrigger] +'",</span>';
			}
		}
	}
	for(var i in node){
		if( !triggerConfig.includes(i) ){
			if(i == "status"){
				tempTriggerText2 += '<p><span class="infoKey">'+ autosysConfig[i] +':</span><span class="infoValue">"'+ statusCode(node[i]) +'"</span></p>';
			}else{
				tempTriggerText2 += '<p><span class="infoKey">'+ autosysConfig[i] +':</span><span class="infoValue">"'+ node[i] +'"</span></p>';
			}
		}
	}
	var startTime = new Date(node.startTime.replace(/-/g,"/"));
	var timeDiff = new Date() - startTime;
	if(timeDiff < 12*60*60*1000){
		node.text = '<p class="triggerLi">'+ tempTriggerText1 +'<pre class="temp-pre">'+ tempTriggerText2 +'</pre><b class="hot-tip">hot</b></p>';
	}else if(timeDiff >= 12*60*60*1000 && timeDiff <= 24*60*60*1000){
		node.text = '<p class="triggerLi">'+ tempTriggerText1 +'<pre class="temp-pre">'+ tempTriggerText2 +'</pre><b class="new-tip">new</b></p>';
	}else{
		node.text = '<p class="triggerLi">'+ tempTriggerText1 +'<pre class="temp-pre">'+ tempTriggerText2 +'</pre></p>';
	}
	
}

function getTree(page){
	$(".pagedown-btn").attr("disabled","disabled");
	$.ajax({
//		type: "get",
//		url: "temp.json",
		type: "post",
		url: "http://" + ip + "/autosys/jobInfo",
		data: '{"page":'+ page +',"size":'+ pageSize +'}',
		contentType: "appliction/json;charset=UTF-8",
		dataType: "json",
		success: function(data) {
			if(data.length){
				triggerSum = 0;
				data.forEach(jobFilter);
				triggerSum==pageSize ? $(".pagedown-btn").removeAttr("disabled") : null;
				$(".currentPage").text("第" + pageIndex +"页");
			}else{
				$(".currentPage").text("");
			}
			$('#tree').treeview({
				levels: 2,
				color: "#ffebc8",
				selectedColor: "#ffebc8",
				borderColor: "#474a4c",
				backColor: "#232628",
				onhoverColor: "#1d4243",
				selectedBackColor: "#1a5656",
	          	nodeIcon: "glyphicon glyphicon-list-alt",
				data: data,
				onNodeSelected: function(event, node) {
	            	$('.box').text(node.text);
	            },
	            onNodeUnselected: function (event, node) {
	            	$('.box').text(node.text);
	            }
			});
//			$('#tree').treeview('search', [ 'schedName', { ignoreCase: true, exactMatch: false }]);
		},
		error: function(request, err, ex) {
			console.log(request.status + ">>> " + request.statusText);
		}
	});
}

function isRestartTimer(page){
	if(page == 1){
		countTimer();
		restartTimer();
	}else{
		clearInterval(timer);
		clearInterval(timerDown);
		$(".loadingLogo").hide();
		radialObj.value(150);
	}
}

function restartTimer(){
    clearInterval(timer);
    timer = setInterval(countTimer,16000);
}

function countTimer(){
	$(".loadingLogo").hide();
	clearInterval(timerDown);
	var tempTotal = 150;
	radialObj.value(tempTotal);
	timerDown = setInterval(function(){
    	radialObj.value(tempTotal--);
    	radialObj.value()<=0 ? $(".loadingLogo").show() : $(".loadingLogo").hide();
		if(tempTotal == -9){
			successTimer();
		}
	},100,160)
}

function successTimer(){
	pageIndex = 1;
	$(".pageup-btn").attr("disabled","disabled");
	getTree(pageIndex);
}

var radialObj = radialIndicator(".autoRefresh",{
    radius : 10,
    percentage :false,
    roundCorner : true,
    barBgColor : '#333',
    barColor: '#f38b1e',
    barWidth: 2,
    initValue : 150,
    minValue: 0,
	maxValue: 150,
	fontSize: 13,
	format: function (value) {
        return Math.ceil(value/10);
    }
});

getTree(pageIndex);
countTimer();
restartTimer();


