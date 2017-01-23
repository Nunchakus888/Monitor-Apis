angular.module("appModule").
controller('monitorUserWatchController', function ($scope, $state, $http, All, $interval, $timeout, utils) {
    $scope.upLock = true;
    $scope.downLock = true;
    $scope.pageIndex = 1;
    $scope.loadingFlag = false;
    var timer;
    var timerDown;
    $scope.refreshNow = function(){
    	$interval.cancel(timer);
    	$interval.cancel(timerDown);
    	$scope.loadingFlag = true;
    	$timeout(function(){
	    	successTimer();
	    	countTimer();
	        restartTimer();
    	},1000)
    }
    $scope.pageDown = function () {
        $scope.upLock = false;
        $scope.pageIndex++;
        $scope.getUserDelay($scope.tempUserName, $scope.pageIndex);

    }
    $scope.pageUp = function () {
        $scope.pageIndex--;
        if ($scope.pageIndex == 1) {
            $scope.upLock = true;
        }
        $scope.getUserDelay($scope.tempUserName, $scope.pageIndex);
    }
    /*gridOptionsUserFrequency*/
    $scope.gridOptionsUserFrequency = {
        enableSorting: false,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: true,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'rank', displayName: '排名', width: '15%'},
            {name: 'userName', displayName: '用户名称', width: '30%'},
            {name: 'lastVisited', displayName: '最后访问时间', width: '25%'},
            {name: 'apiUsageRage', displayName: '用户访问次数', width: '30%'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApiUM1 = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row, d, i) {
                $scope.pageIndex = 1;
                $scope.upLock = true;
                $scope.tempUserName = row.entity.userName;
                $scope.getUserDelay(row.entity.userName, $scope.pageIndex);
                countTimer();
                restartTimer();
                $scope.isTooltipSqlContentShow = false;
            });
        }
    };
    $scope.getApiFrequency = function () {
        $http.get("http://" + All.Ip + "/monitor/userUsageApiRate")
            .success(function (data) {
                if (data.length < 20) {
                    for (var i = 0; i < data.length; i++) {
                        data[i].rank = i + 1;
                    }
                    $scope.gridOptionsUserFrequency.data = data;
                } else {
                    var temp = [];
                    for (var i = 0; i < 20; i++) {
                        data[i].rank = i + 1;
                        temp.push(data[i]);
                    }
                    $scope.gridOptionsUserFrequency.data = temp;
                }
            });
    }

    /*gridOptionsUserDelay*/
    $scope.gridOptionsUserDelay = {
        enableSorting: true,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: false,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'apiName', displayName: 'API名称', width: '40%'},
            {name: 'countSql', displayName: 'SQL执行成功', type: 'number', width: '20%'},
            {name: 'countError', displayName: 'SQL Error', type: 'number', width: '20%'},
            {name: 'apiRate', displayName: 'API访问次数', type: 'number', width: '20%'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApiAM2 = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row, d, i) {
                if(row.entity.apiName){
                    $state.go("main.monitor.monitor-apiWatch", {apiName: row.entity.apiName});
                }else {
                    row.entity.msgDetail =
                        "错误原因:" +
                        "1.用户登陆接口，用户名密码错误，body不涉及api名称，所以没有apiname信息" +
                        "2.用户查询返回数量接口（getrowcount），涉及到无效版本API问题或者登陆用户不存在等问题,不返回apiname信息" +
                        "3.用户查询明细接口（runapi），涉及到无效版本API问题或者登陆用户不存在问题,不返回apiname信息";
                    utils.rowSelectionOperation($scope, row);
                }
            });
        }
    };

    $scope.getUserDelay = function (user, page) {
        $scope.downLock = true;
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/aUserUsageApiList",
            data: {"userName": user, "page": page}
        }).success(function (data) {
        	if(data.length){
	        	var total = Math.ceil(data.pop()/20);
	            $scope.pageIndex!=total ? $scope.downLock = false : null;
            	$scope.currentPage = "第" + $scope.pageIndex + "/" + total +"页";
            }else{
            	$scope.currentPage = "";
            }
            $scope.gridOptionsUserDelay.data = data;
            countTimer();
            restartTimer();
        });
    }

    function restartTimer(){
        $interval.cancel(timer);
        timer = $interval(countTimer,16000);
    }
    function countTimer(){
    	$scope.loadingFlag = false;
    	$interval.cancel(timerDown);
    	$scope.indicatorValue = 150;
    	timerDown = $interval(function(){
	    	$scope.indicatorValue = $scope.indicatorValue - 1;
	    	if($scope.indicatorValue <= 0){
	    		$scope.loadingFlag = true;
	    	}else{
	    		$scope.loadingFlag = false;
	    	}
		},100,150)
    	timerDown.then(successTimer);
    }
	function successTimer(){
		$scope.getApiFrequency();
//  	$scope.pageIndex = 1;
//  	$scope.currentPage = "";
//  	$scope.upLock = true;
	}

	$scope.indicatorOption = {
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
    };

    $scope.$on('$destroy',function(){
       $interval.cancel(timer);
       $interval.cancel(timerDown);
    })

    $scope.getApiFrequency();
	countTimer();
    restartTimer();

})
