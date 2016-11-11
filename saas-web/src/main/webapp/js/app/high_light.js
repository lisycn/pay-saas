/**
 *
 * 作者：weich
 * 邮箱：1329555958@qq.com
 * 日期：2016/3/21
 *
 * 未经作者本人同意，不允许将此文件用作其他用途。违者必究。
 *
 * @ngdoc
 * @author          weich
 * @name            Role
 * @description
 */
define(['app', 'view'], function (app, view) {

    app.controller('sidebarController', function ($rootScope,$location,$scope,$http,ROOT_URL,$state,$window) {
        $rootScope.$on('$stateChangeSuccess',
            function(event, toState, toParams, fromState, fromParams) {
                var urlParams = $location.search().appData;
                if(urlParams){
                    setActiveMenuIndex(toParams.url,urlParams)
                }else{setActiveMenu(toParams.url)};
            });
        $scope.$on("hight_light", function (d, data) {
            $('.sidebar-menu li').removeClass("active");
            $('.sidebar-menu ul').removeClass("active");
            var $am = $(".app_"+data);
            $am.parent('li').addClass('active');
            $am.parents("ul .treeview-menu").addClass('active').show();
            $am.parents("ul .treeview-menu").parent("li.treeview").addClass("active");
        });

        $scope.$on("$stateChangeStart", function (event, toState, toParams, fromState, fromParams) {
            var url = ROOT_URL+ "check_priviledge";
            var merchantId = $rootScope.merchant.id;
            var priviledge = toParams.url;
            if(merchantId && !$scope.isWhite(priviledge)){
                $http({
                    url: url,
                    method: "post",
                    params:{
                        'merchantId':merchantId,
                        'priviledge':priviledge
                    }
                }).then(function (check) {
                    if(!check.data){
                        $state.go("error");
                        event.preventDefault();//阻止模板解析
                    }
                }, function (error) { // optional
                });
            }


        });
        /**
         * 根据url 高亮菜单
         * @param url
         */
        function setActiveMenu(url) {
            $('.sidebar-menu li').removeClass("active");
            $('.sidebar-menu ul').removeClass("active");
            var $am = $(".sidebar-menu li a[href='#"+url+"']");
            $am.parent('li').addClass('active');
            $am.parents("ul .treeview-menu").addClass('active').show();
            $am.parents("ul .treeview-menu").parent("li.treeview").addClass("active");
        }

        function setActiveMenuIndex(url,urlParams) {
            var appData = JSON.parse(urlParams);
            var appId = appData.id;
            $('.sidebar-menu li').removeClass("active");
            $('.sidebar-menu ul').removeClass("active");
            var $am = $(".app_"+appId);
            $am.parent('li').addClass('active');
            $am.parents("ul .treeview-menu").addClass('active').show();
            $am.parents("ul .treeview-menu").parent("li.treeview").addClass("active");
        }

        $scope.isWhite = function(params){
            if(!params){
                return true;
            }
            var whiteArray = ["error","/error.html","/index.html"];
            var isWhite = false;
            for(var white in whiteArray){
                if(whiteArray[white] == params){
                    isWhite =  true;
                    break;
                }
            }
            return isWhite;
        }
    });
});