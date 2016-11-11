package cn.vfinance.open.saas.web.util;

import cn.vfinance.open.tool.util.StringUtils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HttpsUtil {

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * post方式请求服务器(https协议)
     */
    public static byte[] post(String url, byte[] content, String ctype)
            throws Exception {

        SSLContext sc = SSLContext.getInstance("SSL", "SunJSSE");
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());

        URL console = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
        conn.setSSLSocketFactory(sc.getSocketFactory());
        //设置默认证书--如果没有这句将会报错,错误信息↓↓↓
        //java.io.IOException: HTTPS hostname wrong:  should be <127.0.0.1>
        conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", ctype);
        conn.setRequestProperty("User-Agent", "aop-sdk-java");
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.connect();
        DataOutputStream out = new DataOutputStream(conn.getOutputStream());
        out.write(content);
        // 刷新、关闭
        out.flush();
        out.close();
        InputStream is = conn.getInputStream();
        if (is != null) {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            is.close();
            return outStream.toByteArray();
        }
        return null;
    }

    public static String doPost(String url, Map<String, String> params)
            throws Exception {
        String charset = "utf-8";
        String ctype = "application/x-www-form-urlencoded;charset=" + charset;
        String query = buildQuery(params, charset);
        byte[] content = new byte[0];
        if (query != null) {
            content = query.getBytes(charset);
        }
        byte[] res = post(url, content, ctype);
        if (res == null) return "";
        return new String(res);
    }

    public static String buildQuery(Map<String, String> params, String charset) throws IOException {
        if (params != null && !params.isEmpty()) {
            StringBuilder query = new StringBuilder();
            Set entries = params.entrySet();
            boolean hasParam = false;
            Iterator i$ = entries.iterator();

            while (i$.hasNext()) {
                Map.Entry entry = (Map.Entry) i$.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (StringUtils.areNotEmpty(new String[]{name, value})) {
                    if (hasParam) {
                        query.append("&");
                    } else {
                        hasParam = true;
                    }
                    query.append(name).append("=").append(URLEncoder.encode(value, charset));
                }
            }
            return query.toString();
        } else {
            return null;
        }
    }

}
