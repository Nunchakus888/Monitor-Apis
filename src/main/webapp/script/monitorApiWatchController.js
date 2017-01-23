angular.module("appModule").
controller('monitorApiWatchController', function ($scope, $stateParams, $http, All, $interval, $timeout, utils) {
	$scope.searchBtnFlag = false;
    $scope.apiSearchFlag = false;
    $scope.currentApiName = null;
    $scope.upLock = true;
    $scope.downLock = true;
    $scope.pageIndex = 1;
    $scope.loadingFlag = false;
    $scope.searchName;
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
        $scope.getApiDetails($scope.tempApiName, $scope.pageIndex);
    }
    $scope.pageUp = function () {
        $scope.pageIndex--;
        if ($scope.pageIndex == 1) {
            $scope.upLock = true;
        }
        $scope.getApiDetails($scope.tempApiName, $scope.pageIndex);
    }
    $scope.apiSearchBtn = function () {
        $scope.searchBtnFlag = true;
        $scope.apiSearchFlag = true;
    }
    $scope.apiSearchCancel = function () {
        $scope.searchBtnFlag = false;
        $scope.apiSearchFlag = false;
    }
    $scope.apiSearchSubmit = function(apiName,mark){
    	$scope.searchName = apiName;
    	mark=="frequency" ? $scope.getApiFrequency(apiName) : $scope.getApiDelay(apiName);
    	$scope.searchBtnFlag = false;
        $scope.apiSearchFlag = false;
    }
    
    /*gridOptionsApiFrequency*/
    $scope.gridOptionsApiFrequency = {
        enableSorting: false,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: true,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'rank', displayName: '排名', width: '20%'},
            {name: 'apiName', displayName: 'API名称', width: '34%'},
            {name: 'lastVisited', displayName: '最后访问时间', width: '26%'},
            {name: 'countApi', displayName: '使用频率', width: '20%'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApiAM1 = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row, d, i) {
                $scope.pageIndex = 1;
                $scope.upLock = true;
                $scope.currentApiName = "(" + row.entity.apiName + ")";
                $scope.tempApiName = row.entity.apiName;
                $scope.getApiDetails(row.entity.apiName, $scope.pageIndex);
                $scope.isTooltipSqlContentShow = false;
            });
        }
    };

    $scope.getApiFrequency = function (apiName) {
    	$http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/apiUsageRate",
            data: {"page": 1, "size": 20, "apiName": apiName}
        }).success(function (data) {
            if (data.length) {
                for (var i = 0; i < data.length; i++) {
                    data[i].rank = i + 1;
                }
            }
            $scope.gridOptionsApiFrequency.data = data;
            countTimer();
            restartTimer();
        });
    };

    /*gridOptionsApiDelay*/
    $scope.gridOptionsApiDelay = {
        enableSorting: false,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: true,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'rank', displayName: '排名', width: '20%'},
            {name: 'apiName', displayName: 'API名称', width: '34%'},
            {name: 'lastVisited', displayName: '最后访问时间', width: '26%'},
            {name: 'takeTime', displayName: '平均延迟/ms', width: '20%'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApiAM2 = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row, d, i) {
                $scope.pageIndex = 1;
                $scope.upLock = true;
                $scope.currentApiName = "(" + row.entity.apiName + ")";
                $scope.tempApiName = row.entity.apiName;
                $scope.getApiDetails(row.entity.apiName, $scope.pageIndex);
                $scope.isTooltipSqlContentShow = false;
            });
        }
    };

    $scope.getApiDelay = function (apiName) {
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/sqlTaketime",
            data: {"page": 1, "size": 20, "apiName": apiName}
        }).success(function (data) {
            if (data.length) {
                for (var i = 0; i < data.length; i++) {
                    data[i].rank = i + 1;
                }
            }
            $scope.gridOptionsApiDelay.data = data;
            countTimer();
            restartTimer();
        });
    };

    /*gridOptionsApiDetails*/
    $scope.gridOptionsApiDetails = {
        enableSorting: true,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: false,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'userName', displayName: '用户名', width: '10%'},
            {name: 'msgDetail', displayName: 'sql语句', width: '30%'},
            {name: 'rowCount', displayName: '数据行数', width: '10%'},
            {name: 'countSql', displayName: '成功', type: 'number', width: '15%'},
            {name: 'countError', displayName: 'Error', type: 'number', width: '15%'},
            {name: 'sqlTakeTime', displayName: '平均耗时/ms', type: 'number', width: '20%'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApiAM3 = gridApi;
            gridApi.selection.on.rowSelectionChanged($scope, function (row) {
                countTimer();
                restartTimer();
                if(!row.entity.msgDetail){
                    $scope.isTooltipSqlContentShow = false;
                    return false;
                }
                utils.rowSelectionOperation($scope, row);
            });
        }
    };

    $scope.getApiDetails = function (apiName, page) {
        $scope.downLock = true;
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/apiUsagedetails",
            data: {"apiName": apiName, "page": page}
        }).success(function (data) {
            if(data.length){
	        	var total = Math.ceil(data.pop()/20);
	            $scope.pageIndex!=total ? $scope.downLock = false : null;
            	$scope.currentPage = "第" + $scope.pageIndex + "/" + total +"页";
            }else{
            	$scope.currentPage = "";
            }
            $scope.gridOptionsApiDetails.data = data;
            countTimer();
            restartTimer();
        });
    }

    var apiName = $stateParams.apiName;

    if (apiName) {
        $scope.currentApiName = "(" + $stateParams.apiName + ")";
        $scope.pageIndex = 1;
        $scope.getApiDetails($stateParams.apiName, $scope.pageIndex);
    }

    angular.element(document).ready(function () {
        if (apiName) {
            $scope.gridApiAM1.grid.rows.forEach(function (row) {
                if (row.entity.apiName == apiName + "") {
                    row.isSelected = true;
                }
            });
        }

    });

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
    	$scope.getApiFrequency($scope.searchName);
    	$scope.getApiDelay($scope.searchName);
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
    });

	$scope.getApiFrequency();
	$scope.getApiDelay();
	countTimer();
    restartTimer();
})
