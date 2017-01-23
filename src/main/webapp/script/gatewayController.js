angular.module("appModule").
controller('gatewayController', function ($scope, $http, All, $q, $filter) {
	$scope.serverArr = ["All"];
	$scope.serviceKey = "All";	
	$scope.date1 = new Date();
	$scope.date2 = new Date();
	
	$scope.serverNav = function(name){
		$scope.serviceKey = name;
		if(name == "All"){
			getAllData($scope.date1,$scope.date2);
		}else{
			getGatewayData($scope.date1,$scope.date2,name);
		}
	}
	
	$scope.search = function(d1,d2,key){
		if(!d2){
			d2 = new Date();
		}
		if(key == "All"){
			getAllData(d1,d2);
		}else{
			getGatewayData(d1,d2,key);
		}
	}
	
	function getSeverArr(){
    	var deferred = $q.defer();
        var promise = deferred.promise;
        promise.then(function(arr){
        	getAllData($scope.date1,$scope.date2);
        });
        
        $scope.serverArr = $scope.serverArr.concat(All.GatewayKey);
        deferred.resolve($scope.serverArr);

//  	$http({
//          method: "POST",
//          url: "http://" + All.Ip + "/configManage/getChildren",
//          data: {"path":"/com/sumscope/cdh/web/instances"}
//		}).success(function (data) {
//          var tempArr = data.data.value.split(",");
//          for(var i=0; i<tempArr.length; i++){
//          	tempArr[i].indexOf("gateway")!=-1 ? $scope.serverArr.push(tempArr[i]) : null;
//          }
//          deferred.resolve($scope.serverArr);
//      });
    }
	
	function getGatewayData(date1,date2,serviceKey){
		var date1 = $filter('date')(date1,'yyyy-MM-dd') + " 00:00:00";
		var date2 = $filter('date')(date2,'yyyy-MM-dd') + " 23:59:59";
		$http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/gateway",
            data: {"starttime":date1,"endtime":date2,"serviceKey":serviceKey}
		}).success(function (data) {
            for(var i in data.connections){
				data.connections[i].date = $filter('date')(data.connections[i].date,'yyyy-MM-dd HH:mm:ss');
			}
			for(var i in data.throughput){
				data.throughput[i].date = $filter('date')(data.throughput[i].date,'yyyy-MM-dd HH:mm:ss');
			}
			chartNum.dataProvider = data.connections;
			chartNum.validateNow();  
            chartNum.validateData();
            chartData.dataProvider = data.throughput;
			chartData.validateNow();  
            chartData.validateData();
        });
	}
	
	function getAllData(date1,date2){
//		var date1 = $filter('date')(date1,'yyyy-MM-dd HH:mm:ss');
		var date1 = $filter('date')(date1,'yyyy-MM-dd') + " 00:00:00";
		var date2 = $filter('date')(date2,'yyyy-MM-dd') + " 23:59:59";
		$http({
            method: "POST",
            url: "http://" + All.Ip + "/monitor/gateway/all",
            data: {"starttime":date1,"endtime":date2}
		}).success(function (data) {
			for(var i in data.connections){
				data.connections[i].date = $filter('date')(data.connections[i].date,'yyyy-MM-dd HH:mm:ss');
			}
			for(var i in data.throughput){
				data.throughput[i].date = $filter('date')(data.throughput[i].date,'yyyy-MM-dd HH:mm:ss');
			}
			chartNum.dataProvider = data.connections;
			chartNum.validateNow();  
            chartNum.validateData();
            chartData.dataProvider = data.throughput;
			chartData.validateNow();  
            chartData.validateData(); 

        });
	}	

	var chartNum = AmCharts.makeChart("chartNum", {
	    "type": "serial",
	    "theme": "ssChartStyle",
	    "marginRight": 40,
	    "marginLeft": 40,
	    "autoMarginOffset": 20,
	    "dataProvider": [],
	    "categoryField": "date",
	    "startDuration": 0.5,
	    "color": "#7B8082",
//	    "dataDateFormat": [{"period":"hh","format":"JJ:NN"}],
	    "valueAxes": [{
	        "axisAlpha": 0,
	        "position": "left"
	    }],
	    "balloon": {
	        "borderThickness": 1,
	        "color": "#000"
	    },
	    "graphs": [{
	        "title":"连接数",
	        "valueField": "value",
	        "bullet": "round",
	        "bulletBorderAlpha": 1,
	        "bulletColor": "#FF6600",
	        "bulletSize": 5,
	        "lineThickness": 2,
	        "lineColor": "#FF6600",
	        "negativeLineColor": "#637bb6",
	        "balloonText": "Num:[[value]]",
	        "lineAlpha":1
	    }],
	    "chartCursor": {
	        "cursorAlpha":0,
	        "cursorColor":"#258cbb",
	        "zoomable":false,
	        "cursorPosition": "mouse"
	    },
	    "categoryAxis": {
//	    	"parseDates": true,
//	    	"minPeriod":"hh",
			"labelRotation": -45,
	        "gridAlpha": 0,
	        "axisAlpha": 0,
	        "gridPosition": "start",
	        "position": "bottom"
	    },
	    "export": {
	        "enabled": true
	    },
	    "valueAxes": [{
	    	"dashLength": 5,
	    	"axisAlpha": 0,
//	    	"minimum": 0,
//	    	"maxmum":6,
	    	"integersOnly": true,
//	    	"gridCount": 10,
	    	"gridColor":"#7B8082",
	        "dashLength": 0
	    }],
	    "legend": {
	    	"useGraphSettings": true,
	    	"color": "#FF6600"
	    }
	});
	
	var chartData = AmCharts.makeChart("chartData", {
	    "type": "serial",
	    "theme": "ssChartStyle",
	    "marginRight": 40,
	    "marginLeft": 40,
	    "autoMarginOffset": 20,
	    "dataProvider": [],
	    "categoryField": "date",
	    "startDuration": 0.5,
	    "color": "#7B8082",
	    "valueAxes": [{
	        "axisAlpha": 0,
	        "position": "left"
	    }],
	    "balloon": {
	        "borderThickness": 1,
	        "color": "#000"
	    },
	    "graphs": [{
	        "title":"流量",
	        "valueField": "value",
	        "bullet": "round",
	        "bulletBorderAlpha": 1,
	        "bulletColor": "#FCD202",
	        "bulletSize": 5,
	        "lineThickness": 2,
	        "lineColor": "#FCD202",
	        "negativeLineColor": "#637bb6",
	        "balloonText": "Data:[[value]]",
	        "lineAlpha":1
	    }],
	    "chartCursor": {
	        "cursorAlpha":0,
	        "cursorColor":"#258cbb",
	        "zoomable":false,
	        "cursorPosition": "mouse"
	    },
	    "categoryAxis": {
	    	"labelRotation": -45,
	        "gridAlpha": 0,
	        "axisAlpha": 0,
	        "gridPosition": "start",
	        "position": "bottom"
	    },
	    "export": {
	        "enabled": true
	    },
	    "valueAxes": [{
	    	"dashLength": 5,
	    	"axisAlpha": 0,
//	    	"minimum": 0,
//	    	"maxmum":6,
	    	"integersOnly": true,
//	    	"gridCount": 10,
	    	"gridColor":"#7B8082",
	        "dashLength": 0
	    }],
	    "legend": {
	    	"useGraphSettings": true,
	    	"color": "#FCD202"
	    }
	});
	
	getSeverArr();
	
	

	$scope.dt = new Date();

	
	$scope.dateOptions = {
		formatYear: 'yy',
		maxDate: new Date(),
		minDate: new Date(1970,1,1),
		startingDay: 1
	};
	
	
	$scope.open1 = function() {
		$scope.popup1.opened = true;
	};
	
	$scope.open2 = function() {
		$scope.popup2.opened = true;
	};
	
	$scope.setDate = function(year, month, day) {
		$scope.dt = new Date(year, month, day);
	};
	
	$scope.popup1 = {
		opened: false
	};
	
	$scope.popup2 = {
		opened: false
	};
	
	
})