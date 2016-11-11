package cn.vfinance.open.saas.web.response;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Sean Weng
 */
public class FileUplodResult {

    private boolean success;
    private String resultMessage;
    private String url;

    public FileUplodResult(boolean isSuccess, String url, String resultMessage) {
        this.success = isSuccess;
        this.url = url;
        this.resultMessage = resultMessage;
    }

    public FileUplodResult(boolean isSuccess, String resultMessage) {
        this.success = isSuccess;
        this.resultMessage = resultMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
