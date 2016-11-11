/**
 * Created by qiuwei on 2016/9/26.
 */
require("app").register.controller("pManagerController", function ($rootScope, $scope, $http,Upload, $location,$stateParams,$state, ROOT_URL,DataShareService) {
    var param = $location.search();
    var appData = param.appData;
    $scope.$on("broadData", function (d, data) {
        $scope.id = data.id;
        $scope.appName = data.appName;
        $scope.appDesc = data.appDesc;
        $scope.appKey = data.appKey;
        $scope.appSecret = data.appSecret;
        $("#basic_tab a").click();
        $scope.$emit("light",data.id);

        //init channel data begin
        //---- init channel ali scan begin ----
        var channelAliScanUrl = ROOT_URL + "channel/query_channel_ali_scan";
        var channelAliScanParams = {
            appInfoId:$scope.id
        };
        $http({
            url: channelAliScanUrl,
            method: "POST",
            params: channelAliScanParams
            //data: data
        }).then(function (response) {
            // success
            var channelAliDirect = response.data;
            if(channelAliDirect){
                $scope.ali_direct_pId = channelAliDirect.pId;
                $scope.ali_direct_md5 = channelAliDirect.md5;
                $scope.ali_direct_rsa_pub = channelAliDirect.rsaPub;
                $scope.ali_direct_rsa_pri = channelAliDirect.rsaPri;
                $scope.ali_direct_state = channelAliDirect.status==1;
                $scope.ali_direct_open_status= channelAliDirect.status==1;
            }else{
                $scope.ali_direct_state = false;
            }
        }, function (error) { // optional
            $scope.ali_direct_state = false;
        });
        //---- init channel ali scan end ----
        //---- init channel ali app begin ----
        var channelAliAppUrl = ROOT_URL + "channel/query_channel_ali_app";
        var channelAliAppParams = {
            appInfoId:$scope.id
        };
        $http({
            url: channelAliAppUrl,
            method: "POST",
            params: channelAliAppParams
            //data: data
        }).then(function (response) {
            // success
            var channelAliApp = response.data;
            if(channelAliApp){
                $scope.ali_app_pId = channelAliApp.pId;
                $scope.ali_app_md5 = channelAliApp.md5;
                $scope.ali_app_rsa_pub = channelAliApp.rsaPub;
                $scope.ali_app_rsa_pri = channelAliApp.rsaPri;
                $scope.ali_app_state = channelAliApp.status==1;
                $scope.ali_app_open_status= channelAliApp.status==1;
            }else{
                $scope.ali_app_state = false;
            }
        }, function (error) { // optional
            $scope.ali_app_state = false;
        });
        //---- init channel ali app end ----
        //---- init channel wx scan begin ----
        var channelWxScanUrl = ROOT_URL + "channel/query_channel_wx_scan";
        var channelWxScanParams = {
            appInfoId:$scope.id
        };
        $http({
            url: channelWxScanUrl,
            method: "POST",
            params: channelWxScanParams
            //data: data
        }).then(function (response) {
            // success
            var channelWxScan = response.data;
            if(channelWxScan){
                $scope.wx_scan_mchId = channelWxScan.mchId;
                $scope.wx_scan_appId = channelWxScan.appId ;
                $scope.wx_scan_apiKey = channelWxScan.apiKey ;
                $scope.wx_scan_apiSecretPassword = channelWxScan.apiSecretPassword ;
                $scope.wx_scan_apiSecretUrl = channelWxScan.apiSecretUrl ;
                $scope.wx_scan_state = channelWxScan.status == 1;
                $scope.wx_scan_open_status = channelWxScan.status == 1;
            }else{
                $scope.wx_scan_state = false;
            }
        }, function (error) { // optional
            $scope.wx_scan_state = false;
        });
        //---- init channel wx scan end ----

        //---- init channel wx app begin ----
        var channelWxAppUrl = ROOT_URL + "channel/query_channel_wx_app";
        var channelWxAppParams = {
            appInfoId:$scope.id
        };
        $http({
            url: channelWxAppUrl,
            method: "POST",
            params: channelWxAppParams
            //data: data
        }).then(function (response) {
            // success
            var channelAppScan = response.data;
            if(channelAppScan){
                $scope.wx_app_mchId = channelAppScan.mchId;
                $scope.wx_app_appId = channelAppScan.appId ;
                $scope.wx_app_apiKey = channelAppScan.apiKey ;
                $scope.wx_app_apiSecretPassword = channelAppScan.apiSecretPassword ;
                $scope.wx_app_apiSecretUrl = channelAppScan.apiSecretUrl ;
                $scope.wx_app_state = channelAppScan.status == 1;
                $scope.wx_app_open_status = channelAppScan.status == 1;
            }else{
                $scope.wx_app_state = false;
            }
        }, function (error) { // optional
            $scope.wx_app_state = false;
        });
        //---- init channel wx app end ----
        //---- init channel union begin ----
        var channelUnionUrl = ROOT_URL + "channel/query_channel_union";
        var channelUnionParams = {
            appInfoId:$scope.id
        };
        $http({
            url: channelUnionUrl,
            method: "POST",
            params: channelUnionParams
            //data: data
        }).then(function (response) {
            // success
            var channelUnion = response.data;
            if(channelUnion){
                $scope.union_mchId = channelUnion.merId;
                $scope.union_certPassword = channelUnion.certPassword;
                $scope.union_certUrl = channelUnion.certUrl;
                $scope.union_state = channelUnion.status == 1;
                $scope.union_open_status = channelUnion.status == 1;
            }else{
                $scope.union_state = false;
            }
        }, function (error) { // optional
            $scope.union_state = false;
        });
        //---- init channel union end ----
        //init channel data end
    });
    //var index = $rootScope.index;
    if(!appData){
        return;
    }
    var data = JSON.parse(appData);
    $scope.id = data.id;
    $scope.appName = data.appName;
    $scope.appDesc = data.appDesc;
    $scope.appKey = data.appKey;
    $scope.appSecret = data.appSecret.replace(/ /g,"+");//所有空格替换+号，解决url编码时将+号编码成空格的bug

    $scope.to_update_product = function () {
        var appName = $scope.appName;
        var appDesc = $scope.appDesc;
        var id = $scope.id;
        var url = ROOT_URL + "app/update_app";

        var data = {
            'appName': appName,
            'appDesc': appDesc,
            'id': id
        };
        $http({
            url: url,
            method: "POST",
            params: data
            //data: data
        }).then(function (response) {
            // success
            $scope.$emit("emitSave","0");
        }, function (error) { // optional
            // failed
            $location.path('/error');
        });
    };

    //tab2 init begin

    //ali direct
    $scope.ali_direct_state = true;
    $scope.aliDirectTip = "开通";
    $scope.ali_direct_toggle = function () {
        $scope.ali_direct_state = !$scope.ali_direct_state;
        var channelOpenUrl = ROOT_URL + "channel/open_channel_ali_scan";
        var openParams = {
            appInfoId:$scope.id,
            openStatus:$scope.ali_direct_state?1:0
        };
        $http({
            url: channelOpenUrl,
            method: "POST",
            params: openParams
        }).then(function (response) {
            if(response.data > 0){
                $scope.ali_direct_open_status = !$scope.ali_direct_open_status;
                if ($scope.ali_direct_state) {
                    $scope.aliDirectTip = "开通";
                } else {
                    $scope.aliDirectTip = "关闭";
                }
            }else if(response.data == 0){
                $scope.ali_direct_state = false;
                $scope.aliDirectTip = "关闭";
                $scope.alertTitle = "支付宝 扫码支付";
                $scope.alertMessage = "暂无渠道数据，请先设置后开通！";
                $('#modal_alert').modal('show');
            }
        },function(error){

        });

    };
    var channelAliScanUrl = ROOT_URL + "channel/query_channel_ali_scan";
    var channelAliScanParams = {
        appInfoId:$scope.id
    };
    $http({
        url: channelAliScanUrl,
        method: "POST",
        params: channelAliScanParams
        //data: data
    }).then(function (response) {
        // success
        var channelAliDirect = response.data;
        if(channelAliDirect){
            $scope.ali_direct_pId = channelAliDirect.pId;
            $scope.ali_direct_md5 = channelAliDirect.md5;
            $scope.ali_direct_rsa_pub = channelAliDirect.rsaPub;
            $scope.ali_direct_rsa_pri = channelAliDirect.rsaPri;
            $scope.ali_direct_state = channelAliDirect.status==1;
            $scope.ali_direct_open_status= channelAliDirect.status==1;
        }else{
            $scope.ali_direct_state = false;
        }
    }, function (error) { // optional
        $scope.ali_direct_state = false;
    });
    //ali app
    $scope.ali_app_state = true;
    $scope.aliAppTip = "开通";
    $scope.ali_app_toggle = function () {
        $scope.ali_app_state = !$scope.ali_app_state;
        var channelOpenUrl = ROOT_URL + "channel/open_channel_ali_app";
        var openParams = {
            appInfoId:$scope.id,
            openStatus:$scope.ali_app_state?1:0
        };
        $http({
            url: channelOpenUrl,
            method: "POST",
            params: openParams
        }).then(function (response) {
            if(response.data > 0){
                $scope.ali_app_open_status = !$scope.ali_app_open_status;
                if ($scope.ali_app_state) {
                    $scope.aliAppTip = "开通";
                } else {
                    $scope.aliAppTip = "关闭";
                }
            }else if(response.data == 0){
                $scope.ali_app_state = false;
                $scope.aliAppTip = "关闭";
                $scope.alertTitle = "支付宝 App支付";
                $scope.alertMessage = "暂无渠道数据，请先设置后开通！";
                $('#modal_alert').modal('show');
            }
        },function(error){

        });

    };
    var channelAliAppUrl = ROOT_URL + "channel/query_channel_ali_app";
    var channelAliAppParams = {
        appInfoId:$scope.id
    };
    $http({
        url: channelAliAppUrl,
        method: "POST",
        params: channelAliAppParams
        //data: data
    }).then(function (response) {
        // success
        var channelAliApp = response.data;
        if(channelAliApp){
            $scope.ali_app_pId = channelAliApp.pId;
            $scope.ali_app_md5 = channelAliApp.md5;
            $scope.ali_app_rsa_pub = channelAliApp.rsaPub;
            $scope.ali_app_rsa_pri = channelAliApp.rsaPri;
            $scope.ali_app_state = channelAliApp.status==1;
            $scope.ali_app_open_status= channelAliApp.status==1;
        }else{
            $scope.ali_app_state = false;
        }
    }, function (error) { // optional
        $scope.ali_app_state = false;
    });
    //wx scan
    $scope.wx_scan_state = true;
    $scope.wxScanTip = "开通";
    $scope.wx_scan_toggle = function () {
        $scope.wx_scan_state = !$scope.wx_scan_state;
        var channelOpenUrl = ROOT_URL + "channel/open_channel_wx_scan";
        var openParams = {
            appInfoId:$scope.id,
            openStatus:$scope.wx_scan_state?1:0
        };
        $http({
            url: channelOpenUrl,
            method: "POST",
            params: openParams
        }).then(function (response) {
            if(response.data > 0){
                $scope.wx_scan_open_status = !$scope.wx_scan_open_status;
                if ($scope.wx_scan_state) {
                    $scope.wxScanTip = "开通";
                } else {
                    $scope.wxScanTip = "关闭";
                }
            }else if(response.data == 0){
                $scope.wx_scan_state = false;
                $scope.wxScanTip = "关闭";
                $scope.alertTitle = "微信 扫码支付";
                $scope.alertMessage = "暂无渠道数据，请先设置后开通！";
                $('#modal_alert').modal('show');
            }
        },function(error){

        });

    };
    var channelWxScanUrl = ROOT_URL + "channel/query_channel_wx_scan";
    var channelWxScanParams = {
        appInfoId:$scope.id
    };
    $http({
        url: channelWxScanUrl,
        method: "POST",
        params: channelWxScanParams
        //data: data
    }).then(function (response) {
        // success
        var channelWxScan = response.data;
        if(channelWxScan){
            $scope.wx_scan_mchId = channelWxScan.mchId;
            $scope.wx_scan_appId = channelWxScan.appId ;
            $scope.wx_scan_apiKey = channelWxScan.apiKey ;
            $scope.wx_scan_apiSecretPassword = channelWxScan.apiSecretPassword ;
            $scope.wx_scan_apiSecretUrl = channelWxScan.apiSecretUrl ;
            $scope.wx_scan_state = channelWxScan.status == 1;
            $scope.wx_scan_open_status = channelWxScan.status == 1;
        }else{
            $scope.wx_scan_state = false;
        }
    }, function (error) { // optional
        $scope.wx_scan_state = false;
    });
    //wx app
    $scope.wx_app_state = true;
    $scope.wxAppTip = "开通";
    $scope.wx_app_toggle = function () {
        $scope.wx_app_state = !$scope.wx_app_state;
        var channelOpenUrl = ROOT_URL + "channel/open_channel_wx_app";
        var openParams = {
            appInfoId:$scope.id,
            openStatus:$scope.wx_app_state?1:0
        };
        $http({
            url: channelOpenUrl,
            method: "POST",
            params: openParams
        }).then(function (response) {
            if(response.data > 0){
                $scope.wx_app_open_status = !$scope.wx_app_open_status;
                if ($scope.wx_app_state) {
                    $scope.wxAppTip = "开通";
                } else {
                    $scope.wxAppTip = "关闭";
                }
            }else if(response.data == 0){
                $scope.wx_app_state = false;
                $scope.wxAppTip = "关闭";
                $scope.alertTitle = "微信 App支付";
                $scope.alertMessage = "暂无渠道数据，请先设置后开通！";
                $('#modal_alert').modal('show');
            }
        },function(error){

        });
    };
    var channelWxAppUrl = ROOT_URL + "channel/query_channel_wx_app";
    var channelWxAppParams = {
        appInfoId:$scope.id
    };
    $http({
        url: channelWxAppUrl,
        method: "POST",
        params: channelWxAppParams
        //data: data
    }).then(function (response) {
        // success
        var channelAppScan = response.data;
        if(channelAppScan){
            $scope.wx_app_mchId = channelAppScan.mchId;
            $scope.wx_app_appId = channelAppScan.appId ;
            $scope.wx_app_apiKey = channelAppScan.apiKey ;
            $scope.wx_app_apiSecretPassword = channelAppScan.apiSecretPassword ;
            $scope.wx_app_apiSecretUrl = channelAppScan.apiSecretUrl ;
            $scope.wx_app_state = channelAppScan.status == 1;
            $scope.wx_app_open_status = channelAppScan.status == 1;
        }else{
            $scope.wx_app_state = false;
        }
    }, function (error) { // optional
        $scope.wx_app_state = false;
    });
    //union
    $scope.union_state = true;
    $scope.unionTip = "开通";
    $scope.union_toggle = function () {
        $scope.union_state = !$scope.union_state;
        var channelOpenUrl = ROOT_URL + "channel/open_channel_union";
        var openParams = {
            appInfoId:$scope.id,
            openStatus:$scope.union_state?1:0
        };
        $http({
            url: channelOpenUrl,
            method: "POST",
            params: openParams
        }).then(function (response) {
            if(response.data > 0){
                $scope.union_open_status = !$scope.union_open_status;
                if ($scope.union_state) {
                    $scope.unionTip = "开通";
                } else {
                    $scope.unionTip = "关闭";
                }
            }else if(response.data == 0){
                $scope.union_state = false;
                $scope.unionTip = "关闭";
                $scope.alertTitle = "银联 PC网页 移动支付";
                $scope.alertMessage = "暂无渠道数据，请先设置后开通！";
                $('#modal_alert').modal('show');
            }
        },function(error){

        });
    }
    var channelUnionUrl = ROOT_URL + "channel/query_channel_union";
    var channelUnionParams = {
        appInfoId:$scope.id
    };
    $http({
        url: channelUnionUrl,
        method: "POST",
        params: channelUnionParams
        //data: data
    }).then(function (response) {
        // success
        var channelUnion = response.data;
        if(channelUnion){
            $scope.union_mchId = channelUnion.merId;
            $scope.union_certPassword = channelUnion.certPassword;
            $scope.union_certUrl = channelUnion.certUrl;
            $scope.union_state = channelUnion.status == 1;
            $scope.union_open_status = channelUnion.status == 1;
        }else{
            $scope.union_state = false;
        }
    }, function (error) { // optional
        $scope.union_state = false;
    });
    //tab2 init end

    //设置 支付宝扫码支付 开始
    $scope.add_ali_direct = function () {
        var pId = $scope.ali_direct_pId;
        var md5 = $scope.ali_direct_md5;
        var rsaPub = $scope.ali_direct_rsa_pub;
        var rsaPri = $scope.ali_direct_rsa_pri;
        var openStatus = $scope.ali_direct_open_status ? 1 : 0;
        var merchantId = $rootScope.merchant.id;
        var appInfoId = $scope.id;
        var name = "支付宝扫码支付";
        var code = "ALIPAY";
        var url = ROOT_URL + "channel/add_channel_ali";
        var data = {
            'pId': pId,
            'name': name,
            'code': code,
            'md5': md5,
            'rsaPub': rsaPub,
            'rsaPri': rsaPri,
            'merchantId': merchantId,
            'appInfoId':appInfoId,
            'status': openStatus
        };
        $http({
            url: url,
            method: "POST",
            params: data
        }).then(function (response) {
            // success
            var responseStatus = response.data.status;
            var responseData = response.data.channelAli;
            $scope.ali_direct_pId = responseData.pId;
            $scope.ali_direct_md5 = responseData.md5;
            $scope.ali_direct_rsa_pub = responseData.rsaPub;
            $scope.ali_direct_rsa_pri = responseData.rsaPri;
            $scope.ali_direct_open_status = responseData.status == 1;
            if(responseStatus == 3){
                $scope.ali_direct_state = $scope.ali_direct_open_status;
                $('#modal_ali_direct').modal('hide')
                $scope.alertTitle = "支付宝 扫码支付";
                $scope.alertMessage = "渠道设置成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 2){
                $scope.ali_direct_state = $scope.ali_direct_open_status;
                $('#modal_ali_direct').modal('hide')
                $scope.alertTitle = "支付宝 扫码支付";
                $scope.alertMessage = "渠道更新成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 1){
                $('#modal_ali_direct').modal('hide')
                $scope.alertTitle = "支付宝 扫码支付";
                $scope.alertMessage = "渠道已开通，请关闭后重试!";
                $('#modal_alert').modal('show')
            }else{
                $('#modal_ali_direct').modal('hide')
                $scope.alertTitle = "支付宝 扫码支付";
                $scope.alertMessage = "渠道设置异常，请稍后重试!";
                $('#modal_alert').modal('show')
            }
            //$location.path('/product_add');
        }, function (error) { // optional
            // failed
            $location.path('/error');
        });
    };
    //设置 支付宝扫码支付 结束

    //设置 支付宝App支付 开始
    $scope.add_ali_app = function () {
        var pId = $scope.ali_app_pId;
        var md5 = $scope.ali_app_md5;
        var rsaPub = $scope.ali_app_rsa_pub;
        var rsaPri = $scope.ali_app_rsa_pri;
        var openStatus = $scope.ali_app_open_status ? 1 : 0;
        var merchantId = $rootScope.merchant.id;
        var appInfoId = $scope.id;
        var name = "支付宝App支付";
        var code = "ALIAPPPAY";
        var url = ROOT_URL + "channel/add_channel_ali";
        var data = {
            'pId': pId,
            'name': name,
            'code': code,
            'md5': md5,
            'rsaPub': rsaPub,
            'rsaPri': rsaPri,
            'merchantId': merchantId,
            'appInfoId':appInfoId,
            'status': openStatus
        };
        $http({
            url: url,
            method: "POST",
            params: data
        }).then(function (response) {
            // success
            var responseStatus = response.data.status;
            var responseData = response.data.channelAli;
            $scope.ali_app_pId = responseData.pId;
            $scope.ali_app_md5 = responseData.md5;
            $scope.ali_app_rsa_pub = responseData.rsaPub;
            $scope.ali_app_rsa_pri = responseData.rsaPri;
            $scope.ali_app_open_status = responseData.status == 1;
            if(responseStatus == 3){
                $scope.ali_app_state = $scope.ali_app_open_status;
                $('#modal_ali_app').modal('hide')
                $scope.alertTitle = "支付宝 App支付";
                $scope.alertMessage = "渠道设置成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 2){
                $scope.ali_app_state = $scope.ali_app_open_status;
                $('#modal_ali_app').modal('hide')
                $scope.alertTitle = "支付宝 App支付";
                $scope.alertMessage = "渠道更新成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 1){
                $('#modal_ali_app').modal('hide')
                $scope.alertTitle = "支付宝 App支付";
                $scope.alertMessage = "渠道已开通，请关闭后重试!";
                $('#modal_alert').modal('show')
            }else{
                $('#modal_ali_app').modal('hide')
                $scope.alertTitle = "支付宝 App支付";
                $scope.alertMessage = "渠道设置异常，请稍后重试!";
                $('#modal_alert').modal('show')
            }
            //$location.path('/product_add');
        }, function (error) { // optional
            $('#modal_ali_app').modal('hide')
            $scope.alertTitle = "支付宝 App支付";
            $scope.alertMessage = "网络连接异常，请稍后重试!";
            $('#modal_alert').modal('show')
        });
    };
    //设置 支付宝App支付 结束

    //设置 微信扫码支付 开始
    $scope.add_wx_scan = function () {
        var mchId = $scope.wx_scan_mchId;
        var appId = $scope.wx_scan_appId;
        var apiKey = $scope.wx_scan_apiKey;
        var apiSecretPassword = $scope.wx_scan_apiSecretPassword;
        var apiSecretUrl = $scope.wx_scan_apiSecretUrl;
        var openStatus = $scope.wx_scan_open_status ? 1 : 0;
        var merchantId = $rootScope.merchant.id;
        var appInfoId = $scope.id;
        var name = "微信扫码支付";
        var code = "WXPAY";
        var url = ROOT_URL + "channel/add_channel_wx";
        var data = {
            'mchId': mchId,
            'appId': appId,
            'apiKey': apiKey,
            'apiSecretPassword': apiSecretPassword,
            'apiSecretUrl': apiSecretUrl,
            'name': name,
            'code': code,
            'merchantId': merchantId,
            'appInfoId':appInfoId,
            'status': openStatus
        };
        $http({
            url: url,
            method: "POST",
            params: data
        }).then(function (response) {
            // success
            var responseStatus = response.data.status;
            var responseData = response.data.channelWechat;
            $scope.wx_scan_mchId = responseData.mchId;
            $scope.wx_scan_appId = responseData.appId;
            $scope.wx_scan_apiKey = responseData.apiKey;
            $scope.wx_scan_apiSecretPassword = responseData.apiSecretPassword;
            $scope.wx_scan_apiSecretUrl = responseData.apiSecretUrl;
            $scope.wx_scan_open_status = responseData.status == 1;
            if(responseStatus == 3){
                $scope.wx_scan_state = $scope.wx_scan_open_status;
                $('#modal_wx_scan').modal('hide')
                $scope.alertTitle = "微信 扫码支付";
                $scope.alertMessage = "渠道设置成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 2){
                $scope.wx_scan_state = $scope.wx_scan_open_status;
                $('#modal_wx_scan').modal('hide')
                $scope.alertTitle = "微信 扫码支付";
                $scope.alertMessage = "渠道更新成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 1){
                $('#modal_wx_scan').modal('hide')
                $scope.alertTitle = "微信 扫码支付";
                $scope.alertMessage = "渠道已开通，请关闭后重试!";
                $('#modal_alert').modal('show')
            }else{
                $('#modal_wx_scan').modal('hide')
                $scope.alertTitle = "微信 扫码支付";
                $scope.alertMessage = "渠道设置异常，请稍后重试!";
                $('#modal_alert').modal('show')
            }
            //$location.path('/product_add');
        }, function (error) { // optional
            $('#modal_ali_app').modal('hide')
            $scope.alertTitle = "微信 扫码支付";
            $scope.alertMessage = "网络连接异常，请稍后重试!";
            $('#modal_alert').modal('show')
        });
    };

    $scope.uploadWxScan = function (file) {
        var url = ROOT_URL+"channel/upload_channel_cert"
        Upload.upload({
            url: url,
            data: {
                'appKey': $scope.appKey,
                'channelCode':'wx_scan'
            },
            file: file
        }).then(function (response) {
            if(response.data.status == 1){
                $("#wx_scan_progress").width("100%");
                $scope.wx_scan_apiSecretUrl = response.data.url;
            }else{
                $("#wx_scan_progress").width(0);
            }
        }, function (resp) {
            $("#wx_scan_progress").width(0);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $("#wx_scan_progress").width(progressPercentage+"%");
        });
    };
    //设置 微信扫码支付 结束

    //设置 微信App支付 开始
    $scope.add_wx_app = function () {
        var mchId = $scope.wx_app_mchId;
        var appId = $scope.wx_app_appId;
        var apiKey = $scope.wx_app_apiKey;
        var apiSecretPassword = $scope.wx_app_apiSecretPassword;
        var apiSecretUrl = $scope.wx_app_apiSecretUrl;
        var openStatus = $scope.wx_app_open_status ? 1 : 0;
        var merchantId = $rootScope.merchant.id;
        var appInfoId = $scope.id;
        var name = "微信App支付";
        var code = "WXAPPPAY";
        var url = ROOT_URL + "channel/add_channel_wx";
        var data = {
            'mchId': mchId,
            'appId': appId,
            'apiKey': apiKey,
            'apiSecretPassword': apiSecretPassword,
            'apiSecretUrl': apiSecretUrl,
            'name': name,
            'code': code,
            'merchantId': merchantId,
            'appInfoId':appInfoId,
            'status': openStatus
        };
        $http({
            url: url,
            method: "POST",
            params: data
        }).then(function (response) {
            // success
            var responseStatus = response.data.status;
            var responseData = response.data.channelWechat;
            $scope.wx_app_mchId = responseData.mchId;
            $scope.wx_app_appId = responseData.appId;
            $scope.wx_app_apiKey = responseData.apiKey;
            $scope.wx_app_apiSecretPassword = responseData.apiSecretPassword;
            $scope.wx_app_apiSecretUrl = responseData.apiSecretUrl;
            $scope.wx_app_open_status = responseData.status == 1;
            if(responseStatus == 3){
                $scope.wx_app_state = $scope.wx_app_open_status;
                $('#modal_wx_app').modal('hide')
                $scope.alertTitle = "微信 App支付";
                $scope.alertMessage = "渠道设置成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 2){
                $scope.wx_app_state = $scope.wx_app_open_status;
                $('#modal_wx_app').modal('hide')
                $scope.alertTitle = "微信 App支付";
                $scope.alertMessage = "渠道更新成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 1){
                $('#modal_wx_app').modal('hide')
                $scope.alertTitle = "微信 App支付";
                $scope.alertMessage = "渠道已开通，请关闭后重试!";
                $('#modal_alert').modal('show')
            }else{
                $('#modal_wx_app').modal('hide')
                $scope.alertTitle = "微信 App支付";
                $scope.alertMessage = "渠道设置异常，请稍后重试!";
                $('#modal_alert').modal('show')
            }
            //$location.path('/product_add');
        }, function (error) { // optional
            $('#modal_wx_app').modal('hide')
            $scope.alertTitle = "微信 App支付";
            $scope.alertMessage = "网络连接异常，请稍后重试!";
            $('#modal_alert').modal('show')
        });
    };

    $scope.uploadWxApp = function (file) {
        var url = ROOT_URL+"channel/upload_channel_cert"
        Upload.upload({
            url: url,
            data: {
                'appKey': $scope.appKey,
                'channelCode':'wx_app'
            },
            file: file
        }).then(function (response) {
            if(response.data.status == 1){
                $("#wx_app_progress").width("100%");
                $scope.wx_app_apiSecretUrl = response.data.url;
            }else{
                $("#wx_app_progress").width(0);
            }
        }, function (resp) {
            $("#wx_app_progress").width(0);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $("#wx_app_progress").width(progressPercentage+"%");
        });
    };
    //设置 微信App支付 结束

    //设置 银联支付 开始
    $scope.add_union = function () {
        var merId = $scope.union_mchId;
        var certPassword = $scope.union_certPassword;
        var certUrl = $scope.union_certUrl;
        var openStatus = $scope.union_open_status ? 1 : 0;
        var merchantId = $rootScope.merchant.id;
        var appInfoId = $scope.id;
        var name = "银联网页支付";
        var code = "UNIONPAY";
        var url = ROOT_URL + "channel/add_channel_union";
        var data = {
            'merId': merId,
            'certPassword': certPassword,
            'certUrl': certUrl,
            'name': name,
            'code': code,
            'merchantId': merchantId,
            'appInfoId':appInfoId,
            'status': openStatus
        };
        $http({
            url: url,
            method: "POST",
            params: data
        }).then(function (response) {
            // success
            var responseStatus = response.data.status;
            var responseData = response.data.channelUnion;
            $scope.union_mchId = responseData.merId;
            $scope.union_certPassword = responseData.certPassword;
            $scope.union_certUrl = responseData.certUrl;
            $scope.union_open_status = responseData.status == 1;
            if(responseStatus == 3){
                $scope.union_state = $scope.union_open_status;
                $('#modal_union').modal('hide')
                $scope.alertTitle = "银联 PC网页 移动支付";
                $scope.alertMessage = "渠道设置成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 2){
                $scope.union_state = $scope.union_open_status;
                $('#modal_union').modal('hide')
                $scope.alertTitle = "银联 PC网页 移动支付";
                $scope.alertMessage = "渠道更新成功!";
                $('#modal_alert').modal('show')
            }else if(responseStatus == 1){
                $('#modal_union').modal('hide')
                $scope.alertTitle = "银联 PC网页 移动支付";
                $scope.alertMessage = "渠道已开通，请关闭后重试!";
                $('#modal_alert').modal('show')
            }else{
                $('#modal_union').modal('hide')
                $scope.alertTitle = "银联 PC网页 移动支付";
                $scope.alertMessage = "渠道设置异常，请稍后重试!";
                $('#modal_alert').modal('show')
            }
            //$location.path('/product_add');
        }, function (error) { // optional
            $('#modal_union').modal('hide')
            $scope.alertTitle = "银联 PC网页 移动支付";
            $scope.alertMessage = "网络连接异常，请稍后重试!";
            $('#modal_alert').modal('show')
        });
    };

    $scope.uploadUnion = function (file) {
        var url = ROOT_URL+"channel/upload_channel_cert"
        Upload.upload({
            url: url,
            data: {
                'appKey': $scope.appKey,
                'channelCode':'union'
            },
            file: file
        }).then(function (response) {
            if(response.data.status == 1){
                $("#union_progress").width("100%");
                $scope.union_certUrl = response.data.url;
            }else{
                $("#union_progress").width(0);
            }
        }, function (resp) {
            $("#union_progress").width(0);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            $("#union_progress").width(progressPercentage+"%");
        });
    };
    //设置 银联支付 结束

    $scope.copyStatus = "复制 APP SECRET";
    $scope.copyToClipboard = function (elem) {
        // create hidden text element, if it doesn't already exist
        elem = document.getElementById(elem);
        var targetId = "_hiddenCopyText_";
        var isInput = elem.tagName === "INPUT" || elem.tagName === "TEXTAREA";
        var origSelectionStart, origSelectionEnd;
        if (isInput) {
            // can just use the original source element for the selection and copy
            target = elem;
            origSelectionStart = elem.selectionStart;
            origSelectionEnd = elem.selectionEnd;
        } else {
            // must use a temporary form element for the selection and copy
            target = document.getElementById(targetId);
            if (!target) {
                var target = document.createElement("textarea");
                target.style.position = "absolute";
                target.style.left = "-9999px";
                target.style.top = "0";
                target.id = targetId;
                document.body.appendChild(target);
            }
            target.textContent = elem.textContent;
        }
        // select the content
        var currentFocus = document.activeElement;
        target.focus();
        target.setSelectionRange(0, target.value.length);

        // copy the selection
        var succeed;
        try {
            succeed = document.execCommand("copy");
            $scope.copyStatus = "已复制";
        } catch(e) {
            succeed = false;
        }
        // restore original focus
        if (currentFocus && typeof currentFocus.focus === "function") {
            currentFocus.focus();
        }

        if (isInput) {
            // restore prior selection
            elem.setSelectionRange(origSelectionStart, origSelectionEnd);
        } else {
            // clear temporary content
            target.textContent = "";
        }
        return succeed;
    };

    $scope.btnCopyMouseover = function () {
        $scope.copyStatus = "复制 APP SECRET";
    };
});