package cn.vfinance.open.saas.web.enums;

public enum ChannelCode {

	// 入款渠道
	WXAPPPAY("WXAPPPAY","APP支付"),
	WXPAY("WXPAY","微信支付"),
	ALIPAY("ALIPAY","支付宝"),
    ALIAPPPAY("ALIAPPPAY","支付宝APP支付"),
	UNIONPAY("UNIONPAY","中国银联");

    private String code;
    private String message;

    ChannelCode(String code, String message){
        this.code = code;  
        this.message = message;  
    }  
      
    public String getCode() {  
        return code;  
    }  

    public String getMessage() {  
        return message;  
    }  
	 

}
