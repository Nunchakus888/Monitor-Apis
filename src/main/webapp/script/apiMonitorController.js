angular.module("appModule").
controller('apiMonitorController', function ($scope, $state) {
    $scope.$state = $state;
//  $state.go('main.monitor.monitor-apiWatch');
})