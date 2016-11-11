package cn.vfinance.open.saas.web.enums;

/**
 * Created by qiuwei on 2016/10/8.
 */
public enum ChannelStatus {
    DELETED("0"),//删除
    WORKED("1");//使用中

    private String status;

    ChannelStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
