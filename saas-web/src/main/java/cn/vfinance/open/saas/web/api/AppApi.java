package cn.vfinance.open.saas.web.api;

import cn.vfinance.open.saas.web.model.AppInfo;
import cn.vfinance.open.saas.web.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by qiuwei on 2016/9/29.
 */
@RestController
@RequestMapping("/app")
public class AppApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppApi.class);
    @Autowired
    private AppService appService;

    @RequestMapping(value = "add_app", method = RequestMethod.POST)
    @ResponseBody
    public boolean addApp(AppInfo appInfo) {
        LOGGER.info("LOGGER_QW_10070: to add app api. appInfo = {}. ", appInfo);
        boolean addApp = appService.insert(appInfo);
        LOGGER.info("LOGGER_QW_11310: to add app api result = {}. ", addApp);
        return addApp;
    }

    @RequestMapping(value = "query_all_app", method = RequestMethod.POST)
    @ResponseBody
    public List<AppInfo> queryAllApp(String merchantId) {
        LOGGER.info("LOGGER_QW_11320: to query all app api. merchantId = {}. ", merchantId);
        List<AppInfo> allAppList = appService.selectByMerchantId(merchantId);
        LOGGER.info("LOGGER_QW_11330: to query all app api result = {}. ", allAppList);
        return allAppList;
    }

    @RequestMapping(value = "update_app", method = RequestMethod.POST)
    @ResponseBody
    public boolean updateApp(AppInfo appInfo) {
        LOGGER.info("LOGGER_QW_10150: to update app . appInfo = {}. ",appInfo);
        boolean addApp =  appService.update(appInfo);
        LOGGER.info("LOGGER_QW_11340: to update app api result = {}. ", addApp);
        return addApp;
    }
}
