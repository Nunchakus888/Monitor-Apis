angular.module("appModule").
controller('changePasswordController', function ($scope, $http, All, $state) {

    $scope.resetFormData = function(){
        $state.reload();
    };

    $scope.dismissToolTip = function(){
        $scope.userNameExist = '';
        $scope.passwordError = '';
    };

    $scope.RegExpTest = /^[a-zA-Z0-9]{0,16}$/;
    $scope.goToLogin = false;
    $scope.isExist = function (input_yourUserName) {
        $scope.dismissToolTip();
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/user/login",
            data: {"userName": input_yourUserName}
        }).success(function (data) {
            if (data.error) {
                $scope.userNameExist = '';
            } else {
                $scope.userNameExist = '该用户名不存在';
                return true;
            }
        });
    };
    $scope.changePassword = function (username, password, newPassword) {

        var data = {};
        if (!newPassword || !password) {
            //find password only required username & newPassword.
            data = {"userName": username, 'password': password || newPassword}
        } else {
            //all params are required.
            data = {"userName": username, 'newPassword': newPassword, 'password': password}
        }
        $http({
            method: "POST",
            url: "http://" + All.Ip + "/user/updatePassword",
            data: data
        }).success(function (data) {
            console.log(data);
            if (!data.error) {
                $scope.passwordError = ''
                $scope.goToLogin = true;
                $state.go('login');
            } else {
                $scope.passwordError = data.error;
            }
        });
    }
})
