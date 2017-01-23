
var Alljson;
var chooseFlag;								//add&edit标识
var operationFlag;							//确认标识
var clickFlag;								//点击对象标识
var tempObj;								//存放删除时的父节点
var tempChildren;							//存放获取的节点子集合
var tempNodeValue;							//存放获取的节点值

var tdTemplateHtml = "<button class='edit-btn ss-btn ss-btn-info fa fa-pencil-square-o'></button><button class='editA-btn ss-btn ss-btn-info fa fa-plus'></button><button class='editR-btn ss-btn ss-btn-info fa fa-times'></button>";
var tempTrVal1 = "";
var tempTrVal2 = "";

var clickTipFlag = true;
var flag = false;
var RPh = window.screen.availHeight;
var RPw = window.screen.availWidth;

var m = [20, 100, 20, 100],
	w = RPw*0.5 - m[1] - m[3],
	h = RPh*0.9 - m[0] - m[2],
	i = 0,
	root;

var tree = d3.layout.tree()
	.size([h, w]);

var diagonal = d3.svg.diagonal()
	.projection(function(d) {
		return [d.y, d.x];
	});

var vis = d3.select("#body").append("svg:svg")
	.attr("class","svg")
	.attr("onselectstart","return false")
	.attr("width", w + m[1] + m[3])
	.attr("height", h + m[0] + m[2])
	.append("svg:g")
	.attr("transform", "translate(" + m[3] + "," + m[0] + ")");

$.ajax({
	type: "POST",
	contentType: "appliction/json;charset=UTF-8",
	dataType: "json",
	url: "http://"+ ip +"/configManage/getChildren",
	data: '{"path":"/com/sumscope"}',
	success: function(info) {
		if(info.result.status){
			var json = info.data
			json.name = "sumscope";
			json.flag = true;
			for(var i in json.children){
				json.children[i].flag = false;
				if(json.children[i].children.length){
					json.children[i]._children = json.children[i].children;
					json.children[i].children = null;
				}else{
					json.children[i].children = null;
				}
			}
			Alljson = {name:"com",path:"/com",value:"",children:[json],flag:"true"};
			root = Alljson;
			root.x0 = h / 2;
			root.y0 = 0;
			update(root);
		}else{
			errorInfo(info.result);
		}
	},
	error: function(request, err, ex) {
		console.log(request.status + ">>> " + request.statusText);
	}
});

function update(source) {
	var duration = d3.event && d3.event.altKey ? 5000 : 500;

	// Compute the new tree layout.
	var nodes = tree.nodes(root).reverse();

	// Normalize for fixed-depth.
	nodes.forEach(function(d) {
		d.y = d.depth * (w/Layer);
	});

	// Update the nodes…
	var node = vis.selectAll("g.node")
		.data(nodes, function(d) {
			return d.id || (d.id = ++i);
		});

	// Enter any new nodes at the parent's previous position.
	var nodeEnter = node.enter().append("svg:g")
		.attr("class", "node")
		.attr("transform", function(d) {
			return "translate(" + source.y0 + "," + source.x0 + ")";
		})
		.on("click", function(d) {
			tempNodeValue = d.value;
			$("text").removeClass("active");
			$(this).find("text").addClass("active");
			$(".menu-title").html(d.name);
			$(".nodePath span").html(d.path);
			$(".nodeValue").find("a").length == 0 ? $(".edit-box-wrap").hide() : $(".nodeValue a").remove();
			if(d.nodeType){
				$(".nodeValue").append("<a href='"+ d.value +"'>"+ d.value.split("]").pop() +"</a>");
			}else{
				$(".edit-box-wrap").show();
				editTableHtml(d.value);
			}
			if(d.path == "/com"){
				$(".menu-operation button").attr("disabled",true);
				btnDisabled(true);
			}else if(d.path == "/com/sumscope"){
				$(".menu-operation button").attr("disabled",false);
				$(".delData").attr("disabled",true);
				btnDisabled(false);
			}else{
				$(".menu-operation button").attr("disabled",false);
				btnDisabled(false);
			}
			if (d._clickid) {
					clearTimeout(d._clickid);
					d._clickid = null;
					// process double click
					$(".menu").animate({right: "-50%"});
					$(".menu-operation button").attr("disabled",true);
					if(!d.flag){
						$.ajax({
							type: "POST",
							contentType: "appliction/json;charset=UTF-8",
							dataType: "json",
							url: "http://"+ ip +"/configManage/getChildren",
							data: '{"path":"'+ d.path +'"}',
							success: function(info) {
								if(info.result.status){
									tempChildren = info.data.children;
									common(tempChildren);
									if(tempChildren.length){
										for(var i in tempChildren){
											if(tempChildren[i].children.length){
												tempChildren[i]._children = tempChildren[i].children;
												tempChildren[i].children = null;
											}else{
												tempChildren[i].children = null;
											}
										}
										d._children = null;
										d.children = tempChildren;
										d.flag = true;
										update(d);
									}
								}else{
									errorInfo(info.result);
								}
							}
						});
					}else {
						toggle(d);
						update(d);
					}
				} else {
					d._clickid = setTimeout(function() {
						// process simple click
						$(".menu").animate({right: 0});
						if(d.path != clickFlag){
							clickFlag = d.path;
							$.ajax({
								type: "POST",
								contentType: "appliction/json;charset=UTF-8",
								dataType: "json",
								url: "http://"+ ip +"/configManage/getChildren",
								data: '{"path":"'+ d.path +'"}',
								success: function(info) {
									if(info.result.status){
										tempChildren = info.data.children;
										common(tempChildren);
//										for(var i in tempChildren){
//											if(tempChildren[i].children.length){
//												tempChildren[i]._children = tempChildren[i].children;
//												tempChildren[i].children = null;
//											}else{
//												tempChildren[i].children = null;
//											}
//										}
//										d._children = null;
//										d.children = tempChildren;
									}else{
										errorInfo(info.result);
									}
								}
							});
						}else{
							common(tempChildren);
						}
						d._clickid = null;
					}.bind(this), 350);
				} 
		});

	nodeEnter.append("svg:circle")
		.attr("r", 1e-6)
		.style("fill", function(d) {
			return d._children ? "#179595" : "#fff";
		});

	nodeEnter.append("svg:text")
		.attr("x", function(d) {
			return d.children || d._children ? -10 : 10;
		})
		.attr("dy", ".35em")
		.attr("text-anchor", function(d) {
			return d.children || d._children ? "end" : "start";
		})
		.text(function(d) {
			return d.name;
		})
		.style("fill-opacity", 1e-6);

	// Transition nodes to their new position.
	var nodeUpdate = node.transition()
		.duration(duration)
		.attr("transform", function(d) {
			return "translate(" + d.y + "," + d.x + ")";
		});

	nodeUpdate.select("circle")
		.attr("r", 5)
		.style("fill", function(d) {
			return d._children ? "#179595" : "#fff";
		});

	nodeUpdate.select("text")
		.style("fill-opacity", 1);

	// Transition exiting nodes to the parent's new position.
	var nodeExit = node.exit().transition()
		.duration(duration)
		.attr("transform", function(d) {
			return "translate(" + source.y + "," + source.x + ")";
		})
		.remove();

	nodeExit.select("circle")
		.attr("r", 1e-6);

	nodeExit.select("text")
		.style("fill-opacity", 1e-6);

	// Update the links…
	var link = vis.selectAll("path.link")
		.data(tree.links(nodes), function(d) {
			return d.target.id;
		});

	// Enter any new links at the parent's previous position.
	link.enter().insert("svg:path", "g")
		.attr("class", "link")
		.attr("d", function(d) {
			var o = {
				x: source.x0,
				y: source.y0
			};
			return diagonal({
				source: o,
				target: o
			});
		})
		.transition()
		.duration(duration)
		.attr("d", diagonal);

	// Transition links to their new position.
	link.transition()
		.duration(duration)
		.attr("d", diagonal);

	// Transition exiting nodes to the parent's new position.
	link.exit().transition()
		.duration(duration)
		.attr("d", function(d) {
			var o = {
				x: source.x,
				y: source.y
			};
			return diagonal({
				source: o,
				target: o
			});
		})
		.remove();

	// Stash the old positions for transition.
	nodes.forEach(function(d) {
		d.x0 = d.x;
		d.y0 = d.y;
	});

}

// Toggle children.
function toggle(d) {
	if(d.children) {
		d._children = d.children;
		d.children = null;
	} else {
		d.children = d._children;
		d._children = null;
	}
}

// Common showValue function
function common(data) {
	$(".nodeChildren b").html("Children["+ data.length +"]:");
	$(".childrenList").html("");
	for(var i in data){
		if(i == data.length-1){
			$(".childrenList").append("<em>"+ data[i].name +" </em>");
		}else{
			$(".childrenList").append("<em>"+ data[i].name +", </em>");
		}
	}
}

function errorInfo(result){
	switch(result.echoCode){
		case 0:
			alert(result.msg);
			setTimeout(function(){
				window.location.href = "http://" + ip + "/index.html#/login"
			},1000)
			break;
		default:
  			alert("ErrorCode " + result.echoCode + " ： " + result.msg);
	}
}
