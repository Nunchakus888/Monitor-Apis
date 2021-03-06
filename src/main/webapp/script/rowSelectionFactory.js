angular.module("appModule").factory("utils", function () {
    this.preRow = undefined;
    this.rowSelectionOperation = function ($scope, row, isPreCol, msg, is1st) {
        if (row === this.preRow) {
            if (isPreCol) {
                $scope.tooltipSqlContent = msg;
                $scope.isTooltipSqlContentShow = true;
            } else {
                $scope.isTooltipSqlContentShow = !$scope.isTooltipSqlContentShow;
            }
        } else {
            if (is1st) {
                $scope.tooltipSqlContent = msg;
            } else {
                $scope.tooltipSqlContent = row.entity.msgDetail;
                this.preRow = row;
            }
            $scope.isTooltipSqlContentShow = true;
        }
        $scope.$apply();
    };
    return this;
});

angular.module("appModule").service("websocketService",function(){
	return {
		generateMixed : function(n){
			var chars = ['0','1','2','3','4','5','6','7','8','9',
			    'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
			
			var res = "";
		    for(var i = 0; i < n ; i ++) {
		        var id = Math.ceil(Math.random()*61);
		        res += chars[id];
		    }
		    return res;
		}
	}
});