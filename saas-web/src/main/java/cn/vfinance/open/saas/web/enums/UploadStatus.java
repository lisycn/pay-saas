package cn.vfinance.open.saas.web.enums;

/**
 * Created by qiuwei on 2016/9/30.
 */
public enum UploadStatus {
    fail(0),//,"上传失败"
    success(1);//上传成功

    private int status;

    UploadStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
