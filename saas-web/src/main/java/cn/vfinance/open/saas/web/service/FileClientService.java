package cn.vfinance.open.saas.web.service;

import cn.vfinance.open.saas.web.response.FileUplodResult;
import cn.vfinance.open.saas.web.util.ExternalConfig;
import com.netfinworks.ufs.client.UFSClient;
import com.netfinworks.ufs.client.ctx.FileContext;
import com.netfinworks.ufs.client.ctx.InputStreamFileContext;
import com.netfinworks.ufs.client.ctx.OutputStreamFileContext;
import com.netfinworks.ufs.client.domain.FileNameInfo;
import com.netfinworks.ufs.client.exception.CallFailException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UFS文件处理类
 *
 * @author Sean Weng
 */
@Service
public class FileClientService {

    private static Logger logger = LoggerFactory.getLogger(FileClientService.class);

    @Autowired
    private ExternalConfig externalConfig;

    @Autowired
    private UFSClient ufsClient;

    public FileClientService() {
    }

    private UFSClient getUfsClientInstance() {
        logger.info("ufs client is " + ufsClient);
        if (ufsClient == null) {
            ufsClient = new UFSClient();
//            ufsClient.setHost(externalConfig.getUfsHost());
            ufsClient.setUser(externalConfig.getUfsUser());
            ufsClient.setPassword(externalConfig.getUfsPwd());
            ufsClient.setServerFullPath(externalConfig.getUfsServerFullPath());
        }
        return ufsClient;
    }

    public FileClientService(UFSClient ufsClient) {
        this.ufsClient = ufsClient;
    }

    /**
     * 上传本地文件到 UFS
     *
     * @param localFile      本地文件的完整路径（含文件名）
     * @param remoteFilePath 远程路径（例：/opt/config/config.properties）
     */
    public FileUplodResult uploadUFSFile(String localFile, String remoteFilePath) {

        FileUplodResult result;
        try {
            InputStream in = new FileInputStream(new File(localFile));
            String fileName = localFile.substring(localFile.lastIndexOf("/") + 1, localFile.length());
            logger.info("localFile = {}, remoteFilePath = {}, fileName = {}", localFile, remoteFilePath, fileName);
            result = uploadUFSFile(remoteFilePath, in.available(), in, fileName);
        } catch (Exception e) {
            logger.error("文件写异常", e.getMessage());
            result = new FileUplodResult(false, e.getMessage());
        }
        return result;
    }

    /**
     * 上传本地文件到 UFS
     *
     * @param buf            本地文件的内容 byte
     * @param remoteFilePath 远程路径（例：/opt/config/config.properties）
     */
    public FileUplodResult uploadUFSFile(byte[] buf, String remoteFilePath) {

        FileUplodResult result;
        try {
            String path = remoteFilePath.substring(0, remoteFilePath.lastIndexOf("/"));
            String fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1, remoteFilePath.length());
            logger.info("path = {}, fileName = {}", path, fileName);
            FileContext ctx = new FileContext(path, fileName);
            ufsClient.removeFile(ctx);
            ufsClient.mkdir(path);
            InputStream is = new ByteArrayInputStream(buf);
            InputStreamFileContext context = new InputStreamFileContext(remoteFilePath, is, buf.length);
            result = new FileUplodResult(ufsClient.putFile(context), remoteFilePath, context.getDownloadUrl());
        } catch (Exception e) {
            logger.error("文件写异常", e.getMessage());
            result = new FileUplodResult(false, e.getMessage());
        }
        return result;
    }

    private FileUplodResult uploadUFSFile(String path, long length, InputStream in, String fileName) {

        ufsClient = getUfsClientInstance();
        FileUplodResult result;
        try {
            FileContext ctx = new FileContext(path, fileName);
            ufsClient.removeFile(ctx);
            ufsClient.mkdir(path);
            InputStreamFileContext context = new InputStreamFileContext(path, fileName, in, length);
            if (ufsClient.putFile(context)) {
                result = new FileUplodResult(true, context.getDownloadUrl(), fileName);
            } else {
                result = new FileUplodResult(false, ufsClient.getLastErrorMessage());
            }
        } catch (Exception e) {
            logger.error("文件写异常", e);
            result = new FileUplodResult(false, e.getMessage());
        }
        return result;
    }

    private String downLoadFile(String remoteFilePath, String remoteFileName, OutputStream output) {

        ufsClient = getUfsClientInstance();
        OutputStreamFileContext ctx = new OutputStreamFileContext(remoteFilePath, remoteFileName, output);
        try {
            if (!ufsClient.getFile(ctx))
                return ufsClient.getLastErrorMessage();
        } catch (Exception e) {
            logger.error("文件读异常", e);
        }
        return null;
    }

    /**
     * @param remoteFilePath 远程文件路径（不是完整路径）
     * @param remoteFileName 远程文件名
     * @param localFile      完整的本地文件路径（含文件名）
     */
    public String downLoadFile(String remoteFilePath, String remoteFileName, String localFile) {

        try {
            OutputStream output = new FileOutputStream(new File(localFile));
            return downLoadFile(remoteFilePath, remoteFileName, output);
        } catch (Exception e) {
            logger.error("文件写异常", e);
        }
        return null;
    }

    private String getPrefixUrl() {
        ufsClient = getUfsClientInstance();
        String url = ufsClient.getServerFullPath() + "/download/" + ufsClient.getUser();
        logger.info("getPrefixUrl = " + url);
        return url;
    }

    public InputStream readFileToStream(String remoteFile) {
        return readRemoteFileToStream(getPrefixUrl() + remoteFile);
    }

    public String readFileToString(String remoteFile) {
        InputStream inStream = readFileToStream(remoteFile);
        String str = new String(readStream(inStream));
        logger.info("str = " + str);
        return str;
    }

    public InputStream readRemoteFileToStream(String remoteFile) {
        logger.info("remote file url = " + remoteFile);
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = new HttpGet(remoteFile);
        String authStr = ufsClient.getUser() + ":" + ufsClient.getPassword();
        request.addHeader("authorization", "Basic " + Base64.encodeBase64String(authStr.getBytes()));
        try {
            HttpResponse resp = client.execute(request);
            StatusLine statusline = resp.getStatusLine();
            if (statusline.getStatusCode() < 300) {
                return resp.getEntity().getContent(); //获取输入流读取文件内容
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    public String readRemoteFileToString(String remoteFile) {
        InputStream inStream = readRemoteFileToStream(remoteFile);
        String str = new String(readStream(inStream));
        logger.info("str = " + str);
        return str;
    }

    private byte[] readStream(InputStream inStream) {
        if (inStream == null) return new byte[1];
        try {
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }
            outSteam.close();
            inStream.close();
            return outSteam.toByteArray();
        } catch (Exception e) {
            return new byte[1];
        }
    }

    public List<FileNameInfo> listFiles(String remoteFilePath) {
    	try {
			return ufsClient.list(remoteFilePath);
		} catch (CallFailException e) {
			return new ArrayList<>();
		}
    }
    
    public static void main(String[] str) throws Exception {

        UFSClient ufsClient = new UFSClient();
        ufsClient.setUser("hdfstest");
        ufsClient.setPassword("111111");
//        ufsClient.setHost("10.65.215.11:8090");
//        ufsClient.setServerFullPath("http://func48intra.vfinance.cn/ufs-webdav-server");
//        ufsClient.setHost("10.10.87.119:8090");
        ufsClient.setServerFullPath("http://prod.vfintra.cn/ufs");

        String remoteFileName = "xx.png";
        String remoteFilePath = "/opt/pay/qrcode";
        String localUploadFile = "/opt/pay/qrcode/xx.png";

        String remoteFullFile = "http://baseintra.vfinance.cn/ufs/download/hdfstest/saas/file/test.txt";
        String downloadFile = "/Users/sean/Downloads/test2.txt";
        String downloadFile2 = "/Users/sean/Downloads/test3.txt";

//        if (true) {
//        	List<FileNameInfo> list = ufsClient.list(remoteFilePath);
//        	for (FileNameInfo file : list) {
//        		System.out.println(file.getName());
//        	}
//            new FileClientService(ufsClient).readRemoteFileToString("http://func48intra.vfinance.cn/ufs-webdav-server/download/hdfstest/saas/file/test.txt");
//
////            byte[] buf = "这是个测试文件。\r\nthis is a test file".getBytes("UTF-8");
////            new FileClientService(ufsClient).uploadUFSFile(buf, "/opt/pay/config/yy.txt");
//
////            FileUplodResult fileUplodResult = new FileClientService(ufsClient).uploadUFSFile(localUploadFile, "/opt/pay/mobile/android");
////            logger.info("fileUplodResult = {}", fileUplodResult);
//            return;
//        }

        FileClientService fileClient = new FileClientService(ufsClient);
        // testing upload
        InputStream in = new FileInputStream(new File(localUploadFile));

        FileUplodResult uplodResult = fileClient.uploadUFSFile(remoteFilePath, in.available(), in, remoteFileName);
        logger.info("uplodResult = " + uplodResult);

        // testing download
//        OutputStream output = new FileOutputStream(new File(downloadFile));
//        fileClient.downLoadFile(remoteFilePath, remoteFileName, output);
//        fileClient.downLoadFile(remoteFullFile, downloadFile2);
    }
}
