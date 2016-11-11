package cn.vfinance.open.saas.web.response;

import cn.vfinance.open.saas.web.model.ChannelUnion;

/**
 * Created by qiuwei on 2016/10/11.
 */
public class AppChannelUnionResponse {

    private int status;

    private ChannelUnion channelUnion;

    public AppChannelUnionResponse() {
    }

    public AppChannelUnionResponse(int status, ChannelUnion channelUnion) {
        this.status = status;
        this.channelUnion = channelUnion;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ChannelUnion getChannelUnion() {
        return channelUnion;
    }

    public void setChannelUnion(ChannelUnion channelUnion) {
        this.channelUnion = channelUnion;
    }

    @Override
    public String toString() {
        return "AppChannelUnionResponse{" +
                "status=" + status +
                ", channelUnion=" + channelUnion +
                '}';
    }
}
