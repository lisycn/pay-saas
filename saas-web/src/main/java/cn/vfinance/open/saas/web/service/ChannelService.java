package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.analyse.ChannelAnalysor;
import cn.vfinance.open.saas.web.dao.ChannelAliMapper;
import cn.vfinance.open.saas.web.dao.ChannelUnionMapper;
import cn.vfinance.open.saas.web.dao.ChannelWechatMapper;
import cn.vfinance.open.saas.web.enums.ChannelCode;
import cn.vfinance.open.saas.web.enums.ChannelStatus;
import cn.vfinance.open.saas.web.enums.InsertStatus;
import cn.vfinance.open.saas.web.model.ChannelAli;
import cn.vfinance.open.saas.web.model.ChannelUnion;
import cn.vfinance.open.saas.web.model.ChannelWechat;
import cn.vfinance.open.saas.web.response.AppChannelAliResponse;
import cn.vfinance.open.saas.web.response.AppChannelUnionResponse;
import cn.vfinance.open.saas.web.response.AppChannelWechatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by qiuwei on 2016/9/30.
 */
@Service
public class ChannelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelService.class);
    @Autowired
    private ChannelAliMapper channelAliMapper;

    @Autowired
    private ChannelWechatMapper channelWechatMapper;

    @Autowired
    private ChannelUnionMapper channelUnionMapper;

    public AppChannelAliResponse insert(ChannelAli channelAli) {
        try {
            LOGGER.info("LOGGER_QW_10210: to insert channel ali. channelAli={}. ", channelAli);
            ChannelAli isExists = channelAliMapper.selectBySelective(this.buildAli(channelAli.getAppInfoId(),channelAli.getCode()));
            if (isExists != null) {
                LOGGER.info("LOGGER_QW_10230: exits channel ali . exists = {}. ", isExists);
                if (isExists.getStatus().equals(ChannelStatus.WORKED.getStatus())) {//渠道正在使用
                    return ChannelAnalysor.analyseAli(InsertStatus.worked, isExists);
                }
                int update = channelAliMapper.updateByPrimaryKeySelective(this.build(channelAli, isExists.getId()));
                if (update > 0) {
                    LOGGER.info("LOGGER_QW_10590: update channel ali success . ");
                    return ChannelAnalysor.analyseAli(InsertStatus.existed, channelAliMapper.selectByPrimaryKey(isExists.getId()));
                }
                LOGGER.info("LOGGER_QW_10600: update channel ali fail. ");
                return ChannelAnalysor.analyseAli(InsertStatus.fail, channelAliMapper.selectByPrimaryKey(isExists.getId()));
            }
            int result = channelAliMapper.insert(channelAli);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_10240: insert channel ali success . ");
                return ChannelAnalysor.analyseAli(InsertStatus.success,channelAliMapper.selectBySelective(channelAli));
            }
        } catch (Exception e) {
            LOGGER.error("LOGGER_QW_10250: insert channel ali exception : {}. ", e);
            return ChannelAnalysor.analyseAli(InsertStatus.fail);
        }
        LOGGER.info("LOGGER_QW_10260: insert channel ali false .");
        return ChannelAnalysor.analyseAli(InsertStatus.fail);
    }

    public AppChannelWechatResponse insert(ChannelWechat channelWechat) {
        try {
            LOGGER.info("LOGGER_QW_10320: to insert channel wechat. channelWechat = {}. ", channelWechat);
            ChannelWechat isExists = channelWechatMapper.selectBySelective(this.buildWx(channelWechat.getAppInfoId(),channelWechat.getCode()));
            if (isExists != null) {
                LOGGER.info("LOGGER_QW_10330: exits channel wechat. exists = {}. ", isExists);
                if (isExists.getStatus().equals(ChannelStatus.WORKED.getStatus())) {//渠道正在使用
                    return ChannelAnalysor.analyseWechat(InsertStatus.worked, isExists);
                }
                int update = channelWechatMapper.updateByPrimaryKeySelective(this.build(channelWechat, isExists.getId()));
                if (update > 0) {
                    LOGGER.info("LOGGER_QW_10570: update wx success. ");
                    return ChannelAnalysor.analyseWechat(InsertStatus.existed, channelWechatMapper.selectByPrimaryKey(isExists.getId()));
                }
                LOGGER.info("LOGGER_QW_10580: update wx fail. ");
                return ChannelAnalysor.analyseWechat(InsertStatus.fail, channelWechatMapper.selectByPrimaryKey(isExists.getId()));
            }
            int result = channelWechatMapper.insert(channelWechat);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_10390: insert channel wx success .");
                return ChannelAnalysor.analyseWechat(InsertStatus.success,channelWechatMapper.selectBySelective(channelWechat));
            }
        } catch (Exception e) {
            LOGGER.info("LOGGER_QW_10300: insert channel wechat exception : {}. ", e);
            return ChannelAnalysor.analyseWechat(InsertStatus.fail);
        }
        LOGGER.info("LOGGER_QW_10310: insert channel wechat false. ");
        return ChannelAnalysor.analyseWechat(InsertStatus.fail);
    }

    public AppChannelUnionResponse insert(ChannelUnion channelUnion) {
        try {
            LOGGER.info("LOGGER_QW_10420: to insert channel union. channelUnion={}. ", channelUnion);
            ChannelUnion isExists = channelUnionMapper.selectBySelective(this.buildUnion(channelUnion.getAppInfoId(),channelUnion.getCode()));
            if (isExists != null) {
                LOGGER.info("LOGGER_QW_10430: exits channel union . exists = {}. ", isExists);
                if (isExists.getStatus().equals(ChannelStatus.WORKED.getStatus())) {//渠道正在使用
                    return ChannelAnalysor.analyseUnion(InsertStatus.worked, isExists);
                }
                int update = channelUnionMapper.updateByPrimaryKeySelective(this.build(channelUnion, isExists.getId()));
                if (update > 0) {
                    LOGGER.info("LOGGER_QW_10610: update channel union success. ");
                    return ChannelAnalysor.analyseUnion(InsertStatus.existed, channelUnionMapper.selectByPrimaryKey(isExists.getId()));
                }
                LOGGER.info("LOGGER_QW_10620: update channel union fail. ");
                return ChannelAnalysor.analyseUnion(InsertStatus.fail, channelUnionMapper.selectByPrimaryKey(isExists.getId()));
            }
            int result = channelUnionMapper.insert(channelUnion);
            if (result > 0) {
                LOGGER.info("LOGGER_QW_10440: insert channel union success . ");
                return ChannelAnalysor.analyseUnion(InsertStatus.success,channelUnionMapper.selectBySelective(channelUnion));
            }
        } catch (Exception e) {
            LOGGER.info("LOGGER_QW_10450: insert channel union exception : {}. ", e);
            return ChannelAnalysor.analyseUnion(InsertStatus.fail);
        }
        LOGGER.info("LOGGER_QW_10460: insert channel union false .");
        return ChannelAnalysor.analyseUnion(InsertStatus.fail);
    }

    public ChannelAli queryAliScanByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10500: queryAliScanByApp. appInfoId = {}. ", appInfoId);
        ChannelAli channelAli = channelAliMapper.selectBySelective(this.buildAli(appInfoId, ChannelCode.ALIPAY.getCode()));
        LOGGER.info("LOGGER_QW_10540: queryAliScanByApp result={}.", channelAli);
        return channelAli;
    }

    public ChannelAli queryAliAppByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10520: queryAliAppByApp. appInfoId = {}. ", appInfoId);
        ChannelAli channelAli = channelAliMapper.selectBySelective(this.buildAli(appInfoId, ChannelCode.ALIAPPPAY.getCode()));
        LOGGER.info("LOGGER_QW_10550: queryAliAppByApp result={}. ", channelAli);
        return channelAli;
    }

    public ChannelWechat queryWxScanByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10720: queryWxScanByApp. appInfoId = {}. ", appInfoId);
        ChannelWechat channelWechat = channelWechatMapper.selectBySelective(this.buildWx(appInfoId, ChannelCode.WXPAY.getCode()));
        LOGGER.info("LOGGER_QW_10730: queryWxScanByApp result={}. ", channelWechat);
        return channelWechat;
    }

    public ChannelWechat queryWxAppByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10750: queryWxAppByApp. appInfoId = {}. ", appInfoId);
        ChannelWechat channelWechat = channelWechatMapper.selectBySelective(this.buildWx(appInfoId, ChannelCode.WXAPPPAY.getCode()));
        LOGGER.info("LOGGER_QW_10760: queryWxAppByApp result={}. ", channelWechat);
        return channelWechat;
    }

    public ChannelUnion queryUnionByApp(int appInfoId) {
        LOGGER.info("LOGGER_QW_10780: queryUnionByApp. appInfoId = {}. ", appInfoId);
        ChannelUnion channelUnion = channelUnionMapper.selectBySelective(this.buildUnion(appInfoId, ChannelCode.UNIONPAY.getCode()));
        LOGGER.info("LOGGER_QW_10790: queryUnionByApp result={}. ", channelUnion);
        return channelUnion;
    }


    public int updateAliScanByApp(int appInfoId, String openStatus) {
        LOGGER.info("LOGGER_QW_10660: update Ali scan status. appInfoId={}. status={}. ", appInfoId, openStatus);
        int result = channelAliMapper.updateStatusByAppAndCode(this.buildAli(appInfoId, ChannelCode.ALIPAY.getCode(), openStatus));
        LOGGER.info("LOGGER_QW_10640: update Ali scan status result = {}. ",result);
        return result;
    }

    public int updateAliAppByApp(int appInfoId, String openStatus) {
        LOGGER.info("LOGGER_QW_10630: update status. appInfoId={}. status={}. ", appInfoId, openStatus);
        int result = channelAliMapper.updateStatusByAppAndCode(this.buildAli(appInfoId, ChannelCode.ALIAPPPAY.getCode(), openStatus));
        LOGGER.info("LOGGER_QW_10640: updateStatusByAppAndCode result = {}. ",result);
        return result;
    }

    public int updateWxScanByApp(int appInfoId, String openStatus) {
        LOGGER.info("LOGGER_QW_10670: update Wx scan status. appInfoId={}. status={}. ", appInfoId, openStatus);
        int result = channelWechatMapper.updateStatusByAppAndCode(this.buildWx(appInfoId, ChannelCode.WXPAY.getCode(), openStatus));
        LOGGER.info("LOGGER_QW_10680: update Wx scan status result = {}. ",result);
        return result;
    }

    public int updateWxAppByApp(int appInfoId, String openStatus) {
        LOGGER.info("LOGGER_QW_10690: update Wx app status. appInfoId={}. status={}. ", appInfoId, openStatus);
        int result = channelWechatMapper.updateStatusByAppAndCode(this.buildWx(appInfoId, ChannelCode.WXAPPPAY.getCode(), openStatus));
        LOGGER.info("LOGGER_QW_10700: update Wx app status result = {}. ",result);
        return result;
    }

    public int updateUnionByApp(int appInfoId, String openStatus) {
        LOGGER.info("LOGGER_QW_10810: update union status. appInfoId={}. status={}. ", appInfoId, openStatus);
        int result = channelUnionMapper.updateStatusByAppAndCode(this.buildUnion(appInfoId, ChannelCode.UNIONPAY.getCode(), openStatus));
        LOGGER.info("LOGGER_QW_10820: update union status result = {}. ",result);
        return result;
    }


    private ChannelAli buildAli(int appInfoId, String code) {
        ChannelAli channelAli = new ChannelAli();
        channelAli.setCode(code);
        channelAli.setAppInfoId(appInfoId);
        return channelAli;
    }

    private ChannelWechat buildWx(int appInfoId, String code) {
        ChannelWechat channelWechat = new ChannelWechat();
        channelWechat.setCode(code);
        channelWechat.setAppInfoId(appInfoId);
        return channelWechat;
    }

    private ChannelUnion buildUnion(int appInfoId, String code) {
        ChannelUnion channelUnion = new ChannelUnion();
        channelUnion.setCode(code);
        channelUnion.setAppInfoId(appInfoId);
        return channelUnion;
    }

    private ChannelAli build(ChannelAli channelAli, int id) {
        channelAli.setId(id);
        return channelAli;
    }

    private ChannelWechat build(ChannelWechat channelWechat, int id) {
        channelWechat.setId(id);
        return channelWechat;
    }

    private ChannelUnion build(ChannelUnion channelUnion, int id) {
        channelUnion.setId(id);
        return channelUnion;
    }

    private ChannelAli buildAli(int appInfoId, String code, String openStatus) {
        ChannelAli channelAli = new ChannelAli();
        channelAli.setCode(code);
        channelAli.setAppInfoId(appInfoId);
        channelAli.setStatus(openStatus);
        return channelAli;
    }

    private ChannelWechat buildWx(int appInfoId, String code, String openStatus) {
        ChannelWechat channelWechat = new ChannelWechat();
        channelWechat.setCode(code);
        channelWechat.setAppInfoId(appInfoId);
        channelWechat.setStatus(openStatus);
        return channelWechat;
    }

    private ChannelUnion buildUnion(int appInfoId, String code, String openStatus) {
        ChannelUnion channelUnion = new ChannelUnion();
        channelUnion.setCode(code);
        channelUnion.setAppInfoId(appInfoId);
        channelUnion.setStatus(openStatus);
        return channelUnion;
    }

}
