package cn.vfinance.open.saas.web.enums;

/**
 * Created by qiuwei on 2016/11/2.
 * 系统角色
 *     管理员
 *     运营者
 *     开发者
 */
public enum RoleCode {
    ADMIN(20010),//管理员
    operator(20011),//运营者
    developer(20012);//开发者

    private int code;

    RoleCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
