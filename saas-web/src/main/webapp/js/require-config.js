/**
 * Created by qiuwei on 2016/9/23.
 */
/**
 * Created by weichunhe on 2015/12/29.
 */
requirejs.config({
    baseUrl: 'js',
    paths: {
        jquery: 'lib/AdminLTE/plugins/jQuery/jquery-2.2.3.min',
        jquerydataTables: 'lib/datatables/media/js/jquery.dataTables.min',
        jqueryConfirm: 'lib/jquery-confirm2/dist/jquery-confirm.min',
        angular: 'lib/angular/angular.min',
        ngfileupload: 'lib/ng-file-upload/ng-file-upload-all.min',
        route: 'lib/angular-ui-router/release/angular-ui-router.min',
        lodash: 'lib/lodash/dist/lodash.min',
        bootstrap: 'lib/AdminLTE/bootstrap/js/bootstrap.min',
        datepicker: 'lib/eonasdan-bootstrap-datetimepicker/build/js/bootstrap-datetimepicker.min',
        daterangepicker: 'lib/AdminLTE/plugins/daterangepicker/daterangepicker',
        datatables: 'lib/AdminLTE/plugins/datatables/dataTables.bootstrap.min',
        moment: 'lib/moment/min/moment.min',
        slimscroll: 'lib/AdminLTE/plugins/slimScroll/jquery.slimscroll.min',
        adminlte: 'lib/AdminLTE/dist/js/app.min',
        json:'lib/json/json2',
        app: 'app/app',
        view: 'app/view',
        high_light: 'app/high_light',
        // jsonRpc: LIB + 'jquery.jsonrpc',
        //echarts: LIB + 'echarts.min'
    },
    shim: {
        angular: {
            deps: ['jquery'],
            exports: 'angular'
        },
        ngfileupload: {
            deps: ['angular']
        },
        route: {
            deps: ['angular']
        },
        bootstrap: {
            deps: ['jquery']
        },
        datepicker: {
            deps: ['jquery', 'bootstrap']
        },
        daterangepicker: {
            deps: ['jquery', 'bootstrap']
        },
        datatables: {
            deps: ['jquerydataTables']
        },
        slimscroll: {
            deps: ['jquery']
        },
        adminlte: {
            deps: ['bootstrap', 'slimscroll', 'daterangepicker']
        },
        jqueryConfirm:{
            deps: ['jquery', 'bootstrap']
        }
    }
});

require(['app', 'high_light'], function () {
    angular.element(document).ready(function () {
        angular.bootstrap(document, ['app']);
    });
});
