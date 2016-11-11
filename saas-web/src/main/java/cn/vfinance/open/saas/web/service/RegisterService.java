package cn.vfinance.open.saas.web.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public interface RegisterService {

	/**
	 * 发送email
	 */
	public void sendEmail( String email, String content )throws Exception;
	
	/**
	 * 处理激活
	 */
	public String processActivate( String email, String validateCode )throws Exception;
	
	/**
	 * 注册处理
	 */
	public void processRegister( String email, String pwd )throws Exception;
	
	/**
	 * 验证邮箱是否
	 * @param name
	 * @return
	 */
	public void checkEmail( String email )throws Exception;
	
	/**
	 * 输入参数校验
	 * @param name
	 * @param pwd
	 * @param email
	 * @param validateCode
	 * @return
	 * @throws Exception
	 */
	public void checkInput( String pwd, String rePwd, String email, String validationCode, HttpServletRequest request )throws Exception;

}
