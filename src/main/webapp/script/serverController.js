angular.module("appModule").
controller('serverController', function ($scope, $filter, $http, $q, $state, $timeout, All, uiGridConstants, websocketService, utils) {
	var ws = null;
	$scope.serverArr = [];
	$scope.unregisterWatch;
	$scope.reconnect = true;
	$scope.navIndex = 0;
    $scope.upLockBiz = $scope.upLockEx = true;
    $scope.downLockBiz = $scope.downLockEx = true;
    $scope.pageIndexBiz = $scope.pageIndexEx = 1;

	$scope.serverNav = function(index,obj){
		$scope.navIndex = index;
		$scope.serverName = obj.name;
		$scope.upLockBiz = $scope.upLockEx = true;
		$scope.isTooltipSqlContentShow = false;
		$scope.pageIndexBiz = $scope.pageIndexEx = 1;
		$scope.gridOptionsException.data = $scope.gridOptionsBusiness.data = [];
		getExceptionData($scope.serverName,$scope.pageIndexEx);
		getBusinessData($scope.serverName,$scope.pageIndexBiz);
	}

	$scope.pageUp = function(tip){
		if(tip == "biz"){
			$scope.pageIndexBiz--;
	        if ($scope.pageIndexBiz == 1) {
	            $scope.upLockBiz = true;
	        }
	        getBusinessData($scope.serverName, $scope.pageIndexBiz);
		}else if(tip == "ex"){
			$scope.pageIndexEx--;
	        if ($scope.pageIndexEx == 1) {
	            $scope.upLockEx = true;
	        }
	        getExceptionData($scope.serverName, $scope.pageIndexEx);
		}
	}

	$scope.pageDown = function(tip){
		if(tip == "biz"){
			$scope.upLockBiz = false;
	        $scope.pageIndexBiz++;
	        getBusinessData($scope.serverName, $scope.pageIndexBiz);
		}else if(tip == "ex"){
			$scope.upLockEx = false;
	        $scope.pageIndexEx++;
	        getExceptionData($scope.serverName, $scope.pageIndexEx);
		}
	}

	/*gridOptionsException*/
	$scope.gridOptionsException = {
        enableSorting: false,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: false,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'time', displayName: 'UPDATE_TIME', type: 'date', sort: {direction:uiGridConstants.DESC}, width: '15%', cellTemplate:cellTemplateHtml("time")},
            {name: 'errorCode', displayName: 'errorCode', width: '15%', cellTemplate:cellTemplateHtml("errorCode")},
            {name: 'errorStack', displayName: 'errorStack', width: '35%', cellTemplate:cellTemplateHtml("errorStack")},
            {name: 'errorMessage', displayName: 'errorMessage', width: '35%', cellTemplate:cellTemplateHtml("errorMessage")}
        ],
        data: [],
		onRegisterApi: function(gridApi) {
			$scope.gridServer1 = gridApi;
			gridApi.cellNav.raise.navigate = function (a) {
//				console.log(a);
				var row = a.row,
	                col = a.col,
	                msg = '',
	                preRow = '',
	                preCol = '',
	                field = col.field;

				if (field === 'errorStack') {
	                msg = row.entity[field]
	                if (!msg) {
	                    $scope.isTooltipSqlContentShow = false;
	                    return false;
	                }
	            } else {
	                $scope.isTooltipSqlContentShow = false;
	                return false;
	            }
				if (preRow === row) {
	                if (preCol === col) {
	                    utils.rowSelectionOperation($scope, a.row, false);
	                } else {
	                    utils.rowSelectionOperation($scope, a.row, true, msg);
	                    preCol = col;
	                }
	            } else {
	                if (preCol === col) {
	                    utils.rowSelectionOperation($scope, a.row, false);
	                } else {
	                    utils.rowSelectionOperation($scope, a.row, true, msg, true);
	                    preCol = col;
	                }
	                preRow = row;
	            }
			}
		}
	};

    /*gridOptionsBusiness*/
	$scope.gridOptionsBusiness = {
        enableSorting: false,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: true,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'time', displayName: 'UPDATE_TIME', type: 'date', sort: {direction:uiGridConstants.DESC}, width: '15%', cellTemplate:cellTemplateHtml("time")},
            {name: 'flowType', displayName: 'flowType', cellTemplate:cellTemplateHtml("flowType")},
            {name: 'total', displayName: 'total', cellTemplate:cellTemplateHtml("total")},
            {name: 'successCount', displayName: 'successCount', cellTemplate:cellTemplateHtml("successCount")},
            {name: 'handleAverage', displayName: 'handleAverage', cellTemplate:cellTemplateHtml("handleAverage")},
            {name: 'handleMax', displayName: 'handleMax', cellTemplate:cellTemplateHtml("handleMax")},
            {name: 'handleMin', displayName: 'handleMin', cellTemplate:cellTemplateHtml("handleMin")},
            {name: 'handleMedian', displayName: 'handleMedian', cellTemplate:cellTemplateHtml("handleMedian")}
        ],
        data: [],
		onRegisterApi: function(gridApi) {
			$scope.gridServer2 = gridApi;
			gridApi.selection.on.rowSelectionChanged($scope, function (row, d, i) {
				$scope.isTooltipSqlContentShow = false;
			});
		}
    };

    function cellTemplateHtml(tag){
        return '<div style="height:30px;line-height: 30px;" class="{{row.entity.class}}">{{row.entity.'+ tag +'}}</div>'
    };

    function ServerUpDate(){
    	var randomNum = websocketService.generateMixed(8);
    	ws = new WebSocket(All.WebSockUrl + randomNum +"/websocket");
        ws.onopen = function () {
            console.log("onopen");
    		if($state.current.controller != "serverController"){
    			$scope.reconnect = false;
    			ws.close();
    			ws = null
    		}
        };
        ws.onmessage = function (event) {
        	console.log(event.data);
            var data = JSON.parse(event.data);
            moldFilter(data.messageT,data.data);
        };
        ws.onclose = function () {
            console.log("onclose");
            if($scope.reconnect){
            	ServerUpDate();
            }else{
            	$scope.reconnect = true;
            }
        };
    }

    function moldFilter(messageT,data){
    	var tempData;
    	if(messageT == "ht"){
    		tempData = data;
    		for(var i in $scope.serverArr){
    			if($scope.serverArr[i].name == tempData.serviceKey){
    				$scope.serverArr[i].status = tempData.status;
    				break;
    			}
    		}
    		$scope.$apply();
    	}else if(messageT=="ex" && $scope.pageIndexEx==1){
    		tempData = serverFilter($scope.serverName,data);
    		if(tempData){
	    		$scope.gridOptionsException.data.unshift(tempData);
	    		$scope.gridOptionsException.data.length>=20 ? $scope.downLockEx=false : $scope.downLockEx=true;
	    		$scope.gridOptionsException.data = $scope.gridOptionsException.data.slice(0,20);
	    		$timeout(function(){
	    			angular.forEach($scope.gridOptionsException.data, function(value,index){
	    				if(value.class == "grid-row-new"){
	    					value.class = "";
	    				}
	    			})
	    		},1000)
	    		$scope.gridServer1.grid.refresh();
    		}
    	}else if(messageT=="biz" && $scope.pageIndexBiz==1){
    		tempData = serverFilter($scope.serverName,data);
    		if(tempData){
	    		$scope.gridOptionsBusiness.data.unshift(tempData);
	    		$scope.gridOptionsBusiness.data.length>=13 ? $scope.downLockBiz=false : $scope.downLockBiz=true;
	    		$scope.gridOptionsBusiness.data = $scope.gridOptionsBusiness.data.slice(0,13);
	    		$timeout(function(){
	    			angular.forEach($scope.gridOptionsBusiness.data, function(value,index){
	    				if(value.class == "grid-row-new"){
	    					value.class = "";
	    				}
	    			})
	    		},1000)
	    		$scope.gridServer2.grid.refresh();
    		}
    	}
    }

    function serverFilter(name,data){
    	if(data.serviceKey == name){
    		var newTime = new Date(data.time);
    		data.time = $filter('date')(newTime,'yyyy-MM-dd HH:mm:ss');
    		data.class = "grid-row-new";
    		return data
    	}else{
    		return false;
    	}
    }

    function getSeverArr(){
    	var deferred = $q.defer();
        var promise = deferred.promise;
        promise.then(function(arr){
        	$http({
	            method: "POST",
	            url: "http://" + All.Ip + "/configManage/getChildren",
	            data: {"path":"/monitor/cdh"}
			}).success(function (data) {
	            var tempArr = data.data.children;
	            for(var i in arr){
	            	arr[i].status = 0;
	            	for(var j in tempArr){
	            		if(arr[i].name == tempArr[j].name){
	            			arr[i].status = 1;
	            			break;
	            		}
	            	}
	            	for(var k in All.ServiceVirtualNodes){
	            		if(arr[i].name == All.ServiceVirtualNodes[k]){
	            			arr[i].status = 1;
	            			break;
	            		}
	            	}
	            }
	        });
        }).then(function(){
		    getExceptionData($scope.serverName,$scope.pageIndexEx);
		    getBusinessData($scope.serverName,$scope.pageIndexBiz);
        }).then(function(){
    		ServerUpDate();
        });

    	$http({
            method: "POST",
            url: "http://" + All.Ip + "/configManage/getChildren",
            data: {"path":"/com/sumscope/cdh/web/instances"}
		}).success(function (data) {
            var tempArr = data.data.value.split(",");
            for(var i=0; i<tempArr.length; i++){
            	i==0 ? $scope.serverName=tempArr[i] : null;
            	var tempObj = {"name":tempArr[i],"status":1};
            	$scope.serverArr.push(tempObj);
            }
            deferred.resolve($scope.serverArr);
        });
    }

    function getBusinessData(id, page){
    	$scope.downLockBiz = true;
    	$http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/businessInfo",
            data: {"serviceKey": id, "page": page, "size": 13}
		}).success(function (data) {
            if(data.length){
            	var total = Math.ceil(data.pop()/13);
            	$scope.pageIndexBiz!=total ? $scope.downLockBiz = false : null;
            	for(var i in data){
					var newTime = new Date(data[i].time);
					data[i].time = $filter('date')(newTime,'yyyy-MM-dd HH:mm:ss');
				}
            	$scope.BIZcurrentPage = "第" + $scope.pageIndexBiz + "/" + total + "页";
            }else{
            	$scope.BIZcurrentPage = "";
            }
            $scope.gridOptionsBusiness.data = data;
        });
    }

    function getExceptionData(id, page){
    	$scope.downLockEx = true;
    	$http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/exceptionInfo",
            data: {"serviceKey": id, "page": page, "size": 20}
		}).success(function (data) {
            if(data.length){
            	var total = Math.ceil(data.pop()/20);
            	$scope.pageIndexEx!=total ? $scope.downLockEx = false : null;
            	for(var i in data){
					var newTime = new Date(data[i].time);
					data[i].time = $filter('date')(newTime,'yyyy-MM-dd HH:mm:ss');
				}
            	$scope.EXcurrentPage = "第" + $scope.pageIndexEx + "/" + total + "页";
            }else{
            	$scope.EXcurrentPage = "";
            }
            $scope.gridOptionsException.data = data;
        });
    }

    getSeverArr();

    $scope.$on('$destroy',function(){
    	if(ws){
    		$scope.reconnect = false;
        	ws.close();
        	ws = null
    	}
    });

})
