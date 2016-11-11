package cn.vfinance.open.saas.web.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiuwei on 2016/10/26.
 */
public class MerchantPriviledgeGroup {

    private String groupCode;

    private String groupName;

    private String groupIcon;

    private List<MerchantPriviledge> priviledgeList = new ArrayList<>();

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<MerchantPriviledge> getPriviledgeList() {
        return priviledgeList;
    }

    public void setPriviledgeList(List<MerchantPriviledge> priviledgeList) {
        this.priviledgeList = priviledgeList;
    }
}
