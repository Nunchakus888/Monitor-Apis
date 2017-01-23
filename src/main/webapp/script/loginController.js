angular.module("appModule").
controller('loginController', function ($scope, $state, $http, All) {
    
    //remember username
    function init() {
        localStorage.password && $state.go('main');
        if (localStorage.userName) {
            $scope.input_userName = localStorage.userName;
        }
    }

    $scope.dismissToolTip = function(){
        $scope.errorUsername = '';
        $scope.errorPassword = '';
    };

    $scope.RegExpTest = /^[a-zA-Z0-9]{0,100}$/;
    $scope.submitForm = function (input_userName, input_password) {
        if (input_userName && input_password) {
            $http({
                method: "POST",
                url: "http://" + All.Ip + "/user/login",
                data: {"userName": input_userName, "password": input_password}
            }).success(function (data) {
                if (!data.error) {
                    localStorage.userName = input_userName;
                    localStorage.password = input_password;
                    $state.go('main');
                } else {
                    $scope.errorUsername = data.error;
                    $scope.errorPassword = data.error;
                }
            });
        } else {
            // $scope.errorUsername = '输入不正确';
            // $scope.errorPassword = '输入不正确';
            return false;
        }
    };

    $scope.gotoRegister = function () {
        $state.go('register');
    }
    $scope.gotoChangePassword = function () {
        $state.go('changePassword');
    }
    $scope.gotoFindPassword = function () {
        $state.go('findPassword');
    }
})
