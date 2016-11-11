package cn.vfinance.open.saas.web.service.impl;

import cn.vfinance.open.saas.web.dao.MerchantInfoMapper;
import cn.vfinance.open.saas.web.enums.MerchantUsingStatus;
import cn.vfinance.open.saas.web.model.MerchantInfo;
import cn.vfinance.open.saas.web.service.MerchantService;
import cn.vfinance.open.saas.web.service.RegisterService;
import cn.vfinance.open.saas.web.util.CheckUtils;
import cn.vfinance.open.saas.web.util.ConstantClassField;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import cn.vfinance.open.saas.web.util.PasswordEncoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service(value = "registerService")
public class RegisterServiceImpl implements RegisterService{

	@Autowired
	MerchantInfoMapper merchantInfo;

	@Autowired
	MerchantService merchantService;

	@Autowired
	private ExternalConfig externalConfig;
	
	private Logger log = Logger.getLogger(RegisterServiceImpl.class);
	
	/**
	 * 发送邮箱
	 */
    @Override
	public void sendEmail(String email, String content) throws Exception {
    	
    	// 配置发送邮件的环境属性
        final Properties props = new Properties();

        log.info("邮箱密码------"+externalConfig.getMailPwd());
        
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", externalConfig.getMailAuth());
        props.put("mail.smtp.host", externalConfig.getMailHost());
        // 发件人的账号
        props.put("mail.user", externalConfig.getMailFrom());
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", externalConfig.getMailPwd());

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = externalConfig.getMailFrom(); 
                String password = externalConfig.getMailPwd();
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(externalConfig.getMailFrom());
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(RecipientType.TO, to);

        // 设置抄送
        //InternetAddress cc = new InternetAddress("luo_aaaaa@yeah.net");
        //message.setRecipient(RecipientType.CC, cc);

        // 设置密送，其他的收件人不能看到密送的邮件地址
        //InternetAddress bcc = new InternetAddress("aaaaa@163.com");
        //message.setRecipient(RecipientType.CC, bcc);

        // 设置邮件标题
        message.setSubject("维金云钱包-云支付验证邮件");

        // 设置邮件的内容体
        message.setContent(content, "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
	}

	/**
	 * 激活邮箱
	 */
	@Override
	public String processActivate(String email, String validateCode)throws Exception {
       
		MerchantInfo info = new MerchantInfo();
		info.setEmail(email);
		info.setSalt(validateCode);
		List<MerchantInfo> list = merchantInfo.selectBySelective(info);
		
		if( list == null || list.size() == 0 ){
			//throw new Exception("您好用户尚未注册，请返回注册。");
			return "expire";
		}
		
		info = list.get(0); 
		
		if( "1".equals(info.getStatus()) ){
			//throw new Exception("您好已经激活，请去登录。");
			return "already";
		}
		
		Date currentTime = new Date();//获取当前时间 
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(info.getCreateTime());
		cal.add(Calendar.DATE, 2);
		
		
		/* 未在两天内激活，视为无效 */
		if( !currentTime.before(cal.getTime()) ){
			// throw new Exception("您好验证码已经失效。");
			return "expire";
		}
		
		/* 更新状态去登录  */
		info.setStatus("1");
		info.setUsingStatus(MerchantUsingStatus.USING.getStatus());
       	merchantInfo.updateByPrimaryKey(info);	
		/* 注册成管理员 */
		merchantService.setDefaultAdmin(info.getId());
       	return "success";
	} 

	/**
	 * 注册
	 */
	@Override
	public void processRegister(String email, String pwd) throws Exception{
		
		MerchantInfo info = null;
		String salt = PasswordEncoder.random();
		PasswordEncoder pass = new PasswordEncoder(salt, "MD5");
		
		/* 注册时候检查之前如果用户有未激活的情况删除该记录，重新创建新的用户信息。*/
		info = new MerchantInfo();
		info.setEmail(email);
		info.setStatus("0"); // 未激活
		List<MerchantInfo> list = merchantInfo.selectBySelective(info);
		if( list != null && list.size() > 0 ){
			info = list.get(0);
			merchantInfo.deleteByPrimaryKey(info.getId());
		}
		
		/* 创建新的用户信息 */
		info = new MerchantInfo();
		info.setAppKey("");
		info.setCreateTime(new Date());
		info.setDescription("");
		info.setEmail(email);
		info.setPassword(pass.encode(pwd));
		info.setPhone("");
		info.setSalt(salt);
		info.setStatus("0"); // 默认1是有效 0无效
		info.setLoginNum(0);
		
		merchantInfo.insert(info);
		
        //邮箱内容
		StringBuffer str = new StringBuffer();
		str.append("<!DOCTYPE html><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:sec=\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity3\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        str.append("<title>验证通知</title><style>a{ text-decoration:none;}.content{ width:700px; background:#f0f0f0; margin:50px auto;}.content_top{ width:648px; height:52px; padding:26px; margin:0;}.content_topL{ width:200px; float:left; padding:9px 0 0 0;}");
        str.append(".content_topR{ width:200px; float:right; text-align:right; font-family:\"宋体\"; font-size:12px; padding-top:28px; color:#747474;}.content_topR a{ color:#747474;}.content_topR a:hover{ color:#1f86ef;}.content_con{ width:648px; margin:0 25px; padding:0px; border:1px solid #ecebeb; background:url(img/letter_bottom.png)#fcfcfc bottom center no-repeat;}");
        str.append(".content_conT{ width:648px; height:79px; background:url(img/letter_top.png) top center no-repeat; margin:0; padding:0; text-align:center;}.content_conT span{ font-size:26px; padding-top:13px; letter-spacing:1px; display:block; font-family:\"微软雅黑\"; font-weight:100; color:#1f86ef;}.content_conB{ width:580px; height:auto; padding:27px 34px 45px 34px; font-size:16px; font-family:\"微软雅黑\"; line-height:28px;}");
        str.append(".content_conB p{ padding:1px 0; margin:10px 0px 25px 0px;}.content_conB a{ color:#0066ce;}.content_conB a:hover{ color:#439cf7;}.content_bottom{ width:580px; height:100px; padding:46px 60px 35px 60px;}.content_bottomL{ width:120px; height:100px; float:left;}.content_bottomR{ width:460px; height:auto; float:left; color:#666666;}");
        str.append(".content_bottomR .title{ font-size:20px; font-family:\"微软雅黑\";}.content_bottomR p{ font-size:12px; line-height:18px; padding:5px 0; margin:0px; font-family: Arial,;}.content_bottomR a{ color:#666666;}.content_bottomR a:hover{ color:#1f86ef;}</style>");
        str.append("</head>");
        str.append("<body style=\"background:#fafafa;\"><div class=\"content\"><div class=\"content_top\"><div class=\"content_topL\"><img src=\""+externalConfig.getMailImgUrl()+"/img/logo.png\"></div><div class=\"content_topR\"></div></div><div class=\"content_con\"> <div class=\"content_conT\"><span>验证通知</span></div><div class=\"content_conB\">");
        str.append("<p>尊敬的 "+email+" 您好！</p><p>感谢您使用维金云钱包，请点击以下链接完成验证：<a href=\""+externalConfig.getMailUrl()+email+"&validateCode="+salt+"\">点击激活</a></p>"); // <p>如果以上链接无法直接打开，请复制以下地址至浏览器地址栏：</p><p><a>"+externalConfig.getMailUrl()+email+"&validateCode="+salt+"</a></p>
        str.append("<p>此为系统邮件，请勿直接回复。</p></div></div><div class=\"content_bottom\"><div class=\"content_bottomL\"><img src=\""+externalConfig.getMailImgUrl()+"/img/Qr code.png\"></div><div class=\"content_bottomR\"><span class=\"title\">关于维金</span><p>维金作为互联网金融基础设施供应商，致力于协助有意参与互联网金融业务的企业客户，为其建立支付清结算系统，消费金融系统，供应链金融系统，打造适合新经济的互联网金融服务。<br>网址：www.vfinance.cn    服务热线：400-101-9595     合作洽谈：021-80168700</p>");
        str.append("</div></div></div></body></html>");
		
		sendEmail(email, str.toString());
	}

	/**
	 * 检查邮箱是否已经注册
	 */
	@Override
	public void checkEmail(String email) throws Exception{
		MerchantInfo info = new MerchantInfo();
		info.setEmail(email);
		info.setStatus("1"); // 已经激活的用户
		List<MerchantInfo> list = merchantInfo.selectBySelective(info);
		if( list != null && list.size() > 0 ){
			throw new Exception("该邮箱已经被注册！");
		}
	}

	/**
	 * 输入参数校验
	 */
	@Override
	public void checkInput(String pwd, String rePwd, String email,String validationCode, HttpServletRequest request ) throws Exception {
	
		/* 非空判断 */
		if (StringUtils.isBlank(email)) {
			throw new Exception("邮箱不能为空");
		}

		if (StringUtils.isBlank(pwd)) {
			throw new Exception("密码不能为空");
		}
		
		if(StringUtils.isBlank(rePwd)){
			throw new Exception("确认密码不能为空");
		}
		
		if(StringUtils.isBlank(validationCode)){
			throw new Exception("验证码不能为空");
		}
		
		/* 检查密码和邮箱格式 */
		if(!CheckUtils.checkEmail(email)){
			throw new Exception("邮箱格式不正确");
		}
		
		if(!CheckUtils.checkPassword(pwd)){
			throw new Exception("密码格式不符");
		}
		
		if(!pwd.equals(rePwd)){
			throw new Exception("两次密码不一致");
		}
		
		/* 检查验证码 */
		String sesCode = (String)request.getSession().getAttribute(ConstantClassField.SESSION_KEY_OF_RAND_CODE);
		
		if( !validationCode.equalsIgnoreCase(sesCode) ){
			throw new Exception("验证码输入有误");
		}
	    
	}
	
}
