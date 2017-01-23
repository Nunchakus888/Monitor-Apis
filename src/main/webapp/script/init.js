angular.module("appModule",
	["ui.router", 'ngResource', 'avalon.ui', 'DirectiveModule', 'ConstantModule',
		'ui.grid', 'ui.grid.edit', 'ui.grid.resizeColumns', 'ui.grid.pagination', 'ui.grid.selection',
		'my.ui.grid.autoResize', 'ui.grid.cellNav', 'ngResource', 'ui.bootstrap', 'ngclipboard', 'radialIndicator']).
config(function($stateProvider,$urlRouterProvider){
	$urlRouterProvider.otherwise("/login");
	$stateProvider.state('login',{
		url : "/login",
		templateUrl: "./templates/login.html",
		controller: 'loginController'
	}).state('register',{
		url : "/register",
		templateUrl: "./templates/register.html",
		controller: 'registerController'
	}).state('changePassword',{
		url : "/changePassword",
		templateUrl: "./templates/changePassword.html",
		controller: 'changePasswordController'
	}).state('findPassword',{
		url : "/findPassword",
		templateUrl: "./templates/findPassword.html",
		controller: 'findPasswordController'
	}).state("resetPassword" ,{
		url : "/resetPassword",
		templateUrl: "./templates/resetPassword.html",
		controller: 'findPasswordController',
	}).state("main" ,{
		url : "/main",
		templateUrl: "./templates/main.html",
		controller: 'mainController'
	}).state("main.monitor" ,{
		url : "/monitor",
		templateUrl: "./templates/monitor.html",
		controller: 'apiMonitorController'
	}).state("main.monitor.monitor-apiWatch",{//01
		url : "/monitor-apiWatch",
		params: {"apiName":null},
		templateUrl: "./templates/monitor-apiWatch.html",
		controller: 'monitorApiWatchController'
	}).state("main.monitor.monitor-apiWatch2",{//01
		url : "/monitor-apiWatch2",
		params: {"apiName":null},
		templateUrl: "./templates/monitor-apiWatch2.html",
		controller: 'monitorApiWatchController'
	}).state("main.monitor.monitor-userWatch",{//02
		url : "/monitor-userWatch",
		templateUrl: "./templates/monitor-userWatch.html",
		controller: 'monitorUserWatchController'
	}).state("main.monitor.monitor-errorWatch",{//03
		url : "/monitor-errorWatch",
		templateUrl: "./templates/monitor-errorWatch.html",
		controller: 'monitorErrorWatchController'
	}).state("main.apilist" ,{
		url : "/apilist",
		templateUrl: "./templates/apiList.html",
		controller: 'apiListController'
	}).state("main.userinfo" ,{
		url : "/userinfo",
		templateUrl: "./templates/userinfo.html",
		controller: 'userInfoController'
	}).state("main.server" ,{
		url : "/server",
		templateUrl: "./templates/server.html",
		controller: 'serverController'
	}).state("main.gateway" ,{
		url : "/gateway",
		templateUrl: "./templates/gateway.html",
		controller: 'gatewayController'
	}).state("main.autosys" ,{
		url : "/autosys",
//		templateUrl: "./autosys.html",
		controller: function(All){
			window.location.href = "http://" + All.Ip + "/autosys.html";
		}
	}).state("configManage" ,{
		url : "/ConfigManage",
//		templateUrl: "./ConfigManage.html",
		controller: function(All){
			window.location.href = "http://" + All.Ip + "/ConfigManage.html";
		}
	})
});
