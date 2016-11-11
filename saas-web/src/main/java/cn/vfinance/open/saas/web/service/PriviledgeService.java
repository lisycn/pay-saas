package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.dao.MerchantPriviledgeMapper;
import cn.vfinance.open.saas.web.model.MerchantPriviledge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by qiuwei on 2016/11/9.
 */
@Service
public class PriviledgeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriviledgeService.class);

    @Autowired
    private MerchantPriviledgeMapper merchantPriviledgeMapper;

    public boolean isEnoughPriviledge(Integer merchantId, String priviledge) {
        LOGGER.info("LOGGER_QW_11420: begin to check priviledge. merchantId:{},priviledge:{}. ", merchantId, priviledge);
        List<MerchantPriviledge> priviledgeList = merchantPriviledgeMapper.selectPriviledgeByMerchant(merchantId);
        if (priviledgeList.isEmpty()) {
            LOGGER.info("LOGGER_QW_11440: no priviledge. check priviledge result : false");
            return false;
        }
        if (priviledgeList.stream().anyMatch(merchantPriviledge -> priviledge.equals(merchantPriviledge.getPriCode()))) {
            LOGGER.info("LOGGER_QW_11450: priviledge matched. check priviledge result : true");
            return true;
        }
        LOGGER.info("LOGGER_QW_11460: priviledge not matched. check priviledge result : false");
        return false;
    }
}
