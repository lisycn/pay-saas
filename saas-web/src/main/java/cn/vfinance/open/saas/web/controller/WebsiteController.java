package cn.vfinance.open.saas.web.controller;

import cn.vfinance.open.saas.web.util.ConstantClassField;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.service.FileClientService;
import cn.vfinance.open.saas.web.service.LoginService;

import com.netfinworks.common.lang.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sean Weng
 */
@Controller
@RequestMapping("/website")
public class WebsiteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsiteController.class);

    private static final String prefix = "views/website/";

    @Autowired
    private FileClientService fileClientService;

    @Autowired
    private ExternalConfig externalConfig;
    
	@Autowired
	private LoginService loginService;

    @RequestMapping("/")
    public String toHome() {
        return toIndex();
    }

    @RequestMapping("index")
    public String toIndex() {
        return prefix + "Index";
    }

    @RequestMapping("pay")
    public String toPay() {
        return prefix + "Pay";
    }

    @RequestMapping("payPc1")
    public String toPayPc1() {
        return prefix + "Pay_pc1";
    }

    @RequestMapping("payPc2")
    public String toPayPc2() {
        return prefix + "Pay_pc2";
    }

    @RequestMapping("apitool")
    public String toApitool() {
        return prefix + "Apitool";
    }

    @RequestMapping("download")
    public String toDownload() {
        return prefix + "Download";
    }

    @RequestMapping("top")
    public String toTop() {
        return prefix + "top";
    }

    @RequestMapping("footer")
    public String toFooter() {
        return prefix + "footer";
    }

    @RequestMapping("getGatewayRootUrl")
    @ResponseBody
    public Map<String, String> getUrl() {
        Map<String, String> map = new HashMap<>(1);
        map.put("url", externalConfig.getGatewayRootUrl());
        return map;
    }

    @RequestMapping("header")
    public String toHeader() {
        return prefix + "header";
    }

    @RequestMapping("downloadApp")
    public void toDownloadApp(String fileType, HttpServletResponse res) {

        String fullName;
        String path = externalConfig.getAppPath();
        String fileName = "";
        switch (fileType) {
            case "android_demo":
                fileName = "demo.apk";
                path += "android/";
                break;
            case "android_sdk":
                fileName = "vfinance-sdk-1.0.jar";
                path += "android/";
                break;
        }
        fullName = path + fileName;
        InputStream in;
        try {
            boolean ufsEnable = Boolean.parseBoolean(StringUtil.defaultIfBlank(externalConfig.getUfsEnable(), "false"));
            if (ufsEnable) {
                in = fileClientService.readFileToStream(fullName);
            } else {
                File file = new File(fullName);
                if (!file.exists()) {
                    LOGGER.info("file does not exist, file = {}", fullName);
                    return;
                }
                in = new FileInputStream(fullName);
            }
            res.setHeader("content-disposition", "attachment;filename=" + fileName);
            OutputStream out = res.getOutputStream();
            byte buffer[] = new byte[1024];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
    
	/**
	 * ucloud 登录接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("uc_login")
	public ModelAndView ucloudIndex( HttpServletRequest request, HttpServletResponse response ){
		
		/* 由于ucloud会post提交我们的首页 */
	    try {
	    	
	    	LOGGER.info("请求的输入参参数："+request);
	    	MerchantInfo merInfo = loginService.uCloudLogin(request, response);
	    	
	    	if( merInfo != null ){
	    		// 将用户信息保存在session中
	    		LOGGER.info("用户的信息已经获得，并保存在session跳转页面。");
				request.getSession().setAttribute(ConstantClassField.SESSION_KEY_CODE, merInfo);
	    	}
	    	
		} catch (Exception e) {
			LOGGER.info("获得用户信息失败："+e.getMessage());
		}
	    
		return new ModelAndView("views/website/Index");
	}

}
