package cn.vfinance.open.saas.web.response;

import cn.vfinance.open.saas.web.model.MerchantInfo;

import java.util.List;

/**
 * Created by qiuwei on 2016/10/25.
 */
public class MerchantResponse {

    private boolean success;

    private String code;

    private List<MerchantInfo> data;

    public MerchantResponse(boolean success, List<MerchantInfo> data) {
        this.success = success;
        this.data = data;
    }

    public MerchantResponse(boolean success) {
        this.success = success;
    }

    public MerchantResponse(boolean success, String code) {
        this.success = success;
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<MerchantInfo> getData() {
        return data;
    }

    public void setData(List<MerchantInfo> data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "MerchantResponse{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }
}
