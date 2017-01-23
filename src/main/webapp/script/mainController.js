angular.module("appModule").
controller('mainController', function ($scope, $state, $rootScope, $location) {
    $scope.$state = $state;
    $scope.input_userName = localStorage.userName;
//  $state.go("main.monitor");
    $scope.logout = function () {
        delete localStorage.password;
        $state.go("login");
    };
    
    $rootScope.$on("$stateChangeSuccess",function(event, toState, toParams, fromState, fromParams){
    	if(toState.name == "main" || toState.name == "main.monitor"){
    		$state.go("main.monitor.monitor-apiWatch");
    	}
    })
})