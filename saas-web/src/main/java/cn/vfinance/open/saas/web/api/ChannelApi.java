package cn.vfinance.open.saas.web.api;

import cn.vfinance.open.saas.web.enums.UploadStatus;
import cn.vfinance.open.saas.web.model.ChannelAli;
import cn.vfinance.open.saas.web.model.ChannelUnion;
import cn.vfinance.open.saas.web.model.ChannelWechat;
import cn.vfinance.open.saas.web.response.AppChannelAliResponse;
import cn.vfinance.open.saas.web.response.AppChannelUnionResponse;
import cn.vfinance.open.saas.web.response.AppChannelWechatResponse;
import cn.vfinance.open.saas.web.response.UploadResponse;
import cn.vfinance.open.saas.web.service.ChannelService;
import cn.vfinance.open.saas.web.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by qiuwei on 2016/9/30.
 */
@RestController
@RequestMapping("/channel")
public class ChannelApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelApi.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "add_channel_ali", method = RequestMethod.POST)
    public AppChannelAliResponse addChannelAli(ChannelAli channelAli) {
        LOGGER.info("LOGGER_QW_10270: api begin to add channel ali . channelAli={}. ", channelAli);
        AppChannelAliResponse result = channelService.insert(channelAli);
        LOGGER.info("LOGGER_QW_10290: api end result = {}. ", result);
        return result;
    }

    @RequestMapping(value = "upload_channel_cert")
    public UploadResponse uploadChannelCert(@RequestParam("file") MultipartFile file, String appKey,String channelCode) {
        LOGGER.info("LOGGER_QW_10340: to upload channel cert . ");
        String url = uploadService.handleFileUpload(file, appKey,channelCode);
        LOGGER.info("LOGGER_QW_10350: success to upload channel cert. url = {}. ", url);
        return this.analyse(url);
    }

    @RequestMapping(value = "add_channel_wx", method = RequestMethod.POST)
    public AppChannelWechatResponse addChannelWx(ChannelWechat channelWechat) {
        LOGGER.info("LOGGER_QW_10400: api begin to add channel wx . channelAli={}. ", channelWechat);
        AppChannelWechatResponse result = channelService.insert(channelWechat);
        LOGGER.info("LOGGER_QW_10410: api end result = {}. ", result);
        return result;
    }

    @RequestMapping(value = "add_channel_union", method = RequestMethod.POST)
    public AppChannelUnionResponse addChannelUnion(ChannelUnion channelUnion) {
        LOGGER.info("LOGGER_QW_10470: api begin to add channel union . channelUnion={}. ", channelUnion);
        AppChannelUnionResponse result = channelService.insert(channelUnion);
        LOGGER.info("LOGGER_QW_10480: api end result = {}. ", result);
        return result;
    }

    /**
     * 查询渠道信息
     * 支付宝扫码
     * @param appInfoId
     * @return
     */
    @RequestMapping(value = "query_channel_ali_scan", method = RequestMethod.POST)
    public ChannelAli queryChannelAliScanByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10490: queryChannelAliScanByApp. appInfoId = {}. ", appInfoId);
        return channelService.queryAliScanByApp(appInfoId);
    }

    /**
     * 查询渠道信息
     * 支付宝App
     * @param appInfoId
     * @return
     */
    @RequestMapping(value = "query_channel_ali_app", method = RequestMethod.POST)
    public ChannelAli queryChannelAliAppByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10530: queryChannelAliAppByApp. appInfoId = {}. ", appInfoId);
        return channelService.queryAliAppByApp(appInfoId);
    }

    /**
     * 查询渠道信息
     * 微信扫码
     * @param appInfoId
     * @return
     */
    @RequestMapping(value = "query_channel_wx_scan", method = RequestMethod.POST)
    public ChannelWechat queryChannelWxScanByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10710: queryChannelAliAppByApp. appInfoId = {}. ", appInfoId);
        return channelService.queryWxScanByApp(appInfoId);
    }

    /**
     * 查询渠道信息
     * 微信App
     * @param appInfoId
     * @return
     */
    @RequestMapping(value = "query_channel_wx_app", method = RequestMethod.POST)
    public ChannelWechat queryChannelWxAppByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10740: queryChannelWxAppByApp. appInfoId = {}. ", appInfoId);
        return channelService.queryWxAppByApp(appInfoId);
    }

    /**
     * 查询渠道信息
     * 银联支付
     * @param appInfoId
     * @return
     */
    @RequestMapping(value = "query_channel_union", method = RequestMethod.POST)
    public ChannelUnion queryChannelUnionByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10770: queryChannelUnionByApp. appInfoId = {}. ", appInfoId);
        return channelService.queryUnionByApp(appInfoId);
    }

    /**
     * 开通或者关闭渠道
     * 支付宝App
     * @return
     */
    @RequestMapping(value = "open_channel_ali_scan", method = RequestMethod.POST)
    public int openChannelAlScan(int appInfoId,String openStatus) {
        LOGGER.info("LOGGER_QW_10650: open channel ali scan. appInfoId={}. openStatus={}. ", appInfoId, openStatus);
        return channelService.updateAliScanByApp(appInfoId, openStatus);
    }

    /**
     * 开通或者关闭渠道
     * 支付宝App
     * @return
     */
    @RequestMapping(value = "open_channel_ali_app", method = RequestMethod.POST)
    public int openChannelAliApp(int appInfoId,String openStatus) {
        LOGGER.info("LOGGER_QW_10650: open channel ali app. appInfoId={}. openStatus={}. ", appInfoId, openStatus);
        return channelService.updateAliAppByApp(appInfoId, openStatus);
    }

    /**
     * 开通或者关闭渠道
     * 微信扫码
     * @return
     */
    @RequestMapping(value = "open_channel_wx_scan", method = RequestMethod.POST)
    public int openChannelWxScan(int appInfoId,String openStatus) {
        LOGGER.info("LOGGER_QW_10650: open channel ali app. appInfoId={}. openStatus={}. ", appInfoId, openStatus);
        return channelService.updateWxScanByApp(appInfoId, openStatus);
    }

    /**
     * 开通或者关闭渠道
     * 微信App支付
     * @return
     */
    @RequestMapping(value = "open_channel_wx_app", method = RequestMethod.POST)
    public int openChannelWxApp(int appInfoId,String openStatus) {
        LOGGER.info("LOGGER_QW_10650: open channel ali app. appInfoId={}. openStatus={}. ", appInfoId, openStatus);
        return channelService.updateWxAppByApp(appInfoId, openStatus);
    }

    /**
     * 开通或者关闭渠道
     * 银联支付
     * @return
     */
    @RequestMapping(value = "open_channel_union", method = RequestMethod.POST)
    public int openChannelUnion(int appInfoId,String openStatus) {
        LOGGER.info("LOGGER_QW_10800: open channel union. appInfoId={}. openStatus={}. ", appInfoId, openStatus);
        return channelService.updateUnionByApp(appInfoId, openStatus);
    }

    private UploadResponse analyse(String url) {
        if(StringUtils.isEmpty(url)){
            return new UploadResponse(UploadStatus.fail.getStatus());
        }
        return new UploadResponse(UploadStatus.success.getStatus(), url);
    }
}

