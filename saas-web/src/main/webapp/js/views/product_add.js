/**
 * Created by qiuwei on 2016/9/26.
 */
require("app").register.controller("pAddController", function ($rootScope,$scope, $http, $location,ROOT_URL) {

    $scope.add_product = function () {
        var name = $scope.name;
        var desc = $scope.desc;
        var merchantId = $rootScope.merchant.id;
        var url = ROOT_URL + "app/add_app";
        if(!name){
            return;
        }
        var data = {'appName':name,
            'appDesc':desc,
            'merchantId':merchantId};
        $http({
            url: url,
            method: "POST",
            params:data
            //data: data
        }).then(function (response) {
            // success
            $scope.$emit("emitSave","0");
            //$location.path('/product_manager');
        },function (error) { // optional
            // failed
            $location.path('/error');
        });
    }
});