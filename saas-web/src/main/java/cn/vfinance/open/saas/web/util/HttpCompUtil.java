package cn.vfinance.open.saas.web.util;

import cn.vfinance.open.tool.util.WebUtils;

import java.util.Map;

public class HttpCompUtil {

    public static String doPost(String url, Map<String, String> paramsMap) {
        String response;
        try {
            if (url.startsWith("https")) {
                response = HttpsUtil.doPost(url, paramsMap);
            } else {
                response = WebUtils.doPost(url, paramsMap, 3000, 3000);
            }
        }catch (Exception e){
            response = "";
            e.printStackTrace();
        }
        return response;
    }
}
