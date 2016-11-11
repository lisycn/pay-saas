package cn.vfinance.open.saas.web;

import cn.vfinance.open.saas.web.model.MerchantPriviledge;
import cn.vfinance.open.saas.web.model.MerchantPriviledgeGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by qiuwei on 2016/11/8.
 */
public class GroupStream {
    /*
    pri:
    30031	20012	10010	2016-10-21 16:10:59	10010	product_manage	应用管理	product_manage	管理应用	2016-10-21 15:24:17
    30032	20012	10011	2016-10-21 16:10:59	10011	product_manage	应用管理	product_add	添加应用	2016-10-21 15:26:03
    30033	20012	10012	2016-10-21 16:10:59	10012	account_center	账户中心	account_auth	企业认证	2016-10-21 15:27:43
    30034	20012	10013	2016-10-21 16:10:59	10013	account_center	账户中心	account_info	账户信息	2016-10-21 15:28:08

    group:
    30031	20012	10010	2016-10-21 16:10:59	10010	product_manage	应用管理
                                                                                product_manage	管理应用	2016-10-21 15:24:17
                                                                                product_add	添加应用	2016-10-21 15:26:03
    30033	20012	10012	2016-10-21 16:10:59	10012	account_center	账户中心
                                                                                        account_auth	企业认证	2016-10-21 15:27:43
                                                                                        account_info	账户信息	2016-10-21 15:28:08
     */
    private static List<MerchantPriviledgeGroup> mapReduceGroup(List<MerchantPriviledge> merchantPriviledgeList) {
        Map<String, List<MerchantPriviledge>>  priMap = merchantPriviledgeList.stream().collect(Collectors.groupingBy(MerchantPriviledge::getGroupCode));
        List<MerchantPriviledgeGroup> merchantPriviledgeGroupList = new ArrayList<>();
        priMap.forEach((groupCode,merchantPriviledgeMapList)->{
            MerchantPriviledgeGroup merchantPriviledgeGroup = new MerchantPriviledgeGroup();
            merchantPriviledgeGroup.setGroupCode(groupCode);
            merchantPriviledgeGroup.setGroupName(merchantPriviledgeMapList.get(0).getGroupName());
            merchantPriviledgeGroup.setPriviledgeList(merchantPriviledgeMapList.stream().distinct().collect(Collectors.toList()));
            merchantPriviledgeGroupList.add(merchantPriviledgeGroup);
        });

        return merchantPriviledgeGroupList;
    }

    public static void main(String[] args) {
        List<MerchantPriviledge> merchantPriviledgeList = new ArrayList<>();
        MerchantPriviledge merchantPriviledge1 = new MerchantPriviledge();
        merchantPriviledge1.setGroupName("应用管理");
        merchantPriviledge1.setGroupCode("product_manage");
        merchantPriviledge1.setPriName("应用管理");
        merchantPriviledge1.setPriCode("product_manage");
        merchantPriviledgeList.add(merchantPriviledge1);

        MerchantPriviledge merchantPriviledge2 = new MerchantPriviledge();
        merchantPriviledge2.setGroupName("应用管理");
        merchantPriviledge2.setGroupCode("product_manage");
        merchantPriviledge2.setPriName("添加应用");
        merchantPriviledge2.setPriCode("product_add");
        merchantPriviledgeList.add(merchantPriviledge2);

        MerchantPriviledge merchantPriviledge3 = new MerchantPriviledge();
        merchantPriviledge3.setGroupName("账户中心");
        merchantPriviledge3.setGroupCode("account_center");
        merchantPriviledge3.setPriName("企业认证");
        merchantPriviledge3.setPriCode("account_auth");
        merchantPriviledgeList.add(merchantPriviledge3);

        MerchantPriviledge merchantPriviledge4 = new MerchantPriviledge();
        merchantPriviledge4.setGroupName("账户中心");
        merchantPriviledge4.setGroupCode("account_center");
        merchantPriviledge4.setPriName("账户信息");
        merchantPriviledge4.setPriCode("account_info");
        merchantPriviledgeList.add(merchantPriviledge4);

        MerchantPriviledge merchantPriviledge5 = new MerchantPriviledge();
        merchantPriviledge5.setGroupName("账户中心");
        merchantPriviledge5.setGroupCode("account_center");
        merchantPriviledge5.setPriName("账户信息");
        merchantPriviledge5.setPriCode("account_info");
        merchantPriviledgeList.add(merchantPriviledge5);

        List<MerchantPriviledgeGroup> merchantPriviledgeGroupList = mapReduceGroup(merchantPriviledgeList);
        merchantPriviledgeGroupList.forEach((group -> {
            System.out.println("group code : "+group.getGroupCode()+" group name : "+group.getGroupName());
            System.out.println("--------pri list begin-------");
            group.getPriviledgeList().forEach(pri->{
                System.out.println("pri code : " + pri.getPriCode() + " pri name : " + pri.getPriName());
            });
            System.out.println("--------pri list end-------");
        }));
    }
}
