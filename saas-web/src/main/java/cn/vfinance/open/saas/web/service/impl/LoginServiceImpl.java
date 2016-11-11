package cn.vfinance.open.saas.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vfinance.open.saas.web.dao.MerchantInfoMapper;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.service.LoginService;
import cn.vfinance.open.saas.web.util.ConstantClassField;
import cn.vfinance.open.saas.web.util.HttpsUtil;
import cn.vfinance.open.saas.web.util.JsonUtil;
import cn.vfinance.open.saas.web.util.PasswordEncoder;
import cn.vfinance.open.saas.web.util.SecureUtil;


@Service(value = "loginService")
public class LoginServiceImpl implements LoginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	MerchantInfoMapper merchantMapper;

	/**
	 * 获得商户基本信息
	 * @param name
	 * @param pwd
	 * @return
	 */
	@Override
	public MerchantInfo login(String email) throws Exception {
		MerchantInfo merchantInfo = new MerchantInfo();
		merchantInfo.setEmail(email);
		List<MerchantInfo> list = merchantMapper.selectBySelective(merchantInfo);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 身份验证
	 *
	 * @param email
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean checkLogon(String email, String pwd, MerchantInfo info) throws Exception {
		
		PasswordEncoder encoderMd5 = new PasswordEncoder(info.getSalt(), "MD5");

		boolean passwordValid = encoderMd5.isPasswordValid(info.getPassword(), pwd);

		return passwordValid;
	}

	/**
	 * 检查用户的登录次数并更新次数
	 */
	@Override
	public void checkLoginNum(MerchantInfo merchantInfo) throws Exception {
		
		// 错误密码次数加1
		merchantInfo.setLoginNum( merchantInfo.getLoginNum()+1 );
	
		merchantMapper.updateByPrimaryKey(merchantInfo);
		
	}

	/**
	 * 解锁
	 */
	@Override
	public void cleanLock(MerchantInfo merchantInfo) throws Exception {
         
		merchantInfo.setLoginNum(0); // 登录成功后密码lock清零
		
		merchantMapper.updateByPrimaryKey(merchantInfo);
		
	}

	/**
	 * 输入参数校验
	 */
	@Override
	public void checkInfo(String email, String pwd, String validationCode, HttpServletRequest request ) throws Exception {
		
		/* 非空判断 */
		if (StringUtils.isBlank(email)) {
			throw new Exception("邮箱不能为空");
		}

		if (StringUtils.isBlank(pwd)) {
			throw new Exception("密码不能为空");
		}
		
		if( StringUtils.isNotBlank(validationCode) ){
			/* 检查验证码 */
			String sesCode = (String)request.getSession().getAttribute(ConstantClassField.SESSION_KEY_OF_RAND_CODE);
			
			if( !validationCode.equalsIgnoreCase(sesCode) ){
				throw new Exception("验证码输入有误");
			}
		}
		
	}

    /***
     * UCloud 登录 
     */
	@Override
	public MerchantInfo uCloudLogin(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse) throws Exception {
		MerchantInfo merInfo = null;
		String sig_token = servletRequest.getParameter(ConstantClassField.UC_ACCESS_TOKEN); // ucloud 传过来的token
		String client_secret = ConstantClassField.UC_SECRET; // ucloud 提供的公钥信息
		
	    // sig_token="NTgzZDZlMjdkY2U5ZTY0N2IzNzE0YmFiZjY1NGJkYTJiMjgyMzA3OA==.MTY2NzMyNDgtZDhlNi00MmU4LTkyYjMtODEwNzMwOGU3NmY1NTAyNA==";
	    // client_secret = "dbd6ab53ba9e4c73a7f0acbd49df2de64dcfca64";
		
		String userInfo = getUcloudUserInfo( sig_token, client_secret );
		LOGGER.info("ucloud接口返回的信息："+userInfo);
		
		Map<String, Object> jsonMap = JsonUtil.parseJSON2Map(userInfo);
		int RetCode= (int) jsonMap.get("RetCode"); // 返回code
		Map<String, Object> arrayMap = (Map<String,Object>)jsonMap.get("DataSet"); 
		String UserEmail = (String)arrayMap.get("UserEmail"); // 邮箱  
		String UserPhone = (String)arrayMap.get("UserPhone"); // 邮箱  
		
		if( 0 == RetCode ){ // 0 表示成功
			
			// 根据邮箱名字判断是否存在。
			merInfo = new MerchantInfo();
			merInfo.setSource("1"); // 1表ucloud 默认是空
			merInfo.setEmail(UserEmail);
			merInfo.setPhone(UserPhone);
			List<MerchantInfo> merList = merchantMapper.selectBySelective(merInfo);
			
			// 如果用户已经存在返回用户信息,用户不存在帮助用户在我方注册，在返回用户信息
			if(  merList != null && merList.size() > 0 ){
				LOGGER.info("ucloud成功返回信息，并已经在saas注册返回用户信息。");
				return merList.get(0);
			}else{ 
				
				String salt = PasswordEncoder.random();
				PasswordEncoder pass = new PasswordEncoder(salt, "MD5");
				
				merInfo = new MerchantInfo();
				merInfo.setAppKey("");
				merInfo.setCreateTime(new Date());
				merInfo.setDescription("");
				merInfo.setEmail(UserEmail);
				merInfo.setPassword(pass.encode(UserEmail)); // 目前登录名和密码为一致的
				merInfo.setPhone(UserPhone);
				merInfo.setSalt(salt);
				merInfo.setStatus("1"); // 默认0  1是有效  0无效 ucloud过来的用户无需激活
				merInfo.setSource("1"); // 1表ucloud 默认是空
				merInfo.setLoginNum(0);
				
				merchantMapper.insert(merInfo);
				LOGGER.info("ucloud成功返回信息，帮助注册返回用户信息。");
				return merInfo;
			}
		}
		LOGGER.info("调用ucloud返回用户信息失败，返回null");
		return null;
	}
	
	/**
	 * 调用ucloud接口返回用户信息
	 * @param token
	 * @param secret
	 * @return
	 * @throws Exception 
	 */
	private String getUcloudUserInfo( String token, String secret ) throws Exception{
		
		LOGGER.info("根据token和私钥获得用户信息--"+token+"--私钥--"+secret);
		// 以“.”拆分UCloud发送的签名后的AccessToken字段，获取签名和被编码的Token
		String [] arr = token.split("\\.");
		// .对签名进行base64解码 对Token进行base64解码
		String ucSign = SecureUtil.decryptBASE64((arr[0]), ConstantClassField.UTF_8);
		String ucToken = SecureUtil.decryptBASE64((arr[1]), ConstantClassField.UTF_8);
		// 比较签名是否正确
		if(verifySign( ucSign, ucToken, secret )){
			
			Map<String,String> requestMap = new HashMap<String, String>();
			requestMap.put("Action", "GetUserInfo");
			requestMap.put("AccessToken", ucToken);
			requestMap.put("Signature", getSignature(secret,requestMap));
			LOGGER.info("调用ucloud我方的请求数据："+requestMap);
			return HttpsUtil.doPost(ConstantClassField.UC_URL, requestMap);
		}
		
		return "";
	}
	
	/**
	 * ucloud 签名验证
	 * @param token
	 * @param sig
	 * @param secret
	 * @return
	 */
	private boolean verifySign( String sig, String token, String secret ) throws Exception{
		
		LOGGER.info("ucloud解码后的token---"+token+"---签名---"+sig+"---私钥---"+secret);
		String decript = SecureUtil.createSignature( token, secret );
        
		if( sig.equals(decript) ){
			LOGGER.info("签名---"+sig+"---签名之后---"+decript+"---验签结果---true");
			return true;
		}
		LOGGER.info("签名---"+sig+"---签名之后---"+decript+"---验签结果---false");
		return false;
	}
	
	/**
	 * 生产签名
	 * @param secret_key
	 * @param params
	 * @return
	 */
	private String getSignature( String secret_key, Map<String,String> params ) throws Exception{
		LOGGER.info("私钥---"+secret_key+"---组装的数据---"+params);
        StringBuffer sb = new StringBuffer();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            if ("Signature".equals(key)) {
                continue;
            }
            sb.append(key);
            sb.append(params.get(key));
        }
        
        return SecureUtil.createSignature( sb.toString(), secret_key );
	}
	
    
	 
}
