package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.dao.AppInfoMapper;
import cn.vfinance.open.saas.web.dao.MerchantInfoMapper;
import cn.vfinance.open.saas.web.model.AppInfo;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiuwei on 2016/9/29.
 */
@Service
public class AppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppService.class);

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    private MerchantInfoMapper merchantInfoMapper;

    @Autowired
    private ExternalConfig externalConfig;

    /**
     * 如果是子账户，则插入主账户ID
     * @param appInfo
     * @return
     */
    public boolean insert(AppInfo appInfo) {
        try {
            LOGGER.info("LOGGER_QW_10050: insert app info. appInfo={}. ", appInfo);
            appInfo.setAppSecret(externalConfig.getPublicKey());
            Integer parentId = this.isChild(String.valueOf(appInfo.getMerchantId()));
            if (parentId != null) {
                LOGGER.info("LOGGER_QW_11380: insert app info. merchant is child. ");
                appInfo.setMerchantId(parentId);
            }
            int result = appInfoMapper.insert(appInfo);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_10100: insert app info success. result = {}. ",result);
                this.updateMerchantAppKey(appInfo);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("LOGGER_QW_10090: insert app info exception : {}. ", e);
        }
        LOGGER.info("LOGGER_QW_10200: insert app info false. ");
        return false;
    }

    private void updateMerchantAppKey(AppInfo appInfo) {
        List<AppInfo> inserted = appInfoMapper.selectBySelective(appInfo);
        MerchantInfo merchantInfo = new MerchantInfo();
        merchantInfo.setId(appInfo.getMerchantId());
        merchantInfo.setAppKey(inserted.get(0).getAppKey());
        merchantInfoMapper.updateByPrimaryKeySelective(merchantInfo);
    }

    /**
     * 判断是否是子账户，如果是子账户，查询父账户
     * @param merchantId
     * @return
     */
    public List<AppInfo> selectByMerchantId(String merchantId) {
        LOGGER.info("LOGGER_QW_10120: select all app by merchantId = {}. ", merchantId);
        Integer parentId = this.isChild(merchantId);
        if (parentId != null) {
            LOGGER.info("LOGGER_QW_11390: select all app merchant is child. ");
            MerchantInfo child = new MerchantInfo();
            child.setId(parentId);
            MerchantInfo existsChild = merchantInfoMapper.selectBySelective(child).get(0);
            merchantId = String.valueOf(existsChild.getId());
        }
        AppInfo appInfo = new AppInfo();
        appInfo.setMerchantId(Integer.parseInt(merchantId));
        List<AppInfo> appInfoList = appInfoMapper.selectBySelective(appInfo);
        if (appInfoList == null || appInfoList.isEmpty()) {
            LOGGER.info("LOGGER_QW_10140: select all app list is empty. merchantId = {}. ", merchantId);
            return null;
        }
        LOGGER.info("LOGGER_QW_10130: select all app list size: {}. ",appInfoList.size());
        return appInfoList;
    }

    private Integer isChild(String merchantId) {
        MerchantInfo merchant = merchantInfoMapper.selectByPrimaryKey(Integer.parseInt(merchantId));
        if(merchant!=null && merchant.getParentId() != null){
            return merchant.getParentId();
        }
        return null;
    }


    public boolean update(AppInfo appInfo) {
        try {
            LOGGER.info("LOGGER_QW_10160: to update app. appInfo = {}. ", appInfo);
            int result = appInfoMapper.updateByPrimaryKeySelective(appInfo);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_10170: update app info success.");
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("LOGGER_QW_10180: update app info exception : {}. ", e);
        }
        LOGGER.info("LOGGER_QW_10190: update app info false.");
        return false;
    }

}
