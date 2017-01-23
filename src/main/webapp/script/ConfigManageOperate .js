$(".cutline li:nth-of-type(4)").html("<i class='fa fa-arrows-h'></i>最多支持"+ Layer +"层");
$(".operationShade").width($(".svg").width()).height($(".svg").height());

$(".back").click(function(){
	window.location.href = "http://" + ip + "/index.html#/main/monitor/monitor-apiWatch";
});

$("*").click(function (event) {
    if ($(this).hasClass("svg")){
    	$(".menu").animate({right: "-50%"});
        $(".menu-title").html("NodeName");
		$(".nodePath span").html("");
		$(".nodeValue").find("a").length == 0 ? $(".edit-box-wrap").hide() : $(".nodeValue a").remove();
		$(".nodeChildren b").html("Children[ ]");
		$(".childrenList").html("");
		$("text").removeClass("active");
		
    }
    var e = window.event || event; 
	if ( e.stopPropagation ){				//如果提供了事件对象，则这是一个非IE浏览器 
		e.stopPropagation(); 
	}else{									//兼容IE的方式来取消事件冒泡 
		window.event.cancelBubble = true; 
	} 
});

$(".addData").click(function(){
	chooseFlag = "add";
	btnDisabled(true);
	chooseRadio();
	$(".operationShade").show();
	$(".inputWay").show();
	$(".menu-operation button").attr("disabled",true);
	$(".inputName").attr("disabled",false).show();
	$(".menu-choose button").show();
});

$(".editData").click(function(){
	chooseFlag = "edit";
	btnDisabled(true);
	$(".operationShade").show();
	$(".menu-operation button").attr("disabled",true);
	$(".inputName").val($(".menu-title").html()).attr("disabled",true).show();
	$(".menu-choose button").show();
	$(".inputWay").show();
	if($(".nodeValue").find("a").length==0){
		$(".inputWay").find("input[type='radio']").eq(0).prop("checked", true);
		$(".inputValue").val(tempNodeValue).show();
		$("#uploadFile").hide();
		operationFlag = "editText";
	}else{
		$(".inputWay").find("input[type='radio']").eq(1).prop("checked", true);
		$(".inputValue").hide();
		$("#uploadFile").show();
		operationFlag = "editFile";
	}
});

$(".inputWay").change(function(){
	chooseRadio();
});

$(".delData").click(function(){
	$(".shade").show();
	$(".warn-tip-title span").html($(".menu-title").html()).css({"color":"red"});
});

$(".warn-tip-cancel").click(function(){
	$(".shade").hide();
});

$(".warn-tip-confirm").click(function(){
	$.ajax({
		type: "POST",
		contentType: "appliction/json;charset=UTF-8",
		dataType: "json",
		url: "http://"+ ip +"/configManage/deleteChild",
		data: '{"path":"'+ $(".nodePath span").html() +'"}',
		success: function(info) {
			if(info.result.status){
				var comparePath = $(".nodePath span").html();
				Alljson.children.forEach(LoopDel);
				function LoopDel(node,index) {
					for(var i in node.children){
						if(node.children[i].path == comparePath){
							tempObj = node;
						}
					}
					if(node.path == comparePath){
						tempObj.children.splice(index,1);
						update(node);
					}else{
						if(node.children){
							node.children.forEach(LoopDel);
						}
					}
				}
				$(".menu-title").html("NodeName");
				$(".nodePath span").html("");
				$(".nodeChildren span").html("");
				$(".childrenList").html("");
				$(".nodeValue").find("a").length == 0 ? $(".edit-box-wrap").hide() : $(".nodeValue a").remove();
				$(".menu-operation button").attr("disabled",true);
				$(".menu").animate({right: "-50%"});
			}else{
				errorInfo(info.result);
			}
			$(".shade").hide();
		}
	});
});

$(".menu-cancel").click(function(){
	btnDisabled(false);
	$(".menu-operation button").attr("disabled",false);
	$(".nodePath span").html() == "/com/sumscope" ? $(".delData").attr("disabled",true) : null;
	$(".inputName").val("").hide();
	$(".inputValue").val("").hide();			//test textarea
	$("#uploadFile").hide();
	$(".inputWay").hide();
	$(".menu-choose button").hide();
	$(".operationShade").hide();
	operationFlag = null;
	chooseFlag = null;
});

$(".menu-confirm").click(function(){
	if(operationFlag == "editText"){
		if(!valueFilter($(".inputValue").val())){
			return false;
		}
		submitEditText($(".nodePath span").html(),$(".inputName").val(),$(".inputValue").val());
	}else if(operationFlag == "addText"){
		if(!nodeNameReg($(".inputName").val())){
			return false;
		}
		if(!valueFilter($(".inputValue").val())){
			return false;
		}
		var tempName = $(".inputName").val();
		var tempValue = $(".inputValue").val();
		$.ajax({
			type: "POST",
			contentType: "appliction/json;charset=UTF-8",
			dataType: "json",
			url: "http://"+ ip +"/configManage/createChild",
			data: '{"path":"'+ $(".nodePath span").html() +'","name":"'+ tempName + '","value":"'+ tempValue +'"}',
			success: function(info) {
				if(info.result.status){
					tempObj = info.data;
					$(".childrenList").append("<em>, "+ tempObj.name +"</em>");
					var comparePath = $(".nodePath span").html();
					Alljson.children.forEach(LoopAdd);
					function LoopAdd(node) {
						if(node.path == comparePath){
							tempChildren.push(tempObj);
							node.children = tempChildren;
							node._children = null;
							node.flag = true;
							update(node);
						}else{
							if(node.children){
								node.children.forEach(LoopAdd);
							}
						}
					}
					commonRequestSuccessMenuManage(operationFlag);
				}else{
					errorInfo(info.result);
				}
			}
		});
	}else if(operationFlag == "editFile"){
		var formData = new FormData($("#uploadFile")[0]);
		$.ajax({
	        url: "http://"+ ip +"/configManage/updateAndUpload?path="+ $(".nodePath span").html() +"&name="+ $(".inputName").val(),
			type: "POST",
			contentType: "appliction/json;charset=UTF-8",
			dataType: "json",
	        cache: false,  
	        data: formData,
	        processData: false,
	        contentType: false,
	        success: function(info){
	            if(info.result.status){
	            	tempObj = info.data;
	        		$(".nodeValue").find("a").length == 0 ? $(".edit-box-wrap").hide() : $(".nodeValue a").remove();
	        		$(".nodeValue").append("<a href='"+ tempObj.value +"'>"+ tempObj.value.split("]").pop() +"</a>");
					var comparePath = $(".nodePath span").html();
					Alljson.children.forEach(LoopEdit);
					function LoopEdit(node) {
						if(node.path == comparePath){
							node.value = tempObj.value;
							node.nodeType = 1;
						}else{
							if(node.children){
								node.children.forEach(LoopEdit);
							}
						}
					}
	            	commonRequestSuccessMenuManage(operationFlag);
	            }else{
	            	errorInfo(info.result);
	            }
	        }
	    });
	}else if(operationFlag == "addFile"){
		if(!nodeNameReg($(".inputName").val())){
			return false;
		}
		var formData = new FormData($("#uploadFile")[0]);
		$.ajax({
	        url: "http://"+ ip +"/configManage/addAndUpload?path="+ $(".nodePath span").html() +"&name="+ $(".inputName").val(),
			type: "POST",
			contentType: "appliction/json;charset=UTF-8",
			dataType: "json",
	        cache: false,  
	        data: formData,
	        processData: false,
	        contentType: false,
	        success: function(info){
	            if(info.result.status){
	            	tempObj = info.data;
					$(".childrenList").append("<em>, "+ tempObj.name +"</em>");
					var comparePath = $(".nodePath span").html();
					Alljson.children.forEach(LoopAdd);
					function LoopAdd(node) {
						if(node.path == comparePath){
							tempChildren.push(tempObj);
							node.children = tempChildren;
							node._children = null;
							node.flag = true;
							update(node);
						}else{
							if(node.children){
								node.children.forEach(LoopAdd);
							}
						}
					}
	            	commonRequestSuccessMenuManage(operationFlag);
	            }else{
	            	errorInfo(info.result);
	            }
	        }
	    });
	}
});

function submitEditText(tempPath,tempName,tempValue){
	$.ajax({
		type: "POST",
		contentType: "appliction/json;charset=UTF-8",
		dataType: "json",
		url: "http://"+ ip +"/configManage/updateChild",
		data: '{"path":"'+ tempPath +'","name":"'+ tempName + '","value":"'+ tempValue +'"}',
		success: function(info) {
			if(info.result.status){
				$(".nodeValue").find("a").length == 0 ? $(".edit-box-wrap").hide() : $(".nodeValue a").remove();
				$(".edit-box-wrap").show();
				$(".edit-box button").attr("disabled","disabled");
				editTableHtml(tempValue);
				Prism.highlightAll(true);
				var comparePath = tempPath;
				tempNodeValue = tempValue;
				Alljson.children.forEach(LoopEdit);
				function LoopEdit(node) {
					if(node.path == comparePath){
						node.value = tempValue;
						node.nodeType = 0;
					}else{
						if(node.children){
							node.children.forEach(LoopEdit);
						}
					}
				}
				commonRequestSuccessMenuManage(operationFlag);
			}else{
				errorInfo(info.result);
			}
		}
	});
}

function commonRequestSuccessMenuManage(flag){
	if(flag == "addText" || flag == "editText"){
		$(".inputValue").val("").hide();
	}else if(flag == "addFile" || flag == "editFile"){
		$("#uploadFile").hide();
	}
	$(".menu-operation button").attr("disabled",false);
	$(".nodePath span").html() == "/com/sumscope" ? $(".delData").attr("disabled",true) : null;
	$(".inputWay").hide();
	$(".inputName").val("").hide();
	$(".menu-choose button").hide();
	$(".operationShade").hide();
	btnDisabled(false);
}

function nodeNameReg(name){
	var reg = /^[a-zA-Z][a-zA-Z0-9_]{0,14}$/
	if(name.replace(/\s+/g,"").length == 0){
		alert("文件名不能为空");
		return false;
	}else if(!reg.test(name)){
		alert("节点名应由字母开头的15位以内大小写字母、数字、下划线组成");
		return false;
	}else{
		return true;
	}
}

function valueFilter(value){
	var tempArr = value.split("\n");
	for(var i in tempArr){
		var index = tempArr[i].indexOf("=");
		var len = tempArr[i].length;
		if(index != -1){
			if(tempArr[i].substring(0,index).replace(/\s+/g,"").length == 0){
				alert("key值不能为空（value存在时）");
				return false;
			}
		}
	}
	return true;
}

//复制功能
//$(".nodePath").mouseover(function(){
//	$(".nodePath span").html() ? $(".copyPath").show() : $(".copyPath").hide();
//});
//
//$(".nodeValue").mouseover(function(){
//	var text = $(".nodeValue").find("a").length == 0 ? $(".nodeValue pre code").html() : $(".nodeValue a").html();
//	text ? $(".copyValue").show() : $(".copyValue").hide();
//});

//$(".copyPath").zclip({
//	path: "script/ZeroClipboard.swf",
//	copy: function(){
//	return $(".nodePath span").html();
//	},
//	beforeCopy:function(){						/* 按住鼠标时的操作 */
//	},
//	afterCopy:function(){						/* 复制成功后的操作 */
//		var $copysuc = $("<div class='copy-tips'><div class='copy-tips-wrap'>☺ 复制成功</div></div>");
//		$("body").find(".copy-tips").remove().end().append($copysuc);
//		$(".copy-tips").fadeOut(2000);
//  }
//});
//
//$(".copyValue").zclip({
//	path: "script/ZeroClipboard.swf",
//	copy: function(){
//	return  $(".nodeValue").find("a").length == 0 ? tempNodeValue : $(".nodeValue a").html();
//	},
//	beforeCopy:function(){},
//	afterCopy:function(){						/* 复制成功后的操作 */
//		var $copysuc = $("<div class='copy-tips'><div class='copy-tips-wrap'>☺ 复制成功</div></div>");
//		$("body").find(".copy-tips").remove().end().append($copysuc);
//		$(".copy-tips").fadeOut(1800);
//  }
//});

function chooseRadio (){
	if($(".inputWay").find("input")[0].checked){
		$(".inputValue").show();
		$("#uploadFile").hide();
		operationFlag = (chooseFlag == "add") ? "addText":"editText" ;
	}else{
		$(".inputValue").hide();
		$("#uploadFile").show();
		operationFlag = (chooseFlag == "add") ? "addFile":"editFile";
	}
}

function valueTemp (){
	var temp = "";
	var len = $(".content").length-1;
	$(".content").each(function(index){
		var val1 = $(this).find("td").eq(0).text();
		var val2 = $(this).find("td").eq(1).text();
		var val1Mark = val1.replace(/\s+/g,"").length;
		var val2Mark = val2.replace(/\s+/g,"").length;
		if(val1Mark!=0 && val2Mark!=0){
			index==len ? (temp += val1+"="+val2) : (temp += val1+"="+val2+"\n");
		}else{
			index==len ? (temp += val1+val2) : (temp += val1+val2+"\n");
		}
	})
	return temp;
}

//table
function editTableHtml(value){
	$(".edit-tbody").html("");
	var tempArr = value.split("\n");
	for(var i in tempArr){
		var index = tempArr[i].indexOf("=");
		var len = tempArr[i].length;
		if(index != -1){
			var tr = "<tr class='content'><td>"+ tempArr[i].substring(0,index) +"</td><td>"+ tempArr[i].substring(index+1,len) +"</td><td class='tdBtn'>"+ tdTemplateHtml+ "</td></tr>";
		}else{
			var tr = "<tr class='content'><td>"+ tempArr[i] +"</td><td></td><td class='tdBtn'>"+ tdTemplateHtml+ "</td></tr>";
		}
		$(".edit-tbody").append(tr)
	}
}

function btnDisabled(flag){
	$(".edit-btn").each(function(){
		flag ? $(this).attr("disabled","disabled") : $(this).removeAttr("disabled");
	})
	$(".editA-btn").each(function(){
		flag ? $(this).attr("disabled","disabled") : $(this).removeAttr("disabled");
	})
	$(".editR-btn").each(function(){
		flag ? $(this).attr("disabled","disabled") : $(this).removeAttr("disabled");
	})
}

//edit
$(".edit-tbody").on("click",".edit-btn",function(){
	btnDisabled(true);
	$(".menu-operation button").attr("disabled",true);
	$(".operationShade").show();
	tempTrVal1 = $(this).parent().prev().prev().text();
	tempTrVal2 = $(this).parent().prev().text();
	$(this).parent().prev().prev().html("<textarea>"+ tempTrVal1 +"</textarea>");
	$(this).parent().prev().html("<textarea>"+ tempTrVal2 +"</textarea>");
	$(this).parent().html("<button class='editT-btn ss-btn ss-btn-info fa fa-check'></button><button class='editF-btn ss-btn ss-btn-info fa fa-times'></button>");
	$(".edit-box button").attr("disabled","disabled");
})

$(".edit-tbody").on("click",".editT-btn",function(){
	btnDisabled(false);
	var val1 = $(this).parent().prev().prev().find("textarea").val();
	var val2 = $(this).parent().prev().find("textarea").val();
	$(this).parent().prev().prev().text(val1);
	$(this).parent().prev().text(val2);
	$(this).parent().html( tdTemplateHtml );
	if(valueTemp()==tempNodeValue){
		$(".operationShade").hide();
		$(".menu-operation button").attr("disabled",false);
	}else{
		$(".edit-box button").removeAttr("disabled");
	}
})

$(".edit-tbody").on("click",".editF-btn",function(){
	btnDisabled(false);
	$(this).parent().prev().prev().text(tempTrVal1);
	$(this).parent().prev().text(tempTrVal2);
	$(this).parent().html( tdTemplateHtml );
	if(valueTemp()==tempNodeValue){
		$(".operationShade").hide();
		$(".menu-operation button").attr("disabled",false);
	}else{
		$(".edit-box button").removeAttr("disabled");
	}
})

//add
$(".edit-tbody").on("click",".editA-btn",function(){
	$(".menu-operation button").attr("disabled",true);
	$(".operationShade").show();
	var tr = "<tr class='content'><td></td><td></td><td class='tdBtn'>"+ tdTemplateHtml +"</td></tr>";
	$(this).parent().parent().after(tr);
	$(".edit-box button").removeAttr("disabled");
})

//remove
$(".edit-tbody").on("click",".editR-btn",function(){
	$(".menu-operation button").attr("disabled",true);
	$(".operationShade").show();
	$(this).parent().parent().remove();
	$(".edit-box button").removeAttr("disabled");
})

//submit
$(".edit-submit").click(function(){
	var temp = "";
	var len = $(".content").length-1;
	var submitMark = true;
	$(".content").each(function(index){
		var val1 = $(this).find("td").eq(0).text();
		var val2 = $(this).find("td").eq(1).text();
		var val1Mark = val1.replace(/\s+/g,"").length;
		var val2Mark = val2.replace(/\s+/g,"").length;
		if(val1Mark==0 && val2Mark!=0){
			alert("key值不能为空（value存在时）");
			submitMark = false;
			return false
		}else if(val1Mark!=0 && val2Mark!=0){
			index==len ? (temp += val1+"="+val2) : (temp += val1+"="+val2+"\n");
		}else{
			index==len ? (temp += val1+val2) : (temp += val1+val2+"\n");
		}
	})
	if(submitMark){
		submitEditText($(".nodePath span").text(),$(".menu-title").text(),temp);
	}
})

//export
$(".edit-export").click(function(){

})

//cancle
$(".edit-cancle").click(function(){
	$(".operationShade").hide();
	$(".menu-operation button").attr("disabled",false);
	editTableHtml(tempNodeValue);
	$(".edit-box button").attr("disabled","disabled");
})



