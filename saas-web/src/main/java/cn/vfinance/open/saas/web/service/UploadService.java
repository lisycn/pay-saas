package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.response.FileUplodResult;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import com.netfinworks.common.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by qiuwei on 2016/9/30.
 */
@Service
public class UploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
    @Autowired
    private ExternalConfig externalConfig;
    @Autowired
    private FileClientService fileClientService;

    public String handleFileUpload(MultipartFile file,String appKey,String channelCode){
        if(!file.isEmpty()){
            LOGGER.info("LOGGER_QW_10360: to handle file upload . filename:{}. appKey : {}. channelCode :{}. ",file.getOriginalFilename(),appKey,channelCode);
            boolean ufsEnable = Boolean.parseBoolean(StringUtil.defaultIfBlank(externalConfig.getUfsEnable(), "false"));
            LOGGER.info("externalConfig.getUfsEnable() = {}, ufsEnable = {}", externalConfig.getUfsEnable(), ufsEnable);
            return ufsEnable ? uploadWithUFS(file, appKey,channelCode) : uploadWithoutUFS(file, appKey,channelCode);
        }
        return null;
    }

    private String uploadWithUFS(MultipartFile file,String appKey,String channelCode) {
        try {
            String slash = "";
            if (!externalConfig.getCertStoreUrl().endsWith("/"))
                slash = "/";
            String filePath = externalConfig.getCertStoreUrl() + slash + appKey + "/" + channelCode + "/"+ file.getOriginalFilename();
            FileUplodResult result = fileClientService.uploadUFSFile(file.getBytes(), filePath);
            LOGGER.info("result = " + result);
            return result.getUrl();
        } catch (Exception e) {
            LOGGER.error("LOGGER_QW_10380: to handle file upload exception : {}. ", e);
            return null;
        }
    }

    private String uploadWithoutUFS(MultipartFile file,String appKey,String channelCode) {
        try {
            StringBuilder baseUrl = new StringBuilder(externalConfig.getCertStoreUrl()).append("/").append(appKey).append("/").append(channelCode);
            File existsPath = new File(baseUrl.toString());
            if(!existsPath.exists()){
                existsPath.mkdirs();
            }
            String filePath = baseUrl.append("/").append(file.getOriginalFilename()).toString();
            File existsFile = new File(filePath);
            if (existsFile.isDirectory() || !existsFile.exists()) {
                existsFile.createNewFile();
            }
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(existsFile));
            out.write(file.getBytes());
            out.flush();
            out.close();
            return existsFile.toString();
        }  catch (Exception e) {
            LOGGER.error("LOGGER_QW_10380: to handle file upload exception : {}. ", e);
            return null;
        }
    }
}
