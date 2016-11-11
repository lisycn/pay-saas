/**
 * Created by qiuwei on 2016/9/27.
 */
require("app").register.controller("BalanceQueryController", function ($scope, $timeout, $http) {
    var investStatuses = $scope.investStatuses = [{
        text: '全部', value: 'REFUNDING,BID_FAIL,INIT,PAY_SUCCESS,BID_SUCCESS,REFUND_SUCCESS'
    }, {
        text: '待支付', value: 'INIT'
    }, {
        text: '支付成功', value: 'PAY_SUCCESS'
    }, {
        text: '确认成功', value: 'BID_SUCCESS'
    }, {
        text: '已退款', value: 'REFUND_SUCCESS'
    }];
    $scope.orderStatus = investStatuses[0].value; //默认选择全部
    $scope.startDate = new Date(new Date().getTime() - 30 * 24 * 3600000);
    $scope.endDate = new Date();

    // var moment = require('moment');
    // param.startTime = moment($scope.startDate).format('x');
    // param.endTime = moment($scope.endDate).format('x');
    //Date range picker
    $('#balance_date_range_picker').daterangepicker();
});


