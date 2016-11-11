package cn.vfinance.open.saas.web.api;

import cn.vfinance.open.saas.web.enums.MerchantUsingStatus;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.model.MerchantRole;
import cn.vfinance.open.saas.web.response.MerchantResponse;
import cn.vfinance.open.saas.web.service.MerchantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by qiuwei on 2016/9/28.
 */
@RestController
@RequestMapping
public class MerchantApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantApi.class);

    @Autowired
    private MerchantService merchantService;


    @RequestMapping(value = "get_merchant")
    public MerchantInfo getMerchant(int id) {
        LOGGER.info("LOGGER_QW_10000: get merchant params value : {}.", id);
        return merchantService.getMerchant(id);
    }

    @RequestMapping(value = "get_merchant_role",method = RequestMethod.POST)
    public MerchantRole getMerchantRole(int merchantId) {
        LOGGER.info("LOGGER_QW_11300: get merchant role params merchantId {}. ", merchantId);
        return merchantService.getMerchantRole(merchantId);
    }

    @RequestMapping(value = "get_child_merchant")
    public List<MerchantInfo> getChildMerchant(int parentId) {
        LOGGER.info("LOGGER_QW_10860: get child merchant params parentId {}. ", parentId);
        return merchantService.getChildMerchant(parentId);
    }


    @RequestMapping(value = "delete_child_merchant")
    public MerchantResponse deleteChildMerchant(int parentId, int childId) {
        LOGGER.info("LOGGER_QW_10890: delete  child merchant params parentId : {}, childId: {}.  ", parentId, childId);
        boolean deleted = merchantService.deleteChildMerchant(childId);
        if (deleted) {
            return new MerchantResponse(true, merchantService.getChildMerchant(parentId));
        }
        return new MerchantResponse(false);
    }

    @RequestMapping(value = "get_roles", method = RequestMethod.POST)
    public List<MerchantRole> getRoles() {
        LOGGER.info("LOGGER_QW_10970: to get all roles from system. ");
        return merchantService.getRoles();
    }

    /**
     * 添加子账户
     *
     * @param email
     * @param roleId
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_child_merchant", method = RequestMethod.POST)
    public MerchantResponse addChildMerchant(String email, int roleId, int parentId) {
        LOGGER.info("LOGGER_QW_11030: to add child merchant. email = {}, roleId = {},parentId = {}. ", email, roleId, parentId);
        try {
            MerchantResponse addChildResponse = merchantService.addChildMerchant(email, roleId, parentId);
            LOGGER.info("LOGGER_QW_11040: end to add child merchant. response : {}. ", addChildResponse);
            return addChildResponse;
        }catch (Exception e){
            return new MerchantResponse(false, "添加子账户系统出错！");
        }
    }


    @RequestMapping(value = "edit_child_merchant", method = RequestMethod.POST)
    public MerchantResponse editChildMerchant(MerchantInfo child, Integer[] roleIdArray) {
        LOGGER.info("LOGGER_QW_11090: to edit child merchant. child : {}, roleIdArray : {}. ",child,roleIdArray);
        this.resetRoleData(child, roleIdArray);
        boolean updated = merchantService.editChildMerchant(child);
        if(updated){
            return new MerchantResponse(true, merchantService.getChildMerchant(child.getParentId()));
        }
        return new MerchantResponse(false);
    }

    //启用子账户
    @RequestMapping(value = "enable_child_using_status", method = RequestMethod.POST)
    public MerchantResponse enableChildUsingStatus(Integer childId, Integer parentId) {
        LOGGER.info("LOGGER_QW_11100: to enable child using status. childId : {}, parentId: {}.",childId,parentId);
        boolean enable = merchantService.updateUsingStatus(childId, MerchantUsingStatus.USING);
        if(enable){
            return new MerchantResponse(true, merchantService.getChildMerchant(parentId));
        }
        return new MerchantResponse(false);
    }

    //停用子账户
    @RequestMapping(value = "unable_child_using_status", method = RequestMethod.POST)
    public MerchantResponse unableChildUsingStatus(Integer childId, Integer parentId) {
        LOGGER.info("LOGGER_QW_11150: to unable child using status. childId : {}, parentId: {}.",childId,parentId);
        boolean enable = merchantService.updateUsingStatus(childId, MerchantUsingStatus.NOT_USING);
        if(enable){
            return new MerchantResponse(true, merchantService.getChildMerchant(parentId));
        }
        return new MerchantResponse(false);
    }

    private void resetRoleData(MerchantInfo child, Integer[] roleIdArray) {
        List<MerchantRole> merchantRoleList = new ArrayList<>();
        Arrays.stream(roleIdArray).forEach(roleId->{
            MerchantRole merchantRole = new MerchantRole();
            merchantRole.setId(roleId);
            merchantRoleList.add(merchantRole);
        });
        child.setMerchantRoleList(merchantRoleList);
    }
}
