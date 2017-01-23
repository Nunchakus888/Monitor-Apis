angular.module("appModule").
controller('registerController', function ($scope, $state, $http, All) {

    $scope.RegExpEmail = /^([\w-\.*]+@([\w-]+\.)+[\w-]{2,4})?$/;
    $scope.RegExpTest = /^[a-zA-Z0-9]{4,16}$/;
    $scope.isExist = function (register_username) {
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/user/login",
            data: {"userName": register_username}//required & only
        }).success(function (data) {
            if (!data.error) {
                $scope.userNameExist = '';
            } else {
                $scope.userNameExist = data.error;
            }
        });
    };

    $scope.goToLogin = false;
    $scope.register = function (register_username, register_password, register_email, isAdministration) {
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/user/register",
            data: {"userName": register_username, "password": register_password, "email": register_email, "status":isAdministration ? 1 : 0}
        }).success(function (data) {
            if (!data.error) {
                $scope.goToLogin = true;
                setTimeout(function () {
                    $scope.goToLogin = true;
                    $state.go('login');
                }, 1000);
            }
        });
    }
})
