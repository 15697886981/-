app.controller("indexController", function ($scope, loginService) {

    $scope.showName = function () {
        loginService.showName().success(function (response) {
            $scope.loginName = response.username;
        });
    }
    $scope.date1 = new Date().toLocaleDateString();
    $scope.time1 = new Date().toLocaleTimeString();
});