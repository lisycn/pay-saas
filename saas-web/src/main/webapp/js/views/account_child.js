/**
 * Created by qiuwei on 2016/10/24.
 */
require("app").register.controller("accountChildController", function ($rootScope, $scope, $timeout, $http, $location, ROOT_URL) {

    //初始化数据
    $scope.init = function () {
        $scope.childList = {};
        $scope.childFlag = false;
        $scope.roleList = [{
            id: undefined,
            roleName: "未知"
        }];
        $scope.parentId = $rootScope.merchant.parentId ? $rootScope.merchant.parentId : $rootScope.merchant.id;
    };

    //获取所有子账户
    $scope.get_child_merchant = function () {
        var parentId = $scope.parentId;
        var url = ROOT_URL + "get_child_merchant";

        $http({
            url: url,
            method: "post",
            params: {
                'parentId': parentId
            }
        }).then(function (response) {
            // success
            if (response.data) {//
                $scope.childFlag = true;
                $scope.childList = response.data;
            }
        }, function (error) {
        });
    };

    //获取所有角色
    $scope.get_roles = function () {
        var url = ROOT_URL + "get_roles";
        $http({
            url: url,
            method: "post"
        }).then(function (response) {
            // success
            if (response.data) {//
                $scope.roleList = response.data;
                $scope.select_default.add_select_default();
            }
        }, function (error) {
        });
    };

    //添加按钮执行函数
    $scope.add_child = function () {
        $scope.remove_edit_area();
        $("#btn_add").addClass("disabled");
        $("#child_left").removeClass("col-xs-12").addClass("col-xs-10");
        $timeout(function () {
            $("#child_right").removeClass("hidden").addClass("col-xs-2");
        }, 300);

    };

    //子账户设置区域 删除按钮执行函数
    $scope.remove_child = function () {
        $("#btn_add").removeClass("disabled");
        $("#child_left").removeClass("col-xs-10").addClass("col-xs-12");
        $("#child_right").removeClass("col-xs-2").addClass("hidden");
    };

    //子账户编辑区域 删除按钮执行函数
    $scope.remove_edit_area = function () {
        $("#child_left").removeClass("col-xs-10").addClass("col-xs-12");
        $("#child_down").removeClass("col-xs-2").addClass("hidden");
    };

    //编辑按钮执行函数
    $scope.display_edit_area = function (child) {
        $scope.remove_child();
        $scope.child = child;
        $scope.roleEditList = $scope.roleList;
        $scope.select_default.edit_select_default(child);
        $("#child_left").removeClass("col-xs-12").addClass("col-xs-10");
        $timeout(function () {
            $("#child_down").removeClass("hidden").addClass("col-xs-2");
        }, 300);
    };

    //编辑 选择下拉框默认选中
    $scope.select_default = {
        //添加区域
        add_select_default: function () {
            $scope.selectedRole = $scope.roleList[0];
        },
        //编辑区域
        edit_select_default: function (child) {
            var selected = child.merchantRoleList[0];
            for (var index = 0; index < $scope.roleEditList.length; index++) {
                var role = $scope.roleEditList[index];
                if (role.roleCode == selected.roleCode) {
                    $scope.editedRole = role;
                    break;
                }
            }
        }
    };

    //子账户编辑区域 确认
    $scope.execute_edit_child = function () {
        $scope.child.merchantRoleList[0] = $scope.editedRole;
        var url = ROOT_URL + "edit_child_merchant";
        var child = $scope.child;
        var roleList = child.merchantRoleList;
        var roleIdArray = $scope.to_Role_List(roleList);
        $http({
            url: url,
            method: "post",
            params: {
                'email': child.email,
                'roleIdArray': roleIdArray,
                'parentId': child.parentId,
                'id': child.id
            }
        }).then(function (response) {
            // success
            if (response.data && response.data.success == true) {//
                $scope.childFlag = true;
                $scope.childList = response.data.data;
                $scope.alert.editSuccess(response.data.data);
                $scope.remove_edit_area();
            } else {
                $scope.alert.editError();
                $scope.remove_edit_area();
            }
        }, function (error) {
            $scope.alert.editError();
            $scope.remove_edit_area();
        });
    };

    $scope.to_Role_List = function (roleList) {
        var roleArray = new Array();
        for (var index = 0; index < roleList.length; index++) {
            roleArray.push(roleList[index].id);
        }
        return roleArray;
    };


    //子账户设置区域 确认
    $scope.execute_add_child = function () {
        var email = $scope.email;
        var roleId = $scope.selectedRole.id;
        var parentId = $scope.parentId;
        var url = ROOT_URL + "add_child_merchant";
        if(!$scope.check_email(email)){
            return;
        }
        $http({
            url: url,
            method: "post",
            params: {
                'email': email,
                'roleId': roleId,
                'parentId': parentId
            }
        }).then(function (response) {
            // success
            if (response.data && response.data.success == true) {//
                $scope.childFlag = true;
                $scope.childList = response.data.data;
                $scope.alert.addSuccess(response.data.data);
                $scope.remove_child();
            } else {
                $scope.alert.addError(response.data.code);
                $scope.remove_child();
            }
        }, function (error) {
            $scope.alert.addError();
            $scope.remove_child();
        });
    };


    $scope.check_email = function (email) {
        $("#msg").html("");
        if(!email){
            $("#msg").html("<span style='color: red; '>邮箱不能为空!</span>");
            return false;
        }
        var pattern = /^[a-zA-Z0-9]*@[a-zA-Z0-9]*.[a-z]*$/;
        if(!pattern.test(email)){
            $("#msg").html("<span style='color: red; '>邮箱格式非法!</span>");
            return false;
        }
        return true;
    };

    //删除操作 执行函数
    $scope.delete_child = function (childId) {
        $scope.remove_child();
        $.alert({
            title: $scope.alertContent.deleteTitle,
            content: $scope.alertContent.deleteContentTip,
            confirmButton: $scope.alertContent.confirmButton,
            closeIcon: $scope.alertContent.closeIcon,
            icon: $scope.alertContent.icon,
            theme: $scope.alertContent.theme,
            animationSpeed: $scope.alertContent.animationSpeed,
            animation: $scope.alertContent.animation,
            closeAnimation: $scope.alertContent.closeAnimation,
            confirm: function () {
                var url = ROOT_URL + "delete_child_merchant";
                var parentId = $scope.parentId;
                $http({
                    url: url,
                    method: "post",
                    params: {
                        'parentId': parentId,
                        'childId': childId
                    }
                }).then(function (response) {
                    // success
                    if (response.data && response.data.success == true) {//
                        if (response.data.data) {
                            $scope.childFlag = true;
                        } else {
                            $scope.childFlag = false;
                        }
                        $scope.childList = response.data.data;
                        $scope.alert.deleteSuccess();
                    } else {
                        $scope.alert.deleteError();
                    }
                }, function (error) {
                    $scope.alert.deleteError();
                });
            }
        });
    };

    //启用子账户
    $scope.enable_using_status = function (child) {
        $scope.using_status_flag = true;
        $scope.update_using_status(child);
    };

    //停用子账户
    $scope.unable_using_status = function (child) {
        $scope.using_status_flag = false;
        $scope.update_using_status(child);
    };

    $scope.update_using_status = function(child) {
        $.alert({
            title: $scope.using_status_flag?$scope.alertContent.enableTitle:$scope.alertContent.unableTitle,
            content: $scope.using_status_flag?$scope.alertContent.enableContentTip:$scope.alertContent.unableContentTip,
            confirmButton: $scope.alertContent.confirmButton,
            closeIcon: $scope.alertContent.closeIcon,
            icon: $scope.alertContent.icon,
            theme: $scope.alertContent.theme,
            animationSpeed: $scope.alertContent.animationSpeed,
            animation: $scope.alertContent.animation,
            closeAnimation: $scope.alertContent.closeAnimation,
            confirm: function () {
                var url = $scope.using_status_flag?ROOT_URL + "enable_child_using_status":ROOT_URL + "unable_child_using_status";
                var childId = child.id;
                var parentId = child.parentId;
                $http({
                    url: url,
                    method: "post",
                    params: {
                        'childId': childId,
                        'parentId':parentId
                    }
                }).then(function (response) {
                    // success
                    if (response.data && response.data.success == true) {//
                        if (response.data.data) {
                            $scope.childFlag = true;
                        } else {
                            $scope.childFlag = false;
                        }
                        $scope.childList = response.data.data;
                        $scope.using_status_flag?$scope.alert.enableSuccess():$scope.alert.unableSuccess();
                    } else {
                        $scope.using_status_flag?$scope.alert.enableError():$scope.alert.unableError();
                    }
                }, function (error) {
                    $scope.using_status_flag?$scope.alert.enableError():$scope.alert.unableError();
                });
            }
        });
    };

    $scope.alert = {
        addError: function (code) {
            var addContentError = $scope.alertContent.addContentError;
            if (code) {
                addContentError = '<div class="test-center">' + code + '</div>';
            }
            $.alert({
                title: $scope.alertContent.addTitle,
                content: addContentError,
                confirmButton: $scope.alertContent.confirmButton,
                icon: $scope.alertContent.icon,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        addSuccess: function () {
            $.alert({
                title: $scope.alertContent.addTitle,
                content: $scope.alertContent.addContentSuccess(),
                confirmButton: $scope.alertContent.confirmButton,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        editError: function () {
            $.alert({
                title: $scope.alertContent.editTitle,
                content: $scope.alertContent.editContentError,
                confirmButton: $scope.alertContent.confirmButton,
                icon: $scope.alertContent.icon,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        editSuccess: function () {
            $.alert({
                title: $scope.alertContent.editTitle,
                content: $scope.alertContent.editContentSuccess,
                confirmButton: $scope.alertContent.confirmButton,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        deleteError: function () {
            $.alert({
                title: $scope.alertContent.editTitle,
                content: $scope.alertContent.editContentError,
                confirmButton: $scope.alertContent.confirmButton,
                icon: $scope.alertContent.icon,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        deleteSuccess: function () {
            $.alert({
                title: $scope.alertContent.deleteTitle,
                content: $scope.alertContent.deleteContentSuccess,
                confirmButton: $scope.alertContent.confirmButton,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        enableError: function () {
            $.alert({
                title: $scope.alertContent.enableTitle,
                content: $scope.alertContent.enableContentError,
                confirmButton: $scope.alertContent.confirmButton,
                icon: $scope.alertContent.icon,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        enableSuccess: function () {
            $.alert({
                title: $scope.alertContent.enableTitle,
                content: $scope.alertContent.enableContentSuccess,
                confirmButton: $scope.alertContent.confirmButton,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        unableError: function () {
            $.alert({
                title: $scope.alertContent.unableTitle,
                content: $scope.alertContent.unableContentError,
                confirmButton: $scope.alertContent.confirmButton,
                icon: $scope.alertContent.icon,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        },
        unableSuccess: function () {
            $.alert({
                title: $scope.alertContent.unableTitle,
                content: $scope.alertContent.unableContentSuccess,
                confirmButton: $scope.alertContent.confirmButton,
                theme: $scope.alertContent.theme,
                animationSpeed: $scope.alertContent.animationSpeed,
                animation: $scope.alertContent.animation,
                closeAnimation: $scope.alertContent.closeAnimation
            });
        }
    };

    $scope.alertContent = {
        addTitle: '设置子账户',
        addContentSuccess: function(){
            return '<div class="test-center">设置子账户成功，激活邮件已发送至'+$scope.email+'</div>'
        },
        addContentError: '<div class="test-center">设置子账户失败，请稍后重试！</div>',
        editTitle: '更新子账户',
        editContentSuccess: '<div class="test-center">更新子账户成功</div>',
        editContentError: '<div class="test-center">更新子账户失败，请稍后重试！</div>',
        deleteContentTip: '<div class="test-center">账户删除之后无法恢复，您确认要删除此账户？</div>',
        deleteContentSuccess: '<div class="test-center">账户删除成功！</div>',
        deleteContentError: '<div class="test-center">账户删除失败，请稍后重试！</div>',
        deleteTitle: '删除',
        enableTitle:'启用子账户',
        enableContentSuccess: '<div class="test-center">启用子账户成功</div>',
        enableContentError: '<div class="test-center">启用子账户失败，请稍后重试！</div>',
        enableContentTip: '<div class="test-center">当前子账户未启用，您确认要启用此账户？</div>',
        unableTitle:'停用子账户',
        unableContentSuccess: '<div class="test-center">停用子账户成功</div>',
        unableContentError: '<div class="test-center">停用子账户失败，请稍后重试！</div>',
        unableContentTip: '<div class="test-center">当前子账户已启用，您确认要停用此账户？</div>',
        confirmButton: '确认',
        closeIcon: true,
        icon: 'fa fa-warning',
        theme: 'white',
        animationSpeed: 1000,
        animation: 'rotateYR ',
        closeAnimation: 'rotateYR'
    };

    //脚本执行
    $scope.init();
    $scope.get_child_merchant();
    $scope.get_roles();
});