/**
 * Created by sean on 10/11/16.
 */


$(function(){
    // getTop();
    getFooter();
    isLogin();
});

function getTop() {
    getHtml("/saas-web/website/top", "top");
}

function getFooter() {
    getHtml("/saas-web/website/footer", "footer");
}

function getHtml(urlStr, divId) {
    $.ajax({
        type : "POST",
        url : urlStr,
        dataType :"html",
        success : function(data) {
            $("#"+divId).html(data);
        }
    });
}

function isLogin() {
    $.getJSON("/saas-web/get_user_session", function(result){
        if (result && result.email) {
            var mail = result.email;
            if (mail.length>10)
                mail = mail.substring(0, 10) + "...";
            $("#aUser").html(mail);
            isNeedLogin(false);
        } else {
            isNeedLogin(true);
        }
    });
}

function isNeedLogin(isLogin) {
    if (isLogin) {
        $("#divLogin").show();
        $("#divUser").hide();
        $("#aUser").html('');
    } else {
        $("#divLogin").hide();
        $("#divUser").show();
    }
}

$("#logout").bind("click", function(){
    $.getJSON("/saas-web/logout", function(result){
        if (result) {
            isNeedLogin(true);
        }
    });
});
