angular.module("appModule").
controller('findPasswordController', function ($scope, $http, All, $state, $location) {
    $scope.goToLogin = false;

    $scope.dismissToolTip = function(){
        $scope.userNameExist = '';
        $scope.isMatch = '';
    };

    $scope.RegExpTest = /^[a-zA-Z0-9]{0,166}$/;
    $scope.RegExpEmail = /^([\w-\.*]+@([\w-]+\.)+[\w-]{2,4})?$/;

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
            }
        });
    };

    $scope.findPassword = function (userName, email, tag) {
        $scope.signUpForm.$invalid && $scope.dismissToolTip();
        if($scope.signUpForm.$invalid) return false;
            var reqData = {};
        if(tag){
            reqData = {"userName":userName, "email":email, "tag":tag};
        }else{
            reqData = {"userName":userName, "email":email};
            $scope.stopTiwnceReq = true;
        }

        if(userName && email){
            $http({
                method: "POST",
                url: "http://" + All.Ip + "/user/findPassword",
                data: reqData
            }).success(function (data) {
                console.log(data);
                if (!data.error) {
                    if(tag){
                        $scope.goToLogin = false;
                        $scope.regExpName = false;
                        $scope.isMatch = data.error;
                        $scope.stopTiwnceReq = false;
                    }else {
                        $scope.goToLogin = true;
                        setTimeout(function () {
                            $state.go('login');
                        }, 500);
                    }

                }else {
                    $scope.goToLogin = false;
                    $scope.regExpName = true;
                    $scope.isMatch = data.error;
                    $scope.stopTiwnceReq = true;
                }
            });
        }
    };

    $scope.resetPassword = function (password) {
        if(password){
            $http({
                method: "POST",
                url: "http://" + All.Ip + "/user/updatePassword",
                data: {"userName":$location.search().userName, "password":password}
            }).success(function (data) {
                console.log(data);
                if (!data.error) {
                    $scope.goToLogin = true;
                    setTimeout(function () {
                        $state.go('login');
                    }, 1000);
                }else {
                    alert(data.error);
                }
            });
        }
    }
})
