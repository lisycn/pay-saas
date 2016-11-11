package cn.vfinance.open.saas.web.util;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class SecureUtil {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(SecureUtil.class);
	
    /** 
    * BASE64解密 
    * 
    * @param key 
    * @return 
    * @throws Exception 
    */ 
    public static String decryptBASE64(String key, String Character){ 
	    byte[] bt;
	    try {
	    bt = (new BASE64Decoder()).decodeBuffer(key);
	    return new String(bt, Character);
	    } catch (IOException e) {
	    e.printStackTrace();
	    return "";
	    } 
    } 
    
    /** 
    * BASE64加密 
    * 
    * @param key 
    * @return 
    * @throws Exception 
    */ 
    public static String encryptBASE64(String key){ 
		byte[] bt = key.getBytes();
		return (new BASE64Encoder()).encodeBuffer(bt); 
    }
    
    /**
     * SHA-1加密
     * @param decript
     * @return
     */
    public static String createSignature(String token, String secret ){
    	
        try {
        	LOGGER.info("创建签名SHA-1---token---"+token+"---私钥---"+secret);
        	String decript = token + secret;
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    	
        return "";
    }
    
}
