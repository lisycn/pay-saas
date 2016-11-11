package cn.vfinance.open.saas.web.analyse;

import cn.vfinance.open.saas.web.enums.InsertStatus;
import cn.vfinance.open.saas.web.model.ChannelAli;
import cn.vfinance.open.saas.web.model.ChannelUnion;
import cn.vfinance.open.saas.web.model.ChannelWechat;
import cn.vfinance.open.saas.web.response.AppChannelAliResponse;
import cn.vfinance.open.saas.web.response.AppChannelUnionResponse;
import cn.vfinance.open.saas.web.response.AppChannelWechatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by qiuwei on 2016/10/11.
 */
public class ChannelAnalysor {

    private static final Logger log = LoggerFactory.getLogger(ChannelAnalysor.class);

    public static AppChannelAliResponse analyseAli(InsertStatus insertStatus, ChannelAli channelAli) {
        AppChannelAliResponse appResponse = new AppChannelAliResponse();
        appResponse.setStatus(insertStatus.getStatus());
        appResponse.setChannelAli(channelAli);
        return appResponse;
    }

    public static AppChannelAliResponse analyseAli(InsertStatus insertStatus) {
        AppChannelAliResponse appResponse = new AppChannelAliResponse();
        appResponse.setStatus(insertStatus.getStatus());
        return appResponse;
    }

    public static AppChannelWechatResponse analyseWechat(InsertStatus insertStatus, ChannelWechat channelWechat) {
        AppChannelWechatResponse appResponse = new AppChannelWechatResponse();
        appResponse.setStatus(insertStatus.getStatus());
        appResponse.setChannelWechat(channelWechat);
        return appResponse;
    }

    public static AppChannelWechatResponse analyseWechat(InsertStatus insertStatus) {
        AppChannelWechatResponse appResponse = new AppChannelWechatResponse();
        appResponse.setStatus(insertStatus.getStatus());
        return appResponse;
    }

    public static AppChannelUnionResponse analyseUnion(InsertStatus insertStatus, ChannelUnion channelUnion) {
        AppChannelUnionResponse appResponse = new AppChannelUnionResponse();
        appResponse.setStatus(insertStatus.getStatus());
        appResponse.setChannelUnion(channelUnion);
        return appResponse;
    }

    public static AppChannelUnionResponse analyseUnion(InsertStatus insertStatus) {
        AppChannelUnionResponse appResponse = new AppChannelUnionResponse();
        appResponse.setStatus(insertStatus.getStatus());
        return appResponse;
    }
}
