angular.module("appModule").
controller('monitorErrorWatchController', function ($scope, $http, All, utils, $interval, $timeout) {
    // !localStorage.password && $state.go('login');
    $scope.searchBtnFlag = false;
    $scope.errorSearchFlag = false;
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
    $scope.tempSearchJson = {
        "userName": "",
        "className": "",
        "method": "",
        "apiName": "",
        "dataSource": "",
        "page": $scope.pageIndex
    };
    $scope.pageDown = function () {
        $scope.upLock = false;
        $scope.pageIndex++;
        $scope.getErrorLog($scope.pageIndex);
        countTimer();
		restartTimer();
    }
    $scope.pageUp = function () {
        $scope.pageIndex--;
        if ($scope.pageIndex == 1) {
            $scope.upLock = true;
        }
        $scope.getErrorLog($scope.pageIndex);
        countTimer();
		restartTimer();
    }
    $scope.errorSearchBtn = function () {
        $scope.searchBtnFlag = true;
        $scope.errorSearchFlag = true;
    }
    $scope.errorSearchCancel = function () {
        $scope.errorSearchFlag = false;
        $scope.searchBtnFlag = false;
    }
    $scope.errorSearchSubmit = function (a, b, c, d, e) {
        $scope.pageIndex = 1;
        $scope.upLock = true;
        $scope.tempSearchJson = {
            "userName": a,
            "className": b,
            "method": c,
            "apiName": d,
            "dataSource": e,
            "page": $scope.pageIndex
        };
        for (var i in $scope.tempSearchJson) {
            if (!$scope.tempSearchJson[i]) {
                $scope.tempSearchJson[i] = "";
            }
        }
        $scope.getErrorLog($scope.pageIndex);
        countTimer();
    	restartTimer();
        $scope.searchBtnFlag = false;
        $scope.errorSearchFlag = false;
    }
    /*gridOptionsErrorLog*/
    $scope.gridOptionsErrorLog = {
        enableSorting: false,
        enableFullRowSelection: true,
        enableColumnMenus: false,
        enableRowSelection: true,
        enableRowHeaderSelection: false,
        multiSelect: false,
        noUnselect: false,
        enableColumnResizing: true,
        columnDefs: [
            {name: 'logId', displayName: '日志ID', width: '8%'},
            {name: 'userName', displayName: '用户名', width: '8%'},
            {name: 'method', displayName: '方法名', width: '8%'},
            {name: 'createTime', displayName: '日志时间', width: '11%'},
            {name: 'msg', displayName: '日志信息', width: '14%'},
            {name: 'sqlRowCount', displayName: '查询条数', width: '8%'},
            {name: 'msgDetail', displayName: 'sql语句', width: '10%'},
            {name: 'apiName', displayName: 'API名称', width: '9%'},
            {name: 'sqlColcount', displayName: '查询列数', width: '8%'},
            {name: 'sqlTakeTime', displayName: '耗时/ms', width: '8%'},
            {name: 'dataSource', displayName: '数据源ID', width: '8%'}
        ],
        onRegisterApi: function (gridApi) {
            $scope.gridApiAM3 = gridApi;
        }
    };
    var totalErrorPage = 1;
    $scope.getErrorLog = function (page) {
        $scope.isTooltipSqlContentShow = false;
        $scope.downLock = true;
        $scope.tempSearchJson.page = page;
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/error",
            data: $scope.tempSearchJson
        }).success(function (data) {
            if(data.length){
	        	var total = Math.ceil(data.pop()/20);
	            $scope.pageIndex!=total ? $scope.downLock = false : null;
            	$scope.currentPage = "第" + $scope.pageIndex + "/" + total +"页";
            }else{
            	$scope.currentPage = "";
            }
            $scope.gridOptionsErrorLog.data = data;
        });
    };

    angular.element(document).ready(function () {
        $scope.gridApiAM3.cellNav.raise.navigate = function (a) {
            var row = a.row,
                col = a.col,
                msg = '',
                preRow = '',
                preCol = '',
                field = col.field;
            if (field === 'className' || field === 'msgDetail' || field === 'apiName' || field === 'msg') {
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
			countTimer();
    		restartTimer();
        };
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
    	$scope.pageIndex = 1;
		$scope.getErrorLog($scope.pageIndex);
    	$scope.isTooltipSqlContentShow = false;
    	$scope.upLock = true;
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

    $scope.getErrorLog($scope.pageIndex);
	countTimer();
    restartTimer();

})
