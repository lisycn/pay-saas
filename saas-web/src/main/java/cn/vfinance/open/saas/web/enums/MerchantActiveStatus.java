package cn.vfinance.open.saas.web.enums;

/**
 * Created by qiuwei on 2016/10/26.
 */
public enum MerchantActiveStatus {
    NOT_ACTIVE("0"),//未激活
    ACTIVE("1");//激活

    private String status;

    MerchantActiveStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
