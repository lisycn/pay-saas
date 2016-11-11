package cn.vfinance.open.saas.web.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("${spring.external.config}")
@ConfigurationProperties
public class ExternalConfig {
	
	@Value("${RAND_CODE_LENGTH}")
	private String randCodeLength;
	
	@Value("${RAND_CODE_TYPE}")
	private String randCodeType;
	
	@Value("${mail.smtp.host}")
	private String mailHost;
	
	@Value("${mail.store.protocol}")
	private String mailProtocol;
	
	@Value("${mail.smtp.port}")
	private String mailPort;
	
	@Value("${mail.smtp.auth}")
	private String mailAuth;
	
	@Value("${mail.smtp.from}")
	private String mailFrom;

	@Value("${mail.send.url}")
	private String mailUrl;

	/*子账户激活路径*/
	@Value("${child.activate.url}")
	private String childActiveUrl;

	@Value("${mail.img.url}")
	private String mailImgUrl;
	
	@Value("${saas.web.mail.pwd}")
	private String mailPwd;

	@Value("${url.gateway.root}")
	private String gatewayRootUrl;


	@Value("${cert.store.url}")
	private String certStoreUrl;

	@Value("${public.key}")
	private String publicKey;

	@Value("${app.download.path}")
	private String appPath;

	@Value("${ufs.user}")
	private String ufsUser;

	@Value("${ufs.password}")
	private String ufsPwd;

//	@Value("${ufs.host}")
//	private String ufsHost;

	@Value("${ufs.server.fullPath}")
	private String ufsServerFullPath;

	@Value("${ufs.enable}")
	private String ufsEnable;

	public String getRandCodeLength() {
		return randCodeLength;
	}

	public void setRandCodeLength(String randCodeLength) {
		this.randCodeLength = randCodeLength;
	}

	public String getRandCodeType() {
		return randCodeType;
	}

	public void setRandCodeType(String randCodeType) {
		this.randCodeType = randCodeType;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailProtocol() {
		return mailProtocol;
	}

	public void setMailProtocol(String mailProtocol) {
		this.mailProtocol = mailProtocol;
	}

	public String getMailPort() {
		return mailPort;
	}

	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}

	public String getMailAuth() {
		return mailAuth;
	}

	public void setMailAuth(String mailAuth) {
		this.mailAuth = mailAuth;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String getMailUrl() {
		return mailUrl;
	}

	public void setMailUrl(String mailUrl) {
		this.mailUrl = mailUrl;
	}

	public String getChildActiveUrl() {
		return childActiveUrl;
	}

	public String getMailImgUrl() {
		return mailImgUrl;
	}

	public void setMailImgUrl(String mailImgUrl) {
		this.mailImgUrl = mailImgUrl;
	}

	public String getMailPwd() {
		return mailPwd;
	}

	public void setMailPwd(String mailPwd) {
		this.mailPwd = mailPwd;
	}

	public String getGatewayRootUrl() {
		return gatewayRootUrl;
	}

	public String getCertStoreUrl() {
		return certStoreUrl;
	}

	public void setCertStoreUrl(String certStoreUrl) {
		this.certStoreUrl = certStoreUrl;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getAppPath() {
		return appPath;
	}

	public String getUfsUser() {
		return ufsUser;
	}

	public String getUfsPwd() {
		return ufsPwd;
	}

//	public String getUfsHost() {
//		return ufsHost;
//	}

	public String getUfsServerFullPath() {
		return ufsServerFullPath;
	}

	public String getUfsEnable() {
		return ufsEnable;
	}
}
