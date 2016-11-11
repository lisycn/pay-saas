package cn.vfinance.open.saas.web.enums;

/**
 * Created by qiuwei on 2016/9/30.
 */
public enum InsertStatus {
    fail(0),//,"插入失败"
    worked(1),//正在使用中 不能更改
    existed(2),//,"存在删除 则更新"
    success(3);//插入成功

    private int status;

    InsertStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
