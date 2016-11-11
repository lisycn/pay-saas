package cn.vfinance.open.saas.web.controller;

import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.service.CheckService;
import cn.vfinance.open.saas.web.service.MerchantService;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by qiuwei on 2016/11/3.
 */
@RestController
@RequestMapping
public class ChildController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChildController.class);

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CheckService checkService;
    /**
     * 激活子账户
     *
     * @return
     */
    @RequestMapping("active_child_merchant")
    public ModelAndView activeChildMerchant(String email, String id,String createTime, ModelMap modelMap) {
        LOGGER.info("LOGGER_QW_11180: to active child merchant. email={}, id={}. ", email, id);
        MerchantInfo check = checkService.checkValid(id, createTime);
        if(check == null){
            LOGGER.info("LOGGER_QW_11240: to active child merchant. check valid = invalid.");
            return new ModelAndView("/views/child/invalid", modelMap);
        }
        if(!checkService.checkActive(check)){
            LOGGER.info("LOGGER_QW_11250: to active child merchant. check active = activated .");
            return new ModelAndView("/views/login/verifyAlready", modelMap);
        }
        modelMap.addAttribute("email", email);
        modelMap.addAttribute("id", id);
        modelMap.addAttribute("createTime", createTime);
        return new ModelAndView("/views/child/setting", modelMap);
    }

    /**
     * 设置密码 激活子账户状态
     *
     */
    @RequestMapping("active_child_setting")
    public String activeChildSetting(String email, String pwd,String createTime, String repwd,String id) {
        try {
    		/* 后台参数校验 */
            checkService.checkInput(id,pwd, repwd, email,createTime);
            /* 验证邮箱 */
            checkService.checkMerchant(id, email,createTime);
    		/* 处理 */
            merchantService.updateChildSetting(id, pwd);
        } catch (Exception e) {
            LOGGER.info("注册发生异常：" + e.getMessage());
            Map<String, String> rsMap = new HashMap<>();
            rsMap.put("returnCode", "FAIL");
            rsMap.put("returnMsg", e.getMessage());
            return JSON.toJSONString(rsMap);
        }
        Map<String, String> rsMap = new HashMap<>();
        rsMap.put("returnCode", "SUCCESS");
        rsMap.put("returnMsg", "注册成功!");
        return JSON.toJSONString(rsMap);
    }

    /**
     * 激活成功页面
     * @return
     */
    @RequestMapping("active_success")
    public ModelAndView active_success() {
        return  new ModelAndView("views/child/active_success");
    }

    @RequestMapping("to_active")
    public String toActive() {
        return "/views/child/template";
    }
}
