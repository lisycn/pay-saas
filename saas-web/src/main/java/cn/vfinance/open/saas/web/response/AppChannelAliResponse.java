package cn.vfinance.open.saas.web.response;

import cn.vfinance.open.saas.web.model.ChannelAli;

/**
 * Created by qiuwei on 2016/10/11.
 */
public class AppChannelAliResponse {

    private int status;

    private ChannelAli channelAli;

    public AppChannelAliResponse() {
    }

    public AppChannelAliResponse(int status, ChannelAli channelAli) {
        this.status = status;
        this.channelAli = channelAli;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ChannelAli getChannelAli() {
        return channelAli;
    }

    public void setChannelAli(ChannelAli channelAli) {
        this.channelAli = channelAli;
    }

    @Override
    public String toString() {
        return "AppChannelAliResponse{" +
                "status=" + status +
                ", channelAli=" + channelAli +
                '}';
    }
}
