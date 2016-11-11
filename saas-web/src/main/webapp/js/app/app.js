/**
 * Created by weichunhe on 2015/12/29.
 */
define('app', ['view', 'route', 'bootstrap', 'datepicker', 'adminlte', 'jquerydataTables', 'ngfileupload', 'json', 'jqueryConfirm'], function (view) {
    var app = angular.module('app', ['ui.router', 'ngFileUpload']);
    angular.CFG = view; //保存配置数据
    app.constant('VIEWS_BASE_PATH', view.views_base_path);
    app.constant("ROOT_URL", "/saas-web/");
    app.config(function ($stateProvider, VIEWS_BASE_PATH, $controllerProvider, $filterProvider, $urlRouterProvider, $provide,$httpProvider) {

        app.register = {
            controller: $controllerProvider.register,
            filter: $filterProvider.register,
            factory: $provide.factory
        };

        //路由配置使用
        function resolve($q, url, deps) {
            var def = $q.defer();
            require(deps, function () {
                def.resolve();
            }, function () {
                def.resolve();
                console.warn(url + '没有对应的js依赖!');
            });
            return def.promise;
        }

        //处理url,添加后缀
        var suffix = view.view_suffix;

        function addSuffix(url) {
            if (url.indexOf('.') !== -1) {
                return url;
            }
            var index = url.indexOf('?');
            if (index === -1) {
                return url + suffix;
            } else {
                return url.substring(0, index) + suffix + url.substring(index);
            }
        }

        /**
         * 根据一定的规则取出依赖
         * abc/def/hg.html 以hg为依赖
         * @param url
         */
        function getDeps(url) {
            var dep = url;
            if (dep) {
                if (dep.indexOf('/') === 0) {
                    dep = dep.substring(1);
                }
                dep = dep.split(/[.\?]/)[0];
            }
            dep = "views/" + dep;
            return [dep];
        }

        $urlRouterProvider.when(/^\/?$/, view.index_url);

        $stateProvider
        //默认规则配置
            .state('def', {
                    url: '{url:[^@]*}',
                    templateUrl: function ($stateParams) {
                        var url = VIEWS_BASE_PATH + $stateParams.url;
                        return addSuffix(url);
                    },
                    resolve: { //预载入功能
                        require: function ($q, $stateParams) {
                            return resolve($q, $stateParams.url, getDeps($stateParams.url));
                        }
                    }
                }
            )
            .state('error',{
                url:'error.html',
                templateUrl:'view/error.html'
            })
        ;

        //$httpProvider.interceptors.push("httpInterceptor");
    });


    var appList = null;
    app.controller("bodyController", function ($rootScope, $location, $http, $scope, $state, $stateParams, ROOT_URL, $timeout) {
        /*请求用户是否登录，并查询出用户数据*/
        $scope.get_user_session = function () {
            var url = ROOT_URL + "get_user_session";
            $http({
                url: url,
                method: "get"
            }).then(function (merchant_response) {
                // success
                if (!merchant_response.data) {//如果用户数据为空，session失效
                    $('#modal_login').show();
                    return false;
                }
                $rootScope.merchant = merchant_response.data;
                /*请求用户下应用信息*/
                $scope.query_all_app(merchant_response.data);
                $scope.get_merchant_role(merchant_response.data.id);

            }, function (error) { // optional
                // failed
            });
        };

        $scope.to_product_manager = function (appData, index) {
            //$rootScope.index = index;
            //$rootScope.appParams = appParams;
            $('.sidebar-menu li').removeClass("active");
            $('.sidebar-menu ul').removeClass("active");
            var $am = $(".app_" + appData.id);
            $am.parent('li').addClass('active');
            $am.parents("ul .treeview-menu").addClass('active').show();
            $am.parents("ul .treeview-menu").parent("li.treeview").addClass("active");
            $scope.$broadcast("broadData", appData);
        }

        $scope.to_merchant_login = function () {
            var email = $scope.login_email;
            var pwd = $scope.login_password;
            var validationCode = $scope.login_verify_code;
            var url = ROOT_URL + "logon_from";

            var msg = $("#msg");
            msg.html();
            if (!email) {
                msg.html('<span class="text-red">邮箱不能为空!</span>');
                return false;
            }
            if (!pwd) {
                msg.html('<span class="text-red">密码不能为空!</span>');
                return false;
            }
            $http({
                url: url,
                method: "post",
                params: {
                    'email': email,
                    'pwd': pwd,
                    'validationCode': validationCode
                }
            }).then(function (login_response) {
                var returnCode = login_response.data.returnCode;
                var returnMsg = login_response.data.returnMsg;
                var loginNum = login_response.data.loginNum;
                if (loginNum >= 3) {
                    $("#verifycode").show();
                    if (!validationCode) {
                        msg.html('<span class="text-red">验证码不能为空!</span>');
                        return;
                    }
                }
                if ('FAIL' == returnCode) {
                    $("#msg").html('<span class="text-red">' + returnMsg + '</span>')
                    if (loginNum >= 3) {
                        $("#verifycode").show();
                    }
                    return;
                }
                var merchant = login_response.data.merchant;
                if (merchant) {
                    $('#modal_login').modal('hide');
                    $rootScope.merchant = merchant;
                    /*请求用户下应用信息*/
                    $scope.query_all_app(merchant);

                    $scope.get_merchant_role(merchant.id);
                }
            }, function (error) {

            });


        };

        $scope.to_verify_code = function () {
            now = new Date();
            $(".validationCode_img").attr("src", "./create_ver_code?code=" + now.getTime());
        }

        $scope.$on("emitSave", function (d) {
            /*请求用户下应用信息*/
            var url = ROOT_URL + "app/query_all_app";
            var merchantId = $rootScope.merchant.id;
            $http({
                url: url,
                method: "post",
                params: {'merchantId': merchantId}
            }).then(function (app_response) {
                // success
                $scope.appList = app_response.data;
                appList = app_response.data;
                var appData = JSON.stringify(appList[0]);
                $(".product_add").hide();
                $timeout(function () {
                    $location.url("product_manager?appData=" + appData);
                    //$scope.$broadcast("broadData", appList[0]);
                }, 300);

            }, function (error) { // optional
                // failed
            });
        })

        $scope.$on("light", function (d, data) {
            $scope.$broadcast("hight_light", data);
            $('.sidebar-menu li').removeClass("active");
            $('.sidebar-menu ul').removeClass("active");
            var $am = $(".app_" + data);
            $am.parent('li').addClass('active');
            $am.parents("ul .treeview-menu").addClass('active').show();
            $am.parents("ul .treeview-menu").parent("li.treeview").addClass("active");
        });

        $scope.get_merchant_role = function (merchantId) {
            var url = ROOT_URL + "get_merchant_role";
            $http({
                url: url,
                method: "post",
                params: {'merchantId': merchantId}
            }).then(function (roleResponse) {
                // success
                $scope.role = roleResponse.data;
            }, function (error) { // optional
                // failed
            });
        };

        $scope.query_all_app = function (merchant) {
            var url = ROOT_URL + "app/query_all_app";
            var merchantId = merchant.id;
            $http({
                url: url,
                method: "post",
                params: {'merchantId': merchantId}
            }).then(function (app_response) {
                // success
                $scope.appList = app_response.data;

                appList = app_response.data;
                $('#modal_login').hide();
                if (appList) {
                    var appData = JSON.stringify(appList[0]);
                    //$state.go('/product_manager', {appData: appIndex});
                    //$state.go("product_manager?appData="+appIndex)

                    $(".product_add").hide();
                    $timeout(function () {
                        $scope.$broadcast("broadData", appList[0]);
                        //$location.url("product_manager?appData=" + appData);
                    }, 300);
                    $location.path("index");
                } else {
                    $location.path("index");
                }
            }, function (error) { // optional
                // failed
            });
        };

        //执行查询用户数据
        $scope.get_user_session();

    });

    //数据共享service 所有应用列表数据
    app.factory("DataShareService", function () {
        return {
            "appList": appList
        };
    });

    //app.factory("httpInterceptor", ["$q", "$rootScope", function ($q, $rootScope) {
    //    return {
    //        request: function (config) {
    //            // do something on request success
    //            return config || $q.when(config);
    //        },
    //        requestError: function (rejection) {
    //            // do something on request error
    //            return $q.reject(rejection)
    //        },
    //        response: function (response) {
    //            // do something on response success
    //            //var merchant = $rootScope.merchant;
    //            //var state = $rootScope.$state.current.name;
    //            //console.log("merchant : " + merchant.id);
    //            return response || $q.when(response);
    //        },
    //        responseError: function (rejection) {
    //            var state = $rootScope.$state.current.name;
    //            console.log("state : " + state);
    //            // do something on response error
    //            return $q.reject(rejection);
    //        }
    //    };
    //}]);
    return app;
});