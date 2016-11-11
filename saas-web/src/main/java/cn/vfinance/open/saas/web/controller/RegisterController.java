package cn.vfinance.open.saas.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

import cn.vfinance.open.saas.web.service.RegisterService;
import cn.vfinance.open.saas.web.service.VerificationCodeService;

@RestController
@RequestMapping
public class RegisterController extends HttpServlet{

	private static final long serialVersionUID = -812775508313691145L;
	
	private Logger log = Logger.getLogger(RegisterController.class);
	
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private VerificationCodeService verificationCodeService;
	
	/**
	 * 生成验证码
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("create_ver_code")
	public void createVerificationCode(HttpServletRequest request, HttpServletResponse response ){
		try {
			verificationCodeService.generate(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 初始化注册页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("register_index")
	public ModelAndView registerIndex(HttpServletRequest request, HttpServletResponse response ){
		return new ModelAndView("/views/login/register");
	}
	
	/**
	 * 用户注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("register")
    public String register( HttpServletRequest request, HttpServletResponse response ){
		
		Map<String, String> rsMap = null;
		
    	try {
    		
			request.setCharacterEncoding("utf-8");  //这里不设置编码会有乱码
	        response.setContentType("text/html;charset=utf-8");
	        response.setHeader("Cache-Control", "no-cache");  
	        
			/* 获得用户账号，以及密码*/
			String email = request.getParameter("email").trim();
			String pwd = request.getParameter("pwd").trim();
			String rePwd = request.getParameter("repwd").trim();
	    	String validationCode = request.getParameter("validationCode");
	    	
			/* 验证邮箱 */
			registerService.checkEmail(email);
    		
    		/* 后台校验 */
    		registerService.checkInput(pwd, rePwd, email, validationCode, request);
    		
    		/* 处理注册,并发送邮箱 */
    		registerService.processRegister(email, pwd);  
    		
		} catch (Exception e) {
			log.info("注册发生异常："+e.getMessage());
			rsMap = new HashMap<String, String>();
			rsMap.put("returnCode", "FAIL");
			rsMap.put("returnMsg", e.getMessage());
			return JSON.toJSONString(rsMap);
		}
    	
		rsMap = new HashMap<String, String>();
		rsMap.put("returnCode", "SUCCESS");
		rsMap.put("returnMsg", "注册成功!");
		return JSON.toJSONString(rsMap);
    }
	
	/**
	 * 检查邮箱是否已经注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("check_email")
	public String checkEmail( HttpServletRequest request, HttpServletResponse response ){
		
		String bool = "true";
		String email = request.getParameter("email");
		
		try {
			/* 验证邮箱 */
			registerService.checkEmail(email);
		} catch (Exception e) {
			log.info(email+"该邮箱已经被注册");
			bool = "false";
		}
		return bool;
	}
    
	/**
	 * 注册成功页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("regSuccess")
	public ModelAndView regSuccess( HttpServletRequest request, HttpServletResponse response ){
    	// 跳转到成功页面
    	return new ModelAndView("/views/login/regSuccess");
	}
	
    /**
     * 邮箱激活
     * @return
     */
	@RequestMapping("activate")
    public ModelAndView Activate( HttpServletRequest request, HttpServletResponse response ){
    	
    	String email = request.getParameter("email");
    	String validateCode = request.getParameter("validateCode");
    	
    	try {
			
    		/* 账户激活 */
    		String rsStr = registerService.processActivate(email, validateCode);
    		
    		if( "success".equals(rsStr) ){
    	    	return new ModelAndView("/views/login/verifySuccess");
    		}else if ( "already".equals(rsStr) ) {
    	    	return new ModelAndView("/views/login/verifyAlready");
			}else if ( "expire".equals(rsStr) ) {
		    	return new ModelAndView("/views/login/verifyExpire");
			}
    		
		} catch (Exception e) {
			log.info("激活发生异常："+e.getMessage());
		}
    	
    	// 异常默认先跳转到失败页面
    	return new ModelAndView("/views/login/verifyExpire");
    }

}
