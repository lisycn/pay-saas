/**
 * Created by weichunhe on 2015/12/29.
 */
define([], function () {
    var config = {};

    config.views_base_path = 'view'; // 视图根目录
    config.view_suffix = '.html'; // 视图后缀
    config.index_url = '/index'; //默认首页地址 应用管理

    /*事件*/
    var events = config.EVENTS = {};
    events.ROUTER_CHANGE_SUCCESS = "routerChangeSuccessEvent";

    return config;
});