package cn.vfinance.open.saas.web.response;

import cn.vfinance.open.saas.web.model.ChannelWechat;

/**
 * Created by qiuwei on 2016/10/11.
 */
public class AppChannelWechatResponse {

    private int status;

    private ChannelWechat channelWechat;

    public AppChannelWechatResponse() {
    }

    public AppChannelWechatResponse(int status, ChannelWechat channelWechat) {
        this.status = status;
        this.channelWechat = channelWechat;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ChannelWechat getChannelWechat() {
        return channelWechat;
    }

    public void setChannelWechat(ChannelWechat channelWechat) {
        this.channelWechat = channelWechat;
    }

    @Override
    public String toString() {
        return "AppChannelWechatResponse{" +
                "status=" + status +
                ", channelWechat=" + channelWechat +
                '}';
    }
}
