package cn.vfinance.open.saas.web.enums;

/**
 * Created by qiuwei on 2016/10/28.
 */
public enum MerchantUsingStatus {
    NOT_USING("0"),//停用
    USING("1");//启用

    private String status;

    MerchantUsingStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
