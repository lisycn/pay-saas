package cn.vfinance.open.saas.web.response;

/**
 * Created by qiuwei on 2016/9/30.
 */
public class UploadResponse {

    private int status;
    private String url;

    public UploadResponse(int status) {
        this.status = status;
    }

    public UploadResponse(int status, String url) {
        this.status = status;
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
