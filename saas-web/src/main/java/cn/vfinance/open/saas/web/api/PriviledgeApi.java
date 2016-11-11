package cn.vfinance.open.saas.web.api;

import cn.vfinance.open.saas.web.service.PriviledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by qiuwei on 2016/11/9.
 */
@RestController
@RequestMapping
public class PriviledgeApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriviledgeApi.class);

    @Autowired
    private PriviledgeService priviledgeService;

    @RequestMapping(value = "check_priviledge",method = RequestMethod.POST)
    public boolean check(Integer merchantId,String priviledge) {
        LOGGER.info("LOGGER_QW_11400: check priviledge params : merchantId = {}, priviledge = {}. ",merchantId,priviledge);
        return priviledgeService.isEnoughPriviledge(merchantId, priviledge.replaceAll("/", ""));
    }
}
