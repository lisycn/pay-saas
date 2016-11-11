package cn.vfinance.open.saas.web.util;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

public class PasswordEncoder {
	
	 private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
		   "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		  
	 private static Object salt;
	 private static String algorithm;
	  
	 public PasswordEncoder(Object salt, String algorithm) {
	  PasswordEncoder.salt = salt;
	  PasswordEncoder.algorithm = algorithm;
	 }
		 
	 public static String encode(String rawPass) {
	  String result = null;
	  try {
	   MessageDigest md = MessageDigest.getInstance(algorithm);
	   //加密后的字符串 
	   result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass).getBytes("utf-8")));
	  } catch (Exception ex) {
	  }
	  return result;
	 }
		  
     public static boolean isPasswordValid(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass);
 
        return pass1.equals(pass2);
     }
		     
     public static String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }
 
        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
     }
		 
	 /**
	  * 转换字节数组为16进制字串
	  * @param b 字节数组
	  * @return 16进制字串
	  */
	 public static String byteArrayToHexString(byte[] b) {
	  StringBuffer resultSb = new StringBuffer();
	  for (int i = 0; i < b.length; i++) {
	   resultSb.append(byteToHexString(b[i]));
	  }
	  return resultSb.toString();
	 }
	 
	 private static String byteToHexString(byte b) {
	  int n = b;
	  if (n < 0)
	   n = 256 + n;
	  int d1 = n / 16;
	  int d2 = n % 16;
	  return hexDigits[d1] + hexDigits[d2];
	 }
	 
	 /**
	  * 产生四位随机数
	  * @return
	  */
	 public static String random(){
		 Random ran=new Random();
		 int r=0;
		 m1:while(true){
		     int n=ran.nextInt(10000);
		     r=n;
		     int[] bs=new int[4];
		     for(int i=0;i<bs.length;i++){
		         bs[i]=n%10;
		         n/=10;
		     }
		     Arrays.sort(bs);
		     for(int i=1;i<bs.length;i++){
		         if(bs[i-1]==bs[i]){
		             continue m1;
		         }
		     }
		     break;
		 }
		 return r+"";
	 }
	 
		     
    public static void main(String[] args) {
     
     String salt = random();
     System.out.println("salt"+salt);
     PasswordEncoder encoderMd5 = new PasswordEncoder("5813", "MD5");
     /*String encode = encoderMd5.encode("test");
     System.out.println("加密后密码："+encode);*/
     
     boolean passwordValid = encoderMd5.isPasswordValid("4d4ec9a16372e317111bd20830a14353", "test");
     System.out.println(passwordValid);
 
    }

}
