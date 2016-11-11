package cn.vfinance.open.saas.web.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import cn.vfinance.open.saas.web.model.MerchantInfo;

@Service
public interface LoginService {
	
	/**
	 * 获得商户基本信息
	 * @param name
	 * @param pwd
	 * @return
	 */
	public MerchantInfo login( String email )throws Exception;
	
	
	/**
	 * 身份验证
	 * @param email
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public boolean checkLogon( String email, String pwd, MerchantInfo info ) throws Exception;
	
	
	/**
	 * 检查登录密码
	 * @param email
	 * @throws Exception
	 */
	public void checkLoginNum( MerchantInfo merchantInfo ) throws Exception;
	
	
	/**
	 * 解锁
	 * @param merchantInfo
	 * @throws Exception
	 */
	public void cleanLock( MerchantInfo merchantInfo ) throws Exception;
	
	/**
	 * 输入参数校验
	 * @return
	 */
	public void checkInfo( String email, String pwd, String validationCode, HttpServletRequest request ) throws Exception;
	
	/**
	 * ucloud登录
	 * @param servletRequest
	 * @param servletResponse
	 * @return
	 * @throws Exception
	 */
	public MerchantInfo uCloudLogin( HttpServletRequest servletRequest, HttpServletResponse servletResponse ) throws Exception;
	
}
