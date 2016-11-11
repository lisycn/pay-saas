package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.dao.MerchantInfoMapper;
import cn.vfinance.open.saas.web.dao.MerchantPriviledgeMapper;
import cn.vfinance.open.saas.web.dao.MerchantRoleMapper;
import cn.vfinance.open.saas.web.dao.MerchantRoleMappingMapper;
import cn.vfinance.open.saas.web.enums.MerchantActiveStatus;
import cn.vfinance.open.saas.web.enums.MerchantUsingStatus;
import cn.vfinance.open.saas.web.enums.RoleCode;
import cn.vfinance.open.saas.web.model.*;
import cn.vfinance.open.saas.web.response.MerchantResponse;
import cn.vfinance.open.saas.web.settings.MailTemplate;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import cn.vfinance.open.saas.web.util.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by qiuwei on 2016/9/28.
 */
@Service
public class MerchantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantService.class);

    @Autowired
    private MerchantInfoMapper merchantInfoMapper;

    @Autowired
    private MerchantRoleMapper merchantRoleMapper;

    @Autowired
    private MerchantRoleMappingMapper merchantRoleMappingMapper;

    @Autowired
    private MerchantPriviledgeMapper merchantPriviledgeMapper;

    @Autowired
    private ExternalConfig externalConfig;

    @Autowired
    private RegisterService registerService;

    public MerchantInfo getMerchant(Integer merchantId) {
        LOGGER.info("LOGGER_QW_10020: begin to get merchant.merchantID:{}.", merchantId);
        MerchantInfo merchantInfo = merchantInfoMapper.selectByPrimaryKey(merchantId);
        LOGGER.info("LOGGER_QW_10040: end to get merchant. merchant:{}. ", merchantInfo);
        return merchantInfo;
    }

    /**
     * 角色 权限列表
     *  如果用户存在多个角色，统一相同权限
     *
     *  角色名称和角色
     * @param merchantId
     * @return
     */
    public MerchantRole getMerchantRole(int merchantId) {
        LOGGER.info("LOGGER_QW_11280: begin to get merchant role. merchantId = {}. ", merchantId);
        List<MerchantRole> merchantRoleList = merchantRoleMapper.selectRoleByMerchant(merchantId);
        MerchantRole multiRole = new MerchantRole();
        if (!merchantRoleList.isEmpty()) {
            LOGGER.info("LOGGER_QW_11350: merchant has roles.  merchant id : {}, role size ; {}. ", merchantId, merchantRoleList.size());
            List<MerchantPriviledge> merchantPriviledgeList = new ArrayList<>();
            StringBuilder roleCode = new StringBuilder();
            StringBuilder roleName = new StringBuilder();
            merchantRoleList.stream().forEach(merchantRole -> {
                //商户权限
                roleCode.append(merchantRole.getRoleCode()).append(" ");
                roleName.append(merchantRole.getRoleName()).append(" ");
                merchantPriviledgeList.addAll(merchantPriviledgeMapper.selectPriviledgeByRole(merchantRole.getId()));
            });
            multiRole.setRoleCode(roleCode.toString());
            multiRole.setRoleName(roleName.toString());
            multiRole.setPriviledgeGroupList(this.mapReduceGroup(merchantPriviledgeList));
        } else {
            LOGGER.info("LOGGER_QW_11360: merchant has no role. merchant id : {}. ", merchantId);
        }
        return multiRole;
    }

    public List<MerchantInfo> getChildMerchant(Integer parentId) {
        LOGGER.info("LOGGER_QW_10830: begin to get child merchant. parentId = {}. ", parentId);
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setParentId(parentId);
        List<MerchantInfo> merchantInfoList = merchantInfoMapper.selectBySelective(merchantInfo);
        if (!merchantInfoList.isEmpty()) {
            LOGGER.info("LOGGER_QW_10840: end to get child merchant. list size ={}. ", merchantInfoList.size());
            merchantInfoList.stream().forEach(childMerchant -> {
                List<MerchantRole> merchantRoleList = merchantRoleMapper.selectRoleByMerchant(childMerchant.getId());
                if (!merchantRoleList.isEmpty()) {
                    LOGGER.info("LOGGER_QW_10870: merchant has roles.  merchant id : {}, role size ; {}. ", childMerchant.getId(), merchantInfoList.size());
                    merchantRoleList.stream().forEach(merchantRole -> {
                        //商户权限
                        List<MerchantPriviledge> merchantPriviledgeList = merchantPriviledgeMapper.selectPriviledgeByRole(merchantRole.getId());

                        merchantRole.setPriviledgeGroupList(this.mapReduceGroup(merchantPriviledgeList));
                    });
                    childMerchant.setMerchantRoleList(merchantRoleList);
                } else {
                    LOGGER.info("LOGGER_QW_10880: merchant has no role. merchant id : {}. ", childMerchant.getId());
                }
            });
            return merchantInfoList;
        }
        LOGGER.info("LOGGER_QW_10850: end to get child merchant. list size = 0");
        return null;
    }

    @Transactional
    public boolean deleteChildMerchant(Integer childId) {
        try {
            LOGGER.info("LOGGER_QW_10900: begin to delete child merchant. childId = {}. ", childId);
            if (merchantInfoMapper.deleteByPrimaryKey(childId) > 0 &&
                    merchantRoleMappingMapper.deleteByMerchantId(childId) > 0) {
                LOGGER.info("LOGGER_QW_10910:  end to delete child merchant. success");
                return true;
            }
            LOGGER.info("LOGGER_QW_10920: end to delete child merchant. false");
            return false;
        } catch (Exception e) {
            LOGGER.error("LOGGER_QW_10930: end to delete child merchant. error : {}. ", e);
            return false;
        }
    }

    public List<MerchantRole> getRoles() {
        LOGGER.info("LOGGER_QW_10940: begin to get roles. ");
        List<MerchantRole> merchantRoleList = merchantRoleMapper.selectAllRoles();
        if (merchantRoleList.isEmpty()) {
            LOGGER.error("LOGGER_QW_10950: no roles exception. ");
            return null;
        }
        merchantRoleList.stream().forEach(merchantRole -> {
            //商户权限
            List<MerchantPriviledge> merchantPriviledgeList = merchantPriviledgeMapper.selectPriviledgeByRole(merchantRole.getId());

            merchantRole.setPriviledgeGroupList(this.mapReduceGroup(merchantPriviledgeList));
        });
        LOGGER.info("LOGGER_QW_10960: end to get roles . roles size = {}. ", merchantRoleList.size());
        return merchantRoleList;
    }

    @Transactional(rollbackFor = Exception.class)
    public MerchantResponse addChildMerchant(String email, int roleId,int parentId) throws Exception {
        try {
            LOGGER.info("LOGGER_QW_10980: begin to add child merchant. params email = {}, roleId = {}, parentId = {}. ", email, roleId,parentId);
            MerchantInfo emailMerchant = new MerchantInfo();
            emailMerchant.setEmail(email);
            List<MerchantInfo> checkEmail = merchantInfoMapper.selectBySelective(emailMerchant);
            if (!checkEmail.isEmpty()) {
                LOGGER.info("LOGGER_QW_10990: email is exists. email = {}. ", email);
                return new MerchantResponse(false, "邮箱已经存在！");
            }
            MerchantInfo childMerchant = this.buildChildMerchant(email, parentId);
            merchantInfoMapper.insert(childMerchant);
            merchantRoleMappingMapper.insert(this.buildMerchantRoleMapping(roleId, childMerchant.getId()));
            LOGGER.info("LOGGER_QW_11000: begin to send email. ");
            MerchantInfo merchantTime = merchantInfoMapper.selectByPrimaryKey(childMerchant.getId());
            //邮箱内容
            String content = MailTemplate.CHILD_ACTIVE_CONTENT //邮箱模板内容
                    .replaceAll(MailTemplate.CHILD_ACTIVE_EMAIL, email)//替换邮箱
                    .replaceAll(MailTemplate.CHILD_ACTIVE_TIME, String.valueOf(merchantTime.getCreateTime().getTime()))//替换时间
                    .replaceAll(MailTemplate.CHILD_ACTIVE_ID, String.valueOf(childMerchant.getId()))//替换子账户ID
                    .replaceAll(MailTemplate.CHILD_BASE_URL, externalConfig.getMailImgUrl())
                    .replaceAll(MailTemplate.CHILD_ACTIVE_ACTION, externalConfig.getChildActiveUrl()); //替换激活路径

            registerService.sendEmail(email, content);
            LOGGER.info("LOGGER_QW_11010: success to send email. ");
            return new MerchantResponse(true,this.getChildMerchant(parentId));
        } catch (Exception e) {
            LOGGER.error("LOGGER_QW_11020: end to add child merchant error : {}. ", e);
            throw new Exception("添加子账户系统出错！");
        }
    }

    @Transactional
    public boolean editChildMerchant(MerchantInfo childMerchant) {
        try {
            LOGGER.info("LOGGER_QW_11050: begin to edit child merchant. child = {}. ", childMerchant);
            MerchantInfo update = new MerchantInfo();
            update.setEmail(childMerchant.getEmail());
            update.setId(childMerchant.getId());
            int result =  merchantInfoMapper.updateByPrimaryKeySelective(update);
            if (result > 0) {
                merchantRoleMappingMapper.deleteByMerchantId(childMerchant.getId());
                childMerchant.getMerchantRoleList().stream().forEach((merchantRole -> {
                    MerchantRoleMapping roleMapping = new MerchantRoleMapping();
                    roleMapping.setMerchantId(childMerchant.getId());
                    roleMapping.setRoleId(merchantRole.getId());
                    merchantRoleMappingMapper.insertSelective(roleMapping);
                }));
                LOGGER.info("LOGGER_QW_11060: end to edit child merchant. success");
                return true;
            }
            LOGGER.info("LOGGER_QW_11070:  end to edit child merchant. false");
            return false;
        } catch (Exception e) {
            LOGGER.info("LOGGER_QW_11080: end to edit child merchant. error : {}. ", e);
            return false;
        }
    }

    public boolean updateUsingStatus(Integer childId, MerchantUsingStatus usingStatus) {
        LOGGER.info("LOGGER_QW_11110:  begin to edit child merchant. childId = {}, usingStatus ={}. ", childId,usingStatus);
        try {
            MerchantInfo update = new MerchantInfo();
            update.setUsingStatus(usingStatus.getStatus());
            update.setId(childId);
            int result = merchantInfoMapper.updateByPrimaryKeySelective(update);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_11120: end to update child using status. success");
                return true;
            }
            LOGGER.info("LOGGER_QW_11140: end to update child using status. false");
            return false;
        }catch (Exception e){
            LOGGER.info("LOGGER_QW_11130: end to update child using status. error : {}. ", e);
            return false;
        }

    }

    /**
     * 设置密码 更新激活状态
     *
     * @param childId
     * @param pwd
     */
    public boolean updateChildSetting(String childId,String pwd) {
        LOGGER.info("LOGGER_QW_11200:  begin to update child setting. childId = {}", childId);
        try {
            String salt = PasswordEncoder.random();
            PasswordEncoder pass = new PasswordEncoder(salt, "MD5");
            MerchantInfo merchantInfo = new MerchantInfo();
            merchantInfo.setPassword(pass.encode(pwd));
            merchantInfo.setSalt(salt);
            merchantInfo.setStatus(MerchantActiveStatus.ACTIVE.getStatus()); // 默认1是有效 0无效
            merchantInfo.setId(Integer.parseInt(childId));
            int result = merchantInfoMapper.updateByPrimaryKeySelective(merchantInfo);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_11220: end to update child setting. success");
                return true;
            }
            LOGGER.info("LOGGER_QW_11230: end to update child setting. false");
            return false;
        }catch (Exception e){
            LOGGER.info("LOGGER_QW_11220: end to update child setting . error : {}. ", e);
            return false;
        }
    }

    /**
     * 默认注册商户为管理员
     */
    public void setDefaultAdmin(Integer merchantId) {
        MerchantRoleMapping mapping = new MerchantRoleMapping();
        mapping.setMerchantId(merchantId);
        mapping.setRoleId(RoleCode.ADMIN.getCode());
        mapping.setCreateTime(new Date());
        merchantRoleMappingMapper.insert(mapping);
    }

    private MerchantInfo buildChildMerchant(String email,int parentId) {
        MerchantInfo childMerchant = new MerchantInfo();
        childMerchant.setEmail(email);
        childMerchant.setParentId(parentId);
        childMerchant.setStatus(MerchantActiveStatus.NOT_ACTIVE.getStatus());//未激活
        childMerchant.setUsingStatus(MerchantUsingStatus.NOT_USING.getStatus());//未启用
        childMerchant.setAppKey("");
        childMerchant.setLoginNum(0);
        childMerchant.setCreateTime(new Date());
        return childMerchant;
    }

    private MerchantRoleMapping buildMerchantRoleMapping(int roleId, int merchantId) {
        MerchantRoleMapping merchantRoleMapping = new MerchantRoleMapping();
        merchantRoleMapping.setMerchantId(merchantId);
        merchantRoleMapping.setRoleId(roleId);
        merchantRoleMapping.setCreateTime(new Date());
        return merchantRoleMapping;
    }

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
//    private List<MerchantPriviledgeGroup> mapReduceGroup(List<MerchantPriviledge> merchantPriviledgeList) {
//        //按group分组。获取group集合
//        List<MerchantPriviledgeGroup> merchantPriviledgeGroupList = merchantPriviledgeList.stream().map(merchantPriviledge -> {
//            MerchantPriviledgeGroup merchantPriviledgeGroup = new MerchantPriviledgeGroup();
//            merchantPriviledgeGroup.setGroupCode(merchantPriviledge.getGroupCode());
//            merchantPriviledgeGroup.setGroupName(merchantPriviledge.getGroupName());
//            return merchantPriviledgeGroup;
//        }).collect(ArrayList::new,//supplier：一个能创造目标类型实例的方法。
//                (list,item)->{ // accumulator：一个将当元素添加到目标中的方法
//            if(list.stream().noneMatch((groupItem)-> groupItem.getGroupCode().equals(item.getGroupCode()))){
//                list.add(item);
//            }
//        },(list1,list2)-> list2.stream().forEach((item2)->{ //combiner：一个将中间状态的多个结果整合到一起的方法（并发的时候会用到）
//            if(list1.stream().noneMatch((item1)-> item1.getGroupCode().equals(item2.getGroupCode()))){
//                list1.add(item2);
//            }
//        }));
//        //将权限放入group集合中
//        merchantPriviledgeGroupList.stream().forEach(merchantPriviledgeGroup -> {
//            merchantPriviledgeList.stream().forEach(merchantPriviledge -> {
//                if(merchantPriviledge.getGroupCode().equals(merchantPriviledgeGroup.getGroupCode())){
//                    merchantPriviledgeGroup.getPriviledgeList().add(merchantPriviledge);
//                }
//            });
//        });
//
//        //去重
//        return merchantPriviledgeGroupList;
//    }

    private List<MerchantPriviledgeGroup> mapReduceGroup(List<MerchantPriviledge> merchantPriviledgeList) {
        Map<String, List<MerchantPriviledge>> priMap = merchantPriviledgeList.stream().collect(Collectors.groupingBy(MerchantPriviledge::getGroupCode));
        List<MerchantPriviledgeGroup> merchantPriviledgeGroupList = new ArrayList<>();
        priMap.forEach((groupCode,merchantPriviledgeMapList)->{
            MerchantPriviledgeGroup merchantPriviledgeGroup = new MerchantPriviledgeGroup();
            merchantPriviledgeGroup.setGroupCode(groupCode);
            merchantPriviledgeGroup.setGroupName(merchantPriviledgeMapList.get(0).getGroupName());
            merchantPriviledgeGroup.setGroupIcon(merchantPriviledgeMapList.get(0).getGroupIcon());
            merchantPriviledgeGroup.setPriviledgeList(merchantPriviledgeMapList.stream().distinct().collect(Collectors.toList()));
            merchantPriviledgeGroupList.add(merchantPriviledgeGroup);
        });

        return merchantPriviledgeGroupList;
    }
}

