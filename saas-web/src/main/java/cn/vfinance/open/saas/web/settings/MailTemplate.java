package cn.vfinance.open.saas.web.settings;

/**
 * Created by qiuwei on 2016/11/3.
 */
public class MailTemplate {

    /*子账户激活邮箱内容*/
    public static final String CHILD_ACTIVE_CONTENT = "<!DOCTYPE html>\n" +
            "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n" +
            "    <title>验证通知</title>\n" +
            "    <style>\n" +
            "        a {\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "\n" +
            "        .content {\n" +
            "            width: 700px;\n" +
            "            background: #f0f0f0;\n" +
            "            margin: 50px auto;\n" +
            "        }\n" +
            "\n" +
            "        .content_top {\n" +
            "            width: 648px;\n" +
            "            height: 52px;\n" +
            "            padding: 26px;\n" +
            "            margin: 0;\n" +
            "        }\n" +
            "\n" +
            "        .content_topL {\n" +
            "            width: 200px;\n" +
            "            float: left;\n" +
            "            padding: 9px 0 0 0;\n" +
            "        }\n" +
            "\n" +
            "        .content_topR {\n" +
            "            width: 200px;\n" +
            "            float: right;\n" +
            "            text-align: right;\n" +
            "            font-family: \"宋体\";\n" +
            "            font-size: 12px;\n" +
            "            padding-top: 28px;\n" +
            "            color: #747474;\n" +
            "        }\n" +
            "\n" +
            "        .content_topR a {\n" +
            "            color: #747474;\n" +
            "        }\n" +
            "\n" +
            "        .content_topR a:hover {\n" +
            "            color: #1f86ef;\n" +
            "        }\n" +
            "\n" +
            "        .content_con {\n" +
            "            width: 648px;\n" +
            "            margin: 0 25px;\n" +
            "            padding: 0px;\n" +
            "            border: 1px solid #ecebeb;\n" +
            "            background: url(img/letter_bottom.png) #fcfcfc bottom center no-repeat;\n" +
            "        }\n" +
            "\n" +
            "        .content_conT {\n" +
            "            width: 648px;\n" +
            "            height: 79px;\n" +
            "            background: url(img/letter_top.png) top center no-repeat;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "\n" +
            "        .content_conT span {\n" +
            "            font-size: 26px;\n" +
            "            padding-top: 13px;\n" +
            "            letter-spacing: 1px;\n" +
            "            display: block;\n" +
            "            font-family: \"微软雅黑\";\n" +
            "            font-weight: 100;\n" +
            "            color: #1f86ef;\n" +
            "        }\n" +
            "\n" +
            "        .content_conB {\n" +
            "            width: 580px;\n" +
            "            height: auto;\n" +
            "            padding: 27px 34px 45px 34px;\n" +
            "            font-size: 16px;\n" +
            "            font-family: \"微软雅黑\";\n" +
            "            line-height: 28px;\n" +
            "        }\n" +
            "\n" +
            "        .content_conB p {\n" +
            "            padding: 1px 0;\n" +
            "            margin: 10px 0px 25px 0px;\n" +
            "        }\n" +
            "\n" +
            "        .content_conB a {\n" +
            "            color: #0066ce;\n" +
            "        }\n" +
            "\n" +
            "        .content_conB a:hover {\n" +
            "            color: #439cf7;\n" +
            "        }\n" +
            "\n" +
            "        .content_bottom {\n" +
            "            width: 580px;\n" +
            "            height: 100px;\n" +
            "            padding: 46px 60px 35px 60px;\n" +
            "        }\n" +
            "\n" +
            "        .content_bottomL {\n" +
            "            width: 120px;\n" +
            "            height: 100px;\n" +
            "            float: left;\n" +
            "        }\n" +
            "\n" +
            "        .content_bottomR {\n" +
            "            width: 460px;\n" +
            "            height: auto;\n" +
            "            float: left;\n" +
            "            color: #666666;\n" +
            "        }\n" +
            "\n" +
            "        .content_bottomR .title {\n" +
            "            font-size: 20px;\n" +
            "            font-family: \"微软雅黑\";\n" +
            "        }\n" +
            "\n" +
            "        .content_bottomR p {\n" +
            "            font-size: 12px;\n" +
            "            line-height: 18px;\n" +
            "            padding: 5px 0;\n" +
            "            margin: 0px;\n" +
            "            font-family: Arial;\n" +
            "        }\n" +
            "\n" +
            "        .content_bottomR a {\n" +
            "            color: #666666;\n" +
            "        }\n" +
            "\n" +
            "        .content_bottomR a:hover {\n" +
            "            color: #1f86ef;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body style=\"background:#fafafa;\">\n" +
            "<div class=\"content\">\n" +
            "    <div class=\"content_top\">\n" +
            "        <div class=\"content_topL\"><img src=\"_BASE_URL_VALUE_/img/logo.png\"/></div>\n" +
            "        <div class=\"content_topR\"></div>\n" +
            "    </div>\n" +
            "    <div class=\"content_con\">\n" +
            "        <div class=\"content_conT\"><span>验证通知</span></div>\n" +
            "        <div class=\"content_conB\">\n" +
            "            <p>尊敬的_EMAIL_VALUE_您好！</p>\n" +
            "\n" +
            "            <p>\n" +
            "                感谢您使用维金云钱包，请点击以下链接设置账户密码，完成验证：<a href=\"_ACTIVE_ACTION_VALUE_?email=_EMAIL_VALUE_&id=_ID_VALUE_&createTime=_CREATE_TIME_VALUE_\" target=\"_blank\">点击激活</a>\n" +
            "\n" +
            "\n" +
            "            </p>\n" +
            "            <p>此为系统邮件，请勿直接回复。</p></div>\n" +
            "    </div>\n" +
            "    <div class=\"content_bottom\">\n" +
            "        <div class=\"content_bottomL\"><img src=\"_BASE_URL_VALUE_/img/Qr code.png\"/></div>\n" +
            "        <div class=\"content_bottomR\"><span class=\"title\">关于维金</span>\n" +
            "\n" +
            "            <p>维金云钱包支持所有邮箱、最快新邮件提醒、图片附件压缩快速收发节省流量、 手势密码等多重安全保障。\n" +
            "                <br/>网址：www.vfinance.cn 服务热线：400-101-9595合作洽谈：021-80168700</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
    /*子账户邮箱 需要替换的内容*/
    public static final String CHILD_ACTIVE_EMAIL ="_EMAIL_VALUE_";
    /*子账户ID 需要替换的内容*/
    public static final String CHILD_ACTIVE_ID ="_ID_VALUE_";
    /*子账户创建时间 需要替换的内容*/
    public static final String CHILD_ACTIVE_TIME ="_CREATE_TIME_VALUE_";
    /*子账户激活路径 需要替换的内容*/
    public static final String CHILD_ACTIVE_ACTION ="_ACTIVE_ACTION_VALUE_";
    /*子账户域名路径 需要替换的内容*/
    public static final String CHILD_BASE_URL ="_BASE_URL_VALUE_";


}
