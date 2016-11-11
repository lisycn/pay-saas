package cn.vfinance.open.saas.web.controller;

import cn.vfinance.open.saas.web.enums.MerchantUsingStatus;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.service.LoginService;
import cn.vfinance.open.saas.web.util.ConstantClassField;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class LoginController extends HttpServlet{

	private static final long serialVersionUID = -5543576463350923303L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private LoginService loginService;
	 
	/**
	 * 初始化登录界面
	 * @return
	 */
	@RequestMapping("logon_index")
	public ModelAndView logonIndex( HttpServletRequest request, HttpServletResponse response ){
		String loginNum = (String)request.getSession().getAttribute(ConstantClassField.LOGIN_NUM);
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("loginNum", loginNum);
		return new ModelAndView("/views/login/login", map);
	}
	
	/**
	 * 登录验证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("logon_from")
	public String logonFrom( HttpServletRequest request, HttpServletResponse response){
		
		Map<String, Object> rsMap = null;
		String loginNum = (String)request.getSession().getAttribute(ConstantClassField.LOGIN_NUM);

		try {
			
			request.setCharacterEncoding("utf-8");  //这里不设置编码会有乱码
	        response.setContentType("text/html;charset=utf-8");
	        response.setHeader("Cache-Control", "no-cache");  
	        
			/* 获得用户账号，以及密码*/
			String email = request.getParameter("email").trim();
			String pwd = request.getParameter("pwd").trim();
			String validationCode = request.getParameter("validationCode");
			
			if( loginNum == null ){
				request.getSession().setAttribute(ConstantClassField.LOGIN_NUM, "1");
				loginNum = "1";
			}else{
				loginNum = String.valueOf(Integer.parseInt(loginNum)+1);
				request.getSession().removeAttribute(ConstantClassField.LOGIN_NUM);
				request.getSession().setAttribute(ConstantClassField.LOGIN_NUM, loginNum);
			}
			
			/* 后台校验 */
			loginService.checkInfo(email, pwd, validationCode, request);
			
			/* 根据客户邮箱地址获得用户的密码以及盐 */
			MerchantInfo merchantInfo = loginService.login(email);
			if (merchantInfo == null) {
				throw new Exception("用户不存在，请联系管理员!");
			}

			if( merchantInfo.getLoginNum() >= 5 ){
				throw new Exception("登录密码错误大于五次，账户锁定请联系管理员。");
			}
			
			if( !"1".equals(merchantInfo.getStatus()) ){
				throw new Exception("用户尚未激活，请去邮箱激活。");
			}
			if(!MerchantUsingStatus.USING.getStatus().equals(merchantInfo.getUsingStatus())){
				throw new Exception("用户未启用，请通知管理员启用。");
			}
		
			if(loginService.checkLogon(email,pwd,merchantInfo)){
				
				// 判断用户登录失败是否超过5次
				if( merchantInfo.getLoginNum() >=  5 ){
					rsMap = new HashMap<String, Object>();
					rsMap.put("returnCode", "FAIL");
					rsMap.put("returnMsg", "账户已经锁定，请联系管理员!");
					rsMap.put("loginNum", loginNum);
					return JSON.toJSONString(rsMap);
				}
				
				// 登录成功，保存session。
				request.getSession().setAttribute(ConstantClassField.SESSION_KEY_CODE, merchantInfo);
				rsMap = new HashMap<String, Object>();
				rsMap.put("returnCode", "SUCCESS");
				rsMap.put("returnMsg", "登录成功!");
				rsMap.put("loginNum", loginNum);
				rsMap.put("merchant", merchantInfo);
				return JSON.toJSONString(rsMap);
			}else{
				// 记录登录错误的次数
				loginService.checkLoginNum(merchantInfo);
				
				// 登录失败，跳回登录页面
				rsMap = new HashMap<String, Object>();
				rsMap.put("returnCode", "FAIL");
				rsMap.put("returnMsg", "用户名或密码错误!");
				rsMap.put("loginNum", loginNum);
				return JSON.toJSONString(rsMap);
			}
			
		} catch (Exception e) {
			LOGGER.info("登录发生异常：{}",e.getMessage());
			rsMap = new HashMap<String, Object>();
			rsMap.put("returnCode", "FAIL");
			rsMap.put("returnMsg", e.getMessage());
			rsMap.put("loginNum", loginNum);
			return JSON.toJSONString(rsMap);
		}
		
	}
	
	/**
	 * 判断用户session是存在
	 * @return
	 */
	@RequestMapping("get_user_session")
	public String checkSession(HttpSession session){
		MerchantInfo merchantInfo = (MerchantInfo)session.getAttribute(ConstantClassField.SESSION_KEY_CODE);
		if( merchantInfo != null){
			return JSON.toJSONString(merchantInfo);
		}else{
			return "null";
		}
	}
	
	/**
	 * 登出
	 * @param session
	 * @return
	 */
	@RequestMapping("logout")
	public String loginOut(HttpSession session){
		
		String rs = "true";
		
		try {
			session.invalidate();
		} catch (Exception e) {
			LOGGER.info("登出异常"+e.getMessage());
			rs = "false";
		}
		
		return rs;
		
	}
	
}
